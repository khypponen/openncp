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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sb.epsos.web.ErrorFeedbackPanel;
import se.sb.epsos.web.model.ErrorFeedback;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-context.xml"})
public class BasePageTest extends AbstractPageTest {
    private SimpleBasePage testPage;
    private ErrorFeedback error = new ErrorFeedback();

    @Before
    public void prepare() {
        testPage = new SimpleBasePage();
        tester.startPage(testPage);
        error.setErrorCode("errorCode");
        error.setErrorMessage("Error message");
    }

    @Test
    public void testDisplayErrorObjectIsPassedToPanelAndSetToVisible() {
        testPage.displayError(error);
        ErrorFeedbackPanel panel = (ErrorFeedbackPanel) tester.getComponentFromLastRenderedPage("errorFeedback");
        Assert.assertSame(error, ((ArrayList<ErrorFeedback>) panel.getDefaultModelObject()).get(0));
        Assert.assertEquals(true, panel.isVisible());
        tester.assertRenderedPage(SimpleBasePage.class);
    }

    @Test
    public void testDisplayErrorAfterAjaxErrorPanelIsAddedToTarget() {
        AjaxRequestTarget target = mock(AjaxRequestTarget.class, settings);
        testPage.displayErrorAfterAjax(error, target);

        ArgumentCaptor<ErrorFeedbackPanel> capturedPanel = ArgumentCaptor.forClass(ErrorFeedbackPanel.class);
        verify(target).addComponent(capturedPanel.capture());
        Assert.assertSame(error, ((ArrayList<ErrorFeedback>) capturedPanel.getValue().getDefaultModelObject()).get(0));
        Assert.assertEquals(true, capturedPanel.getValue().isVisible());

    }
}
