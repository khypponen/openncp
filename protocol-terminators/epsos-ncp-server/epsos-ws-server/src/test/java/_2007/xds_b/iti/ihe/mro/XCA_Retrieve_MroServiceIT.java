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
package _2007.xds_b.iti.ihe.mro;

import eu.epsos.assertionvalidator.XSPARole;
import eu.epsos.pt.server.it.ServerGenericIT;
import java.util.Collection;
import org.junit.*;
import org.opensaml.saml2.core.Assertion;
import tr.com.srdc.epsos.util.Constants;

/**
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class XCA_Retrieve_MroServiceIT extends ServerGenericIT {

//    private static final String URL = CONFIG_SERVICE.getServiceWSE(COUNTRY_CODE, Constants.OrderService);
    private static final String QUERY_FILE_L3 = "xca/retrieve/mro/xca-retrieve.xml";
    private static final String QUERY_FILE_L1 = "xca/retrieve/mro/xca-retrieve-pdf.xml";
    private static final String QUERY_FILE_INVALD_ID = "xca/retrieve/mro/xca-retrieve-invalid-id.xml";
    private static final String QUERY_FILE_EMPTY_ID = "xca/retrieve/mro/xca-retrieve-empty-id.xml";
    private static final String PATIENT_ISO = "182581814^^^&2.16.620.1.101.10.1.1&ISO";

    public XCA_Retrieve_MroServiceIT() {
    }

    @Override
    protected Collection<Assertion> getAssertions(String requestPath, XSPARole role) {
        return hcpAndTrcAssertionCreate(PATIENT_ISO, XSPARole.PHARMACIST);
    }

    @BeforeClass
    public static void setUpClass() {
        LOG.info("----------------------------");
        LOG.info(" Retrieve Document (MRO)");
        LOG.info("----------------------------");

        epr = CONFIG_SERVICE.getServiceWSE(COUNTRY_CODE, Constants.OrderService);
    }

    /**
     * Test of respondingGateway_CrossGatewayRetrieve method, of class
     * XCA_ServiceSkeleton.
     */
    @Test
    public void testRetrieveDocument() {
        this.assertions = this.assertions = this.getAssertions(QUERY_FILE_L3, XSPARole.PHARMACIST);
        testGood("testRetrieveDocument", QUERY_FILE_L3);
    }
    
    /**
     * Test of respondingGateway_CrossGatewayRetrieve method, of class
     * XCA_ServiceSkeleton.
     */
    @Test
    public void testRetrieveDocumentPdf() {
        this.assertions = this.assertions = this.getAssertions(QUERY_FILE_L1, XSPARole.PHARMACIST);
        testGood("testRetrieveDocument", QUERY_FILE_L1);
    }

    @Ignore
    @Test
    public void testRetrieveInvalidDocument() {
        this.assertions = this.assertions = this.getAssertions(QUERY_FILE_INVALD_ID, XSPARole.PHARMACIST);
        testFail("testRetrieveInvalidDocument", "Failure", QUERY_FILE_INVALD_ID);
    }

    @Ignore
    @Test
    public void testRetrieveEmptyDocument() {
        this.assertions = this.getAssertions(QUERY_FILE_EMPTY_ID, XSPARole.PHARMACIST);
        testFail("testRetrieveEmptyDocument", "Failure", QUERY_FILE_EMPTY_ID);
    }
}
