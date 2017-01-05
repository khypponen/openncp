/*
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. epsos@srdc.com.tr
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
 * SRDC epSOS NCP. If not, see http://www.gnu.org/licenses/.
 */
package tr.com.srdc.epsos.ws.xcpd.client;

import com.spirit.epsos.cc.adc.EadcEntry;
import ee.affecto.epsos.util.EventLogClientUtil;
import ee.affecto.epsos.util.EventLogUtil;
import epsos.ccd.gnomon.auditmanager.EventLog;
import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.ccd.gnomon.auditmanager.EventType;
import eu.epsos.pt.eadc.EadcUtilWrapper;
import eu.epsos.pt.eadc.util.EadcUtil;
import eu.epsos.util.EvidenceUtils;
import eu.epsos.util.xcpd.XCPDConstants;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.datamodel.hl7v3.Hl7v3Schematron;
import eu.epsos.validation.services.XcpdValidationService;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.soap.impl.llom.soap12.SOAP12HeaderBlockImpl;
import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.util.XMLUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.XMLUtil;

import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

/**
 * RespondingGateway_ServiceStub java implementation.
 *
 * @author SRDC<code> - epsos@srdc.com.tr>
 * @author Aarne Roosi<code> - Aarne.Roosi@Affecto.com</code>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class RespondingGateway_ServiceStub extends org.apache.axis2.client.Stub {

    protected org.apache.axis2.description.AxisOperation[] _operations;
    // hashmaps to keep the fault mapping
    private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
    private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
    private java.util.HashMap faultMessageMap = new java.util.HashMap();
    private static int counter = 0;
    private static Logger LOG = LoggerFactory.getLogger(RespondingGateway_ServiceStub.class);
    private static final javax.xml.bind.JAXBContext wsContext;
    private String countryCode;
    private Date transactionStartTime;
    private Date transactionEndTime;

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    private static synchronized String getUniqueSuffix() {
        // reset the counter if it is greater than 99999
        if (counter > 99999) {
            counter = 0;
        }
        counter++;
        return Long.toString(System.currentTimeMillis()) + "_" + counter;
    }

    private void populateAxisService() {

        // creating the Service with a unique name
        _service = new org.apache.axis2.description.AxisService("RespondingGateway_Service" + getUniqueSuffix());
        addAnonymousOperations();

        // creating the operations
        org.apache.axis2.description.AxisOperation __operation;
        _operations = new org.apache.axis2.description.AxisOperation[1];
        __operation = new org.apache.axis2.description.OutInAxisOperation();
        __operation.setName(new javax.xml.namespace.QName(XCPDConstants.SOAP_HEADERS.NAMESPACE_URI, XCPDConstants.SOAP_HEADERS.NAMESPACE_REQUEST_LOCAL_PART));
        _service.addOperation(__operation);
        _operations[0] = __operation;

    }

    // populates the faults
    private void populateFaults() {
    }

    /**
     * Constructor that takes in a configContext
     */
    public RespondingGateway_ServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext, String targetEndpoint) {
        this(configurationContext, targetEndpoint, false);
    }

    /**
     * Constructor that takes in a configContext and useseperate listner
     */
    public RespondingGateway_ServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext, String targetEndpoint, boolean useSeparateListener) {
        // To populate AxisService
        populateAxisService();
        populateFaults();
        try {
            _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext, _service);
        } catch (AxisFault ex) {
            throw new RuntimeException(ex);
        }

        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);

        _serviceClient.getOptions().setTimeOutInMilliSeconds(180000); //Wait time after which a client times out in a blocking scenario: 3 minutes

        // Set the soap version
        _serviceClient.getOptions().setSoapVersionURI(org.apache.axiom.soap.SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

        MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();

        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setDefaultMaxConnectionsPerHost(20);
        multiThreadedHttpConnectionManager.setParams(params);
        HttpClient httpClient = new HttpClient(multiThreadedHttpConnectionManager);

        this._getServiceClient().getServiceContext().getConfigurationContext().setProperty(HTTPConstants.REUSE_HTTP_CLIENT, false);
        this._getServiceClient().getServiceContext().getConfigurationContext().setProperty(HTTPConstants.CACHED_HTTP_CLIENT, httpClient);
        this._getServiceClient().getServiceContext().getConfigurationContext().setProperty(HTTPConstants.AUTO_RELEASE_CONNECTION, false);
    }

    /**
     * Constructor taking the target endpoint
     */
    public RespondingGateway_ServiceStub(String targetEndpoint) {
        this(null, targetEndpoint);
    }

    /**
     * Auto generated method signature
     *
     * @param pRPA_IN201305UV02
     * @param idAssertion
     * @return
     * @throws java.rmi.RemoteException
     * @see tr.com.srdc.epsos.ws.xcpd.client.RespondingGateway_Service#respondingGateway_PRPA_IN201305UV02
     */
    public org.hl7.v3.PRPAIN201306UV02 respondingGateway_PRPA_IN201305UV02(PRPAIN201305UV02 pRPA_IN201305UV02, Assertion idAssertion) {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try {
            // TMP
            // XCPD request start time
            long start = System.currentTimeMillis();

            org.apache.axis2.client.OperationClient operationClient = _serviceClient.createClient(_operations[0].getName());
            operationClient.getOptions().setAction(XCPDConstants.SOAP_HEADERS.REQUEST_ACTION);
            operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
            addPropertyToOperationClient(operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

            SOAPFactory soapFactory = getFactory(operationClient.getOptions().getSoapVersionURI());

            /* create SOAP envelope with that payload */
            org.apache.axiom.soap.SOAPEnvelope env;
            env = toEnvelope(soapFactory,
                    pRPA_IN201305UV02,
                    optimizeContent(new javax.xml.namespace.QName(XCPDConstants.SOAP_HEADERS.NAMESPACE_URI, XCPDConstants.SOAP_HEADERS.NAMESPACE_REQUEST_LOCAL_PART)));

            /* adding SOAP soap_headers */
            OMFactory factory = OMAbstractFactory.getOMFactory();
            OMNamespace ns2 = factory.createOMNamespace(XCPDConstants.SOAP_HEADERS.OM_NAMESPACE, "");

            SOAPHeaderBlock action = new SOAP12HeaderBlockImpl("Action", ns2, soapFactory);
            OMNode node = factory.createOMText(XCPDConstants.SOAP_HEADERS.REQUEST_ACTION);
            action.addChild(node);
            OMAttribute att = factory.createOMAttribute(XCPDConstants.SOAP_HEADERS.MUST_UNDERSTAND, env.getNamespace(), "1");
            action.addAttribute(att);

            SOAPHeaderBlock id = new SOAP12HeaderBlockImpl("MessageID", ns2, soapFactory);
            OMNode node2 = factory.createOMText(Constants.UUID_PREFIX + UUID.randomUUID().toString());
            id.addChild(node2);

            OMNamespace ns = factory.createOMNamespace(XCPDConstants.SOAP_HEADERS.SECURITY_XSD, "wsse");
            SOAPHeaderBlock shbSecurity = new SOAP12HeaderBlockImpl("Security", ns, soapFactory);

            try {
                shbSecurity.addChild(XMLUtils.toOM(idAssertion.getDOM()));
                _serviceClient.addHeader(shbSecurity);
            } catch (Exception ex) {
                LOG.error(ex.getLocalizedMessage(), ex);
            }
            _serviceClient.addHeader(action);
            _serviceClient.addHeader(id);
            _serviceClient.addHeadersToEnvelope(env);

            /* set the message context with that soap envelope */
            org.apache.axis2.context.MessageContext messageContext = new org.apache.axis2.context.MessageContext();
            messageContext.setEnvelope(env);

            /* add the message contxt to the operation client */
            operationClient.addMessageContext(messageContext);

            /* Log soap request */
            String logRequestBody;
            try {
                String logRequestMsg = XMLUtil.prettyPrint(XMLUtils.toDOM(env));
                LOG.debug(XCPDConstants.LOG.OUTGOING_XCPD_MESSAGE
                        + System.getProperty("line.separator") + logRequestMsg);
                logRequestBody = XMLUtil.prettyPrint(XMLUtils.toDOM(env.getBody().getFirstElement()));

                LOG.info("XCPD Request sent. EVIDENCE NRO");
                // NRO
                try {
                    EvidenceUtils.createEvidenceREMNRO(XMLUtil.prettyPrint(XMLUtils.toDOM(env)),
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                            EventType.epsosIdentificationServiceFindIdentityByTraits.getCode(),
                            new DateTime(),
                            EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                            "NCPB_XCPD_REQ");
                } catch (Exception e) {
                    LOG.error(ExceptionUtils.getStackTrace(e));
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            // TMP
            // XCPD response end time
            long end = System.currentTimeMillis();
            LOG.info("XCPD REQUEST-RESPONSE TIME: " + (end - start) / 1000.0);

            // TMP
            // Validation start time
            start = System.currentTimeMillis();

            /* Validate Request Messages */
            XcpdValidationService.getInstance().validateSchematron(logRequestBody, Hl7v3Schematron.EPSOS_ID_SERVICE_REQUEST.toString(), NcpSide.NCP_B);

            // TMP
            // Transaction end time
            end = System.currentTimeMillis();
            LOG.info("XCPD VALIDATION REQ TIME: " + (end - start) / 1000.0);

            // TMP
            // Transaction start time
            start = System.currentTimeMillis();

            /* execute the operation client */
            transactionStartTime = new Date();
            operationClient.execute(true);

            org.apache.axis2.context.MessageContext _returnMessageContext = operationClient.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
            transactionEndTime = new Date();

            // TMP
            // Transaction end time
            end = System.currentTimeMillis();
            LOG.info("XCPD TRANSACTION TIME: " + (end - start) / 1000.0);

            // TMP
            // Validation start time
            start = System.currentTimeMillis();

            /* Log soap response */
            String logResponseBody;
            try {
                String logResponseMsg = XMLUtil.prettyPrint(XMLUtils.toDOM(_returnEnv));
                LOG.debug(XCPDConstants.LOG.INCOMING_XCPD_MESSAGE
                        + System.getProperty("line.separator") + logResponseMsg);
                logResponseBody = XMLUtil.prettyPrint(XMLUtils.toDOM(_returnEnv.getBody().getFirstElement()));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            /* Validate Response Messages */
            XcpdValidationService.getInstance().validateSchematron(logResponseBody, Hl7v3Schematron.EPSOS_ID_SERVICE_RESPONSE.toString(), NcpSide.NCP_B);

            Object object = fromOM(_returnEnv.getBody().getFirstElement(), org.hl7.v3.PRPAIN201306UV02.class, getEnvelopeNamespaces(_returnEnv));

            // TMP
            // Validation end time
            end = System.currentTimeMillis();
            LOG.info("XCPD VALIDATION RES TIME: " + (end - start) / 1000.0);

            // TMP
            // eADC start time
            start = System.currentTimeMillis();

            /*
             * Invoke NRR
             */
            LOG.info("XCPD Response received. EVIDENCE NRR");
            // NRR
            try {
                EvidenceUtils.createEvidenceREMNRR(XMLUtil.prettyPrint(XMLUtils.toDOM(env)),
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                        EventType.epsosIdentificationServiceFindIdentityByTraits.getCode(),
                        new DateTime(),
                        EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                        "NCPB_XCPD_RES");
            } catch (Exception e) {
                LOG.error(ExceptionUtils.getStackTrace(e));
            }
            /*
             * Invoque eADC
             */
            try {
                EadcUtilWrapper.invokeEadc(messageContext, // Request message context
                        _returnMessageContext, // Response message context
                        this._getServiceClient(), //Service Client
                        null, // CDA document
                        transactionStartTime, // Transaction Start Time
                        transactionEndTime, // Transaction End Time
                        this.countryCode, // Country A ISO Code
                        EadcEntry.DsTypes.XCPD, // Data source type
                        EadcUtil.Direction.OUTBOUND); // Transaction direction
            } catch (ParserConfigurationException ex) {
                LOG.error("EADC INVOCATION FAILED!", ex);
            } catch (Exception ex) {
                LOG.error("EADC INVOCATION FAILED!", ex);
            }

            // TMP
            // eADC end time
            end = System.currentTimeMillis();
            LOG.info("XCPD eADC TIME: " + (end - start) / 1000.0);

            // TMP
            // Audit start time
            start = System.currentTimeMillis();

            // eventLog
            EventLog eventLog = createAndSendEventLog(pRPA_IN201305UV02, (org.hl7.v3.PRPAIN201306UV02) object, messageContext, _returnEnv, env, idAssertion, this._getServiceClient().getOptions().getTo().getAddress());

            try {
                LOG.info("SOAP MESSAGE IS: " + XMLUtils.toDOM(_returnEnv));
            } catch (Exception ex) {
                LOG.error(null, ex);
            }

            LOG.info("Submission Time is : " + eventLog.getEI_EventDateTime());
            LOG.info("EventType is : " + eventLog.getEventType());
            LOG.info("Event Outcome is: " + eventLog.getEI_EventOutcomeIndicator().toString());
            LOG.info("KEYSTORE PATH: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH);
            LOG.info("KEYSTORE PWD: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD);
            LOG.info("KEY ALIAS: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS);
            LOG.info("PRIVATE KEY PASSWORD: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_PASSWORD);
            // Audit end time
            end = System.currentTimeMillis();
            LOG.info("XCPD AUDIT TIME: " + (end - start) / 1000.0);

            return (org.hl7.v3.PRPAIN201306UV02) object;

        } catch (org.apache.axis2.AxisFault f) {
//            // TODO A.R. Audit log SOAP Fault is still missing
//            LOG.error(f.getLocalizedMessage(), f);

            org.apache.axiom.om.OMElement faultElt = f.getDetail();

            if (faultElt != null) {

                if (faultExceptionNameMap.containsKey(faultElt.getQName())) {

                    /* make the fault by reflection */
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap.get(faultElt.getQName());
                        Class exceptionClass = Class.forName(exceptionClassName);
                        Exception ex = (Exception) exceptionClass.newInstance();
                        // message class
                        String messageClassName = (String) faultMessageMap.get(faultElt.getQName());
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt, messageClass, null);
                        Method m = exceptionClass.getMethod("setFaultMessage", new Class[]{messageClass});
                        m.invoke(ex, new Object[]{messageObject});

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);

                    } catch (Exception e) {
                        throw new RuntimeException(f.getMessage(), f);    // we cannot intantiate the class - throw the original Axis fault
                    }
                }
            }
            throw new RuntimeException(f.getMessage(), f);

        } finally {
            if (_messageContext != null && _messageContext.getTransportOut() != null && _messageContext.getTransportOut().getSender() != null) {
                try {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                } catch (AxisFault ex) {
                    LOG.error(null, ex);
                }
            }
        }
    }

    /**
     * A utility method that copies the namespaces from the SOAPEnvelope
     */
    private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env) {
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();

        while (namespaceIterator.hasNext()) {
            org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
        }

        return returnMap;
    }

    private javax.xml.namespace.QName[] opNameArray = null;

    private boolean optimizeContent(javax.xml.namespace.QName opName) {

        if (opNameArray == null) {
            return false;
        }
        for (int i = 0; i < opNameArray.length; i++) {
            if (opName.equals(opNameArray[i])) {
                return true;
            }
        }
        return false;
    }

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
            Runtime.getRuntime().exit(-1);
        } finally {
            wsContext = jc;
        }
    }

    private org.apache.axiom.om.OMElement toOM(org.hl7.v3.PRPAIN201305UV02 param, boolean optimizeContent)
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
                    XCPDConstants.HL7_V3_NAMESPACE_URI, XCPDConstants.PATIENT_DISCOVERY_REQUEST);
            org.apache.axiom.om.OMNamespace namespace = factory
                    .createOMNamespace(XCPDConstants.HL7_V3_NAMESPACE_URI, null);
            return factory.createOMElement(source, XCPDConstants.PATIENT_DISCOVERY_REQUEST,
                    namespace);
        } catch (javax.xml.bind.JAXBException bex) {
            throw org.apache.axis2.AxisFault.makeFault(bex);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.hl7.v3.PRPAIN201305UV02 param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        org.apache.axiom.soap.SOAPEnvelope envelope = factory
                .getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent));
        return envelope;
    }

    private org.apache.axiom.om.OMElement toOM(org.hl7.v3.PRPAIN201306UV02 param, boolean optimizeContent)
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
                    XCPDConstants.HL7_V3_NAMESPACE_URI, XCPDConstants.PATIENT_DISCOVERY_RESPONSE);
            org.apache.axiom.om.OMNamespace namespace = factory
                    .createOMNamespace(XCPDConstants.HL7_V3_NAMESPACE_URI, null);
            return factory.createOMElement(source, XCPDConstants.PATIENT_DISCOVERY_RESPONSE,
                    namespace);
        } catch (javax.xml.bind.JAXBException bex) {
            throw org.apache.axis2.AxisFault.makeFault(bex);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.hl7.v3.PRPAIN201306UV02 param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        org.apache.axiom.soap.SOAPEnvelope envelope = factory
                .getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent));
        return envelope;
    }

    /**
     * get the default envelope
     */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory) {
        return factory.getDefaultEnvelope();
    }

    private Object fromOM(org.apache.axiom.om.OMElement param, Class type, java.util.Map extraNamespaces)
            throws org.apache.axis2.AxisFault {
        try {
            javax.xml.bind.JAXBContext context = wsContext;
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();

            return unmarshaller.unmarshal(param.getXMLStreamReaderWithoutCaching(), type).getValue();

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
        public JaxbRIDataSource(Class clazz, Object obj, javax.xml.bind.Marshaller marshaller, String nsuri, String name) {
            this.outClazz = clazz;
            this.outObject = obj;
            this.marshaller = marshaller;
            this.nsuri = nsuri;
            this.name = name;
        }

        public void serialize(java.io.OutputStream output, org.apache.axiom.om.OMOutputFormat format)
                throws javax.xml.stream.XMLStreamException {
            try {
                marshaller.marshal(
                        new javax.xml.bind.JAXBElement(new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), output);

            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException("Error in JAXB marshalling", e);
            }
        }

        public void serialize(java.io.Writer writer, org.apache.axiom.om.OMOutputFormat format)
                throws javax.xml.stream.XMLStreamException {
            try {
                marshaller.marshal(
                        new javax.xml.bind.JAXBElement(new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), writer);

            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException("Error in JAXB marshalling", e);
            }
        }

        public void serialize(javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            try {
                marshaller.marshal(new javax.xml.bind.JAXBElement(new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), xmlWriter);

            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException("Error in JAXB marshalling", e);
            }
        }

        public javax.xml.stream.XMLStreamReader getReader()
                throws javax.xml.stream.XMLStreamException {
            try {
                javax.xml.bind.JAXBContext context = wsContext;
                org.apache.axiom.om.impl.builder.SAXOMBuilder builder = new org.apache.axiom.om.impl.builder.SAXOMBuilder();
                javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                marshaller.marshal(new javax.xml.bind.JAXBElement(new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), builder);

                return builder.getRootElement().getXMLStreamReader();

            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException("Error in JAXB marshalling", e);
            }
        }
    }

    private EventLog createAndSendEventLog(PRPAIN201305UV02 sended, PRPAIN201306UV02 received, org.apache.axis2.context.MessageContext msgContext, org.apache.axiom.soap.SOAPEnvelope _returnEnv, org.apache.axiom.soap.SOAPEnvelope env, Assertion idAssertion, String address) {
        EventLog eventLog = EventLogClientUtil.prepareEventLog(msgContext, _returnEnv, address);
        EventLogClientUtil.logIdAssertion(eventLog, idAssertion);
        EventLogUtil.prepareXCPDCommonLog(eventLog, sended, received);
        EventLogClientUtil.sendEventLog(eventLog);
        return eventLog;
    }
}
