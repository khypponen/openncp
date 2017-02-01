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
package tr.com.srdc.epsos.ws.xdr.client;

import com.spirit.epsos.cc.adc.EadcEntry;
import ee.affecto.epsos.util.EventLogClientUtil;
import ee.affecto.epsos.util.EventLogUtil;
import epsos.ccd.gnomon.auditmanager.EventLog;
import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.ccd.gnomon.auditmanager.EventType;
import eu.epsos.pt.eadc.EadcUtilWrapper;
import eu.epsos.pt.eadc.util.EadcUtil;
import eu.epsos.util.EvidenceUtils;
import eu.epsos.util.xdr.XDRConstants;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.datamodel.xd.XdModel;
import eu.epsos.validation.services.XdrValidationService;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.activation.DataHandler;
import javax.xml.parsers.ParserConfigurationException;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.soap.impl.llom.soap12.SOAP12HeaderBlockImpl;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.util.XMLUtils;
import org.slf4j.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.com.srdc.epsos.util.DateUtil;
import tr.com.srdc.epsos.util.XMLUtil;

public class DocumentRecipient_ServiceStub extends org.apache.axis2.client.Stub {

    private static Logger LOG = LoggerFactory.getLogger(DocumentRecipient_ServiceStub.class);
    private static int counter = 0;
    // http://servicelocation/DocumentRecipient_Service
    private static final javax.xml.bind.JAXBContext wsContext;
    private org.apache.axis2.description.AxisOperation[] _operations;
    // hashmaps to keep the fault mapping
    private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
    private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
    private java.util.HashMap faultMessageMap = new java.util.HashMap();
    private javax.xml.namespace.QName[] opNameArray = null;
    private String countryCode;
    private Date transactionStartTime;
    private Date transactionEndTime;

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    static {
        javax.xml.bind.JAXBContext jc;
        jc = null;
        try {
            jc = javax.xml.bind.JAXBContext.newInstance(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.class, RegistryResponseType.class);

        } catch (javax.xml.bind.JAXBException ex) {
            LOG.error("Unable to create JAXBContext: " + ex.getMessage());
            LOG.error(ex.getLocalizedMessage(), ex);

            Runtime.getRuntime().exit(-1);

        } finally {
            wsContext = jc;
        }
    }


    /*
     * Contructors
     */
    /**
     * Constructor that takes in a configContext
     */
    public DocumentRecipient_ServiceStub(ConfigurationContext configurationContext, String targetEndpoint) throws AxisFault {
        this(configurationContext, targetEndpoint, false);
    }

    /**
     * Constructor that takes in a configContext and useseperate listner
     */
    public DocumentRecipient_ServiceStub(ConfigurationContext configurationContext, String targetEndpoint, boolean useSeparateListener) throws AxisFault {
        // To populate AxisService
        populateAxisService();
        populateFaults();

        _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext, _service);

        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);

        _serviceClient.getOptions().setTimeOutInMilliSeconds(180000); //Wait time after which a client times out in a blocking scenario: 3 minutes

        // Set the soap version
        _serviceClient.getOptions().setSoapVersionURI(org.apache.axiom.soap.SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

        _serviceClient.getOptions().setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);

    }

    /**
     * Default Constructor
     */
    public DocumentRecipient_ServiceStub(ConfigurationContext configurationContext) throws AxisFault {
        this(configurationContext, "http://195.142.27.167:8111/epsos/services/xdsrepositoryb");
    }

    /**
     * Default Constructor
     */
    public DocumentRecipient_ServiceStub() throws AxisFault {
        this("http://195.142.27.167:8111/epsos/services/xdsrepositoryb");
    }

    /**
     * Constructor taking the target endpoint
     */
    public DocumentRecipient_ServiceStub(String targetEndpoint) throws AxisFault {
        this(null, targetEndpoint);
    }

    /*
     * Methods
     */
    /**
     * Auto generated method signature
     *
     * @see
     * tr.com.srdc.epsos.ws.xdr.client.DocumentRecipient_Service#documentRecipient_ProvideAndRegisterDocumentSetB
     * @param provideAndRegisterDocumentSetRequest
     */
    public RegistryResponseType documentRecipient_ProvideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType provideAndRegisterDocumentSetRequest,
            Assertion idAssertion, Assertion trcAssertion)
            throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
            _operationClient.getOptions().setAction(XDRConstants.SOAP_HEADERS.REQUEST_ACTION);
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    provideAndRegisterDocumentSetRequest,
                    optimizeContent(new javax.xml.namespace.QName(XDRConstants.NAMESPACE_URI,
                                    XDRConstants.SOAP_HEADERS.NAMESPACE_REQUEST_LOCAL_PART)));

            /*
             * adding SOAP soap_headers
             */
            SOAPFactory soapFactory = getFactory(_operationClient.getOptions().getSoapVersionURI());
            OMFactory factory = OMAbstractFactory.getOMFactory();

            OMNamespace ns2 = factory.createOMNamespace(XDRConstants.SOAP_HEADERS.OM_NAMESPACE, "");

            SOAPHeaderBlock action = new SOAP12HeaderBlockImpl(XDRConstants.SOAP_HEADERS.ACTION_STR, ns2, soapFactory);
            OMNode node = factory.createOMText(XDRConstants.SOAP_HEADERS.REQUEST_ACTION);
            action.addChild(node);

            OMAttribute att = factory.createOMAttribute(XDRConstants.SOAP_HEADERS.MUST_UNDERSTAND_STR, env.getNamespace(), "1");
            action.addAttribute(att);

            SOAPHeaderBlock id = new SOAP12HeaderBlockImpl(XDRConstants.SOAP_HEADERS.MESSAGEID_STR, ns2, soapFactory);
            OMNode node2 = factory.createOMText(tr.com.srdc.epsos.util.Constants.UUID_PREFIX + UUID.randomUUID().toString());
            id.addChild(node2);

            Element idAssertionElement = idAssertion.getDOM();

            Element trcAssertionElement = trcAssertion.getDOM();

            OMNamespace ns = factory.createOMNamespace(XDRConstants.SOAP_HEADERS.SECURITY_XSD,
                    "wsse");

            SOAPHeaderBlock security = new SOAP12HeaderBlockImpl(XDRConstants.SOAP_HEADERS.SECURITY_STR, ns, soapFactory);

            try {
                security.addChild(XMLUtils.toOM(trcAssertionElement));
                security.addChild(XMLUtils.toOM(idAssertionElement));
                _serviceClient.addHeader(security);

            } catch (Exception e) {
                LOG.error(e.getLocalizedMessage(), e);
            }

            _serviceClient.addHeader(action);
            _serviceClient.addHeader(id);
            _serviceClient.addHeadersToEnvelope(env);

            /*
             * Prepare request
             */
            _messageContext.setEnvelope(env);   // set the message context with that soap envelope
            _operationClient.addMessageContext(_messageContext);    // add the message contxt to the operation client

            /* Log soap request */
            String requestLogMsg;
            try {
                String logRequestMsg = XMLUtil.prettyPrint(XMLUtils.toDOM(env));
                LOG.debug(XDRConstants.LOG.OUTGOING_XDR_PROVIDEANDREGISTER_MESSAGE
                        + System.getProperty("line.separator") + logRequestMsg);
                requestLogMsg = XMLUtil.prettyPrint(XMLUtils.toDOM(env.getBody()));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            // NRO
            try {
                EvidenceUtils.createEvidenceREMNRO(XMLUtil.prettyPrint(XMLUtils.toDOM(env)),
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                        EventType.epsosDispensationServiceInitialize.getCode(),
                        new DateTime(),
                        EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                        "NCPB_XDR_SUBMIT_REQ");
            } catch (Exception e) {
                LOG.error(ExceptionUtils.getStackTrace(e));
            }

            /* Perform validation of request message */
            XdrValidationService.getInstance().validateModel(requestLogMsg, XdModel.obtainModelXdr(requestLogMsg).toString(), NcpSide.NCP_B);

            /*
             * Execute Operation
             */
            transactionStartTime = new Date();
            org.apache.axiom.soap.SOAPEnvelope returnEnv;
            _operationClient.execute(true);
            org.apache.axis2.context.MessageContext _returnMessageContext;
            _returnMessageContext = _operationClient.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            returnEnv = _returnMessageContext.getEnvelope();
            transactionEndTime = new Date();

            /*
             * Invoque eADC
             */
            try {
                EadcUtilWrapper.invokeEadc(_messageContext, // Request message context
                        _returnMessageContext, // Response message context
                        this._getServiceClient(), //Service Client
                        null, // CDA document
                        transactionStartTime, // Transaction Start Time
                        transactionEndTime, // Transaction End Time
                        this.countryCode, // Country A ISO Code
                        EadcEntry.DsTypes.XDR, // Data source type
                        EadcUtil.Direction.OUTBOUND); // Transaction direction
            } catch (ParserConfigurationException ex) {
                LOG.error("EADC INVOCATION FAILED!", ex);
            } catch (Exception ex) {
                LOG.error("EADC INVOCATION FAILED!", ex);
            }

            /* Log soap response */
            String responseLogMsg;
            try {
                String logResponseMsg = XMLUtil.prettyPrint(XMLUtils.toDOM(returnEnv));
                LOG.debug(XDRConstants.LOG.INCOMING_XDR_PROVIDEANDREGISTER_MESSAGE
                        + System.getProperty("line.separator") + logResponseMsg);
                responseLogMsg = XMLUtil.prettyPrint(XMLUtils.toDOM(returnEnv.getBody()));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            /* Perform validation of response message */
            XdrValidationService.getInstance().validateModel(responseLogMsg, XdModel.obtainModelXdr(requestLogMsg).toString(), NcpSide.NCP_B);

            /*
             * Return
             */
            Object object = fromOM(returnEnv.getBody().getFirstElement(), RegistryResponseType.class);
            RegistryResponseType registryResponse = (RegistryResponseType) object;
            EventLog eventLog = createAndSendEventLogConsent(provideAndRegisterDocumentSetRequest, registryResponse.getRegistryErrorList(),
                    _messageContext, returnEnv, env, idAssertion, trcAssertion, this._getServiceClient().getOptions().getTo().getAddress());

            // Call to Evidence Emitter
            try {
                EvidenceUtils.createEvidenceREMNRR(XMLUtil.prettyPrint(XMLUtils.toDOM(returnEnv)),
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                        EventType.epsosDispensationServiceInitialize.getCode(),
                        DateUtil.GregorianCalendarToJodaTime(eventLog.getEI_EventDateTime()),
                        EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                        "NCPB_XDR_SUBMIT_RES");
            } catch (Exception e) {
                LOG.error(ExceptionUtils.getStackTrace(e));
            }

            LOG.info("Submission Time is : " + eventLog.getEI_EventDateTime());
            LOG.info("EventType is : " + eventLog.getEventType());
            LOG.info("Event Outcome is: " + eventLog.getEI_EventOutcomeIndicator().toString());
            LOG.info("KEYSTORE PATH: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH);
            LOG.info("KEYSTORE PWD: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD);
            LOG.info("KEY ALIAS: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS);
            LOG.info("PRIVATE KEY PASSWORD: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_PASSWORD);

            return registryResponse;

        } catch (AxisFault f) {
            // TODO audit log on exception

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(faultElt.getQName())) {
                    // make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap.get(faultElt.getQName());
                        Class exceptionClass = Class.forName(exceptionClassName);
                        Exception ex = (Exception) exceptionClass.newInstance();
                        // message class
                        String messageClassName = (String) faultMessageMap.get(faultElt.getQName());
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt, messageClass);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage", new Class[]{messageClass});
                        m.invoke(ex, new Object[]{messageObject});

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);

                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);    // we cannot intantiate the class - throw the original Axis fault
                    }
                }
            }

            throw new RuntimeException(f.getMessage(), f);

        } finally {
            if (_messageContext != null && _messageContext.getTransportOut() != null && _messageContext.getTransportOut().getSender() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

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

    private org.apache.axiom.om.OMElement toOM(oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest param)
            throws AxisFault {
        try {
            javax.xml.bind.JAXBContext context = wsContext;
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

            DocumentRecipient_ServiceStub.JaxbRIDataSource source;
            source = new DocumentRecipient_ServiceStub.JaxbRIDataSource(oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest.class,
                    param, marshaller, XDRConstants.REGREP_LCM,
                    "SubmitObjectsRequest");
            org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace(XDRConstants.REGREP_LCM, null);
            return factory.createOMElement(source, "SubmitObjectsRequest", namespace);

        } catch (javax.xml.bind.JAXBException bex) {
            throw AxisFault.makeFault(bex);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType param, boolean optimizeContent)
            throws AxisFault {
        org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();

        org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace(XDRConstants.NAMESPACE_URI, "xdr");

        OMElement provideAndRegisterDoc = factory.createOMElement(XDRConstants.PROVIDE_AND_REGISTER_DOCUMENT_SET_REQ_STR, namespace);
        OMElement submitObjectsRequest = toOM(param.getSubmitObjectsRequest());
        provideAndRegisterDoc.addChild(submitObjectsRequest);
        envelope.getBody().addChild(provideAndRegisterDoc);

        List<ProvideAndRegisterDocumentSetRequestType.Document> documents = param.getDocument();
        for (ProvideAndRegisterDocumentSetRequestType.Document document : documents) {
            OMElement documentElement = factory.createOMElement("Document", namespace);
            provideAndRegisterDoc.addChild(submitObjectsRequest);

            ByteArrayDataSource rawData = new ByteArrayDataSource(document.getValue());
            DataHandler dH = new DataHandler(rawData);
//            DataHandler dH = document.getValue();
            org.apache.axiom.om.OMText textData = factory.createOMText(dH, true);
            textData.setOptimize(true);
            textData.setContentID(document.getId());
            String contentID = textData.getContentID();

            OMAttribute att = factory.createOMAttribute("id", null, contentID);
            documentElement.addAttribute(att);

            documentElement.addChild(textData);
            provideAndRegisterDoc.addChild(documentElement);
        }

        return envelope;
    }

    private Object fromOM(org.apache.axiom.om.OMElement param, Class type)
            throws AxisFault {
        try {
            javax.xml.bind.JAXBContext context = wsContext;
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();

            return unmarshaller.unmarshal(param.getXMLStreamReaderWithoutCaching(), type).getValue();

        } catch (javax.xml.bind.JAXBException bex) {
            throw AxisFault.makeFault(bex);
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
                marshaller.marshal(new javax.xml.bind.JAXBElement(
                        new javax.xml.namespace.QName(nsuri, name), outObject
                        .getClass(), outObject), output);
            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException(
                        XDRConstants.EXCEPTIONS.ERROR_JAXB_MARSHALLING, e);
            }
        }

        public void serialize(java.io.Writer writer,
                org.apache.axiom.om.OMOutputFormat format)
                throws javax.xml.stream.XMLStreamException {
            try {
                marshaller.marshal(new javax.xml.bind.JAXBElement(
                        new javax.xml.namespace.QName(nsuri, name), outObject
                        .getClass(), outObject), writer);
            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException(
                        XDRConstants.EXCEPTIONS.ERROR_JAXB_MARSHALLING, e);
            }
        }

        public void serialize(javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            try {
                marshaller.marshal(new javax.xml.bind.JAXBElement(
                        new javax.xml.namespace.QName(nsuri, name), outObject
                        .getClass(), outObject), xmlWriter);
            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException(
                        XDRConstants.EXCEPTIONS.ERROR_JAXB_MARSHALLING, e);
            }
        }

        public javax.xml.stream.XMLStreamReader getReader()
                throws javax.xml.stream.XMLStreamException {
            try {
                javax.xml.bind.JAXBContext context = wsContext;
                org.apache.axiom.om.impl.builder.SAXOMBuilder builder = new org.apache.axiom.om.impl.builder.SAXOMBuilder();
                javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                marshaller.marshal(new javax.xml.bind.JAXBElement(
                        new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), builder);

                return builder.getRootElement().getXMLStreamReader();

            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException(XDRConstants.EXCEPTIONS.ERROR_JAXB_MARSHALLING, e);
            }
        }
    }

    // TODO A.R. eDispensation handling
    private EventLog createAndSendEventLogConsent(ProvideAndRegisterDocumentSetRequestType request, RegistryErrorList rel,
            org.apache.axis2.context.MessageContext msgContext,
            org.apache.axiom.soap.SOAPEnvelope _returnEnv, org.apache.axiom.soap.SOAPEnvelope env, Assertion idAssertion, Assertion trcAssertion, String address) {
        EventLog eventLog = EventLogClientUtil.prepareEventLog(msgContext, _returnEnv, address);
        EventLogClientUtil.logIdAssertion(eventLog, idAssertion);
        EventLogClientUtil.logTrcAssertion(eventLog, trcAssertion);
        EventLogUtil.prepareXDRCommonLog(eventLog, request, rel);
        EventLogClientUtil.sendEventLog(eventLog);
        return eventLog;
    }

    private static synchronized String getUniqueSuffix() {
        if (counter > 99999) {
            counter = 0;    // reset the counter if it is greater than 99999
        }
        counter++;
        return Long.toString(System.currentTimeMillis()) + "_" + counter;
    }

    private void populateAxisService() throws AxisFault {

        // creating the Service with a unique name
        _service = new org.apache.axis2.description.AxisService(XDRConstants.DOCUMENT_RECIPIENT_SERVICE_STR + getUniqueSuffix());
        addAnonymousOperations();

        // creating the operations
        org.apache.axis2.description.AxisOperation __operation;

        _operations = new org.apache.axis2.description.AxisOperation[1];

        __operation = new org.apache.axis2.description.OutInAxisOperation();

        __operation.setName(new javax.xml.namespace.QName(XDRConstants.NAMESPACE_URI,
                XDRConstants.PROVIDE_AND_REGISTER_DOCUMENT_SET_REQ_STR));
        _service.addOperation(__operation);

        _operations[0] = __operation;

    }

    // populates the faults
    private void populateFaults() {
    }
}
