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

import org.apache.wicket.PageParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.model.*;
import se.sb.epsos.web.model.TRC.TrcPurpose;
import se.sb.epsos.web.service.MetaDocument;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.service.NcpServiceFacade;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-context.xml"})
public class PdfPageTest extends AbstractPageTest {
    private Person person;
    private NcpServiceFacade serviceFacade;
    private List<MetaDocument> docs;

    @Before
    public void prepare() throws NcpServiceException {
        String sessionId = tester.getServletSession().getId();
        Person person = PersonBuilder.create(sessionId, "192405038569", "SE", "Ofelia", "Ordinationslista", "F", "19240503");
        this.person = person;
        login("apotek", "1234");
        setTrc(person, TrcPurpose.TREATMENT);
        serviceFacade = ((EpsosAuthenticatedWebSession) tester.getWicketSession()).getServiceFacade();
        AuthenticatedUser user = ((EpsosAuthenticatedWebSession) tester.getWicketSession()).getUserDetails();
        docs = serviceFacade.queryDocuments(person, "EP", user);
        assertNotNull(docs);
        assertTrue(docs.size() > 0);
    }

    @Test
    public void testRenderPageSucessfullyEP() throws Exception {
        PdfDocument prescriptionAsPdf = (PdfDocument) serviceFacade.retrieveDocument(new MetaDocument((tester.getWicketSession()).getId(),
                person.getEpsosId(),
                docs.get(0).getChildDocumentPdf()));

        PageParameters pm = new PageParameters();
        pm.add("personId", person.getEpsosId());
        pm.add("docType", "EP");
        pm.add("id", docs.get(0).getDoc().getUuid());

        tester.startPage(new PdfPage(new LoadableDocumentModel<PdfDocument>(prescriptionAsPdf), pm));
        tester.assertRenderedPage(PdfPage.class);
        tester.assertNoErrorMessage();
    }

    @Test
    public void testRenderPageSucessfullyPS() throws Exception {
        PdfDocument prescriptionAsPdf = (PdfDocument) serviceFacade.retrieveDocument(new MetaDocument((tester.getWicketSession()).getId(),
                person.getEpsosId(),
                docs.get(0).getChildDocumentPdf()));

        PageParameters parameters = new PageParameters();
        parameters.add("personId", person.getEpsosId());
        parameters.add("docType", "PS");

        tester.startPage(new PdfPage(new LoadableDocumentModel<PdfDocument>(prescriptionAsPdf), parameters));
        tester.assertRenderedPage(PdfPage.class);
        tester.assertNoErrorMessage();
    }

    @Test
    public void testBackButtonReturnsUserToDispensationWhenShowingEP() throws Exception {
        Prescription prescription = (Prescription) serviceFacade.retrieveDocument(docs.get(0));
        PageParameters params = new PageParameters("personId=" + person.getEpsosId() + ",docType=EP");
        params.put("prescription", new LoadableDocumentModel<Prescription>(prescription));
        ((EpsosAuthenticatedWebSession) tester.getWicketSession()).addToBreadCrumbList(new BreadCrumbVO("title", new DispensePrescriptionPage(params)));
        testRenderPageSucessfullyEP();
        tester.executeAjaxEvent("form:back", "onclick");
        tester.assertRenderedPage(DispensePrescriptionPage.class);
        tester.assertNoErrorMessage();
    }

    @Test
    public void testBackButtonReturnsUserToPatientSummaryListWhenShowingPS() throws Exception {
        ((EpsosAuthenticatedWebSession) tester.getWicketSession()).addToBreadCrumbList(new BreadCrumbVO("title", new QueryDocumentsPage(new PageParameters("personId=" + person.getEpsosId() + ",docType=PS"))));
        testRenderPageSucessfullyPS();
        tester.executeAjaxEvent("form:back", "onclick");
        tester.assertRenderedPage(QueryDocumentsPage.class);
        tester.assertNoErrorMessage();
    }
}
