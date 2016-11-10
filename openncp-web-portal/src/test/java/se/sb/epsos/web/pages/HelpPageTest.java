/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.pages;

import org.apache.wicket.Localizer;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.sb.epsos.web.EpsosWebApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-application-context.xml" })
public class HelpPageTest {
	private WicketTester tester;
        private Localizer localizer;
	
	@Autowired
	private EpsosWebApplication epsosWebApplication;
	
	@Before
	public void setUp() {
		tester = new WicketTester(epsosWebApplication);
                localizer = tester.getApplication().getResourceSettings().getLocalizer();
        }
	
	@Test
	public void helpPageRender() {
		tester.startPage(HelpPage.class);
		tester.assertRenderedPage(HelpPage.class);
		tester.assertContains(localizer.getString("label.contact", null));
		tester.assertContains(localizer.getString("label.contactname", null));
		tester.assertContains(localizer.getString("label.contactemail", null));
		tester.assertContains(localizer.getString("label.contactphone", null));
		tester.assertNoErrorMessage();
	}
}
