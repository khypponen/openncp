/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/
package se.sb.epsos.web.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.shelob.ws.client.jaxws.EpsosDocument;
import se.sb.epsos.web.model.DocType;


public class MetaDocument implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MetaDocument.class);
	private DocumentClientDtoCacheKey dtoCacheKey;
	private EpsosDocument doc;

	public MetaDocument(DocumentClientDtoCacheKey cacheKey, boolean forceCacheOverride) {
		super();
		this.dtoCacheKey = cacheKey;
		this.doc = new EpsosDocument();
		this.doc.setUuid(cacheKey.getDocumentId());
		if (DocumentCache.getInstance().contains(dtoCacheKey) && forceCacheOverride) {
			DocumentCache.getInstance().flush(dtoCacheKey);
		}
		DocumentCache.getInstance().put(this.dtoCacheKey, this);
    }

	public MetaDocument(String sessionId, String patientId, EpsosDocument doc) {
		this(sessionId, patientId, doc, false);
	}

	public MetaDocument(String sessionId, String patientId, EpsosDocument doc, boolean forceCacheOverride) {
		this(new DocumentClientDtoCacheKey(sessionId, patientId, doc.getUuid()), forceCacheOverride);
		this.doc = doc;
	}

	public DocType getType() {
		if (doc != null) {
			return DocType.getFromFormatCode(doc.getFormatCode().getNodeRepresentation());
		}
		return null;
	}

	public String getDescription() {
		if (doc != null && doc.getDescription() != null) {
			return doc.getDescription();
		}
		return null;
	}

	public String getTitle() {
		if (doc != null && doc.getTitle() != null) {
			return doc.getTitle();
		}
		return null;
	}

	public String getAuthor() {
		if (doc != null && doc.getAuthor() != null) {
			return doc.getAuthor();
		}
		return null;
	}

	public Date getCreationdate() {
		if (doc != null && doc.getCreationDate() != null) {
			LOGGER.debug("Creationdate: " + doc.getCreationDate());
			return doc.getCreationDate().toGregorianCalendar().getTime();
		}
		return null;
	}

	public EpsosDocument getDoc() {
		return doc;
	}

	public DocumentClientDtoCacheKey getDtoCacheKey() {
		return dtoCacheKey;
	}

    public EpsosDocument getChildDocumentPdf() {
        List<EpsosDocument> associatedDocuments = doc.getAssociatedDocuments();
        if (associatedDocuments != null && associatedDocuments.size() > 0) {
            for (EpsosDocument epsosDocument : associatedDocuments) {
                if (DocType.PDF.formatCode.equals(epsosDocument.getFormatCode().getNodeRepresentation())) {
                    return epsosDocument;
                }
            }
        }
        return null;
    }
}
