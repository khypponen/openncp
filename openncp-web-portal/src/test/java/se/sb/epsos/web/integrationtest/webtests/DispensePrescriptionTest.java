/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.integrationtest.webtests;

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
public class DispensePrescriptionTest extends EpsosWebIntegrationTestBase {

	@Before
	public void prepare() throws Exception {
		if (!System.getProperty("os.name").startsWith("Windows")) {
			enterLoginCredentials("apotek", "1234");
			verifyQueryPersonSuccess(Roles.PHARMACIST);
			clickOnElement(driver.findElement(By.linkText(getProp("person.actions.prescriptions"))));
			selenium.waitForPageToLoad("3000");
			acceptTrc();
		}
	}

	@Test
	@Ignore
	public void testDispensePrescriptionPageIsAvailable() throws Exception {
		if (!System.getProperty("os.name").startsWith("Windows")) {
			clickDispensePrescription();
			assertTrue(selenium.isTextPresent(getProp("dispensation.prescriptioninfo")));
		}
	}

	@Test
	@Ignore
	public void testDispensePrescriptionAfterBackButton() throws Exception {
		if (!System.getProperty("os.name").startsWith("Windows")) {
			clickDispensePrescription();
			selenium.goBack();

			selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", "3000");

			clickDispensePrescription();
			assertTrue(selenium.isTextPresent(getProp("dispensation.prescriptioninfo")));
		}
	}

}
