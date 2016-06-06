package eu.epsos.validation.esense;

import eu.epsos.util.EvidenceUtils;
import eu.esens.abb.nonrep.EnforcePolicyException;
import eu.esens.abb.nonrep.MalformedIHESOAPException;
import eu.esens.abb.nonrep.MalformedMIMEMessageException;
import eu.esens.abb.nonrep.ObligationDischargeException;
import eu.esens.abb.nonrep.TOElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import org.apache.commons.io.FileUtils;
import org.herasaf.xacml.core.SyntaxException;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import tr.com.srdc.epsos.util.DateUtil;
import tr.com.srdc.epsos.util.XMLUtil;

/**
 *
 * @author karkaletsis
 */
public class EvidenceEmitterTest {

    private static X509Certificate cert;
    private static PrivateKey key;

    public EvidenceEmitterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {

        ClassLoader loader;
        loader = EvidenceEmitterTest.class.getClassLoader();
        InputStream inputStream = loader.getResourceAsStream("testData//sample.keystore");
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(inputStream,
                "spirit".toCharArray());
        cert = (X509Certificate) ks.getCertificate("server1");
        key = (PrivateKey) ks.getKey("server1", "spirit".toCharArray());
        org.apache.xml.security.Init.init();

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGregorianCalendarToJoda() throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        DateTime dt = DateUtil.GregorianCalendarToJodaTime(date2);
    }

    @Test
    public void testCreateEvidenceREMNRR() throws MalformedMIMEMessageException, MalformedIHESOAPException, SOAPException, ParserConfigurationException, SAXException, IOException, URISyntaxException, TOElementException, EnforcePolicyException, ObligationDischargeException, TransformerException, SyntaxException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        ClassLoader loader;
        loader = EvidenceEmitterTest.class.getClassLoader();
        InputStream inputStream = loader.getResourceAsStream("testData//incomingMsg.xml");
        InputStream keyStore = loader.getResourceAsStream("testData///sample.keystore");
        File keyStoreFile = File.createTempFile("sample", ".keystore");
        FileUtils.copyInputStreamToFile(keyStore, keyStoreFile);
        org.w3c.dom.Document incomingMsg = XMLUtil.newDocumentFromInputStream(inputStream);
        String a1 = XMLUtil.DocumentToString(incomingMsg);
        System.out.println(a1);
        //org.w3c.dom.Document incomingMsg = XMLUtil.newDocumentFromInputStream(inputStream);
        EvidenceUtils.createEvidenceREMNRR(
                a1,
                keyStoreFile.getAbsolutePath(),
                "spirit",
                "server1",
                "epsos-11",
                new DateTime(),
                "1",
                "testNRR");
        keyStoreFile.delete();
    }

    @Test
    public void testCreateEvidenceREMNRO() throws MalformedMIMEMessageException, MalformedIHESOAPException, SOAPException, ParserConfigurationException, SAXException, IOException, URISyntaxException, TOElementException, EnforcePolicyException, ObligationDischargeException, TransformerException, SyntaxException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        ClassLoader loader;
        loader = EvidenceEmitterTest.class.getClassLoader();
        InputStream inputStream = loader.getResourceAsStream("testData//sample.xml");
        org.w3c.dom.Document incomingMsg = XMLUtil.newDocumentFromInputStream(inputStream);
        String a1 = XMLUtil.DocumentToString(incomingMsg);
        InputStream keyStore = loader.getResourceAsStream("testData///sample.keystore");
        File keyStoreFile = File.createTempFile("sample", ".keystore");
        FileUtils.copyInputStreamToFile(keyStore, keyStoreFile);
        System.out.println(a1);
        //org.w3c.dom.Document incomingMsg = XMLUtil.newDocumentFromInputStream(inputStream);
        EvidenceUtils.createEvidenceREMNRO(
                a1,
                keyStoreFile.getAbsolutePath(),
                "spirit",
                "server1",
                "epsos-11",
                new DateTime(),
                "1", "testNRO", "111111222222");
        keyStoreFile.delete();
    }

}
