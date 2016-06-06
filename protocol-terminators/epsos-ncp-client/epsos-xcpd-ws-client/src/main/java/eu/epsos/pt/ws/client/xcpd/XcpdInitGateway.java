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
package eu.epsos.pt.ws.client.xcpd;

import eu.epsos.exceptions.NoPatientIdDiscoveredException;
import eu.epsos.dts.xcpd.RespondingGateway_RequestReceiver;
import java.util.List;
import org.hl7.v3.PRPAIN201306UV02;
import org.opensaml.saml2.core.Assertion;
import tr.com.srdc.epsos.data.model.PatientDemographics;
import tr.com.srdc.epsos.ws.xcpd.client.RespondingGateway_RequestSender;

/**
 * XCPD Initiating Gateway
 *
 * This is an implementation of a IHE XCPD Initiation Gateway. This class provides the necessary operations to perform
 * PatientDiscovery.
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class XcpdInitGateway {

    /**
     * Performs a Patient Discovery for the given Patient Demographics.
     *
     * @param pd the Patient Demographics set to be used in the request.
     * @param idAssertion HCP identity assertion.
     * @param countryCode country code - ISO 3166-1 alpha-2
     *
     * @return a List of matching Patient Demographics, each representing a patient person.
     *
     * @throws NoPatientIdDiscoveredException contains the error message
     */
    public static List<PatientDemographics> patientDiscovery(final PatientDemographics pd,
                                                             final Assertion idAssertion,
                                                             final String countryCode)
            throws NoPatientIdDiscoveredException {
        List<PatientDemographics> result;

        PRPAIN201306UV02 response;
        response = RespondingGateway_RequestSender.respondingGateway_PRPA_IN201305UV02(pd,
                                                                                       idAssertion,
                                                                                       countryCode);
        result = RespondingGateway_RequestReceiver.respondingGateway_PRPA_IN201306UV02(response);

        return result;
    }

    /**
     * Private constructor to disable class instantiation.
     */
    private XcpdInitGateway() {
    }
}
