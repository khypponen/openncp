/*
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. <epsos@srdc.com.tr>
 *
 * This file is part of SRDC epSOS NCP.
 *
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.ws.xcpd.client;

import ee.affecto.epsos.util.EventLogClientUtil;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.epsos.dts.xcpd.PRPAIN201305UV022DTS;
import java.util.Locale;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.opensaml.saml2.core.Assertion;
import tr.com.srdc.epsos.data.model.PatientDemographics;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.OidUtil;

/**
 * RespondingGateway_RequestSender class.
 *
 * Contains the necessary operations to build a XCPD request and to send it to the NCP-A.
 *
 * @author SRDC <code> - epsos@srdc.com.tr</code>
 * @author Aarne Roosi<code> - Aarne.Roosi@Affecto.com</code>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public final class RespondingGateway_RequestSender {

    /**
     * Builds and sends a PRPA_IN201305UV02 HL7 message, representing an XCPD Request process.
     *
     * @param pd the Patient Demographics object.
     * @param idAssertion the assertion.
     * @return a PRPAIN201306UV02 (XCPD Response) message.
     *
     * @see PRPAIN201306UV02
     * @see PatientDemographics
     * @see Assertion
     * @see String
     */
    public static PRPAIN201306UV02 respondingGateway_PRPA_IN201305UV02(final PatientDemographics pd,
                                                                       final Assertion idAssertion,
                                                                       final String countryCode) {

        String epr;
        ConfigurationManagerService configManager = ConfigurationManagerService.getInstance();
        epr = configManager.getServiceWSE(countryCode.toLowerCase(Locale.ENGLISH),
                                     Constants.PatientIdentificationService);

        PRPAIN201305UV02 hl7Request;
        String dstHomeCommunityId = OidUtil.getHomeCommunityId(countryCode.toLowerCase(Locale.ENGLISH));
        hl7Request = PRPAIN201305UV022DTS.newInstance(pd, dstHomeCommunityId);

        return sendRequest(epr, hl7Request, idAssertion, countryCode);
    }

    /**
     *
     * @param epr
     * @param pRPAIN201305UV022
     * @param idAssertion
     * @param countryCode
     * @return
     */
    private static PRPAIN201306UV02 sendRequest(String epr,
                                               PRPAIN201305UV02 pRPAIN201305UV022,
                                               Assertion idAssertion, final String countryCode) {
        RespondingGateway_ServiceStub stub = new RespondingGateway_ServiceStub(epr);

        EventLogClientUtil.createDummyMustUnderstandHandler(stub);  // Dummy handler for any mustUnderstand
        stub.setCountryCode(countryCode);

        return stub.respondingGateway_PRPA_IN201305UV02(pRPAIN201305UV022, idAssertion);
    }

    private RespondingGateway_RequestSender() {
    }
}
