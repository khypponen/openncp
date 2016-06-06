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
package eu.epsos.pt.cc.mro;

import eu.epsos.assertionvalidator.XSPARole;
import eu.epsos.protocolterminators.integrationtest.ihe.cda.CdaExtraction;
import eu.epsos.protocolterminators.integrationtest.ihe.cda.CdaModel;
import eu.epsos.pt.cc.ClientGenericIT;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.naming.NamingException;
import javax.xml.soap.SOAPElement;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opensaml.saml2.core.Assertion;

/**
 * Implements all the Integration Test for the XCA Retrieve operation for MRO
 * Service.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class XcaMroRetrieveIT extends ClientGenericIT {

    /**
     * Folder under test/resources were are soap requests for this tests.
     */
    private static final String REQ_FOLDER = "xca/mroservice/";

    @BeforeClass
    public static void setUpClass() throws NamingException {
        ClientGenericIT.setUpClass();
        LOG.info("----------------------------");
        LOG.info(" Retrieve MRO Documents");
        LOG.info("----------------------------");
    }

    /*
     * Normal usage
     */
    /**
     * This test performs an XCA Retrieve for Mro, using valid Document
     * Identifiers. It is a simple test designed uniquely for testing the normal
     * work-flow of the XCA Retrieve.
     */
    @Test
    public void testRetrieveMro() {
        List<String> permissions = new ArrayList<String>(2);
        permissions.add("4");
        permissions.add("10");
        assertions = getAssertions(permissions, REQ_FOLDER + "PT_CLIENT_XCA_MRO_#1.xml", XSPARole.PHARMACIST);

        SOAPElement rspSoapMsg = testGood("PT_CLIENT_XCA_MRO_#1", REQ_FOLDER + "PT_CLIENT_XCA_MRO_#1.xml");
        validateCDA(rspSoapMsg, CdaExtraction.MessageType.PORTAL, CdaModel.MRO);
    }
    /*
     * Normal usage
     */

    /**
     * This test performs an XCA Retrieve for MRO Pdf, using valid Document
     * Identifiers. It is a simple test designed uniquely for testing the normal
     * work-flow of the XCA Retrieve.
     */
    @Test
    public void testRetrieveMroPdf() {
        List<String> permissions = new ArrayList<String>(2);
        permissions.add("4");
        permissions.add("10");
        assertions = getAssertions(permissions, REQ_FOLDER + "PT_CLIENT_XCA_MRO_#2.xml", XSPARole.PHARMACIST);

        SOAPElement rspSoapMsg = testGood("PT_CLIENT_XCA_MRO_#2", REQ_FOLDER + "PT_CLIENT_XCA_MRO_#2.xml");
        validateCDA(rspSoapMsg, CdaExtraction.MessageType.PORTAL, CdaModel.MRO);
    }
    /*
     * Invalid scenarios: N/A ATM.
     */

    /*
     * Auxiliar methods
     */
    @Override
    protected Collection<Assertion> getAssertions(String requestPath, XSPARole role) {
        return hcpAndTrcAssertionCreate(role);
    }

    protected Collection<Assertion> getAssertions(List<String> permissions, String requestPath, XSPARole role) {
        return hcpAndTrcAssertionCreate("182581814^^^&amp;2.16.620.1.101.10.1.1&amp;ISO", permissions, role);
    }
}
