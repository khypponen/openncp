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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.util.Map.Entry;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.protocol.http.MockHttpServletRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
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
import se.sb.epsos.web.model.TRC.TrcPurpose;
import se.sb.epsos.web.service.MetaDocument;
import se.sb.epsos.web.service.NcpServiceFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-context.xml"})
public class QueryDocumentsPageTest extends AbstractPageTest {

    private Person person;
    private String sessionId;

    @Before
    public void prepare() {
        sessionId = tester.getServletSession().getId();
        person = PersonBuilder.create(sessionId, "192405038569", "SE", "Ofelia", "Ordinationslista", "F", "19240503");
        login("apotek", "1234");
    }

    @Test
    public void testRedirecToTrcPage() throws Exception {
        final WebRequestCycle cycle = tester.createRequestCycle();
        final String url = cycle.urlFor(QueryDocumentsPage.class, new PageParameters("docType=EP,personId=" + person.getEpsosId())).toString();
        MockHttpServletRequest servletRequest = tester.getServletRequest();
        String newUrl = "http://localhost" + servletRequest.getServletPath() + "" + servletRequest.getContextPath() + "/" + url;
        tester.getServletRequest().setURL(newUrl);
        tester.processRequestCycle();

//        setTrc(null);
        boolean redirected = false;
        try {
            tester.startPage(new QueryDocumentsPage(new PageParameters("docType=EP,personId=" + person.getEpsosId())));
        } catch (RestartResponseAtInterceptPageException ex) {
            redirected = true;
        }
        assertTrue(redirected);
    }

    @Test
    public void testRenderPageSucessfully_EP() throws Exception {
        setTrc(TRC.TrcPurpose.TREATMENT);
        tester.startPage(QueryDocumentsPage.class, new PageParameters("docType=EP,personId=" + person.getEpsosId()));
        tester.assertRenderedPage(QueryDocumentsPage.class);
//        tester.assertContains("Fenoximetylpenicillin");
//        tester.assertContains("Natriumkromoglikat");
        tester.assertContains(localizer.getString("document.actions.dispense", null));
        tester.assertNoErrorMessage();
    }

    @Test
    public void testRenderPageSucessfully_PS() throws Exception {
        setTrc(TRC.TrcPurpose.TREATMENT);
        tester.startPage(QueryDocumentsPage.class, new PageParameters("docType=PS,personId=" + person.getEpsosId()));
        tester.assertRenderedPage(QueryDocumentsPage.class);
        tester.assertNoErrorMessage();
    }

    @Test
    public void testTrcValidationHandlesDifferentPersonObjectsWithSameEpsosId() throws Exception {
        AuthenticatedUser userDetails = ((EpsosAuthenticatedWebSession) tester.getWicketSession()).getUserDetails();
        //Create a new Person object for the TRC.    
        TRC trc = new TRC(PersonBuilder.create(sessionId, "192405038569", "SE", "Ofelia", "Ordinationslista", "F", "19240503"), TrcPurpose.TREATMENT);
        trc.setConfirmed(true);
        userDetails.setTrc(new TRC(person, TrcPurpose.TREATMENT));
        tester.startPage(QueryDocumentsPage.class, new PageParameters("docType=PS,personId=" + person.getEpsosId()));
        tester.assertRenderedPage(QueryDocumentsPage.class);
    }

    /**
     * Test that all known NcpServiceExceptions are handled when failing to
     * query documents.
     *
     * @throws Exception
     */
    @Test
    public void testQueryDocumentsFailureKnownNcpExceptionsHandled() throws Exception {
        for (Entry<String, String> knownExceptionEntry : epsosErrorCodesAndExpectedTexts.entrySet()) {
            verifyFailureQueryDocuments(knownExceptionEntry.getKey(), localizer.getString("EP.error.service",null) + ": " + knownExceptionEntry.getValue());
        }
    }
    
    /**
     * Helper method to test known NcpServiceException when querying documents.
     *
     * @param errorCode
     * @param expectedMessage
     * @throws Exception
     */
    private void verifyFailureQueryDocuments(String errorCode, String expectedMessage) throws Exception {
        NcpServiceFacade mockedFacade = mock(NcpServiceFacade.class, settings);
        ((EpsosAuthenticatedWebSession) tester.getWicketSession()).setServiceFacade(mockedFacade);

        doThrow(createKnownException(errorCode)).when(mockedFacade).queryDocuments(any(Person.class), any(String.class), any(AuthenticatedUser.class));

        setTrc(TRC.TrcPurpose.TREATMENT);
        tester.startPage(QueryDocumentsPage.class, new PageParameters("docType=EP,personId=" + person.getEpsosId()));

        assertErrorPanel(errorCode, expectedMessage);
    }

    /**
     * Test that all known NcpServiceExceptions are handled when failing to
     * dispense a recipe.
     *
     * @throws Exception
     */
    @Test
    public void testClickDispenseKnownNcpExceptionsHandled() throws Exception {
    	testRenderPageSucessfully_EP();
        for (Entry<String, String> knownExceptionEntry : epsosErrorCodesAndExpectedTexts.entrySet()) {
            verifyFailureClickDispense(knownExceptionEntry.getKey(), localizer.getString("EP.error.service",null) + ": " + knownExceptionEntry.getValue());
        }
    }
    
    /**
     * Helper method to test known NcpServiceException when clicking on
     * dispense.
     *
     * @param errorCode
     * @param expectedMessage
     * @throws Exception
     */
    private void verifyFailureClickDispense(String errorCode, String expectedMessage) throws Exception {
        NcpServiceFacade mockedFacade = mock(NcpServiceFacade.class, settings);
        ((EpsosAuthenticatedWebSession) tester.getWicketSession()).setServiceFacade(mockedFacade);        
        doThrow(createKnownException(errorCode)).when(mockedFacade).retrieveDocument(any(MetaDocument.class));

//        tester.debugComponentTrees();
        tester.clickLink("dataTable:body:rows:1:cells:5:cell:list:0:item:link", true);

        assertErrorPanel(errorCode, expectedMessage);
    }

    /**
     * Test that all known NcpServiceExceptions are handled when failing to view
     * the original PS.
     *
     * @throws Exception
     */
    @Test
    public void testClickViewOriginalPSKnownNcpExceptionsHandled() throws Exception {
        testRenderPageSucessfully_PS();
        for (Entry<String, String> knownExceptionEntry : epsosErrorCodesAndExpectedTexts.entrySet()) {
            verifyFailureClickViewOriginalPS(knownExceptionEntry.getKey(), localizer.getString("PS.error.service",null) + ": " + knownExceptionEntry.getValue());
        }
    }
    
    private void verifyFailureClickViewOriginalPS(String errorCode, String expectedMessage) throws Exception {
        NcpServiceFacade mockedFacade = mock(NcpServiceFacade.class, settings);
        ((EpsosAuthenticatedWebSession) tester.getWicketSession()).setServiceFacade(mockedFacade);
        doThrow(createKnownException(errorCode)).when(mockedFacade).retrieveDocument(any(MetaDocument.class));

//        tester.debugComponentTrees();
        tester.clickLink("dataTable:body:rows:1:cells:5:cell:list:1:item:link", true);

        assertErrorPanel(errorCode, expectedMessage);
    }

    /**
     * Test that all known NcpServiceExceptions are handled when failing to view
     * the original PS.
     *
     * @throws Exception
     */
    @Test
    public void testClickViewPSKnownNcpExceptionsHandled() throws Exception {
        testRenderPageSucessfully_PS();
        for (Entry<String, String> knownExceptionEntry : epsosErrorCodesAndExpectedTexts.entrySet()) {
            verifyFailureClickViewPS(knownExceptionEntry.getKey(), localizer.getString("PS.error.service",null) + ": " + knownExceptionEntry.getValue());
        }
    }

    private void verifyFailureClickViewPS(String errorCode, String expectedMessage) throws Exception {
        NcpServiceFacade mockedFacade = mock(NcpServiceFacade.class, settings);
        ((EpsosAuthenticatedWebSession) tester.getWicketSession()).setServiceFacade(mockedFacade);
        doThrow(createKnownException(errorCode)).when(mockedFacade).retrieveDocument(any(MetaDocument.class));

//        tester.debugComponentTrees();
        tester.clickLink("dataTable:body:rows:1:cells:5:cell:list:1:item:link", true);

        assertErrorPanel(errorCode, expectedMessage);
    }

    private void setTrc(TRC.TrcPurpose purpose) {
        setTrc(person, purpose);
    }
}
