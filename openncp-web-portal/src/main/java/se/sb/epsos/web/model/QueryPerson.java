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
package se.sb.epsos.web.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: andreas Date: 2011-06-30 Time: 15.39 To change this template use File | Settings | File Templates.
 */
public class QueryPerson implements Serializable {
	private static final long serialVersionUID = 1585978324260405871L;
	private List<PatientIdVO> patientIds;
	private CountryVO country;
	private String countryInfo;
	private String helpLink;
	private String helpLabel;

	public List<PatientIdVO> getPatientIds() {
		return patientIds;
	}

	public void setPatientIds(List<PatientIdVO> patientIds) {
		this.patientIds = patientIds;
	}

	public CountryVO getCountry() {
		return country;
	}

	public void setCountry(CountryVO country) {
		this.country = country;
	}

	public String getCountryInfo() {
		return countryInfo;
	}

	public void setCountryInfo(String countryInfo) {
		this.countryInfo = countryInfo;
	}

	public String getHelpLink() {
		return helpLink;
	}

	public void setHelpLink(String helpLink) {
		this.helpLink = helpLink;
	}

	public String getHelpLabel() {
		return helpLabel;
	}

	public void setHelpLabel(String helpLabel) {
		this.helpLabel = helpLabel;
	}
	
	

}
