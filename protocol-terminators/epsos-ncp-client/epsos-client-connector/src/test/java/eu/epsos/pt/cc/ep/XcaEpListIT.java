/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012  SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
package eu.epsos.pt.cc.ep;

import eu.epsos.assertionvalidator.XSPARole;
import eu.epsos.pt.cc.ClientGenericIT;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.naming.NamingException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opensaml.saml2.core.Assertion;

/**
 * Implements all the Integration Test for the XCA List operation for Order
 * Service.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class XcaEpListIT extends ClientGenericIT {

    /**
     * Folder under test/resources were are soap requests for this tests.
     */
    private static final String REQ_FOLDER = "xca/orderservice/";

    @BeforeClass
    public static void setUpClass() throws NamingException {
        ClientGenericIT.setUpClass();
        LOG.info("----------------------------");
        LOG.info(" Query EP Documents");
        LOG.info("----------------------------");
    }

    /*
     * Normal usage
     */
    /**
     * This test performs an XCA Query for EP, using valid Patient Identifiers.
     * It is a simple test designed uniquely for testing the normal work-flow of
     * the XCA query.
     */
    @Test
    public void testQueryEP() {
        List<String> permissions = new ArrayList<String>(2);
        permissions.add("4");
        permissions.add("10");
        assertions = getAssertions(permissions, REQ_FOLDER + "PT_CLIENT_XCA_EP_#0.xml", XSPARole.PHARMACIST);

        testGood("PT_CLIENT_XCA_EP_#0", REQ_FOLDER + "PT_CLIENT_XCA_EP_#0.xml");
    }

    /*
     * Invalid scenarios
     * See D3.4.2 | 3.4.1.5
     */
    /**
     * (ERROR)
     *
     * The patient has not given consent to the requested service.
     *
     * Response Status: Failure, Message: No Consent, Code: 4701
     */
    @Test
    @Ignore
    public void testQueryEPNoConsent() {
        testFailScenario("PT_CLIENT_XCA_EP_#4", "4701", REQ_FOLDER + "PT_CLIENT_XCA_EP_#4.xml");
    }

    /**
     * (ERROR)
     *
     * Country A requests a higher authentication trust level than assigned to
     * the HCP (e.g. password-based login not accepted for the requested
     * operation).
     *
     * Response Status: Failure, Message: Weak, Authentication Code: 4702
     */
    @Test
    @Ignore
    public void testQueryEPLowAuth() {
        testFailScenario("PT_CLIENT_XCA_EP_#2", "4702", REQ_FOLDER + "PT_CLIENT_XCA_EP_#2.xml");
    }

    /**
     * (ERROR)
     *
     * Either the security policy of country A or a privacy policy of the
     * patient (that was given in country A) does not allow the requested
     * operation to be performed by the HCP.
     *
     * Response Status: Failure, Message: Insufficient Rights, Code: 4703
     */
    @Test
    public void testQueryEPInsRights() {
        List<String> permissions = new ArrayList<String>(1);
        permissions.add("4");
        assertions = getAssertions(permissions, REQ_FOLDER + "PT_CLIENT_XCA_EP_#3.xml", XSPARole.PHARMACIST);
        testFailScenario("PT_CLIENT_XCA_EP_#3", "4703", REQ_FOLDER + "PT_CLIENT_XCA_EP_#3.xml");
    }

    /**
     * (INFO)
     *
     * There is no ePrescription data registered for the given patient.
     *
     * Response Status: Success, Message: No Data, Code: 1101
     */
    @Test
    public void testQueryNoEP() {
        List<String> permissions = new ArrayList<String>(2);
        permissions.add("4");
        permissions.add("10");
        assertions = getAssertions(permissions, REQ_FOLDER + "PT_CLIENT_XCA_EP_#4.xml", XSPARole.PHARMACIST);
        testFailScenario("PT_CLIENT_XCA_EP_#4", "1101", REQ_FOLDER + "PT_CLIENT_XCA_EP_#4.xml");
    }

    /**
     * (ERROR)
     *
     * The requested encoding cannot be provided due to a transcoding error.
     *
     * Response Status: Failure, Message: Transcoding Error, Code: 4203
     */
    @Test
    @Ignore
    public void testQueryEPTranscErr() {
        testFailScenario("PT_CLIENT_XCA_OS_#5", "4203", REQ_FOLDER + "PT_CLIENT_XCA_OS_#5.xml");
    }

    /**
     * (ERROR)
     *
     * The ePrescription registry is not accessible.
     *
     * Response Status: Failure, Message: Registry Failure, Code: 4103
     */
    @Test
    @Ignore
    public void testQueryEPRegNotAccs() {
        testFailScenario("PT_CLIENT_XCA_EP_#6", "4103", REQ_FOLDER + "PT_CLIENT_XCA_EP_#6.xml");
    }

    /**
     * (ERROR)
     *
     * There is ePrescription data registered for the patient but the service
     * provider is unable to access it.
     *
     * Response Status: Failure, Message: Data Accesss Failure, Code: 4104
     */
    @Test
    @Ignore
    public void testQueryEPNotAccs() {
        testFailScenario("PT_CLIENT_XCA_EP_#7", "4104", REQ_FOLDER + "PT_CLIENT_XCA_EP_#7.xml");
    }

    /**
     * (ERROR).
     *
     * The service provider is unable to evaluate the given argument.
     *
     * Response Status: Failure, Message: Unknown Filters, Code: 4204
     */
    @Test
    @Ignore
    public void testQueryEPInvArgs() {
        testFailScenario("PT_CLIENT_XCA_EP_#8", "4204", REQ_FOLDER + "PT_CLIENT_XCA_EP_#8.xml");
    }
    /*
     * Auxiliar methods
     */

    /**
     * Not all of the requested encodings are provided (e.g. due to inability to
     * transcode a certain national code).
     */
    @Test
    @Ignore
    public void testQueryPSPartTrnscSucc() {
        testFailScenario("PT_CLIENT_XCA_EP_#5.1", "4101", REQ_FOLDER + "PT_CLIENT_XCA_EP_#5.1.xml");
    }

    /**
     * The HCP MUST consider additionally the source coded document because it
     * MAY contain information that is not included in the epSOS pivot CDA .
     * Reason: field were nullified due to missing code mappings!
     */
    @Test
    @Ignore
    public void testQueryPSConsiderAddDoc() {
        testFailScenario("PT_CLIENT_XCA_EP_#5.2", "2102", REQ_FOLDER + "PT_CLIENT_XCA_EP_#5.2.xml");
    }

    @Override
    protected Collection<Assertion> getAssertions(String requestPath, XSPARole role) {
        return hcpAndTrcAssertionCreate(getPatientIdIso(requestPath), role);
    }

    protected Collection<Assertion> getAssertions(List<String> permissions, String requestPath, XSPARole role) {
        return hcpAndTrcAssertionCreate(getPatientIdIso(requestPath), permissions, role);
    }
}
