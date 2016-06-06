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
package eu.epsos.pt.cc.stub;

import java.text.ParseException;

import epsos.openncp.protocolterminator.clientconnector.EpsosDocument1;
import epsos.openncp.protocolterminator.clientconnector.PatientDemographics;
import eu.epsos.exceptions.XdrException;
import eu.epsos.pt.cc.dts.axis2.XdrRequestDts;
import eu.epsos.pt.ws.client.xdr.XdrDocumentSource;
import org.opensaml.saml2.core.Assertion;
import tr.com.srdc.epsos.data.model.XdrRequest;
import tr.com.srdc.epsos.data.model.XdrResponse;

/**
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class DispensationService {

    /**
     * Notify the patient’s country of affiliation on a successful dispensation
     * of an ePrescription.
     *
     * <br/> <dl> <dt><b>Preconditions: </b> <dd>Service consumer and service
     * provider share a common identifier for the patient <dd>The patient has
     * given consent to the use of epSOS <dd>The service consumer has previously
     * retrieved the list of the patient’s available ePrescriptions <dd>All
     * available ePrescriptions for the identified patient are accessible for
     * NCP-A and the provided eDispensation data relates to these ePrescriptions
     * <dd>A treatment relationship exists between the patient and the
     * requesting HCP and the attesting assertion can be verified by the service
     * provider <dd>The HCP is authorised to dispense medication for the patient
     * </dl> <dl> <dt><b>Fault Conditions: </b> <dd>Preconditions for a success
     * scenario are not met <dd>The requesting HCP has insufficient rights to
     * dispense the identified patient’s ePrescriptions <dd>One or more of the
     * provided dispensation items do not relate to available ePrescriptions of
     * the identified patient <dd>The ePrescription that is referred to by an
     * eDispensation has already been dispensed. <dd>No consent for
     * ePrescription sharing and dispensing is registered for the identified
     * patient <dd>The eDispensation data is not provided in all mandatory
     * encodings <dd>Temporary failure (e.g. verification of a signature cannot
     * be performed due to a PKI failure) </dl> <dl> <dt><b>Warning Conditions:
     * </b> <dd>eDispensation data is not processed by the patient’s country of
     * affiliation </dl>
     *
     * @param document document to be submitted
     * @param patient patient's demographic data
     * @param hcpAssertion HCP Assertion
     * @param trcAssertion TRC Assertion
     * @throws ParseException
     */
    public static /* InitializeResponse */ XdrResponse initialize(final EpsosDocument1 document, final PatientDemographics patient, final String countryCode,
                                                                                        final Assertion hcpAssertion, final Assertion trcAssertion) throws XdrException, ParseException {
        XdrRequest request;
        request = XdrRequestDts.newInstance(document, patient, hcpAssertion, trcAssertion);
        return XdrDocumentSource.initialize(request, countryCode);
    }

    /**
     * Notify the patient’s country of affiliation on an erroneous eDispensation
     * notification, in order to allow it to roll back any changes made on its
     * internal data that were triggered by the erroneous notification.
     *
     * <br/> <dl> <dt><b>Preconditions: </b> <dd>Service consumer and service
     * provider share a common identifier for the patient <dd>The service
     * consumer has previously retrieved the list of the patient’s available
     * ePrescriptions and dispensed the identified medicine </dl> <dl>
     * <dt><b>Fault Conditions: </b> <dd>Preconditions for a success scenario
     * are not met <dd>The HCP has insufficient rights to process the patient’s
     * ePrescription data <dd>The HCP was not the original dispenser of the
     * identified medication item <dd>The identified item had not been dispensed
     * previously <dd>Temporary failure (e.g. service provider is temporarily
     * unable to access an internal service) </dl> <dl> <dt><b>Warning
     * Conditions: </b> <dd>eDispensation data is not processed by the country
     * of affiliation <dd>eDispensations are not rolled back automatically by
     * the country of affiliation </dl>
     *
     */
    public static /* InitializeResponse */ XdrResponse discard(/* Initialize */) {
        throw new UnsupportedOperationException("Operation not supported.");
    }

    private DispensationService() {
    }
}
