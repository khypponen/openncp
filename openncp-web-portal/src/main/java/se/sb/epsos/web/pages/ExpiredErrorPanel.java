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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

public class ExpiredErrorPanel extends Panel {
	private static final long serialVersionUID = -8456126238726150697L;

	public ExpiredErrorPanel(String id) {
		super(id);
		add(new Label("label", new StringResourceModel("errorpage.sessionTimeout", this, null, "The session has been closed due to timeout.")));
		add(new Label("label2", new StringResourceModel("errorpage.loginAgain", this, null, "You can continue by logging in again: ")));
		add(new BookmarkablePageLink<Object>("homeLink", getApplication().getHomePage()));
	}
}
