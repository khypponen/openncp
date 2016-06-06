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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import eu.epsos.validation.ValidationTestBase;

public class ReportTransformerTest extends ValidationTestBase {

    private String validationResult;
    private String validatedObject;
    
//  @Before
    public void setUp() throws Exception {
	validationResult = getResource("validationResult.xml");
	validatedObject = getResource("validatedObject.xml");
    }

//  @Test
    public void testGetHtmlReport() {
	ReportTransformer rt = new ReportTransformer(validationResult, validatedObject); 
	String html = rt.getHtmlReport();
	System.out.println(html);
    }
}
