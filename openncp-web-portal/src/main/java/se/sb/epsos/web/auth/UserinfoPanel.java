/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.auth;

import java.util.Locale;

import javax.servlet.http.Cookie;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.service.DocumentCache;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.util.LinkedPanel;
import se.sb.epsos.web.util.MasterConfigManager;

/**
 * Created by IntelliJ IDEA.
 * User: andreas
 * Date: 2011-06-20
 * Time: 14.27
 * To change this template use File | Settings | File Templates.
 */
public class UserinfoPanel extends Panel {
	public static final Logger LOGGER = LoggerFactory.getLogger(UserinfoPanel.class);
	private static final long serialVersionUID = 1L;
	private static final String LOGOUT_URL = MasterConfigManager.get("ApplicationConfigManager.LogoutUrl");

	public UserinfoPanel(String name, final AuthenticatedUser userDetails) {
        super(name, new CompoundPropertyModel<AuthenticatedUser>(userDetails));
        final EpsosAuthenticatedWebSession session = (EpsosAuthenticatedWebSession)getSession();
        add(new Label("commonName"));
        add(new Label("organizationName"));
        add(new ListView<String>("roles", userDetails.getRoles()) {
			private static final long serialVersionUID = -6797409773110811005L;

			@Override
            protected void populateItem(final ListItem<String> item) {
                item.add(new LinkedPanel<Panel>("role", null, new Model<String>(getString(item.getModelObject()))) {
					private static final long serialVersionUID = 4974177502907602369L;

					@Override
                    public void onClick() {
                        userDetails.setPrmiaryRole(item.getModelObject());
                        try {
                            ((EpsosAuthenticatedWebSession)getSession()).getServiceFacade().initUser(userDetails);
                        } catch (NcpServiceException ex) {
                            error(getString("error.login.init"));
                        }
                    }
                });
            }
        });
        
        add(new ListView<Locale>("locales", userDetails.getLocales()) {
			private static final long serialVersionUID = 1L;

			@Override
            protected void populateItem(final ListItem<Locale> item) {
                item.add(new LinkedPanel<Panel>("locale", null, new Model<String>(getString(item.getModelObject().toString()))) {
					private static final long serialVersionUID = 1L;

					@Override
                    public void onClick() {
                        session.setLocale(item.getModelObject());
                        setResponsePage(getApplication().getHomePage());
                    }
                });
            }
        });
                
        add(new Link<Void>("logoutLink") {
			private static final long serialVersionUID = -3877947678874130360L;

			@Override
            public void onClick() {
				Cookie cookie = new Cookie("epSOSlocale", session.getLocale().toString());
				cookie.setMaxAge(99999999);
				((WebResponse)getResponse()).addCookie(cookie);
                session.invalidate();
                LOGGER.debug("LOGOUT_URL:" + LOGOUT_URL);
                getRequestCycle().setRequestTarget(new RedirectRequestTarget(LOGOUT_URL));
            }
        });
    }
}
