/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee.affecto.epsos.ws.handler;

import epsos.ccd.gnomon.auditmanager.IHEEventType;
import eu.epsos.util.xca.XCAConstants;
import eu.epsos.util.xcpd.XCPDConstants;
import eu.epsos.util.xdr.XDRConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.transform.TransformerException;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.util.XMLUtils;
import org.apache.log4j.Logger;
import org.opensaml.saml2.core.Assertion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tr.com.srdc.epsos.securityman.helper.Helper;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.XMLUtil;

/**
 *  Ancillary methods to the In/Out-FlowEvidenceEmitter classes
 * 
 * @author jgoncalves
 */
public class EvidenceEmitterHandlerUtils {
    
    private static final Logger LOG = Logger.getLogger(EvidenceEmitterHandlerUtils.class);
    
    private static final String CLIENT_CONNECTOR_XML_NAMESPACE = "http://clientconnector.protocolterminator.openncp.epsos/";
    private static final String CLIENT_CONNECTOR_SUBMIT_DOCUMENT_REQUEST = "submitDocument";
    private static final String CLIENT_CONNECTOR_SUBMIT_DOCUMENT_RESPONSE = "submitDocumentResponse";
    private static final String CLIENT_CONNECTOR_QUERY_PATIENT_REQUEST = "queryPatient";
    private static final String CLIENT_CONNECTOR_QUERY_PATIENT_RESPONSE = "queryPatientResponse";
    private static final String CLIENT_CONNECTOR_QUERY_DOCUMENTS_REQUEST = "queryDocuments";
    private static final String CLIENT_CONNECTOR_QUERY_DOCUMENTS_RESPONSE = "queryDocumentsResponse";
    private static final String CLIENT_CONNECTOR_RETRIEVE_DOCUMENT_REQUEST = "retrieveDocument";
    private static final String CLIENT_CONNECTOR_RETRIEVE_DOCUMENT_RESPONSE = "retrieveDocumentResponse";
    
    private static final List<String> clientConnectorOperations;
    static {
        List<String> list = new ArrayList<>();
        list.add(CLIENT_CONNECTOR_SUBMIT_DOCUMENT_REQUEST);
        list.add(CLIENT_CONNECTOR_SUBMIT_DOCUMENT_RESPONSE);
        list.add(CLIENT_CONNECTOR_QUERY_PATIENT_REQUEST);
        list.add(CLIENT_CONNECTOR_QUERY_PATIENT_RESPONSE);
        list.add(CLIENT_CONNECTOR_QUERY_DOCUMENTS_REQUEST);
        list.add(CLIENT_CONNECTOR_QUERY_DOCUMENTS_RESPONSE);
        list.add(CLIENT_CONNECTOR_RETRIEVE_DOCUMENT_REQUEST);
        list.add(CLIENT_CONNECTOR_RETRIEVE_DOCUMENT_RESPONSE);
        clientConnectorOperations = Collections.unmodifiableList(list);
    }
    
    private static final Map<String,String> iheEvents; // maps the message type to its related IHE event
    static {
        Map<String,String> map = new HashMap<>();
        map.put(XCPDConstants.PATIENT_DISCOVERY_REQUEST, IHEEventType.epsosIdentificationServiceFindIdentityByTraits.getCode()); // ITI-55
        map.put(XCPDConstants.PATIENT_DISCOVERY_RESPONSE, IHEEventType.epsosIdentificationServiceFindIdentityByTraits.getCode()); // ITI-55
        map.put(XCAConstants.ADHOC_QUERY_REQUEST, IHEEventType.epsosPatientServiceList.getCode()); // ITI-38: same for PS or eP List
        map.put(XCAConstants.ADHOC_QUERY_RESPONSE, IHEEventType.epsosPatientServiceList.getCode()); // ITI-38: same for PS or eP List
        map.put(XCAConstants.RETRIEVE_DOCUMENTSET_REQUEST, IHEEventType.epsosPatientServiceRetrieve.getCode()); // ITI-39: same for PS or eP Retrieve
        map.put(XCAConstants.RETRIEVE_DOCUMENTSET_RESPONSE, IHEEventType.epsosPatientServiceRetrieve.getCode()); // ITI-39: same for PS or eP Retrieve
        map.put(XDRConstants.PROVIDE_AND_REGISTER_DOCUMENT_SET_REQ_STR, IHEEventType.epsosDispensationServiceInitialize.getCode()); // ITI-41: same for Dispensation Initialize/Discard
        map.put(XDRConstants.DOC_RCP_PRVDANDRGSTDOCSETB_STR, IHEEventType.epsosConsentServicePut.getCode()); // ITI-41: same for Consent Put/Discard
        map.put(XDRConstants.REGISTRY_RESPONSE_STR, IHEEventType.epsosDispensationServiceInitialize.getCode()); // ITI-41: same for Dispensation Initialize/Discard and Consent Put/Discard
        // Portal-NCP interactions
        map.put(CLIENT_CONNECTOR_SUBMIT_DOCUMENT_REQUEST,"PORTAL_PD_REQ");
        map.put(CLIENT_CONNECTOR_SUBMIT_DOCUMENT_RESPONSE, "NCPB_XDR_RES");
        map.put(CLIENT_CONNECTOR_QUERY_PATIENT_REQUEST, "PORTAL_PD_REQ");
        map.put(CLIENT_CONNECTOR_QUERY_PATIENT_RESPONSE, "NCPB_PD_RES");
        map.put(CLIENT_CONNECTOR_QUERY_DOCUMENTS_REQUEST, "PORTAL_DQ_REQ");
        map.put(CLIENT_CONNECTOR_QUERY_DOCUMENTS_RESPONSE, "NCPB_DQ_RES");
        map.put(CLIENT_CONNECTOR_RETRIEVE_DOCUMENT_REQUEST, "PORTAL_DR_REQ");
        map.put(CLIENT_CONNECTOR_RETRIEVE_DOCUMENT_RESPONSE, "NCPB_DR_RES");
        iheEvents = Collections.unmodifiableMap(map);
    }
    
    private static final Map<String,String> transactionNames; // maps the message type to the ad-hoc transaction name to be placed in the evidence filename
    static {
        Map<String,String> map = new HashMap<>();
        map.put(XCPDConstants.PATIENT_DISCOVERY_REQUEST, "XCPD_REQ");
        map.put(XCPDConstants.PATIENT_DISCOVERY_RESPONSE, "XCPD_RES");
        map.put(XCAConstants.ADHOC_QUERY_REQUEST, "XCA_LIST_REQ");
        map.put(XCAConstants.ADHOC_QUERY_RESPONSE, "XCA_LIST_RES");
        map.put(XCAConstants.RETRIEVE_DOCUMENTSET_REQUEST, "XCA_RETRIEVE_REQ");
        map.put(XCAConstants.RETRIEVE_DOCUMENTSET_RESPONSE, "XCA_RETRIEVE_RES");
        map.put(XDRConstants.PROVIDE_AND_REGISTER_DOCUMENT_SET_REQ_STR, "XDR_SUBMIT_REQ");
        map.put(XDRConstants.DOC_RCP_PRVDANDRGSTDOCSETB_STR, "XDR_SUBMIT_REQ");
        map.put(XDRConstants.REGISTRY_RESPONSE_STR, "XDR_SUBMIT_RES");
        // Portal-NCP interactions
        map.put(CLIENT_CONNECTOR_SUBMIT_DOCUMENT_REQUEST,"PORTAL_XDR_REQ_RECEIVED");
        map.put(CLIENT_CONNECTOR_SUBMIT_DOCUMENT_RESPONSE, "NCPB_XDR_RES_SENT");
        map.put(CLIENT_CONNECTOR_QUERY_PATIENT_REQUEST, "PORTAL_PD_REQ_RECEIVED");
        map.put(CLIENT_CONNECTOR_QUERY_PATIENT_RESPONSE, "NCPB_PD_RES_SENT");
        map.put(CLIENT_CONNECTOR_QUERY_DOCUMENTS_REQUEST, "PORTAL_DQ_REQ_RECEIVED");
        map.put(CLIENT_CONNECTOR_QUERY_DOCUMENTS_RESPONSE, "NCPB_DQ_RES_SENT");
        map.put(CLIENT_CONNECTOR_RETRIEVE_DOCUMENT_REQUEST, "PORTAL_DR_REQ_RECEIVED");
        map.put(CLIENT_CONNECTOR_RETRIEVE_DOCUMENT_RESPONSE, "NCPB_DR_RES_SENT");
        transactionNames = Collections.unmodifiableMap(map);
    }
    
    public EvidenceEmitterHandlerUtils() {}
    
    public String getEventTypeFromMessage(SOAPBody soapBody) {
        String messageElement = soapBody.getFirstElementLocalName();
        LOG.debug("Message body element: " + messageElement);
        return iheEvents.get(messageElement);
    }
    
    public String getTransactionNameFromMessage(SOAPBody soapBody) {
        String messageElement = soapBody.getFirstElementLocalName();
        LOG.debug("Message body element: " + messageElement);
        return transactionNames.get(messageElement);
    }
    
    public String getServerSideTitle(SOAPBody soapBody) {
        String operation = soapBody.getFirstElementLocalName();
        String title = transactionNames.get(operation);
        if (!this.isClientConnectorOperation(operation)) {
            title = "NCPA_" + title;
        }
        return title;
    }
    
    private boolean isClientConnectorOperation(String operation) {
        return clientConnectorOperations.contains(operation);
    }
    
    public String getMsgUUID(SOAPHeader soapHeader, SOAPBody soapBody) throws Exception {
        String msguuid = null;
        Element elemSoapHeader = XMLUtils.toDOM(soapHeader);
        String operation = soapBody.getFirstElementLocalName();
        if (isClientConnectorOperation(operation)) {
            // we're in a Portal-NCPB interaction
            Assertion assertion = Helper.getHCPAssertion(elemSoapHeader);
            //response to Portal doesn't have IdA, only the request from the Portal has it
            if (assertion != null) {
                msguuid = assertion. getID();
            } else {
                // we don't have the IdA nor SOAP message ID, so we generate one UUID
                msguuid = Constants.UUID_PREFIX + UUID.randomUUID().toString();
            }
        }
        return msguuid;
    }
    
    public Document canonicalizeAxiomSoapEnvelope(SOAPEnvelope env) throws TransformerException, Exception {
        Document envCanonicalized = null;
        LOG.debug("Step 1: marshall it to document, since no c14n are available in OM");
        Element envAsDom = XMLUtils.toDOM(env);
        LOG.debug("Step 2: canonicalize it");
        envCanonicalized = XMLUtil.canonicalize(envAsDom.getOwnerDocument());
        LOG.debug("Pretty printing canonicalized: \n" + XMLUtil.prettyPrint(envCanonicalized));
        return envCanonicalized;
    }
}
