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
 * XDR_ServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.5  Built on : May 28, 2011 (08:30:56 CEST)
 */
package _2007.xds_b.iti.ihe;

import org.apache.axiom.soap.SOAPHeader;
import org.apache.log4j.Logger;

import tr.com.srdc.epsos.ws.server.xdr.XDRServiceImpl;

import epsos.ccd.gnomon.auditmanager.EventLog;
import eu.epsos.protocolterminators.ws.server.xdr.XDRServiceInterface;

/**
 * XDR_ServiceSkeleton java skeleton for the axisService
 */
public class XDR_ServiceSkeleton {

	private XDRServiceInterface service = null;

	// added by Kela
	public static Logger logger = Logger.getLogger(XCA_ServiceSkeleton.class);

	public XDR_ServiceSkeleton() {
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param provideAndRegisterDocumentSetRequest
	 */
	public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentRecipient_ProvideAndRegisterDocumentSetB(
			ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType provideAndRegisterDocumentSetRequest,
			SOAPHeader sh, EventLog eventLog) throws Exception {
		if (service == null) {
			service = new XDRServiceImpl();
		}

		return service.saveDocument(provideAndRegisterDocumentSetRequest, sh,
				eventLog);
	}
}
