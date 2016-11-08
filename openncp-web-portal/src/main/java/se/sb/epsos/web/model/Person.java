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

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import se.sb.epsos.shelob.ws.client.jaxws.PatientDemographics;
import se.sb.epsos.shelob.ws.client.jaxws.PatientId;
import se.sb.epsos.web.service.PersonCache;
import se.sb.epsos.web.service.PersonCacheKey;
import se.sb.epsos.web.util.EpsosStringUtils;

public class Person {
	private static final long serialVersionUID = -164051720779795644L;
	private PersonCacheKey cacheKey;
	private PatientDemographics patientDemographics;
	private String countryCode;

	public Person() {
		this(null, null, null);
	}

    public Person(String sessionId, PatientDemographics patientDemographics, String countryCode) {
        this.patientDemographics = patientDemographics;
        this.countryCode = countryCode;
        cacheKey = new PersonCacheKey(sessionId, getEpsosId());
        PersonCache.getInstance().put(cacheKey, this);
    }

    public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public PersonCacheKey getCacheKey() {
		return cacheKey;
	}

	public String getId() {
        return patientDemographics.getPatientId().get(0).getExtension(); // TODO: Fix show all patients Ids
    }

	public String getEpsosId() {
        if(patientDemographics == null) // todo: fix for test.... to be removed
            return "";

        StringBuilder epsosId = new StringBuilder()
                .append(patientDemographics.getPatientId().get(0).getExtension())// TODO: use all patients Ids
                .append("^^^&")     //TODO:check if this strange string is needed
                .append(patientDemographics.getPatientId().get(0).getRoot())
                .append("&ISO");
        return epsosId.toString();
	}

	public String getCountry() {
        if(patientDemographics == null) // todo: fix for test.... to be removed
            return "";
        return patientDemographics.getCountry();
    }

	public String getFirstname() {
        return patientDemographics.getGivenName();
    }

	public String getLastname() {
        return patientDemographics.getFamilyName();
    }

	public String getCommonName() {
        StringBuilder commonName = new StringBuilder()
                .append(getFirstname())
                .append(" ")
                .append(getLastname());
        return commonName.toString();
	}

    /*
     * @return always empty string ""
     * @deprecated
     */
	public String getMiddlename() {
        return "";
	}

    /*
     * @return always empty string ""
     * @deprecated
     */
    public String getNamePrefix() {
        return "";
	}

    /*
     * @return always empty string ""
     * @deprecated
     */
    public String getNameSuffix() {
        return "";
	}

    /*
     * @return always empty string ""
     * @deprecated Use getCommonName, returns same name
     */
    public String getFullname() {
        return getCommonName();
	}

	public String getCity() {
        return patientDemographics.getCity();
    }

	public String getGender() {
        if(patientDemographics == null) // todo: fix for test.... to be removed
            return "";

        if (StringUtils.isEmpty(patientDemographics.getAdministrativeGender())) {
            return "U";
        } else {
            return this.patientDemographics.getAdministrativeGender();
        }
    }

    public String getStreetAddress() {
        return patientDemographics.getStreetAddress();
	}

	public String getZipcode() {
        return patientDemographics.getPostalCode();
	}

	public String getState() {
        return "";
	}

	public String getPhoneNumber() {
        return patientDemographics.getTelephone();
	}

	public String getPlaceofbirth() {
        return"";
	}

	public String getCitizenship() {
        return"";
    }

	public String getNationality() {
        return "";
	}

	public String getAccountnumber() {
        return "";
	}

	public String getDriverslicensnumber() {
        return"";
    }

	public Date getBirthdate() {
        return patientDemographics.getBirthDate().toGregorianCalendar().getTime();
	}


	public PatientDemographics getPatientDemographics() {
		return patientDemographics;
	}

    public PatientId getPatientId() {
        return patientDemographics.getPatientId().get(0); // TODO: return only the first one..
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cacheKey == null) ? 0 : cacheKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object person) {
		if (!(person instanceof Person)) {
			return false;
		} else {
			String epsosId = ((Person) person).getEpsosId();
			return (EpsosStringUtils.nullSafeCompare(this.getEpsosId(), epsosId));
		}
	}

}
