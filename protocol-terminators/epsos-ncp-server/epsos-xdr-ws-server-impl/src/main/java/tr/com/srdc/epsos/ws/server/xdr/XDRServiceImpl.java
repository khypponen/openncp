/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * <p>
 * This file is part of SRDC epSOS NCP.
 * <p>
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * XDR service implementation for ePrescription by Kela (The Social Insurance
 * Institution of Finland) GNU General Public License v3
 */
package tr.com.srdc.epsos.ws.server.xdr;

import epsos.ccd.gnomon.auditmanager.*;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import eu.epsos.exceptions.DocumentTransformationException;
import eu.epsos.protocolterminators.ws.server.exception.NIException;
import eu.epsos.protocolterminators.ws.server.xdr.DocumentProcessingException;
import eu.epsos.protocolterminators.ws.server.xdr.DocumentSubmitInterface;
import eu.epsos.protocolterminators.ws.server.xdr.XDRServiceInterface;
import eu.epsos.pt.transformation.TMServices;
import eu.epsos.util.EvidenceUtils;
import eu.epsos.util.IheConstants;
import eu.epsos.validation.datamodel.cda.CdaModel;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.services.CdaValidationService;
import fi.kela.se.epsos.data.model.DocumentFactory;
import fi.kela.se.epsos.data.model.EPSOSDocument;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.util.XMLUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.com.srdc.epsos.securityman.SAML2Validator;
import tr.com.srdc.epsos.securityman.exceptions.AssertionValidationException;
import tr.com.srdc.epsos.securityman.exceptions.InsufficientRightsException;
import tr.com.srdc.epsos.securityman.exceptions.InvalidFieldException;
import tr.com.srdc.epsos.securityman.exceptions.MissingFieldException;
import tr.com.srdc.epsos.securityman.helper.Helper;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.DateUtil;
import tr.com.srdc.epsos.util.XMLUtil;
import tr.com.srdc.epsos.util.http.HTTPUtil;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ServiceLoader;

public class XDRServiceImpl implements XDRServiceInterface {

    public static Logger logger = LoggerFactory.getLogger(XDRServiceImpl.class);
    private oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory ofRs;
    private ServiceLoader<DocumentSubmitInterface> serviceLoader;
    private DocumentSubmitInterface documentSubmitService;

    public XDRServiceImpl() throws Exception {
        serviceLoader = ServiceLoader.load(DocumentSubmitInterface.class);
        try {
            logger.info("Loading National implementation of DocumentSubmitInterface...");
            documentSubmitService = serviceLoader.iterator().next();
            logger.info("Successfully loaded documentSubmitService");
        } catch (Exception e) {
            logger.error("Failed to load implementation of documentSubmitService: " + e.getMessage(), e);
            throw e;
        }

        ofRs = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory();
    }

    protected XDRServiceImpl(DocumentSubmitInterface dsi, oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory ofRs) {
        this.documentSubmitService = dsi;
        this.ofRs = ofRs;
    }

    private RegistryError createErrorMessage(String errorCode, String codeContext, String value, boolean isWarning) {

        RegistryError re = ofRs.createRegistryError();
        re.setErrorCode(errorCode);
        re.setLocation(getLocation());
        re.setSeverity("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:"
                + (isWarning ? "Warning" : "Error"));
        re.setCodeContext(codeContext);
        re.setValue(value);

        return re;
    }

    protected String getLocation() {
        ConfigurationManagerService configManager = ConfigurationManagerService.getInstance();
        String location = configManager.getServiceWSE(Constants.COUNTRY_CODE.toLowerCase(Locale.ENGLISH),
                Constants.ConsentService);

        return location;
    }

    /**
     * Prepare audit log for the dispensation service, initialize() operation,
     * i.e. dispensation submit operation
     *
     * @throws DatatypeConfigurationException
     * @author konstantin.hypponen@kela.fi
     */
    public void prepareEventLogForDispensationInitialize(EventLog eventLog,
                                                         ProvideAndRegisterDocumentSetRequestType request,
                                                         RegistryResponseType response, Element sh) {

        ConfigurationManagerService cms = ConfigurationManagerService.getInstance();
        eventLog.setEventType(EventType.epsosDispensationServiceInitialize);
        eventLog.setEI_TransactionName(TransactionName.epsosDispensationServiceInitialize);
        eventLog.setEI_EventActionCode(EventActionCode.UPDATE);
        try {
            eventLog.setEI_EventDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
        } catch (DatatypeConfigurationException e) {
            logger.error("DatatypeConfigurationException: {}", e.getMessage());
        }
        if (request.getSubmitObjectsRequest().getRegistryObjectList() != null) {
            for (int i = 0; i < request.getSubmitObjectsRequest()
                    .getRegistryObjectList().getIdentifiable().size(); i++) {
                if (!(request.getSubmitObjectsRequest().getRegistryObjectList()
                        .getIdentifiable().get(i).getValue() instanceof ExtrinsicObjectType)) {
                    continue;
                }
                ExtrinsicObjectType eot = (ExtrinsicObjectType) request
                        .getSubmitObjectsRequest().getRegistryObjectList()
                        .getIdentifiable().get(i).getValue();
                String documentId = "";
                for (ExternalIdentifierType eit : eot.getExternalIdentifier()) {
                    if (eit.getIdentificationScheme().equals(
                            "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab")) {
                        documentId = eit.getValue();
                    }
                }
                eventLog.setET_ObjectID(documentId);
                break;
            }
        }

        if (response.getRegistryErrorList() != null) {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.PERMANENT_FAILURE);
        } else {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.FULL_SUCCESS);
        }

        eventLog.setHR_UserID(Constants.HR_ID_PREFIX + "<" + Helper.getUserID(sh) + "@" + Helper.getOrganization(sh) + ">");
        eventLog.setHR_AlternativeUserID(Helper.getAlternateUserID(sh));
        eventLog.setHR_RoleID(Helper.getRoleID(sh));

        eventLog.setSP_UserID(HTTPUtil.getSubjectDN(true));

        eventLog.setPT_PatricipantObjectID(getDocumentEntryPatientId(request));

        eventLog.setAS_AuditSourceId(Constants.COUNTRY_PRINCIPAL_SUBDIVISION);

        if (response.getRegistryErrorList() != null) {
            RegistryError re = response.getRegistryErrorList()
                    .getRegistryError().get(0);
            eventLog.setEM_PatricipantObjectID(re.getErrorCode());
            eventLog.setEM_PatricipantObjectDetail(re.getCodeContext()
                    .getBytes());
        }

        logger.debug("Event log prepared");
    }

    /**
     * Prepare audit log for the consent service, put() operation, i.e. consent
     * submission
     *
     * @throws DatatypeConfigurationException
     * @author konstantin.hypponen@kela.fi TODO: check the audit logs in
     * Gazelle, fix if needed
     */
    public void prepareEventLogForConsentPut(EventLog eventLog, ProvideAndRegisterDocumentSetRequestType request, RegistryResponseType response, Element sh) {

        ConfigurationManagerService cms = ConfigurationManagerService.getInstance();
        eventLog.setEventType(EventType.epsosConsentServicePut);
        eventLog.setEI_TransactionName(TransactionName.epsosConsentServicePut);
        eventLog.setEI_EventActionCode(EventActionCode.UPDATE);
        try {
            eventLog.setEI_EventDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
        } catch (DatatypeConfigurationException e) {
            logger.error("DatatypeConfigurationException: {}", e.getMessage());
        }

        if (request.getSubmitObjectsRequest().getRegistryObjectList() != null) {
            for (int i = 0; i < request.getSubmitObjectsRequest()
                    .getRegistryObjectList().getIdentifiable().size(); i++) {
                if (!(request.getSubmitObjectsRequest().getRegistryObjectList()
                        .getIdentifiable().get(i).getValue() instanceof ExtrinsicObjectType)) {
                    continue;
                }
                ExtrinsicObjectType eot = (ExtrinsicObjectType) request
                        .getSubmitObjectsRequest().getRegistryObjectList()
                        .getIdentifiable().get(i).getValue();
                String documentId = "";
                for (ExternalIdentifierType eit : eot.getExternalIdentifier()) {
                    if (eit.getIdentificationScheme().equals(
                            "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab")) {
                        documentId = eit.getValue();
                    }
                }
                eventLog.setET_ObjectID(documentId);
                break;
            }
        }

        if (response.getRegistryErrorList() != null) {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.PERMANENT_FAILURE);
        } else {
            eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.FULL_SUCCESS);
        }

        eventLog.setHR_UserID(Constants.HR_ID_PREFIX + "<" + Helper.getUserID(sh) + "@" + Helper.getOrganization(sh) + ">");
        eventLog.setHR_AlternativeUserID(Helper.getAlternateUserID(sh));
        eventLog.setHR_RoleID(Helper.getRoleID(sh));

        eventLog.setSP_UserID(HTTPUtil.getSubjectDN(true));

        eventLog.setPT_PatricipantObjectID(getDocumentEntryPatientId(request));

        eventLog.setAS_AuditSourceId(Constants.COUNTRY_PRINCIPAL_SUBDIVISION);

        if (response.getRegistryErrorList() != null) {
            RegistryError re = response.getRegistryErrorList()
                    .getRegistryError().get(0);
            eventLog.setEM_PatricipantObjectID(re.getErrorCode());
            eventLog.setEM_PatricipantObjectDetail(re.getCodeContext()
                    .getBytes());
        }

        logger.debug("Event log prepared");
    }

    /**
     * Prepare audit log for the HCER service, put() operation
     *
     * @throws DatatypeConfigurationException
     */ /* HCER audit not wanted?
     public void prepareEventLogForHCERPut(EventLog eventLog, ProvideAndRegisterDocumentSetRequestType request,
     RegistryResponseType response, Element sh) throws DatatypeConfigurationException {
     eventLog.setEventType(EventType.epsosHCERServicePut);
     eventLog.setEI_TransactionName(TransactionName.epsosHCERServicePut);
     eventLog.setEI_EventActionCode(EventActionCode.READ);
     eventLog.setEI_EventDateTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));

     if (request.getSubmitObjectsRequest().getRegistryObjectList() != null) {
     for (int i = 0; i < request.getSubmitObjectsRequest()
     .getRegistryObjectList().getIdentifiable().size(); i++) {
     if (!(request.getSubmitObjectsRequest().getRegistryObjectList()
     .getIdentifiable().get(i).getValue() instanceof ExtrinsicObjectType)) {
     continue;
     }
     ExtrinsicObjectType eot = (ExtrinsicObjectType) request
     .getSubmitObjectsRequest().getRegistryObjectList()
     .getIdentifiable().get(i).getValue();
     String documentId = "";
     for (ExternalIdentifierType eit : eot.getExternalIdentifier()) {
     if (eit.getIdentificationScheme().equals(
     "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab")) {
     documentId = eit.getValue();
     }
     }
     eventLog.setET_ObjectID(documentId);
     break;
     }
     }

     if (response.getRegistryErrorList() != null) {
     eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.PERMANENT_FAILURE);
     } else {
     eventLog.setEI_EventOutcomeIndicator(EventOutcomeIndicator.FULL_SUCCESS);
     }

     eventLog.setHR_UserID(Helper.getUserID(sh));
     eventLog.setHR_AlternativeUserID(Helper.getAlternateUserID(sh));
     eventLog.setHR_RoleID(Helper.getRoleID(sh));

     eventLog.setSP_UserID(HTTPUtil.getSubjectDN(true));

     eventLog.setPS_PatricipantObjectID(getDocumentEntryPatientId(request));

     eventLog.setAS_AuditSourceId(Constants.COUNTRY_PRINCIPAL_SUBDIVISION);

     if (response.getRegistryErrorList() != null) {
     RegistryError re = response.getRegistryErrorList()
     .getRegistryError().get(0);
     eventLog.setEM_PatricipantObjectID(re.getErrorCode());
     eventLog.setEM_PatricipantObjectDetail(re.getCodeContext()
     .getBytes());
     }

     logger.debug("Event log prepared");
     }
     */
    private String getDocumentEntryPatientId(
            ProvideAndRegisterDocumentSetRequestType request) {
        String patientId = "";
        // Traverse all ExtrinsicObjects
        for (int i = 0; i < request.getSubmitObjectsRequest()
                .getRegistryObjectList().getIdentifiable().size(); i++) {
            if (!(request.getSubmitObjectsRequest().getRegistryObjectList()
                    .getIdentifiable().get(i).getValue() instanceof ExtrinsicObjectType)) {
                continue;
            }
            ExtrinsicObjectType eot = (ExtrinsicObjectType) request
                    .getSubmitObjectsRequest().getRegistryObjectList()
                    .getIdentifiable().get(i).getValue();
            // Traverse all Classification blocks in the ExtrinsicObject
            // selected
            for (int j = 0; j < eot.getSlot().size(); j++) {
                // Search for the slot with the name "sourcePatientId"
                if (eot.getSlot().get(j).getName().equals("sourcePatientId")) {
                    patientId = eot.getSlot().get(j).getValueList().getValue()
                            .get(0);
                    return patientId;
                }
            }
        }
        logger.error("Could not locate the patient id of the XDR request.");
        return patientId;
    }

    private String getPatientId(ProvideAndRegisterDocumentSetRequestType request) {
        String patientId = getDocumentEntryPatientId(request);
        String nationalId = patientId.substring(0, patientId.indexOf("^^^"));
        return nationalId;
    }

    public RegistryResponseType saveDispensation(
            ProvideAndRegisterDocumentSetRequestType request, SOAPHeader sh,
            EventLog eventLog) throws Exception {
        RegistryResponseType response = new RegistryResponseType();
        String sigCountryCode = null;

        Element shElement;
        try {
            shElement = XMLUtils.toDOM(sh);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        documentSubmitService.setSOAPHeader(shElement);

        RegistryErrorList rel = ofRs.createRegistryErrorList();
        try {
            sigCountryCode = SAML2Validator.validateXDRHeader(shElement,
                    Constants.ED_CLASSCODE);
        } catch (InsufficientRightsException e) {
            rel.getRegistryError().add(createErrorMessage(e.getCode(), e.getMessage(), "", false));
        } catch (AssertionValidationException e) {
            rel.getRegistryError().add(createErrorMessage(e.getCode(), e.getMessage(), "", false));
        } catch (SMgrException e) {
            rel.getRegistryError().add(createErrorMessage("", e.getMessage(), "", false));
        }

        String documentId = request.getDocument().get(0).getId();
        String patientId = getPatientId(request);
        logger.info("Received an eDispensation document for patient "
                + patientId);
        String countryCode = "";
        String DN = eventLog.getSC_UserID();
        int cIndex = DN.indexOf("C=");

        if (cIndex > 0) {
            countryCode = DN.substring(cIndex + 2, cIndex + 4);
        } // Mustafa: This part is added for handling consents when the call is
        // not https
        // In this case, we check the country code of the signature certificate
        // that
        // ships within the HCP assertion
        // TODO: Might be necessary to remove later, although it does no harm in
        // reality!
        else {
            logger.info("Could not get client country code from the service consumer certificate. The reason can be that the call was not via HTTPS. Will check the country code from the signature certificate now.");
            if (sigCountryCode != null) {
                logger.info("Found the client country code via the signature certificate.");
                countryCode = sigCountryCode;
            }
        }
        logger.info("The client country code to be used by the PDP: "
                + countryCode);
        if (!SAML2Validator.isConsentGiven(patientId, countryCode)) {
            logger.debug("No consent given, throwing InsufficientRightsException");
            InsufficientRightsException e = new InsufficientRightsException(4701);
            rel.getRegistryError().add(createErrorMessage(e.getCode(), e.getMessage(), "", false));
        }
        if (rel.getRegistryError().size() > 0) {
            response.setRegistryErrorList(rel);
            response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        } else {
            try {
                shElement = XMLUtils.toDOM(sh);
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw e;
            }

            CdaValidationService cdaValidationService = CdaValidationService.getInstance();
            for (int i = 0; i < request.getDocument().size(); i++) {
                Document doc = request.getDocument().get(i);
                byte[] docBytes = doc.getValue();
                try {

                    /* Validate CDA epSOS Pivot */
                    cdaValidationService.validateModel(
                            new String(doc.getValue(), "UTF-8"),
                            CdaModel.obtainCdaModel(obtainClassCode(request), true),
                            NcpSide.NCP_A);

                    docBytes = TMServices.transformDocument(docBytes, Constants.LANGUAGE_CODE); //Resets the response document to a translated version.

                    /* Validate CDA epSOS Pivot */
                    cdaValidationService.validateModel(
                            new String(docBytes, "UTF-8"),
                            CdaModel.obtainCdaModel(obtainClassCode(request), true),
                            NcpSide.NCP_A);

                } catch (DocumentTransformationException ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                }
                try {
                    org.w3c.dom.Document domDocument = TMServices.byteToDocument(docBytes);
                    EPSOSDocument epsosDocument = DocumentFactory.createEPSOSDocument(patientId, Constants.ED_CLASSCODE, domDocument);
                    // call to NI
                    try {
                        EvidenceUtils.createEvidenceREMNRO(epsosDocument.toString(),
                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                                tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                                EventType.epsosDispensationServiceInitialize.getCode(),
                                new DateTime(),
                                EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                                "NI_XDR_DISP_REQ",
                                Helper.getDocumentEntryPatientIdFromTRCAssertion(shElement) + "__" + DateUtil.getCurrentTimeGMT());
                    } catch (Exception e) {
                        logger.error(ExceptionUtils.getStackTrace(e));
                    }
                    documentSubmitService.submitDispensation(epsosDocument);
                    try {
                        EvidenceUtils.createEvidenceREMNRR(epsosDocument.toString(),
                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                                tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                                EventType.epsosDispensationServiceInitialize.getCode(),
                                new DateTime(),
                                EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                                "NI_XDR_DISP_RES",
                                Helper.getDocumentEntryPatientIdFromTRCAssertion(shElement) + "__" + DateUtil.getCurrentTimeGMT());
                    } catch (Exception e) {
                        logger.error(ExceptionUtils.getStackTrace(e));
                    }
                } catch (NIException e) {
                    rel.getRegistryError().add(createErrorMessage(e.getCode(), e.getMessage(), "", false));
                } catch (Exception e) {
                    rel.getRegistryError().add(createErrorMessage("", e.getMessage(), "", false));
                }
            }
            if (rel.getRegistryError().size() > 0) {
                response.setRegistryErrorList(rel);
                response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            } else {
                response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success");
            }
        }
        prepareEventLogForDispensationInitialize(eventLog, request, response,
                shElement);
        return response;
    }

    public RegistryResponseType saveDocument(
            ProvideAndRegisterDocumentSetRequestType request, SOAPHeader sh,
            EventLog eventLog) throws Exception {

        // Traverse all ExtrinsicObjects
        for (int i = 0; i < request.getSubmitObjectsRequest()
                .getRegistryObjectList().getIdentifiable().size(); i++) {
            if (!(request.getSubmitObjectsRequest().getRegistryObjectList()
                    .getIdentifiable().get(i).getValue() instanceof ExtrinsicObjectType)) {
                continue;
            }
            ExtrinsicObjectType eot = (ExtrinsicObjectType) request
                    .getSubmitObjectsRequest().getRegistryObjectList()
                    .getIdentifiable().get(i).getValue();
            // Traverse all Classification blocks in the ExtrinsicObject
            // selected
            for (int j = 0; j < eot.getClassification().size(); j++) {
                // If classified as eDispensation, process as eDispensation
                if (eot.getClassification().get(j).getNodeRepresentation()
                        .compareTo(Constants.ED_CLASSCODE) == 0) {
                    logger.info("Processing an eDispensation message");

                    return saveDispensation(request, sh, eventLog);

                } // TODO: check the right LOINC code, currently coded as in
                // example 3.4.2 ver. 2.2 p. 82
                else if (eot.getClassification().get(j).getNodeRepresentation()
                        .compareTo(Constants.CONSENT_CLASSCODE) == 0) {
                    logger.info("Processing a consent");
                    return saveConsent(request, sh, eventLog);
                } else if (eot.getClassification().get(j).getNodeRepresentation()
                        .equals(Constants.HCER_CLASSCODE)) {
                    logger.info("Processing HCER document");
                    return saveHCER(request, sh, eventLog);
                }
            }
            break;
        }

        return reportDocumentTypeError(request);
    }

    public RegistryResponseType saveConsent(
            ProvideAndRegisterDocumentSetRequestType request, SOAPHeader sh,
            EventLog eventLog) {
        RegistryResponseType response = new RegistryResponseType();
        String sigCountryCode = null;

        Element shElement = null;
        try {
            shElement = XMLUtils.toDOM(sh);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        documentSubmitService.setSOAPHeader(shElement);

        RegistryErrorList rel = ofRs.createRegistryErrorList();
        try {
            sigCountryCode = SAML2Validator.validateXDRHeader(shElement,
                    Constants.CONSENT_CLASSCODE);
        } catch (InsufficientRightsException e) {
            rel.getRegistryError().add(createErrorMessage(e.getCode(), e.getMessage(), "", false));
        } catch (AssertionValidationException e) {
            rel.getRegistryError().add(createErrorMessage(e.getCode(), e.getMessage(), "", false));
        } catch (SMgrException e) {
            rel.getRegistryError().add(createErrorMessage("", e.getMessage(), "", false));
        }

        String documentId = request.getDocument().get(0).getId();
        String patientId = getPatientId(request);
        logger.info("Received a consent document for patient " + patientId);
        /*
         * Here PDP checks and related calls are skipped, necessary checks to be
         * performed in the NI while processing the consent document.
         */
        try {
            shElement = XMLUtils.toDOM(sh);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        CdaValidationService cdaValidationService = CdaValidationService.getInstance();
        for (int i = 0; i < request.getDocument().size(); i++) {
            Document doc = request.getDocument().get(i);
            byte[] docBytes = doc.getValue();
            try {

                /* Validate CDA epSOS Pivot */
                cdaValidationService.validateModel(
                        new String(doc.getValue(), "UTF-8"),
                        CdaModel.obtainCdaModel(obtainClassCode(request), true),
                        NcpSide.NCP_A);

                docBytes = TMServices.transformDocument(docBytes, Constants.LANGUAGE_CODE); //Resets the response document to a translated version.

                /* Validate CDA epSOS Pivot */
                cdaValidationService.validateModel(
                        new String(docBytes, "UTF-8"),
                        CdaModel.obtainCdaModel(obtainClassCode(request), true),
                        NcpSide.NCP_A);

            } catch (DocumentTransformationException ex) {
                logger.error(ex.getLocalizedMessage(), ex);
            } catch (Exception ex) {
                logger.error(null, ex);
            }
            try {
                org.w3c.dom.Document domDocument = TMServices.byteToDocument(docBytes);
                EPSOSDocument epsosDocument = DocumentFactory.createEPSOSDocument(patientId, Constants.CONSENT_CLASSCODE, domDocument);

                // call to NI
                try {
                    EvidenceUtils.createEvidenceREMNRO(epsosDocument.toString(),
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                            EventType.epsosConsentServicePut.getCode(),
                            new DateTime(),
                            EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                            "NI_XDR_CONSENT_REQ",
                            Helper.getDocumentEntryPatientIdFromTRCAssertion(shElement) + "__" + DateUtil.getCurrentTimeGMT());
                } catch (Exception e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }

                documentSubmitService.submitPatientConsent(epsosDocument);
                // call to NI
                try {
                    EvidenceUtils.createEvidenceREMNRR(epsosDocument.toString(),
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                            EventType.epsosConsentServicePut.getCode(),
                            new DateTime(),
                            EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                            "NI_XDR_CONSENT_RES",
                            Helper.getDocumentEntryPatientIdFromTRCAssertion(shElement) + "__" + DateUtil.getCurrentTimeGMT());
                } catch (Exception e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }

            } catch (DocumentProcessingException e) {
                rel.getRegistryError().add(createErrorMessage(e.getCode(), e.getMessage(), "", false));
            } catch (Exception e) {
                rel.getRegistryError().add(createErrorMessage("", e.getMessage(), "", false));
            }
        }
        if (rel.getRegistryError().size() > 0) {
            response.setRegistryErrorList(rel);
            response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        } else {
            response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success");
        }

        try {
            prepareEventLogForConsentPut(eventLog, request, response, shElement);
        } catch (Exception ex) {
            logger.error(null, ex);
            // Is this fatal?
        }
        return response;
    }

    protected String validateXDRHeader(Element sh, String classCode) throws MissingFieldException, InvalidFieldException, SMgrException, InsufficientRightsException {
        return SAML2Validator.validateXDRHeader(sh, classCode);
    }

    public RegistryResponseType saveHCER(ProvideAndRegisterDocumentSetRequestType request, SOAPHeader sh, EventLog eventLog) {
        RegistryResponseType response = new RegistryResponseType();
        String sigCountryCode = null;

        Element shElement = null;
        try {
            shElement = XMLUtils.toDOM(sh);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        documentSubmitService.setSOAPHeader(shElement);

        RegistryErrorList rel = ofRs.createRegistryErrorList();
        try {
            sigCountryCode = validateXDRHeader(shElement, Constants.HCER_CLASSCODE);
        } catch (InsufficientRightsException e) {
            rel.getRegistryError().add(createErrorMessage(e.getCode(), e.getMessage(), "", false));
        } catch (AssertionValidationException e) {
            rel.getRegistryError().add(createErrorMessage(e.getCode(), e.getMessage(), "", false));
        } catch (SMgrException e) {
            rel.getRegistryError().add(createErrorMessage("", e.getMessage(), "", false));
        }

        String documentId = request.getDocument().get(0).getId();
        String patientId = getPatientId(request);
        logger.info("Received a HCER document for patient " + patientId);
        /*
         * Here PDP checks and related calls are skipped, necessary checks to be
         * performed in the NI while processing the consent document.
         */

        CdaValidationService cdaValidationService = CdaValidationService.getInstance();
        for (int i = 0; i < request.getDocument().size(); i++) {
            Document doc = request.getDocument().get(i);

            try {
                /* Validate CDA epSOS Pivot */
                cdaValidationService.validateModel(
                        new String(doc.getValue(), "UTF-8"),
                        CdaModel.obtainCdaModel(obtainClassCode(request), true),
                        NcpSide.NCP_A);

                String documentString = new String(TMServices.transformDocument(doc.getValue(), Constants.LANGUAGE_CODE), "UTF-8");

                /* Validate CDA epSOS Pivot */
                cdaValidationService.validateModel(
                        documentString,
                        CdaModel.obtainCdaModel(obtainClassCode(request), true),
                        NcpSide.NCP_A);

                org.w3c.dom.Document domDocument = XMLUtil.parseContent(documentString);
                EPSOSDocument epsosDocument = DocumentFactory.createEPSOSDocument(patientId, Constants.HCER_CLASSCODE, domDocument);
                documentSubmitService.submitHCER(epsosDocument);
            } catch (DocumentProcessingException e) {
                rel.getRegistryError().add(createErrorMessage(e.getCode(), e.getMessage(), "", false));
            } catch (DocumentTransformationException e) {
                rel.getRegistryError().add(createErrorMessage(e.getErrorCode(), e.getCodeContext(), e.getMessage(), false));
            } catch (Exception e) {
                rel.getRegistryError().add(createErrorMessage("", e.getMessage(), "", false));
            }
        }
        if (rel.getRegistryError().size() > 0) {
            response.setRegistryErrorList(rel);
            response.setStatus(IheConstants.REGREP_RESPONSE_FAILURE);
        } else {
            response.setStatus(IheConstants.REGREP_RESPONSE_SUCCESS);
        }
        /* HCER audit not wanted?
         try {
         prepareEventLogForHCERPut(eventLog, request, response, shElement);
         } catch (Exception ex) {
         logger.error(ex);
         // Is this fatal?
         }
         */
        return response;
    }

    public RegistryResponseType reportDocumentTypeError(
            ProvideAndRegisterDocumentSetRequestType request) {
        RegistryResponseType response = new RegistryResponseType();

        response.setRegistryErrorList(new RegistryErrorList());

        RegistryError error = new RegistryError();
        error.setErrorCode("4202");
        error.setCodeContext("Unknown document");
        response.getRegistryErrorList().getRegistryError().add(error);
        response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");

        return response;
    }

    /**
     * This method will extract the document class code from a given
     * ProvideAndRegisterDocumentSetRequestType message.
     *
     * @param request the request containing the class code.
     * @return the class code.
     */
    private String obtainClassCode(final ProvideAndRegisterDocumentSetRequestType request) {

        if (request == null) {
            logger.error("The provided request message in order to extract the classcode is null.");
            return null;
        }

        final String CLASS_SCHEME = "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";
        String result = "";

        for (int i = 0; i < request.getSubmitObjectsRequest()
                .getRegistryObjectList().getIdentifiable().size(); i++) {
            if (!(request.getSubmitObjectsRequest().getRegistryObjectList()
                    .getIdentifiable().get(i).getValue() instanceof ExtrinsicObjectType)) {
                continue;
            }
            ExtrinsicObjectType eot = (ExtrinsicObjectType) request
                    .getSubmitObjectsRequest().getRegistryObjectList()
                    .getIdentifiable().get(i).getValue();
            for (int j = 0; j < eot.getClassification().size(); j++) {
                if (eot.getClassification().get(j).getClassificationScheme().equals(CLASS_SCHEME)) {
                    result = eot.getClassification().get(j).getNodeRepresentation();
                    break;
                }
            }
        }

        if (result.isEmpty()) {
            logger.warn("No class code was found in request object.");
        }
        return result;
    }

    private DocumentProcessingException normalizeDocProcException(DocumentProcessingException dPe) {

        final String REPOSITORY_INTERNAL_ERROR = "XDSRepositoryError";
        DocumentProcessingException result = new DocumentProcessingException();

        if (dPe.getMessage().equals("DOCUMENT TRANSLATION FAILED.")) {
            result.setCode(REPOSITORY_INTERNAL_ERROR);
            result.setCodeSystem(null);
            result.setMessage("An error has occurred during the document translation.");
            result.setStackTrace(dPe.getStackTrace());
        }

        return result;
    }
}
