/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.pt.cc;

import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.openncp.protocolterminator.clientconnector.QueryDocumentsDocument;
import epsos.openncp.protocolterminator.clientconnector.QueryDocumentsResponseDocument;
import epsos.openncp.protocolterminator.clientconnector.QueryPatientDocument;
import epsos.openncp.protocolterminator.clientconnector.QueryPatientResponseDocument;
import epsos.openncp.protocolterminator.clientconnector.RetrieveDocumentDocument1;
import epsos.openncp.protocolterminator.clientconnector.RetrieveDocumentResponseDocument;
import epsos.openncp.protocolterminator.clientconnector.SayHelloDocument;
import epsos.openncp.protocolterminator.clientconnector.SayHelloResponseDocument;
import epsos.openncp.protocolterminator.clientconnector.SubmitDocumentDocument1;
import epsos.openncp.protocolterminator.clientconnector.SubmitDocumentResponseDocument;
import eu.epsos.util.EvidenceUtils;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.TransformerException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.receivers.AbstractInOutMessageReceiver;
import org.apache.axis2.util.JavaUtils;
import org.apache.axis2.util.XMLUtils;
import org.apache.axis2.xmlbeans.XmlBeansXMLReader;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hibernate.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.com.srdc.epsos.securityman.SAML2Validator;
import tr.com.srdc.epsos.util.DateUtil;
import tr.com.srdc.epsos.util.XMLUtil;

/**
 * ClientConnectorServiceServiceMessageReceiverInOut message receiver.
 *
 */
public class ClientConnectorServiceMessageReceiverInOut extends AbstractInOutMessageReceiver {

    protected static final Logger LOG = LoggerFactory.getLogger(ClientConnectorServiceMessageReceiverInOut.class);
    
    static {
        LOG.debug("Loading the WS-Security init libraries in ClientConnectorServiceMessageReceiverInOut 2009");
        org.apache.xml.security.Init.init(); // Joao added 10/03/2017. 
    }

    @Override
    public void invokeBusinessLogic(MessageContext msgContext, MessageContext newMsgContext) throws AxisFault {
        SOAPEnvelope reqEnv = msgContext.getEnvelope();
        String operationName = msgContext.getOperationContext().getOperationName();

        /*
         * Log soap request
         */
        try {
            String logRequestMsg = XMLUtil.prettyPrint(XMLUtils.toDOM(reqEnv));
            LOG.debug("Incoming " + operationName + " request message from portal:"
                    + System.getProperty("line.separator") + logRequestMsg);

        } catch (TransformerException ex) {
            LOG.debug(ex.getLocalizedMessage(), ex);
        } catch (Exception ex) {
            LOG.debug(ex.getLocalizedMessage(), ex);
        }

        /*
         * Body
         */
        try {

            /* get the implementation class for the Web Service */
            ClientConnectorServiceSkeletonInterface skel;
            Object obj = getTheImplementationObject(msgContext);
            skel = (ClientConnectorServiceSkeletonInterface) obj;

            /* Out Envelop */
            SOAPEnvelope envelope;

            /* Find the axisOperation that has been set by the Dispatch phase. */
            org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
            if (op == null) {
                throw new AxisFault("Operation is not located,"
                        + " if this is doclit style the SOAP-ACTION "
                        + "should specified via the SOAP Action to use the RawXMLProvider");
            }

            java.lang.String methodName;
            if ((op.getName() != null)
                    && ((methodName = JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null)) {

                /*
                 * Assertions
                 */
                List<Assertion> assertions;
                Element soapHeader = XMLUtils.toDOM(reqEnv.getHeader());
                assertions = SAML2Validator.getAssertions(soapHeader);
                Assertion mainHcpAssertion = null;
                for (Assertion ass : assertions) {
                    if (ass.getAdvice() == null) {
                        mainHcpAssertion = ass;
                    }
                }

                /*
                 * Call to service
                 */
                if ("submitDocument".equals(methodName)) {
//                    SAML2Validator.validateXDRHeader(soapHeader, Constants.CONSENT_CLASSCODE);
                    Assertion hcpAssertion = null;
                    Assertion trcAssertion = null;
                    for (Assertion ass : assertions) {
                        if (ass.getAdvice() == null) {
                            hcpAssertion = ass;
                        } else {
                            trcAssertion = ass;
                        }
                    }

//                    try {
//                        EvidenceUtils.createEvidenceREMNRR(XMLUtil.prettyPrint(XMLUtils.toDOM(reqEnv)),
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
//                                "PORTAL_XDR_REQ",
//                                new DateTime(),
//                                EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
//                                "PORTAL_XDR_REQ_RECEIVED");
//
//                    } catch (Exception e) {
//                        LOG.error(ExceptionUtils.getStackTrace(e));
//                    }

                    SubmitDocumentResponseDocument submitDocumentResponse11;
                    SubmitDocumentDocument1 wrappedParam;
                    wrappedParam = (SubmitDocumentDocument1) fromOM(reqEnv.getBody().getFirstElement(),
                            SubmitDocumentDocument1.class,
                            getEnvelopeNamespaces(reqEnv));
                    submitDocumentResponse11 = skel.submitDocument(wrappedParam, hcpAssertion, trcAssertion);

                    envelope = toEnvelope(getSOAPFactory(msgContext), submitDocumentResponse11);

//                    try {
//                        EvidenceUtils.createEvidenceREMNRO(XMLUtil.prettyPrint(XMLUtils.toDOM(envelope)),
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
//                                "NCPB_XDR_RES",
//                                new DateTime(),
//                                EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
//                                "NCPB_XDR_RES_SENT");
//
//                    } catch (Exception e) {
//                        LOG.error(ExceptionUtils.getStackTrace(e));
//                    }

                    /* Query Patient */
                } else if ("queryPatient".equals(methodName)) {
//                    SAML2Validator.validateXCPDHeader(soapHeader);
                    Assertion hcpAssertion = null;
                    for (Assertion ass : assertions) {
                        hcpAssertion = ass;
                        if (hcpAssertion.getAdvice() == null) {
                            break;
                        }
                    }

//                    try {
//                        EvidenceUtils.createEvidenceREMNRR(XMLUtil.prettyPrint(XMLUtils.toDOM(reqEnv)),
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
//                                "PORTAL_PD_REQ",
//                                new DateTime(),
//                                EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
//                                "PORTAL_PD_REQ_RECEIVED",
//                                hcpAssertion.getID() + "__" + DateUtil.getCurrentTimeGMT());
//
//                    } catch (Exception e) {
//                        LOG.error(ExceptionUtils.getStackTrace(e));
//                    }

                    QueryPatientDocument wrappedParam;
                    wrappedParam = (QueryPatientDocument) fromOM(reqEnv.getBody().getFirstElement(),
                            QueryPatientDocument.class,
                            getEnvelopeNamespaces(reqEnv));

                    QueryPatientResponseDocument queryPatientResponse13 = skel.queryPatient(wrappedParam, hcpAssertion);
                    envelope = toEnvelope(getSOAPFactory(msgContext), queryPatientResponse13);

//                    try {
//                        EvidenceUtils.createEvidenceREMNRO(XMLUtil.prettyPrint(XMLUtils.toDOM(envelope)),
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
//                                "NCPB_PD_RES",
//                                new DateTime(),
//                                EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
//                                "NCPB_PD_RES_SENT",
//                                hcpAssertion.getID() + "__" + DateUtil.getCurrentTimeGMT());
//                    } catch (Exception e) {
//                        LOG.error(ExceptionUtils.getStackTrace(e));
//                    }

                    /* Say hello */
                } else if ("sayHello".equals(methodName)) {
                    SayHelloResponseDocument sayHelloResponse15;
                    SayHelloDocument wrappedParam;
                    wrappedParam = (SayHelloDocument) fromOM(reqEnv.getBody().getFirstElement(),
                            SayHelloDocument.class,
                            getEnvelopeNamespaces(reqEnv));
                    sayHelloResponse15 = skel.sayHello(wrappedParam);

                    envelope = toEnvelope(getSOAPFactory(msgContext), sayHelloResponse15);

                    /* Query Documents */
                } else if ("queryDocuments".equals(methodName)) {
//                    SAML2Validator.validateXCAHeader(soapHeader, Constants.PS_CLASSCODE);
                    Assertion hcpAssertion = null;
                    Assertion trcAssertion = null;
                    for (Assertion ass : assertions) {
                        if (ass.getAdvice() == null) {
                            hcpAssertion = ass;
                        } else {
                            trcAssertion = ass;
                        }
                    }

//                    try {
//                        EvidenceUtils.createEvidenceREMNRR(XMLUtil.prettyPrint(XMLUtils.toDOM(reqEnv)),
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
//                                "PORTAL_DQ_REQ",
//                                new DateTime(),
//                                EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
//                                "PORTAL_DQ_REQ_RECEIVED",
//                                hcpAssertion.getID() + "__" + DateUtil.getCurrentTimeGMT());
//
//                    } catch (Exception e) {
//                        LOG.error(ExceptionUtils.getStackTrace(e));
//                    }

                    QueryDocumentsResponseDocument queryDocumentsResponse17;

                    QueryDocumentsDocument wrappedParam;
                    wrappedParam = (QueryDocumentsDocument) fromOM(reqEnv.getBody().getFirstElement(),
                            QueryDocumentsDocument.class,
                            getEnvelopeNamespaces(reqEnv));
                    queryDocumentsResponse17 = skel.queryDocuments(wrappedParam, hcpAssertion, trcAssertion);

                    envelope = toEnvelope(getSOAPFactory(msgContext), queryDocumentsResponse17);

//                    try {
//                        EvidenceUtils.createEvidenceREMNRO(XMLUtil.prettyPrint(XMLUtils.toDOM(envelope)),
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
//                                "NCPB_DQ_RES",
//                                new DateTime(),
//                                EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
//                                "NCPB_DQ_RES_SENT",
//                                hcpAssertion.getID() + "__" + DateUtil.getCurrentTimeGMT());
//
//                    } catch (Exception e) {
//                        LOG.error(ExceptionUtils.getStackTrace(e));
//                    }

                    /*
                     * Retrieve Document
                     */
                } else if ("retrieveDocument".equals(methodName)) {
//                    SAML2Validator.validateXCAHeader(soapHeader, Constants.PS_CLASSCODE);
                    Assertion hcpAssertion = null;
                    Assertion trcAssertion = null;
                    for (Assertion ass : assertions) {
                        if (ass.getAdvice() == null) {
                            hcpAssertion = ass;
                        } else {
                            trcAssertion = ass;
                        }
                    }

//                    try {
//                        EvidenceUtils.createEvidenceREMNRR(XMLUtil.prettyPrint(XMLUtils.toDOM(reqEnv)),
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
//                                "PORTAL_DR_REQ",
//                                new DateTime(),
//                                EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
//                                "PORTAL_DR_REQ_RECEIVED",
//                                hcpAssertion.getID() + "__" + DateUtil.getCurrentTimeGMT());
//                    } catch (Exception e) {
//                        LOG.error(ExceptionUtils.getStackTrace(e));
//                    }

                    RetrieveDocumentResponseDocument retrieveDocumentResponse19;
                    RetrieveDocumentDocument1 wrappedParam;
                    wrappedParam = (RetrieveDocumentDocument1) fromOM(reqEnv.getBody().getFirstElement(),
                            RetrieveDocumentDocument1.class,
                            getEnvelopeNamespaces(reqEnv));
                    retrieveDocumentResponse19 = skel.retrieveDocument(wrappedParam, hcpAssertion, trcAssertion);

                    envelope = toEnvelope(getSOAPFactory(msgContext), retrieveDocumentResponse19);

//                    try {
//                        EvidenceUtils.createEvidenceREMNRO(XMLUtil.prettyPrint(XMLUtils.toDOM(envelope)),
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
//                                "NCPB_DR_RES",
//                                new DateTime(),
//                                EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
//                                "NCPB_DR_RES_SENT",
//                                hcpAssertion.getID() + "__" + DateUtil.getCurrentTimeGMT());
//
//                    } catch (Exception e) {
//                        LOG.error(ExceptionUtils.getStackTrace(e));
//                    }
                    /*
                     * Else
                     */
                } else {
                    throw new java.lang.RuntimeException("method not found");
                }

                /*
                 * Log soap request
                 */
                try {
                    String logRequestMsg = XMLUtil.prettyPrint(XMLUtils.toDOM(envelope));
                    LOG.debug("Outgoing " + operationName + " response message to portal:"
                            + System.getProperty("line.separator") + logRequestMsg);

                } catch (Exception ex) {
                    LOG.debug(ex.getLocalizedMessage(), ex);
//                    try {
//                        EvidenceUtils.createEvidenceREMNRO(XMLUtil.prettyPrint(XMLUtils.toDOM(envelope)),
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PATH,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_KEYSTORE_PASSWORD,
//                                tr.com.srdc.epsos.util.Constants.NCP_SIG_PRIVATEKEY_ALIAS,
//                                "PORTAL_RES",
//                                new DateTime(),
//                                EventOutcomeIndicator.TEMPORAL_FAILURE.getCode().toString(),
//                                "PORTAL_RES_SENT",
//                                mainHcpAssertion.getID() + "__" + DateUtil.getCurrentTimeGMT());
//
//                    } catch (Exception e) {
//                        LOG.error(ExceptionUtils.getStackTrace(e));
//                    }
                }
                newMsgContext.setEnvelope(envelope);
            }

        } catch (java.lang.Exception e) {

            LOG.error(e.getLocalizedMessage(), e);
            throw AxisFault.makeFault(e);
        }
    }

    /*
     * ELEMENT
     */
    private OMElement toOM(final SubmitDocumentResponseDocument param) throws AxisFault {

        XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSaveNoXmlDecl();
        xmlOptions.setSaveAggressiveNamespaces();
        xmlOptions.setSaveNamespacesFirst();
        OMXMLParserWrapper builder = OMXMLBuilderFactory.createOMBuilder(
                new javax.xml.transform.sax.SAXSource(new XmlBeansXMLReader(param, xmlOptions), new org.xml.sax.InputSource()));
        try {
            return builder.getDocumentElement(true);
        } catch (java.lang.Exception e) {
            throw AxisFault.makeFault(e);
        }
    }

    private OMElement toOM(final QueryPatientResponseDocument param) throws AxisFault {
        XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSaveNoXmlDecl();
        xmlOptions.setSaveAggressiveNamespaces();
        xmlOptions.setSaveNamespacesFirst();
        OMXMLParserWrapper builder = OMXMLBuilderFactory.createOMBuilder(
                new javax.xml.transform.sax.SAXSource(new XmlBeansXMLReader(param, xmlOptions), new org.xml.sax.InputSource()));
        try {
            return builder.getDocumentElement(true);
        } catch (java.lang.Exception e) {
            throw AxisFault.makeFault(e);
        }
    }

    private OMElement toOM(final SayHelloResponseDocument param) throws AxisFault {
        XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSaveNoXmlDecl();
        xmlOptions.setSaveAggressiveNamespaces();
        xmlOptions.setSaveNamespacesFirst();
        OMXMLParserWrapper builder = OMXMLBuilderFactory.createOMBuilder(
                new javax.xml.transform.sax.SAXSource(new XmlBeansXMLReader(param, xmlOptions), new org.xml.sax.InputSource()));
        try {
            return builder.getDocumentElement(true);
        } catch (java.lang.Exception e) {
            throw AxisFault.makeFault(e);
        }
    }

    private OMElement toOM(final QueryDocumentsResponseDocument param) throws AxisFault {
        XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSaveNoXmlDecl();
        xmlOptions.setSaveAggressiveNamespaces();
        xmlOptions.setSaveNamespacesFirst();
        OMXMLParserWrapper builder = OMXMLBuilderFactory.createOMBuilder(
                new javax.xml.transform.sax.SAXSource(new XmlBeansXMLReader(param, xmlOptions), new org.xml.sax.InputSource()));
        try {
            return builder.getDocumentElement(true);
        } catch (java.lang.Exception e) {
            throw AxisFault.makeFault(e);
        }
    }

    private OMElement toOM(final RetrieveDocumentResponseDocument param) throws AxisFault {
        XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSaveNoXmlDecl();
        xmlOptions.setSaveAggressiveNamespaces();
        xmlOptions.setSaveNamespacesFirst();
        OMXMLParserWrapper builder = OMXMLBuilderFactory.createOMBuilder(
                new javax.xml.transform.sax.SAXSource(new XmlBeansXMLReader(param, xmlOptions), new org.xml.sax.InputSource()));
        try {
            return builder.getDocumentElement(true);
        } catch (java.lang.Exception e) {
            throw AxisFault.makeFault(e);
        }
    }

    /*
     * ENVELOP
     */
    private SOAPEnvelope toEnvelope(SOAPFactory factory, SubmitDocumentResponseDocument param) throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        if (param != null) {
            envelope.getBody().addChild(toOM(param));
        }
        return envelope;
    }

    private SOAPEnvelope toEnvelope(SOAPFactory factory, QueryPatientResponseDocument param) throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        if (param != null) {
            envelope.getBody().addChild(toOM(param));
        }
        return envelope;
    }

    private SOAPEnvelope toEnvelope(SOAPFactory factory, SayHelloResponseDocument param) throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        if (param != null) {
            envelope.getBody().addChild(toOM(param));
        }
        return envelope;
    }

    private SOAPEnvelope toEnvelope(SOAPFactory factory, QueryDocumentsResponseDocument param) throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        if (param != null) {
            envelope.getBody().addChild(toOM(param));
        }
        return envelope;
    }

    private SOAPEnvelope toEnvelope(SOAPFactory factory, RetrieveDocumentResponseDocument param) throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        if (param != null) {
            envelope.getBody().addChild(toOM(param));
        }
        return envelope;
    }

    /*
     * OTHER
     */
    public XmlObject fromOM(OMElement param, Class type, Map extraNamespaces) throws AxisFault {
        try {

            if (SubmitDocumentDocument1.class.equals(type)) {

                if (extraNamespaces != null) {
                    return SubmitDocumentDocument1.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching(),
                            new XmlOptions().setLoadAdditionalNamespaces(extraNamespaces));

                } else {
                    return SubmitDocumentDocument1.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching());
                }
            }

            if (SubmitDocumentResponseDocument.class.equals(type)) {

                if (extraNamespaces != null) {
                    return SubmitDocumentResponseDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching(),
                            new XmlOptions().setLoadAdditionalNamespaces(extraNamespaces));

                } else {
                    return SubmitDocumentResponseDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching());
                }
            }

            if (QueryPatientDocument.class.equals(type)) {

                if (extraNamespaces != null) {
                    return QueryPatientDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching(),
                            new XmlOptions().setLoadAdditionalNamespaces(extraNamespaces));

                } else {
                    return QueryPatientDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching());
                }
            }

            if (QueryPatientResponseDocument.class.equals(type)) {
                if (extraNamespaces != null) {
                    return QueryPatientResponseDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching(),
                            new XmlOptions().setLoadAdditionalNamespaces(extraNamespaces));

                } else {
                    return QueryPatientResponseDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching());
                }
            }

            if (SayHelloDocument.class.equals(type)) {
                if (extraNamespaces != null) {
                    XMLStreamReader xmlStreamReaderWithoutCaching = param.getXMLStreamReaderWithoutCaching();
                    XmlOptions setLoadAdditionalNamespaces = new XmlOptions().setLoadAdditionalNamespaces(extraNamespaces);
                    SayHelloDocument parse = SayHelloDocument.Factory.parse(xmlStreamReaderWithoutCaching, setLoadAdditionalNamespaces);

                    return parse;

                } else {
                    return SayHelloDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching());
                }
            }

            if (SayHelloResponseDocument.class.equals(type)) {
                if (extraNamespaces != null) {
                    return SayHelloResponseDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching(),
                            new org.apache.xmlbeans.XmlOptions().setLoadAdditionalNamespaces(extraNamespaces));

                } else {
                    return SayHelloResponseDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching());
                }
            }

            if (QueryDocumentsDocument.class.equals(type)) {
                if (extraNamespaces != null) {
                    return QueryDocumentsDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching(),
                            new XmlOptions().setLoadAdditionalNamespaces(extraNamespaces));

                } else {
                    return QueryDocumentsDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching());
                }
            }

            if (QueryDocumentsResponseDocument.class.equals(type)) {
                if (extraNamespaces != null) {
                    return QueryDocumentsResponseDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching(),
                            new XmlOptions().setLoadAdditionalNamespaces(extraNamespaces));

                } else {
                    return QueryDocumentsResponseDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching());
                }
            }

            if (RetrieveDocumentDocument1.class.equals(type)) {
                if (extraNamespaces != null) {
                    return RetrieveDocumentDocument1.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching(),
                            new XmlOptions().setLoadAdditionalNamespaces(extraNamespaces));

                } else {
                    return RetrieveDocumentDocument1.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching());
                }
            }

            if (RetrieveDocumentResponseDocument.class.equals(type)) {
                if (extraNamespaces != null) {
                    return RetrieveDocumentResponseDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching(),
                            new XmlOptions().setLoadAdditionalNamespaces(extraNamespaces));

                } else {
                    return RetrieveDocumentResponseDocument.Factory.parse(
                            param.getXMLStreamReaderWithoutCaching());
                }
            }

        } catch (Exception e) {
            throw AxisFault.makeFault(e);
        }
        return null;
    }

    /**
     * A utility method that copies the namespaces from the SOAPEnvelope.
     */
    private Map getEnvelopeNamespaces(SOAPEnvelope env) {
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
            OMNamespace ns = (OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
        }
        return returnMap;
    }
}
