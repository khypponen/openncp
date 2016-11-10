/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.util;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.resource.ContextRelativeResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.model.BreadCrumbVO;
import se.sb.epsos.web.pages.QueryDocumentsPage;

public class SpecialBreadCrumbPanel extends Panel {

    private static final long serialVersionUID = 1091379628212976568L;
    protected static Logger LOGGER = LoggerFactory.getLogger(SpecialBreadCrumbPanel.class);

    public SpecialBreadCrumbPanel(String id) {
        super(id);
        add(new BookmarkablePageLink<Object>("homeLink", getApplication().getHomePage()));
        add(new ListView<BreadCrumbVO<?>>("history", ((EpsosAuthenticatedWebSession) getSession()).getBreadCrumbList()) {

            private static final long serialVersionUID = -397424834597149793L;

            @Override
            protected void populateItem(final ListItem<BreadCrumbVO<?>> item) {
                Link<BreadCrumbVO<?>> link = new Link<BreadCrumbVO<?>>("link") {

                    private static final long serialVersionUID = 5483304002140228959L;

                    @Override
                    public void onClick() {
                        BreadCrumbVO<?> bc = item.getModel().getObject();
                        ((EpsosAuthenticatedWebSession) getSession()).removeAllAfterBc(item.getModel().getObject());
                        if (bc.getWebPage().getPageClass() == QueryDocumentsPage.class) {
                            setResponsePage(new QueryDocumentsPage(bc.getWebPage().getPageParameters()));
                        } else {
                            setResponsePage(bc.getWebPage());
                        }
                    }
                };

                link.add(new Label("title", item.getModel().getObject().getTitle())).setEnabled(item.getModel().getObject().getClickable());
                if (item.getModel().getObject().getTitle().equals(((EpsosAuthenticatedWebSession) getSession()).getBreadCrumbList().get(0).getTitle())) {
                    item.add(new Image("marker", new ContextRelativeResource("images/rak.png")));
                } else {
                    item.add(new Image("marker", new ContextRelativeResource("images/right.png")));
                }
                item.add(link);
            }
        });
    }
}
