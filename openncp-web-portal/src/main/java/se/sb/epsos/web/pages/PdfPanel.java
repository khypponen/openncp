/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.pages;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebResponse;

import se.sb.epsos.web.model.LoadableDocumentModel;
import se.sb.epsos.web.model.PdfDocument;
import se.sb.epsos.web.util.DynamicResource;

public class PdfPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public PdfPanel(String id, final PdfDocument pdfDocument) {
		super(id);
		DynamicResource dynResource = new DynamicResource(){
			private static final long serialVersionUID = 1L;

			@Override
			protected void setHeaders(WebResponse response) {
				super.setHeaders(response);
				response.setHeader("Cache-Control", "private"); 
			}
			
		};
		dynResource.setDocument(new LoadableDocumentModel<PdfDocument>(pdfDocument));
		setRenderBodyOnly(true);
		add(new DocumentInlineIFrame("pdfDoc", dynResource));
	}
}