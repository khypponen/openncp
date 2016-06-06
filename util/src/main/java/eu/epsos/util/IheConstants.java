/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012  SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.util;

/**
 * Holds multiple IHE Constants.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public final class IheConstants {

    public static final String FORMAT_CODE_SCHEME = "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d";
    public static final String TYPE_CODE_SCHEME = "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983";
    
    public static final String REGREP_RESPONSE_SUCCESS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    public static final String REGREP_RESPONSE_PARTIALSUCCESS = "urn:ihe:iti:2007:ResponseStatusType:PartialSuccess";
    public static final String REGREP_RESPONSE_FAILURE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String REGREP_STATUSTYPE_APPROVED = "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved";
    
    public static final String ClASSCODE_SCHEME = "2.16.840.1.113883.6.1";

    public static final String DISPENSATION_FORMATCODE_DISPLAYNAME="epSOS coded eDispensation";
    public static final String DISPENSATION_FORMATCODE_NODEREPRESENTATION="urn:epsos:ep:dis:2010";
    public static final String DISPENSATION_FORMATCODE_CODINGSCHEMA="epSOS formatCodes";
        
    
    public static final String CONSENT_FORMATCODE_DISPLAYNAME="Consent";
    public static final String CONSENT_FORMATCODE_NODEREPRESENTATION="urn:ihe:iti:bppc:2007";
    public static final String CONSENT_FORMATCODE_CODINGSCHEMA="2.16.840.1.113883.6.1";
    
    
    public class SOAP_HEADERS {

        public static final String SECURITY_XSD = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    }
}
