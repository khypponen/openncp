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
package se.sb.epsos.web.service;

import java.io.Serializable;

import se.sb.epsos.web.util.EpsosStringUtils;

/**
 *
 * @author andreas
 */
public class PersonCacheKey implements Serializable {
	private static final long serialVersionUID = 7639120662338895244L;
	private String sessionId;
    private String personId;

    public PersonCacheKey(String sessionId, String personId) {
        this.sessionId = sessionId;
        this.personId = personId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "{" + this.sessionId + ", " + this.personId + "}";
    }
    
    @Override
    public int hashCode() {
        return (sessionId + personId).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if ( !(o instanceof PersonCacheKey) ) return false;
        PersonCacheKey key = (PersonCacheKey) o;
        boolean test = EpsosStringUtils.nullSafeCompare(this.sessionId, key.getSessionId());
        test = test & EpsosStringUtils.nullSafeCompare(this.personId, key.getPersonId());
        return test;
    }
}
