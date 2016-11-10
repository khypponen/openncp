/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class PdfHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(PdfHandler.class);

	public static byte[] convertStringToPdf(String htmlString) throws DocumentException, IOException {
		File file = new File("css");
		LOGGER.info(file.toURI().toURL().toString());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		Document document = new Document();
	    PdfWriter writer = PdfWriter.getInstance(document, outputStream);
	    writer.setPageEvent( new PdfHeaderFooterHandler() );
	    
	    document.open();
	    InputStream isHtml = new ByteArrayInputStream(htmlString.getBytes());
	    InputStream isCss = new ByteArrayInputStream(htmlString.getBytes());
	    
	    XMLWorkerHelper.getInstance().parseXHtml(writer, document, isHtml, isCss);
	    document.close();

	    return outputStream.toByteArray();	
	}
}
