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

import java.util.ArrayList;
import java.util.List;
import tr.com.srdc.epsos.data.model.PatientId;

/**
 * This is an Data Transformation Service. This provide functions to transform
 * data into a PatientDemographics object.
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public final class PatientIdDts {

    public static List<PatientId> newInstance(final epsos.openncp.protocolterminator.clientconnector.PatientId[] patientIdList) {
        if (patientIdList == null) {
            return null;
        }

        List<PatientId> result = new ArrayList<PatientId>(patientIdList.length);
        for (int i = 0; i < patientIdList.length; i++) {
            result.add(newInstance(patientIdList[i]));
        }

        return result;
    }

    public static PatientId newInstance(final epsos.openncp.protocolterminator.clientconnector.PatientId patientId) {
        if (patientId == null) {
            return null;
        }

        PatientId result = new PatientId();
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
