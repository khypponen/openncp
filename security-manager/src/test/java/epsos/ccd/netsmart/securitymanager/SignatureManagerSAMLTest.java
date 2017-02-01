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
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.*;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.fail;

/**
 * @author jerouris
 */
public class SignatureManagerSAMLTest {

    private static final Logger logger = LoggerFactory.getLogger(SignatureManagerSAMLTest.class);

    public SignatureManagerSAMLTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        DefaultBootstrap.bootstrap();
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
     * Test of signSAMLAssertion method, of class SignatureManager.
     *
     * @throws java.io.IOException
     */
    @Ignore
    @Test
    public void testSignAndVerifySAMLAssertion() throws IOException {
        try {
            System.out.println("signSAMLAssertion");

            // Get parser pool manager
            BasicParserPool ppMgr = new BasicParserPool();
            ppMgr.setNamespaceAware(true);
            // Parse metadata file
            InputStream in = ClassLoader.getSystemResourceAsStream("SAMLIdAssertion.xml");
            Document samlas = ppMgr.parse(in);
            Element samlasRoot = samlas.getDocumentElement();
            // Get apropriate unmarshaller
            UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
            Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(samlasRoot);
            // Unmarshall using the document root element, an EntitiesDescriptor in this case
            Assertion as = (Assertion) unmarshaller.unmarshall(samlasRoot);

            SignatureManager instance = new SignatureManager();

            instance.signSAMLAssertion(as);
            //MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();
            // Get the Subject marshaller
            // Marshall the Subject

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document signedDoc = dbf.newDocumentBuilder().newDocument();
            Configuration.getMarshallerFactory().getMarshaller(as).marshall(as, signedDoc);
            try {
                XMLUtils.sendXMLtoStream(signedDoc, new FileOutputStream("SignedSamlAssertion.xml"));
            } catch (FileNotFoundException ex) {
                logger.error(null, ex);
            }

            //Verify the Signed SAML Assertion
            instance.verifySAMLAssestion(as);

            try {

                // It can also be verified using the DOM
                instance.verifyEnvelopedSignature(signedDoc);
            } catch (SMgrException e) {
                fail("test fail: " + e.getMessage());
            }

            Unmarshaller unmarshaller2 = unmarshallerFactory.getUnmarshaller(signedDoc.getDocumentElement());
            Assertion as2 = (Assertion) unmarshaller2.unmarshall(signedDoc.getDocumentElement());

            try {
                instance.verifySAMLAssestion(as2);
            } catch (SMgrException ex) {
                fail(ex.getMessage());

            }

        } catch (XMLParserException ex) {
            logger.error(null, ex);
            fail("XmL Parser:" + ex.getMessage());
        } catch (ParserConfigurationException ex) {
            logger.error(null, ex);
            fail("Parse Conf:" + ex.getMessage());
        } catch (MarshallingException ex) {
            logger.error(null, ex);
            fail("Marshalling:" + ex.getMessage());
        } catch (SMgrException ex) {
            logger.error(null, ex);
            fail("SMGR :" + ex.getMessage());
        } catch (UnmarshallingException ex) {
            logger.error(null, ex);
            fail("Unmarshalling:" + ex.getMessage());
        }
    }
}
