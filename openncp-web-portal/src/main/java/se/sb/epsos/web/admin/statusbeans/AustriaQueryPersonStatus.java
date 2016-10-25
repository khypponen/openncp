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
public class AustriaQueryPersonStatus implements NcpStatusInterface, Serializable {

    private static final long serialVersionUID = 8527455709309411259L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AustriaQueryPersonStatus.class);

    @Override
    public String getTitle() {
        return "Patientöversikter Österrike";
    }

    @Override
    public Boolean getStatus() {
        NcpServiceFacade serviceFacade = new NcpServiceFacadeImpl();
        try {
            NcpStatusHelper.initFacade(serviceFacade);
            EpsosAuthenticatedWebSession session = (EpsosAuthenticatedWebSession) EpsosAuthenticatedWebSession.get();

            List<PatientIdVO> patientValueList = new ArrayList<PatientIdVO>();
            patientValueList.add(new PatientIdVO("patient.search.patient.id", "2.16.840.1.113883.2.16.3.1.3.100.990.1", null));
            patientValueList.add(new PatientIdVO("patient.search.patient.svnr", "2.16.840.1.113883.2.16.3.1.3.100.990.1", null));
            patientValueList.add(new PatientIdVO("patient.data.givenname", null, null));
            patientValueList.add(new PatientIdVO("patient.data.street.address", null, null));
            patientValueList.add(new PatientIdVO("patient.data.code", null, null));
            patientValueList.add(new PatientIdVO("patient.data.city", null, null));

            List<Person> persons = serviceFacade.queryForPatient(null, patientValueList, new CountryVO("AT", "Österrike", "2.16.840.1.113883.2.16.3.1.3.100.990.1"));
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
}
