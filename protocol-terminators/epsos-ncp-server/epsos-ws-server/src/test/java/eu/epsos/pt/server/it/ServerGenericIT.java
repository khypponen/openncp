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
package eu.epsos.pt.server.it;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.epsos.protocolterminators.integrationtest.common.AbstractIT;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.reporting.ValidationReport;
import javax.xml.soap.SOAPElement;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.junit.Assert;
import org.w3c.dom.Document;

/**
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public abstract class ServerGenericIT extends AbstractIT {

    protected static final String CONF_MANAGER_FILE_PATH = System.getenv("EPSOS_PROPS_PATH") + "/integration/configmanager.hibernate.xml";
    /**
     * Aux instance of ConfigurationManagerService.
     */
    protected static final ConfigurationManagerService CONFIG_SERVICE = ConfigurationManagerService.getInstance(CONF_MANAGER_FILE_PATH);
    /**
     * Integration test fictional Country.
     */
    protected static final String COUNTRY_CODE = "zz";

    /**
     *
     * @param testName Test name for logging purposes
     * @param expected expected error code
     * @param request file with PRPA_IN201305UV02
     */
    protected void testFail(String testName, String expected, String request) {
        ValidationReport.cleanValidationDir(NcpSide.NCP_A);
        SOAPElement response;
        try {
            response = callService(request);
            Assert.assertNotNull(testName + ": response is not null", response);

        } catch (RuntimeException ex) {
            LOG.info(fail(testName));                                   // pretty status print to tests list
            Assert.fail(testName + ": " + ex.getLocalizedMessage());    // fail the test

            return;
        }

        String xml = soapElementToString(response);
        if (xml.contains(expected)) {
            LOG.info(success(testName));
        } else {
            LOG.info(fail(testName));
        }

        Assert.assertTrue(fail(testName), xml.contains(expected));
        ValidationReport.write(NcpSide.NCP_A, true);
    }

    /**
     * This method will retrieve a ISO format patient id from a given
     * AdHocQueryRequest message.
     *
     * @param queryRequestPath the path for the message XML file
     * @return the ISO format Patient ID
     */
    protected static String getPatientIdIso(String queryRequestPath) {
        String pid = "";
        Document requestDoc = readDoc(queryRequestPath);
        XPath xPath = XPathFactory.newInstance().newXPath();

        try {
            pid = xPath.evaluate("//*[local-name()='Slot' and @name='$XDSDocumentEntryPatientId'][1]/*[local-name()='ValueList']/*[local-name()='Value']", requestDoc);
        } catch (XPathExpressionException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }

        return pid.substring(1, pid.length() - 1);
    }

    @Override
    protected SOAPElement testGood(String testName, String request) {
        SOAPElement result;
        ValidationReport.cleanValidationDir(NcpSide.NCP_A);
        result = super.testGood(testName, request);
        ValidationReport.write(NcpSide.NCP_A, true);
        return result;
    }
}
