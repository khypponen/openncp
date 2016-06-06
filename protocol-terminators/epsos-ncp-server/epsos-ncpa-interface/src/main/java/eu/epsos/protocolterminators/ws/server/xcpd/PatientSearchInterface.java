/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik Tic. Ltd. Sti. epsos@srdc.com.tr
 *
 * This file is part of SRDC epSOS NCP.
 *
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with SRDC epSOS NCP. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Changes by Kela (The Social Insurance Institution of Finland) Contact email: epsos@kanta.fi or
 * Konstantin.Hypponen@kela.fi
 */
package eu.epsos.protocolterminators.ws.server.xcpd;

import eu.epsos.protocolterminators.ws.server.common.NationalConnectorInterface;
import eu.epsos.protocolterminators.ws.server.exception.NIException;

import java.util.List;
import tr.com.srdc.epsos.data.model.PatientDemographics;
import tr.com.srdc.epsos.data.model.PatientId;
import tr.com.srdc.epsos.securityman.exceptions.InsufficientRightsException;

/**
 * This interface describes the National Connector API regarding Patient Identification Service.
 * 
 * @author Konstantin Hypponen<code> - Konstantin.Hypponen@kela.fi</code>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public interface PatientSearchInterface extends NationalConnectorInterface {

    /**
     * Translates a National citizen number in an epSOS id.
     * @param citizenNumber a valid citizen identifier
     * @return the citizen epSOS identifier
     */
    public String getPatientId(String citizenNumber) throws NIException, InsufficientRightsException;

    /**
     * Searches the NI for all the patients that relates to the given <code>idList</code>.
     * @param idList A set of patient's epSOS identifiers
     * @return A set of patient demographics
     */
    public List<PatientDemographics> getPatientDemographics(List<PatientId> idList) throws NIException, InsufficientRightsException;
}
