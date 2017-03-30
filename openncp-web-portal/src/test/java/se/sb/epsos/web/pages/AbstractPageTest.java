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

import org.apache.wicket.Localizer;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.MockSettings;
import org.springframework.beans.factory.annotation.Autowired;
import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.EpsosWebApplication;
import se.sb.epsos.web.ErrorFeedbackPanel;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.auth.LoginPage;
import se.sb.epsos.web.model.ErrorFeedback;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.model.TRC;
import se.sb.epsos.web.service.NcpServiceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public abstract class AbstractPageTest {

    protected WicketTester tester;
    protected Map<String, String> epsosErrorCodesAndExpectedTexts = new HashMap<String, String>();
    protected MockSettings settings = withSettings().serializable();
    Localizer localizer;
    @Autowired
    private EpsosWebApplication epsosWebApplication;

    @Before
    public void setUp() {
        tester = new WicketTester(epsosWebApplication);
        tester.setupRequestAndResponse();
        localizer = tester.getApplication().getResourceSettings().getLocalizer();
        populateKnownErrors();
    }

    private void putError(String errorCode) {
        epsosErrorCodesAndExpectedTexts.put(errorCode, localizer.getString("error.epsos." + errorCode, null));
    }

    private void populateKnownErrors() {
        putError("1002");
        putError("4701");
        putError("4703");
        putError("4202");
        putError("4203");
        putError("4204");
        putError("4103");
        putError("4104");
        putError("XDSRegistryError");
        putError("XDSREpositoryError");
        putError("PrivacyViolation");
        putError("InsufficientRights");
        putError("PatientAuthenticationRequired");
        putError("AnswerNotAvailable");
        putError("PolicyViolation");
        putError("1031");
        epsosErrorCodesAndExpectedTexts.put("666", localizer.getString("error.epsos.unknown", null));
    }

    protected void login(String username, String password) {
        ((EpsosAuthenticatedWebSession) tester.getWicketSession()).signOut();
        tester.startPage(epsosWebApplication.getHomePage());
        tester.assertRenderedPage(LoginPage.class);
        FormTester formTester = tester.newFormTester("loginForm");
        formTester.setValue("username", username);
        formTester.setValue("password", password);
        tester.executeAjaxEvent("loginForm:submitButton", "onclick");
        tester.assertRenderedPage(epsosWebApplication.getHomePage());
    }

    protected void assertAuthorization(String username, String password) {
        tester.assertRenderedPage(LoginPage.class);
        FormTester formTester = tester.newFormTester("loginForm");
        formTester.setValue("username", username);
        formTester.setValue("password", password);
        tester.executeAjaxEvent("loginForm:submitButton", "onclick");
    }

    protected void assertErrorPanel(String expectedErrorCode, String expectedMessage) {
        ErrorFeedbackPanel panel = (ErrorFeedbackPanel) tester.getComponentFromLastRenderedPage("errorFeedback");
        Assert.assertEquals(expectedErrorCode, ((ArrayList<ErrorFeedback>) panel.getDefaultModelObject()).get(0).getErrorCode());
        Assert.assertEquals(expectedMessage, ((ArrayList<ErrorFeedback>) panel.getDefaultModelObject()).get(0).getErrorMessage());
    }

    protected NcpServiceException createKnownException(String errorCode) {
        NcpServiceException knownException = mock(NcpServiceException.class, settings);
        when(knownException.isKnownEpsosError()).thenReturn(true);
        when(knownException.getEpsosErrorCode()).thenReturn(errorCode);
        return knownException;
    }

    protected void setTrc(Person person, TRC.TrcPurpose purpose) {
        AuthenticatedUser userDetails = ((EpsosAuthenticatedWebSession) tester.getWicketSession()).getUserDetails();
        if (purpose != null) {
            TRC trc = new TRC(person, purpose);
            trc.setConfirmed(true);
            userDetails.setTrc(new TRC(person, purpose));
        }
        Assert.assertNotNull(userDetails.getTrc());
    }
}
