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
public class DocumentClientDtoCacheKey implements Serializable {
	private static final long serialVersionUID = 8619078236148769223L;
	private String sessionId;
    private String patientId;
    private String documentId;
    
    public DocumentClientDtoCacheKey(String sessionId, String patientId, String documentId) {
        this.sessionId = sessionId;
        this.patientId = patientId;
        this.documentId = documentId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getPatientId() {
        return patientId;
    }
    
    public String getDocumentId() {
        return documentId;
    }

    @Override
    public int hashCode() {
        return (sessionId + patientId +documentId).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if ( !(o instanceof DocumentClientDtoCacheKey) ) return false;
        DocumentClientDtoCacheKey key = (DocumentClientDtoCacheKey) o;
        boolean test = EpsosStringUtils.nullSafeCompare(this.sessionId, key.getSessionId());
        test = test & EpsosStringUtils.nullSafeCompare(this.patientId, key.getPatientId());
        test = test & EpsosStringUtils.nullSafeCompare(this.documentId, key.getDocumentId());
        return test;
    }

    @Override
    public String toString() {
        return "{" + this.sessionId + ", " + this.patientId + ", " + this.documentId + "}";
    }
}
