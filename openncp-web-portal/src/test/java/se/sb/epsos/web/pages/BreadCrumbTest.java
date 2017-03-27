/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
 *
 *    This file is part of epSOS-WEB.
 *
 *    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
 **//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sb.epsos.web.pages;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.model.BreadCrumbVO;

/**
 * @author danielgronberg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-context.xml"})
public class BreadCrumbTest extends AbstractPageTest {
    private EpsosAuthenticatedWebSession session;

    @Before
    public void prepare() {
        session = (EpsosAuthenticatedWebSession) tester.getWicketSession();
        login("doktor", "1234");
    }

    @Test
    public void testBreadCrumbListAddClickable() {
        session.addToBreadCrumbList(new BreadCrumbVO("title", new SimpleBasePage()));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        Link clickableLink = (Link) tester.getComponentFromLastRenderedPage("breadcrumb:history:1:link");
        Assert.assertTrue(clickableLink.isEnabled());
    }

    @Test
    public void testBreadCrumbListAddBcNotPossibleToAddTwice() {
        session.addToBreadCrumbList(new BreadCrumbVO("title"));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        int breadCrumbLength = ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize();
        session.addToBreadCrumbList(new BreadCrumbVO("title2"));
        session.addToBreadCrumbList(new BreadCrumbVO("title2"));
        session.addToBreadCrumbList(new BreadCrumbVO("title3", new SimpleBasePage()));
        session.addToBreadCrumbList(new BreadCrumbVO("title3", new SimpleBasePage()));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        Assert.assertEquals(breadCrumbLength + 2, ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize());
    }

    @Test
    public void testBreadCrumbListAddNotClickable() {
        session.addToBreadCrumbList(new BreadCrumbVO("title"));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        Link clickableLink = (Link) tester.getComponentFromLastRenderedPage("breadcrumb:history:1:link");
        Assert.assertFalse(clickableLink.isEnabled());
    }

    @Test
    public void testRemoveAllAfterBc() {
        session.addToBreadCrumbList(new BreadCrumbVO("title"));
        session.addToBreadCrumbList(new BreadCrumbVO("title2"));
        session.addToBreadCrumbList(new BreadCrumbVO("title3"));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        int breadCrumbLength = ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize();
        session.removeAllAfterBc(new BreadCrumbVO("title2"));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        Assert.assertEquals(breadCrumbLength - 1, ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize());
    }

    @Test
    public void testRemoveAllAfterTitle() {
        session.addToBreadCrumbList(new BreadCrumbVO("title"));
        session.addToBreadCrumbList(new BreadCrumbVO("title2"));
        session.addToBreadCrumbList(new BreadCrumbVO("title3"));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        int breadCrumbLength = ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize();
        session.removeAllAfterTitle("title2");
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        Assert.assertEquals(breadCrumbLength - 1, ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize());
    }

    @Test
    public void testRemoveAllAfterBcItemNotInList() {
        session.addToBreadCrumbList(new BreadCrumbVO("title"));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        int breadCrumbLength = ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize();
        session.removeAllAfterBc(new BreadCrumbVO("notFound"));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        Assert.assertEquals(breadCrumbLength, ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize());
    }

    @Test
    public void testRemoveAllAfterTitleItemNotInList() {
        session.addToBreadCrumbList(new BreadCrumbVO("title"));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        int breadCrumbLength = ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize();
        session.removeAllAfterTitle("notFound");
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        Assert.assertEquals(breadCrumbLength, ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize());
    }

    @Test
    public void testRemovePostFromBreadCrumbList() {
        session.addToBreadCrumbList(new BreadCrumbVO("title"));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        int breadCrumbLength = ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize();
        session.removePostFromBreadCrumbList("title");
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        Assert.assertEquals(breadCrumbLength - 1, ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize());
    }

    @Test
    public void testRemovePostFromBreadCrumbListNotInList() {
        session.addToBreadCrumbList(new BreadCrumbVO("title"));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        int breadCrumbLength = ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize();
        session.removePostFromBreadCrumbList("notinlist");
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        Assert.assertEquals(breadCrumbLength, ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize());
    }

    @Test
    public void testClearBreadCrumbList() {
        session.addToBreadCrumbList(new BreadCrumbVO("title"));
        session.addToBreadCrumbList(new BreadCrumbVO("title2"));
        session.addToBreadCrumbList(new BreadCrumbVO("title3"));
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        session.clearBreadCrumbList();
        tester.startPage(SimpleBasePage.class);
        tester.assertRenderedPage(SimpleBasePage.class);
        Assert.assertEquals(0, ((ListView<BreadCrumbVO>) tester.getComponentFromLastRenderedPage("breadcrumb:history")).getViewSize());
    }
}
