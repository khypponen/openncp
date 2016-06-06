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
import epsos.openncp.protocolterminator.clientconnector.QueryPatientResponse;
import java.util.List;

/**
 * This is an Data Transformation Service. This provide functions to transform
 * data into a QueryPatientResponse object.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class QueryPatientResponseDts {

    /**
     * Private constructor to disable class instantiation.
     */
    private QueryPatientResponseDts() {
    }

    /**
     * Converts a list of {@link PatientDemographics} object into a
     * {@link QueryPatientResponse} new instance.
     *
     * @param patientDemographics the object to be converted.
     * @return {@link QueryPatientResponse} object - the result of the
     * conversion.
     *
     * @see QueryPatientResponse
     * @see PatientDemographics
     * @see List
     */
    public static QueryPatientResponse newInstance(final List<PatientDemographics> patientDemographics) {

        /*
         * PRE-CONDITIONS 
         */
        if (patientDemographics == null) {
            return null;
        }

        /* 
         * BODY 
         */
        final QueryPatientResponse result = QueryPatientResponse.Factory.newInstance();
        result.setReturnArray(patientDemographics.toArray(new PatientDemographics[0]));
        return result;
    }
}
