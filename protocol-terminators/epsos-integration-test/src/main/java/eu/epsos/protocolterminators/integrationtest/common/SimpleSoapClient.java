package eu.epsos.protocolterminators.integrationtest.common;

import eu.epsos.assertionvalidator.XSPARole;
import eu.epsos.util.IheConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.soap.SOAPFaultException;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.impl.AssertionMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tr.com.srdc.epsos.util.FileUtil;
import tr.com.srdc.epsos.util.XMLUtil;

/**
 * Test client that uses SAAJ (SOAP with Attachments API for Java) for calling web services. To be used for test
 * purposes only, for example integration-tests.
 *
 * @author gareth
 */
public class SimpleSoapClient {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleSoapClient.class);
    private static final String SECURITY_NS = IheConstants.SOAP_HEADERS.SECURITY_XSD;
    private static final String SECURITY_HEADER = "Security";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final String endpoint;

    /**
     * Instanciate the SimpleSoapClient
     *
     * @param endpoint URL for an NCP-A IHE Service Endpoint (eg.
     * http://localhost:8090/epsos-ws-server/services/XCA_Service/)
     */
    public SimpleSoapClient(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Construct a SOAP message and send to web service endpoint.
     *
     * @param document An XML document with the contents of the SOAP Body
     * @param assertions List of SAML2 assertions to be included in the SOAP Header
     * @return SOAPElement
     */
    public SOAPElement call(Document document, Collection<Assertion> assertions) throws SOAPFaultException {
        SOAPElement returnElement = null;
        try {
            // construct message
            SOAPMessage message = MessageFactory.newInstance().createMessage();

            // populate SOAP header
            SOAPHeader header = message.getSOAPHeader();
            SOAPElement security = header.addChildElement(SECURITY_HEADER, "wsse", SECURITY_NS);
            for (Assertion assertion : assertions) {
                AssertionMarshaller marshaller = new AssertionMarshaller();
                Element element = marshaller.marshall(assertion);
                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPElement assertionSOAP = soapFactory.createElement(element);
                security.addChildElement(assertionSOAP);
            }

            // populate SOAP body
            SOAPBody body = message.getSOAPBody();
            body.addDocument(document);
            LOG.debug("Request: " + LINE_SEPARATOR + getXmlFromSOAPMessage(message));

            // do SOAP request
            SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();
            SOAPMessage response = connection.call(message, endpoint);
            connection.close();

            // process response
            SOAPBody responseBody = response.getSOAPBody();
            LOG.debug("Response: " + LINE_SEPARATOR + getXmlFromSOAPMessage(response));
            SOAPBodyElement responseElement = (SOAPBodyElement) responseBody.getChildElements().next();
            returnElement = (SOAPElement) responseElement;

            /* If error present */
            if (responseBody.getFault() != null) {
            	LOG.info(LINE_SEPARATOR + getXmlFromSOAPMessage(response));
                throw new SOAPFaultException(responseBody.getFault());
            }

        } catch (SOAPException e) {
            LOG.info(e.getLocalizedMessage(), e);
        } catch (IOException e) {
            LOG.info(e.getLocalizedMessage(), e);
        } catch (MarshallingException e) {
            LOG.info(e.getLocalizedMessage(), e);
        }

        return returnElement;
    }

    /**
     * Main class for testing & development purposes
     *
     * @param args
     */
    public static void main(String[] args) {

        // load SOAP body content from file system
        String bodyStr = new ResourceLoader().getResource("/xca/AdhocQueryRequest.xml");
        System.out.println(bodyStr);
        Document doc = null;
        try {
            doc = XMLUtil.parseContent(bodyStr.getBytes(FileUtil.UTF_8));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // build list of SAML2 assertions
        Assertion idAssertion = new HCPIAssertionCreator().createHCPIAssertion(XSPARole.PHYSICIAN);
        Assertion trcAssertion = new TRCAssertionCreator().createTRCAssertion("","");
        Collection<Assertion> assertions = new ArrayList<Assertion>();
        assertions.add(idAssertion);
        assertions.add(trcAssertion);

        // send soap request
        SimpleSoapClient client = new SimpleSoapClient("http://localhost:8080/epsos-ws-server/services/XCA_Service/");
        SOAPElement response;
        try {
            response = client.call(doc, assertions);
            System.out.println("");
            System.out.println(response.getValue());
            
        } catch (SOAPFaultException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    /**
     * Convert any SOAP object that implements SOAPMessage into a String
     *
     * @param msg SOAP object
     * @return String
     * @throws SOAPException
     * @throws IOException
     */
    private static String getXmlFromSOAPMessage(SOAPMessage msg) throws SOAPException, IOException {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        msg.writeTo(byteArrayOS);
        return new String(byteArrayOS.toByteArray());
    }
}
