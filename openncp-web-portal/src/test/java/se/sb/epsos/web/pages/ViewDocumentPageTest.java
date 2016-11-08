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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
import se.sb.epsos.web.service.MetaDocument;
import se.sb.epsos.web.service.NcpServiceFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-application-context.xml" })
public class ViewDocumentPageTest extends AbstractPageTest {
	private Person person;
	
	@Before
	public void prepare() {
		String sessionId = tester.getServletSession().getId();
		Person person = PersonBuilder.create(sessionId, "192405038569","SE","Ofelia","Ordinationslista","F","19240503");
		this.person = person;
	}
	
	@Test
	public void testRenderPageSucessfully_EP() throws Exception {
		login("apotek","1234");
		NcpServiceFacade serviceFacade = ((EpsosAuthenticatedWebSession) tester.getWicketSession()).getServiceFacade();
		AuthenticatedUser user = ((EpsosAuthenticatedWebSession) tester.getWicketSession()).getUserDetails();
		List<MetaDocument> docs = serviceFacade.queryDocuments(person, "EP", user);
		assertNotNull(docs);
		System.out.println(docs);
		assertTrue(docs.size()>0);
		tester.startPage(ViewDocumentPage.class, new PageParameters("personId=" + person.getEpsosId() + ",id=" + docs.get(0).getDoc().getUuid() + ",docType=EP"));
		tester.assertRenderedPage(ViewDocumentPage.class);

		tester.assertNoErrorMessage();
	}
	
}
