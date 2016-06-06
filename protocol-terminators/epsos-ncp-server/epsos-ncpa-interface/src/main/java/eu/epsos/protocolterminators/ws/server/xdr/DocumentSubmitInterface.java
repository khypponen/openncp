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

import tr.com.srdc.epsos.securityman.exceptions.InsufficientRightsException;
import eu.epsos.protocolterminators.ws.server.common.NationalConnectorInterface;
import eu.epsos.protocolterminators.ws.server.exception.NIException;
import fi.kela.se.epsos.data.model.ConsentDocumentMetaData;
import fi.kela.se.epsos.data.model.DocumentAssociation;
import fi.kela.se.epsos.data.model.EDDocumentMetaData;
import fi.kela.se.epsos.data.model.EPSOSDocument;

/**
 * Interface for XDR document submit service implementation
 */
public interface DocumentSubmitInterface extends NationalConnectorInterface {
	/**
	 * Stores a dispensation in the national infrastructure
	 * @param dispensation eDispensation document in epSOS pivot (CDA) form
	 */
	public void submitDispensation(EPSOSDocument dispensationDocument) throws NIException, InsufficientRightsException;
	
	/**
	 * Discards a previously submitted dispensation
	 * @param dispensationToDiscard Metadata of the dispensation to be discarded (XML and PDF versions)		
	 */
	public void cancelDispensation(DocumentAssociation<EDDocumentMetaData> dispensationToDiscard) throws NIException, InsufficientRightsException;
	
	/**
	 * Stores a patient consent in the national infrastructure
	 * @param patient consent document in epSOS pivot (CDA) form
	 */
	public void submitPatientConsent(EPSOSDocument consentDocument) throws NIException, InsufficientRightsException;

	/**
	 * Discards a previously submitted consent
	 * @param consentToDiscard Metadata of the consent to be discarded (XML and PDF versions)	
	 */
	public void cancelConsent(DocumentAssociation<ConsentDocumentMetaData> consentToDiscard) throws NIException, InsufficientRightsException;

	/**
	 * Stores a HCER document in the national infrastructure
	 * @param HCER document in epSOS pivot (CDA) form
	 */
	public void submitHCER(EPSOSDocument hcerDocument) throws DocumentProcessingException;
	
}
