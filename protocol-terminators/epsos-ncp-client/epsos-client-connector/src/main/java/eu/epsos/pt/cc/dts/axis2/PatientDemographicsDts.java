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
package eu.epsos.pt.cc.dts.axis2;

import epsos.openncp.protocolterminator.clientconnector.PatientDemographics;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * This is an Data Transformation Service. This provide functions to transform
 * data into a PatientDemographics object.
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public final class PatientDemographicsDts {

    /**
     * Converts a list of
     * {@link tr.com.srdc.epsos.data.model.PatientDemographics} into a list of
     * {@link epsos.openncp.protocolterminator.clientconnector.PatientDemographics}.
     *
     * @param patientDemList a list of
     * {@link tr.com.srdc.epsos.data.model.PatientDemographics} objects - to be
     * transformed.
     * @return a list of
     * {@link epsos.openncp.protocolterminator.clientconnector.PatientDemographics}
     * objects - the result of the transformation.
     *
     * @see PatientDemographics
     * @see tr.com.srdc.epsos.data.model.PatientDemographics
     * @see List
     */
    public static List<PatientDemographics> newInstance(final List<tr.com.srdc.epsos.data.model.PatientDemographics> patientDemList) {
        if (patientDemList == null) {
            return null;
        }

        final List<PatientDemographics> result = new ArrayList<PatientDemographics>(patientDemList.size());

        for (tr.com.srdc.epsos.data.model.PatientDemographics pd : patientDemList) {
            result.add(newInstance(pd));
        }

        return result;
    }

    /**
     * Converts a {@link tr.com.srdc.epsos.data.model.PatientDemographics} into
     * a
     * {@link epsos.openncp.protocolterminator.clientconnector.PatientDemographics}.
     *
     * @param patientDem a
     * {@link tr.com.srdc.epsos.data.model.PatientDemographics} object to be
     * transformed.
     * @return a
     * {@link epsos.openncp.protocolterminator.clientconnector.PatientDemographics}
     * object - the result of the transformation.
     *
     * @see PatientDemographics
     * @see tr.com.srdc.epsos.data.model.PatientDemographics
     */
    public static PatientDemographics newInstance(final tr.com.srdc.epsos.data.model.PatientDemographics patientDem) {

        /*
         * PRE-CONDITIONS 
         */
        if (patientDem == null) {
            return null;
        }

        /* 
         * BODY 
         */
        final PatientDemographics result = PatientDemographics.Factory.newInstance();

        if (patientDem.getAdministrativeGender() != null) {
            result.setAdministrativeGender(patientDem.getAdministrativeGender().toString());
        }

        if (patientDem.getBirthDate() != null) {
            final Calendar cal = new GregorianCalendar();
            cal.setTime(patientDem.getBirthDate());
            result.setBirthDate(cal);
        }
        result.setCity(patientDem.getCity());
        result.setCountry(patientDem.getCountry());
        result.setEmail(patientDem.getEmail());
        result.setFamilyName(patientDem.getFamilyName());
        result.setGivenName(patientDem.getGivenName());
        result.setPatientIdArray(PatientIdDts.newInstance(patientDem.getIdList()));
        result.setPostalCode(patientDem.getPostalCode());
        result.setStreetAddress(patientDem.getStreetAddress());
        result.setTelephone(patientDem.getTelephone());

        return result;
    }

    /**
     * Private constructor to disable class instantiation.
     */
    private PatientDemographicsDts() {
    }
}
