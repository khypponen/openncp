package eu.epsos.util;

import eu.esens.abb.nonrep.Context;
import eu.esens.abb.nonrep.ESensObligation;
import eu.esens.abb.nonrep.EnforcePolicy;
import eu.esens.abb.nonrep.EnforcePolicyException;
import eu.esens.abb.nonrep.MalformedIHESOAPException;
import eu.esens.abb.nonrep.MalformedMIMEMessageException;
import eu.esens.abb.nonrep.MessageInspector;
import eu.esens.abb.nonrep.MessageType;
import eu.esens.abb.nonrep.ObligationDischargeException;
import eu.esens.abb.nonrep.ObligationHandler;
import eu.esens.abb.nonrep.ObligationHandlerFactory;
import eu.esens.abb.nonrep.TOElementException;
import eu.esens.abb.nonrep.UnknownMessageType;
import eu.esens.abb.nonrep.Utilities;
import eu.esens.abb.nonrep.XACMLAttributes;
import eu.esens.abb.nonrep.XACMLRequestCreator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.UnorderedPolicyRepository;
import org.herasaf.xacml.core.policy.PolicyMarshaller;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.herasaf.xacml.core.utils.JAXBMarshallerConfiguration;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.DateUtil;
import tr.com.srdc.epsos.util.FileUtil;
import tr.com.srdc.epsos.util.XMLUtil;

/**
 *
 * @author karkaletsis
 */
public class EvidenceUtils {

    private static final Logger logger = Logger.getLogger(EvidenceUtils.class);
    public static final String DATATYPE_STRING = "http://www.w3.org/2001/XMLSchema#string";
    public static final String DATATYPE_DATETIME = "http://www.w3.org/2001/XMLSchema#dateTime";
    public static final String IHE_ITI_XCA_RETRIEVE = "urn:ihe:iti:2007:CrossGatewayRetrieve";

    private static boolean checkCorrectnessofIHEXCA(final MessageType messageType) {
        return true;
    }

    public static void createEvidenceREMNRR(
            Document incomingMsg,
            String issuerKeyStorePath,
            String issuerKeyPassword,
            String issuerCertAlias,
            String senderKeyStorePath,
            String senderKeyPassword,
            String senderCertAlias,
            String recipientKeyStorePath,
            String recipientKeyPassword,
            String recipientCertAlias,
            String eventType,
            DateTime submissionTime,
            String status,
            String title
    ) throws MalformedMIMEMessageException, MalformedIHESOAPException, SOAPException, ParserConfigurationException, SAXException, IOException, URISyntaxException, TOElementException, EnforcePolicyException, ObligationDischargeException, TransformerException, SyntaxException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {


        MessageType messageType = null;
        String msguuid = "";
        try {
            MessageInspector messageInspector = new MessageInspector(incomingMsg);
            messageType = messageInspector.getMessageType();
            msguuid = messageInspector.getMessageUUID();
        } catch (Exception e) {
            UnknownMessageType umt = new UnknownMessageType(incomingMsg);
            messageType = umt;
            msguuid = UUID.randomUUID().toString();
        }
        createEvidenceREMNRR(incomingMsg, issuerKeyStorePath, issuerKeyPassword, 
                issuerCertAlias, senderKeyStorePath, senderKeyPassword,
                senderCertAlias, recipientKeyStorePath, recipientKeyPassword,
                recipientCertAlias, eventType, submissionTime, status, title, msguuid);
    }

    public static void createEvidenceREMNRR(
            Document incomingMsg,
            String issuerKeyStorePath,
            String issuerKeyPassword,
            String issuerCertAlias,
            String senderKeyStorePath,
            String senderKeyPassword,
            String senderCertAlias,
            String recipientKeyStorePath,
            String recipientKeyPassword,
            String recipientCertAlias,
            String eventType,
            DateTime submissionTime,
            String status,
            String title,
            String msguuid
    ) throws MalformedMIMEMessageException, MalformedIHESOAPException, SOAPException, ParserConfigurationException, SAXException, IOException, URISyntaxException, TOElementException, EnforcePolicyException, ObligationDischargeException, TransformerException, SyntaxException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        String statusmsg = "failure";
        if (status.equals("0")) {
            statusmsg = "success";
        }

        PDP simplePDP = SimplePDPFactory.getSimplePDP();

        UnorderedPolicyRepository polrep = (UnorderedPolicyRepository) simplePDP
                .getPolicyRepository();

        ClassLoader loader;
        loader = EvidenceUtils.class.getClassLoader();
        InputStream inputStream = loader.getResourceAsStream("policy/samplePolicyNRR.xml");

        polrep.deploy(PolicyMarshaller.unmarshal(inputStream));

        /*
         * Instantiate the message inspector, to see which type of message is
         */
        MessageType messageType = null;
//        String msguuid = "";
        try {
            MessageInspector messageInspector = new MessageInspector(incomingMsg);
            messageType = messageInspector.getMessageType();
        } catch (Exception e) {
            UnknownMessageType umt = new UnknownMessageType(incomingMsg);
            messageType = umt;
        }
        /*
         * Now create the XACML request
         */
        LinkedList<XACMLAttributes> actionList = new LinkedList<XACMLAttributes>();
        XACMLAttributes action = new XACMLAttributes();
        action.setDataType(new URI(DATATYPE_STRING));
        action.setIdentifier(new URI("urn:eSENS:outcome"));
        actionList.add(action);
        action.setValue(statusmsg);

        LinkedList<XACMLAttributes> environmentList = new LinkedList<XACMLAttributes>();
        XACMLAttributes environment = new XACMLAttributes();
        environment.setDataType(new URI(DATATYPE_DATETIME));
        environment.setIdentifier(new URI("urn:esens:2014:event"));
        environment.setValue(new DateTime().toString());
        environmentList.add(environment);

        XACMLRequestCreator requestCreator = new XACMLRequestCreator(
                messageType, null, null, actionList, environmentList);

        Element request = requestCreator.getRequest();

        // just some printouts
        Utilities.serialize(request);

        EnforcePolicy enforcePolicy = new EnforcePolicy(simplePDP);

        enforcePolicy.decide(request);
        Utilities.serialize(enforcePolicy.getResponseAsDocument()
                .getDocumentElement());

        List<ESensObligation> obligations = enforcePolicy.getObligationList();

        Context context = new Context();
        context.setIncomingMsg(incomingMsg);
        
        /* Loading the different certificates */
        X509Certificate issuerCert = getCertificate(issuerKeyStorePath, issuerKeyPassword, issuerCertAlias);
        X509Certificate senderCert = getCertificate(senderKeyStorePath, senderKeyPassword, senderCertAlias);
        X509Certificate recipientCert = getCertificate(recipientKeyStorePath, recipientKeyPassword, recipientCertAlias);
        context.setIssuerCertificate(issuerCert);
        context.setSenderCertificate(senderCert); 
        context.setRecipientCertificate(recipientCert);
        
        /* Signing key is the issuer key */
        PrivateKey key = getSigningKey(issuerKeyStorePath, issuerKeyPassword, issuerCertAlias);
        context.setSigningKey(key);
        context.setSubmissionTime(submissionTime);
        context.setEvent(eventType);
        context.setMessageUUID(msguuid);
        context.setAuthenticationMethod("http//:uri.etsi.org/REM/AuthMethod#Strong");
        context.setRequest(request);
        context.setEnforcer(enforcePolicy);

        ObligationHandlerFactory handlerFactory = ObligationHandlerFactory
                .getInstance();
        List<ObligationHandler> handlers = handlerFactory.createHandler(
                messageType, obligations, context);

        int handlersSize = handlers.size();
        for (int j = 0; j < handlersSize; j++) {
            ObligationHandler oh = handlers.get(j);
            oh.discharge();
            Utilities.serialize(handlers.get(j).getMessage().getDocumentElement());
            String oblString = XMLUtil.DocumentToString(handlers.get(j).getMessage());
            if (title == null || title.isEmpty()) {
                title = getPath() + "nrr/" + getDocumentTitle(msguuid, handlers.get(j).toString()) + ".xml";
            } else {
                title = getPath() + "nrr/" + getDocumentTitle(msguuid, title) + ".xml";
            }
            logger.info("MSGUUID: " + msguuid + " " + "NRR TITLE :" + title);
            FileUtil.constructNewFile(title, oblString.getBytes());
        }
    }

    /* Joao: commented since this is not used anymore */
//    public static void createEvidenceREMNRR(
//            Document incomingSoap,
//            String eventType,
//            DateTime submissionTime,
//            String status,
//            String title,
//            String msguuid) throws MalformedMIMEMessageException, MalformedIHESOAPException, SOAPException, ParserConfigurationException, SAXException, IOException, URISyntaxException, TOElementException, EnforcePolicyException, ObligationDischargeException, TransformerException, SyntaxException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
//
//        createEvidenceREMNRR(incomingSoap, Constants.NCP_SIG_KEYSTORE_PATH, Constants.NCP_SIG_KEYSTORE_PASSWORD, Constants.NCP_SIG_PRIVATEKEY_ALIAS, eventType, submissionTime, status, title, msguuid);
//    }

    /* Joao: commented since this is not used anymore */
//    public static void createEvidenceREMNRO(
//            Document incomingSoap,
//            String eventType,
//            DateTime submissionTime,
//            String status,
//            String title,
//            String msguuid) throws MalformedMIMEMessageException, MalformedIHESOAPException, SOAPException, ParserConfigurationException, SAXException, IOException, URISyntaxException, TOElementException, EnforcePolicyException, ObligationDischargeException, TransformerException, SyntaxException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
//
//        createEvidenceREMNRO(incomingSoap, Constants.NCP_SIG_KEYSTORE_PATH, Constants.NCP_SIG_KEYSTORE_PASSWORD, Constants.NCP_SIG_PRIVATEKEY_ALIAS, eventType, submissionTime, status, title, msguuid);
//
//    }

    public static void createEvidenceREMNRO(
            Document incomingSoap,
            String issuerKeyStorePath,
            String issuerKeyPassword,
            String issuerCertAlias,
            String senderKeyStorePath,
            String senderKeyPassword,
            String senderCertAlias,
            String recipientKeyStorePath,
            String recipientKeyPassword,
            String recipientCertAlias,
            String eventType,
            DateTime submissionTime,
            String status,
            String title) throws MalformedMIMEMessageException, MalformedIHESOAPException, SOAPException, ParserConfigurationException, SAXException, IOException, URISyntaxException, TOElementException, EnforcePolicyException, ObligationDischargeException, TransformerException, SyntaxException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {

//        Document incomingMsg = XMLUtil.parseContent(incomingSoap);

        MessageType messageType = null;
        String msguuid = "";
        try {
            MessageInspector messageInspector = new MessageInspector(incomingSoap);
            messageType = messageInspector.getMessageType();
            msguuid = messageInspector.getMessageUUID();
        } catch (Exception e) {
            UnknownMessageType umt = new UnknownMessageType(incomingSoap);
            messageType = umt;
            msguuid = UUID.randomUUID().toString();
        }
        createEvidenceREMNRO(incomingSoap, issuerKeyStorePath, issuerKeyPassword, 
                issuerCertAlias, senderKeyStorePath, senderKeyPassword,
                senderCertAlias, recipientKeyStorePath, recipientKeyPassword,
                recipientCertAlias, eventType, submissionTime, status, title, msguuid);

    }

    public static void createEvidenceREMNRO(
            Document incomingSoap,
            String issuerKeyStorePath,
            String issuerKeyPassword,
            String issuerCertAlias,
            String senderKeyStorePath,
            String senderKeyPassword,
            String senderCertAlias,
            String recipientKeyStorePath,
            String recipientKeyPassword,
            String recipientCertAlias,
            String eventType,
            DateTime submissionTime,
            String status,
            String title,
            String msguuid
    ) throws MalformedMIMEMessageException, MalformedIHESOAPException, SOAPException, ParserConfigurationException, SAXException, IOException, URISyntaxException, TOElementException, EnforcePolicyException, ObligationDischargeException, TransformerException, SyntaxException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        String statusmsg = "failure";
        if (status.equals("0")) {
            statusmsg = "success";
        }
//        Document incomingMsg = XMLUtil.parseContent(incomingSoap);
        PDP simplePDP = SimplePDPFactory.getSimplePDP();
        UnorderedPolicyRepository polrep = (UnorderedPolicyRepository) simplePDP
                .getPolicyRepository();

        JAXBMarshallerConfiguration conf = new JAXBMarshallerConfiguration();
        conf.setValidateParsing(false);
        conf.setValidateWriting(false);
        PolicyMarshaller.setJAXBMarshallerConfiguration(conf);
        // Populate the policy repository
        ClassLoader loader;
        loader = EvidenceUtils.class.getClassLoader();
        InputStream inputStream = loader.getResourceAsStream("policy/samplePolicy.xml");
        polrep.deploy(PolicyMarshaller.unmarshal(inputStream));

        // Read the message as it arrives at the facade
        MessageType messageType = null;
        try {
            MessageInspector messageInspector = new MessageInspector(incomingSoap);
            messageType = messageInspector.getMessageType();
        } catch (Exception e) {
            UnknownMessageType umt = new UnknownMessageType(incomingSoap);
            messageType = umt;
        }
        if (checkCorrectnessofIHEXCA(messageType)) {
            logger.info("The message type : " + messageType + " is correct");
        }

        /*
         * Now create the XACML request
         */
        LinkedList<XACMLAttributes> actionList = new LinkedList<XACMLAttributes>();
        XACMLAttributes action = new XACMLAttributes();
        action.setDataType(new URI(DATATYPE_STRING));
        action.setIdentifier(new URI("urn:eSENS:outcome"));
        actionList.add(action);
        action.setValue(statusmsg);

        LinkedList<XACMLAttributes> environmentList = new LinkedList<XACMLAttributes>();
        XACMLAttributes environment = new XACMLAttributes();
        environment.setDataType(new URI(DATATYPE_DATETIME));
        environment.setIdentifier(new URI("urn:esens:2014:event"));
        environment.setValue(new DateTime().toString());
        environmentList.add(environment);

        XACMLRequestCreator requestCreator = new XACMLRequestCreator(
                messageType, null, null, actionList, environmentList);

        Element request = requestCreator.getRequest();
        Utilities.serialize(request);

        /*
         * Call the XACML engine.
         *
         * The policy has been deployed in the setupBeforeClass.
         */
        EnforcePolicy enforcePolicy = new EnforcePolicy(simplePDP);

        enforcePolicy.decide(request);
        Utilities.serialize(enforcePolicy.getResponseAsDocument()
                .getDocumentElement());

        List<ESensObligation> obligations = enforcePolicy.getObligationList();

        Context context = new Context();
        context.setIncomingMsg(incomingSoap);
        
        /* Loading the different certificates */
        X509Certificate issuerCert = getCertificate(issuerKeyStorePath, issuerKeyPassword, issuerCertAlias);
        X509Certificate senderCert = getCertificate(senderKeyStorePath, senderKeyPassword, senderCertAlias);
        X509Certificate recipientCert = getCertificate(recipientKeyStorePath, recipientKeyPassword, recipientCertAlias);
        context.setIssuerCertificate(issuerCert);
        context.setSenderCertificate(senderCert); 
        context.setRecipientCertificate(recipientCert);
        
        /* Signing key is the issuer key */
        PrivateKey key = getSigningKey(issuerKeyStorePath, issuerKeyPassword, issuerCertAlias);
        context.setSigningKey(key);
        context.setSubmissionTime(submissionTime);
        context.setEvent(eventType);
        context.setMessageUUID(msguuid);
        context.setAuthenticationMethod("http//:uri.etsi.org/REM/AuthMethod#Strong");
        context.setRequest(request);
        context.setEnforcer(enforcePolicy);
        ObligationHandlerFactory handlerFactory = ObligationHandlerFactory
                .getInstance();
        List<ObligationHandler> handlers = handlerFactory.createHandler(
                messageType, obligations, context);

        // Here I discharge manually. This behavior is to let free an
        // implementation
        int handlersSize = handlers.size();

        for (int j = 0; j < handlersSize; j++) {
            handlers.get(j).discharge();
            Utilities.serialize(handlers.get(j).getMessage().getDocumentElement());
            String oblString = XMLUtil.DocumentToString(handlers.get(j).getMessage());
            if (title == null || title.isEmpty()) {
                title = getPath() + "nro/" + getDocumentTitle(msguuid, handlers.get(j).toString()) + ".xml";
            } else {
                title = getPath() + "nro/" + getDocumentTitle(msguuid, title) + ".xml";
            }
            logger.info("MSGUUID: " + msguuid + " " + "NRO TITLE :" + title);
            FileUtil.constructNewFile(title, oblString.getBytes());
        }

    }

    private static String getPath() {
        String exportPath = System.getenv("EPSOS_PROPS_PATH");
        return exportPath + "obligations/";
    }

    private static String getDocumentTitle(String uuid, String title) {
        return DateUtil.getCurrentTimeGMT() + "_" + uuid + "_" + title;
    }

    public static Document readMessage(String file)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document incomingMsg = db.parse(new File(file));
        return incomingMsg;
    }

    private static X509Certificate getCertificate(String keyStorePath, String keyPassword, String certAlias) throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore ks = KeyStore.getInstance("JKS");
        InputStream keyStream = new FileInputStream(new File(keyStorePath));
        ks.load(keyStream, keyPassword.toCharArray());
        X509Certificate cert = (X509Certificate) ks.getCertificate(certAlias);
        return cert;
    }
    
    private static PrivateKey getSigningKey(String keyStorePath, String keyPassword, String certAlias) throws FileNotFoundException, KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("JKS");
        InputStream keyStream = new FileInputStream(new File(keyStorePath));
        ks.load(keyStream, keyPassword.toCharArray());
        PrivateKey key = (PrivateKey) ks.getKey(certAlias, keyPassword.toCharArray());
        return key;
    }
}
