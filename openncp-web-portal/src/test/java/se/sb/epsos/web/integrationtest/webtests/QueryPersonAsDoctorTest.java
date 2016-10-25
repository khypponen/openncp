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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.integrationtest.EpsosWebIntegrationTestBase;

/**
 * 
 * @author andreas
 */
public class QueryPersonAsDoctorTest extends EpsosWebIntegrationTestBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryPersonAsDoctorTest.class);
	

	@Override
	public void setUp() throws Exception {
		super.setUp();
		enterLoginCredentials("doktor", "1234");
	}

	@Test
	public void testQueryPersonSuccess_PAGE_IS_AVAILABLE() {
		LOGGER.info("Starting test:::testQueryPersonSuccess_PAGE_IS_AVAILABLE()");
		selenium.waitForPageToLoad("3000");
		assertTrue(selenium.isTextPresent(getProp("queryPersonPage.title")));
		LOGGER.info("Finished test:::testQueryPersonSuccess_PAGE_IS_AVAILABLE()");
	}

	@Test
	public void testQueryPersonSuccess_COUNTRY_SELECT() {
		assertQueryPageCountries();
	}

	@Test
	public void testQueryPersonFailure_INVALID_PATIENT_ID() {
		validateQueryPageInvalidPersonId();
	}

	@Test
	public void testQueryPersonSuccess_QUERY_PATIENT() {
		verifyQueryPersonSuccess(Roles.DOCTOR);
	}

	@Test
	public void testQueryPersonSuccess_PERSON_DETAILS() {
		LOGGER.info("Starting test:::testQueryPersonSuccess_PERSON_DETAILS()");
		testQueryPersonSuccess_QUERY_PATIENT();
		clickOnElement(driver.findElement(By.linkText(getProp("person.actions.details"))));
		selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", "3000");
		assertTrue(selenium.isElementPresent("css=div.wicket-modal"));
		clickOnElement(driver.findElement(By.className("w_close")));
		selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", "3000");
		assertFalse(selenium.isElementPresent("css=div.wicket-modal"));
		LOGGER.info("Finished test:::testQueryPersonSuccess_PERSON_DETAILS()");
	}

	@Test
	public void testQueryPersonSuccess_PATIENT_SUMMARY() {
		LOGGER.info("Starting test:::testQueryPersonSuccess_PATIENT_SUMMARY()");
		verifyQueryPersonSuccess(Roles.DOCTOR);
		clickOnElement(driver.findElement(By.linkText(getProp("person.actions.patientsummary"))));
		selenium.waitForPageToLoad("3000");
		assertTrue(selenium.isTextPresent(getProp("trc.tile")));
		LOGGER.info("Finished test:::testQueryPersonSuccess_PATIENT_SUMMARY()");
	}
        
    @Test
    public void testQueryPersonSuccessPSAfterBackButton(){
        LOGGER.info("Starting test:::testQueryPersonSuccess_PATIENT_SUMMARY()");
		verifyQueryPersonSuccess(Roles.DOCTOR);
		clickOnElement(driver.findElement(By.linkText(getProp("person.actions.patientsummary"))));
		selenium.waitForPageToLoad("3000");
		assertTrue(selenium.isTextPresent(getProp("trc.tile")));
        selenium.goBack();
        selenium.select("id=country", "label=" + getProp("country.DK"));
		selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", "3000");
        verifyQueryPersonSuccess(Roles.DOCTOR);
        clickOnElement(driver.findElement(By.linkText(getProp("person.actions.patientsummary"))));
		selenium.waitForPageToLoad("3000");
		assertTrue(selenium.isTextPresent(getProp("trc.tile")));
    }
}
