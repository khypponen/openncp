/*
 *  Copyright 2010 Jerry Dimitriou <jerouris at netsmart.gr>.
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

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObject;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
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

import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;

/**
 *
 * @author Jerry Dimitriou <jerouris at netsmart.gr>
 */
public class SamlTRCIssuerTest {

    private static final Logger logger = LoggerFactory.getLogger(SamlTRCIssuerTest.class);

    public SamlTRCIssuerTest() {
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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of issueTrcToken method, of class SamlTRCIssuer.
     *
     * @throws java.io.IOException
     */
    @Ignore
    @Test
    public void testIssueTrcToken() throws IOException {
        try {

            System.out.println("issueTrcToken");

            // Get parser pool manager
            BasicParserPool ppMgr = new BasicParserPool();
            ppMgr.setNamespaceAware(true);
            // Parse metadata file
            InputStream in = ClassLoader.getSystemResourceAsStream("SignedSamlAssertion.xml");
            //InputStream in = ClassLoader.getSystemResourceAsStream("SAMLSignedIdentityAssertion.xml");
            Document samlas = ppMgr.parse(in);
            Element samlasRoot = samlas.getDocumentElement();
            // Get apropriate unmarshaller
            UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
            Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(samlasRoot);
            // Unmarshall using the document root element, an EntitiesDescriptor in this case
            Assertion hcpIdentityAssertion = (Assertion) unmarshaller.unmarshall(samlasRoot);
            logger.info("Name Id Value:{0}", hcpIdentityAssertion.getSubject().getNameID().getValue());
            String patientID = "theID";
            //List<String> purposeOfUse = Collections.singletonList("TREATMENT");
            List<Attribute> attrValuePair = null;
            SamlTRCIssuer instance = new SamlTRCIssuer();
            //Assertion expResult = null;

            Assertion result = instance.issueTrcToken(hcpIdentityAssertion, patientID, null, attrValuePair);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document signedDoc = dbf.newDocumentBuilder().newDocument();
            Configuration.getMarshallerFactory().getMarshaller(result).marshall(result, signedDoc);

            XMLUtils.sendXMLtoStream(signedDoc, new FileOutputStream("trc.xml"));

        } catch (FileNotFoundException ex) {
            logger.error(null, ex);
        } catch (MarshallingException ex) {
            logger.error(null, ex);
        } catch (UnmarshallingException ex) {
            fail(ex.getMessage());
        } catch (XMLParserException ex) {
            fail(ex.getMessage());
        } catch (ParserConfigurationException ex) {
            logger.error(null, ex);
            fail(ex.getMessage());
        } catch (SMgrException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test of verifyTrcToken method, of class SamlTRCIssuer.
     *
     * @throws java.lang.Exception
     */
    @Ignore
    @Test
    public void testVerifyTrcToken() throws Exception {
        System.out.println("verifyTrcToken");

        // Get parser pool manager
        Assertion idas = loadSamlAssertionAsResource("SignedSamlAssertion.xml");
        Assertion trc = null;
        String patientID = "theID";
        //List<String> purposeOfUse = Collections.singletonList("TREATMENT");
        List<Attribute> attrValuePair = null;
        SamlTRCIssuer instance = new SamlTRCIssuer();
        SignatureManager sm = new SignatureManager();

        try {

            sm.verifySAMLAssestion(idas);
        } catch (SMgrException e) {
            fail("IdAssert SigVal: " + e.getMessage());
        }

        try {

            sm.verifySAMLAssestion(idas);
        } catch (SMgrException e) {
            fail("IdAssert2 SigVal: " + e.getMessage());
        }

        writeSAMLObjectToStream(idas, "assertion_before_verification.xml");
        try {

            trc = instance.issueTrcToken(idas, patientID, null, attrValuePair);
        } catch (SMgrException e) {
            fail("TRC Issue: " + e.getMessage());
        }

        writeSAMLObjectToStream(idas, "assertion_after_verification.xml");

        idas = loadSamlAssertionAsResource("SignedSamlAssertion.xml");
        try {
            instance.verifyTrcToken(trc, idas, patientID);

        } catch (SMgrException e) {
            fail("verifyToken: " + e.getMessage());
        }
        try {

            sm.verifySAMLAssestion(idas);
        } catch (SMgrException e) {
            fail("IdAssert SigVal: " + e.getMessage());
        }

        try {

            sm.verifySAMLAssestion(trc);
        } catch (SMgrException e) {
            fail("TRC SigVal: " + e.getMessage());
        }

    }

    private Assertion loadSamlAssertionAsResource(String filename) {
        Assertion hcpIdentityAssertion = null;
        try {
            BasicParserPool ppMgr = new BasicParserPool();
            ppMgr.setNamespaceAware(true);
            // Parse metadata file
            InputStream in = ClassLoader.getSystemResourceAsStream(filename);
            Document samlas = ppMgr.parse(in);
            Element samlasRoot = samlas.getDocumentElement();
            // Get apropriate unmarshaller
            UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
            Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(samlasRoot);
            // Unmarshall using the document root element, an EntitiesDescriptor in this case
            hcpIdentityAssertion = (Assertion) unmarshaller.unmarshall(samlasRoot);
        } catch (UnmarshallingException ex) {
            logger.error(null, ex);
        } catch (XMLParserException ex) {
            logger.error(null, ex);
        }

        return hcpIdentityAssertion;

    }

    private void writeSAMLObjectToStream(SAMLObject so, String f) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document signedDoc = dbf.newDocumentBuilder().newDocument();
            Configuration.getMarshallerFactory().getMarshaller(so).marshall(so, signedDoc);
            XMLUtils.sendXMLtoStream(signedDoc, new FileOutputStream(f));
        } catch (FileNotFoundException ex) {
            logger.error(null, ex);
        } catch (MarshallingException ex) {
            logger.error(null, ex);
        } catch (ParserConfigurationException ex) {
            logger.error(null, ex);
        }
    }
}
