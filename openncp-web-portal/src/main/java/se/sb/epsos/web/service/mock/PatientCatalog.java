/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
 *
 *    This file is part of epSOS-WEB.
 *
 *    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
 **/
package se.sb.epsos.web.service.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sb.epsos.shelob.ws.client.jaxws.PatientDemographics;
import se.sb.epsos.shelob.ws.client.jaxws.PatientId;
import se.sb.epsos.web.service.CountryConfigManager;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class PatientCatalog {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientCatalog.class);

    private static final Map<String, PatientDemographics> patients = createPatientMap();

    private static Map<String, PatientDemographics> createPatientMap() {
        Map<String, PatientDemographics> map = new HashMap<>();
        // SE patients
        map.put("191212121212^^^&2.16.17.710.807.1000.990.1", createPatient("191212121212", "SE", "19121212", "Tolvan", "Tolvansson", "M"));
        map.put("193508249079^^^&2.16.17.710.807.1000.990.1", createPatient("193508249079", "SE", "19350824", "Aivars", "Brun", "M"));
        map.put("199604082397^^^&2.16.17.710.807.1000.990.1", createPatient("199604082397", "SE", "19960408", "Aivars", "Numan", "M"));
        map.put("192405038569^^^&2.16.17.710.807.1000.990.1", createPatient("192405038569", "SE", "19240503", "Ofelia", "Ordinationslista", "F"));
        map.put("195011052478^^^&2.16.17.710.807.1000.990.1", createPatient("195011052478", "SE", "19501105", "Kenny", "Kanon", "M"));

        // DK patients
        map.put("2601010001^^^&2.16.17.710.802.1000.990.1", createPatient("2601010001", "DK", "19010126", "EPSOS001", "EPSOS001", "M"));
        map.put("2601010002^^^&2.16.17.710.802.1000.990.1", createPatient("2601010002", "DK", "19010126", "EPSOS002", "EPSOS002", "F"));
        map.put("2512484916^^^&2.16.17.710.802.1000.990.1", createPatient("2512484916", "DK", "19481225", "Nancy", "Berggren", "F"));

        // ME patients
        map.put("190001011234^^^&2.16.17.710.811.1000.990.1", createPatient("190001011234", "ME", "19000101", "Frodo", "Baggins", "M"));
        map.put("190012121234^^^&2.16.17.710.811.1000.990.1", createPatient("190012121234", "ME", "19001212", "Bilbo", "Baggins", "M"));

        // FI patients
        map.put("031082-9958^^^&1.2.246.556.12001.4.1000.990.1", createPatient("031082-9958", "FI", "19821003", "Pekka", "Potilas", "M"));

        // EU Patients
        map.put("1^^^&2.16.17.710.850.1000.990.1", createPatient("1", "EU", "19800110", "Robert", "Schuman", "M"));

        return map;
    }

    public static PatientDemographics query(String epsosID) {
        LOGGER.info("Looking for patient: " + epsosID);
        PatientDemographics pat = patients.get(epsosID);
        LOGGER.info("Patient: " + epsosID + " was " + (pat != null ? "found" : "not found") + " in catalog");
        return pat;
    }

    private static PatientDemographics createPatient(String id, String country, String birthDate, String fName, String lName, String sex) {
        String homeCommunityId = CountryConfigManager.getHomeCommunityId(country);
        PatientDemographics patientDemographics = new PatientDemographics();
        PatientId patientId = new PatientId();
        patientId.setRoot(homeCommunityId);
        patientId.setExtension(id);
        patientDemographics.getPatientId().add(patientId);
        patientDemographics.setGivenName(fName);
        patientDemographics.setFamilyName(lName);
        patientDemographics.setAdministrativeGender(sex);
        patientDemographics.setCountry(country);
        XMLGregorianCalendar cal = null;
        try {
            cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        cal.setYear(Integer.parseInt(birthDate.substring(0, 4)));
        cal.setMonth(Integer.parseInt(birthDate.substring(4, 6)));
        cal.setDay(Integer.parseInt(birthDate.substring(6, 8)));
        patientDemographics.setBirthDate(cal);
        return patientDemographics;
    }
}
