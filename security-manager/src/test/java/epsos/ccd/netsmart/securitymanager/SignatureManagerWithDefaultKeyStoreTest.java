/*
 *  Copyright 2010 jerouris.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package epsos.ccd.netsmart.securitymanager;

import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import epsos.ccd.netsmart.securitymanager.key.impl.NSTestKeyStoreManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author jerouris
 */
public class SignatureManagerWithDefaultKeyStoreTest {

	public SignatureManagerWithDefaultKeyStoreTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {

	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {

		XMLUnit.setNormalizeWhitespace(true);
		XMLUnit.setIgnoreWhitespace(true);
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of signXMLWithEnvelopedSig method, of class SignatureManager.
	 */
	@Ignore
	@Test
	public void testSignXMLWithEnvelopedSig() {
		try {

			System.out.println("signXMLWithEnvelopedSig");

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);

			File f = new File("mySignedFile.xml");
			// f.deleteOnExit();

			Document doc;
			doc = dbf
					.newDocumentBuilder()
					.parse(ClassLoader
							.getSystemResourceAsStream("ePsample_stripped.xml"));

			SignatureManager smgr = new SignatureManager();
			smgr.signXMLWithEnvelopedSig(doc);

			XMLUtils.sendXMLtoStream(doc, new FileOutputStream(f));

			smgr.verifyEnvelopedSignature(doc);

			// Document signedDoc;
			// signedDoc = dbf.newDocumentBuilder().parse(f);

			// smgr.verifyEnvelopedSignature(signedDoc);

		}

		catch (SMgrException ex) {
			Logger.getLogger(
					SignatureManagerWithDefaultKeyStoreTest.class.getName())
					.log(Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(
					SignatureManagerWithDefaultKeyStoreTest.class.getName())
					.log(Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (SAXException ex) {
			Logger.getLogger(
					SignatureManagerWithDefaultKeyStoreTest.class.getName())
					.log(Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (IOException ex) {
			Logger.getLogger(
					SignatureManagerWithDefaultKeyStoreTest.class.getName())
					.log(Level.SEVERE, null, ex);
			fail(ex.getMessage());
		}
	}

	/**
	 * Test of verifyEnvelopedSignature method, of class SignatureManager.
	 */
	@Ignore
	@Test
	public void testSuccessfulVerifyEnvelopedSignature() {
		try {
			System.out.println("verifyEnvelopedSignature");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document signedDoc;
			signedDoc = dbf
					.newDocumentBuilder()
					.parse(ClassLoader
							.getSystemResourceAsStream("signed_ePsample_UNK.xml"));
			SignatureManager instance = new SignatureManager(
					new NSTestKeyStoreManager());
			instance.verifyEnvelopedSignature(signedDoc);
		}

		catch (SMgrException ex) {
			Logger.getLogger(
					SignatureManagerWithDefaultKeyStoreTest.class.getName())
					.log(Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(
					SignatureManagerWithDefaultKeyStoreTest.class.getName())
					.log(Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (SAXException ex) {
			Logger.getLogger(
					SignatureManagerWithDefaultKeyStoreTest.class.getName())
					.log(Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (IOException ex) {
			Logger.getLogger(
					SignatureManagerWithDefaultKeyStoreTest.class.getName())
					.log(Level.SEVERE, null, ex);
			fail(ex.getMessage());
		}
	}

}