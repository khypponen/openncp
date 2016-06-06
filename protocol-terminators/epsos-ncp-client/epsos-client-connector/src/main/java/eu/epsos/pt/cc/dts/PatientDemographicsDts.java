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
package eu.epsos.pt.cc.dts;

import java.text.ParseException;

import tr.com.srdc.epsos.data.model.PatientDemographics;
import tr.com.srdc.epsos.data.model.PatientDemographics.Gender;

/**
 * This is an Data Transformation Service. This provide functions to transform
 * data into a PatientDemographics object.
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public final class PatientDemographicsDts {

    /**
     * Converts a QueryPatientDocument object into a PatienDemographics Object.
     *
     * @param patientDemographis representing a QueryPatientDocument.
     * @return a PatientDemographics object.
     * @throws ParseException
     *
     * @see PatientDemographics
     * @see QueryPatientDocument
     */
    public static PatientDemographics newInstance(final epsos.openncp.protocolterminator.clientconnector.PatientDemographics patientDemographis) throws ParseException {
        if (patientDemographis == null) {
            return null;
        }

        final PatientDemographics result = new PatientDemographics();

        if (patientDemographis.getAdministrativeGender() != null && !patientDemographis.getAdministrativeGender().isEmpty()) {
            result.setAdministrativeGender(Gender.parseGender(patientDemographis.getAdministrativeGender()));
        }
        if (patientDemographis.getBirthDate() != null) {
            result.setBirthDate(patientDemographis.getBirthDate().getTime());
        }
        if (patientDemographis.getCity() != null && !patientDemographis.getCity().isEmpty()) {
            result.setCity(patientDemographis.getCity());
        }
        if (patientDemographis.getCountry() != null && !patientDemographis.getCountry().isEmpty()) {
            result.setCountry(patientDemographis.getCountry());
        }
        if (patientDemographis.getEmail() != null && !patientDemographis.getEmail().isEmpty()) {
            result.setEmail(patientDemographis.getEmail());
        }
        if (patientDemographis.getFamilyName() != null && !patientDemographis.getFamilyName().isEmpty()) {
            result.setFamilyName(patientDemographis.getFamilyName());
        }
        if (patientDemographis.getGivenName() != null && !patientDemographis.getGivenName().isEmpty()) {
            result.setGivenName(patientDemographis.getGivenName());
        }
        if (patientDemographis.getPatientIdArray() != null) {
            result.setIdList(PatientIdDts.newInstance(patientDemographis.getPatientIdArray()));
        }
        if (patientDemographis.getPostalCode() != null && !patientDemographis.getPostalCode().isEmpty()) {
            result.setPostalCode(patientDemographis.getPostalCode());
        }
        if (patientDemographis.getStreetAddress() != null && !patientDemographis.getStreetAddress().isEmpty()) {
            result.setStreetAddress(patientDemographis.getStreetAddress());
        }
        if (patientDemographis.getTelephone() != null && !patientDemographis.getTelephone().isEmpty()) {
            result.setTelephone(patientDemographis.getTelephone());
        }

        return result;
    }

    /**
     * Private constructor to disable class instantiation.
     */
    private PatientDemographicsDts() {
    }
}
