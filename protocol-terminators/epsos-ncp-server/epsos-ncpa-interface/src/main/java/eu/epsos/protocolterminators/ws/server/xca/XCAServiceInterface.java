/**
 * Combined interface for Patient Summary and ePrescription XCA Service implementation.
 * 
 * Patient summary part by SRDC
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * 
 * This file is part of SRDC epSOS NCP.
 * 
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SRDC epSOS NCP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 * 
 * ePrescription part by Kela (The Social Insurance Institution of Finland)
 * GNU General Public License v3
 * 
 * Corrections/updates by other epSOS project participants 
 */
package eu.epsos.protocolterminators.ws.server.xca;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPHeader;

import epsos.ccd.gnomon.auditmanager.EventLog;

public interface XCAServiceInterface {

	public AdhocQueryResponse queryDocument(AdhocQueryRequest pRPA_IN201305UV02, SOAPHeader sh, EventLog eventLog) throws Exception;
	
	public void retrieveDocument(RetrieveDocumentSetRequestType pRPA_IN201305UV02, SOAPHeader sh, EventLog eventLog, OMElement response) throws Exception;
	
}
