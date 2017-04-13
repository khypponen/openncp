/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.sante.ehdsi.openncp.evidence.utils;

import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import eu.epsos.util.EvidenceUtils;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.Handler;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.w3c.dom.Document;

/**
 * EvidenceEmitterHandler
 * Generates all NROs for the Portal
 * Currently supporting the generation of evidences in the following cases:
 *      Portal sends request to NCP-B *
 * @author jgoncalves
 */
public class OutFlowEvidenceEmitterHandler extends AbstractHandler {
    
    private static final Logger LOG = Logger.getLogger(OutFlowEvidenceEmitterHandler.class);
    
    private EvidenceEmitterHandlerUtils evidenceEmitterHandlerUtils;

    @Override
    public Handler.InvocationResponse invoke(MessageContext msgcontext) throws AxisFault {
        LOG.debug("OutFlow Evidence Emitter handler is executing");
        this.evidenceEmitterHandlerUtils = new EvidenceEmitterHandlerUtils();

        /* I'll leave this here as it might be useful in the future */
        
//        SOAPHeader soapHeader = msgcontext.getEnvelope().getHeader();
//        if (soapHeader != null) {
//            Iterator<?> blocks = soapHeader.examineAllHeaderBlocks();
//            LOG.debug("Iterating over soap headers");
//            while (blocks.hasNext()) {
//                LOG.debug("Processing header");
//                SOAPHeaderBlock block = (SOAPHeaderBlock)blocks.next();
//                LOG.debug(block.toString());
//                block.setProcessed();
//            }
//        }
        
//        LOG.debug("LOGGING TEST VALUES");
//        LOG.debug("MessageContext properties: " + msgcontext.getProperties());
//        LOG.debug("MessageContext messageID: " + msgcontext.getMessageID());
//        
//        SessionContext sessionCtx = msgcontext.getSessionContext();
//        if (sessionCtx != null) {
//            LOG.debug("SessionContext CookieID: " + sessionCtx.getCookieID());
//        } else {
//            LOG.debug("SessionContext is null!");
//        }
        
//        OperationContext operationCtx = msgcontext.getOperationContext();
//        if (operationCtx != null) {
//            LOG.debug("OperationContext operationName: " + operationCtx.getOperationName());
//            LOG.debug("OperationContext serviceGroupName: " + operationCtx.getServiceGroupName());
//            LOG.debug("OperationContext serviceName; " + operationCtx.getServiceName());
//            LOG.debug("OperationContext isComplete: " + operationCtx.isComplete());
//        } else {
//            LOG.debug("OperationContext is null!");
//        }
        
//        ServiceGroupContext serviceGroupCtx = msgcontext.getServiceGroupContext();
//        if (serviceGroupCtx != null) {
//            LOG.debug("ServiceGroupContext ID: " + serviceGroupCtx.getId());
//            AxisServiceGroup axisServiceGroup = serviceGroupCtx.getDescription();
//            Iterator<AxisService> itAxisService = axisServiceGroup.getServices();
//            while (itAxisService.hasNext()) {
//                AxisService axisService = itAxisService.next();
//                LOG.debug("AxisService BindingName: " + axisService.getBindingName());
//                LOG.debug("AxisService CustomSchemaNamePrefix: " + axisService.getCustomSchemaNamePrefix());
//                LOG.debug("AxisService CustomSchemaNameSuffix: " + axisService.getCustomSchemaNameSuffix());
//                LOG.debug("AxisService endpointName: " + axisService.getEndpointName());
//                Map<String,AxisEndpoint> axisEndpoints = axisService.getEndpoints();
//                for (String key : axisEndpoints.keySet()) {
//                    AxisEndpoint axisEndpoint = axisEndpoints.get(key);
//                    LOG.debug("AxisEndpoint calculatedEndpointURL: " + axisEndpoint.calculateEndpointURL());
//                    LOG.debug("AxisEndpoint alias: " + axisEndpoint.getAlias());
//                    LOG.debug("AxisEndpoint endpointURL: " + axisEndpoint.getEndpointURL());
//                    LOG.debug("AxisEndpoint active: " + axisEndpoint.isActive());
//                }
//                LOG.debug("AxisService EPRs: " + Arrays.toString((String[]) axisService.getEPRs()));
//                LOG.debug("AxisService name: " + axisService.getName());
//                LOG.debug("AxisService isClientSide: " + axisService.isClientSide());
//            } 
//        } else {
//            LOG.debug("ServiceGroupContext is null!");
//        }
        
//        ConfigurationContext configCtx = msgcontext.getRootContext();
//        if (configCtx != null) {
//            LOG.debug("ConfigurationContext contextRoot: " + configCtx.getContextRoot());
//            LOG.debug("ConfigurationContext serviceGroupContextIDs: " + Arrays.toString((String[])configCtx.getServiceGroupContextIDs()));
//            LOG.debug("ConfigurationContext servicePath: " + configCtx.getServicePath());
//        } else {
//            LOG.debug("ConfigurationContext is null!");
//        }
       
        try {
            /* Canonicalizing the full SOAP message */
            Document envCanonicalized = this.evidenceEmitterHandlerUtils.canonicalizeAxiomSoapEnvelope(msgcontext.getEnvelope());
        
            SOAPHeader soapHeader = msgcontext.getEnvelope().getHeader();
            SOAPBody soapBody = msgcontext.getEnvelope().getBody();
            String eventType = this.evidenceEmitterHandlerUtils.getEventTypeFromMessage(soapBody);
            String title = this.evidenceEmitterHandlerUtils.getTransactionNameFromMessage(soapBody);
            String msgUUID = this.evidenceEmitterHandlerUtils.getMsgUUID(soapHeader, soapBody);
            LOG.debug("eventType: " + eventType);
            LOG.debug("title: " + title);
            LOG.debug("msgUUID: " + msgUUID);
            
            /* Portal sends request to NCP-B*/
            EvidenceUtils.createEvidenceREMNRO(envCanonicalized,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                            tr.com.srdc.epsos.util.Constants.SC_KEYSTORE_PATH,
                            tr.com.srdc.epsos.util.Constants.SC_KEYSTORE_PASSWORD,
                            tr.com.srdc.epsos.util.Constants.SC_PRIVATEKEY_ALIAS,
                            eventType,
                            new DateTime(),
                            EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                            title,
                            msgUUID);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
            
        return Handler.InvocationResponse.CONTINUE;
    }
}
