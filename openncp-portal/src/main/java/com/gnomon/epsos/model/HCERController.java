package com.gnomon.epsos.model;

import com.gnomon.LiferayUtils;
import com.gnomon.epsos.MyServletContextListener;
import com.gnomon.epsos.service.EpsosHelperService;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.ccd.gnomon.cda.parser.beans.Address;
import epsos.ccd.gnomon.cda.parser.beans.Author;
import epsos.ccd.gnomon.cda.parser.beans.Birthdate;
import epsos.ccd.gnomon.cda.parser.beans.Code;
import epsos.ccd.gnomon.cda.parser.beans.Contact;
import epsos.ccd.gnomon.cda.parser.beans.Custodian;
import epsos.ccd.gnomon.cda.parser.beans.Gender;
import epsos.ccd.gnomon.cda.parser.beans.GenericCode;
import epsos.ccd.gnomon.cda.parser.beans.LegalAuthenticator;
import epsos.ccd.gnomon.cda.parser.beans.MedicalComponent;
import epsos.ccd.gnomon.cda.parser.beans.Name;
import epsos.ccd.gnomon.cda.parser.beans.Observation;
import epsos.ccd.gnomon.cda.parser.beans.Organization;
import epsos.ccd.gnomon.cda.parser.beans.Problem;
import epsos.ccd.gnomon.cda.parser.beans.RelatedDocument;
import epsos.ccd.gnomon.cda.parser.beans.Time;
import epsos.ccd.gnomon.cda.parser.docs.ClinicalDocument;
import epsos.ccd.gnomon.cda.parser.docs.HCEReport;
import epsos.ccd.gnomon.cda.parser.enums.ContactUse;
import epsos.ccd.gnomon.cda.parser.enums.MedicalSection;
import epsos.ccd.gnomon.cda.parser.enums.URLScheme;
import epsos.openncp.protocolterminator.ClientConnectorConsumer;
import epsos.openncp.protocolterminator.clientconnector.EpsosDocument1;
import epsos.openncp.protocolterminator.clientconnector.GenericDocumentCode;
import eu.epsos.util.EvidenceUtils;
import eu.epsos.util.IheConstants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.portlet.PortletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.hibernate.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;
import org.primefaces.context.RequestContext;
import org.xml.sax.InputSource;

/**
 * A plain HCER controller.
 *
 * @author Akis Papadopoulos
 */
@ManagedBean(name = "hcerController")
@SessionScoped
public class HCERController {

    //Messages LOGGER
    private static final Logger LOGGER = Logger.getLogger("HCERController");
    //Patient controller bean
    @ManagedProperty(value = "#{myBean}")
    private MyBean myBean;
    //List of current session stored diagnoses
    @ManagedProperty(value = "#{diagnoses}")
    private Diagnoses diagnoses;

    /**
     * A method setting the MyBean property.
     *
     * @param myBean the bean to set.
     */
    public void setMyBean(MyBean myBean) {
        this.myBean = myBean;
    }

    /**
     * A method setting the reference of the diagnoses list.
     *
     * @param diagnoses the current stored list diagnoses.
     */
    public void setDiagnoses(Diagnoses diagnoses) {
        this.diagnoses = diagnoses;
    }

    /**
     * A method returning if the HCER document is filled at least with one
     * medical entry.
     *
     * @return if the HCER document is filled with medical entries.
     */
    public boolean isFilled() {
        return !diagnoses.isEmpty();
    }

    /**
     * A method parsing stored medical entries into a valid HCER document
     * submitting it to the client connector.
     *
     * @param event an action event.
     */
    public void submit(ActionEvent event) {
        //Getting the request context
        RequestContext rc = RequestContext.getCurrentInstance();

        //Parsing enties into a valid HCER clinical document
        String hcer = parse();

        //Getting the session selected country
        String selectedCountry = myBean.getSelectedCountry();

        //Getting the selected session patient
        com.gnomon.epsos.model.Patient patient = myBean.getSelectedPatient();

        //Getting the session store assertions
        Assertion hcpAssertion = myBean.getHcpAssertion();
        Assertion trcAssertion = myBean.getTrcAssertion();

        //Getting the logged in session user
        User user = LiferayUtils.getPortalUser();

        //TMP
        LOGGER.info("---------------------------------------");
        LOGGER.info("Country: " + selectedCountry);
        LOGGER.info("Patient: " + patient);
        LOGGER.info("HCP-Assertion: " + hcpAssertion);
        LOGGER.info("TRC-Assertion: " + trcAssertion);
        LOGGER.info("User: " + user);
        LOGGER.info("---------------------------------------");
        //TMP

        //Extracting the bytecode from the HCER XML document
        byte[] bytes = hcer.getBytes();

        //Validating the bytecode
        if (Validator.isNotNull(bytes)) {
            //Getting the CC service URL
            String serviceUrl = EpsosHelperService.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);

            //Creating a CC consumer
            ClientConnectorConsumer proxy = MyServletContextListener.getClientConnectorConsumer();

            //Creating a new EPSOS document
            EpsosDocument1 document = EpsosDocument1.Factory.newInstance();

            //Setting class codes
            GenericDocumentCode classCode = GenericDocumentCode.Factory.newInstance();
            //TBM
            classCode.setNodeRepresentation("34133-9"); //HCER_CLASSCODE = "34133-9"
            //TBM
            classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
            classCode.setValue("Healthcare encounter report"); //HCER_TITLE = Healthcare encounter report
            document.setClassCode(classCode);

            //Setting format codes
            GenericDocumentCode formatCode = GenericDocumentCode.Factory.newInstance();
            //TBM
            formatCode.setSchema("epSOS coded healthcare encounter report"); //HCER_FORMATCODE_DISPLAYNAME = epSOS coded healthcare encounter report
            formatCode.setNodeRepresentation("urn:epsos:hcer:hcer:2012"); //DISPENSATION_FORMATCODE_NODEREPRESENTATION = urn:epsos:hcer:hcer:2012
            formatCode.setValue("epSOS formatCodes"); //DISPENSATION_FORMATCODE_CODINGSCHEMA = epSOS formatCodes
            //TBM
            document.setFormatCode(formatCode);

            //Setting document attributes
            document.setAuthor(user.getFullName());
            document.setCreationDate(Calendar.getInstance());
            //TBM
            document.setDescription("Healthcare encounter report");
            document.setTitle("Healthcare encounter report");
            //TBM
            document.setUuid(EpsosHelperService.getUniqueId());

            //Setting the document bytecode
            document.setBase64Binary(bytes);

//            try {
//                EvidenceUtils.createEvidenceREMNRO(classCode.toString(),
//                        "NI_XDR_" + classCode.getValue(),
//                        new DateTime(),
//                        EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
//                        "NI_XDR_" + classCode.getValue() + "_REQ",
//                        trcAssertion.getID());
//            } catch (Exception e) {
//                LOGGER.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
//            }

            try {
                //Submitting the HCER document
                proxy.submitDocument(hcpAssertion, trcAssertion, selectedCountry, document, patient.getPatientDemographics());
//                try {
//                    EvidenceUtils.createEvidenceREMNRR(classCode.toString(),
//                            "NI_XDR" + classCode.getValue(),
//                            new DateTime(),
//                            EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
//                            "NI_XDR_" + classCode.getValue() + "_RES_SUCC",
//                            trcAssertion.getID());
//                } catch (Exception e) {
//                    LOGGER.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
//                }
                //Clearing the data table in the ui
                diagnoses.clear();

                //Adding the validation indicator back to the client
                rc.addCallbackParam("submitted", true);
            } catch (Exception exc) {
//                try {
//                    EvidenceUtils.createEvidenceREMNRR(classCode.toString(),
//                            "NI_XDR" + classCode.getValue(),
//                            new DateTime(),
//                            EventOutcomeIndicator.TEMPORAL_FAILURE.getCode().toString(),
//                            "NI_XDR_" + classCode.getValue() + "_RES_FAIL",
//                            trcAssertion.getID());
//                } catch (Exception e) {
//                    LOGGER.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
//                }
                LOGGER.error(ExceptionUtils.getStackTrace(exc));
                LOGGER.error("[" + new Date() + "]" + " ERROR: " + "Submitting the HCER EPSOS document to the Client Connector.");
                //Adding the validation indicator back to the client
                rc.addCallbackParam("submitted", false);
            }
        } else {
            LOGGER.info("$$$$$$$$$$$$$$$$$$$$$$$4ERROR: HCER submission failed dye to the invalid bytecode");
            //Logging an error message
            LOGGER.error("[" + new Date() + "]" + " ERROR: " + "Validating the HCER XML bytecode.");

            //Adding the validation indicator back to the client
            rc.addCallbackParam("submitted", false);
        }
    }

    /**
     * A method parsing stored medical entries into a valid HCER document
     * submitting it to the client connector.
     *
     * @param event an action event.
     */
    public void save() throws IOException {
        //Getting the request context
        LOGGER.info("Saving the document");
        RequestContext rc = RequestContext.getCurrentInstance();

        //Parsing enties into a valid HCER clinical document
        String hcer = parse();
        String txt = "test";
        //Extracting the bytecode from the HCER XML document
        byte[] bytes = hcer.getBytes();
        //Validating the bytecode
        if (Validator.isNotNull(bytes)) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            PortletResponse portletResponse = (PortletResponse) ec.getResponse();

            HttpServletResponse res = PortalUtil.getHttpServletResponse(portletResponse);
            String filename = "hcer.xml";
            res.setHeader("Content-Disposition", "attachment; filename=hcer.xml");
            res.setContentType("text/xml");
            res.setHeader("Cache-Control", "no-cache");
            res.setDateHeader("Expires", 0);
            res.setHeader("Pragma", "No-cache");
            res.flushBuffer();
            OutputStream OutStream = res.getOutputStream();
            OutStream.write(bytes);
            OutStream.flush();
            OutStream.close();
            fc.responseComplete();

        } else {
            //TMP
            LOGGER.info("$$$$$$$$$$$$$$$$$$$$$$$4ERROR: HCER submission failed dye to the invalid bytecode");
            //TMP

            //Logging an error message
            LOGGER.error("[" + new Date() + "]" + " ERROR: " + "Validating the HCER XML bytecode.");

            //Adding the validation indicator back to the client
            rc.addCallbackParam("submitted", false);
        }
    }

    /**
     * A method building the HCER clinical document.
     *
     * @return the XML representation of the HCER document.
     */
    private String parse() {
        User user = LiferayUtils.getPortalUser();
        LOGGER.info(user.getFullName() + " " + myBean.getSelectedPatient());
        //Getting the patient identifier
        String extension = myBean.getSelectedPatient().getExtension();
        String root = myBean.getSelectedPatient().getRoot();
        epsos.ccd.gnomon.cda.parser.beans.Identifier pid;
        pid = new epsos.ccd.gnomon.cda.parser.beans.Identifier(extension, root);

        //Getting the patient name
        String family = myBean.getSelectedPatient().getFamilyName();
        String given = myBean.getSelectedPatient().getName();
        Name pname = new Name(family, given);

        //Getting the patient gender
        String gender = myBean.getSelectedPatient().getAdministrativeGender();
        Gender pgender = new Gender(gender);

        //Getting the patient date of birth
        String date = myBean.getSelectedPatient().getBirthDate();
        Birthdate pdob = new Birthdate(date);

        //Getting the address of the patient
        String street = myBean.getSelectedPatient().getAddress();
        String city = myBean.getSelectedPatient().getCity();
        String postal = myBean.getSelectedPatient().getPostalCode();
        String country = myBean.getSelectedPatient().getCountry();
        Address paddress = new Address(street, city, postal, country);

        //Creating the selected patient
        epsos.ccd.gnomon.cda.parser.beans.Patient patient;
        patient = new epsos.ccd.gnomon.cda.parser.beans.Patient(pid, pname, pgender, pdob);
        patient.setAddress(paddress);

        //Getting the patient phone contact
        String phone = myBean.getSelectedPatient().getTelephone();

        //Getting the patient email contact
        String email = myBean.getSelectedPatient().getEmail();

        //Checking if the contact are provided
        if (phone == null && email == null) {
            //Creating a null flavor contact
            Contact nfc = new Contact();

            //Adding to the patient
            patient.addContact(nfc);
        } else if (phone != null) {
            //Creating a phone contact
            Contact contact = new Contact(ContactUse.H, URLScheme.TEL, phone);

            //Adding the phone to the patients contacts
            patient.addContact(contact);
        } else {
            //Creating a email contact
            Contact contact = new Contact(ContactUse.H, URLScheme.MAILTO, email);

            //Adding the phone to the patients contacts
            patient.addContact(contact);
        }

        //Setting the patient class codes
        patient.setTypeCode("RCT");
        patient.setContextControlCode("OP");
        patient.setClassCode("PAT");

        //OPZ
        //Creating represented organization id
        epsos.ccd.gnomon.cda.parser.beans.Identifier orgid;
        orgid = new epsos.ccd.gnomon.cda.parser.beans.Identifier("90009016", "1.3.6.1.4.1.28284.6.2.4.1");

        //Creating an organization
        String aphone = "";
        try {
            aphone = user.getPhones().get(0).getExtension() + " " + user.getPhones().get(0).getNumber();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        Organization org = new Organization(orgid, user.getFullName());
        org.addContact(new Contact(ContactUse.WP, URLScheme.TEL, aphone));
        org.addContact(new Contact(ContactUse.WP, URLScheme.MAILTO, "mailto:" + user.getEmailAddress()));

        //Creating the author of the clinical document
        Author author = new Author(pid, new Name(user.getFirstName(), user.getLastName()), org);
        author.setTime(new Date());
        author.setClassCode("ASSIGNED");

        String astreet = "";
        String acity = "";
        String apostal = "";
        String azipcode = "";
        String acountry = "";

        try {
            if (user.getAddresses().size() > 0) {
                //Getting the address of the author
                astreet = user.getAddresses().get(0).getStreet1();
                acity = user.getAddresses().get(0).getCity();
                apostal = user.getAddresses().get(0).getZip();
                acountry = user.getAddresses().get(0).getCountry().getName();
            }
            Address aaddress = new Address(astreet, acity, apostal, acountry);
            author.setAddress(aaddress);

        } catch (Exception ex) {
            LOGGER.error(ExceptionUtils.getStackTrace(ex));
        }

        //Creating legal authenticator identifier
        epsos.ccd.gnomon.cda.parser.beans.Identifier legid;
        legid = new epsos.ccd.gnomon.cda.parser.beans.Identifier("121312", "1.3.6.1.4.1.28284.6.2.4.32");

        //Creating a legal authenticator
        LegalAuthenticator authenticator = new LegalAuthenticator(legid, null, org);
        authenticator.setTime(new Date());

        //Creating a custodian
        Custodian custodian = new Custodian(org);
        //OPZ

        //Creating the document class code
        Code docode = new GenericCode("34133-9", "Summarization of Episode Note", "2.16.840.1.113883.6.1", "LOINC");

        //Creating the HCER document identifier
        epsos.ccd.gnomon.cda.parser.beans.Identifier docid;
        docid = new epsos.ccd.gnomon.cda.parser.beans.Identifier(extension + ".hcer", root);

        //Creating an HCER clinical document
        ClinicalDocument hcer = new HCEReport(docode, docid, new Date(), new Date(), epsos.ccd.gnomon.cda.parser.enums.Country.United_Kingdom);
        hcer.setTitle("Health Care Encounter Report");
        hcer.setPatient(patient);
        hcer.setAuthor(author);
        hcer.setAuthenticator(authenticator);
        hcer.setCustodian(custodian);

        //OPZ
        //Adding related documents
        hcer.addRelatedDocument(new RelatedDocument("XFRM", new epsos.ccd.gnomon.cda.parser.beans.Identifier("201304291013151234", "1.3.6.1.4.1.28284.6.2.4.75")));
        hcer.addRelatedDocument(new RelatedDocument("APND", new epsos.ccd.gnomon.cda.parser.beans.Identifier("201304290913154321", "2.16.17.710.820.1000.990.1.1.1")));
        //OPZ

        //Creating a diagnosis medical section component
        MedicalComponent component = new MedicalComponent(MedicalSection.PROBLEM_LIST, "Problem list");

        //Iterating through the session stored list of diagnoses
        for (Diagnosis diag : diagnoses.getList()) {
            //Creating the diagnosis problem code
            Code code = new GenericCode(diag.getProblemCode(), diag.getProblem(), "2.16.840.1.113883.6.96", "SNOMED CT");

            //Creating the effective time
            Time time = new Time(diag.getOnset(), diag.getResolution());

            //Creating the diagnosis medical problem
            Problem problem = new Problem(code, time, diag.getStatus().toLowerCase());
            problem.setDescription(diag.getDescription());
            LOGGER.info("#####:" + diag.getDescription());
            problem.setRoot("2.16.470.1.100.1.1.1000.990.1");

            //Getting the entry list of stored observations
            List<Map.Entry<String, String>> entries = diag.getSelectedObservations();

            //Iterating through the stored observations
            for (int i = 0; i < entries.size(); i++) {
                //Getting the next observation entry
                Map.Entry<String, String> entry = entries.get(i);

                //Creating the observation related to the problem
                Observation observation = new Observation(entry.getValue(), entry.getKey(), "1.3.6.1.4.1.12559.11.10.1.3.1.44.2", "ICD-10");

                //Adding the next observation to the current diagnosis problem
                problem.addObservation(observation);
            }

            //Adding the medical entry
            component.addEntry(problem);
        }

        //Adding some roots into the diagnoses medical section
        component.addRoot("2.16.840.1.113883.10.20.1.11");
        component.addRoot("1.3.6.1.4.1.19376.1.5.3.1.3.6");

        //Adding the component to the HCER document
        hcer.addComponent(component);

        //TMP
        LOGGER.info("-----------------------------------------------------");
        LOGGER.info("Extension: " + extension);
        LOGGER.info("Root: " + root);
        LOGGER.info("Family: " + family);
        LOGGER.info("Given: " + given);
        LOGGER.info("Gender: " + gender);
        LOGGER.info("Birthdate: " + date);
        LOGGER.info("Street: " + street);
        LOGGER.info("City: " + city);
        LOGGER.info("Postal: " + postal);
        LOGGER.info("Country: " + country);
        LOGGER.info("Phone: " + phone);
        LOGGER.info("Email: " + email);
        LOGGER.info("-----------------------------------------------------");
        try {
            // Saving locally the XML patient summary into an XML file
            FileWriter writer = new FileWriter(new File("hcer-report.xml"));
            writer.write(hcer.toString());
            writer.flush();

            // Printing a well-formed XML format into the console
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(hcer.toString().getBytes())));
            StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);
            LOGGER.info(new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray()));
        } catch (Exception exc) {
            LOGGER.error(ExceptionUtils.getStackTrace(exc));
        }
        LOGGER.info("-----------------------------------------------------");
        //TMP

        return hcer.toString();
    }
}
