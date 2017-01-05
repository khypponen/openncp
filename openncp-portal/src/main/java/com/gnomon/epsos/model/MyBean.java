package com.gnomon.epsos.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.portlet.RenderRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.parse.BasicParserPool;
import org.primefaces.model.StreamedContent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.com.srdc.epsos.util.Constants;

import com.gnomon.LiferayUtils;
import com.gnomon.epsos.FacesService;
import com.gnomon.epsos.MyServletContextListener;
import com.gnomon.epsos.service.ConsentException;
import com.gnomon.epsos.service.Demographics;
import com.gnomon.epsos.service.EpsosHelperService;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;

import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.openncp.protocolterminator.ClientConnectorConsumer;
import epsos.openncp.protocolterminator.clientconnector.EpsosDocument1;
import epsos.openncp.protocolterminator.clientconnector.GenericDocumentCode;
import epsos.openncp.protocolterminator.clientconnector.PatientDemographics;
import epsos.openncp.protocolterminator.clientconnector.PatientId;
import epsos.openncp.protocolterminator.clientconnector.SubmitDocumentResponse;
import eu.epsos.util.EvidenceUtils;
import eu.epsos.util.IheConstants;

@ManagedBean
@SessionScoped
public class MyBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Country> countries;
	private String selectedCountry;
	private List<Identifier> identifiers;
	private List<Demographics> demographics;
	private List<Patient> patients;

	@ManagedProperty(value = "#{selectedPatient}")
	private Patient selectedPatient;
	private List<PatientDocument> patientDocuments;
	private PatientDocument selectedDocument;
	private EpsosDocument selectedEpsosDocument;
	private String documentHtml;
	private List<PatientDocument> patientPrescriptions;
	private String prescriptionHtml;
	private boolean showDemographics;
	private boolean showPatientList = true;
	private StreamedContent prescriptionFile;
	private PatientDocument selectedPrescriptionFile;
	private Assertion hcpAssertion = null;
	private boolean trcassertionexists = false;
	private boolean trcassertionnotexists = true;
	private Assertion trcAssertion;
	private Date consentStartDate;
	private Date consentEndDate;
	private String consentOpt;
	private String purposeOfUse;
	private String purposeOfUseForPS;
	private String purposeOfUseForEP;
	private String queryPatientsException;
	private String queryDocumentsException;
	private String queryPrescriptionsException;
	private boolean showEP;
	private boolean showPS;
	private boolean showMRO;
	private boolean enableMRO;
	private boolean enableHCER;
	private boolean enableCCD;
	private boolean enableCONSENT;
	private boolean enablePatientDocuments;
	private boolean enablePrescriptionDocuments;
	private boolean canConvert;
	private boolean consentExists;
	private String errorUserAssertion;
	private String cdaStylesheet;
	private String signedTRC;
	private static final Logger log = LoggerFactory.getLogger("MyBean");

	public MyBean() {
		log.info("Initializing MyBean ...");
		checkButtonPermissions();
		String epsosPropsPath = System.getenv("EPSOS_PROPS_PATH");
		log.info("EPSOS PROPS PATH IS : " + epsosPropsPath);
		if (!Validator.isNull(epsosPropsPath)) {
			try {
				cdaStylesheet = EpsosHelperService
						.getConfigProperty(EpsosHelperService.PORTAL_CDA_STYLESHEET);
				consentExists = false;
				consentOpt = "1";
				countries = new ArrayList<Country>();
				identifiers = new ArrayList<Identifier>();
				demographics = new ArrayList<Demographics>();
				countries = EpsosHelperService.getCountriesFromCS(LiferayUtils
						.getPortalLanguage());
				log.info("Countries found : " + countries.size());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error getting user", ""));
				log.error("Error getting user");
				log.error(ExceptionUtils.getStackTrace(e));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"EPSOS_PROPS_PATH not found", ""));
		}
	}

	public void showPrescription(ActionEvent actionEvent)
			throws SystemException {
		RenderRequest renderRequest = (RenderRequest) (FacesContext
				.getCurrentInstance().getExternalContext().getRequestMap()
				.get("javax.portlet.request"));
		String parameter = renderRequest.getParameter("productId");
		log.info("PRODUCT ID: " + parameter);
	}

	public List<Country> getCountries() {
		return countries;
	}

	public String getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(String selectedCountry) {

		User user = LiferayUtils.getPortalUser();
		LiferayUtils.storeToSession("user", user);

		this.selectedCountry = selectedCountry;
		identifiers = EpsosHelperService.getCountryIdentifiers(
				this.selectedCountry, LiferayUtils.getPortalLanguage(), null,
				null);
		demographics = EpsosHelperService.getCountryDemographics(
				this.selectedCountry, LiferayUtils.getPortalLanguage(), null,
				null);

		showDemographics = !demographics.isEmpty();
		LiferayUtils.storeToSession("selectedCountry", selectedCountry);
		patients = new ArrayList<Patient>();
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

	public void searchPatientsRequest(ActionEvent event) {
		checkButtonPermissions();
		log.info("searchPatientORequest ::: Selected country is : "
				+ selectedCountry);
		String country = (String) event.getComponent().getAttributes()
				.get("selectedCountry");
		identifiers = (List<Identifier>) event.getComponent().getAttributes()
				.get("identifiers");
		demographics = (List<Demographics>) event.getComponent()
				.getAttributes().get("demographics");
		if (Validator.isNotNull(country)) {
			selectedCountry = country;
		}
		log.info("searchPatientsRequest ::: Selected country is : "
				+ selectedCountry);
		searchPatients();
	}

	private void searchPatients(Assertion assertion, List<Identifier> id_,
			List<Demographics> dem_, String country) {
		log.info("Search Patients selected country is: " + country);
		String runningMode = MyServletContextListener.getRunningMode();
		String strMsg = "";
		Assertion ass = null;
		if (runningMode.equals("demo")) {
			patients = EpsosHelperService.getMockPatients();
			trcAssertion = null;
			trcassertionnotexists = true;
			trcassertionexists = false;
			showPatientList = true;
		} else {
			try {
				trcAssertion = null;
				trcassertionnotexists = true;
				trcassertionexists = false;
				patients = new ArrayList<Patient>();
				String serviceUrl = EpsosHelperService
						.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);
				log.info("CONNECTOR URL IS: " + serviceUrl);

				PatientDemographics pd = EpsosHelperService
						.createPatientDemographicsForQuery(id_, dem_);

				ClientConnectorConsumer proxy = MyServletContextListener
						.getClientConnectorConsumer();// new
														// ClientConnectorConsumer(serviceUrl);
				log.info("Test Assertions: "
						+ EpsosHelperService
								.getConfigProperty(EpsosHelperService.PORTAL_TEST_ASSERTIONS));
				ass = assertion;

				log.info("Searching for patients in " + country);
				log.info("Assertion id: " + ass.getID());
				log.info("PD: " + pd.toString());
				strMsg = pd.toString();

				try {
					EvidenceUtils.createEvidenceREMNRO(strMsg, "NI_PD",
							new DateTime(), EventOutcomeIndicator.FULL_SUCCESS
									.getCode().toString(), "NI_PD_REQ", ass
									.getID());
				} catch (Exception e) {
					log.error(ExceptionUtils.getStackTrace(e));
				}

				List<PatientDemographics> queryPatient = proxy.queryPatient(
						ass, country, pd);

				try {
					EvidenceUtils.createEvidenceREMNRR(strMsg, "NI_PD",
							new DateTime(), EventOutcomeIndicator.FULL_SUCCESS
									.getCode().toString(), "NI_PD_RES_SUCC",
							ass.getID());
				} catch (Exception e) {
					log.error(ExceptionUtils.getStackTrace(e));
				}

				for (PatientDemographics aux : queryPatient) {
					Patient patient = EpsosHelperService.populatePatient(aux);
					patients.add(patient);
					queryPatientsException = "";
				}
				if (queryPatient.isEmpty()) {
					queryPatientsException = LiferayUtils
							.getPortalTranslation("patient.list.no.patient");
				}
				log.info("Found " + patients.size() + " patients");
				showPatientList = true;
			} catch (Exception ex) {

				try {
					EvidenceUtils.createEvidenceREMNRR(strMsg, "NI_PD",
							new DateTime(),
							EventOutcomeIndicator.TEMPORAL_FAILURE.getCode()
									.toString(), "NI_PD_RES_FAIL", ass.getID());
				} catch (Exception e) {
					log.error(ExceptionUtils.getStackTrace(e));
				}

				log.error(ExceptionUtils.getStackTrace(ex));
				patients = new ArrayList<Patient>();
				showPatientList = true;
				queryPatientsException = LiferayUtils.getPortalTranslation(ex
						.getMessage());
			}
		}
	}

	public void searchPatients() {
		log.info("Searching for patients (creating assertions) ....");
		Object obj = EpsosHelperService.getUserAssertion();
		if (obj instanceof Assertion) {
			hcpAssertion = (Assertion) obj;
		} else if (obj instanceof String) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ASSERTION",
							obj.toString()));
			errorUserAssertion = (String) obj;
		}
		log.info("Searching for patients (deomgraphics) " + demographics.size()
				+ " (Identifiers) + " + identifiers.size());
		searchPatients(hcpAssertion, identifiers, demographics, selectedCountry);
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

	public void setDocumentHtml(String documentHtml) {
		this.documentHtml = documentHtml;
	}

	public List<PatientDocument> getPatientPrescriptions() {

		return patientPrescriptions;
	}

	public void setPatientPrescriptions(
			List<PatientDocument> patientPrescriptions) {
		this.patientPrescriptions = patientPrescriptions;
	}

	public void setPrescriptionHtml(String prescriptionHtml) {
		this.prescriptionHtml = prescriptionHtml;
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

	public void setCountries(List<Country> countries) {
		this.countries = countries;
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

	public void showForm(ActionEvent actionEvent) throws SystemException {

		// create user assertion
		errorUserAssertion = "";
		try {
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							"/view1.xhtml?javax.portlet.faces.PortletMode=view&amp;javax.portlet.faces.WindowState=normal");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public boolean getShowConsent() {
		log.debug("### GET SHOW CONSENT ###");
		log.debug("trcassertionexists: " + trcassertionexists);
		log.debug("consentExists: " + consentExists);
		if (!trcassertionexists) {
			return false;
		}
		if (trcassertionexists) {
			if (consentExists) {
				return false;
			}
		}
		return true;
	}

	public boolean getShowConfirmation() {
		log.debug("### GET SHOW CONFIRMATION ###");
		log.debug("trcassertionexists: " + trcassertionexists);
		log.debug("consentExists: " + consentExists);
		return !trcassertionexists;
	}

	public boolean getShowPrescriptions() {
		return showEP;
	}

	public boolean getShowPatientSummary() {
		return showPS;
	}

	public void getMRODocs() throws ConsentException {
		consentExists = true;
		trcassertionexists = true;
		PatientId patientId = null;
		try {
			patientDocuments = new ArrayList<PatientDocument>();
			String serviceUrl = EpsosHelperService
					.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL); // serviceUrl
																						// =
																						// LiferayUtils.getFromPrefs("client_connector_url");
			log.info("CLIENTCONNECTOR: " + serviceUrl);
			ClientConnectorConsumer clientConectorConsumer = MyServletContextListener
					.getClientConnectorConsumer();

			patientId = PatientId.Factory.newInstance();
			patientId.setExtension(selectedPatient.getExtension());
			patientId.setRoot(selectedPatient.getRoot());
			GenericDocumentCode classCode = GenericDocumentCode.Factory
					.newInstance();
			classCode.setNodeRepresentation(Constants.MRO_CLASSCODE);
			classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
			classCode.setValue(Constants.MRO_TITLE); // Patient
			// Summary
			// ClassCode.

			try {
				EvidenceUtils
						.createEvidenceREMNRO(patientId.toString(),
								"NI_DQ_MRO", new DateTime(),
								EventOutcomeIndicator.FULL_SUCCESS.getCode()
										.toString(), "NI_DQ_MRO_REQ",
								trcAssertion.getID());
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
			}

			log.info("MRO QUERY: Getting mro documents for : "
					+ patientId.getExtension() + " from " + selectedCountry);
			List<EpsosDocument1> queryDocuments = clientConectorConsumer
					.queryDocuments(hcpAssertion, trcAssertion,
							selectedCountry, patientId, classCode);
			log.info("MRO QUERY: Found " + queryDocuments.size() + " for : "
					+ patientId.getExtension() + " from " + selectedCountry);

			try {
				EvidenceUtils
						.createEvidenceREMNRR(patientId.toString(),
								"NI_DQ_MRO", new DateTime(),
								EventOutcomeIndicator.FULL_SUCCESS.getCode()
										.toString(), "NI_DQ_MRO_RES_SUCC",
								trcAssertion.getID());
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
			}

			showMRO = true;
			consentExists = true;
			for (EpsosDocument1 aux : queryDocuments) {
				PatientDocument document = EpsosHelperService.populateDocument(
						aux, "mro");
				patientDocuments.add(document);
			}
			queryDocumentsException = LiferayUtils
					.getPortalTranslation("report.list.no.document");
			log.debug("Selected Country: "
					+ LiferayUtils.getFromSession("selectedCountry"));
		} catch (Exception ex) {
			try {
				EvidenceUtils.createEvidenceREMNRR(patientId.toString(),
						"NI_DQ_MRO", new DateTime(),
						EventOutcomeIndicator.TEMPORAL_FAILURE.getCode()
								.toString(), "NI_DQ_MRO_RES_FAIL", trcAssertion
								.getID());
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
			}
			consentExists = true;
			log.error(ExceptionUtils.getStackTrace(ex));
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"DOCUMENT QUERY", LiferayUtils
									.getPortalTranslation(ex.getMessage())));
			log.error("MRO QUERY: Error getting ps documents for : "
					+ patientId.getExtension() + " from " + selectedCountry
					+ " - " + ex.getMessage());
			queryDocumentsException = LiferayUtils.getPortalTranslation(ex
					.getMessage());

			if (ex.getMessage().contains("4701")) {
				consentExists = false;
				throw new ConsentException();
			}

		}
	}

	public void getPSDocs() throws ConsentException {
		String runningMode = MyServletContextListener.getRunningMode();
		if (runningMode.equals("demo")) {
			patientDocuments = EpsosHelperService.getMockPSDocuments();
			consentExists = true;
			trcassertionexists = true;
			showPS = true;
		} else {
			consentExists = true;
			trcassertionexists = true;
			PatientId patientId = null;
			try {
				patientDocuments = new ArrayList<PatientDocument>();
				ClientConnectorConsumer clientConectorConsumer = MyServletContextListener
						.getClientConnectorConsumer();
				patientId = PatientId.Factory.newInstance();
				patientId.setExtension(selectedPatient.getExtension());
				patientId.setRoot(selectedPatient.getRoot());
				GenericDocumentCode classCode = GenericDocumentCode.Factory
						.newInstance();
				classCode.setNodeRepresentation(Constants.PS_CLASSCODE);
				classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
				classCode.setValue(Constants.PS_TITLE);

				log.info("PS QUERY: Getting ps documents for : "
						+ patientId.getExtension() + " from " + selectedCountry);
				log.info("HCP ASSERTION IS : " + hcpAssertion.getID());
				log.info("TRCA ASSERTION IS : " + trcAssertion.getID());
				log.info("selectedCountry : " + selectedCountry);
				log.info("patientId: " + patientId);
				log.info("classCode: " + classCode);

				// NRO
				try {
					EvidenceUtils.createEvidenceREMNRO(patientId.toString(),
							"NI_DQ_PS", new DateTime(),
							EventOutcomeIndicator.FULL_SUCCESS.getCode()
									.toString(), "NI_DQ_PS_REQ", trcAssertion
									.getID());
				} catch (Exception e) {
					log.error(ExceptionUtils.getStackTrace(e));
				}

				List<EpsosDocument1> queryDocuments = clientConectorConsumer
						.queryDocuments(hcpAssertion, trcAssertion,
								selectedCountry, patientId, classCode);

				log.info("PS QUERY: Found " + queryDocuments.size() + " for : "
						+ patientId.getExtension() + " from " + selectedCountry);

				try {
					EvidenceUtils.createEvidenceREMNRR(patientId.toString(),
							"NI_DQ_PS", new DateTime(),
							EventOutcomeIndicator.FULL_SUCCESS.getCode()
									.toString(), "NI_DQ_PS_RES_SUCC",
							trcAssertion.getID());
				} catch (Exception e) {
					log.error(ExceptionUtils.getStackTrace(e));
				}

				showPS = true;
				consentExists = true;
				for (EpsosDocument1 aux : queryDocuments) {
					PatientDocument document = EpsosHelperService
							.populateDocument(aux, "ps");
					patientDocuments.add(document);
				}
				queryDocumentsException = LiferayUtils
						.getPortalTranslation("report.list.no.document");
				log.debug("Selected Country: "
						+ LiferayUtils.getFromSession("selectedCountry"));
			} catch (Exception ex) {
				try {
					EvidenceUtils.createEvidenceREMNRR(patientId.toString(),
							"NI_DQ_PS", new DateTime(),
							EventOutcomeIndicator.TEMPORAL_FAILURE.getCode()
									.toString(), "NI_DQ_PS_RES_FAIL",
							trcAssertion.getID());
				} catch (Exception e) {
					log.error(ExceptionUtils.getStackTrace(e));
				}

				consentExists = true;
				log.error(ExceptionUtils.getStackTrace(ex));
				FacesContext.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"DOCUMENT QUERY", LiferayUtils
												.getPortalTranslation(ex
														.getMessage())));
				log.error("PS QUERY: Error getting ps documents for : "
						+ patientId.getExtension() + " from " + selectedCountry
						+ " - " + ex.getMessage());
				queryDocumentsException = LiferayUtils.getPortalTranslation(ex
						.getMessage());
				if (ex.getMessage().contains("4701")) {
					consentExists = false;
					throw new ConsentException();
				}
			}
		}
	}

	private void createTRCA(String docType, String purposeOfUse) {
		String runningMode = MyServletContextListener.getRunningMode();

		if (docType.equals("ps")) {
			showPS = false;
		}
		if (docType.equals("ep")) {
			showEP = false;
		}
		log.info("TRCA: Starting setting the purpose of use: " + purposeOfUse);
		log.info("signedTRC: " + getSignedTRC());
		PatientId patientId = null;
		try {
			patientId = PatientId.Factory.newInstance();
			patientId.setExtension(selectedPatient.getExtension());
			patientId.setRoot(selectedPatient.getRoot());
			this.purposeOfUse = purposeOfUse;
			log.info("TRCA: Creating trca for hcpAssertion : "
					+ hcpAssertion.getID() + " for patient "
					+ patientId.getRoot() + ". Purpose of use is : "
					+ purposeOfUse);
			if (runningMode.equals("demo")) {
				log.info("demo running so trca not created");
			} else if (getSignedTRC() == null) {
				trcAssertion = EpsosHelperService
						.createPatientConfirmationPlain(purposeOfUse,
								hcpAssertion, patientId);
				log.info("TRCA: Created " + trcAssertion.getID() + " for : "
						+ hcpAssertion.getID() + " for patient "
						+ patientId.getRoot() + "_" + patientId.getExtension()
						+ ". Purpose of use is : " + purposeOfUse);
			}
			trcassertionexists = true;
			trcassertionnotexists = false;
			// get patient documents
			try {
				if (docType.equals("ps")) {
					getPSDocs();
				}
				if (docType.equals("ep")) {
					getEPDocs();
				}
			} catch (ConsentException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"CONSENT ERROR", LiferayUtils
										.getPortalTranslation(e.getMessage())));
				consentExists = false;
			} catch (Exception ex) {
				FacesContext.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"TRCA ERROR", LiferayUtils
												.getPortalTranslation(ex
														.getMessage())));
				consentExists = false;
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "TRCA ERROR",
							LiferayUtils.getPortalTranslation(e.getMessage())));
			log.error("TRCA: Error creating trca for patient : "
					+ patientId.getExtension() + " with hcpAssetion : "
					+ hcpAssertion.getID() + ". Purpose of use is : "
					+ purposeOfUse + " - " + e.getMessage());
			log.error(ExceptionUtils.getStackTrace(e));
			trcassertionexists = false;
			trcassertionnotexists = true;
			queryDocumentsException = LiferayUtils.getPortalTranslation(e
					.getMessage());
		}
	}

	public void setPurposeOfUseForEP(String purposeOfUse) {
		createTRCA("ep", purposeOfUse);
	}

	public void setPurposeOfUseForPS(String purposeOfUse) {
		createTRCA("ps", purposeOfUse);
	}

	public void setPurposeOfUseForConsent(String purposeOfUse) {
		createTRCA("consent", purposeOfUse);
	}

	public void setPurposeOfUseForGeneric(String purposeOfUse) {
		createTRCA("generic", purposeOfUse);
	}

	private void saveConsent(ActionEvent actionEvent, String docType) {
		String consentCode = "";
		String consentDisplayName = "";

		log.info("Consent Start Date: " + consentStartDate);
		log.info("Consent End Date: " + consentEndDate);
		log.info("Consent Opt: " + consentOpt);

		String consentStartDateStr = new SimpleDateFormat("yyyyMMdd")
				.format(consentStartDate);
		String consentEndDateStr = new SimpleDateFormat("yyyyMMdd")
				.format(consentEndDate);

		if (consentOpt.equals("1")) {
			consentCode = "1.3.6.1.4.1.12559.11.10.1.3.2.4.1.1";
			consentDisplayName = "Opt-in";
		}
		if (consentOpt.equals("2")) {
			consentCode = "1.3.6.1.4.1.12559.11.10.1.3.2.4.1.2";
			consentDisplayName = "Opt-out";
		}

		Patient patient = selectedPatient;
		String consent = EpsosHelperService.createConsent(patient, consentCode,
				consentDisplayName, consentStartDateStr, consentEndDateStr);

		SubmitDocumentResponse resp = null;
		try {
			resp = submitConsent(consent);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", resp
							.toString()));
			// consentExists=true;
			// showPS=true;
			// showEP=true;
		} catch (Exception e) {
			log.error("CONSENT: Error submitting consent for patient : "
					+ patient.getRoot() + " with hcpAssetion : "
					+ hcpAssertion.getID() + " - " + e.getMessage());
			log.error(ExceptionUtils.getStackTrace(e));
			log.error(resp);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							LiferayUtils.getPortalTranslation(e.getMessage())));
			consentExists = false;
		}

		try {
			if (docType.equals("ps")) {
				getPSDocs();
			}
			if (docType.equals("ep")) {
				getEPDocs();
			}
			if (docType.equals("mro")) {
				getMRODocs();
			}
		} catch (ConsentException e) {
			consentExists = false;
			showPS = false;
			showEP = false;
		}

	}

	public void saveConsentPS(ActionEvent actionEvent) throws SystemException {
		saveConsent(actionEvent, "ps");
	}

	public void saveConsentEP(ActionEvent actionEvent) throws SystemException {
		saveConsent(actionEvent, "ep");
	}

	public void saveConsentMRO(ActionEvent actionEvent) throws SystemException {
		saveConsent(actionEvent, "mro");
	}

	public void saveConsentOther(ActionEvent actionEvent)
			throws SystemException {
		saveConsent(actionEvent, "other");
	}

	private SubmitDocumentResponse submitConsent(String xml) {
		String serviceUrl = EpsosHelperService
				.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);
		serviceUrl = EpsosHelperService
				.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);

		// serviceUrl = LiferayUtils.getFromPrefs("client_connector_url");
		ClientConnectorConsumer proxy = MyServletContextListener
				.getClientConnectorConsumer();

		GenericDocumentCode classCode = GenericDocumentCode.Factory
				.newInstance();
		classCode.setNodeRepresentation(Constants.CONSENT_CLASSCODE);
		classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
		classCode.setValue(Constants.CONSENT_TITLE);

		GenericDocumentCode formatCode = GenericDocumentCode.Factory
				.newInstance();

		formatCode.setSchema(IheConstants.CONSENT_FORMATCODE_CODINGSCHEMA);
		formatCode
				.setNodeRepresentation(IheConstants.CONSENT_FORMATCODE_NODEREPRESENTATION);
		formatCode.setValue(IheConstants.CONSENT_FORMATCODE_DISPLAYNAME);

		EpsosDocument1 document = EpsosDocument1.Factory.newInstance();
		document.setAuthor(LiferayUtils.getPortalUser().getFullName());
		Calendar cal = Calendar.getInstance();
		document.setCreationDate(cal);
		document.setDescription("Privacy Policy Acknowledgement Document");
		document.setTitle("Privacy Policy Acknowledgement Document ");
		document.setUuid(EpsosHelperService.getUniqueId());
		document.setClassCode(classCode);
		document.setFormatCode(formatCode);
		document.setBase64Binary(xml.getBytes());

		SubmitDocumentResponse resp = proxy.submitDocument(hcpAssertion,
				trcAssertion, selectedCountry, document,
				selectedPatient.getPatientDemographics());
		return resp;
	}

	public String getPurposeOfUseForPS() {
		return purposeOfUseForPS;
	}

	private void getEPDocs() throws ConsentException {
		consentExists = true;
		PatientId patientId = null;
		try {
			patientPrescriptions = new ArrayList<PatientDocument>();
			String serviceUrl = EpsosHelperService
					.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL); // serviceUrl
																						// =
																						// LiferayUtils.getFromPrefs("client_connector_url");
			ClientConnectorConsumer clientConectorConsumer = MyServletContextListener
					.getClientConnectorConsumer();

			patientId = PatientId.Factory.newInstance();
			patientId.setExtension(selectedPatient.getExtension());
			patientId.setRoot(selectedPatient.getRoot());

			GenericDocumentCode classCode = GenericDocumentCode.Factory
					.newInstance();
			classCode.setNodeRepresentation(Constants.EP_CLASSCODE);
			classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
			classCode.setValue(Constants.EP_TITLE); // EP

			try {
				EvidenceUtils
						.createEvidenceREMNRO(patientId.toString(), "NI_DQ_EP",
								new DateTime(),
								EventOutcomeIndicator.FULL_SUCCESS.getCode()
										.toString(), "NI_DQ_EP_REQ",
								trcAssertion.getID());
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
			}

			log.info("EP QUERY: Getting ep documents for : "
					+ patientId.getExtension() + " from " + selectedCountry);
			List<EpsosDocument1> queryDocuments = clientConectorConsumer
					.queryDocuments(hcpAssertion, trcAssertion,
							selectedCountry, patientId, classCode);

			try {
				EvidenceUtils
						.createEvidenceREMNRR(patientId.toString(), "NI_DQ_EP",
								new DateTime(),
								EventOutcomeIndicator.FULL_SUCCESS.getCode()
										.toString(), "NI_DQ_EP_RES_SUCC",
								trcAssertion.getID());
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
			}

			log.info("EP QUERY: Found " + queryDocuments.size() + " for : "
					+ patientId.getExtension() + " from " + selectedCountry);
			showEP = true;
			for (EpsosDocument1 aux : queryDocuments) {
				PatientDocument document = EpsosHelperService.populateDocument(
						aux, "ep");
				patientPrescriptions.add(document);
			}
			queryPrescriptionsException = LiferayUtils.getPortalTranslation(
					"document.empty.list", FacesService.getPortalLanguage());
			log.info("Documents are " + queryDocuments.size());
		} catch (Exception ex) {
			try {
				EvidenceUtils.createEvidenceREMNRR(patientId.toString(),
						"NI_DQ_EP", new DateTime(),
						EventOutcomeIndicator.TEMPORAL_FAILURE.getCode()
								.toString(), "NI_DQ_EP_RES_FAIL", trcAssertion
								.getID());
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
			}

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"QUERY DOCUMENTS", LiferayUtils
									.getPortalTranslation(ex.getMessage(),
											FacesService.getPortalLanguage())));
			log.error("EP QUERY: Error getting ep documents for : "
					+ patientId.getExtension() + " from " + selectedCountry
					+ " - " + ex.getMessage());
			log.error(ExceptionUtils.getStackTrace(ex));
			queryPrescriptionsException = LiferayUtils.getPortalTranslation(
					ex.getMessage(), FacesService.getPortalLanguage());
			if (ex.getMessage().contains("4701")) {
				consentExists = false;
				throw new ConsentException();
			}
		}
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

	public String getQueryPrescriptionsException() {
		return queryPrescriptionsException;
	}

	public void setQueryPrescriptionsException(
			String queryPrescriptionsException) {
		this.queryPrescriptionsException = queryPrescriptionsException;
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

	public boolean isShowEP() {
		return showEP;
	}

	public void setShowEP(boolean showEP) {
		this.showEP = showEP;
	}

	public boolean isShowPS() {
		return showPS;
	}

	public void setShowPS(boolean showPS) {
		this.showPS = showPS;
	}

	public boolean isShowMRO() {
		return showMRO;
	}

	public void setShowMRO(boolean showMRO) {
		this.showMRO = showMRO;
	}

	public String getConsentOpt() {
		return consentOpt;
	}

	public void setConsentOpt(String consentOpt) {
		this.consentOpt = consentOpt;
	}

	public Date getConsentStartDate() {
		return consentStartDate;
	}

	public void setConsentStartDate(Date consentStartDate) {
		this.consentStartDate = consentStartDate;
	}

	public Date getConsentEndDate() {
		return consentEndDate;
	}

	public void setConsentEndDate(Date consentEndDate) {
		this.consentEndDate = consentEndDate;
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

	/**
	 * The main goal of this method is to define an action to be taken by the
	 * portal if the Patient does not agrees with the specific and final consent
	 * (answer obtained through a last question in the work-flow). This action
	 * will force the portal to submit new consent for the patient.
	 *
	 * @param confirmation
	 */
	public void setSpecificConsent(String confirmation) {
		if (confirmation.equals("No")) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									LiferayUtils
											.getPortalTranslation("message.error"),
									LiferayUtils
											.getPortalTranslation("consent.not.given")));
			showPS = false;
		}
	}

	/**
	 * This method is placed here just to keep compliance with the presence of
	 * "getters" and "setters" of the property.
	 *
	 * @return
	 */
	public String getSpecificConsent() {
		return "No";
	}

	public String checkConfirmationDocuments() {
		if (!trcassertionexists) {
			ExternalContext ec = FacesContext.getCurrentInstance()
					.getExternalContext();
			ec.getRequestMap().put("selectedPatient", selectedPatient);
			return "viewPatientConfirmationForDocuments";
		}
		return "";
	}

	public String submitConfirmation() {
		createTRCA("generic", purposeOfUse);
		log.info("### Selected patient: " + selectedPatient.getFamilyName());
		ExternalContext ec = FacesContext.getCurrentInstance()
				.getExternalContext();
		ec.getRequestMap().put("trcAssertion", trcAssertion);
		return "genericPatientConfirmation";
	}

	public String getPurposeOfUseForEP() {
		return purposeOfUseForEP;
	}

	public boolean getEnableMRO() {
		return enableMRO;
	}

	public void setEnableMRO(boolean enableMRO) {
		this.enableMRO = enableMRO;
	}

	public boolean getEnableHCER() {
		return enableHCER;
	}

	public void setEnableHCER(boolean enableHCER) {
		this.enableHCER = enableHCER;
	}

	public boolean getEnablePatientDocuments() {
		return enablePatientDocuments;
	}

	public void setEnablePatientDocuments(boolean enablePatientDocuments) {
		this.enablePatientDocuments = enablePatientDocuments;
	}

	public boolean getEnablePrescriptionDocuments() {
		return enablePrescriptionDocuments;
	}

	public void setEnablePrescriptionDocuments(
			boolean enablePrescriptionDocuments) {
		this.enablePrescriptionDocuments = enablePrescriptionDocuments;
	}

	private void checkButtonPermissions() {
		User user = LiferayUtils.getPortalUser();
		if (Validator.isNotNull(user)) {

			String checkPermissions = EpsosHelperService
					.getConfigProperty(EpsosHelperService.PORTAL_CHECK_PERMISSIONS);
			String checkHCER = EpsosHelperService
					.getConfigProperty(EpsosHelperService.PORTAL_HCER_ENABLED);
			boolean hcer = false;
			if (Validator.isNotNull(checkHCER)) {
				if (checkHCER.equalsIgnoreCase("true")) {
					hcer = true;
				}
			}
			String checkMRO = EpsosHelperService
					.getConfigProperty(EpsosHelperService.PORTAL_MRO_ENABLED);
			boolean mro = false;
			if (Validator.isNotNull(checkMRO)) {
				if (checkMRO.equalsIgnoreCase("true")) {
					mro = true;
				}
			}

			String checkCCD = EpsosHelperService
					.getConfigProperty(EpsosHelperService.PORTAL_CCD_ENABLED);
			boolean ccd = false;
			if (Validator.isNotNull(checkCCD)) {
				if (checkCCD.equalsIgnoreCase("true")) {
					ccd = true;
					enableCCD = true;
				}
			}

			String checkCONSENT = EpsosHelperService
					.getConfigProperty(EpsosHelperService.PORTAL_CONSENT_ENABLED);
			boolean consent = false;
			if (Validator.isNotNull(checkCONSENT)) {
				if (checkCONSENT.equalsIgnoreCase("true")) {
					consent = true;
					enableCONSENT = true;
				} else {
					consent = false;
					enableCONSENT = false;
				}
			}

			if (Validator.isNotNull(checkPermissions)) {
				if (checkPermissions.equalsIgnoreCase("false")) {
					enableMRO = true;
					enableHCER = true;
					enablePatientDocuments = true;
					enablePrescriptionDocuments = true;
					enableCONSENT = true;
					return;
				}
			}
			boolean isPhysician = LiferayUtils.isDoctor(user.getUserId(),
					user.getCompanyId());
			boolean isPharmacist = LiferayUtils.isPharmacist(user.getUserId(),
					user.getCompanyId());
			boolean isNurse = LiferayUtils.isNurse(user.getUserId(),
					user.getCompanyId());
			boolean isAdministrator = LiferayUtils.isAdministrator(
					user.getUserId(), user.getCompanyId());

			if (isNurse) {
				enableMRO = false;
				enableHCER = false;
				enablePatientDocuments = false;
				enablePrescriptionDocuments = true;
			} else if (isPhysician) {
				enableMRO = true && mro;
				enableHCER = true && hcer;
				enablePatientDocuments = true;
				enablePrescriptionDocuments = false;
			} else if (isPharmacist) {
				enableMRO = false;
				enableHCER = false;
				enablePatientDocuments = false;
				enablePrescriptionDocuments = true;
			} else if (isAdministrator) {
				enableMRO = false;
				enableHCER = false;
				enablePatientDocuments = false;
				enablePrescriptionDocuments = false;
			} else {
				enableMRO = false;
				enableHCER = false;
				enablePatientDocuments = false;
				enablePrescriptionDocuments = false;
			}

			canConvert = false;
			try {
				String canConvertToCCD = PropsUtil.get("can.convert.to.ccd");
				if (Validator.isNotNull(canConvertToCCD)) {
					if (canConvertToCCD.equalsIgnoreCase("true")) {
						canConvert = true;
					}
				}
			} catch (Exception e) {
				log.error("Error getting property for can convert to ccd");
				log.error(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	public boolean isCanConvert() {
		return canConvert;
	}

	public void setCanConvert(boolean canConvert) {
		this.canConvert = canConvert;
	}

	public boolean getEnableCCD() {
		return enableCCD;
	}

	public void setEnableCCD(boolean enableCCD) {
		this.enableCCD = enableCCD;
	}

	public boolean getEnableCONSENT() {
		return enableCONSENT;
	}

	public void setEnableCONSENT(boolean enableCONSENT) {
		this.enableCONSENT = enableCONSENT;
	}

	public String getCdaStylesheet() {
		return cdaStylesheet;
	}

	public void setCdaStylesheet(String cdaStylesheet) {
		this.cdaStylesheet = cdaStylesheet;
	}

	public String getSignedTRC() {
		return signedTRC;
	}

	public void setSignedTRC(String signedTRC) throws Exception {
		log.info("signedTRC: " + signedTRC);
		if (signedTRC != null && !signedTRC.isEmpty()) {
			// Initialize the library
			DefaultBootstrap.bootstrap();

			// Get parser pool manager
			BasicParserPool ppMgr = new BasicParserPool();
			ppMgr.setNamespaceAware(true);

			// Parse metadata file
			InputStream in = new ByteArrayInputStream(
					signedTRC.getBytes("UTF-8"));
			Document inCommonMDDoc = ppMgr.parse(in);
			Element metadataRoot = inCommonMDDoc.getDocumentElement();

			// Get apropriate unmarshaller
			UnmarshallerFactory unmarshallerFactory = Configuration
					.getUnmarshallerFactory();
			Unmarshaller unmarshaller = unmarshallerFactory
					.getUnmarshaller(metadataRoot);

			// Unmarshall using the document root element, an EntitiesDescriptor
			// in this case
			trcAssertion = (Assertion) unmarshaller.unmarshall(metadataRoot);

			// Assertion trca = (Assertion)
			// EpsosHelperService.fromElement(doc.getDocumentElement());
			log.info("TRCA " + trcAssertion + " with ID: "
					+ trcAssertion.getID());
			LiferayUtils.storeToSession("trcAssertion", trcAssertion);
			this.signedTRC = signedTRC;
		}
	}

}
