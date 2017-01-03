/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. <epsos@srdc.com.tr>
 *
 * This file is part of SRDC epSOS NCP.
 *
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * XCPD_ServiceMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.5.4
 * Built on : Dec 19, 2010 (08:18:42 CET)
 */
package _2009.xcpd.iti.ihe;

import com.spirit.epsos.cc.adc.EadcEntry;
import epsos.ccd.gnomon.auditmanager.AuditService;
import epsos.ccd.gnomon.auditmanager.EventLog;
import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.ccd.gnomon.auditmanager.EventType;
import eu.epsos.pt.eadc.EadcUtilWrapper;
import eu.epsos.pt.eadc.util.EadcUtil;
import eu.epsos.util.EvidenceUtils;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.datamodel.hl7v3.Hl7v3Schematron;
import eu.epsos.validation.services.XcpdValidationService;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.util.XMLUtils;
import org.apache.log4j.Logger;
import org.hibernate.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.XMLUtil;
import tr.com.srdc.epsos.util.http.HTTPUtil;

/**
 * XCPD_ServiceMessageReceiverInOut message receiver
 */
public class XCPD_ServiceMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver {

	static {
		System.out.println("Loading the WS-Security init libraries in XCPD_ServiceMessageReceiverInOut xcpd 2009");

		org.apache.xml.security.Init.init(); // Massi added 3/1/2017. 
	}
    public static Logger logger = Logger
            .getLogger(XCPD_ServiceMessageReceiverInOut.class);

    private String getIPofSender(
            org.apache.axis2.context.MessageContext msgContext) {
        String remoteAddress_IPConsumer = (String) msgContext
                .getProperty("REMOTE_ADDR");
        return remoteAddress_IPConsumer;
    }

    private String getMessageID(org.apache.axiom.soap.SOAPEnvelope envelope) {
        Iterator<OMElement> it = envelope.getHeader().getChildrenWithName(
                new QName("http://www.w3.org/2005/08/addressing", "MessageID"));
        if (it.hasNext()) {
            return it.next().getText();
        } else {
            // [Mustafa: May 8, 2012]: Should not be empty string, sch. gives
            // error.
            return Constants.UUID_PREFIX;
        }
    }

    public void invokeBusinessLogic(
            org.apache.axis2.context.MessageContext msgContext,
            org.apache.axis2.context.MessageContext newMsgContext)
            throws org.apache.axis2.AxisFault {

        try {
            Date startTime = new Date();

            // get the implementation class for the Web Service
            Object obj = getTheImplementationObject(msgContext);

            SOAPHeader sh = msgContext.getEnvelope().getHeader();

            EventLog eventLog = new EventLog();
            String ip = getIPofSender(msgContext);
            eventLog.setSourceip(ip);
            eventLog.setReqM_ParticipantObjectID(getMessageID(msgContext
                    .getEnvelope()));
            eventLog.setReqM_PatricipantObjectDetail(msgContext.getEnvelope()
                    .getHeader().toString().getBytes());

            HttpServletRequest req = (HttpServletRequest) msgContext
                    .getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
            String clientDN = HTTPUtil.getClientCertificate(req);
            eventLog.setSC_UserID(clientDN);
            eventLog.setTargetip(req.getServerName());

            logger.debug("Incoming XCPD Request Message:\n"
                    + XMLUtil.prettyPrint(XMLUtils.toDOM(msgContext
                                    .getEnvelope())));

            SOAPEnvelope env = msgContext.getEnvelope();
            // Send NRR
            Document envCanonicalized = null;
            try {
            	logger.debug("Step 1: marshall it to document, since no c14n are available in OM");
            	Element envAsDom = XMLUtils.toDOM(env);
            	logger.debug("Step 1: DOM element is: " + XMLUtil.prettyPrint(envAsDom));
            	logger.debug("Step 2: canonicalize it");
            	envCanonicalized = XMLUtil.canonicalize(envAsDom.getOwnerDocument());
                logger.debug("Pretty printing canonicalized" + XMLUtil.prettyPrint(envCanonicalized));

			} catch (Exception e1) {
				throw new IllegalArgumentException(e1);
			}
            try {
                EvidenceUtils.createEvidenceREMNRR(envCanonicalized,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                        EventType.epsosIdentificationServiceFindIdentityByTraits.getCode(),
                        new DateTime(),
                        EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                        "NCPA_XCPD_REQ");
                
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }

            /* Validate incoming request message */
            XcpdValidationService.getInstance().validateSchematron(
                    XMLUtil.prettyPrint(XMLUtils.toDOM(msgContext.getEnvelope().getBody().getFirstElement())),
                    Hl7v3Schematron.EPSOS_ID_SERVICE_REQUEST.toString(),
                    NcpSide.NCP_A);

            XCPD_ServiceSkeleton skel = (XCPD_ServiceSkeleton) obj;
            // Out Envelop
            org.apache.axiom.soap.SOAPEnvelope envelope = null;
            // Find the axisOperation that has been set by the Dispatch phase.
            org.apache.axis2.description.AxisOperation op = msgContext
                    .getOperationContext().getAxisOperation();
            if (op == null) {
                throw new org.apache.axis2.AxisFault(
                        "Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
            }

            String randomUUID = Constants.UUID_PREFIX + UUID.randomUUID().toString();
            java.lang.String methodName;
            if ((op.getName() != null)
                    && ((methodName = org.apache.axis2.util.JavaUtils
                    .xmlNameToJavaIdentifier(op.getName()
                            .getLocalPart())) != null)) {

                if ("respondingGateway_PRPA_IN201305UV02".equals(methodName)) {

                    org.hl7.v3.PRPAIN201306UV02 pRPA_IN201306UV021 = null;
                    org.hl7.v3.PRPAIN201305UV02 wrappedParam = (org.hl7.v3.PRPAIN201305UV02) fromOM(
                            msgContext.getEnvelope().getBody()
                            .getFirstElement(),
                            org.hl7.v3.PRPAIN201305UV02.class,
                            getEnvelopeNamespaces(msgContext.getEnvelope()));

                    pRPA_IN201306UV021 = skel
                            .respondingGateway_PRPA_IN201305UV02(wrappedParam,
                                    sh, eventLog);

                    envelope = toEnvelope(getSOAPFactory(msgContext),
                            pRPA_IN201306UV021, false);

                    // SOAPHeaderBlock shb = (SOAPHeaderBlock)
                    // envelope.getHeader().getHeaderBlocksWithNSURI("http://www.w3.org/2005/08/addressing");
                    // shb.setMustUnderstand(true);
                    /* Validate reponse message */
                    XcpdValidationService.getInstance().validateSchematron(XMLUtil.prettyPrint(XMLUtils.toDOM(
                            envelope.getBody().getFirstElement())),
                            Hl7v3Schematron.EPSOS_ID_SERVICE_RESPONSE.toString(),
                            NcpSide.NCP_A);

                    eventLog.setResM_ParticipantObjectID(randomUUID);
                    eventLog.setResM_PatricipantObjectDetail(envelope.getHeader().toString().getBytes());

                    AuditService auditService = new AuditService();
                    auditService.write(eventLog, "", "1");

                    XMLGregorianCalendar cal = eventLog.getEI_EventDateTime();

                    logger.debug("Outgoing XCPD Response Message:\n"
                            + XMLUtil.prettyPrint(XMLUtils.toDOM(envelope)));
                    logger.info("NOT Doing evidence on the response");
//
//                    try {
//                        EvidenceUtils.createEvidenceREMNRO(XMLUtil.prettyPrint(XMLUtils.toDOM(envelope)),
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
//                                EventType.epsosIdentificationServiceFindIdentityByTraits.getCode(),
//                                new DateTime(),
//                                EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
//                                "NCPA_XCPD_RES");
//                    } catch (Exception e) {
//                        log.error(ExceptionUtils.getStackTrace(e));
//                    }

                } else {
                    throw new java.lang.RuntimeException("method not found");
                }

                logger.info("Submission Time is : " + eventLog.getEI_EventDateTime());
                logger.info("EventType is : " + eventLog.getEventType());
                logger.info("Event Outcome is: " + eventLog.getEI_EventOutcomeIndicator().toString());
                logger.info("KEYSTORE PATH: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH);
                logger.info("KEYSTORE PWD: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD);
                logger.info("KEY ALIAS: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS);
                logger.info("PRIVATE KEY PASSWORD: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_PASSWORD);

                Date endTime = new Date();
                newMsgContext.setEnvelope(envelope);
                newMsgContext.getOptions().setMessageId(randomUUID);

                try {
                    EadcUtilWrapper.invokeEadc(msgContext,
                            newMsgContext,
                            null,
                            null,
                            startTime,
                            endTime,
                            "",
                            EadcEntry.DsTypes.XCA,
                            EadcUtil.Direction.OUTBOUND);
                } catch (Exception ex) {
                    logger.error("EADC INVOCATION FAILED!", ex);
                }
            }
        } catch (java.lang.Exception e) {
            logger.error(e.getMessage(), e);
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }
    //
    private static final javax.xml.bind.JAXBContext wsContext;

    static {
        javax.xml.bind.JAXBContext jc;
        jc = null;
        try {
            jc = javax.xml.bind.JAXBContext.newInstance(
                    org.hl7.v3.PRPAIN201305UV02.class,
                    org.hl7.v3.PRPAIN201306UV02.class);
        } catch (javax.xml.bind.JAXBException ex) {
            System.err.println("Unable to create JAXBContext: "
                    + ex.getMessage());
            ex.printStackTrace(System.err);
            // Runtime.getRuntime().exit(-1);
        } finally {
            wsContext = jc;
        }
    }

    private org.apache.axiom.om.OMElement toOM(
            org.hl7.v3.PRPAIN201305UV02 param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            javax.xml.bind.JAXBContext context = wsContext;
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT,
                    Boolean.TRUE);

            org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory
                    .getOMFactory();

            JaxbRIDataSource source = new JaxbRIDataSource(
                    org.hl7.v3.PRPAIN201305UV02.class, param, marshaller,
                    "urn:hl7-org:v3", "PRPA_IN201305UV02");
            org.apache.axiom.om.OMNamespace namespace = factory
                    .createOMNamespace("urn:hl7-org:v3", null);
            return factory.createOMElement(source, "PRPA_IN201305UV02",
                    namespace);
        } catch (javax.xml.bind.JAXBException bex) {
            throw org.apache.axis2.AxisFault.makeFault(bex);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
            org.apache.axiom.soap.SOAPFactory factory,
            org.hl7.v3.PRPAIN201305UV02 param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        org.apache.axiom.soap.SOAPEnvelope envelope = factory
                .getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent));
        return envelope;
    }

    private org.apache.axiom.om.OMElement toOM(
            org.hl7.v3.PRPAIN201306UV02 param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            javax.xml.bind.JAXBContext context = wsContext;
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT,
                    Boolean.TRUE);

            org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory
                    .getOMFactory();

            JaxbRIDataSource source = new JaxbRIDataSource(
                    org.hl7.v3.PRPAIN201306UV02.class, param, marshaller,
                    "urn:hl7-org:v3", "PRPA_IN201306UV02");
            org.apache.axiom.om.OMNamespace namespace = factory
                    .createOMNamespace("urn:hl7-org:v3", null);
            return factory.createOMElement(source, "PRPA_IN201306UV02",
                    namespace);
        } catch (javax.xml.bind.JAXBException bex) {
            throw org.apache.axis2.AxisFault.makeFault(bex);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
            org.apache.axiom.soap.SOAPFactory factory,
            org.hl7.v3.PRPAIN201306UV02 param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        org.apache.axiom.soap.SOAPEnvelope envelope = factory
                .getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent));
        return envelope;
    }

    /**
     * get the default envelope
     */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
            org.apache.axiom.soap.SOAPFactory factory) {
        return factory.getDefaultEnvelope();
    }

    private java.lang.Object fromOM(org.apache.axiom.om.OMElement param,
            java.lang.Class type, java.util.Map extraNamespaces)
            throws org.apache.axis2.AxisFault {
        try {
            javax.xml.bind.JAXBContext context = wsContext;
            javax.xml.bind.Unmarshaller unmarshaller = context
                    .createUnmarshaller();

            return unmarshaller.unmarshal(
                    param.getXMLStreamReaderWithoutCaching(), type).getValue();
        } catch (javax.xml.bind.JAXBException bex) {
            throw org.apache.axis2.AxisFault.makeFault(bex);
        }
    }

    class JaxbRIDataSource implements org.apache.axiom.om.OMDataSource {

        /**
         * Bound object for output.
         */
        private final Object outObject;
        /**
         * Bound class for output.
         */
        private final Class outClazz;
        /**
         * Marshaller.
         */
        private final javax.xml.bind.Marshaller marshaller;
        /**
         * Namespace
         */
        private String nsuri;
        /**
         * Local name
         */
        private String name;

        /**
         * Constructor from object and marshaller.
         *
         * @param obj
         * @param marshaller
         */
        public JaxbRIDataSource(Class clazz, Object obj,
                javax.xml.bind.Marshaller marshaller, String nsuri, String name) {
            this.outClazz = clazz;
            this.outObject = obj;
            this.marshaller = marshaller;
            this.nsuri = nsuri;
            this.name = name;
        }

        public void serialize(java.io.OutputStream output,
                org.apache.axiom.om.OMOutputFormat format)
                throws javax.xml.stream.XMLStreamException {
            try {
                marshaller.marshal(
                        new javax.xml.bind.JAXBElement(
                                new javax.xml.namespace.QName(nsuri, name),
                                outObject.getClass(), outObject), output);
            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException(
                        "Error in JAXB marshalling", e);
            }
        }

        public void serialize(java.io.Writer writer,
                org.apache.axiom.om.OMOutputFormat format)
                throws javax.xml.stream.XMLStreamException {
            try {
                marshaller.marshal(
                        new javax.xml.bind.JAXBElement(
                                new javax.xml.namespace.QName(nsuri, name),
                                outObject.getClass(), outObject), writer);
            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException(
                        "Error in JAXB marshalling", e);
            }
        }

        public void serialize(javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            try {
                marshaller.marshal(
                        new javax.xml.bind.JAXBElement(
                                new javax.xml.namespace.QName(nsuri, name),
                                outObject.getClass(), outObject), xmlWriter);
            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException(
                        "Error in JAXB marshalling", e);
            }
        }

        public javax.xml.stream.XMLStreamReader getReader()
                throws javax.xml.stream.XMLStreamException {
            try {
                javax.xml.bind.JAXBContext context = wsContext;
                org.apache.axiom.om.impl.builder.SAXOMBuilder builder = new org.apache.axiom.om.impl.builder.SAXOMBuilder();
                javax.xml.bind.Marshaller marshaller = context
                        .createMarshaller();
                marshaller.marshal(
                        new javax.xml.bind.JAXBElement(
                                new javax.xml.namespace.QName(nsuri, name),
                                outObject.getClass(), outObject), builder);

                return builder.getRootElement().getXMLStreamReader();
            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException(
                        "Error in JAXB marshalling", e);
            }
        }
    }

    /**
     * A utility method that copies the namepaces from the SOAPEnvelope
     */
    private java.util.Map getEnvelopeNamespaces(
            org.apache.axiom.soap.SOAPEnvelope env) {
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
            org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator
                    .next();
            returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
        }
        return returnMap;
    }

    private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }

        return f;
    }
}// end of class
