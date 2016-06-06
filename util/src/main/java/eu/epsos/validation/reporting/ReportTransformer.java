/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2013 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.validation.reporting;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class ReportTransformer {

    private String validationResult;
    private String validatedObject;
    private static final String XSL_FILE = "xsl//resultStylesheet.xsl";

    public ReportTransformer(String validationResult, String validatedObject) {
	this.validationResult = validationResult;
	this.validatedObject = validatedObject;
    }

    public String getHtmlReport() {
	ClassLoader loader = getClass().getClassLoader();
	InputStream inputStream = loader.getResourceAsStream(XSL_FILE);
	Source xsl = new StreamSource(inputStream);
	
	StringReader reader = new StringReader(validationResult);
	Source in = new StreamSource(reader);
	
	StringWriter writer = new StringWriter();	
	StreamResult out = new StreamResult(writer);
	
	TransformerFactory factory = TransformerFactory.newInstance();
	try {
	    Transformer transformer = factory.newTransformer(xsl);
	    transformer.transform(in, out);
	} catch (TransformerException e) {
	    e.printStackTrace();
	}
	
	return writer.toString();
    }
}
