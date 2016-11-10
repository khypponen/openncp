/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.livenesstest;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.integrationtest.EpsosWebIntegrationTestBase;

public abstract class AbstractLivenessTest extends EpsosWebIntegrationTestBase {

    private String timeToWait = "30000";
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLivenessTest.class);

    @Before
    @Override
    public void setUp() throws Exception {
        this.driver = System.getProperty("os.name").startsWith("Windows") ? new InternetExplorerDriver() : new FirefoxDriver();
        this.targetUrl = "http://usbeta13:8080/epsos-web";
        this.selenium = selenium == null ? new WebDriverBackedSelenium(this.driver, this.targetUrl) : selenium;
    }

    @After
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.close();
        }
    }

    protected void queryPerson(String country, String... idTexts) {
        selenium.waitForPageToLoad(timeToWait);
        selenium.select("id=country", "label=" + country);
        selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", timeToWait);
        assertTrue(selenium.isElementPresent("patientIds:0:idTextField"));
        for (int i = 0; i < idTexts.length; i++) {
            selenium.type("patientIds:" + i + ":idTextField", idTexts[i]);
        }
        clickOnElement(driver.findElement(By.id("submit")));
        selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", timeToWait);
        assertTrue(selenium.isTextPresent("Detaljer"));
    }

    protected void loginAsDoktor() {
        selenium.open("/");
        selenium.waitForPageToLoad("3000");
        selenium.type("username", "doktor");
        selenium.type("password", "1234");
        clickOnElement(driver.findElement(By.name("submitButton")));
        if (System.getProperty("os.name").startsWith("Windows")) {
            selenium.waitForPageToLoad(timeToWait);
        } else {
            selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", timeToWait);
        }
    }
    
    protected void loginAsApotek() {
        selenium.open("/");
        selenium.waitForPageToLoad("3000");
        selenium.type("username", "apotek");
        selenium.type("password", "1234");
        clickOnElement(driver.findElement(By.name("submitButton")));
        if (System.getProperty("os.name").startsWith("Windows")) {
            selenium.waitForPageToLoad(timeToWait);
        } else {
            selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", timeToWait);
        }
    }
    
    protected void showPS() {
        clickOnElement(driver.findElement(By.linkText("Patientöversikt")));
    	selenium.waitForPageToLoad(timeToWait);
    	clickOnElement(driver.findElement(By.name("confirmButton")));
    	if(System.getProperty("os.name").startsWith("Windows")) {
    		selenium.waitForPageToLoad(timeToWait);
    	}
    	else {
    		selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", timeToWait);
    	}      
    	assertTrue(selenium.isTextPresent("Tillgängliga patientöversikter"));
        clickOnElement(driver.findElement(By.linkText("Visa")));
    	if(System.getProperty("os.name").startsWith("Windows")) {
    		selenium.waitForPageToLoad(timeToWait);
    	}
    	else {
    		selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", timeToWait);
    	}     
        assertTrue(selenium.isTextPresent("Patientöversikt"));
    }
    
    protected void showEP() {
        clickOnElement(driver.findElement(By.linkText("Recept")));
    	selenium.waitForPageToLoad(timeToWait);
    	clickOnElement(driver.findElement(By.name("confirmButton")));
    	if(System.getProperty("os.name").startsWith("Windows")) {
    		selenium.waitForPageToLoad(timeToWait);
    	}
    	else {
    		selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", timeToWait);
    	}      
    	assertTrue(selenium.isTextPresent("Tillgängliga recept"));
        clickOnElement(driver.findElement(By.linkText("Expediera")));
    	if(System.getProperty("os.name").startsWith("Windows")) {
    		selenium.waitForPageToLoad(timeToWait);
    	}
    	else {
    		selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().wicketAjaxBusy()", timeToWait);
    	}     
        assertTrue(selenium.isTextPresent("Receptinformation"));
    }
}
