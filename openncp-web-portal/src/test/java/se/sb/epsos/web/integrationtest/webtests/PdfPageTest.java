/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sb.epsos.web.integrationtest.webtests;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import se.sb.epsos.web.integrationtest.EpsosWebIntegrationTestBase;

/**
 * 
 * @author danielgronberg
 */
public class PdfPageTest extends EpsosWebIntegrationTestBase {

	@Test
	@Ignore
	public void testPdfPageShowOriginalEP() throws Exception {
		if (!System.getProperty("os.name").startsWith("Windows")) {
			enterLoginCredentials("apotek", "1234");
			verifyQueryPersonSuccess(Roles.PHARMACIST);
			clickOnElement(driver.findElement(By.linkText("Recept")));
			selenium.waitForPageToLoad("3000");
			acceptTrc();
			clickDispensePrescription();
			clickOnElement(driver.findElement(By
					.linkText("Visa Originalrecept")));
			selenium.waitForPageToLoad("3000");
			assertTrue(selenium.isTextPresent("Original"));
		}
	}

	@Test
	@Ignore
	public void testPdfPageShowOriginalEPAfterBackButton() throws Exception {
		if (!System.getProperty("os.name").startsWith("Windows")) {
			testPdfPageShowOriginalEP();
			selenium.goBack();
			clickOnElement(driver.findElement(By
					.linkText("Visa Originalrecept")));
			selenium.waitForPageToLoad("3000");
			assertTrue(selenium.isTextPresent("Original"));
		}
	}

	@Test
	@Ignore
	public void testPdfPageShowOriginalPS() throws Exception {
            if (!System.getProperty("os.name").startsWith("Windows")) {
		enterLoginCredentials("doktor", "1234");
		verifyQueryPersonSuccess(Roles.DOCTOR);
		clickOnElement(driver.findElement(By.linkText("Patientöversikt")));
		selenium.waitForPageToLoad("3000");
		acceptTrc();
		clickOnElement(driver.findElement(By.linkText("Visa original")));
		selenium.waitForPageToLoad("3000");
		assertTrue(selenium.isTextPresent("Original"));
            }
	}

	@Test
	@Ignore
	public void testPdfPageShowOriginalPSAfterBackButton() throws Exception {
            if (!System.getProperty("os.name").startsWith("Windows")) {
		testPdfPageShowOriginalPS();
		selenium.goBack();
		clickOnElement(driver.findElement(By.linkText("Visa original")));
		selenium.waitForPageToLoad("3000");
		assertTrue(selenium.isTextPresent("Original"));
            }
	}
}
