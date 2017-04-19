package epsos.ccd.netsmart.securitymanager;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import epsos.ccd.netsmart.securitymanager.key.impl.NSTestKeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.SPMSTestKeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.TianiTestKeyStoreManager;
import junit.framework.TestCase;

/**
 * All tests use hibernate(util) and seems to require a database containing some
 * properties for the tests to execute properly. Therefore these are integration
 * tests. A constructor for the original class is provided where external
 * resouces can be provided, thus the class should be more easy to test. <br/>
 * <br/>
 * FIXME: Move these tests back to 'non integration' test class, with the fix
 * that the parameters required from the database are provided from the tests.
 * 
 * @author fivilmyrs
 */
@RunWith(JUnit4.class)
public class SignatureManagerIntegrationTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(SignatureManagerTest.class);

	@Before
	public void setUp() {
		XMLUnit.setNormalizeWhitespace(true);
		XMLUnit.setIgnoreWhitespace(true);
	}

	/**
	 * Test of signXMLWithEnvelopedSig method, of class SignatureManager.
	 */
	@Test
	public void testSignXMLWithEnvelopedSig() {
		try {

			System.out.println("signXMLWithEnvelopedSig");
			String keyAlias = "testncp";
			String keyPassword = "epsos123";

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);

			File f = new File("mySignedFile.xml");
			// f.deleteOnExit();

			Document doc;
			doc = dbf.newDocumentBuilder().parse(ClassLoader.getSystemResourceAsStream("ePsample_stripped.xml"));

			SignatureManager smgr = new SignatureManager(new NSTestKeyStoreManager());
			smgr.signXMLWithEnvelopedSig(doc, keyAlias, keyPassword.toCharArray());

			XMLUtils.sendXMLtoStream(doc, new FileOutputStream(f));

			smgr.verifyEnvelopedSignature(doc);

			Document signedDoc;
			signedDoc = dbf.newDocumentBuilder().parse(f);

			smgr.verifyEnvelopedSignature(signedDoc);

		} catch (SMgrException | ParserConfigurationException | SAXException | IOException ex) {
			logger.error(null, ex);
			fail(ex.getMessage());
		}
	}

	@Test
	public void testSignXMLWithEnvelopedSigTiani() {
		try {

			System.out.println("signXMLWithEnvelopedSigTiani");
			String keyAlias = "server1";
			String keyPassword = "spirit";

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);

			Document doc;
			doc = dbf.newDocumentBuilder().parse(ClassLoader.getSystemResourceAsStream("ePsample_stripped.xml"));

			SignatureManager smgr = new SignatureManager(new TianiTestKeyStoreManager());
			smgr.signXMLWithEnvelopedSig(doc, keyAlias, keyPassword.toCharArray());

			smgr.verifyEnvelopedSignature(doc);

		} catch (SMgrException | ParserConfigurationException | SAXException | IOException ex) {
			logger.error(null, ex);
			fail(ex.getMessage());
		}
	}

	/**
	 * Test of verifyEnvelopedSignature method, of class SignatureManager.
	 */
	@Test
	public void testSuccessfulVerifyEnvelopedSignature() {
		try {
			System.out.println("verifyEnvelopedSignature");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document signedDoc;
			signedDoc = dbf.newDocumentBuilder()
					.parse(ClassLoader.getSystemResourceAsStream("signed_ePsample_UNK.xml"));
			SignatureManager instance = new SignatureManager(new NSTestKeyStoreManager());
			instance.verifyEnvelopedSignature(signedDoc);
		} catch (SMgrException | ParserConfigurationException | SAXException | IOException ex) {
			logger.error(null, ex);
			fail(ex.getMessage());
		}
	}

	@Test
	public void testFailedVerifyEnvelopedSignatureTiani() {
		try {
			System.out.println("failedVerifyEnvelopedSignature");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document signedDoc;
			signedDoc = dbf.newDocumentBuilder()
					.parse(ClassLoader.getSystemResourceAsStream("signed_ePsample_UNK.xml"));

			// Provide a wrong keystore. This should make the signature invalid
			SignatureManager instance = new SignatureManager(new TianiTestKeyStoreManager());
			instance.verifyEnvelopedSignature(signedDoc);
		} catch (SMgrException ex) {
			logger.error(null, ex);

		} catch (ParserConfigurationException | SAXException | IOException ex) {
			logger.error(null, ex);
			fail(ex.getMessage());
		}
	}

	@Test
	public void testFailedVerifyEnvelopedSignatureSPMS() {
		try {
			System.out.println("failedVerifyEnvelopedSignatureSPMS");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document signedDoc;
			signedDoc = dbf.newDocumentBuilder()
					.parse(ClassLoader.getSystemResourceAsStream("signed_ePsample_UNK.xml"));

			// Provide a wrong keystore. This should make the signature invalid
			SignatureManager instance = new SignatureManager(new SPMSTestKeyStoreManager());
			instance.verifyEnvelopedSignature(signedDoc);
		} catch (SMgrException ex) {
			logger.error(null, ex);

		} catch (ParserConfigurationException | SAXException | IOException ex) {
			logger.error(null, ex);
			fail(ex.getMessage());
		}
	}

	@Test
	public void testSignXMLWithEnvelopedSigSPMS() {

		try {

			System.out.println("signXMLWithEnvelopedSigSPMS");
			String keyAlias = "ppt.ncp-signature.epsos.spms.pt";
			String keyPassword = "changeit";

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);

			Document doc = dbf.newDocumentBuilder()
					.parse(ClassLoader.getSystemResourceAsStream("ePsample_stripped.xml"));

			SignatureManager smgr = new SignatureManager(new SPMSTestKeyStoreManager());
			smgr.signXMLWithEnvelopedSig(doc, keyAlias, keyPassword.toCharArray());

			smgr.verifyEnvelopedSignature(doc);

		} catch (SMgrException | ParserConfigurationException | SAXException | IOException ex) {
			logger.error(null, ex);
			fail(ex.getMessage());
		}
	}
}
