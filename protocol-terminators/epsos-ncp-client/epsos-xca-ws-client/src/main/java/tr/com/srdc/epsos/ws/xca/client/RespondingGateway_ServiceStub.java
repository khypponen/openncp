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
package tr.com.srdc.epsos.ws.xca.client;

import com.spirit.epsos.cc.adc.EadcEntry;
import ee.affecto.epsos.util.EventLogClientUtil;
import ee.affecto.epsos.util.EventLogUtil;
import epsos.ccd.gnomon.auditmanager.EventLog;
import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.ccd.gnomon.auditmanager.EventType;
import eu.epsos.pt.eadc.EadcUtilWrapper;
import eu.epsos.pt.eadc.util.EadcUtil.Direction;
import eu.epsos.pt.transformation.TMServices;
import eu.epsos.util.EvidenceUtils;
import eu.epsos.util.xca.XCAConstants;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.datamodel.xd.XdModel;
import eu.epsos.validation.services.XcaValidationService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.Date;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.soap.impl.llom.soap12.SOAP12HeaderBlockImpl;
import org.apache.axis2.util.XMLUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.XMLUtil;

/*
 *  RespondingGateway_ServiceStub java implementation
 */
public class RespondingGateway_ServiceStub extends org.apache.axis2.client.Stub {

    protected org.apache.axis2.description.AxisOperation[] _operations;
    private static Logger LOG = LoggerFactory.getLogger(RespondingGateway_ServiceStub.class);
    private static int counter = 0;
    //hashmaps to keep the fault mapping
    private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
    private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
    private java.util.HashMap faultMessageMap = new java.util.HashMap();
    private String addr;
    private String countryCode;
    private Date transactionStartTime;
    private Date transactionEndTime;

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    private static synchronized java.lang.String getUniqueSuffix() {
        // reset the counter if it is greater than 99999
        if (counter > 99999) {
            counter = 0;
        }
        counter++;
        return java.lang.Long.toString(System.currentTimeMillis()) + "_" + counter;
    }

    private void populateAxisService() throws org.apache.axis2.AxisFault {
        //creating the Service with a unique name
        _service = new org.apache.axis2.description.AxisService(XCAConstants.RESPONDING_GATEWAY_SERVICE + getUniqueSuffix());
        addAnonymousOperations();
        //creating the operations
        org.apache.axis2.description.AxisOperation __operation;
        _operations = new org.apache.axis2.description.AxisOperation[2];
        __operation = new org.apache.axis2.description.OutInAxisOperation();
        __operation.setName(new javax.xml.namespace.QName(XCAConstants.SOAP_HEADERS.NAMESPACE_URI, XCAConstants.SOAP_HEADERS.QUERY.NAMESPACE_REQUEST_LOCAL_PART));
        _service.addOperation(__operation);
        _operations[0] = __operation;
        __operation = new org.apache.axis2.description.OutInAxisOperation();
        __operation.setName(new javax.xml.namespace.QName(XCAConstants.SOAP_HEADERS.NAMESPACE_URI, XCAConstants.SOAP_HEADERS.RETRIEVE.NAMESPACE_REQUEST_LOCAL_PART));
        _service.addOperation(__operation);
        _operations[1] = __operation;
    }

    //populates the faults
    private void populateFaults() {
    }

    /**
     * Constructor that takes in a configContext
     */
    public RespondingGateway_ServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext, java.lang.String targetEndpoint)
            throws org.apache.axis2.AxisFault {
        this(configurationContext, targetEndpoint, false);
    }

    /**
     * Constructor that takes in a configContext and useseperate listner
     */
    public RespondingGateway_ServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext, java.lang.String targetEndpoint, boolean useSeparateListener)
            throws org.apache.axis2.AxisFault {
        //To populate AxisService
        populateAxisService();
        populateFaults();

        _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext, _service);

        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);

        _serviceClient.getOptions().setTimeOutInMilliSeconds(180000); //Wait time after which a client times out in a blocking scenario: 3 minutes

        //Set the soap version
        _serviceClient.getOptions().setSoapVersionURI(org.apache.axiom.soap.SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
    }

    /**
     * Default Constructor
     */
    public RespondingGateway_ServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext)
            throws org.apache.axis2.AxisFault {
        this(configurationContext, "http://195.142.27.167:8111/tr-xca/services/RespondingGateway_Service");
    }

    /**
     * Default Constructor
     */
    public RespondingGateway_ServiceStub() throws org.apache.axis2.AxisFault {
        this("http://195.142.27.167:8111/tr-xca/services/RespondingGateway_Service");
    }

    /**
     * Constructor taking the target endpoint
     */
    public RespondingGateway_ServiceStub(java.lang.String targetEndpoint)
            throws org.apache.axis2.AxisFault {
        this(null, targetEndpoint);
    }

    /**
     * Auto generated method signature
     *
     * @see
     * tr.com.srdc.epsos.test.xca.RespondingGateway_Service#respondingGateway_CrossGatewayQuery
     * @param adhocQueryRequest
     *
     */
    public AdhocQueryResponse respondingGateway_CrossGatewayQuery(AdhocQueryRequest adhocQueryRequest,
            Assertion idAssertion,
            Assertion trcAssertion,
            String classCode)
            throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try {
            // TMP
            // XCA list request start time
            long start = System.currentTimeMillis();

            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
            _operationClient.getOptions().setAction(XCAConstants.SOAP_HEADERS.QUERY.REQUEST_ACTION);
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env;
            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    adhocQueryRequest,
                    optimizeContent(new javax.xml.namespace.QName(XCAConstants.SOAP_HEADERS.NAMESPACE_URI, XCAConstants.SOAP_HEADERS.QUERY.NAMESPACE_REQUEST_LOCAL_PART)));

            /*
             * adding SOAP soap_headers
             */
            SOAPFactory soapFactory = getFactory(_operationClient.getOptions().getSoapVersionURI());
            OMFactory factory = OMAbstractFactory.getOMFactory();

            OMNamespace ns2 = factory.createOMNamespace(XCAConstants.SOAP_HEADERS.QUERY.OM_NAMESPACE, "addressing");

            SOAPHeaderBlock action = new SOAP12HeaderBlockImpl("Action", ns2, soapFactory);
            OMNode node = factory.createOMText(XCAConstants.SOAP_HEADERS.QUERY.REQUEST_ACTION);
            action.addChild(node);
            OMAttribute att1 = factory.createOMAttribute(XCAConstants.SOAP_HEADERS.MUST_UNDERSTAND, env.getNamespace(), "1");
            action.addAttribute(att1);

            SOAPHeaderBlock id = new SOAP12HeaderBlockImpl("MessageID", ns2, soapFactory);
            OMNode node2 = factory.createOMText(Constants.UUID_PREFIX + UUID.randomUUID().toString());
            id.addChild(node2);

            SOAPHeaderBlock to = new SOAP12HeaderBlockImpl("To", ns2, soapFactory);
            OMNode node3 = factory.createOMText(addr);
            to.addChild(node3);
            OMAttribute att2 = factory.createOMAttribute(XCAConstants.SOAP_HEADERS.MUST_UNDERSTAND, env.getNamespace(), "1");
            to.addAttribute(att2);

            SOAPHeaderBlock replyTo = new SOAP12HeaderBlockImpl("ReplyTo", ns2, soapFactory);
            OMElement address = new SOAP12HeaderBlockImpl("Address", ns2, soapFactory);
            OMNode node4 = factory.createOMText(XCAConstants.SOAP_HEADERS.ADDRESSING_NAMESPACE);
            address.addChild(node4);
            replyTo.addChild(address);

            _serviceClient.addHeader(to);
            _serviceClient.addHeader(action);
            _serviceClient.addHeader(id);
            _serviceClient.addHeader(replyTo);

            OMNamespace ns = factory.createOMNamespace(XCAConstants.SOAP_HEADERS.SECURITY_XSD, "wsse");
            SOAPHeaderBlock security = new SOAP12HeaderBlockImpl("Security", ns, soapFactory);
            try {
                security.addChild(XMLUtils.toOM(trcAssertion.getDOM()));
                security.addChild(XMLUtils.toOM(idAssertion.getDOM()));
                _serviceClient.addHeader(security);
            } catch (Exception ex) {
                LOG.error(ex.getLocalizedMessage(), ex);
            }

            _serviceClient.addHeadersToEnvelope(env);

            /*
             * Prepare request
             */
            _messageContext.setEnvelope(env);   // set the message context with that soap envelope
            _operationClient.addMessageContext(_messageContext);    // add the message contxt to the operation client

            /* Log soap request */
            String logRequestBody;
            try {
                String logRequestMsg = XMLUtil.prettyPrint(XMLUtils.toDOM(env));
                LOG.debug(XCAConstants.LOG.OUTGOING_XCA_QUERY_MESSAGE
                        + System.getProperty("line.separator") + logRequestMsg);
                logRequestBody = XMLUtil.prettyPrint(XMLUtils.toDOM(env.getBody().getFirstElement()));
                // NRO
                try {
                    EvidenceUtils.createEvidenceREMNRO(XMLUtil.prettyPrint(XMLUtils.toDOM(env)),
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                            EventType.epsosOrderServiceList.getCode(),
                            new DateTime(),
                            EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                            "NCPB_XCA_LIST_DOC_REQ");
                } catch (Exception e) {
                    LOG.error(ExceptionUtils.getStackTrace(e));
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            // TMP
            // XCA List response end time
            long end = System.currentTimeMillis();
            LOG.info("XCA LIST REQUEST-RESPONSE TIME: " + (end - start) / 1000.0);

            // TMP
            // Validation start time
            start = System.currentTimeMillis();

            /* Validate Request Message */
            XcaValidationService.getInstance().validateModel(logRequestBody, XdModel.obtainModelXca(logRequestBody).toString(), NcpSide.NCP_B);

            // TMP
            // Transaction end time
            end = System.currentTimeMillis();
            LOG.info("XCA LIST VALIDATION REQ TIME: " + (end - start) / 1000.0);

            // TMP
            // Transaction start time
            start = System.currentTimeMillis();

            /*
             * Execute Operation
             */
            transactionStartTime = new Date();
            _operationClient.execute(true);
            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
            transactionEndTime = new Date();

            // TMP
            // Transaction end time
            end = System.currentTimeMillis();
            LOG.info("XCA LIST TRANSACTION TIME: " + (end - start) / 1000.0);

            // TMP
            // Transaction start time
            start = System.currentTimeMillis();

            /* Log soap response */
            String logResponseBody;
            try {
                String logResponseMsg = XMLUtil.prettyPrint(XMLUtils.toDOM(_returnEnv));
                LOG.debug(XCAConstants.LOG.INCOMING_XCA_QUERY_MESSAGE
                        + System.getProperty("line.separator") + logResponseMsg);
                logResponseBody = XMLUtil.prettyPrint(XMLUtils.toDOM(_returnEnv.getBody().getFirstElement()));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            /* Validate Response Message */
            XcaValidationService.getInstance().validateModel(logResponseBody, XdModel.obtainModelXca(logResponseBody).toString(), NcpSide.NCP_B);

            // TMP
            // Validation end time
            end = System.currentTimeMillis();
            LOG.info("XCA LIST VALIDATION RES TIME: " + (end - start) / 1000);

            // TMP
            // eADC start time
            start = System.currentTimeMillis();

            // NRR
            try {
                EvidenceUtils.createEvidenceREMNRR(XMLUtil.prettyPrint(XMLUtils.toDOM(env)),
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                        EventType.epsosOrderServiceList.getCode(),
                        new DateTime(),
                        EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                        "NCPB_XCA_LIST_RES");
            } catch (Exception e) {
                LOG.error(ExceptionUtils.getStackTrace(e));
            }

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
                        EadcEntry.DsTypes.XCA, // Data source type
                        Direction.OUTBOUND); // Transaction direction
            } catch (ParserConfigurationException ex) {
                LOG.error("EADC INVOCATION FAILED!", ex);
            } catch (Exception ex) {
                LOG.error("EADC INVOCATION FAILED!", ex);
            }

            // TMP
            // eADC end time
            end = System.currentTimeMillis();
            LOG.info("XCA LIST eADC TIME: " + (end - start) / 1000.0);

            // TMP
            // Audit start time
            start = System.currentTimeMillis();

            /*
             * Return
             */
            java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(),
                    oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse.class,
                    getEnvelopeNamespaces(_returnEnv));
            AdhocQueryResponse adhocQueryResponse = (AdhocQueryResponse) object;
            EventLog eventLog = createAndSendEventLogQuery(adhocQueryRequest, adhocQueryResponse,
                    _messageContext, _returnEnv, env, idAssertion, trcAssertion,
                    this._getServiceClient().getOptions().getTo().getAddress(),
                    classCode); // Audit

            LOG.info("Submission Time is : " + eventLog.getEI_EventDateTime());
            LOG.info("EventType is : " + eventLog.getEventType());
            LOG.info("Event Outcome is: " + eventLog.getEI_EventOutcomeIndicator().toString());
            LOG.info("KEYSTORE PATH: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH);
            LOG.info("KEYSTORE PWD: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD);
            LOG.info("KEY ALIAS: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS);
            LOG.info("PRIVATE KEY PASSWORD: " + tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_PASSWORD);
            // TMP
            // Audit end time
            end = System.currentTimeMillis();
            LOG.info("XCA LIST AUDIT TIME: " + (end - start) / 1000.0);

            return adhocQueryResponse;

        } catch (org.apache.axis2.AxisFault f) {
            // TODO A.R. Audit log SOAP Fault is still missing
            org.apache.axiom.om.OMElement faultElt = f.getDetail();

            if (faultElt != null) {

                if (faultExceptionNameMap.containsKey(faultElt.getQName())) {

                    //make the fault by reflection
                    try {
                        java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex
                                = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String) faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex, new java.lang.Object[]{messageObject});

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);

                        /* we cannot intantiate the class - throw the original Axis fault */
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
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

    /**
     * Auto generated method signature
     *
     * @see
     * tr.com.srdc.epsos.test.xca.RespondingGateway_Service#respondingGateway_CrossGatewayRetrieve
     * @param retrieveDocumentSetRequest
     * @param idAssertion
     * @param trcAssertion
     * @param classCode Class code of the document to be retrieved, needed for
     * audit log preparation
     * @return
     * @throws java.rmi.RemoteException
     *
     */
    public RetrieveDocumentSetResponseType respondingGateway_CrossGatewayRetrieve(RetrieveDocumentSetRequestType retrieveDocumentSetRequest,
            Assertion idAssertion,
            Assertion trcAssertion,
            String classCode)
            throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        org.apache.axiom.soap.SOAPEnvelope env;
        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1].getName());
            _operationClient.getOptions().setAction(XCAConstants.SOAP_HEADERS.RETRIEVE.REQUEST_ACTION);
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
            addPropertyToOperationClient(_operationClient, org.apache.axis2.Constants.Configuration.ENABLE_MTOM, org.apache.axis2.Constants.VALUE_TRUE);

            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    retrieveDocumentSetRequest,
                    optimizeContent(new javax.xml.namespace.QName(XCAConstants.SOAP_HEADERS.NAMESPACE_URI, XCAConstants.SOAP_HEADERS.RETRIEVE.NAMESPACE_REQUEST_LOCAL_PART)));

            /*
             * adding SOAP soap_headers
             */
            SOAPFactory soapFactory = getFactory(_operationClient.getOptions().getSoapVersionURI());
            OMFactory factory = OMAbstractFactory.getOMFactory();

            OMNamespace ns2 = factory.createOMNamespace(XCAConstants.SOAP_HEADERS.ADDRESSING_NAMESPACE, "addressing");

            OMElement action = new SOAP12HeaderBlockImpl("Action", ns2, soapFactory);
            OMNode node = factory.createOMText(XCAConstants.SOAP_HEADERS.RETRIEVE.REQUEST_ACTION);
            action.addChild(node);

            //OMAttribute att1 = factory.createOMAttribute(XCAConstants.SOAP_HEADERS.MUST_UNDERSTAND, env.getNamespace(), "1");
            //action.addAttribute(att1);
            OMElement id = new SOAP12HeaderBlockImpl("MessageID", ns2, soapFactory);
            OMNode node2 = factory.createOMText(Constants.UUID_PREFIX + UUID.randomUUID().toString());
            id.addChild(node2);

            OMElement to = new SOAP12HeaderBlockImpl("To", ns2, soapFactory);
            OMNode node3 = factory.createOMText(addr);
            to.addChild(node3);

            //OMAttribute att2 = factory.createOMAttribute(XCAConstants.SOAP_HEADERS.MUST_UNDERSTAND, env.getNamespace(), "1");
            //to.addAttribute(att2);
            OMElement replyTo = new SOAP12HeaderBlockImpl("ReplyTo", ns2, soapFactory);
            OMElement address = new SOAP12HeaderBlockImpl("Address", ns2, soapFactory);
            OMNode node4 = factory.createOMText(XCAConstants.SOAP_HEADERS.ADDRESSING_NAMESPACE);
            address.addChild(node4);
            replyTo.addChild(address);

            _serviceClient.addHeader(to);
            _serviceClient.addHeader(action);
            _serviceClient.addHeader(id);
            _serviceClient.addHeader(replyTo);

            Element assertion1 = idAssertion.getDOM();
            Element assertion2 = trcAssertion.getDOM();
            OMNamespace ns = factory.createOMNamespace(XCAConstants.SOAP_HEADERS.SECURITY_XSD, "wsse");
            OMElement security = new SOAP12HeaderBlockImpl("Security", ns, soapFactory);

            try {
                security.addChild(XMLUtils.toOM(assertion2));
                security.addChild(XMLUtils.toOM(assertion1));
                _serviceClient.addHeader(security);
            } catch (Exception ex) {
                LOG.error(ex.getLocalizedMessage(), ex);
            }

            _serviceClient.addHeadersToEnvelope(env);

            /*
             * Prepare request
             */
            _messageContext.setEnvelope(env);   // set the message context with that soap envelope
            _operationClient.addMessageContext(_messageContext);    // add the message contxt to the operation client

            /* Log soap request */
            String logRequestBody;
            try {
                String logRequestMsg = XMLUtil.prettyPrint(XMLUtils.toDOM(env));
                LOG.debug(XCAConstants.LOG.OUTGOING_XCA_RETRIEVE_MESSAGE
                        + System.getProperty("line.separator") + logRequestMsg);
                logRequestBody = XMLUtil.prettyPrint(XMLUtils.toDOM(env.getBody().getFirstElement()));
                // NRO
                try {
                    EvidenceUtils.createEvidenceREMNRO(XMLUtil.prettyPrint(XMLUtils.toDOM(env)),
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                            tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                            EventType.epsosOrderServiceRetrieve.getCode(),
                            new DateTime(),
                            EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                            "NCPB_XCA_RETRIEVE_DOC_REQ");
                } catch (Exception e) {
                    LOG.error(ExceptionUtils.getStackTrace(e));
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            /* Validate Request Message */
            XcaValidationService.getInstance().validateModel(logRequestBody, XdModel.obtainModelXca(logRequestBody).toString(), NcpSide.NCP_B);

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

            /* Log soap response */
            String logResponseBody;
            try {
                String logResponseMsg = XMLUtil.prettyPrint(XMLUtils.toDOM(returnEnv));
                LOG.debug(XCAConstants.LOG.INCOMING_XCA_RETRIEVE_MESSAGE
                        + System.getProperty("line.separator") + logResponseMsg);
                logResponseBody = XMLUtil.prettyPrint(XMLUtils.toDOM(returnEnv.getBody().getFirstElement()));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            /* Validate Response Message */
            XcaValidationService.getInstance().validateModel(logResponseBody, XdModel.obtainModelXca(logResponseBody).toString(), NcpSide.NCP_B);

            /*
             * Return
             */
            RetrieveDocumentSetResponseType retrieveDocumentSetResponse;
            Object object = fromOM(returnEnv.getBody().getFirstElement(),
                    RetrieveDocumentSetResponseType.class,
                    getEnvelopeNamespaces(returnEnv));
            retrieveDocumentSetResponse = (RetrieveDocumentSetResponseType) object;

            LOG.info("XCA Retrieve Request received. EVIDENCE NRR");
            // NRR
            try {
                EvidenceUtils.createEvidenceREMNRR(XMLUtil.prettyPrint(XMLUtils.toDOM(env)),
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
                        tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
                        EventType.epsosOrderServiceRetrieve.getCode(),
                        new DateTime(),
                        EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                        "NCPB_XCA_RETRIEVE_RES");
            } catch (Exception e) {
                LOG.error(ExceptionUtils.getStackTrace(e));
            }

            /*
             * Invoque eADC
             */
            try {
                Document cda = null;
                if (retrieveDocumentSetResponse.getDocumentResponse() != null && !retrieveDocumentSetResponse.getDocumentResponse().isEmpty()) {
                    byte[] cdaBytes = retrieveDocumentSetResponse.getDocumentResponse().get(0).getDocument();
                    cda = TMServices.byteToDocument(cdaBytes);
                }

                EadcUtilWrapper.invokeEadc(_messageContext, // Request message context
                        _returnMessageContext, // Response message context
                        this._getServiceClient(), //Service Client
                        cda, // CDA document
                        transactionStartTime, // Transaction Start Time
                        transactionEndTime, // Transaction End Time
                        this.countryCode, // Country A ISO Code
                        EadcEntry.DsTypes.XCA, // Data source type
                        Direction.OUTBOUND); // Transaction direction
            } catch (ParserConfigurationException ex) {
                LOG.error("EADC INVOCATION FAILED!", ex);
            } catch (Exception ex) {
                LOG.error("EADC INVOCATION FAILED!", ex);
            }

            /*
             * Create Audit messages
             */
            EventLog eventLog = createAndSendEventLogRetrieve(retrieveDocumentSetRequest, retrieveDocumentSetResponse,
                    _messageContext, returnEnv, env, idAssertion, trcAssertion,
                    this._getServiceClient().getOptions().getTo().getAddress(),
                    classCode);

            return retrieveDocumentSetResponse;

        } catch (org.apache.axis2.AxisFault f) {
            // TODO A.R. Audit log SOAP Fault is still missing
            org.apache.axiom.om.OMElement faultElt = f.getDetail();

            if (faultElt != null) {

                if (faultExceptionNameMap.containsKey(faultElt.getQName())) {

                    //make the fault by reflection
                    try {
                        java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex
                                = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String) faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex, new java.lang.Object[]{messageObject});

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);

                    } catch (Exception e) { // we cannot intantiate the class - throw the original Axis fault
                        throw new RuntimeException(e.getMessage(), e);
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

    /**
     * A utility method that copies the namepaces from the SOAPEnvelope
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
    //http://localhost:8080/axis2/services/RespondingGateway_Soap12
    private static final javax.xml.bind.JAXBContext wsContext;

    static {
        javax.xml.bind.JAXBContext jc;
        jc = null;
        try {
            jc = javax.xml.bind.JAXBContext.newInstance(
                    oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest.class,
                    oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse.class,
                    RetrieveDocumentSetRequestType.class,
                    RetrieveDocumentSetResponseType.class);
        } catch (javax.xml.bind.JAXBException ex) {
            System.err.println(XCAConstants.EXCEPTIONS.UNABLE_CREATE_JAXB_CONTEXT + " " + ex.getMessage());
            ex.printStackTrace(System.err);
            Runtime.getRuntime().exit(-1);
        } finally {
            wsContext = jc;
        }
    }

    private org.apache.axiom.om.OMElement toOM(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            javax.xml.bind.JAXBContext context = wsContext;
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

            RespondingGateway_ServiceStub.JaxbRIDataSource source = new RespondingGateway_ServiceStub.JaxbRIDataSource(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest.class,
                    param,
                    marshaller,
                    XCAConstants.REGREP_QUERY,
                    XCAConstants.ADHOC_QUERY_REQUEST);
            org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace(XCAConstants.REGREP_QUERY,
                    null);
            return factory.createOMElement(source, XCAConstants.ADHOC_QUERY_REQUEST, namespace);
        } catch (javax.xml.bind.JAXBException bex) {
            throw org.apache.axis2.AxisFault.makeFault(bex);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent));
        return envelope;
    }

    private org.apache.axiom.om.OMElement toOM(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            javax.xml.bind.JAXBContext context = wsContext;
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

            RespondingGateway_ServiceStub.JaxbRIDataSource source = new RespondingGateway_ServiceStub.JaxbRIDataSource(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse.class,
                    param,
                    marshaller,
                    XCAConstants.REGREP_QUERY,
                    XCAConstants.ADHOC_QUERY_RESPONSE);
            org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace(XCAConstants.REGREP_QUERY,
                    null);
            return factory.createOMElement(source, XCAConstants.ADHOC_QUERY_RESPONSE, namespace);
        } catch (javax.xml.bind.JAXBException bex) {
            throw org.apache.axis2.AxisFault.makeFault(bex);
        }
    }

    @SuppressWarnings("unused")
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent));
        return envelope;
    }

    private org.apache.axiom.om.OMElement toOM(RetrieveDocumentSetRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            javax.xml.bind.JAXBContext context = wsContext;
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

            RespondingGateway_ServiceStub.JaxbRIDataSource source = new RespondingGateway_ServiceStub.JaxbRIDataSource(RetrieveDocumentSetRequestType.class,
                    param,
                    marshaller,
                    XCAConstants.SOAP_HEADERS.NAMESPACE_URI,
                    XCAConstants.RETRIEVE_DOCUMENTSET_REQUEST);
            org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace(XCAConstants.SOAP_HEADERS.NAMESPACE_URI,
                    null);
            return factory.createOMElement(source, XCAConstants.RETRIEVE_DOCUMENTSET_REQUEST, namespace);
        } catch (javax.xml.bind.JAXBException bex) {
            throw org.apache.axis2.AxisFault.makeFault(bex);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, RetrieveDocumentSetRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent));
        return envelope;
    }

    private org.apache.axiom.om.OMElement toOM(RetrieveDocumentSetResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            javax.xml.bind.JAXBContext context = wsContext;
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

            RespondingGateway_ServiceStub.JaxbRIDataSource source = new RespondingGateway_ServiceStub.JaxbRIDataSource(RetrieveDocumentSetResponseType.class,
                    param,
                    marshaller,
                    XCAConstants.SOAP_HEADERS.NAMESPACE_URI,
                    XCAConstants.RETRIEVE_DOCUMENTSET_RESPONSE);
            org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace(XCAConstants.SOAP_HEADERS.NAMESPACE_URI,
                    null);
            return factory.createOMElement(source, XCAConstants.RETRIEVE_DOCUMENTSET_RESPONSE, namespace);
        } catch (javax.xml.bind.JAXBException bex) {
            throw org.apache.axis2.AxisFault.makeFault(bex);
        }
    }

    @SuppressWarnings("unused")
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, RetrieveDocumentSetResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent));
        return envelope;
    }

    /**
     * get the default envelope
     */
    @SuppressWarnings("unused")
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory) {
        return factory.getDefaultEnvelope();
    }

    @SuppressWarnings("unchecked")
    private java.lang.Object fromOM(
            org.apache.axiom.om.OMElement param,
            java.lang.Class type,
            java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault {
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
        @SuppressWarnings("unused")
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

        @SuppressWarnings("unchecked")
        public void serialize(java.io.OutputStream output, org.apache.axiom.om.OMOutputFormat format) throws javax.xml.stream.XMLStreamException {
            try {
                marshaller.marshal(new javax.xml.bind.JAXBElement(
                        new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), output);
            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException(XCAConstants.EXCEPTIONS.ERROR_JAXB_MARSHALLING, e);
            }
        }

        @SuppressWarnings("unchecked")
        public void serialize(java.io.Writer writer, org.apache.axiom.om.OMOutputFormat format) throws javax.xml.stream.XMLStreamException {
            try {
                marshaller.marshal(new javax.xml.bind.JAXBElement(
                        new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), writer);
            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException(XCAConstants.EXCEPTIONS.ERROR_JAXB_MARSHALLING, e);
            }
        }

        @SuppressWarnings("unchecked")
        public void serialize(javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            try {
                marshaller.marshal(new javax.xml.bind.JAXBElement(
                        new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), xmlWriter);
            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException(XCAConstants.EXCEPTIONS.ERROR_JAXB_MARSHALLING, e);
            }
        }

        @SuppressWarnings("unchecked")
        public javax.xml.stream.XMLStreamReader getReader() throws javax.xml.stream.XMLStreamException {
            try {
                javax.xml.bind.JAXBContext context = wsContext;
                org.apache.axiom.om.impl.builder.SAXOMBuilder builder = new org.apache.axiom.om.impl.builder.SAXOMBuilder();
                javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                marshaller.marshal(new javax.xml.bind.JAXBElement(
                        new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), builder);

                return builder.getRootElement().getXMLStreamReader();
            } catch (javax.xml.bind.JAXBException e) {
                throw new javax.xml.stream.XMLStreamException(XCAConstants.EXCEPTIONS.ERROR_JAXB_MARSHALLING, e);
            }
        }
    }

    private EventLog createAndSendEventLogQuery(AdhocQueryRequest request, AdhocQueryResponse response,
            org.apache.axis2.context.MessageContext msgContext,
            org.apache.axiom.soap.SOAPEnvelope _returnEnv,
            org.apache.axiom.soap.SOAPEnvelope env,
            Assertion idAssertion, Assertion trcAssertion,
            String address, String classCode) {
        EventLog eventLog = EventLogClientUtil.prepareEventLog(msgContext, _returnEnv, address);
        EventLogClientUtil.logIdAssertion(eventLog, idAssertion);
        EventLogClientUtil.logTrcAssertion(eventLog, trcAssertion);
        EventLogUtil.prepareXCACommonLogQuery(eventLog, request, response, classCode);
        EventLogClientUtil.sendEventLog(eventLog);
        return eventLog;
    }

    private EventLog createAndSendEventLogRetrieve(RetrieveDocumentSetRequestType request, RetrieveDocumentSetResponseType response,
            org.apache.axis2.context.MessageContext msgContext,
            org.apache.axiom.soap.SOAPEnvelope _returnEnv,
            org.apache.axiom.soap.SOAPEnvelope env,
            Assertion idAssertion, Assertion trcAssertion,
            String address, String classCode) {
        EventLog eventLog = EventLogClientUtil.prepareEventLog(msgContext, _returnEnv, address);
        EventLogClientUtil.logIdAssertion(eventLog, idAssertion);
        EventLogClientUtil.logTrcAssertion(eventLog, trcAssertion);
        EventLogUtil.prepareXCACommonLogRetrieve(eventLog, request, response, classCode);
        EventLogClientUtil.sendEventLog(eventLog);
        return eventLog;
    }
}
