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
package _2007.xds_b.iti.ihe.consent;

import eu.epsos.assertionvalidator.XSPARole;
import eu.epsos.pt.server.it.ServerGenericIT;
import java.util.Collection;
import org.junit.*;
import org.opensaml.saml2.core.Assertion;
import tr.com.srdc.epsos.util.Constants;

/**
 * Integration test class for the XDR Submit Document service.
 * @author gareth
 */
public class XDR_Submit_ConsentServiceIT extends ServerGenericIT {

    @BeforeClass
    public static void setUpClass() {
        LOG.info("----------------------------");
        LOG.info(" Submit Document Consent");
        LOG.info("----------------------------");

        epr = CONFIG_SERVICE.getServiceWSE(COUNTRY_CODE, Constants.ConsentService);
    }

    /**
     * Test of respondingGateway_ProvideAndRegisterDocumentSet method, of class XDR_ServiceSkeleton.
     * Expected result is a response with success status.
     */
    @Test
    public void testSubmitConsent() {
        this.assertions = this.getAssertions("xdr/consent/testSubmitConsent.xml", XSPARole.PHYSICIAN);
        testGood("testSubmitConsent", "xdr/consent/testSubmitConsent.xml");
    }

    @Override
    protected Collection<Assertion> getAssertions(String requestPath, XSPARole role) {
        return hcpAndTrcAssertionCreate(role);
    }
}
