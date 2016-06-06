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
public class XCA_Query_MroServiceIT extends ServerGenericIT {

    private static final String QUERY_FILE = "xca/query/mro/xca-query.xml";
    private static final String QUERY_FILE_INVALID_ID = "xca/query/mro/xca-query-invalid-id.xml";
    private static final String QUERY_FILE_EMPTY_ID = "xca/query/mro/xca-query-empty-id.xml";

    @BeforeClass
    public static void setUpClass() {
        LOG.info("----------------------------");
        LOG.info(" Query Documents (MRO)");
        LOG.info("----------------------------");

        epr = CONFIG_SERVICE.getServiceWSE(COUNTRY_CODE, Constants.OrderService);
    }

    /**
     * Test of respondingGateway_CrossGatewayQuery method, of class
     * XCA_ServiceSkeleton.
     */
    @Test
    public void testQueryDocuments() {
        this.assertions = this.getAssertions(QUERY_FILE, XSPARole.PHARMACIST);
        testGood("testQueryDocuments", QUERY_FILE);
    }

    @Test
    @Ignore
    public void testQueryDocumentInvalidId() {
        this.assertions = this.getAssertions(QUERY_FILE_INVALID_ID, XSPARole.PHARMACIST);
        testFail("testQueryInvalidDocument", "errorCode=\"1101\"", QUERY_FILE_INVALID_ID);
    }

    @Test
    @Ignore
    public void testQueryDocumentEmptyId() {
        this.assertions = this.getAssertions(QUERY_FILE_EMPTY_ID, XSPARole.PHARMACIST);
        testFail("testQueryDocumentEmptyId", "errorCode=\"1101\"", QUERY_FILE_EMPTY_ID);
    }

    @Override
    @Ignore
    protected Collection<Assertion> getAssertions(String requestPath, XSPARole role) {
        return hcpAndTrcAssertionCreate(getPatientIdIso(requestPath), role);
    }
}
