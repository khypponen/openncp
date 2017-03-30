package epsos.ccd.posam.tm.service.impl;

import epsos.ccd.gnomon.auditmanager.*;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.ccd.posam.tm.exception.TMError;
import epsos.ccd.posam.tm.exception.TMException;
import epsos.ccd.posam.tm.exception.TmErrorCtx;
import epsos.ccd.posam.tm.response.TMResponseStructure;
import epsos.ccd.posam.tm.service.ITransformationService;
import epsos.ccd.posam.tm.util.*;
import epsos.ccd.posam.tsam.exception.ITMTSAMEror;
import epsos.ccd.posam.tsam.response.TSAMResponseStructure;
import epsos.ccd.posam.tsam.service.ITerminologyService;
import epsos.ccd.posam.tsam.util.CodedElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.http.HTTPUtil;

import javax.xml.datatype.DatatypeFactory;
import java.util.*;

/**
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.25, 2010, 20 October
 * @see ITransformationService
 */
public class TransformationService implements ITransformationService,
        TMConstants, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(TransformationService.class);

    private ITerminologyService tsamApi = null;
    private HashMap<String, String> level1Type;
    private HashMap<String, String> level3Type;
    private TMConfiguration config;

    public TMResponseStructure toEpSOSPivot(Document epSOSOriginalData) {
        log.debug("toEpsosPivot BEGIN");
        TMResponseStructure responseStructure = process(epSOSOriginalData,
                null, true);
        log.debug("toEpsosPivot END");
        return responseStructure;
    }

    /**
     * Method checks for CDA document code and body and returns constant
     * determining Document type (PatientSummary, ePrescription, eDispensation)
     * and level of CDA document (1 - unstructured, or 3 - structured)
     *
     * @param document input CDA document
     * @return Constant which determines one of six Document types
     * @throws TMException
     * @throws Exception
     */
    public String getCDADocumentType(Document document) throws TMException,
            Exception {

//		XPath xpath = XPathFactory.newInstance().newXPath();
//		XPathExpression expr = xpath.compile(XPATH_CLINICALDOCUMENT_CODE);
//		NodeList nodeList = (NodeList) expr.evaluate(document,
//				XPathConstants.NODESET);
        List<Node> nodeList = XmlUtil.getNodeList(document, XPATH_CLINICALDOCUMENT_CODE);

        // Document type code
        String docTypeCode = null;
        // exactly 1 document type element should exist
        if (nodeList.size() == 1) {
            Element docTypeCodeElement = (Element) nodeList.get(0);
            docTypeCode = docTypeCodeElement.getAttribute(CODE);
            log.debug("docTypeCode: " + docTypeCode);
            if (docTypeCode == null || docTypeCode.length() == 0) {
                throw new TMException(TMError.ERROR_DOCUMENT_CODE_NOT_EXIST);
            } /*else if (!TMConfiguration.getInstance().getDocTypesCollection()
             .contains(docTypeCode)) {
             throw new TMException(TMError.ERROR_DOCUMENT_CODE_UNKNOWN);
             }*/
        } else {
            log
                    .error("Problem obtaining document type code ! found /ClinicalDocument/code elements:"
                            + nodeList.size());
            throw new TMException(TMError.ERROR_DOCUMENT_CODE_NOT_EXIST);
        }

        // Document level (1 - unstructured or 3 - structured)
        boolean level3Doc = false;
        // check if structuredDocument
//		XPathExpression expr2 = xpath.compile(XPATH_STRUCTUREDBODY);
//		Node nodeStructuredBody = (Node) expr2.evaluate(document,
//				XPathConstants.NODE);
        Node nodeStructuredBody = XmlUtil.getNode(document, XPATH_STRUCTUREDBODY);
        ;
        if (nodeStructuredBody != null) {
            log.debug("Found - " + XPATH_STRUCTUREDBODY);
            // LEVEL 3 document
            level3Doc = true;
        } else {
            // check if unstructured document
            log.debug("Not Found  - " + XPATH_STRUCTUREDBODY);
//			XPathExpression expr3 = xpath.compile(XPATH_NONXMLBODY);
//			Node nodeNonXMLBody = (Node) expr3.evaluate(document,
//					XPathConstants.NODE);
            Node nodeNonXMLBody = XmlUtil.getNode(document, XPATH_NONXMLBODY);
            if (nodeNonXMLBody != null) {
                log.debug("Found - " + NON_XML_BODY);
                // LEVEL 1 document
                level3Doc = false;
            } else {
                // NO BODY - Document will be processed as LEVEL 1
                level3Doc = false;
            }
        }

        String docTypeConstant = null;

        // find constant for Document type
        if (level3Doc) {
            docTypeConstant = level3Type.get(docTypeCode);
        } else {
            docTypeConstant = level1Type.get(docTypeCode);
        }

        if (docTypeConstant == null) {
            throw new TMException(new TmErrorCtx(TMError.ERROR_DOCUMENT_CODE_UNKNOWN, docTypeCode));
        }

        log.debug("docTypeConstant is: " + docTypeConstant);
        return docTypeConstant;
    }

    public TMResponseStructure translate(Document epSosCDA,
                                         String targetLanguageCode) {
        log.debug("translate BEGIN");
        TMResponseStructure responseStructure = process(epSosCDA,
                targetLanguageCode, false);
        log.debug("translate END");
        return responseStructure;
    }

    private TMResponseStructure process(Document inputDocument,
                                        String targetLanguageCode, boolean isTranscode) {
        TMResponseStructure responseStructure = null;
        String status;
        List<ITMTSAMEror> errors = new ArrayList<>();
        List<ITMTSAMEror> warnings = new ArrayList<>();
        byte[] inputDocbytes = null;

        try {
            if (inputDocument == null) {
                status = STATUS_FAILURE;
                errors.add(TMError.ERROR_NULL_INPUT_DOCUMENT);
                responseStructure = new TMResponseStructure(inputDocument,
                        status, errors, warnings);
                log.error("Error, null input document!");
                return responseStructure;
            } else {
                // validate schema
                inputDocbytes = XmlUtil.doc2bytes(inputDocument);
                Document namespaceAwareDoc = XmlUtil
                        .getNamespaceAwareDocument(inputDocbytes);

                // check document type
                // check if document is structured or unstructured
                Document namespaceNotAwareDoc = inputDocument;
                String cdaDocumentType = getCDADocumentType(namespaceNotAwareDoc);

                boolean schemaValid = Validator.validateToSchema(namespaceAwareDoc);

                // mda validation
                if (config.isModelValidationEnabled()) {
                    boolean validateFriendly = isTranscode ? true : false;
                    ModelValidatorResult validateMDA = Validator.validateMDA(
                            new String(inputDocbytes), cdaDocumentType,
                            validateFriendly);

                    if (!validateMDA.isSchemaValid()) {
                        warnings.add(TMError.WARNING_INPUT_XSD_VALIDATION_FAILED);
                    }

                    if (!validateMDA.isModelValid()) {
                        warnings.add(TMError.WARNING_OUTPUT_MDA_VALIDATION_FAILED);
                    }

                } else {
                    // if model validation is enabled schema validation is done as part
                    // of it so there is no point doing it again
                    if (!schemaValid) {
                        status = STATUS_FAILURE;
                        warnings.add(TMError.WARNING_INPUT_XSD_VALIDATION_FAILED);
                    }
                }

                // validate INPUT (schematron)
                if (config
                        .isSchematronValidationEnabled()) {
                    // if transcoding, validate against friendly scheme,
                    // else against pivot scheme
                    boolean validateFriendly = isTranscode ? true : false;
                    SchematronResult result = Validator.validateSchematron(
                            inputDocument, cdaDocumentType, validateFriendly);
                    if (result == null || !result.isValid()) {
                        status = STATUS_FAILURE;
                        warnings.add(TMError.WARNING_INPUT_SCHEMATRON_VALIDATION_FAILED);
                        log.error("Schematron validation error, input document is invalid!");
                        log.error(result.toString());
                    }
                } else {
                    log.info("Schematron validation disabled");
                }

                // transcode/translate document
                status = isTranscode ? transcodeDocument(namespaceNotAwareDoc,
                        errors, warnings, cdaDocumentType) : translateDocument(
                        namespaceNotAwareDoc, targetLanguageCode, errors,
                        warnings, cdaDocumentType);

                Document finalDoc = XmlUtil
                        .removeEmptyXmlns(namespaceNotAwareDoc);

                if (config.isModelValidationEnabled()) {
                    ModelValidatorResult validateMDA = Validator.validateMDA(
                            XmlUtil.xmlToString(finalDoc), cdaDocumentType,
                            !isTranscode);
                    if (!validateMDA.isSchemaValid()) {
                        warnings.add(TMError.WARNING_OUTPUT_XSD_VALIDATION_FAILED);
                    }
                    if (!validateMDA.isModelValid()) {
                        warnings.add(TMError.WARNING_OUTPUT_MDA_VALIDATION_FAILED);
                    }

                }
                // validate RESULT (schematron)
                if (config
                        .isSchematronValidationEnabled()) {
                    SchematronResult result = Validator.validateSchematron(
                            finalDoc, cdaDocumentType, !isTranscode);
                    if (result == null || !result.isValid()) {
                        status = STATUS_FAILURE;
                        warnings.add(TMError.WARNING_OUTPUT_SCHEMATRON_VALIDATION_FAILED);
                        responseStructure = new TMResponseStructure(finalDoc,
                                status, errors, warnings);
                        log.error("Schematron validation error, result document is invalid!");
                        log.error(result.toString());
                        return responseStructure;
                    }
                } else {
                    log.debug("Schematron validation disabled");
                }

                // create & fill TMResponseStructure
                responseStructure = new TMResponseStructure(finalDoc, status,
                        errors, warnings);
                if (log.isInfoEnabled()) {
                    log.info("TM result:\n" + responseStructure.toString());
                }
            }
        } catch (TMException e) {
            status = STATUS_FAILURE;
            errors.add(e.getReason());
            responseStructure = new TMResponseStructure(inputDocument, status,
                    errors, warnings);
            // log ERROR
            log.error(e.getReason().toString(), e);
        } catch (Exception e) {
            // write ERROR to ResponseStructure
            status = STATUS_FAILURE;
            errors.add(TMError.ERROR_PROCESSING_ERROR);
            responseStructure = new TMResponseStructure(inputDocument, status,
                    errors, warnings);
            // log ERROR
            log.error(TMError.ERROR_PROCESSING_ERROR.toString(), e);
        }

        // audit log
        writeAuditTrail(responseStructure);

        return responseStructure;
    }

    public List<String> getLtrLanguages() {
        return tsamApi.getLtrLanguages();
    }

    private void writeAuditTrail(TMResponseStructure responseStructure) {
        if (config.isAuditTrailEnabled()) {
            log.debug("Audit trail BEGIN");
            try {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                // TODO this is not final/correct version
                EventLog logg = EventLog.createEventLogPivotTranslation(
                        //config.getAuditTrailTransactionNumber(),// The number of transaction including the epsos- prefix
                        TransactionName.epsosPivotTranslation, // Possible values according to D4.5.6 are E,R,U,D
                        EventActionCode.EXECUTE,
                        DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar),
                        (responseStructure != null && responseStructure.getStatus().equals(STATUS_SUCCESS) ? EventOutcomeIndicator.FULL_SUCCESS : EventOutcomeIndicator.PERMANENT_FAILURE),
                        HTTPUtil.getSubjectDN(false), // The string encoded CN of the TLS certificate of the NCP triggered the epsos operation
                        getOIDFromDocument(responseStructure.getDocument()), // Identifier that allows to univocally identify the SOURCE document or source data entries. (UUID Format)
                        getOIDFromDocument(responseStructure.getResponseCDA()), // Identifier that allows to univocally identify the TARGET document. (UUID Format)
                        Constants.UUID_PREFIX + "", // The value MUST contain the base64 encoded security header
                        new byte[0], // ReqM_PatricipantObjectDetail - The value MUST contain the base64 encoded security header
                        Constants.UUID_PREFIX + "", // String-encoded UUID of the response message
                        new byte[0], // ResM_PatricipantObjectDetail - The value MUST contain the base64 encoded security header
                        ConfigurationManagerService.getInstance().getProperty("SERVER_IP") // The IP Address of the target Gateway
                );
                logg.setEventType(EventType.epsosPivotTranslation);

                new AuditService().write(
                        logg,
                        config.getAuditTrailFacility(), // the facility number according to log4j
                        config.getAuditTrailSeverity());// the severity of the message
            } catch (Exception e) {
                log.error("Audit trial ERROR! ", e);
            }
            log.debug("Audit trail END");
        } else {
            log.debug("Audit trail DISABLED");
        }

    }

    /**
     * Method iterates document for coded elements, calls for each
     * TSAM.getEpSOSConceptByCode method, Input document is enriched with
     * translation elements (transcoded Concept), list of errors & warnings is
     * filled, finally status of operation is returned
     *
     * @param document        original CDA document
     * @param errors          empty list for TMErrors
     * @param warnings        empty list for TMWarnings
     * @param cdaDocumentType
     * @return String - final status of transcoding
     */
    private String transcodeDocument(Document document,
                                     List<ITMTSAMEror> errors, List<ITMTSAMEror> warnings,
                                     String cdaDocumentType) throws TMException, Exception {
        return processDocument(document, null, errors, warnings,
                cdaDocumentType, true);
    }

    /**
     * Search textContent for referenced values and replace it with
     * transcoded/translated displayName for each key (reference id)
     *
     * @param document
     * @param hmReffIdDisplayName
     * @param process
     */
    @SuppressWarnings("unused")
    @Deprecated
    private void replaceReferencedValues(Document document,
                                         HashMap<String, String> hmReffIdDisplayName, String process) {
        // just log referenced keys and displayNames
        logReferences(hmReffIdDisplayName, process);
        log.debug("replaceReferencedValues BEGIN");
        try {
            // find elements with ID attribute which id=remembered reference key
            // and
            // replace their textContent
            List<Node> idElements = XmlUtil.getNodeList(document,
                    XPATH_ALL_ELEMENTS_WITH_ID_ATTR);
            String id = null;
            for (int i = 0; i < idElements.size(); i++) {
                Element idElement = (Element) idElements.get(i);
                id = idElement.getAttribute(ID);
                if (notEmpty(id) && hmReffIdDisplayName.containsKey(id)
                        && idElement.getChildNodes().getLength() == 1) {
                    log.debug("replaced id: " + id + " "
                            + idElement.getTextContent() + " -> "
                            + hmReffIdDisplayName.get(id));
                    idElement.setTextContent(hmReffIdDisplayName.get(id));
                }
            }
            log.debug("replaceReferencedValues END");
        } catch (Exception e) {
            log.error("replaceReferencedValues error: ", e);
        }
    }

    /**
     * Just logs referenced keys and displayNames
     *
     * @param hmReffIdDisplayName
     * @param process
     */
    private void logReferences(HashMap<String, String> hmReffIdDisplayName,
                               String process) {
        if (hmReffIdDisplayName != null && !hmReffIdDisplayName.isEmpty()) {
            Iterator<String> iKeys = hmReffIdDisplayName.keySet().iterator();
            log.debug("List (" + process + ") - Reference id : displayName - ");
            while (iKeys.hasNext()) {
                String key = (String) iKeys.next();
                log.debug(EMPTY_STRING + key + COLON
                        + hmReffIdDisplayName.get(key));
            }
        }
    }

    /**
     * Calls TSAM.getEpSOSConceptByCode method, if transcoding is succesful,
     * constructs translation element for original data, new/transcoded data are
     * placed in original element.
     *
     * @param originalElement     - transcoded Coded Element
     * @param document            - input CDA document
     * @param hmReffIdDisplayName hashMap for ID of referencedValues and
     *                            transcoded DisplayNames
     * @param warnings
     * @param errors
     * @return boolean - true if SUCCES otherwise false
     */
    private boolean transcodeElement(Element originalElement,
                                     Document document, HashMap<String, String> hmReffIdDisplayName,
                                     String valueSet, String valueSetVersion, List<ITMTSAMEror> errors,
                                     List<ITMTSAMEror> warnings) {
        return processElement(originalElement, document, null,
                hmReffIdDisplayName, valueSet, valueSetVersion, true, errors,
                warnings);
    }

    /**
     * Checks if element contains originalText with reference value, if yes, remember displayName for this reference id.
     *
     * @param hmReffIdDisplayName
     * @param displayName
     * @param codedElement
     */
    @SuppressWarnings("unused")
    private void checkRememberReference(
            HashMap<String, String> hmReffIdDisplayName, String displayName,
            Element codedElement) {
        try {
            // find Elements with tagname "originalText"
            NodeList texts = codedElement.getElementsByTagName(TMConstants.ORIGINAL_TEXT);
            for (int i = 0; i < texts.getLength(); i++) {
                Element text = (Element) texts.item(i);
                // find Element with tagname "reference"
                NodeList references = text.getElementsByTagName(TMConstants.REFERENCE);
                if (references.getLength() > 0) {
                    Element reference = (Element) references.item(0);
                    String key = reference.getAttribute(VALUE);
                    if (notEmpty(key)) {
                        key = key.replace(HASH, EMPTY_STRING);
                        if (!hmReffIdDisplayName.containsKey(key)) {
                            hmReffIdDisplayName.put(key, displayName);
                            log.debug("remembered @reference= " + key + "|"
                                    + displayName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("checkRememberReference error: ", e);
        }
    }

    /**
     * Method iterates document for translated coded elements, calls for each
     * TSAM.getDesignationByEpSOSConcept method, Input document is enriched with
     * translation elements (translated Concept), list of errors & warnings is
     * filled, finally status of operation is returned
     *
     * @param document           - translated CDA document
     * @param targetLanguageCode - language Code
     * @param errors             empty list for TMErrors
     * @param warnings           empty list for TMWarnings
     * @return String - final status of transcoding
     */
    private String translateDocument(Document document,
                                     String targetLanguageCode, List<ITMTSAMEror> errors,
                                     List<ITMTSAMEror> warnings, String cdaDocumentType)
            throws Exception {
        return processDocument(document, targetLanguageCode, errors, warnings,
                cdaDocumentType, false);
    }

    private String processDocument(Document document,
                                   String targetLanguageCode, List<ITMTSAMEror> errors,
                                   List<ITMTSAMEror> warnings, String cdaDocumentType,
                                   boolean isTranscode) throws Exception {
        boolean processingOK = true;
        // hashMap for ID of referencedValues and transcoded/translated
        // DisplayNames
        HashMap<String, String> hmReffId_DisplayName = new HashMap<String, String>();
        boolean isProcessingSuccesful;

        if (CodedElementList.getInstance()
                .isConfigurableElementIdentification()) {
            Collection<CodedElementListItem> ceList = CodedElementList
                    .getInstance().getList(cdaDocumentType);
            log
                    .info("Configurable Element Identification is set, CodedElementList for "
                            + cdaDocumentType
                            + " contains elements: "
                            + ceList.size());
            if (ceList == null || ceList.isEmpty()) {
                warnings.add(TMError.WARNING_CODED_ELEMENT_LIST_EMPTY);
                return STATUS_SUCCESS;
            }
            Iterator<CodedElementListItem> iProcessed = ceList.iterator();
            CodedElementListItem codedElementListItem;
            String xPathExpression;
            List<Node> nodeList;
            boolean isRequired;
            String celTargetLanguageCode = null;
            boolean useCELTargetLanguageCode = false;
            while (iProcessed.hasNext()) {
                codedElementListItem = (CodedElementListItem) iProcessed.next();
                log.debug("Looking for: " + codedElementListItem.toString());
                xPathExpression = codedElementListItem.getxPath();

                isRequired = codedElementListItem.isRequired();
                if (!isTranscode) {
                    celTargetLanguageCode = codedElementListItem
                            .getTargetLanguageCode();

                    // if targetLanguageCode is specified in CodedElementList,
                    // this
                    // is used for translation
                    if (celTargetLanguageCode != null
                            && celTargetLanguageCode.length() > 1) {
                        useCELTargetLanguageCode = true;
                    } else {
                        useCELTargetLanguageCode = false;
                    }
                }

                nodeList = XmlUtil.getNodeList(document, xPathExpression);
                log.debug("Found: "
                        + (nodeList == null ? "NULL" : nodeList.size())
                        + " elements");
                if (isRequired
                        && (nodeList == null || nodeList.size() == 0)) {
                    log.error("Required element is missing: "
                            + codedElementListItem.toString());
                    processingOK = false;
                    errors.add(new TmErrorCtx(TMError.ERROR_REQUIRED_CODED_ELEMENT_MISSING, codedElementListItem.toString()));
                } else {
                    Element originalElement;
                    for (int i = 0; i < nodeList.size(); i++) {
                        // iterate elements for processing
                        originalElement = (Element) nodeList.get(i);

                        // check if xsi:type is "CE" or "CD"
                        checkType(originalElement, warnings);

                        // call tsam transcode/translate method for each coded
                        // element
                        isProcessingSuccesful = (isTranscode ? transcodeElement(
                                originalElement, document, hmReffId_DisplayName, null,
                                null, errors, warnings) : translateElement(
                                originalElement, document, targetLanguageCode,
                                hmReffId_DisplayName, null, null, errors, warnings));

                        // if is required & processing is unsuccesful,
                        // report ERROR
                        if (isRequired && !isProcessingSuccesful) {
                            processingOK = false;
                            String ctx = XmlUtil.getElementPath(originalElement);
                            errors
                                    .add(isTranscode ? new TmErrorCtx(TMError.ERROR_REQUIRED_CODED_ELEMENT_NOT_TRANSCODED, ctx)
                                            : new TmErrorCtx(TMError.ERROR_REQUIRED_CODED_ELEMENT_NOT_TRANSLATED, ctx));
                            log.error("Required coded element was not translated");
                        }
                    }
                }
            }
        } else {
            log
                    .info("Configurable Element Identification is NOT set - looking for //*[@code] elements");
            // NodeList nodeList = epSosCDA.getElementsByTagName("code");
            List<Node> nodeList = XmlUtil.getNodeList(document,
                    XPATH_ALL_ELEMENTS_WITH_CODE_ATTR);
            log.info("Found " + nodeList.size() + " elements to translate/transcode");
            Element originalElement;
            for (int i = 0; i < nodeList.size(); i++) {
                // iterate elements for translation
                originalElement = (Element) nodeList.get(i);
                // if element name is translation, dont don't do anything
                if (TRANSLATION.equals(originalElement.getLocalName())) {

                    CodedElement ce = new CodedElement(originalElement);
                    log.debug("translation element - skipping: " + ce.toString());
                    continue;
                }
                // check if xsi:type is "CE" or "CD"
                checkType(originalElement, warnings);

                // call tsam transcode/translate method for each coded element
                isProcessingSuccesful = (isTranscode ? transcodeElement(
                        originalElement, document, hmReffId_DisplayName, null,
                        null, errors, warnings) : translateElement(
                        originalElement, document, targetLanguageCode,
                        hmReffId_DisplayName, null, null, errors, warnings));
            }
        }
//		replaceReferencedValues(document, hmReffId_DisplayName,
//				(isTranscode ? TRANSCODING : TRANSLATION));

        return (processingOK ? STATUS_SUCCESS : STATUS_FAILURE);
    }

    private void checkType(Element originalElement, List<ITMTSAMEror> warnings) {
        if (originalElement != null) {
            if (notEmpty(originalElement.getAttribute(XSI_TYPE))) {
                if (!(originalElement.getAttribute(XSI_TYPE).equals(CE) || originalElement
                        .getAttribute(XSI_TYPE).equals(CD))) {
                    warnings.add(TMError.WARNING_CODED_ELEMENT_NOT_PROPER_TYPE);
                }
            }
        }
    }

    private boolean translateElement(Element originalElement,
                                     Document document, String targetLanguageCode,
                                     HashMap<String, String> hmReffIdDisplayName, String valueSet,
                                     String valueSetVersion, List<ITMTSAMEror> errors,
                                     List<ITMTSAMEror> warnings) {
        return processElement(originalElement, document, targetLanguageCode,
                hmReffIdDisplayName, valueSet, valueSetVersion, false, errors,
                warnings);
    }

    private boolean processElement(Element originalElement, Document document,
                                   String targetLanguageCode,
                                   HashMap<String, String> hmReffIdDisplayName, String valueSet,
                                   String valueSetVersion, boolean isTranscode,
                                   List<ITMTSAMEror> errors, List<ITMTSAMEror> warnings) {
        try {
            // kontrola na povinne atributy
            Boolean checkAttributes = checkAttributes(originalElement, warnings);
            if (checkAttributes != null) {
                return checkAttributes.booleanValue();
            }

            CodedElement codedElement = new CodedElement(
                    (Element) originalElement);
            codedElement.setVsOid(valueSet);
            codedElement.setValueSetVersion(valueSetVersion);

            // hladanie vnoreneho elementu translation
            Node oldTranslationElement = findOldTranslation(originalElement);

            TSAMResponseStructure tsamResponse = isTranscode ? tsamApi
                    .getEpSOSConceptByCode((CodedElement) codedElement)
                    : tsamApi.getDesignationByEpSOSConcept(
                    (CodedElement) codedElement, targetLanguageCode);

            if (tsamResponse.isStatusSuccess()) {
                log.debug("processing successful " + codedElement.toString());
                // +++++ Element editing BEGIN +++++

                // NEW TRANSLATION element
                Element newTranslation = null;
                newTranslation = document.createElementNS(originalElement.getNamespaceURI(), TRANSLATION);
                if (originalElement.getPrefix() != null) {
                    newTranslation.setPrefix(originalElement.getPrefix());
                }
                boolean attributesFilled = false;
                // check - no repeated attributed in translation element by
                // transcoding
                // if codeSystem && code for source and target are same
                if (notEmpty(tsamResponse.getCodeSystem())
                        && notEmpty(codedElement.getOid())
                        && !codedElement.getOid().equalsIgnoreCase(
                        tsamResponse.getCodeSystem())
                        || (codedElement.getOid().equalsIgnoreCase(
                        tsamResponse.getCodeSystem()) && !codedElement
                        .getCode().equals(tsamResponse.getCode()))) {
                    // code
                    if (notEmpty(codedElement.getCode())) {
                        newTranslation.setAttribute(CODE, codedElement
                                .getCode());
                    }
                    // codeSystem
                    if (notEmpty(codedElement.getOid())) {
                        newTranslation.setAttribute(CODE_SYSTEM, codedElement
                                .getOid());
                    }
                    // codeSystemName
                    if (notEmpty(codedElement.getCodeSystem())) {
                        newTranslation.setAttribute(CODE_SYSTEM_NAME,
                                codedElement.getCodeSystem());
                    }
                    // codeSystemVersion
                    if (notEmpty(codedElement.getVersion())) {
                        newTranslation.setAttribute(CODE_SYSTEM_VERSION,
                                codedElement.getVersion());
                    }
                    attributesFilled = true;
                }
                // designation (only if source and target differs)
                if (!tsamResponse.getDesignation().equals(
                        codedElement.getDisplayName())) {
                    newTranslation.setAttribute(DISPLAY_NAME, codedElement
                            .getDisplayName());
                    attributesFilled = true;
                } else {
                    log.debug("Translation is same as original: " + tsamResponse.getDesignation());
                }
                if (attributesFilled) {
                    if (oldTranslationElement != null) {
                        oldTranslationElement = originalElement
                                .removeChild(oldTranslationElement);
                        newTranslation.appendChild(oldTranslationElement);
                    }
                    originalElement.appendChild(newTranslation);
                }

                // CHANGE original attributes
                // code
                if (notEmpty(tsamResponse.getCode())) {
                    originalElement.setAttribute(CODE, tsamResponse.getCode());
                }
                // codeSystem
                if (notEmpty(tsamResponse.getCodeSystem())) {
                    originalElement.setAttribute(CODE_SYSTEM, tsamResponse
                            .getCodeSystem());
                }
                // codeSystemName
                if (notEmpty(tsamResponse.getCodeSystemName())) {
                    originalElement.setAttribute(CODE_SYSTEM_NAME, tsamResponse
                            .getCodeSystemName());
                }
                // codeSystemVersion
                if (notEmpty(tsamResponse.getCodeSystemVersion())) {
                    originalElement.setAttribute(CODE_SYSTEM_VERSION,
                            tsamResponse.getCodeSystemVersion());
                }
                // designation
                if (notEmpty(tsamResponse.getDesignation())) {
                    originalElement.setAttribute(DISPLAY_NAME, tsamResponse
                            .getDesignation());
                }
                // +++++ Element editing END +++++
                errors.addAll(tsamResponse.getErrors());
                warnings.addAll(tsamResponse.getWarnings());
                return true;
            } else {
                log.error("processing failure! " + codedElement.toString());
                errors.addAll(tsamResponse.getErrors());
                warnings.addAll(tsamResponse.getWarnings());
                return false;
            }
        } catch (Exception e) {
            // system error
            log.error("processing failure! ", e);
            return false;
        }
    }

    /**
     * Najde a vrati vnoreny translation element ak existuje
     *
     * @param originalElement
     * @return
     */
    private Node findOldTranslation(Element originalElement) {
        Node oldTranslationElement = null;
        NodeList nodeList = originalElement.getChildNodes();
        if (nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (TMConstants.TRANSLATION.equals(node.getLocalName())) {
                        oldTranslationElement = node;
                        log.debug("Old translation found");
                        break;
                    }
                }
            }
        }
        return oldTranslationElement;
    }

    /**
     * Kontrola na povinne atributy.
     *
     * @param originalElement
     * @param warnings
     * @return Vrati true ak je povolene nemat povinne atributy, false ak nie,
     * null ak je vsetko ok
     */
    private Boolean checkAttributes(Element originalElement,
                                    List<ITMTSAMEror> warnings) {
        String elName = XmlUtil.getElementPath(originalElement);
        // ak je nullFlavor, neprekladat, nevyhadzovat chybu
        if (originalElement.hasAttribute("nullFlavor")) {
            log.debug("nullFlavor, skippink : " + elName);
            return true;
        } else {
            // ak chyba code alebo codeSystem vyhodit warning
            boolean noCode = false;
            boolean noCodeSystem = false;
            if (!originalElement.hasAttribute("code")) {
                noCode = true;
            }

            if (!originalElement.hasAttribute("codeSystem")) {
                noCodeSystem = true;
            }
            if (noCode || noCodeSystem) {
                NodeList origText = originalElement.getElementsByTagName("originalText");
                if (origText.getLength() > 0) {
                    // ak element obsahuje originalText, preskocit, nevyhazovat warning
                    log.debug("Element without required attributes, but has originalText, ignoring: " + elName);
                    return true;
                } else {
                    log.warn("Element has no \"code or \"codeSystem\" attribute: " + elName);
                    warnings.add(new TmErrorCtx(TMError.WARNING_MANDATORY_ATTRIBUTES_MISSING, "Element " + elName));
                    return false;
                }
            }
            return null;
        }
    }

    // private void addComment(Document document) {
    // Comment comment = document.createComment(POSAM_COMMENT);
    // document.getDocumentElement().appendChild(comment);
    // }
    public void setTsamApi(ITerminologyService tsamApi) {
        this.tsamApi = tsamApi;
    }

    public boolean notEmpty(String string) {
        return (string != null && string.length() > 0);
    }

    public void setConfig(TMConfiguration config) {
        this.config = config;
    }

    public void afterPropertiesSet() throws Exception {
        level1Type = new HashMap<String, String>();
        level1Type.put(config.getPatientSummaryCode(), PATIENT_SUMMARY1);
        level1Type.put(config.geteDispensationCode(), EDISPENSATION1);
        level1Type.put(config.getePrescriptionCode(), EPRESCRIPTION1);
        level1Type.put(config.getHcerCode(), HCER1);
        level1Type.put(config.getMroCode(), MRO1);

        level3Type = new HashMap<String, String>();
        level3Type.put(config.getPatientSummaryCode(), PATIENT_SUMMARY3);
        level3Type.put(config.geteDispensationCode(), EDISPENSATION3);
        level3Type.put(config.getePrescriptionCode(), EPRESCRIPTION3);
        level3Type.put(config.getHcerCode(), HCER3);
        level3Type.put(config.getMroCode(), MRO3);
    }

    /**
     * Obtains the unique identifier of the document
     *
     * @param doc
     * @return
     */
    private String getOIDFromDocument(Document doc) {
        String oid = "";
        if (doc.getElementsByTagName("id").getLength() > 0) {
            Node id = doc.getElementsByTagName("id").item(0);
            if (id.getAttributes().getNamedItem("root") != null) {
                oid = oid + id.getAttributes().getNamedItem("root").getTextContent();
            }
            if (id.getAttributes().getNamedItem("extension") != null) {
                oid = oid + "^" + id.getAttributes().getNamedItem("extension").getTextContent();
            }
        }
        return oid;
    }
}
