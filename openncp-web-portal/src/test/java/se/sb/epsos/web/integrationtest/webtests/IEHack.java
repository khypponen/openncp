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

import org.junit.Test;

import se.sb.epsos.web.integrationtest.EpsosWebIntegrationTestBase;

/**
 * The purpose of this class is to get the test suite working with Internet Explorer. For some reason, the 
 * submit button on the login page is not clickable the first 3 times when running the suite. This test takes care
 * of these 3 and the rest of the test cases will be able to run. This is kinda ugly but the Internet Explorer driver
 * is not as stable as one could hope.
 * 
 * See:See: http://code.google.com/p/selenium/issues/detail?id=2864
 * 
 * @author Daniel Grönberg
 *
 */
public class IEHack extends EpsosWebIntegrationTestBase {

    @Test
    public void loginAsDoctorFirstTime()  throws Exception {
        enterLoginCredentials("doktor", "1234");
    }
    
    @Test
    public void loginAsDoctorSecondTime() throws Exception {
    	enterLoginCredentials("doktor", "1234");
    }
    
    @Test
    public void loginAsDoctorThirdTime()  throws Exception {
        enterLoginCredentials("doktor", "1234");
    }
    
}
