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
package eu.epsos.pt.cc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import eu.epsos.protocolterminators.integrationtest.common.AbstractIT;
import eu.epsos.validation.datamodel.common.EpsosService;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.reporting.ValidationReport;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.naming.NamingException;
import javax.xml.soap.SOAPElement;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.w3c.dom.Document;

/**
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public abstract class ClientGenericIT extends AbstractIT {

    private static final String NCP_B = "/epsos-client-connector/services/ClientConnectorService";
    protected static EpsosService currentService;

    @BeforeClass
    public static void setUpClass() throws NamingException {
        try {
            Properties portalProps;
            portalProps = new Properties();
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("portal.properties");
            portalProps.load(is);

            epr = portalProps.getProperty("ncpb.addr") + NCP_B;

            /* Add JDBC connection parameters to environment, instead of traditional JNDI */
            final SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
            final ComboPooledDataSource ds = new ComboPooledDataSource();
            try {
                ds.setDriverClass(portalProps.getProperty("db.driverclass"));
            } catch (PropertyVetoException ex) {
                LOG.error(ex.getLocalizedMessage(), ex);
            }
            ds.setJdbcUrl(portalProps.getProperty("db.jdbcurl"));
            ds.setUser(portalProps.getProperty("db.user"));
            ds.setPassword(portalProps.getProperty("db.password"));
            ds.setMaxPoolSize(1);
            ds.setMaxPoolSize(15);
            ds.setAcquireIncrement(3);
            ds.setMaxStatementsPerConnection(100);
            ds.setNumHelperThreads(20);
            builder.bind(portalProps.getProperty("db.resname"), ds);
            try {
                builder.activate();
            } catch (IllegalStateException ex) {
                LOG.error(ex.getLocalizedMessage(), ex);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param testName Test name for logging purposes
     * @param expected expected error code
     * @param request file with PRPA_IN201305UV02
     */
    protected void testFailScenario(String testName, String expected, String request) {
        ValidationReport.cleanValidationDir(NcpSide.NCP_B);
        try {
            callService(request);  // call

            LOG.info(fail(testName));                                   // preaty status print to tests list
            Assert.fail(testName + "Unexpected success result; missing error exception"); // fail the test

        } catch (SOAPFaultException ex) {
            if (expected.equals((ex.getMessage()))) {        // is expected exception error?
                LOG.info(success(testName));

            } else {
                LOG.info(fail(testName));
            }

            Assert.assertEquals(testName, expected, ex.getMessage());

        } catch (RuntimeException ex) {
            LOG.info(fail(testName));                                   // preaty status print to tests list
            Assert.fail(testName + ": " + ex.getMessage()); // fail the test
        }
        ValidationReport.write(NcpSide.NCP_B, true);
    }

    /**
     * This method will retrieve a ISO format patient id from a given
     * AdHocQueryRequest message.
     *
     * @param queryRequestPath the path for the message XML file
     * @return the ISO format Patient ID
     */
    protected static String getPatientIdIso(String queryRequestPath) {
        String root = "";
        String extension = "";
        Document requestDoc = readDoc(queryRequestPath);
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            extension = xPath.evaluate("//patientId/extension", requestDoc);
            root = xPath.evaluate("//patientId/root", requestDoc);
        } catch (XPathExpressionException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        return extension + "^^^&;" + root + "&;ISO";
    }

    @Override
    protected SOAPElement testGood(String testName, String request) {
        SOAPElement result;
        ValidationReport.cleanValidationDir(NcpSide.NCP_B);
        result = super.testGood(testName, request);
        ValidationReport.write(NcpSide.NCP_B, true);
        return result;
    }
}
