/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.auth;

import java.net.URL;

import org.apache.wicket.Localizer;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.jaas.JaasGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sb.epsos.web.EpsosWebApplication;
import se.sb.epsos.web.service.NcpServiceConfigManager;
import se.sb.epsos.web.service.NcpServiceFacadeImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-application-context.xml" })
public class LoginPageTest  {
	private WicketTester tester;
    Localizer localizer;

	@Autowired
	private EpsosWebApplication epsosWebApplication;

	@Before
	public void setUp() {
		tester = new WicketTester(epsosWebApplication);
        localizer = tester.getApplication().getResourceSettings().getLocalizer();
    }

	@Test
    @Ignore
    public void testSetupSSL() {
		epsosWebApplication.setSSL();
		assertNotNull(System.getProperty("javax.net.ssl.trustStore"));
		assertNotNull(System.getProperty("javax.net.ssl.trustStorePassword"));
		assertNotNull(System.getProperty("javax.net.ssl.keyStore"));
		assertNotNull(System.getProperty("javax.net.ssl.keyStorePassword"));
		assertNotNull(System.getProperty("javax.net.ssl.key.alias"));
		assertNotNull(System.getProperty("javax.net.ssl.privateKeyPassword"));
    }

	@Test
	public void testLoginPageRendersSuccessfully() {
		//start and render the test page
		tester.startPage(LoginPage.class);
		//assert promt for login
		tester.assertRenderedPage(LoginPage.class);
	}

	@Test
	public void testLoginSucess() {
		//start and render the test page
		tester.startPage(LoginPage.class);
		// perform login
		FormTester formTester = tester.newFormTester("loginForm");
		formTester.setValue("username", "doktor");
		formTester.setValue("password", "1234");
		tester.executeAjaxEvent("loginForm:submitButton", "onclick");
		//assert home page is rendered
		tester.assertRenderedPage(epsosWebApplication.getHomePage());
	}

	@Test
	public void testLogout() {
		testLoginSucess();
		try {
			tester.clickLink("userinfo:logoutLink");
		} catch (Exception e) {
			assertTrue(e instanceof RestartResponseAtInterceptPageException);
		}
	}

	@Test
	public void testLoginFailure() {
		//start and render the test page
		tester.startPage(LoginPage.class);
		// perform login
		FormTester formTester = tester.newFormTester("loginForm");
		formTester.setValue("username", "doktor");
		formTester.setValue("password", "xxxx");
		tester.executeAjaxEvent("loginForm:submitButton", "onclick");
		//assert home page is rendered
		tester.assertRenderedPage(LoginPage.class);
		tester.assertErrorMessages(new String[] { localizer.getString("error.login", null) });
	}
}
