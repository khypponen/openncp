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

import org.junit.Test;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.integrationtest.EpsosWebIntegrationTestBase;

public class LoginTest extends EpsosWebIntegrationTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginTest.class);

	@Test
    public void testApplicationIsResponding()  throws Exception {
        LOGGER.info("Starting test:::testApplicationIsResponding()");
        selenium.open("/");
        selenium.waitForPageToLoad("3000");
        assertTrue(selenium.isElementPresent("epsos"));
        assertTrue(selenium.isElementPresent("userinfo"));
        assertTrue(selenium.isElementPresent("loginForm"));
        assertTrue(selenium.isTextPresent(getProp("message.notloggedin")));
        LOGGER.info("Finished test:::testApplicationIsResponding()");
    }

    @Test
    public void testLoginSuccess_PHARMACIST()  throws Exception {
        LOGGER.info("Starting test:::testLoginSuccess_PHARMACIST()");
        enterLoginCredentials("apotek", "1234");
        assertSuccessfulLogin("Apo Tekare");
        LOGGER.info("Finished test:::testLoginSuccess_PHARMACIST()");
    }

    @Test
    public void testLoginSuccess_DOCTOR() throws Exception {
        LOGGER.info("Starting test:::testLoginSuccess_DOCTOR()");
        enterLoginCredentials("doktor", "1234");
        assertSuccessfulLogin("Doktor Kosmos");
        LOGGER.info("Finished test:::testLoginSuccess_DOCTOR()");
    }
    
    @Test
    public void testLoginSuccess_NURSE()  throws Exception {
        LOGGER.info("Starting test:::testLoginSuccess_NURSE()");
        enterLoginCredentials("syster", "1234");
        assertSuccessfulLogin("Syster Yster");
        LOGGER.info("Finished test:::testLoginSuccess_NURSE()");
    }
    
    @Test
    public void testLoginFail()  throws Exception {
    	LOGGER.info("Starting test:::testLoginFail()");
    	enterLoginCredentials("hacker", "badpassword");
    	selenium.waitForPageToLoad("3000");
    	assertTrue(selenium.isElementPresent("userinfo"));
        assertTrue(selenium.isElementPresent("loginForm"));
        assertTrue(selenium.isTextPresent(getProp("error.login")));
    	LOGGER.info("Finished test:::testLoginFail()");
    }
    
    private void assertSuccessfulLogin(String name) {
    	if(System.getProperty("os.name").startsWith("Windows")) {
    		selenium.waitForPageToLoad("3000");
    	} else {
    		selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", "3000");
    	}
    	assertTrue(selenium.isTextPresent(name));
        assertTrue(selenium.isElementPresent("commonName"));
        assertTrue(selenium.isElementPresent("organizationName"));
        assertTrue(selenium.isElementPresent("roles"));
        assertTrue(selenium.isElementPresent("role"));
        assertTrue(selenium.isElementPresent("logoutLink"));
    }
    
}
