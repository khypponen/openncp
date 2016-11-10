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

import java.io.Serializable;
import java.util.List;

public class SearchFieldVO implements Serializable {
	private static final long serialVersionUID = 65906847403406598L;
	private List<PatientIdVO> patientIds;
	private TextFieldVO birthdate;
	private TextFieldVO surname;
	private TextFieldVO givenname;
	private TextFieldVO streetAddress;
	private TextFieldVO postalCode;
	private TextFieldVO city;
	private TextFieldVO sex;

	public List<PatientIdVO> getPatientIds() {
		return patientIds;
	}

	public void setPatientIds(List<PatientIdVO> patientIds) {
		this.patientIds = patientIds;
	}

	public TextFieldVO getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(TextFieldVO birthdate) {
		this.birthdate = birthdate;
	}

	public TextFieldVO getSurname() {
		return surname;
	}

	public void setSurname(TextFieldVO surname) {
		this.surname = surname;
	}

	public TextFieldVO getGivenname() {
		return givenname;
	}

	public void setGivenname(TextFieldVO givenname) {
		this.givenname = givenname;
	}

	public TextFieldVO getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(TextFieldVO streetAddress) {
		this.streetAddress = streetAddress;
	}

	public TextFieldVO getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(TextFieldVO postalCode) {
		this.postalCode = postalCode;
	}

	public TextFieldVO getCity() {
		return city;
	}

	public void setCity(TextFieldVO city) {
		this.city = city;
	}

	public TextFieldVO getSex() {
		return sex;
	}

	public void setSex(TextFieldVO sex) {
		this.sex = sex;
	}

}
