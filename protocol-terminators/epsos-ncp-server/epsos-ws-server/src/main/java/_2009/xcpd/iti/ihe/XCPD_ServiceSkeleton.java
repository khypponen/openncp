/**
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
 */
/**
 * XCPD_ServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 */
package _2009.xcpd.iti.ihe;

import org.apache.axiom.soap.SOAPHeader;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

import tr.com.srdc.epsos.ws.server.xcpd.impl.XCPDServiceImpl;

import epsos.ccd.gnomon.auditmanager.EventLog;
import eu.epsos.protocolterminators.ws.server.xcpd.XCPDServiceInterface;

/**
 * XCPD_ServiceSkeleton java skeleton for the axisService
 */
public class XCPD_ServiceSkeleton {

	private XCPDServiceInterface service = null;

	public XCPD_ServiceSkeleton() {
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param pRPA_IN201305UV02
	 */

	public PRPAIN201306UV02 respondingGateway_PRPA_IN201305UV02(
			PRPAIN201305UV02 pRPA_IN201305UV02, SOAPHeader sh, EventLog eventLog) throws Exception {

		if (service == null) {
			service = new XCPDServiceImpl();
		}

		return service.queryPatient(pRPA_IN201305UV02, sh, eventLog);
	}
}
