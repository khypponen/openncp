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
package eu.epsos.pt.cc.cs;

import eu.epsos.assertionvalidator.XSPARole;
import eu.epsos.protocolterminators.integrationtest.ihe.cda.CdaExtraction;
import eu.epsos.protocolterminators.integrationtest.ihe.cda.CdaModel;
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
 * Implements all the Integration Test for the XDR operations for Consent
 * Service.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class XdrCsIT extends ClientGenericIT {

    private static final String REQ_FOLDER = "xdr/consentservice/";

    @BeforeClass
    public static void setUpClass() throws NamingException {
        ClientGenericIT.setUpClass();
        LOG.info("----------------------------");
        LOG.info(" Submit Consent Documents   ");
        LOG.info("----------------------------");
    }

    /*
     * Test cases
     */
    /*
     * Normal Usage
     */
    /**
     * This test performs a simple submitting action for a consent.
     *
     * It is a simple test designed uniquely for testing the normal work-flow of
     * the XDR.
     */
    @Test
    public void testSubmitConsent() {
        List<String> permissions = new ArrayList<String>(1);
        permissions.add("32");
        assertions = getAssertions(permissions, REQ_FOLDER + "PT_CLIENT_XDR_CS_#0.xml", XSPARole.PHYSICIAN);

        testGood("PT_CLIENT_XDR_CS_#0", REQ_FOLDER + "PT_CLIENT_XDR_CS_#0.xml");
        validateCDA(REQ_FOLDER + "PT_CLIENT_XDR_CS_#0.xml", CdaExtraction.MessageType.PORTAL, CdaModel.CONSENT);
    }

    /*
     * Invalid Scenarios
     */
    /**
     * (ERROR)
     *
     * Country A does not allow for consent giving or revoking in other
     * countries
     *
     * Response Status: Failure, Message: Policy Violation, Code: 4705
     */
    @Test
    @Ignore
    public void testSubmitUnsupCons() {
        testFailScenario("PT_CLIENT_XDR_CS_#1", "4705", REQ_FOLDER + "PT_CLIENT_XDR_CS_#1.xml");
    }

    /**
     * (ERROR)
     *
     * Country A requests a higher authentication trust level than assigned to
     * the HCP (e.g. password-based login is not accepted for the requested
     * operation).
     *
     * Response Status: Failure, Message: Weak Authentication, Code: 4702
     */
    @Test
    @Ignore
    public void testSubmitLowAuth() {
        testFailScenario("PT_CLIENT_XDR_DS_#2", "4702", REQ_FOLDER + "PT_CLIENT_XDR_DS_#2.xml");
    }

    /**
     * (ERROR)
     *
     * The provided privacy policy identifiers not supported by country A.
     *
     * Response Status: Failure, Message: Unknown policy, Code: 4706
     */
    @Test
    @Ignore
    public void testSubmitUnsppPol() {
        testFailScenario("PT_CLIENT_XDR_CS_#3", "4706", REQ_FOLDER + "PT_CLIENT_XDR_CS_#3.xml");
    }

    /**
     * (ERROR)
     *
     * Country-A requires for a general consent for epSOS that MUST have been
     * given in country A before more specific consents can be accepted.
     *
     * Response Status: Failure, Message: No consent, Code: 4701
     */
    @Test
    @Ignore
    public void testSubmitCmplxCons() {
        testFailScenario("PT_CLIENT_XDR_CS_#4", "4701", REQ_FOLDER + "PT_CLIENT_XDR_CS_#4.xml");
    }

    @Override
    protected Collection<Assertion> getAssertions(String requestPath, XSPARole role) {
        return hcpAndTrcAssertionCreate(role);
    }

    /**
     * Allows the customization of specific assertions.
     *
     * @param permissions
     * @return a Collection of customized assertions.
     */
    protected Collection<Assertion> getAssertions(List<String> permissions, String requestPath, XSPARole role) {
        return hcpAndTrcAssertionCreate("", permissions, role);
    }
}
