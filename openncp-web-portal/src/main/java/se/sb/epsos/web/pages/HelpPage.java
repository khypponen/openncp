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

import se.sb.epsos.web.BasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

/**
 * Created by IntelliJ IDEA.
 * User: andreas
 * Date: 2011-06-21
 * Time: 16.08
 * To change this template use File | Settings | File Templates.
 */
public class HelpPage extends BasePage {

    public HelpPage() {
    	getSession().clearBreadCrumbList();
        add(new Label("contactname",getLocalizer().getString("label.contactname",this)));
        add(new Label("contactemail",getLocalizer().getString("label.contactemail",this)));
        add(new Label("contactphone",getLocalizer().getString("label.contactphone",this)));
    }
}
