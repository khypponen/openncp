/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. <epsos@srdc.com.tr>
 *
 * This file is part of SRDC epSOS NCP.
 *
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.data.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Last update on 2012-09-08 part of review for version 0.3.0.
 *
 * @since 0.1.0
 *
 * @author SRDC<code> - epsos@srdc.com.tr</code>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class PatientDemographics {

	public static enum Gender {
		FEMALE("F"),
		MALE("M"),
		UNDIFFERENTIATED("UN");

		private String mnemonic;
		
		private Gender(String mnemonic) {
			this.mnemonic = mnemonic;
		}
		
		public String getMnemonic() {
			return mnemonic;
		}
		
		public static Gender parseGender(String genderString) throws ParseException {
			for(Gender g : Gender.values()) {
				if(g.name().equalsIgnoreCase(genderString) || g.mnemonic.equalsIgnoreCase(genderString)) {
					return g;
				}
			}

			throw new ParseException("Unable to parse gender string ('" + genderString + "') to a valid gender.", -1);
		}
		
		@Override
	 	public String toString() {
			return mnemonic;
	 	}		
		
	}
	
	private static Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
	private static Pattern telephonePattern = Pattern.compile("^\\+?(\\(.+\\))?[0-9 ?\\-?]+[0-9]$");

    private String familyName;
    private String givenName;
    private Date birthDate;
    private Gender administrativeGender;
    private String city;
    private String country;
    private String postalCode;
    private String streetAddress;
    private String telephone;
    private String email;
    private List<PatientId> idList;

    /**
     * @return the familyName
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * @param familyName the familyName to set
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * @return the givenName
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * @param givenName the givenName to set
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * A shortcut method for backward compatibility with XCPD server version
     * 0.2.0
     * 
     * NOTICE: If need to use multiple id's, use: getId(int i)
     *
     * @return he extension part of the first patient ID.
     */
    public String getId() {
    	return getId(0);
    }

    /**
     * A shortcut method for backward compatibility with XCPD server version
     * 0.2.0
     * 
     * @param  i index number of id's
     * @return he extension part of the first patient ID.
     */
    public String getId(int i) {
        if (idList != null && !idList.isEmpty() && idList.size() > i) {
            return idList.get(i).getExtension();
        }
        return null;
    }
    
    /**
     * @param id the id to set
     *
     * NOTICE: If need to set multiple id's use: setId(int i, String id)
     *
     */
    public void setId(String id) {
    	setId(0, id);
    }

    /**
     * @param id the id to set
     *
     * NOTICE: If need to set multiple id's use: setId(int i, String id)
     *
     */
    public void setId(int i, String id) {
        if (this.idList == null) {
            this.idList = new ArrayList<PatientId>();
        }

        for(int j=idList.size();j<i+1;j++) {
        	idList.add(null);
        }

        PatientId pId = idList.get(i);
        if(pId == null) {
        	pId = new PatientId();
            pId.setExtension(id);
            idList.set(i, pId);
        } else {
            pId.setExtension(id);
        }
    }

    /**
     * A shortcut method for backward compatibility with XCPD server version
     * 0.2.0
     *
     * NOTICE: If need to use multiple use getHomeCommunityId(int i)
     *
     * @return he root part of the first patient ID.
     */
    public String getHomeCommunityId() {
    	return getHomeCommunityId(0);
    }

    /**
     * A shortcut method for backward compatibility with XCPD server version
     * 0.2.0
     *
     * @param	i index number
     *
     * @return he root part of the first patient ID.
     */
    public String getHomeCommunityId(int i) {
        if (idList != null && !idList.isEmpty() && idList.size() > i) {
            return idList.get(i).getRoot();
        }
        return null;
    }
    
    /**
     * @param homeCommunityId the homeCommunityId to set
     *
     * NOTICE: If need to use multiple use setHomeCommunityId(int i, String homeCommunityId)
     */
    public void setHomeCommunityId(String homeCommunityId) {
    	setHomeCommunityId(0, homeCommunityId);
    }

    /**
     * @param homeCommunityId the homeCommunityId to set
     * @param i index number
     */
    public void setHomeCommunityId(int i, String homeCommunityId) {
        if (this.idList == null) {
            this.idList = new ArrayList<PatientId>();
        }

        for(int j=idList.size();j<i+1;j++) {
        	idList.add(null);
        }

        PatientId pId = idList.get(i);
        if(pId == null) {
        	pId = new PatientId();
            pId.setRoot(homeCommunityId);
            idList.set(i, pId);
        } else {
            pId.setRoot(homeCommunityId);
        }
    }

    /**
     * @return the birthDate
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate the birthDate to set
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @return the administrativeGender
     */
    public Gender getAdministrativeGender() {
        return administrativeGender;
    }

    /**
     * @param administrativeGender the administrativeGender to set
     */
    public void setAdministrativeGender(Gender administrativeGender) {
        this.administrativeGender = administrativeGender;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the streetAddress
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * @param streetAddress the streetAddress to set
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) throws ParseException {
    	Matcher m = telephonePattern.matcher(telephone);
    	if(m.matches()) {
        	this.telephone = telephone;
    	} else {
    		throw new ParseException("Parsing failed for '" + telephone + "' using regexp '" + telephonePattern.pattern() + "'", 0);
    	}
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) throws ParseException {
    	Matcher m = emailPattern.matcher(email);
    	if(m.matches()) {
            this.email = email;
    	} else {
    		throw new ParseException("Parsing failed for '" + email + "' using regexp '" + emailPattern.pattern() + "'", 0);
    	}
    }

    /**
     * @return the idList
     */
    public List<PatientId> getIdList() {
        return idList;
    }

    /**
     * @param idList the idList to set
     */
    public void setIdList(List<PatientId> idList) {
        this.idList = idList;
    }

	@Override
	public String toString() {
		return "PatientDemographics [familyName=" + familyName + ", givenName="
				+ givenName + ", birthDate=" + birthDate
				+ ", administrativeGender=" + administrativeGender + ", city="
				+ city + ", country=" + country + ", postalCode=" + postalCode
				+ ", streetAddress=" + streetAddress + ", telephone="
				+ telephone + ", email=" + email + ", idList=" + idList + "]";
	}
}
