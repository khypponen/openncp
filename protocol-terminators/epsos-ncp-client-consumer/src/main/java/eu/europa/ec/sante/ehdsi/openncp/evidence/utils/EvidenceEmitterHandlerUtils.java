/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.sante.ehdsi.openncp.evidence.utils;

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
 *  Ancillary methods to the EvidenceEmitter class supporting In-Out flows in the Portal
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
    
    private static final Map<String,String> events; // maps the message type to its related ad-hoc event
    static {
        Map<String,String> map = new HashMap<>();
        // Portal-NCP interactions
        map.put(CLIENT_CONNECTOR_SUBMIT_DOCUMENT_REQUEST,"NI_XDR_REQ");
        map.put(CLIENT_CONNECTOR_SUBMIT_DOCUMENT_RESPONSE, "NI_XDR_RES");
        map.put(CLIENT_CONNECTOR_QUERY_PATIENT_REQUEST, "NI_PD_REQ");
        map.put(CLIENT_CONNECTOR_QUERY_PATIENT_RESPONSE, "NI_PD_RES");
        map.put(CLIENT_CONNECTOR_QUERY_DOCUMENTS_REQUEST, "NI_DQ_REQ");
        map.put(CLIENT_CONNECTOR_QUERY_DOCUMENTS_RESPONSE, "NI_DQ_RES");
        map.put(CLIENT_CONNECTOR_RETRIEVE_DOCUMENT_REQUEST, "NI_DR_REQ");
        map.put(CLIENT_CONNECTOR_RETRIEVE_DOCUMENT_RESPONSE, "NI_DR_RES");
        events = Collections.unmodifiableMap(map);
    }
    
    private static final Map<String,String> transactionNames; // maps the message type to the ad-hoc transaction name to be placed in the evidence filename
    static {
        Map<String,String> map = new HashMap<>();
        // Portal-NCP interactions
        map.put(CLIENT_CONNECTOR_SUBMIT_DOCUMENT_REQUEST,"NI_XDR_REQ_SENT");
        map.put(CLIENT_CONNECTOR_SUBMIT_DOCUMENT_RESPONSE, "NI_XDR_RES_RECEIVED");
        map.put(CLIENT_CONNECTOR_QUERY_PATIENT_REQUEST, "NI_PD_REQ_SENT");
        map.put(CLIENT_CONNECTOR_QUERY_PATIENT_RESPONSE, "NI_PD_RES_RECEIVED");
        map.put(CLIENT_CONNECTOR_QUERY_DOCUMENTS_REQUEST, "NI_DQ_REQ_SENT");
        map.put(CLIENT_CONNECTOR_QUERY_DOCUMENTS_RESPONSE, "NI_DQ_RES_RECEIVED");
        map.put(CLIENT_CONNECTOR_RETRIEVE_DOCUMENT_REQUEST, "NI_DR_REQ_SENT");
        map.put(CLIENT_CONNECTOR_RETRIEVE_DOCUMENT_RESPONSE, "NI_DR_RES_RECEIVED");
        transactionNames = Collections.unmodifiableMap(map);
    }
    
    public EvidenceEmitterHandlerUtils() {}
    
    public String getEventTypeFromMessage(SOAPBody soapBody) {
        String messageElement = soapBody.getFirstElementLocalName();
        LOG.debug("Message body element: " + messageElement);
        return events.get(messageElement);
    }
    
    public String getTransactionNameFromMessage(SOAPBody soapBody) {
        String messageElement = soapBody.getFirstElementLocalName();
        LOG.debug("Message body element: " + messageElement);
        return transactionNames.get(messageElement);
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
            Assertion identityAssertion = Helper.getHCPAssertion(elemSoapHeader);
            Assertion trca = Helper.getTRCAssertion(elemSoapHeader);
            if (identityAssertion != null && trca == null) {
                // this is a XCPD request from Portal to NCP-B, we don't yet have the TRCA
                msguuid = identityAssertion. getID();
            } else if (identityAssertion != null && trca != null) {
                // this is a XCA Query or Retrieve from Portal to NCP-B, we already have the TRCA
                msguuid = trca.getID();
            } else {
                //response to Portal doesn't have IdA, only the request from the Portal has it
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
