/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.model;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import se.sb.epsos.shelob.ws.client.jaxws.PatientDemographics;
import se.sb.epsos.shelob.ws.client.jaxws.PatientId;

public class PersonBuilder {

	public static Person create(String id, String country, String fName, String lName, String sex, String birthDate) {
		return create(null,id,country,fName,lName,sex,birthDate);
	}

    public static Person create(String sessionId, String id, String country, String fName, String lName, String gender, String birthDate) {
        PatientDemographics patientDemographics = new PatientDemographics();

        PatientId patientId = new PatientId();
        patientId.setRoot("2.16.17.710.807.1000.990.1");
        patientId.setExtension(id);
        patientDemographics.getPatientId().add(patientId);
        patientDemographics.setGivenName(fName);
        patientDemographics.setFamilyName(lName);
        patientDemographics.setAdministrativeGender(gender);
        patientDemographics.setCountry(country);
        XMLGregorianCalendar cal = null;
        try {
            cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        cal.setYear(Integer.parseInt(birthDate.substring(0,4)));
        cal.setMonth(Integer.parseInt(birthDate.substring(4,6)));
        cal.setDay(Integer.parseInt(birthDate.substring(6,8)));
        patientDemographics.setBirthDate(cal);
        return new Person(sessionId, patientDemographics, country);
    }

    public static Person create() {
		return create("191212121212","SE","Tolvan","Tolvansson","M","19121212");
	}
}
