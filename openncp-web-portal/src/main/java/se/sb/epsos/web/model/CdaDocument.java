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

package se.sb.epsos.web.model;

import java.util.ArrayList;
import java.util.List;

import se.sb.epsos.shelob.ws.client.jaxws.EpsosDocument;
import se.sb.epsos.web.service.DocumentClientDtoCacheKey;
import se.sb.epsos.web.service.MetaDocument;


public abstract class CdaDocument extends MetaDocument {
	private static final long serialVersionUID = -7009718766448230201L;

	protected byte[] bytes;
	private List<ErrorFeedback> errorList = new ArrayList<ErrorFeedback>();
    private EpsosDocument epsosDocument;

	public CdaDocument(DocumentClientDtoCacheKey cacheKey) {
		super(cacheKey, true);
	}

	public CdaDocument(MetaDocument metaDoc) {
		super(metaDoc.getDtoCacheKey().getSessionId(), metaDoc.getDtoCacheKey().getPatientId(), metaDoc.getDoc(), true);
	}

    public CdaDocument(MetaDocument metaDoc, byte[] bytes, EpsosDocument epsosDocument) {
        super(metaDoc.getDtoCacheKey().getSessionId(), metaDoc.getDtoCacheKey().getPatientId(), metaDoc.getDoc(), true);
        this.bytes = bytes;
        this.epsosDocument = epsosDocument;
    }

    public byte[] getBytes() {
		return bytes;
	}

	public List<ErrorFeedback> getError() {
		if (!errorList.isEmpty()) {
			errorList.clear();
		}

//		if (!rsp.getPartialErros().isEmpty()) {
//			for (int i = 0; i < rsp.getPartialErros().size(); i++) {
//				ErrorFeedback error = new ErrorFeedback();
//				error.setErrorCode(rsp.getPartialErros().get(i).getErrorCode());
//				error.setErrorMessage(rsp.getPartialErros().get(i).getCodeContext());
//				error.setSeverityType(rsp.getPartialErros().get(i).getSeverity());
//				errorList.add(error);
//			}
//		}
		return errorList;
	}
}
