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
package eu.epsos.util.xcpd;

import eu.epsos.util.IheConstants;

/**
 * Holds all the fixed properties used in the XCPD Profile transactions.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class XCPDConstants {

    public static final String ITS_VERSION = "XML_1.0";
    public static final String HL7_VERSION = "V3PR1";
    public static final String INTERACTION_IDS_NAMESPACE = "2.16.840.1.113883.1.6";
    public static final String PATIENT_DISCOVERY_REQUEST = "PRPA_IN201305UV02";
    public static final String PATIENT_DISCOVERY_RESPONSE = "PRPA_IN201306UV02";
    
    /* Processing Code */
    public static final String PROCESSING_CODE = "P";
    public static final String PROCESSING_MODE_CODE = "T";
    
    public static final String ACCEPT_ACK_CODE = "AL";
    
    public static final String DETERMINER_CODE_INSTANCE = "INSTANCE";
    
    /* Class Code */
    public static final String CLASS_CODE_AGNT = "AGNT";
    public static final String CLASS_CODE_ORG = "ORG";
    public static final String CLASS_CODE_ASSIGNED = "ASSIGNED";

    public static final String HL7_V3_NAMESPACE_URI = "urn:hl7-org:v3";
    
    public class LOG {

        /* ProvideAndRegister */
        public static final String OUTGOING_XCPD_MESSAGE = "Outgoing XCPD request message to NCP-A:";
        public static final String INCOMING_XCPD_MESSAGE = "Incoming XCPD response message from NCP-A:";
    }

    public class CONTROL_ACT_PROCESS {

        public static final String CODE = "PRPA_TE201305UV02";
        public static final String QUERY_BY_PARAMETER_ID_ROOT = "1.263507841149";
        public static final String QUERY_BY_PARAMETER_STATUS_CODE = "new";
        public static final String QUERY_BY_PARAMETER_RESPONSE_MODALITY_CODE = "R";
        public static final String QUERY_BY_PARAMETER_RESPONSE_PRIORITY_CODE = "I";
        public static final String QUERY_BY_PARAMETER_LIVING_SUBJECT_NAME_SEMANTICS = "LivingSubject.name";
    }

    public class SOAP_HEADERS {

        /* Mixed */        
        public static final String REQUEST_ACTION = "urn:hl7-org:v3:PRPA_IN201305UV02:CrossGatewayPatientDiscovery";
        public static final String NAMESPACE_URI = "urn:ihe:iti:xcpd:2009";
        public static final String MUST_UNDERSTAND = "mustUnderstand";
        public static final String OM_NAMESPACE = "http://www.w3.org/2005/08/addressing";   
        public static final String SECURITY_XSD = IheConstants.SOAP_HEADERS.SECURITY_XSD;
        
        /* Request */
        public static final String NAMESPACE_REQUEST_LOCAL_PART = "respondingGateway_PRPA_IN201305UV02";
        
        /* Response */
        //TODO: Insert response hard-coded properties here.
    }
}
