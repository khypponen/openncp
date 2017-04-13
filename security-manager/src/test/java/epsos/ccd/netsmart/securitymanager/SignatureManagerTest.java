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
import epsos.ccd.netsmart.securitymanager.key.impl.SPMSTestKeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.TianiTestKeyStoreManager;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.fail;

/**
 * @author jerouris
 */
public class SignatureManagerTest {

    private static final Logger logger = LoggerFactory.getLogger(SignatureManagerTest.class);

    public SignatureManagerTest() {
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
            String keyAlias = "testncp";
            String keyPassword = "epsos123";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            File f = new File("mySignedFile.xml");
            //f.deleteOnExit();

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

    @Ignore
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

    @Test
    public void testSignXMLWithEnvelopedSigSPMS() {
        try {

            System.out.println("signXMLWithEnvelopedSigSPMS");
            String keyAlias = "ppt.ncp-signature.epsos.spms.pt";
            String keyPassword = "changeit";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            Document doc = dbf.newDocumentBuilder().parse(ClassLoader.getSystemResourceAsStream("ePsample_stripped.xml"));

            SignatureManager smgr = new SignatureManager(new SPMSTestKeyStoreManager());
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
    @Ignore
    @Test
    public void testSuccessfulVerifyEnvelopedSignature() {
        try {
            System.out.println("verifyEnvelopedSignature");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document signedDoc;
            signedDoc = dbf.newDocumentBuilder().parse(ClassLoader.getSystemResourceAsStream("signed_ePsample_UNK.xml"));
            SignatureManager instance = new SignatureManager(new NSTestKeyStoreManager());
            instance.verifyEnvelopedSignature(signedDoc);
        } catch (SMgrException | ParserConfigurationException | SAXException | IOException ex) {
            logger.error(null, ex);
            fail(ex.getMessage());
        }
    }

    @Ignore
    @Test
    public void testFailedVerifyEnvelopedSignatureTiani() {
        try {
            System.out.println("failedVerifyEnvelopedSignature");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document signedDoc;
            signedDoc = dbf.newDocumentBuilder().parse(ClassLoader.getSystemResourceAsStream("signed_ePsample_UNK.xml"));

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

    @Ignore
    @Test
    public void testFailedVerifyEnvelopedSignatureSPMS() {
        try {
            System.out.println("failedVerifyEnvelopedSignatureSPMS");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document signedDoc;
            signedDoc = dbf.newDocumentBuilder().parse(ClassLoader.getSystemResourceAsStream("signed_ePsample_UNK.xml"));

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
}
