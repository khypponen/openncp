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
public class TrcAsDoctorTest extends EpsosWebIntegrationTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrcAsDoctorTest.class);

    @Override
    public void setUp() throws Exception {
        super.setUp();
        enterLoginCredentials("doktor", "1234");
        verifyQueryPersonSuccess(Roles.DOCTOR);
    }
    
    @Test
    public void testTrcSuccess_PERSON_DETAILS_NO_TRC_NEEDED() {
       LOGGER.info("Starting test:::testTrcSuccess_PERSON_DETAILS()");
       clickOnElement(driver.findElement(By.linkText(getProp("person.actions.details"))));
       selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", "3000"); 
       assertTrue(selenium.isElementPresent("css=div.wicket-modal"));
       clickOnElement(driver.findElement(By.className("w_close")));
       selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", "3000"); 
       assertFalse(selenium.isElementPresent("css=div.wicket-modal"));
       LOGGER.info("Finished test:::testTrcSuccess_PERSON_DETAILS()");
    }
    
    @Test
    public void testTrcSuccess_CONFIRM() {
    	LOGGER.info("Starting test:::testTrcSuccess_CONFIRM()");
    	clickOnElement(driver.findElement(By.linkText(getProp("person.actions.patientsummary"))));
    	selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", "3000"); 
    	clickOnElement(driver.findElement(By.name("confirmButton")));
    	assertTrue(waitForTextPresent("PS.pagetitle", 30000));
    	LOGGER.info("Finished test:::testTrcSuccess_CONFIRM()");
    }
}
