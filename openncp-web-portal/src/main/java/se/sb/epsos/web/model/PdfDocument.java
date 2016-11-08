/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.model;

import hl7OrgV3.ClinicalDocumentDocument1;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.shelob.ws.client.jaxws.EpsosDocument;
import se.sb.epsos.web.service.MetaDocument;
import sun.misc.BASE64Decoder;

public class PdfDocument extends CdaDocument {
	private static final long serialVersionUID = -84248995964958326L;
        private static final Logger LOGGER = LoggerFactory.getLogger(PdfDocument.class);
	private ClinicalDocumentDocument1 doc;
	
	public PdfDocument(MetaDocument metaDoc, byte[] bytes, EpsosDocument epsosDocument) throws XmlException {
        super(metaDoc, bytes, epsosDocument);
        try {
            ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.parse(new ByteArrayInputStream(bytes));	
            this.doc = doc;
        } catch (IOException ex) {
           throw new XmlException("Failed to parse CDA", ex);
        }
}
	
	public byte[] getPdf() {
            byte[] pdfBytes = null;
            try {
                pdfBytes = new BASE64Decoder().decodeBuffer(doc.getClinicalDocument().getComponent().getNonXMLBody().getText().newCursor().getTextValue());
            } catch (IOException ex) {
                LOGGER.error("Failed to base64 decode PDF",ex);
            }
            return pdfBytes;
	}
}
