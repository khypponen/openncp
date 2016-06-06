/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.protocolterminators.integrationtest.common;

import eu.epsos.assertionvalidator.XSPARole;
import eu.epsos.protocolterminators.integrationtest.ihe.cda.CdaExtraction;
import eu.epsos.protocolterminators.integrationtest.ihe.cda.CdaModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.transform.TransformerException;
import javax.xml.ws.soap.SOAPFaultException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import tr.com.srdc.epsos.util.FileUtil;
import tr.com.srdc.epsos.util.XMLUtil;

/**
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public abstract class AbstractIT {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractIT.class);
    protected static String epr;
    protected Collection<Assertion> assertions;

    @AfterClass
    public static void tearDownClass() {
        LOG.info("");
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        LOG.info("----------------------------");
    }

    public static Document readDoc(String name) {
        Document doc = null;
        String bodyStr = new ResourceLoader().getResource(name); // load SOAP body
        Assert.assertTrue("Empty request body", bodyStr != null && bodyStr.isEmpty() == false);

        try {
            doc = XMLUtil.parseContent(bodyStr.getBytes(FileUtil.UTF_8));
        } catch (ParserConfigurationException ex) {
            Assert.fail(ex.getLocalizedMessage());
        } catch (SAXException ex) {
            Assert.fail(ex.getLocalizedMessage());
        } catch (IOException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }

        return doc;
    }

    protected static String extractErrorElem(SOAPElement response) {
        SOAPElement ack = (SOAPElement) response.getChildElements(new QName(response.getNamespaceURI(), "acknowledgement")).next();

        try {
            SOAPElement msg = (SOAPElement) ack.getChildElements(new QName(ack.getNamespaceURI(), "acknowledgementDetail")).next();
            SOAPElement txt = (SOAPElement) msg.getChildElements(new QName(msg.getNamespaceURI(), "text")).next();

            return txt.getTextContent();

        } catch (NoSuchElementException ex) {
            return null;
        }
    }

    protected static String success(String testName) {
        return "\u2714 " + testName;
    }

    protected static String fail(String testName) {
        return "\u2718 " + testName;
    }

    protected static SOAPElement extractElem(SOAPElement response, String[] path) {
        try {
            QName nextElem = new QName(response.getNamespaceURI(), path[0]);
            SOAPElement auxElem = (SOAPElement) response.getChildElements(nextElem).next();

            for (int i = 1; i < path.length; i++) {
                nextElem = new QName(response.getNamespaceURI(), path[i]);
                auxElem = (SOAPElement) auxElem.getChildElements(nextElem).next();
            }

            return auxElem;

        } catch (NoSuchElementException ex) {
            return null;
        }
    }

    protected String soapElementToString(SOAPElement element) {
        String str = null;
        try {
            str = XMLUtil.prettyPrint(element);
        } catch (Exception ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
        return str;
    }

    protected static Collection<Assertion> hcpAssertionCreate(XSPARole role) {
        Collection<Assertion> assertions = new ArrayList<Assertion>(1);
        assertions.add(HCPIAssertionCreator.createHCPIAssertion(role));

        return assertions;
    }

    protected static Collection<Assertion> hcpAndTrcAssertionCreate(XSPARole role) {
        Collection<Assertion> assertions = new ArrayList<Assertion>(2);

        Assertion hcpAssertion = HCPIAssertionCreator.createHCPIAssertion(role);
        Assertion trcAssertion = TRCAssertionCreator.createTRCAssertion("2.16.17.710.804.1000.990.1", "6212122451", hcpAssertion.getID());

        assertions.add(hcpAssertion);
        assertions.add(trcAssertion);

        return assertions;
    }

    protected static Collection<Assertion> hcpAssertionCreate(List<String> permissions, XSPARole role) {
        Collection<Assertion> assertions = new ArrayList<Assertion>(1);
        assertions.add(HCPIAssertionCreator.createHCPIAssertion(permissions, role));

        return assertions;
    }

    protected static Collection<Assertion> hcpAndTrcAssertionCreate(String patientIdIso, XSPARole role) {
        Collection<Assertion> assertions = new ArrayList<Assertion>(2);

        Assertion hcpAssertion = HCPIAssertionCreator.createHCPIAssertion(role);
        Assertion trcAssertion;

        if (patientIdIso == null && patientIdIso.isEmpty()) {
            trcAssertion = TRCAssertionCreator.createTRCAssertion("2.16.17.710.804.1000.990.1", "6212122451", hcpAssertion.getID());
        } else {
            trcAssertion = TRCAssertionCreator.createTRCAssertion(patientIdIso, hcpAssertion.getID());
        }

        assertions.add(hcpAssertion);
        assertions.add(trcAssertion);

        return assertions;
    }

    protected static Collection<Assertion> hcpAndTrcAssertionCreate(String patientIdIso, List<String> permissions, XSPARole role) {
        Collection<Assertion> assertions = new ArrayList<Assertion>(2);

        Assertion hcpAssertion = HCPIAssertionCreator.createHCPIAssertion(permissions, role);
        Assertion trcAssertion;


        if (patientIdIso == null || patientIdIso.isEmpty()) {
            trcAssertion = TRCAssertionCreator.createTRCAssertion("2.16.17.710.804.1000.990.1", "6212122451", hcpAssertion.getID());
        } else {
            trcAssertion = TRCAssertionCreator.createTRCAssertion(patientIdIso, hcpAssertion.getID());
        }

        assertions.add(hcpAssertion);
        assertions.add(trcAssertion);

        return assertions;
    }

    protected static void validateCDA(String reqFilePath, CdaExtraction.MessageType msgType, CdaModel model) {

        //Extract document
        String base64Doc = CdaExtraction.extract(msgType, reqFilePath);
        invokeIheCdaService(base64Doc, msgType, model);

    }

    protected static void validateCDA(SOAPElement soapBody, CdaExtraction.MessageType msgType, CdaModel model) {
        String base64Doc = null;
        SOAPBodyElement body = (SOAPBodyElement) soapBody;
        try {
            base64Doc = CdaExtraction.extract(msgType, XMLUtil.parseContent(XMLUtil.prettyPrint(body.getFirstChild()).getBytes(FileUtil.UTF_8)));
        } catch (ParserConfigurationException ex) {
            LOG.error("An error has occurred during CDA Validation.", ex);
        } catch (SAXException ex) {
            LOG.error("An error has occurred during CDA Validation.", ex);
        } catch (IOException ex) {
            LOG.error("An error has occurred during CDA Validation.", ex);
        } catch (TransformerException ex) {
            LOG.error("An error has occurred during CDA Validation.", ex);
        }
        invokeIheCdaService(base64Doc, msgType, model);

    }

    private static void invokeIheCdaService(String base64Doc, CdaExtraction.MessageType msgType, CdaModel model) {

//        try {
//
//            ModelBasedValidationWSService service = new ModelBasedValidationWSService();
//            ModelBasedValidationWS port = service.getModelBasedValidationWSPort();
//
//            // Validate document against IHE services
//            DetailedResult detailedResult = CdaExtraction.unmarshalDetails(port.validateBase64Document(base64Doc, model.getName()));
//            LOG.info("\u21b3Document validation result");
//            LOG.info(" \u251c\u2500Service Name: " + detailedResult.getValResultsOverview().getValidationServiceName().split(" : ")[1]);
//            LOG.info(" \u251c\u2500XSD: " + detailedResult.getDocumentValidXsd().getResult());
//            LOG.info(" \u251c\u2500Document Well Formed: " + detailedResult.getDocumentWellFormed().getResult());
//            LOG.info(" \u251c\u2500Number of Errors: " + detailedResult.getMdaValidation().getNotes().size());
//            LOG.info(" \u2514\u2500Overall Result: " + detailedResult.getValResultsOverview().getValidationTestResult());
//        } catch (SOAPException_Exception ex) {
//            LOG.error("An error has occurred during CDA Validation.", ex);
//        }
    }
    /*
     * Instance methods
     */

    protected abstract Collection<Assertion> getAssertions(String requestPath, XSPARole role);

    protected SOAPElement testGood(String testName, String request) {
        SOAPElement result = null;

        try {
            result = callService(request);  // call

        } catch (RuntimeException ex) {
            LOG.info(fail(testName));// preaty status print to tests list
            LOG.info(ex.getMessage(), ex);//must have stack trace for troubleshooting failed integration tests
            Assert.fail(testName + ": " + ex.getMessage());             // fail the test
        }

        LOG.info(success(testName)); // pretty status print to tests list
        return result;
    }

    protected SOAPElement callService(String request) throws SOAPFaultException {
        Document doc = readDoc(request);                            // read soap request
        SimpleSoapClient client = new SimpleSoapClient(epr);        // SOAP client
        return client.call(doc, assertions);                    // Call service

    }
}
