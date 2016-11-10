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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.itextpdf.text.DocumentException;

public class PdfHandlerTest {
	
	@Test
	public void testConvertStringToPdf() {
		byte[] bytes = null;
        try {
        	String testHtml = FileUtils.readFileToString(new File("src/main/java/se/sb/epsos/web/pages/ViewDispensationPage.html"));
        	bytes = PdfHandler.convertStringToPdf(testHtml);
            File testResourceFolder = new File("src/test/resources/pdf");
            testResourceFolder.mkdirs();
            File file = new File(testResourceFolder, "File.pdf");
            FileOutputStream pdfFileOs = new FileOutputStream(file);
            pdfFileOs.write(bytes);
            pdfFileOs.flush();
            pdfFileOs.close();
            
            assert(file.exists());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

        assert(bytes!=null);
	}
}
