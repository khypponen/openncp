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


import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.sun.xml.ws.api.security.trust.WSTrustException;
import epsos.ccd.netsmart.securitymanager.EvidenceEmitter;
import epsos.ccd.netsmart.securitymanager.NoXACMLEvidenceEmitter;
import epsos.ccd.netsmart.securitymanager.SamlTRCIssuer;
import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import epsos.ccd.netsmart.securitymanager.key.impl.DefaultKeyStoreManager;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.MarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.UUID;

/**
 * This class is the implementation of the unsigned TRC STS manager for the eID
 * level 2. This class is changed because we need to return an unsigned TRC STS
 * (to be signed by the patient). The layout is changed as well, to avoid to
 * pass back and forth the IdA (to avoid to have the IdA crossing several
 * security zones).
 *
 * @author Jerry Dimitriou <jerouris at netsmart.gr>, Massimiliano Masi
 *         <massimiliano.masi@tiani-spirit.com>
 */
@ServiceMode(value = Mode.MESSAGE)
@WebServiceProvider(targetNamespace = "http://epsos.eu/", serviceName = "SecurityTokenServiceEID", portName = "ISecurityTokenServiceEID_Port")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class STSServiceEID implements Provider<SOAPMessage> {

    private static final Logger logger = LoggerFactory.getLogger(STSServiceEID.class);

    private static final QName Messaging_To = new QName(
            "http://www.w3.org/2005/08/addressing", "To");
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
    private String messageIdResponse;

    @Override
    public SOAPMessage invoke(SOAPMessage source) {
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
            String doctorId = getDoctorID(body);
            DateTime notBefore = getNotBefore(body);
            DateTime notOnOrAfter = getNotOnOrAfter(body);
            DateTime authnInstant = getAuthNInstant(body);
            DateTime sessionNotOnOrAfter = getSessionNotOnOrAfter(body);
            String idaReference = getIdaReference(body);

            String mid = getMessageIdFromHeader(header);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();

            // The repsonse TRC Assertion Issuer.
            SamlTRCIssuer samlTRCIssuer = new SamlTRCIssuer();

            Assertion trc = samlTRCIssuer.issueTrcTokenUnsigned(purposeOfUse, patientID, doctorId, notBefore, notOnOrAfter, authnInstant, sessionNotOnOrAfter, idaReference);

            Document signedDoc = builder.newDocument();
            Configuration.getMarshallerFactory().getMarshaller(trc)
                    .marshall(trc, signedDoc);

            SOAPMessage resp = MessageFactory.newInstance(
                    SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            resp.getSOAPBody().addDocument(createRSTRC(signedDoc));
            createResponseHeader(resp.getSOAPHeader(), mid);

            String strRespHeader = domElementToString(resp.getSOAPHeader());
            String strReqHeader = domElementToString(header);


            String tls_cn = getSSLCertPeer();
            logger.info("tls_cn: " + tls_cn);

            if (context.getUserPrincipal() != null) {
                tls_cn = context.getUserPrincipal().getName();
            }

            // audit(samlTRCIssuer.getPointofCare(),
            // samlTRCIssuer.getHumanRequestorNameId(),
            // samlTRCIssuer.getHumanRequestorSubjectId(),
            // samlTRCIssuer.getHRRole(),
            // patientID,
            // samlTRCIssuer.getFacilityType(),
            // trc.getID(),
            // tls_cn,
            // mid,
            // Base64.encodeBase64(strReqHeader.getBytes()),
            // getMessageIdFromHeader(resp.getSOAPHeader()),
            // Base64.encodeBase64(strRespHeader.getBytes()));

            // Logger.getLogger(STSService.class.getName()).log(Level.INFO,
            // "header1:{0}", new String(strReqHeader.getBytes()));
            // Logger.getLogger(STSService.class.getName()).log(Level.INFO,
            // "header2:{0}", new String(strRespHeader.getBytes()));


            DefaultKeyStoreManager lamkm = new DefaultKeyStoreManager();

            X509Certificate clientCert = getSSLCert();
            if (clientCert == null) {
                clientCert = (X509Certificate) lamkm.getDefaultCertificate();
            }
            EvidenceEmitter ee = new NoXACMLEvidenceEmitter(lamkm);
            Element el = ee.emitNRR(UUID.randomUUID().toString(),
                    "urn:esens:policyId:trc",
                    c2s(lamkm.getDefaultCertificate()),
                    new DateTime().toString(),
                    "3",
                    c2s(lamkm.getDefaultCertificate()),
                    c2s(clientCert),
                    "TRC-Issuance",
                    messageIdResponse,
                    doDigest(resp)
            );

            // what should I do here? Let me just print it out, waiting for an official
            // notary service
            OutputFormat format = new OutputFormat();

            format.setLineWidth(65);
            format.setIndenting(false);
            format.setIndent(2);
            format.setEncoding("UTF-8");
            format.setOmitComments(true);
            format.setOmitXMLDeclaration(false);
            format.setVersion("1.0");
            format.setStandalone(true);

            XMLSerializer serializer = new XMLSerializer(System.out, format);
            serializer.serialize(el.getOwnerDocument());
            return resp;

        } catch (SOAPException ex) {
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
        } catch (CertificateEncodingException ex) {
            logger.error(null, ex);
            throw new WebServiceException(ex);
        } catch (SAXException ex) {
            logger.error(null, ex);
            throw new WebServiceException(ex);
        } catch (IOException ex) {
            logger.error(null, ex);
            throw new WebServiceException(ex);
        } catch (NoSuchAlgorithmException ex) {
            logger.error(null, ex);
            throw new WebServiceException(ex);
        } catch (Exception ex) {
            logger.error(null, ex);
            throw new WebServiceException(ex);
        }
    }

    private String c2s(Certificate certificate) throws CertificateEncodingException {
        byte[] cert = certificate.getEncoded();
        return Base64.getEncoder().encodeToString(cert);
    }

    private String getIdaReference(SOAPBody body) {
        if (body.getElementsByTagNameNS(TRC_NS, "TRCParameters").getLength() < 1) {
            throw new WebServiceException("No TRC Parameters in RST");
        }

        SOAPElement trcDetails = (SOAPElement) body.getElementsByTagNameNS(
                TRC_NS, "TRCParameters").item(0);
        if (trcDetails.getElementsByTagNameNS(TRC_NS, "IdAReference").item(0) == null) {
            return null;
        }

        String idAReference = trcDetails
                .getElementsByTagNameNS(TRC_NS, "IdAReference").item(0)
                .getTextContent();

        return idAReference;
    }

    private DateTime getSessionNotOnOrAfter(SOAPBody body) {
        if (body.getElementsByTagNameNS(TRC_NS, "TRCParameters").getLength() < 1) {
            throw new WebServiceException("No TRC Parameters in RST");
        }

        SOAPElement trcDetails = (SOAPElement) body.getElementsByTagNameNS(
                TRC_NS, "TRCParameters").item(0);
        if (trcDetails.getElementsByTagNameNS(TRC_NS, "SessionNotOnOrAfter").item(0) == null) {
            return null;
        }

        String sessionNotOnOrAfter = trcDetails
                .getElementsByTagNameNS(TRC_NS, "SessionNotOnOrAfter").item(0)
                .getTextContent();

        return new DateTime(sessionNotOnOrAfter);
    }

    private DateTime getAuthNInstant(SOAPBody body) {
        if (body.getElementsByTagNameNS(TRC_NS, "TRCParameters").getLength() < 1) {
            throw new WebServiceException("No TRC Parameters in RST");
        }

        SOAPElement trcDetails = (SOAPElement) body.getElementsByTagNameNS(
                TRC_NS, "TRCParameters").item(0);
        if (trcDetails.getElementsByTagNameNS(TRC_NS, "AuthnInstant").item(0) == null) {
            return null;
        }

        String authnInstant = trcDetails
                .getElementsByTagNameNS(TRC_NS, "AuthnInstant").item(0)
                .getTextContent();

        return new DateTime(authnInstant);
    }

    private DateTime getNotOnOrAfter(SOAPBody body) {
        if (body.getElementsByTagNameNS(TRC_NS, "TRCParameters").getLength() < 1) {
            throw new WebServiceException("No TRC Parameters in RST");
        }

        SOAPElement trcDetails = (SOAPElement) body.getElementsByTagNameNS(
                TRC_NS, "TRCParameters").item(0);
        if (trcDetails.getElementsByTagNameNS(TRC_NS, "NotOnOrAfter").item(0) == null) {
            return null;
        }

        String notOnOrAfter = trcDetails
                .getElementsByTagNameNS(TRC_NS, "NotOnOrAfter").item(0)
                .getTextContent();

        return new DateTime(notOnOrAfter);
    }

    private DateTime getNotBefore(SOAPBody body) {
        if (body.getElementsByTagNameNS(TRC_NS, "TRCParameters").getLength() < 1) {
            throw new WebServiceException("No TRC Parameters in RST");
        }

        SOAPElement trcDetails = (SOAPElement) body.getElementsByTagNameNS(
                TRC_NS, "TRCParameters").item(0);
        if (trcDetails.getElementsByTagNameNS(TRC_NS, "NotBefore").item(0) == null) {
            return null;
        }

        String notBefore = trcDetails
                .getElementsByTagNameNS(TRC_NS, "NotBefore").item(0)
                .getTextContent();

        return new DateTime(notBefore);
    }

    private String getDoctorID(SOAPBody body) {
        if (body.getElementsByTagNameNS(TRC_NS, "TRCParameters").getLength() < 1) {
            throw new WebServiceException("No TRC Parameters in RST");
        }

        SOAPElement trcDetails = (SOAPElement) body.getElementsByTagNameNS(
                TRC_NS, "TRCParameters").item(0);
        if (trcDetails.getElementsByTagNameNS(TRC_NS, "DoctorId").item(0) == null) {
            return null;
        }

        String doctorId = trcDetails
                .getElementsByTagNameNS(TRC_NS, "DoctorId").item(0)
                .getTextContent();

        return doctorId;
    }

    private String getPurposeOfUse(SOAPElement body) {

        if (body.getElementsByTagNameNS(TRC_NS, "TRCParameters").getLength() < 1) {
            throw new WebServiceException("No TRC Parameters in RST");
        }

        SOAPElement trcDetails = (SOAPElement) body.getElementsByTagNameNS(
                TRC_NS, "TRCParameters").item(0);
        if (trcDetails.getElementsByTagNameNS(TRC_NS, "PurposeOfUse").item(0) == null) {
            return null;
        }

        String purposeOfUse = trcDetails
                .getElementsByTagNameNS(TRC_NS, "PurposeOfUse").item(0)
                .getTextContent();
        if (purposeOfUse != null
                && (!"TREATMENT".equals(purposeOfUse) && !"EMERGENCY"
                .equals(purposeOfUse))) {
            throw new WebServiceException(
                    "Purpose of Use MUST be either TREATMENT of EMERGENCY");
        }
        return purposeOfUse;
    }

    private String getPatientID(SOAPElement body) {
        if (body.getElementsByTagNameNS(TRC_NS, "TRCParameters").getLength() < 1) {
            throw new WebServiceException("No TRC Parameters in RST");
        }

        SOAPElement trcDetails = (SOAPElement) body.getElementsByTagNameNS(
                TRC_NS, "TRCParameters").item(0);
        if (trcDetails.getElementsByTagNameNS(TRC_NS, "PatientId").item(0) == null) {
            throw new WebServiceException("Patient ID is Missing from the RST"); // Cannot
            // be
            // null!
        }
        String patientId = trcDetails
                .getElementsByTagNameNS(TRC_NS, "PatientId").item(0)
                .getTextContent();
        return patientId;
    }


    private String getMessageIdFromHeader(SOAPHeader header) {
        if (header.getElementsByTagNameNS(ADDRESSING_NS, "MessageID")
                .getLength() < 1) {
            throw new WebServiceException("Message ID not found in Header");
        }
        String mid = header.getElementsByTagNameNS(ADDRESSING_NS, "MessageID")
                .item(0).getTextContent();
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
            SOAPElement messageIdElem = header.addHeaderElement(new QName(
                    ADDRESSING_NS, "MessageID", "wsa"));
            messageIdResponse = "uuid:" + UUID.randomUUID().toString();
            messageIdElem
                    .setTextContent(messageIdResponse);
            SOAPElement securityHeaderElem = header.addHeaderElement(new QName(
                    WS_SEC_NS, "Security", "wsse"));

            SOAPElement timeStampElem = fac.createElement("Timestamp", "wsu",
                    WS_SEC_UTIL_NS);
            SOAPElement ltCreated = fac.createElement("Created", "wsu",
                    WS_SEC_UTIL_NS);
            ltCreated.setTextContent(now.toDateTime(DateTimeZone.UTC)
                    .toString());

            SOAPElement ltExpires = fac.createElement("Expires", "wsu",
                    WS_SEC_UTIL_NS);
            ltExpires.setTextContent(now.plusHours(2)
                    .toDateTime(DateTimeZone.UTC).toString());

            timeStampElem.addChildElement(ltCreated);
            timeStampElem.addChildElement(ltExpires);

            securityHeaderElem.addChildElement(timeStampElem);

            SOAPElement actionElem = header.addHeaderElement(new QName(
                    ADDRESSING_NS, "Action", "wsa"));
            actionElem.setTextContent("urn:IssueTokenResponse");

            SOAPElement relatesToElem = header.addHeaderElement(new QName(
                    ADDRESSING_NS, "RelatesTo", "wsa"));
            relatesToElem.setTextContent(messageId);

        } catch (SOAPException ex) {
            logger.error(null, ex);
            throw new WebServiceException("Could not create Response Header");
        }
    }

    private Document createRSTRC(Document assertion) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document respBody = builder.newDocument();

            Element rstrcElem = respBody.createElementNS(WS_TRUST_NS,
                    "wst:RequestSecurityTokenResponseCollection");
            respBody.appendChild(rstrcElem);

            Element rstrElem = respBody.createElementNS(WS_TRUST_NS,
                    "wst:RequestSecurityTokenResponse");
            rstrcElem.appendChild(rstrElem);

            Element rstElem = respBody.createElementNS(WS_TRUST_NS,
                    "wst:RequestedSecurityToken");
            rstrElem.appendChild(rstElem);

            // add the Assertion
            rstElem.appendChild(respBody.importNode(
                    assertion.getDocumentElement(), true));

            Element tokenTypeElem = respBody.createElementNS(WS_TRUST_NS,
                    "wst:TokenType");
            tokenTypeElem.setTextContent(SAML20_TOKEN_URN);

            rstrElem.appendChild(tokenTypeElem);

            Element lifeTimeElem = respBody.createElementNS(WS_TRUST_NS,
                    "wst:LifeTime");
            rstrElem.appendChild(lifeTimeElem);

            DateTime now = new DateTime();

            Element ltCreated = respBody.createElementNS(WS_SEC_UTIL_NS,
                    "wsu:Created");
            ltCreated.setTextContent(now.toDateTime(DateTimeZone.UTC)
                    .toString());

            lifeTimeElem.appendChild(ltCreated);

            Element ltExpires = respBody.createElementNS(WS_SEC_UTIL_NS,
                    "wsu:Expires");
            ltExpires.setTextContent(now.plusHours(2)
                    .toDateTime(DateTimeZone.UTC).toString());

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

        return body.getElementsByTagNameNS(WS_TRUST_NS, "RequestType").item(0)
                .getTextContent();
    }

    private String getRequestedToken(SOAPBody body) throws WSTrustException {

        if (body.getElementsByTagNameNS(WS_TRUST_NS, "TokenType").getLength() < 1) {
            throw new WSTrustException("No Token Type is Specified.");
        }

        return body.getElementsByTagNameNS(WS_TRUST_NS, "TokenType").item(0)
                .getTextContent();

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

    // private void audit(String pointOfCareID,
    // String humanRequestorNameID,
    // String humanRequestorSubjectID,
    // String humanRequestorRole,
    // String patientID,
    // String facilityType,
    // String assertionId,
    // String tls_cn,
    // String reqMid,
    // byte[] reqSecHeader,
    // String resMid,
    // byte[] resSecHeader)
    // {
    // AuditService asd = new AuditService();
    // GregorianCalendar c = new GregorianCalendar();
    // c.setTime(new Date());
    // XMLGregorianCalendar date2 = null;
    // try {
    // date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    // } catch (DatatypeConfigurationException ex) {
    //
    // }
    //
    // ConfigurationManagerService cms =
    // ConfigurationManagerService.getInstance();
    //
    // EventLog evLogTRC = EventLog.createEventLogTRCA(
    // TransactionName.epsosTRCAssertion,
    // EventActionCode.EXECUTE,
    // date2,
    // EventOutcomeIndicator.FULL_SUCCESS,
    // pointOfCareID,
    // facilityType,
    // cms.getProperty("ncp.country")+"<" + humanRequestorNameID + "@" +
    // cms.getProperty("ncp.country")+ ">" ,
    // humanRequestorRole,
    // humanRequestorSubjectID,
    // tls_cn,
    // getServerIP(),
    // cms.getProperty("COUNTRY_PRINCIPAL_SUBDIVISION"),
    // patientID,
    // "urn:uuid:" + assertionId,
    // reqMid,
    // reqSecHeader,
    // resMid,
    // resSecHeader,
    // getServerIP(),
    // getClientIP());
    //
    // evLogTRC.setEventType(EventType.epsosTRCAssertion);
    // asd.write(evLogTRC, "13", "2");
    //
    // }

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
        HttpServletRequest req = (HttpServletRequest) mc
                .get(MessageContext.SERVLET_REQUEST);
        return req.getRemoteAddr();
    }

    private X509Certificate getSSLCert() {
        MessageContext msgCtx = context.getMessageContext();

        javax.servlet.ServletRequest sreq = (javax.servlet.ServletRequest) msgCtx
                .get(MessageContext.SERVLET_REQUEST);

        if (sreq instanceof HttpServletRequest && sreq.isSecure()) {
            logger.info("Secure and http");
            HttpServletRequest hreq = (HttpServletRequest) sreq;
            X509Certificate peerCert[] = (X509Certificate[]) hreq
                    .getAttribute("javax.servlet.request.X509Certificate");
            if (peerCert != null) {
                return peerCert[0];
            }
        }
        return null;
    }

    private String getSSLCertPeer() {
        String user = "Unknown(No Client Certificate)";
        MessageContext msgCtx = context.getMessageContext();

        javax.servlet.ServletRequest sreq = (javax.servlet.ServletRequest) msgCtx
                .get(MessageContext.SERVLET_REQUEST);

        if (sreq instanceof HttpServletRequest && sreq.isSecure()) {
            logger.info("Secure and http");
            HttpServletRequest hreq = (HttpServletRequest) sreq;
            X509Certificate peerCert[] = (X509Certificate[]) hreq
                    .getAttribute("javax.servlet.request.X509Certificate");
            if (peerCert != null) {
                return peerCert[0].getSubjectDN().getName();
            }
        }
        return user;
    }

    /**
     * Performs a digest over the soapBody.getOwnerDocument().
     *
     * @return The md5 string
     * @throws IOException              in case of I/O error serializing
     * @throws SOAPException            if the document created is wrong
     * @throws NoSuchAlgorithmException If the message digest is not able to find SHA-1
     */
    public final String doDigest(SOAPMessage envelope) throws IOException, SOAPException, NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.reset();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        OutputFormat format = new OutputFormat();

        format.setIndenting(false);
        format.setIndent(2);
        format.setEncoding("UTF-8");
        format.setOmitComments(true);
        format.setOmitXMLDeclaration(false);
        format.setVersion("1.0");
        format.setStandalone(true);


        XMLSerializer serializer = new XMLSerializer(baos, format);
        serializer.serialize(envelope.getSOAPBody().getOwnerDocument());
        md.update(baos.toByteArray());
        return Base64.getEncoder().encodeToString(md.digest());
    }
}
