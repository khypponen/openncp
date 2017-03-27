package com.gnomon.epsos.model;

import com.gnomon.LiferayUtils;
import com.gnomon.epsos.MyServletContextListener;
import com.gnomon.epsos.service.ConsentException;
import com.gnomon.epsos.service.Demographics;
import com.gnomon.epsos.service.EpsosHelperService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.openncp.protocolterminator.ClientConnectorConsumer;
import epsos.openncp.protocolterminator.HCPIAssertionCreator;
import epsos.openncp.protocolterminator.clientconnector.EpsosDocument1;
import epsos.openncp.protocolterminator.clientconnector.GenericDocumentCode;
import epsos.openncp.protocolterminator.clientconnector.PatientDemographics;
import epsos.openncp.protocolterminator.clientconnector.PatientId;
import eu.epsos.util.IheConstants;
import eu.europa.ec.joinup.ecc.openstork.utils.StorkUtils;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.opensaml.saml2.core.Assertion;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.util.Constants;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.*;

@ManagedBean
@SessionScoped
public class PacRepBean implements Serializable {

    private static final Logger log = LoggerFactory.getLogger("PacRepBean");

    private static final long serialVersionUID = 1L;
    private String selectedCountry;
    private List<Identifier> identifiers;
    private List<Demographics> demographics;
    private List<Patient> patients;
    private Patient selectedPatient;
    private List<PatientDocument> patientDocuments;
    private PatientDocument selectedDocument;
    private boolean showDemographics;
    private boolean showPatientList = true;
    private StreamedContent prescriptionFile;
    private PatientDocument selectedPrescriptionFile;
    private Assertion hcpAssertion = null;
    private boolean trcassertionexists = false;
    private boolean trcassertionnotexists = true;
    private Assertion trcAssertion;
    private String purposeOfUse;
    private String purposeOfUseForPS;
    private String queryPatientsException;
    private String queryDocumentsException;
    private boolean showPS;
    private boolean consentExists;
    private Map<String, String> ltrlanguages = new HashMap<String, String>();
    private Map<String, String> repuser = new HashMap<String, String>();
    private String repUserString;
    private boolean enablePacRep;
    private String ltrlang;

    private String patientgivenname;
    private String patientsurname;
    private String patientaddress;
    private String patientzip;
    private String patientcity;
    private String patientcountry;

    private Assertion onBehalf;
    private Map<String, String> onbehalfidentityattrs;
    private Map<String, String> onbehalfdemographicsattrs;
    private String eIdentifier;
    private String errorUserAssertion;
    private List<tr.com.srdc.epsos.data.model.PatientId> patientIdentifiers;
    private STORKAuthnResponse authnResponse;
    private boolean enableCCD;


    public PacRepBean() {
        log.info("PAC REP BEAN CREATED");
        selectedCountry = ConfigurationManagerService.getInstance().getProperty("ncp.country");
        STORKSAMLEngine engine = STORKSAMLEngine.getInstance("SP");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
        HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(portletRequest);

        String host = httpServletRequest.getRemoteHost();
        log.info("HOST IS : " + host);

        String USER_samlResponseXML = (String) LiferayUtils.getFromSession("USER_samlResponseXML");
        try {
            authnResponse = engine.validateSTORKAuthnResponse(USER_samlResponseXML.getBytes(), host);
            onBehalf = StorkUtils.convertOnBehalfStorktoHcpAssertion(authnResponse);
            hcpAssertion = onBehalf;
            LiferayUtils.storeToSession("hcpAssertion", hcpAssertion);

            EpsosHelperService.printAssertion(onBehalf);
            onbehalfdemographicsattrs = StorkUtils.getRepresentedDemographics(authnResponse);
            patientIdentifiers = StorkUtils.getRepresentedEidentifiers(authnResponse, selectedCountry);
            log.info("PATIENT IDENTIFIERS SIZE: " + patientIdentifiers.size());
            for (int l = 0; l < patientIdentifiers.size(); l++) {
                log.info("#################" + patientIdentifiers.get(l).getRoot() + "-" + patientIdentifiers.get(l).getExtension());
            }

            for (Map.Entry<String, String> entry : onbehalfdemographicsattrs.entrySet()) {
                log.info("#################" + entry.getKey() + "/" + entry.getValue());
            }
            log.info("ASSERTION ID: " + onBehalf.getID());
            log.info("ASSERTION ATTRIBUTES: " + onbehalfdemographicsattrs.size());
            log.info("ASSERTION GIVENNAME :" + onbehalfdemographicsattrs.get("patient.data.givenname"));
            log.info("ASSERTION EIDENTIFIER :" + patientIdentifiers.get(0).getRoot() + " " + patientIdentifiers.get(0).getExtension());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        log.debug("Response is :" + USER_samlResponseXML);

        log.info("Selected Country: " + selectedCountry);
        identifiers = new ArrayList<Identifier>();
        demographics = new ArrayList<Demographics>();
        ltrlanguages = EpsosHelperService.getLTRLanguages();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Country", selectedCountry));
        try {
            getPacRepPerson();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public String getSelectedCountry() {
        return selectedCountry;
    }

    public void displayLocation() {
        FacesMessage msg = new FacesMessage("Selected", "Country:" + ltrlang);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void getPacRepPerson() throws Exception {
        log.info("Pac Representative Module is enabled!");
        getPACRepDocuments();
    }

    public void getPACRepDocuments() throws Exception {
        log.info("HCP Assertion has already be created by Stork Plugin");
        errorUserAssertion = "";
        log.info("HCP Assertion has been created");

        EpsosHelperService.printAssertion(hcpAssertion);

        identifiers = new ArrayList<Identifier>();

        for (int l = 0; l < patientIdentifiers.size(); l++) {
            Identifier id = new Identifier();
            id.setKey(patientIdentifiers.get(l).getRoot());
            String domain = patientIdentifiers.get(l).getRoot();
            id.setDomain(domain);
            String idvalue = patientIdentifiers.get(l).getExtension();
            log.info("Identifiers: " + domain + "_" + idvalue);

            id.setUserValue(idvalue);
            identifiers.add(id);
        }

        demographics = new ArrayList<Demographics>();
        for (Map.Entry<String, String> entry : onbehalfdemographicsattrs.entrySet()) {
            Demographics dd = new Demographics();
            dd.setLabel(entry.getKey());
            dd.setKey(entry.getKey());

            String idvalue = entry.getValue();
            dd.setUserValue(idvalue);

            log.info("Demographics: " + dd.getKey() + "_" + dd.getUserValue());
            demographics.add(dd);
        }

        log.info("Selected Country: " + selectedCountry);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Country", selectedCountry));
        LiferayUtils.storeToSession("selectedCountry", selectedCountry);
        User user = LiferayUtils.getPortalUser();
        log.info("STORING USER TO SESSION " + user.getScreenName());
        LiferayUtils.storeToSession("user", user);
        log.info("Get my patient info (Start)");
        getMyPatientInfo();
        log.info("Get my patient info (End)");
        if (Validator.isNotNull(selectedPatient)) {
            log.info("Create TRCA (Start)");
            createTRCA("ps", "TREATMENT");
            log.info("Create TRCA (End)");
            try {
                log.info("Get PS Docs (Start)");
                getPSDocs();
                log.info("Get PS Docs (End)");
            } catch (ConsentException e) {
                queryDocumentsException = LiferayUtils.getPortalTranslation(e.getMessage());
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

    public boolean getAssertionExists() {
        if (trcassertionexists) {
            return (true);
        } else {
            return (false);
        }
    }

    public boolean getAssertionNotExists() {
        if (!trcassertionexists) {
            return (true);
        } else {
            return (false);
        }
    }

    private void getMyPatientInfo() {
        try {
            trcAssertion = null;
            trcassertionnotexists = true;
            trcassertionexists = false;
            patients = new ArrayList<Patient>();
            String serviceUrl = EpsosHelperService.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);

            PatientDemographics pd = EpsosHelperService.
                    createPatientDemographicsForQuery(identifiers, demographics);

            log.info("Running client connector : " + serviceUrl);
            ClientConnectorConsumer proxy = MyServletContextListener.getClientConnectorConsumer();
            log.info("Test Assertions: "
                    + EpsosHelperService.getConfigProperty(EpsosHelperService.PORTAL_TEST_ASSERTIONS));
            boolean testAssertion = GetterUtil.getBoolean(
                    EpsosHelperService.getConfigProperty(EpsosHelperService.PORTAL_TEST_ASSERTIONS), true);
            Assertion ass = null;
            if (testAssertion) {
                ass = new HCPIAssertionCreator().createHCPIAssertion();
            } else {
                ass = hcpAssertion;
            }
            log.info("Searching for patients in " + selectedCountry);
            log.info("Assertion id: " + ass.getID());
            try {
                log.info("PD : " + pd.getPatientIdArray(0));
            } catch (Exception e) {
                log.error("No patient identifier declared " + e.getMessage());
            }

            List<PatientDemographics> queryPatient = proxy.queryPatient(ass, selectedCountry, pd);
            int i = 0;
            for (PatientDemographics aux : queryPatient) {
                Patient patient = EpsosHelperService.populatePatient(aux);
                log.info("PATIENT FOUND: " + patient.getExtension());
                selectedPatient = patient;
                patientgivenname = patient.getName();
                patientsurname = patient.getFamilyName();
                patientaddress = patient.getAddress();
                patientzip = patient.getPostalCode();
                patientcity = patient.getCity();
                patientcountry = patient.getCountry();

                i++;
                if (i > 1) {
                    selectedPatient = null;
                    patient = null;
                    log.error("More than one patients exist with these criteria. Exiting ...");
                    return;
                }
            }
            queryPatientsException = LiferayUtils.getPortalTranslation("patient.list.no.patient");
            log.info("Found " + i + " patients");
            showPatientList = true;
        } catch (Exception ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
            patients = new ArrayList<Patient>();
            showPatientList = true;
            queryPatientsException = LiferayUtils.getPortalTranslation(ex.getMessage());
        }
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public List<PatientDocument> getPatientDocuments() {
        return patientDocuments;
    }

    public void setPatientDocuments(List<PatientDocument> patientDocuments) {
        this.patientDocuments = patientDocuments;

    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
        LiferayUtils.storeToSession("patient", selectedPatient);
    }

    public PatientDocument getSelectedDocument() {
        return selectedDocument;
    }

    public void setSelectedDocument(PatientDocument selectedDocument) {
        this.selectedDocument = selectedDocument;

    }

    public boolean getDisablePatientDocuments() {
        User user = LiferayUtils.getPortalUser();

        boolean isPhysician = LiferayUtils.isDoctor(user.getUserId(),
                user.getCompanyId());
        boolean isPharmacist = LiferayUtils.isPharmacist(user.getUserId(),
                user.getCompanyId());
        boolean isNurse = LiferayUtils.isNurse(user.getUserId(),
                user.getCompanyId());
        boolean isAdministrator = LiferayUtils.isAdministrator(
                user.getUserId(), user.getCompanyId());

        if (isNurse) {
            return false;
        } else if (isPhysician) {
            return false;
        } else if (isPharmacist) {
            return true;
        } else if (isAdministrator) {
            return false;
        } else {
            return false;
        }
    }

    public boolean isShowDemographics() {
        return showDemographics;
    }

    public void setShowDemographics(boolean showDemographics) {
        this.showDemographics = showDemographics;
    }

    public boolean getShowPatientList() {
        return showPatientList;
    }

    public void setShowPatientList(boolean showPatientList) {
        this.showPatientList = showPatientList;
    }

    public StreamedContent getPrescriptionFile() {
        return prescriptionFile;
    }

    public void setPrescriptionFile(StreamedContent prescriptionFile) {
        this.prescriptionFile = prescriptionFile;
    }

    public PatientDocument getSelectedPrescriptionFile() {
        return selectedPrescriptionFile;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public List<Patient> getPatients() {

        return patients;
    }

    public List<Demographics> getDemographics() {
        return demographics;
    }

    public void setDemographics(List<Demographics> demographics) {
        this.demographics = demographics;
    }

    public Assertion getHcpAssertion() {
        return hcpAssertion;
    }

    public void setHcpAssertion(Assertion hcpAssertion) {
        this.hcpAssertion = hcpAssertion;
    }

    public Assertion getTrcAssertion() {
        return trcAssertion;
    }

    public void setTrcAssertion(Assertion trcAssertion) {
        this.trcAssertion = trcAssertion;
    }

    public String getPurposeOfUse() {
        return purposeOfUse;
    }

    public boolean getShowConfirmation() {
        log.debug("### GET SHOW CONFIRMATION ###");
        log.debug("trcassertionexists: " + trcassertionexists);
        log.debug("consentExists: " + consentExists);
        return !trcassertionexists;
    }

    public boolean getShowPatientSummary() {
        return showPS;
    }

    public void getPSDocs() throws ConsentException {
        consentExists = true;
        trcassertionexists = true;
        PatientId patientId = null;
        try {
            patientDocuments = new ArrayList<PatientDocument>();
            String serviceUrl = EpsosHelperService.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);
            log.info("CLIENTCONNECTOR: " + serviceUrl);
            ClientConnectorConsumer clientConectorConsumer = MyServletContextListener.getClientConnectorConsumer();

            patientId = PatientId.Factory.newInstance();
            patientId.setExtension(selectedPatient.getExtension());
            patientId.setRoot(selectedPatient.getRoot());
            GenericDocumentCode classCode = GenericDocumentCode.Factory
                    .newInstance();
            classCode.setNodeRepresentation(Constants.PS_CLASSCODE);
            classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
            classCode.setValue(Constants.PS_TITLE); // Patient
            // Summary
            // ClassCode.

            log.info("PS QUERY: Getting ps documents for : " + patientId.getExtension() + " from " + selectedCountry);
            List<EpsosDocument1> queryDocuments = clientConectorConsumer
                    .queryDocuments(hcpAssertion, trcAssertion,
                            selectedCountry, patientId, classCode);
            log.info("PS QUERY: Found " + queryDocuments.size() + " for : " + patientId.getExtension() + " from " + selectedCountry);
            showPS = true;
            consentExists = true;
            for (EpsosDocument1 aux : queryDocuments) {
                PatientDocument document = new PatientDocument();
                document.setAuthor(aux.getAuthor());
                Calendar cal = aux.getCreationDate();
                DateFormat sdf = LiferayUtils.getPortalUserDateFormat();
                try {
                    document.setCreationDate(sdf.format(cal.getTime()));
                } catch (Exception e) {
                    //document.setCreationDate(aux.getCreationDate()+"");
                    log.error("Problem converting date" + aux.getCreationDate());
                }
                document.setDescription(aux.getDescription());
                document.setHealthcareFacility("");
                document.setTitle(aux.getTitle());
                document.setFile(aux.getBase64Binary());
                document.setUuid(URLEncoder.encode(aux.getUuid(), "UTF-8"));
                document.setFormatCode(aux.getFormatCode());
                document.setRepositoryId(aux.getRepositoryId());
                document.setHcid(aux.getHcid());
                log.info("#### " + document.getUuid() + "/" + document.getFormatCode());
                patientDocuments.add(document);
            }
            queryDocumentsException = LiferayUtils.getPortalTranslation("report.list.no.document");
        } catch (Exception ex) {
            consentExists = true;
            log.error(ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "DOCUMENT QUERY", ex.getMessage()));
            log.error("PS QUERY: Error getting ps documents for : " + patientId.getExtension() + " from " + selectedCountry + " - " + ex.getMessage());
            queryDocumentsException = LiferayUtils.getPortalTranslation(ex.getMessage());
            if (ex.getMessage().contains("4701")) {
                consentExists = false;
                throw new ConsentException();
            }

        }
    }

    private void createTRCA(String docType, String purposeOfUse) {
        if (docType.equals("ps")) {
            showPS = false;
        }
        log.info("TRCA: Starting setting the purpose of use: " + purposeOfUse);
        PatientId patientId = null;
        try {
            patientId = PatientId.Factory.newInstance();
            patientId.setExtension(selectedPatient.getExtension());
            patientId.setRoot(selectedPatient.getRoot());
            this.purposeOfUse = purposeOfUse;
            log.info("TRCA: Creating trca for hcpAssertion : " + hcpAssertion.getID() + " for patient " + patientId.getRoot() + ". Purpose of use is : " + purposeOfUse);
            trcAssertion = EpsosHelperService.createPatientConfirmationPlain(purposeOfUse, hcpAssertion, patientId);
            log.info("TRCA: Created " + trcAssertion.getID() + " for : " + hcpAssertion.getID() + " for patient " + patientId.getRoot() + "_" + patientId.getExtension() + ". Purpose of use is : " + purposeOfUse);
            log.info("TRCA:" + trcAssertion);
            trcassertionexists = true;
            trcassertionnotexists = false;
            // get patient documents
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TRCA ERROR", e.getMessage()));
            log.error("TRCA: Error creating trca for patient : " + patientId.getExtension() + " with hcpAssetion : " + hcpAssertion.getID() + ". Purpose of use is : " + purposeOfUse + " - " + e.getMessage());
            trcassertionexists = false;
            trcassertionnotexists = true;
            queryDocumentsException = LiferayUtils.getPortalTranslation(e.getMessage());
        }
    }

    public void setPurposeOfUseForEP(String purposeOfUse) {
        createTRCA("ep", purposeOfUse);
    }

    public void setPurposeOfUseForPS(String purposeOfUse) {
        createTRCA("ps", purposeOfUse);
    }

    public String getPurposeOfUseForPS() {
        return purposeOfUseForPS;
    }

    public String getQueryPatientsException() {
        return queryPatientsException;
    }

    public void setQueryPatientsException(String queryPatientsException) {
        this.queryPatientsException = queryPatientsException;
    }

    public String getErrorUserAssertion() {
        return errorUserAssertion;
    }

    public void setErrorUserAssertion(String errorUserAssertion) {
        this.errorUserAssertion = errorUserAssertion;
    }

    public String getQueryDocumentsException() {
        return queryDocumentsException;
    }

    public void setQueryDocumentsException(String queryDocumentsException) {
        this.queryDocumentsException = queryDocumentsException;
    }

    public boolean istrcassertionexists() {
        return trcassertionexists;
    }

    public void settrcassertionexists(boolean trcassertionexists) {
        this.trcassertionexists = trcassertionexists;
    }

    public boolean istrcassertionnotexists() {
        return trcassertionnotexists;
    }

    public void settrcassertionnotexists(boolean trcassertionnotexists) {
        this.trcassertionnotexists = trcassertionnotexists;
    }

    public boolean isShowPS() {
        return showPS;
    }

    public void setShowPS(boolean showPS) {
        this.showPS = showPS;
    }

    public boolean isTrcassertionexists() {
        return trcassertionexists;
    }

    public void setTrcassertionexists(boolean trcassertionexists) {
        this.trcassertionexists = trcassertionexists;
    }

    public boolean isTrcassertionnotexists() {
        return trcassertionnotexists;
    }

    public void setTrcassertionnotexists(boolean trcassertionnotexists) {
        this.trcassertionnotexists = trcassertionnotexists;
    }

    public boolean isConsentExists() {
        return consentExists;
    }

    public void setConsentExists(boolean consentExists) {
        this.consentExists = consentExists;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public void setSelectedPrescriptionFile(PatientDocument selectedPrescriptionFile) {
        this.selectedPrescriptionFile = selectedPrescriptionFile;
    }

    public void setPurposeOfUse(String purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }

    public Map<String, String> getLtrlanguages() {
        return ltrlanguages;
    }

    public void setLtrlanguages(Map<String, String> ltrlanguages) {
        this.ltrlanguages = ltrlanguages;
    }

    public String getLtrlang() {
        return ltrlang;
    }

    public void setLtrlang(String ltrlang) {
        this.ltrlang = ltrlang;
    }

    public boolean getEnablePacRep() {
        return enablePacRep;
    }

    public void setEnablePacRep(boolean enablePacRep) {
        this.enablePacRep = enablePacRep;
    }

    public Map<String, String> getRepuser() {
        return repuser;
    }

    public void setRepuser(Map<String, String> repuser) {
        this.repuser = repuser;
    }

    public String getRepUserString() {
        return repUserString;
    }

    public void setRepUserString(String repUserString) {
        this.repUserString = repUserString;
    }

    public Assertion getOnBehalf() {
        return onBehalf;
    }

    public void setOnBehalf(Assertion onBehalf) {
        this.onBehalf = onBehalf;
    }

    public Map<String, String> getOnbehalfidentityattrs() {
        return onbehalfidentityattrs;
    }

    public void setOnbehalfidentityattrs(Map<String, String> onbehalfidentityattrs) {
        this.onbehalfidentityattrs = onbehalfidentityattrs;
    }

    public Map<String, String> getOnbehalfdemographicsattrs() {
        return onbehalfdemographicsattrs;
    }

    public void setOnbehalfdemographicsattrs(Map<String, String> onbehalfdemographicsattrs) {
        this.onbehalfdemographicsattrs = onbehalfdemographicsattrs;
    }

    public String geteIdentifier() {
        return eIdentifier;
    }

    public void seteIdentifier(String eIdentifier) {
        this.eIdentifier = eIdentifier;
    }

    public List<tr.com.srdc.epsos.data.model.PatientId> getPatientIdentifiers() {
        return patientIdentifiers;
    }

    public void setPatientIdentifiers(List<tr.com.srdc.epsos.data.model.PatientId> patientIdentifiers) {
        this.patientIdentifiers = patientIdentifiers;
    }

    public STORKAuthnResponse getAuthnResponse() {
        return authnResponse;
    }

    public void setAuthnResponse(STORKAuthnResponse authnResponse) {
        this.authnResponse = authnResponse;
    }

    public boolean isEnableCCD() {
        return enableCCD;
    }

    public void setEnableCCD(boolean enableCCD) {
        this.enableCCD = enableCCD;
    }

    public String getPatientgivenname() {
        return patientgivenname;
    }

    public void setPatientgivenname(String patientgivenname) {
        this.patientgivenname = patientgivenname;
    }

    public String getPatientsurname() {
        return patientsurname;
    }

    public void setPatientsurname(String patientsurname) {
        this.patientsurname = patientsurname;
    }

    public String getPatientaddress() {
        return patientaddress;
    }

    public void setPatientaddress(String patientaddress) {
        this.patientaddress = patientaddress;
    }

    public String getPatientzip() {
        return patientzip;
    }

    public void setPatientzip(String patientzip) {
        this.patientzip = patientzip;
    }

    public String getPatientcity() {
        return patientcity;
    }

    public void setPatientcity(String patientcity) {
        this.patientcity = patientcity;
    }

    public String getPatientcountry() {
        return patientcountry;
    }

    public void setPatientcountry(String patientcountry) {
        this.patientcountry = patientcountry;
    }

}
