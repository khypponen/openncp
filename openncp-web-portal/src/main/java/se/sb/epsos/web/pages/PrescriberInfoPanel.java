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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import se.sb.epsos.web.model.Prescription;

public class PrescriberInfoPanel extends Panel {

	private static final long serialVersionUID = 2207004074071769243L;

	public PrescriberInfoPanel(String id, IModel<Prescription> model) {
		super(id,model);
        add(new Label("createDate"));
		add(new Label("performer"));
		add(new Label("profession"));
		add(new Label("facility"));
		add(new Label("address"));
		add(new Label("contact1"));
		add(new Label("contact2"));
		add(new Label("country"));
	}
	
}
