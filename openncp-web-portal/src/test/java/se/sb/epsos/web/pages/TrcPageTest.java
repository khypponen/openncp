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
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.util.Map.Entry;

import org.apache.wicket.PageParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.model.PersonBuilder;
import se.sb.epsos.web.model.TRC;
import se.sb.epsos.web.service.NcpServiceFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-application-context.xml" })
public class TrcPageTest extends AbstractPageTest {
	private NcpServiceFacade mockedFacade;

	@Before
	public void prepare() throws Exception {
		mockedFacade =  mock(NcpServiceFacade.class);
		((EpsosAuthenticatedWebSession) tester.getWicketSession()).setServiceFacade(mockedFacade);
		tester.startPage(tester.getApplication().getHomePage());
		assertAuthorization("doktor", "1234");
		
		String sessionId = tester.getServletSession().getId();
		Person person = PersonBuilder.create(sessionId, "192405038569","SE","Ofelia","Ordinationslista","F","19240503");
		tester.startPage(new TrcPage(new PageParameters("personId=" + person.getEpsosId())));
	}
	
	@Test
	public void testRenderPage() throws Exception {
		//assert  page is rendered
		tester.assertRenderedPage(TrcPage.class);
	}
	
	/**
	 * Test that known NcpServiceExceptions are correctly handled when submitting the trc form.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSetTrcAssertionKnownNcpFailures() throws Exception {
		for(Entry<String, String> knownExceptionEntry: epsosErrorCodesAndExpectedTexts.entrySet()) {
			verifyFailureSubmitForm(knownExceptionEntry.getKey(), localizer.getString("TRC.error.service",null) + ": " + knownExceptionEntry.getValue());
		}
	}
	
	
	/**
	 * Helper method to test known NCPServiceExceptions when submitting the trc form.
	 *  
	 * @param errorCode
	 * @param errorMessage
	 * @throws Exception
	 */
	private void verifyFailureSubmitForm(String errorCode, String errorMessage) throws Exception{
		doThrow(createKnownException(errorCode)).when(mockedFacade).setTRCAssertion((TRC)notNull(), (AuthenticatedUser)notNull());
		tester.executeAjaxEvent("trcForm:confirmButton", "onclick");
		assertErrorPanel(errorCode, errorMessage);	
	}
}
