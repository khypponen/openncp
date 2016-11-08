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
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.util.List;
import java.util.Map.Entry;

import org.apache.wicket.PageParameters;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.model.BreadCrumbVO;
import se.sb.epsos.web.model.Dispensation;
import se.sb.epsos.web.model.LoadableDocumentModel;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.model.PersonBuilder;
import se.sb.epsos.web.model.Prescription;
import se.sb.epsos.web.model.TRC.TrcPurpose;
import se.sb.epsos.web.service.MetaDocument;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.service.NcpServiceFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-context.xml"})
public class DispensePrescriptionPageTest extends AbstractPageTest {

    private Person person;
    private NcpServiceFacade mockedFacade;

    @Before
    public void prepare() {
        String sessionId = tester.getServletSession().getId();
        Person person = PersonBuilder.create(sessionId, "192405038569", "SE", "Ofelia", "Ordinationslista", "F", "19240503");
        this.person = person;
        login("apotek", "1234");
        setTrc(person, TrcPurpose.TREATMENT);
    }

    @Test
    public void testRenderPageSucessfully() throws Exception {
        NcpServiceFacade serviceFacade = ((EpsosAuthenticatedWebSession) tester.getWicketSession()).getServiceFacade();
        AuthenticatedUser user = ((EpsosAuthenticatedWebSession) tester.getWicketSession()).getUserDetails();
        List<MetaDocument> docs = serviceFacade.queryDocuments(person, "EP", user);
        Prescription prescription = (Prescription) serviceFacade.retrieveDocument(docs.get(0));
        assertNotNull(docs);
        assertTrue(docs.size() > 0);
        PageParameters params = new PageParameters("personId=" + person.getEpsosId());
        params.put("prescription", new LoadableDocumentModel<Prescription>(prescription));
        tester.startPage(new DispensePrescriptionPage(params));
        tester.assertRenderedPage(DispensePrescriptionPage.class);
        tester.assertNoErrorMessage();
    }

    @Test
    @Ignore
    public void testBackButtonReturnsUserToViewPrescriptionsFromModalWindow() throws Exception {
        ((EpsosAuthenticatedWebSession) tester.getWicketSession()).addToBreadCrumbList(new BreadCrumbVO("title", new QueryDocumentsPage(new PageParameters("personId=" + person.getEpsosId() + ",docType=EP"))));
        testRenderPageSucessfully();
        tester.executeAjaxEvent("dispensationForm:dispenseRows:0:submit", "onclick");
        tester.assertRenderedPage(DispensePrescriptionPage.class);
        tester.clickLink("dispenseModalWindow:content:backToQueryDocuments:link");
        tester.assertRenderedPage(QueryDocumentsPage.class);
    }

    @Test
    public void testClickViewOriginalKnownNcpExceptionsHandledCorrectly() throws Exception {
        testRenderPageSucessfully();
        mockedFacade = mock(NcpServiceFacade.class, settings);
        ((EpsosAuthenticatedWebSession) tester.getWicketSession()).setServiceFacade(mockedFacade);
        for (Entry<String, String> knownExceptionEntry : epsosErrorCodesAndExpectedTexts.entrySet()) {
            verifyFailureViewOriginal(knownExceptionEntry.getKey(), localizer.getString("PDF.error.service", null) +": " + knownExceptionEntry.getValue());
        }
    }

    @Test
    @Ignore
    public void testClickPrintKnownNcpExceptionsHandledCorrectly() throws Exception {
        testRenderPageSucessfully();
        mockedFacade = mock(NcpServiceFacade.class, settings);
        ((EpsosAuthenticatedWebSession) tester.getWicketSession()).setServiceFacade(mockedFacade);
        for (Entry<String, String> knownExceptionEntry : epsosErrorCodesAndExpectedTexts.entrySet()) {
            verifyFailurePrint(knownExceptionEntry.getKey(), localizer.getString("EP.error.service", null) +": " + knownExceptionEntry.getValue());
        }
    }

    @Test
    @Ignore
    public void testSubmitDispensationKnownNcpExceptionsHandledCorrectly() throws Exception {
        testRenderPageSucessfully();
        mockedFacade = mock(NcpServiceFacade.class, settings);
        ((EpsosAuthenticatedWebSession) tester.getWicketSession()).setServiceFacade(mockedFacade);
        for (Entry<String, String> knownExceptionEntry : epsosErrorCodesAndExpectedTexts.entrySet()) {
            verifyFailureSubmitDispensation(knownExceptionEntry.getKey(), localizer.getString("dispensation.error.service", null) +": " + knownExceptionEntry.getValue());
        }
    }

    @Test
    public void testBackButtonReturnsToQueryDocPage() throws Exception {
        ((EpsosAuthenticatedWebSession) tester.getWicketSession()).addToBreadCrumbList(new BreadCrumbVO("title", new QueryDocumentsPage(new PageParameters("personId=" + person.getEpsosId() + ",docType=EP"))));
        testRenderPageSucessfully();
        tester.executeAjaxEvent("form:back", "onclick");
        tester.assertRenderedPage(QueryDocumentsPage.class);
    }

    private void verifyFailureSubmitDispensation(String errorCode, String errorMessage) throws NcpServiceException {
        doThrow(createKnownException(errorCode)).when(mockedFacade).submitDocument((Dispensation) notNull(), (AuthenticatedUser) notNull(), (Person) notNull(), (String) notNull());
        tester.executeAjaxEvent("dispensationForm:dispenseRows:0:submit", "onclick");
        assertErrorPanel(errorCode, errorMessage);
    }

    private void verifyFailureViewOriginal(String errorCode, String errorMessage) throws NcpServiceException {
        doThrow(createKnownException(errorCode)).when(mockedFacade).retrieveDocument((MetaDocument) notNull());
        tester.clickLink("dispensationForm:showPrescriptionPdfAction:link", true);
        assertErrorPanel(errorCode, errorMessage);
    }

    private void verifyFailurePrint(String errorCode, String errorMessage) throws NcpServiceException {
        doThrow(createKnownException(errorCode)).when(mockedFacade).retrieveDocument((MetaDocument) notNull());
        tester.clickLink("dispensationForm:printPrescriptionAction:link", true);
        assertErrorPanel(errorCode, errorMessage);
    }
}
