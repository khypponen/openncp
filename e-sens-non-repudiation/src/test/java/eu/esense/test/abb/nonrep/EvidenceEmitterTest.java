package eu.esense.test.abb.nonrep;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.UnorderedPolicyRepository;
import org.herasaf.xacml.core.policy.PolicyMarshaller;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.herasaf.xacml.core.utils.JAXBMarshallerConfiguration;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import eu.esens.abb.nonrep.Context;
import eu.esens.abb.nonrep.ESensObligation;
import eu.esens.abb.nonrep.EnforcePolicy;
import eu.esens.abb.nonrep.EnforcePolicyException;
import eu.esens.abb.nonrep.IHEXCARetrieve;
import eu.esens.abb.nonrep.MalformedIHESOAPException;
import eu.esens.abb.nonrep.MalformedMIMEMessageException;
import eu.esens.abb.nonrep.MessageInspector;
import eu.esens.abb.nonrep.MessageType;
import eu.esens.abb.nonrep.ObligationDischargeException;
import eu.esens.abb.nonrep.ObligationHandler;
import eu.esens.abb.nonrep.ObligationHandlerFactory;
import eu.esens.abb.nonrep.TOElementException;
import eu.esens.abb.nonrep.Utilities;
import eu.esens.abb.nonrep.XACMLAttributes;
import eu.esens.abb.nonrep.XACMLRequestCreator;
import junit.framework.TestCase;

@RunWith(JUnit4.class)
public class EvidenceEmitterTest extends TestCase {

    public static final String DATATYPE_STRING = "http://www.w3.org/2001/XMLSchema#string";
    public static final String DATATYPE_DATETIME = "http://www.w3.org/2001/XMLSchema#dateTime";
    public static final String IHE_ITI_XCA_RETRIEVE = "urn:ihe:iti:2007:CrossGatewayRetrieve";
    private PDP simplePDP;
    private X509Certificate cert;
    private PrivateKey key;

    @Before
    public void setUpBeforeClass() throws Exception {

        /*
         * the polrep is here because an adHoc implementation can have more
         * requirements than the default from herasaf, if needed
         */
        simplePDP = SimplePDPFactory.getSimplePDP();
        UnorderedPolicyRepository polrep = (UnorderedPolicyRepository) simplePDP
                .getPolicyRepository();

        // Populate the policy repository
        Document policy = readMessage("src/test/testData/samplePolicy.xml");

        polrep.deploy(PolicyMarshaller.unmarshal(policy));

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("src/test/testData/s1.keystore"),
                "spirit".toCharArray());
        cert = (X509Certificate) ks.getCertificate("server1");
        key = (PrivateKey) ks.getKey("server1", "spirit".toCharArray());
        org.apache.xml.security.Init.init();
    }

    
    /**
     * This test reads a sample message from the eHealth domain (XCA) and will
     * issue an ATNA-specific audit trail.
     *
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws MalformedIHESOAPException
     * @throws URISyntaxException
     * @throws TOElementException
     * @throws EnforcePolicyException
     * @throws ObligationDischargeException
     * @throws SOAPException
     */
    public void testGenerateATNA() throws ParserConfigurationException,
            SAXException, IOException, MalformedIHESOAPException,
            URISyntaxException, TOElementException, EnforcePolicyException,
            ObligationDischargeException, SOAPException {
        testGenerateAtna();
    }

    /**
     * (Muhammet) I had to add this method because I need the message, but Junit
     * does not like non-void test methods.
     *
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws MalformedIHESOAPException
     * @throws URISyntaxException
     * @throws TOElementException
     * @throws EnforcePolicyException
     * @throws ObligationDischargeException
     * @throws SOAPException
     */
    public Document testGenerateAtna() throws ParserConfigurationException,
            SAXException, IOException, MalformedIHESOAPException,
            URISyntaxException, TOElementException, EnforcePolicyException,
            ObligationDischargeException, SOAPException {
        /*
         * AlternativeUserID = IP of the machine NetworkAccessPointID = IP of
         * the machine UserName = user from the SAML assertion
         *
         * NetworkAccessPointID = IP of the remote
         *
         * UserId = Subject of the assertion UserName = subject-id
         *
         * ParticipantObjectID = xdsb:RepositoryUniqueId ParticipantObjectID =
         * xdsb:DocumentUniqueId
         */

        /*
         * The flow is as follows (imagine that the PEP is a facade in front of
         * the Corner). The message is inspected, the relevant information is
         * retrieved and placed into the XACML request. The PDP evaluates the
         * request and returns the pointer of the obligation handler.
         */
        // Configure Log4j
        //BasicConfigurator.configure();

        // Read the message as it arrives at the facade
//		Document incomingMsg = readMessage("test/testData/incomingMsg.xml");
        Document incomingMsg = readMessage("src/test/testData/audit.xml");
        //	SOAPMessage message = Utilities.toSoap(incomingMsg, null);
		/*
         * Instantiate the message inspector, to see which type of message is
         */
        MessageInspector messageInspector = new MessageInspector(incomingMsg);
        MessageType messageType = messageInspector.getMessageType();
        assertNotNull(messageType);

        /*
         * In this mock, we have an IHE
         */
        checkCorrectnessofIHEXCA(messageType);

        /*
         * Now create the XACML request
         */
        LinkedList<XACMLAttributes> actionList = new LinkedList<XACMLAttributes>();
        XACMLAttributes action = new XACMLAttributes();
        action.setDataType(new URI(DATATYPE_STRING));
        action.setIdentifier(new URI(
                "urn:oasis:names:tc:xacml:1.0:action:action-id"));
        actionList.add(action);

        // Here I imagine a table lookup or similar
        action.setValue(messageType instanceof IHEXCARetrieve ? "IHE_ITI_XCA_RETRIEVE"
                : "UNKNOWN");

        LinkedList<XACMLAttributes> environmentList = new LinkedList<XACMLAttributes>();
        XACMLAttributes environment = new XACMLAttributes();
        environment.setDataType(new URI(DATATYPE_DATETIME));
        environment.setIdentifier(new URI("urn:esens:2014:event"));
        environment.setValue(new DateTime().toString());
        environmentList.add(environment);

        XACMLRequestCreator requestCreator = new XACMLRequestCreator(
                messageType, null, null, actionList, environmentList);

        Element request = requestCreator.getRequest();
        assertNotNull(request);

        // just some printouts
        Utilities.serialize(request);

        /*
         * Call the XACML engine.
         *
         * The policy has been deployed in the setupBeforeClass.
         */
        EnforcePolicy enforcePolicy = new EnforcePolicy(simplePDP);

        enforcePolicy.decide(request);
        assertNotNull(enforcePolicy.getResponseAsDocument());
        assertNotNull(enforcePolicy.getResponseAsObject());
        Utilities.serialize(enforcePolicy.getResponseAsDocument()
                .getDocumentElement());

        List<ESensObligation> obligations = enforcePolicy.getObligationList();
        assertNotNull(obligations);

        Context context = new Context();
        context.setIncomingMsg(incomingMsg);
        context.setRequest(request); // here I pass the XML in order to give to
        // the developers the posisbility
        // to use their own implementation. Although an object is easier to get
        // the relevant types (e.g., action
        // environment
        context.setEnforcer(enforcePolicy);
        context.setUsername("demo2");
        context.setCurrentHost("127.0.0.1");
        context.setRemoteHost("192.168.10.1");

        ObligationHandlerFactory handlerFactory = ObligationHandlerFactory
                .getInstance();
        List<ObligationHandler> handlers = handlerFactory.createHandler(
                messageType, obligations, context);

        // Here I discharge manually. This behavior is to let free an
        // implementation
        // to still decide which handler to trigger
        handlers.get(0).discharge();
        handlers.get(1).discharge();

        // Give me the ATNA, it's an ATNA test
        assertNotNull(handlers.get(0).getMessage());
        Utilities.serialize(handlers.get(0).getMessage().getDocumentElement());

        // I think I need to return handler.getMessage() which will be the audit
        // the audit will go to the server and get validated by another wrapper
        return handlers.get(0).getMessage();
    }

    @Test
    public void testGenerateRemNRO() throws ParserConfigurationException,
            SAXException, IOException, MalformedIHESOAPException,
            URISyntaxException, TOElementException, EnforcePolicyException,
            ObligationDischargeException, SOAPException, MalformedMIMEMessageException {
        testGenerateREMNRO();
    }

    /**
     * This method issue a REM NRO evidence
     *
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws MalformedIHESOAPException
     * @throws URISyntaxException
     * @throws TOElementException
     * @throws EnforcePolicyException
     * @throws ObligationDischargeException
     * @throws SOAPException
     * @throws MalformedMIMEMessageException
     */
    public Document testGenerateREMNRO() throws ParserConfigurationException,
            SAXException, IOException, MalformedIHESOAPException,
            URISyntaxException, TOElementException, EnforcePolicyException,
            ObligationDischargeException, SOAPException, MalformedMIMEMessageException {


        /*
         * The flow is as follows (imagine that the PEP is a facade in front of
         * the Corner). The message is inspected, the relevant information is
         * retrieved and placed into the XACML request. The PDP evaluates the
         * request and returns the pointer of the obligation handler.
         */
        // Configure Log4j
        //BasicConfigurator.configure();

        // Read the message as it arrives at the facade
        Document incomingMsg = readMessage("src/test/testData/incomingMsg.xml");
        SOAPMessage message = Utilities.toSoap(incomingMsg, null);

        /*
         * Instantiate the message inspector, to see which type of message is
         */
        MessageInspector messageInspector = new MessageInspector(message);
        MessageType messageType = messageInspector.getMessageType();
        assertNotNull(messageType);
        assertNotNull(messageInspector.getMessageUUID());
        assertEquals("uuid:C3F5A03D-1A0C-4F62-ADC7-F3C007CD50CF", messageInspector.getMessageUUID());

        /*
         * In this mock, we have an IHE
         */
        checkCorrectnessofIHEXCA(messageType);

        /*
         * Now create the XACML request
         */
        LinkedList<XACMLAttributes> actionList = new LinkedList<XACMLAttributes>();
        XACMLAttributes action = new XACMLAttributes();
        action.setDataType(new URI(DATATYPE_STRING));
        action.setIdentifier(new URI(
                "urn:eSENS:outcome"));
        actionList.add(action);

        // Here I imagine a table lookup or similar
        action.setValue("success");

        LinkedList<XACMLAttributes> environmentList = new LinkedList<XACMLAttributes>();
        XACMLAttributes environment = new XACMLAttributes();
        environment.setDataType(new URI(DATATYPE_DATETIME));
        environment.setIdentifier(new URI("urn:esens:2014:event"));
        environment.setValue(new DateTime().toString());
        environmentList.add(environment);

        XACMLRequestCreator requestCreator = new XACMLRequestCreator(
                messageType, null, null, actionList, environmentList);

        Element request = requestCreator.getRequest();
        assertNotNull(request);

        // just some printouts
        Utilities.serialize(request);

        /*
         * Call the XACML engine.
         *
         * The policy has been deployed in the setupBeforeClass.
         */
        EnforcePolicy enforcePolicy = new EnforcePolicy(simplePDP);

        enforcePolicy.decide(request);
        assertNotNull(enforcePolicy.getResponseAsDocument());
        assertNotNull(enforcePolicy.getResponseAsObject());
        Utilities.serialize(enforcePolicy.getResponseAsDocument()
                .getDocumentElement());

        List<ESensObligation> obligations = enforcePolicy.getObligationList();
        assertNotNull(obligations);

        Context context = new Context();
        context.setIncomingMsg(message);
        context.setIssuerCertificate(cert);
        context.setSenderCertificate(cert);
        context.setRecipientCertificate(cert);
        context.setSigningKey(key);
        context.setSubmissionTime(new DateTime());
        context.setEvent("epSOS-31");
        
        context.setMessageUUID(messageInspector.getMessageUUID());
        context.setAuthenticationMethod("3");
        context.setRequest(request); // here I pass the XML in order to give to
        // the developers the possibility to use their own implementation.
        // Although an object is easier to get the relevant types (e.g., action environment
        context.setEnforcer(enforcePolicy);
        context.setUsername("demo2");
        context.setCurrentHost("127.0.0.1");
        context.setRemoteHost("192.168.10.1");

        ObligationHandlerFactory handlerFactory = ObligationHandlerFactory
                .getInstance();
        List<ObligationHandler> handlers = handlerFactory.createHandler(
                messageType, obligations, context);

        // Here I discharge manually. This behavior is to let free an
        // implementation
        // to still decide which handler to trigger
        handlers.get(0).discharge();
        handlers.get(1).discharge();

        // Give me the ATNA, it's an ATNA test
        assertNotNull(handlers.get(1).getMessage());
        Utilities.serialize(handlers.get(1).getMessage().getDocumentElement());

        // I think I need to return handler.getMessage() which will be the audit
        // the audit will go to the server and get validated by another wrapper
        return handlers.get(1).getMessage();
    }

    @Test
    public void testGenerateRemNRR() throws ParserConfigurationException,
            SAXException, IOException, MalformedIHESOAPException,
            URISyntaxException, TOElementException, EnforcePolicyException,
            ObligationDischargeException, SOAPException, MalformedMIMEMessageException, SyntaxException {
        testGenerateREMNRR();
    }

    /**
     * This method issue a REM NRO evidence
     *
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws MalformedIHESOAPException
     * @throws URISyntaxException
     * @throws TOElementException
     * @throws EnforcePolicyException
     * @throws ObligationDischargeException
     * @throws SOAPException
     * @throws MalformedMIMEMessageException
     * @throws SyntaxException
     */
    public Document testGenerateREMNRR() throws ParserConfigurationException,
            SAXException, IOException, MalformedIHESOAPException,
            URISyntaxException, TOElementException, EnforcePolicyException,
            ObligationDischargeException, SOAPException, MalformedMIMEMessageException, SyntaxException {


        /*
         * The flow is as follows (imagine that the PEP is a facade in front of
         * the Corner). The message is inspected, the relevant information is
         * retrieved and placed into the XACML request. The PDP evaluates the
         * request and returns the pointer of the obligation handler.
         */
        simplePDP = SimplePDPFactory.getSimplePDP();
        UnorderedPolicyRepository polrep = (UnorderedPolicyRepository) simplePDP
                .getPolicyRepository();

        JAXBMarshallerConfiguration conf = new JAXBMarshallerConfiguration();
        conf.setValidateParsing(false);
        conf.setValidateWriting(false);
        PolicyMarshaller.setJAXBMarshallerConfiguration(conf);

        // Populate the policy repository
        Document policy = readMessage("src/test/testData/samplePolicyNRR.xml");

        polrep.deploy(PolicyMarshaller.unmarshal(policy));

        // Configure Log4j
        //BasicConfigurator.configure();

        // Read the message as it arrives at the facade
//		Document incomingMsg = readMessage("test/testData/audit.xml");
        Document incomingMsg = readMessage("src/test/testData/incomingMsg.xml");

        SOAPMessage message = Utilities.toSoap(incomingMsg, null);

        /*
         * Instantiate the message inspector, to see which type of message is
         */
        MessageInspector messageInspector = new MessageInspector(message);
        MessageType messageType = messageInspector.getMessageType();
        assertNotNull(messageType);
	//	assertNotNull(messageInspector.getMessageUUID());
//		assertEquals("uuid:C3F5A03D-1A0C-4F62-ADC7-F3C007CD50CF",messageInspector.getMessageUUID());

        /*
         * In this mock, we have an IHE
         */
        //	checkCorrectnessofIHEXCA(messageType);

        /*
         * Now create the XACML request
         */
        LinkedList<XACMLAttributes> actionList = new LinkedList<XACMLAttributes>();
        XACMLAttributes action = new XACMLAttributes();
        action.setDataType(new URI(DATATYPE_STRING));
        action.setIdentifier(new URI(
                "urn:eSENS:outcome"));
        actionList.add(action);

        // Here I imagine a table lookup or similar
        action.setValue("success");

        LinkedList<XACMLAttributes> environmentList = new LinkedList<XACMLAttributes>();
        XACMLAttributes environment = new XACMLAttributes();
        environment.setDataType(new URI(DATATYPE_DATETIME));
        environment.setIdentifier(new URI("urn:esens:2014:event"));
        environment.setValue(new DateTime().toString());
        environmentList.add(environment);

        XACMLRequestCreator requestCreator = new XACMLRequestCreator(
                messageType, null, null, actionList, environmentList);

        Element request = requestCreator.getRequest();
        assertNotNull(request);

        // just some printouts
        Utilities.serialize(request);

        /*
         * Call the XACML engine.
         *
         * The policy has been deployed in the setupBeforeClass.
         */
        EnforcePolicy enforcePolicy = new EnforcePolicy(simplePDP);

        enforcePolicy.decide(request);
        assertNotNull(enforcePolicy.getResponseAsDocument());
        assertNotNull(enforcePolicy.getResponseAsObject());
        Utilities.serialize(enforcePolicy.getResponseAsDocument()
                .getDocumentElement());

        List<ESensObligation> obligations = enforcePolicy.getObligationList();
        assertNotNull(obligations);

        Context context = new Context();
        context.setIncomingMsg(incomingMsg);
        context.setIssuerCertificate(cert);
        context.setSenderCertificate(cert);
        context.setRecipientCertificate(cert);
        context.setSigningKey(key);
        context.setSubmissionTime(new DateTime());
        context.setEvent("epSOS-31");
        context.setMessageUUID(messageInspector.getMessageUUID());
        context.setAuthenticationMethod("3");
        context.setRequest(request); // here I pass the XML in order to give to
        // the developers the posisbility
        // to use their own implementation. Although an object is easier to get
        // the relevant types (e.g., action
        // environment
        context.setEnforcer(enforcePolicy);
//		context.setUsername("demo2");
//		context.setCurrentHost("127.0.0.1");
//		context.setRemoteHost("192.168.10.1");

        ObligationHandlerFactory handlerFactory = ObligationHandlerFactory
                .getInstance();
        List<ObligationHandler> handlers = handlerFactory.createHandler(
                messageType, obligations, context);

        // Here I discharge manually. This behavior is to let free an
        // implementation
        // to still decide which handler to trigger
        System.out.println(handlers.get(0).getClass().getName());

        handlers.get(0).discharge();
	//	handlers.get(1).discharge();

        // Give me the ATNA, it's an ATNA test
        assertNotNull(handlers.get(0).getMessage());
        Utilities.serialize(handlers.get(0).getMessage().getDocumentElement());

        // I think I need to return handler.getMessage() which will be the audit
        // the audit will go to the server and get validated by another wrapper
        return handlers.get(0).getMessage();
    }

    @Test
    public void testGenerateRemNRD() throws ParserConfigurationException,
            SAXException, IOException, MalformedIHESOAPException,
            URISyntaxException, TOElementException, EnforcePolicyException,
            ObligationDischargeException, SOAPException, MalformedMIMEMessageException, SyntaxException {
        testGenerateREMNRD();
    }
    /**
     * This method issue a REM NRD evidence
     *
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws MalformedIHESOAPException
     * @throws URISyntaxException
     * @throws TOElementException
     * @throws EnforcePolicyException
     * @throws ObligationDischargeException
     * @throws SOAPException
     * @throws MalformedMIMEMessageException
     * @throws SyntaxException
     */
    public Document testGenerateREMNRD() throws ParserConfigurationException,
            SAXException, IOException, MalformedIHESOAPException,
            URISyntaxException, TOElementException, EnforcePolicyException,
            ObligationDischargeException, SOAPException, MalformedMIMEMessageException, SyntaxException {


        /*
         * The flow is as follows (imagine that the PEP is a facade in front of
         * the Corner). The message is inspected, the relevant information is
         * retrieved and placed into the XACML request. The PDP evaluates the
         * request and returns the pointer of the obligation handler.
         */
        simplePDP = SimplePDPFactory.getSimplePDP();
        UnorderedPolicyRepository polrep = (UnorderedPolicyRepository) simplePDP
                .getPolicyRepository();

        JAXBMarshallerConfiguration conf = new JAXBMarshallerConfiguration();
        conf.setValidateParsing(false);
        conf.setValidateWriting(false);
        PolicyMarshaller.setJAXBMarshallerConfiguration(conf);

        // Populate the policy repository
        Document policy = readMessage("src/test/testData/samplePolicyNRD.xml");

        polrep.deploy(PolicyMarshaller.unmarshal(policy));

        // Configure Log4j
        //        BasicConfigurator.configure();
        //BasicConfigurator.configure();

        // Read the message as it arrives at the facade
//		Document incomingMsg = readMessage("test/testData/audit.xml");
        Document incomingMsg = readMessage("src/test/testData/incomingMsg.xml");

        SOAPMessage message = Utilities.toSoap(incomingMsg, null);

        /*
         * Instantiate the message inspector, to see which type of message is
         */
        MessageInspector messageInspector = new MessageInspector(message);
        MessageType messageType = messageInspector.getMessageType();
        assertNotNull(messageType);
	//	assertNotNull(messageInspector.getMessageUUID());
//		assertEquals("uuid:C3F5A03D-1A0C-4F62-ADC7-F3C007CD50CF",messageInspector.getMessageUUID());

        /*
         * In this mock, we have an IHE
         */
        //	checkCorrectnessofIHEXCA(messageType);

        /*
         * Now create the XACML request
         */
        LinkedList<XACMLAttributes> actionList = new LinkedList<XACMLAttributes>();
        XACMLAttributes action = new XACMLAttributes();
        action.setDataType(new URI(DATATYPE_STRING));
        action.setIdentifier(new URI(
                "urn:eSENS:outcome"));
        actionList.add(action);

        // Here I imagine a table lookup or similar
        action.setValue("success");

        LinkedList<XACMLAttributes> environmentList = new LinkedList<XACMLAttributes>();
        XACMLAttributes environment = new XACMLAttributes();
        environment.setDataType(new URI(DATATYPE_DATETIME));
        environment.setIdentifier(new URI("urn:esens:2014:event"));
        environment.setValue(new DateTime().toString());
        environmentList.add(environment);

        XACMLRequestCreator requestCreator = new XACMLRequestCreator(
                messageType, null, null, actionList, environmentList);

        Element request = requestCreator.getRequest();
        assertNotNull(request);

        // just some printouts
        Utilities.serialize(request);

        /*
         * Call the XACML engine.
         *
         * The policy has been deployed in the setupBeforeClass.
         */
        EnforcePolicy enforcePolicy = new EnforcePolicy(simplePDP);

        enforcePolicy.decide(request);
        assertNotNull(enforcePolicy.getResponseAsDocument());
        assertNotNull(enforcePolicy.getResponseAsObject());
        Utilities.serialize(enforcePolicy.getResponseAsDocument()
                .getDocumentElement());

        List<ESensObligation> obligations = enforcePolicy.getObligationList();
        assertNotNull(obligations);

        Context context = new Context();
        context.setIncomingMsg(incomingMsg);
        context.setIssuerCertificate(cert); 
        
        // Justice domain has them optional
        context.setSenderCertificate(cert);
        context.setRecipientCertificate(cert);
        context.setSigningKey(key);
        context.setSubmissionTime(new DateTime());
        context.setEvent("epSOS-31"); // TODO, change to setEventCode
        context.setMessageUUID(messageInspector.getMessageUUID());
        context.setAuthenticationMethod("3");
        context.setRequest(request); // here I pass the XML in order to give to
        // the developers the posisbility
        // to use their own implementation. Although an object is easier to get
        // the relevant types (e.g., action
        // environment
        context.setEnforcer(enforcePolicy);
//		context.setUsername("demo2");
//		context.setCurrentHost("127.0.0.1");
//		context.setRemoteHost("192.168.10.1");
        LinkedList<String> namesPostalAddress = new LinkedList<>();
        namesPostalAddress.add("Test");
        namesPostalAddress.add("Test2");
        
        context.setRecipientNamePostalAddress(namesPostalAddress);
        LinkedList<String> sendernamesPostalAddress = new LinkedList<>();
        sendernamesPostalAddress.add("SenderTest");
        sendernamesPostalAddress.add("SenderTest2");
        context.setSenderNamePostalAddress(sendernamesPostalAddress);
        ObligationHandlerFactory handlerFactory = ObligationHandlerFactory
                .getInstance();
        List<ObligationHandler> handlers = handlerFactory.createHandler(
                messageType, obligations, context);

        // Here I discharge manually. This behavior is to let free an
        // implementation
        // to still decide which handler to trigger
        System.out.println(handlers.get(0).getClass().getName());

        handlers.get(0).discharge();
	//	handlers.get(1).discharge();

        // Give me the ATNA, it's an ATNA test
        assertNotNull(handlers.get(0).getMessage());
        Utilities.serialize(handlers.get(0).getMessage().getDocumentElement());

        // I think I need to return handler.getMessage() which will be the audit
        // the audit will go to the server and get validated by another wrapper
        return handlers.get(0).getMessage();
    }

    
    private static Document readMessage(String file)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document incomingMsg = db.parse(new File(file));
        return incomingMsg;
    }

    private void checkCorrectnessofIHEXCA(final MessageType messageType) {
        assertTrue(messageType instanceof IHEXCARetrieve);

        IHEXCARetrieve xca = (IHEXCARetrieve) messageType;
        assertNotNull(xca.getDocumentUniqueId());
        assertNotNull(xca.getHomeCommunityID());
        assertNotNull(xca.getRepositoryUniqueId());

        assertEquals("urn:oid:2.16.840.1.113883.3.42.10001.100001.19",
                xca.getHomeCommunityID());
        assertEquals("2.16.840.1.113883.3.333.1", xca.getRepositoryUniqueId());
        assertEquals("4eb38f09-78da-43a8-a5b4-92b115c74add",
                xca.getDocumentUniqueId());
    }
}
