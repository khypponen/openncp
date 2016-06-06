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

import java.util.List;
import epsos.openncp.protocolterminator.clientconnector.PatientId;

/**
 * This is an Data Transformation Service. This provide functions to transform
 * data into a PatientDemographics object.
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public final class PatientIdDts {

    public static PatientId[] newInstance(final List<tr.com.srdc.epsos.data.model.PatientId> patientId) {
        if (patientId == null) {
            return null;
        }

        PatientId[] result = new PatientId[patientId.size()];
        for(int i = 0; i < patientId.size(); i++) {
            result[i] = newInstance(patientId.get(i));
        }

        return result;
    }

    public static PatientId newInstance(final tr.com.srdc.epsos.data.model.PatientId patientId) {
        if (patientId == null) {
            return null;
        }

        PatientId result = PatientId.Factory.newInstance();
        result.setRoot(patientId.getRoot());
        result.setExtension(patientId.getExtension());

        return result;
    }

    /**
     * Private constructor to disable class instantiation.
     */
    private PatientIdDts() {
    }
}
