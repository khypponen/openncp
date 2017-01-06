/***Licensed to the Apache Software Foundation (ASF) under one
 *or more contributor license agreements.  See the NOTICE file
 *distributed with this work for additional information
 *regarding copyright ownership.  The ASF licenses this file
 *to you under the Apache License, Version 2.0 (the
 *"License"); you may not use this file except in compliance
 *with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing,
 *software distributed under the License is distributed on an
 *"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *KIND, either express or implied.  See the License for the
 *specific language governing permissions and limitations
 *under the License.
 **/
package epsos.ccd.netsmart.securitymanager.sts;

import com.sun.xml.ws.api.security.trust.WSTrustException;
import epsos.ccd.gnomon.auditmanager.*;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.ccd.netsmart.securitymanager.SamlTRCIssuer;
import epsos.ccd.netsmart.securitymanager.SignatureManager;
import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.*;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.handler.MessageContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.InetAddress;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * @author Jerry Dimitriou <jerouris at netsmart.gr>
 */
@ServiceMode(value = Mode.MESSAGE)
@WebServiceProvider(targetNamespace = "http://epsos.eu/", serviceName = "SecurityTokenService", portName = "ISecurityTokenService_Port")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class STSService implements Provider<SOAPMessage> {

    private static final Logger logger = LoggerFactory.getLogger(STSService.class);

    private static final QName Messaging_To = new QName("http://www.w3.org/2005/08/addressing", "To");
    private static final String SAML20_TOKEN_URN = "urn:oasis:names:tc:SAML:2.0:assertion"; // What
    // can
    // be
    // only
    // requested
    // from
    // the
    // STS
    private static final String SUPPORTED_ACTION_URI = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue"; // Only
    // Issuance
    // is
    // supported
    private static final String TRC_NS = "http://epsos.eu/trc"; // TRC
    // Parameters
    // Namespace
    private static final String WS_TRUST_NS = "http://docs.oasis-open.org/ws-sx/ws-trust/200512"; // WS-Trust
    // Namespace
    private static final String ADDRESSING_NS = "http://www.w3.org/2005/08/addressing"; // WSA
    // Namespace
    private static final String WS_SEC_UTIL_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
    private static final String WS_SEC_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    @Resource
    WebServiceContext context;

    @Override
    public SOAPMessage invoke(SOAPMessage source) {

        log(source);

        SOAPBody body = null;
        SOAPHeader header = null;
        try {
            body = source.getSOAPBody();
            header = source.getSOAPHeader();
        } catch (SOAPException ex) {
            throw new WebServiceException("Cannot get Soap Message Parts", ex);
        }

        try {
            DefaultBootstrap.bootstrap();
        } catch (ConfigurationException ex) {
            logger.error(null, ex);
        }
        try {
            if (!SUPPORTED_ACTION_URI.equals(getRSTAction(body))) {
                throw new WebServiceException("Only ISSUE action is supported");
            }
        } catch (WSTrustException ex) {
            throw new WebServiceException(ex);
        }
        try {
            if (!SAML20_TOKEN_URN.equals(getRequestedToken(body))) {
                throw new WebServiceException("Only SAML2.0 Tokens are Issued");
            }
        } catch (WSTrustException ex) {
            throw new WebServiceException(ex);
        }

        try {
            // these calls are both getters and checkers of message.
            // So we call them first
            String purposeOfUse = getPurposeOfUse(body);
            String patientID = getPatientID(body);
            String mid = getMessageIdFromHeader(header);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();

            // The response TRC Assertion Issuer.
            SamlTRCIssuer samlTRCIssuer = new SamlTRCIssuer();
            Assertion hcpIdAssertion = getIdAssertionFromHeader(header);
            Assertion trc = samlTRCIssuer.issueTrcToken(hcpIdAssertion, patientID, purposeOfUse, null);
            Document signedDoc = builder.newDocument();
            Configuration.getMarshallerFactory().getMarshaller(trc).marshall(trc, signedDoc);

            SOAPMessage resp = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            resp.getSOAPBody().addDocument(createRSTRC(signedDoc));
            createResponseHeader(resp.getSOAPHeader(), mid);

            String strRespHeader = domElementToString(resp.getSOAPHeader());
            String strReqHeader = domElementToString(header);

            String tls_cn = getSSLCertPeer();
            logger.info("tls_cn: " + tls_cn);

            if (context.getUserPrincipal() != null) {
                tls_cn = context.getUserPrincipal().getName();
            }

            audit(samlTRCIssuer.getPointofCare(), samlTRCIssuer.getHumanRequestorNameId(),
                    samlTRCIssuer.getHumanRequestorSubjectId(), samlTRCIssuer.getHRRole(), patientID,
                    samlTRCIssuer.getFacilityType(), trc.getID(), tls_cn, mid,
                    Base64.encodeBase64(strReqHeader.getBytes()), getMessageIdFromHeader(resp.getSOAPHeader()),
                    Base64.encodeBase64(strRespHeader.getBytes()));

            // Logger.getLogger(STSService.class.getName()).log(Level.INFO,
            // "header1:{0}", new String(strReqHeader.getBytes()));
            // Logger.getLogger(STSService.class.getName()).log(Level.INFO,
            // "header2:{0}", new String(strRespHeader.getBytes()));
            log(resp);
            return resp;

        } catch (SOAPException ex) {
            logger.error(null, ex);
            throw new WebServiceException(ex);
        } catch (WSTrustException ex) {
            logger.error(null, ex);
            throw new WebServiceException(ex);
        } catch (MarshallingException ex) {
            logger.error(null, ex);
            throw new WebServiceException(ex);
        } catch (ParserConfigurationException ex) {
            logger.error(null, ex);
            throw new WebServiceException(ex);
        } catch (SMgrException ex) {
            logger.error(null, ex);
            throw new WebServiceException(ex);
        } catch (IOException ex) {
            logger.error(null, ex);
            throw new WebServiceException(ex);
        }
    }

    private String getPurposeOfUse(SOAPElement body) {

        if (body.getElementsByTagNameNS(TRC_NS, "TRCParameters").getLength() < 1) {
            throw new WebServiceException("No TRC Parameters in RST");
        }

        SOAPElement trcDetails = (SOAPElement) body.getElementsByTagNameNS(TRC_NS, "TRCParameters").item(0);
        if (trcDetails.getElementsByTagNameNS(TRC_NS, "PurposeOfUse").item(0) == null) {
            return null;
        }

        String purposeOfUse = trcDetails.getElementsByTagNameNS(TRC_NS, "PurposeOfUse").item(0).getTextContent();
        if (purposeOfUse != null && (!"TREATMENT".equals(purposeOfUse) && !"EMERGENCY".equals(purposeOfUse))) {
            throw new WebServiceException("Purpose of Use MUST be either TREATMENT of EMERGENCY");
        }
        return purposeOfUse;
    }

    private String getPatientID(SOAPElement body) {
        if (body.getElementsByTagNameNS(TRC_NS, "TRCParameters").getLength() < 1) {
            throw new WebServiceException("No TRC Parameters in RST");
        }

        SOAPElement trcDetails = (SOAPElement) body.getElementsByTagNameNS(TRC_NS, "TRCParameters").item(0);
        if (trcDetails.getElementsByTagNameNS(TRC_NS, "PatientId").item(0) == null) {
            throw new WebServiceException("Patient ID is Missing from the RST"); // Cannot
            // be
            // null!
        }
        String patientId = trcDetails.getElementsByTagNameNS(TRC_NS, "PatientId").item(0).getTextContent();
        return patientId;
    }

    private Assertion getIdAssertionFromHeader(SOAPHeader header) throws WSTrustException {
        try {
            // First, find the assertion from the header.
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            if (header.getElementsByTagNameNS(SAML20_TOKEN_URN, "Assertion").getLength() != 1) {
                throw new WSTrustException("SAML Identity Assertion is missing from the Security Header");
            }
            SOAPElement assertion = (SOAPElement) header.getElementsByTagNameNS(SAML20_TOKEN_URN, "Assertion").item(0);
            Document assertDoc = builder.newDocument();

            Node dupBody = assertDoc.importNode(assertion, true);
            assertDoc.appendChild(dupBody);
            if (assertion == null) {
                return null;
            } else {
                assertDoc.getDocumentElement().setIdAttribute("ID", true);
                SignatureManager sm = new SignatureManager();
                try {
                    sm.verifyEnvelopedSignature(assertDoc);
                } catch (SMgrException ex) {
                    logger.error(null, ex);
                    throw new WSTrustException("Error validating SAML Assertion signature", ex);
                }
            }
            UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
            Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(assertion);
            Assertion hcpIdentityAssertion = (Assertion) unmarshaller.unmarshall(assertDoc.getDocumentElement());
            return hcpIdentityAssertion;

        } catch (ParserConfigurationException ex) {
            logger.error(null, ex);
            throw new WSTrustException("Error Parsing SAML Assertion in Message Header", ex);
        } catch (UnmarshallingException ex) {
            logger.error(null, ex);
            throw new WSTrustException("Error Parsing SAML Assertion in Message Header", ex);
        } catch (IOException ex) {
            logger.error(null, ex);
            throw new WSTrustException("Error Parsing SAML Assertion in Message Header", ex);
        }
    }

    private String getMessageIdFromHeader(SOAPHeader header) {
        if (header.getElementsByTagNameNS(ADDRESSING_NS, "MessageID").getLength() < 1) {
            throw new WebServiceException("Message ID not found in Header");
        }
        String mid = header.getElementsByTagNameNS(ADDRESSING_NS, "MessageID").item(0).getTextContent();
        // PT-236 has to be checked again why is coming like this from soap
        // header
        if (mid.startsWith("uuid"))
            mid = "urn:" + mid;
        return mid;
    }

    private void createResponseHeader(SOAPHeader header, String messageId) {
        try {

            DateTime now = new DateTime();

            SOAPFactory fac = SOAPFactory.newInstance();
            SOAPElement messageIdElem = header.addHeaderElement(new QName(ADDRESSING_NS, "MessageID", "wsa"));
            messageIdElem.setTextContent("uuid:" + UUID.randomUUID().toString());
            SOAPElement securityHeaderElem = header.addHeaderElement(new QName(WS_SEC_NS, "Security", "wsse"));

            SOAPElement timeStampElem = fac.createElement("Timestamp", "wsu", WS_SEC_UTIL_NS);
            SOAPElement ltCreated = fac.createElement("Created", "wsu", WS_SEC_UTIL_NS);
            ltCreated.setTextContent(now.toDateTime(DateTimeZone.UTC).toString());

            SOAPElement ltExpires = fac.createElement("Expires", "wsu", WS_SEC_UTIL_NS);
            ltExpires.setTextContent(now.plusHours(2).toDateTime(DateTimeZone.UTC).toString());

            timeStampElem.addChildElement(ltCreated);
            timeStampElem.addChildElement(ltExpires);

            securityHeaderElem.addChildElement(timeStampElem);

            SOAPElement actionElem = header.addHeaderElement(new QName(ADDRESSING_NS, "Action", "wsa"));
            actionElem.setTextContent("urn:IssueTokenResponse");

            SOAPElement relatesToElem = header.addHeaderElement(new QName(ADDRESSING_NS, "RelatesTo", "wsa"));
            relatesToElem.setTextContent(messageId);

        } catch (SOAPException ex) {
            logger.error(null, ex);
            throw new WebServiceException("Could not create Response Header");
        }
    }

    private Document createRSTRC(Document assertion) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document respBody = builder.newDocument();

            Element rstrcElem = respBody.createElementNS(WS_TRUST_NS, "wst:RequestSecurityTokenResponseCollection");
            respBody.appendChild(rstrcElem);

            Element rstrElem = respBody.createElementNS(WS_TRUST_NS, "wst:RequestSecurityTokenResponse");
            rstrcElem.appendChild(rstrElem);

            Element rstElem = respBody.createElementNS(WS_TRUST_NS, "wst:RequestedSecurityToken");
            rstrElem.appendChild(rstElem);

            // add the Assertion
            rstElem.appendChild(respBody.importNode(assertion.getDocumentElement(), true));

            Element tokenTypeElem = respBody.createElementNS(WS_TRUST_NS, "wst:TokenType");
            tokenTypeElem.setTextContent(SAML20_TOKEN_URN);

            rstrElem.appendChild(tokenTypeElem);

            Element lifeTimeElem = respBody.createElementNS(WS_TRUST_NS, "wst:LifeTime");
            rstrElem.appendChild(lifeTimeElem);

            DateTime now = new DateTime();

            Element ltCreated = respBody.createElementNS(WS_SEC_UTIL_NS, "wsu:Created");
            ltCreated.setTextContent(now.toDateTime(DateTimeZone.UTC).toString());

            lifeTimeElem.appendChild(ltCreated);

            Element ltExpires = respBody.createElementNS(WS_SEC_UTIL_NS, "wsu:Expires");
            ltExpires.setTextContent(now.plusHours(2).toDateTime(DateTimeZone.UTC).toString());

            lifeTimeElem.appendChild(ltExpires);

            return respBody;

        } catch (Exception ex) {
            logger.error(null, ex);
            throw new WebServiceException("Cannot create RSTSC Message");
        }
    }

    private String getRSTAction(SOAPBody body) throws WSTrustException {

        if (body.getElementsByTagNameNS(WS_TRUST_NS, "RequestType").getLength() < 1) {
            throw new WSTrustException("No Request Type is Specified.");
        }

        return body.getElementsByTagNameNS(WS_TRUST_NS, "RequestType").item(0).getTextContent();
    }

    private String getRequestedToken(SOAPBody body) throws WSTrustException {

        if (body.getElementsByTagNameNS(WS_TRUST_NS, "TokenType").getLength() < 1) {
            throw new WSTrustException("No Token Type is Specified.");
        }

        return body.getElementsByTagNameNS(WS_TRUST_NS, "TokenType").item(0).getTextContent();

    }

    private String domElementToString(Element elem) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            StringWriter sw = new StringWriter();
            trans.transform(new DOMSource(elem), new StreamResult(sw));
            return sw.toString();
        } catch (TransformerException ex) {
            logger.error(null, ex);
            throw new WebServiceException("Error Creating audit message");
        }
    }

    private void audit(String pointOfCareID, String humanRequestorNameID, String humanRequestorSubjectID,
                       String humanRequestorRole, String patientID, String facilityType, String assertionId, String tls_cn,
                       String reqMid, byte[] reqSecHeader, String resMid, byte[] resSecHeader) {
        AuditService asd = new AuditService();
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar date2 = null;
        try {
            date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException ex) {

        }

        ConfigurationManagerService cms = ConfigurationManagerService.getInstance();

        EventLog evLogTRC = EventLog.createEventLogTRCA(TransactionName.epsosTRCAssertion, EventActionCode.EXECUTE,
                date2, EventOutcomeIndicator.FULL_SUCCESS, pointOfCareID, facilityType,
                cms.getProperty("ncp.country") + "<" + humanRequestorNameID + "@" + cms.getProperty("ncp.country")
                        + ">",
                humanRequestorRole, humanRequestorSubjectID, tls_cn, getServerIP(),
                cms.getProperty("COUNTRY_PRINCIPAL_SUBDIVISION"), patientID, "urn:uuid:" + assertionId, reqMid,
                reqSecHeader, resMid, resSecHeader, getServerIP(), getClientIP());

        evLogTRC.setEventType(EventType.epsosTRCAssertion);
        asd.write(evLogTRC, "13", "2");

    }

    private String getServerIP() {
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            return thisIp.getHostAddress();
        } catch (Exception e) {
            return "Unknown IP";
        }
    }

    private String getClientIP() {
        MessageContext mc = context.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        return req.getRemoteAddr();
    }

    private String getSSLCertPeer() {
        String user = "Unknown(No Client Certificate)";
        MessageContext msgCtx = context.getMessageContext();

        javax.servlet.ServletRequest sreq = (javax.servlet.ServletRequest) msgCtx.get(MessageContext.SERVLET_REQUEST);

        if (sreq instanceof HttpServletRequest && sreq.isSecure()) {
            logger.info("Secure and http");
            HttpServletRequest hreq = (HttpServletRequest) sreq;
            X509Certificate peerCert[] = (X509Certificate[]) hreq.getAttribute("javax.servlet.request.X509Certificate");
            if (peerCert != null) {
                return peerCert[0].getSubjectDN().getName();
            }
        }
        return user;
    }

    private String log(SOAPMessage message) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            message.writeTo(out);
        } catch (IOException | SOAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info(out.toString());
        System.out.println("***************************************************************************************");
        System.out.println(out.toString());
        System.out.println("***************************************************************************************");
        return out.toString();
    }
}
