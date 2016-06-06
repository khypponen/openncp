/*
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. epsos@srdc.com.tr
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
 * SRDC epSOS NCP. If not, see http://www.gnu.org/licenses/.
 */
package tr.com.srdc.epsos.ws.xca.client.retrieve;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import tr.com.srdc.epsos.util.Constants;

public class RetrieveDocumentSetRequestTypeCreator {

    public RetrieveDocumentSetRequestType createRetrieveDocumentSetRequestType(String documentId, String homeCommunityId, String repositoryUniqId) {
        RetrieveDocumentSetRequestType rdsrt = new RetrieveDocumentSetRequestType();

        // Create DocumentRequest
        DocumentRequest dr = new DocumentRequest();

        // Check for OID prefix, and adds it if not present (The OID prefix is required, as present in ITI TF-2b: 3.38.4.1.2.1);
        if (!homeCommunityId.startsWith(Constants.OID_PREFIX)) {
            homeCommunityId = Constants.OID_PREFIX + homeCommunityId;
        }

        // Set DocumentRequest/HomeCommunityId
        dr.setHomeCommunityId(homeCommunityId);

        // Set DocumentRequest/RepositoryUniqueId
        dr.setRepositoryUniqueId(repositoryUniqId);

        // Set DocumentRequest/DocumentUniqueId
        dr.setDocumentUniqueId(documentId);

        rdsrt.getDocumentRequest().add(dr);

        return rdsrt;
    }
}
