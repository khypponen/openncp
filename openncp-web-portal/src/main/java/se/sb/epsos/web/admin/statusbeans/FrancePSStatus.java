/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sb.epsos.web.admin.statusbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.admin.NcpStatusHelper;
import se.sb.epsos.web.admin.NcpStatusInterface;
import se.sb.epsos.web.model.CdaDocument;
import se.sb.epsos.web.model.CountryVO;
import se.sb.epsos.web.model.PatientIdVO;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.model.TRC;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.service.NcpServiceFacade;
import se.sb.epsos.web.service.NcpServiceFacadeImpl;

/**
 *
 * @author danielgronberg
 */
@Component
public class FrancePSStatus implements NcpStatusInterface, Serializable {

    private static final long serialVersionUID = 8958263399522316920L;
    private static final Logger LOGGER = LoggerFactory.getLogger(FrancePSStatus.class);

    @Override
    public Boolean getStatus() {
        NcpServiceFacade serviceFacade = new NcpServiceFacadeImpl();
        try {
            NcpStatusHelper.initFacade(serviceFacade);
            EpsosAuthenticatedWebSession session = (EpsosAuthenticatedWebSession) EpsosAuthenticatedWebSession.get();

            List<PatientIdVO> patientValueList = new ArrayList<PatientIdVO>();
            patientValueList.add(new PatientIdVO("patient.data.ins.france", "2.16.17.710.790.1000.990.1", "0467172911163665402182"));

            List<Person> persons = serviceFacade.queryForPatient(null, patientValueList, new CountryVO("FR", "Frankrike", "2.16.17.710.790.1000.990.1"));
            if (persons.size() > 0) {
                serviceFacade.setTRCAssertion(new TRC(persons.get(0), TRC.TrcPurpose.EMERGENCY), session.getUserDetails());
                CdaDocument ep = NcpStatusHelper.getPSForPerson(serviceFacade, persons.get(0));
                if (ep != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (NcpServiceException ex) {
            LOGGER.error("Exception caught!: " + ex);
            return false;
        }
    }

    @Override
    public String getTitle() {
        return "Patientöversikter Frankrike";
    }
}
