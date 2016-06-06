/**
 * Combined interface for Patient Summary and ePrescription XCA Service
 * implementation.
 *
 * Patient summary part by SRDC Copyright (C) 2011, 2012 SRDC Yazilim Arastirma
 * ve Gelistirme ve Danismanlik Tic. Ltd. Sti. <epsos@srdc.com.tr>
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
 *
 * ePrescription part by Kela (The Social Insurance Institution of Finland) GNU
 * General Public License v3
 *
 * Corrections/updates by other epSOS project participants
 */
package eu.epsos.protocolterminators.ws.server.xca;

import eu.epsos.protocolterminators.ws.server.common.NationalConnectorInterface;
import eu.epsos.protocolterminators.ws.server.exception.NIException;
import fi.kela.se.epsos.data.model.DocumentAssociation;
import fi.kela.se.epsos.data.model.EPDocumentMetaData;
import fi.kela.se.epsos.data.model.EPSOSDocument;
import fi.kela.se.epsos.data.model.MroDocumentMetaData;
import fi.kela.se.epsos.data.model.PSDocumentMetaData;
import fi.kela.se.epsos.data.model.SearchCriteria;
import java.util.List;
import tr.com.srdc.epsos.securityman.exceptions.InsufficientRightsException;

/**
 * Combined interface for Patient Summary and ePrescription XCA Service
 * implementation. Implementations of the interface in the countries supporting
 * only eP or only PS should throw UnsupportedOperationException on the missing
 * methods.
 */
public interface DocumentSearchInterface extends NationalConnectorInterface {

    /**
     * This method returns one DocumentAssociation (with PSDocumentMetaData in
     * XML and/or PDF format) that matches the searchCriteria.
     *
     * @param searchCriteria (see SearchCriteria interface for more info)
     * @return DocumentAssociation<PSDocumentMetaData>
     */
    public DocumentAssociation<PSDocumentMetaData> getPSDocumentList(SearchCriteria searchCriteria) throws NIException, InsufficientRightsException;

    /**
     * This method returns one DocumentAssociation (with MroDocumentMetaData in
     * XML and/or PDF format) that matches the searchCriteria.
     *
     * @param searchCriteria (see SearchCriteria interface for more info)
     * @return DocumentAssociation<MroDocumentMetaData>
     */
    public DocumentAssociation<MroDocumentMetaData> getMroDocumentList(SearchCriteria searchCriteria) throws NIException, InsufficientRightsException;

    /**
     * This method returns one/several DocumentAssociation(s)
     * (EPDocumentMetaData in XML and/or PDF format) that matches the
     * searchCriteria.
     *
     * @param searchCriteria (see SearchCriteria interface for more info)
     * @return List<DocumentAssociation<EPDocumentMetaData>>
     */
    public List<DocumentAssociation<EPDocumentMetaData>> getEPDocumentList(SearchCriteria searchCriteria) throws NIException, InsufficientRightsException;

    /**
     * This method returns one EPSOSDocument which includes document metaData
     * and the DOM document itself matching the searchCriteria. The
     * searchCriteria shall have PatientId and DocumentId as mandatory fields
     * filled.
     *
     * @param searchCriteria (see SearchCriteria interface for more info)
     * @return EPSOSDocument
     */
    public EPSOSDocument getDocument(SearchCriteria searchCriteria) throws NIException, InsufficientRightsException;
}
