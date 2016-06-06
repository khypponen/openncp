/**
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 Kela (The Social Insurance Institution of Finland)
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
 * Contact email: epsos@kanta.fi or Konstantin.Hypponen@kela.fi
 */
package eu.epsos.protocolterminators.ws.server.xdr;

import org.apache.axiom.soap.SOAPHeader;

import epsos.ccd.gnomon.auditmanager.EventLog;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public interface XDRServiceInterface {
	
	public RegistryResponseType saveDocument(ProvideAndRegisterDocumentSetRequestType request, SOAPHeader sh, EventLog eventLog) throws Exception;

}
