/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/
package se.sb.epsos.web.pages;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;
import junit.framework.TestCase;
import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.auth.LoginPage;
import se.sb.epsos.web.model.CountryVO;
import se.sb.epsos.web.model.PatientIdVO;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.service.NcpServiceFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-application-context.xml" })
public class QueryPersonPageTest extends AbstractPageTest {

	@Before
	public void login() {
		// Adding this property fixes runtime error, not sure what for it is
		// needed
		System.setProperty("epsos-internationalsearch-config-path", "justSomething");
		tester.startPage(QueryPersonPage.class);
		tester.assertRenderedPage(LoginPage.class);
		assertAuthorization("doktor", "1234");
		tester.assertRenderedPage(QueryPersonPage.class);
	}

	@Test
	public void testRendersSuccessfully() throws NcpServiceException {
		tester.startPage(QueryPersonPage.class);
		tester.assertRenderedPage(QueryPersonPage.class);
	}

	// @Test
	// public void testQuerySuccessfully() throws NcpServiceException {
	// queryPerson();
	// tester.assertRenderedPage(QueryPersonPage.class);
	// tester.debugComponentTrees();
	// tester.assertContains("Aivars");
	// tester.assertContains("Numan");
	// tester.assertContains(localizer.getString("person.actions.details",
	// null));
	// tester.assertContains(localizer.getString("person.actions.patientsummary",
	// null));
	// tester.assertNoErrorMessage();
	// }

	/**
	 * Test that all known NcpServiceExceptions are handled when failing to
	 * query persons.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQueryDocumentsFailureKnownNcpExceptionsHandled() throws Exception {
		for (Map.Entry<String, String> knownExceptionEntry : epsosErrorCodesAndExpectedTexts.entrySet()) {
			verifyFailureQueryPersons(knownExceptionEntry.getKey(),
					localizer.getString("error.ncpservice.query", null) + ": " + knownExceptionEntry.getValue());
		}
	}

	private void verifyFailureQueryPersons(String errorCode, String expectedMessage) throws Exception {
		NcpServiceFacade mockedFacade = mock(NcpServiceFacade.class, settings);
		((EpsosAuthenticatedWebSession) tester.getWicketSession()).setServiceFacade(mockedFacade);

		doThrow(createKnownException(errorCode)).when(mockedFacade).queryForPatient((AuthenticatedUser) notNull(),
				(List<PatientIdVO>) notNull(), (CountryVO) notNull());
		queryPerson();
		assertErrorPanel(errorCode, expectedMessage);
	}

	// TODO - Fails if order is changed in country-config.xml
	private void queryPerson() {
		tester.startPage(QueryPersonPage.class);
		// Don't know what there is supposed to be as data in the first place,
		// but this can be used to validate that the right content gets rendered to validate the test result
		List<PatientIdVO> patients = new ArrayList<>();
		PatientIdVO pivo = new PatientIdVO();
		pivo.setDomain("domain123");
		pivo.setLabel("label123");
		pivo.setMax(100);
		pivo.setMin(1);
		pivo.setValue("value123");
		patients.add(pivo);
		QueryPersonPage page = (QueryPersonPage) tester.getLastRenderedPage();
		page.setPatientVOList(patients);

		tester.assertRenderedPage(QueryPersonPage.class);
		FormTester formTester = tester.newFormTester("form");
		formTester.select("patientContainer:country", 10);
		tester.executeAjaxEvent("form:patientContainer:country", "onchange");
		tester.assertRenderedPage(QueryPersonPage.class);
		tester.assertNoErrorMessage();
		tester.dumpPage();
		tester.assertNoErrorMessage();
		tester.assertComponent("form", Form.class);
		tester.assertComponent("form:patientContainer", WebMarkupContainer.class);
		tester.assertComponent("form:patientContainer:patientIds", ListView.class);
		Component comp = tester.getComponentFromLastRenderedPage("form:patientContainer:patientIds");
		if (comp instanceof ListView) {
			TestCase.assertEquals(1, ((ListView) comp).size());
		} else {
			TestCase.fail("form:patientContainer:patientIds was not of type ListView");
		}
		
		tester.assertComponent("form:patientContainer:patientIds:0:idLabel", Label.class);
		tester.assertContains("label123");
		tester.assertComponent("form:patientContainer:patientIds:0:idTextField", TextField.class);
		tester.assertContains("value123");
		
		// Does not exist in the first place?
//		String localizedString = localizer.getString("patient.search.patient.svnr", null);
//		tester.assertContains(localizedString);
		formTester = tester.newFormTester("form");
		formTester.setValue("patientContainer:country", "SE");
		formTester.setValue("patientContainer:patientIds:0:idTextField", "199604082397");
		tester.executeAjaxEvent("form:patientContainer:submit", "onclick");
	}

}
