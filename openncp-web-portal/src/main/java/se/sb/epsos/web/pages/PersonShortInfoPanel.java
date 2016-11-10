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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import se.sb.epsos.web.EpsosAuthenticatedWebSession;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.model.LinkAction;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.util.ActionLinkPanel;

public class PersonShortInfoPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	private ModalWindow personInfoModalWindow;
	
	public PersonShortInfoPanel(String id, IModel<? extends Person> model) {
        super(id, model);
        AuthenticatedUser userDetails = ((EpsosAuthenticatedWebSession)getSession()).getUserDetails();
        add(new Label("id"));
        add(new Label("fullname"));
        String countryCode = model.getObject().getCountry();
        add(new Label("country", countryCode));
        //add(new Label("country",countryCode!=null? getLocalizer().getString("country."+countryCode,this):null));
//        add(new Label("sex"));
        add(new Label("birthdate"));
        add(new Label("gender",new StringResourceModel("person.gender."+model.getObject().getGender(), this, null)));
        if (userDetails.getTrc()!=null && userDetails.getTrc().isConfirmed()) {
        	add(new Label("trc",getLocalizer().getString("person.trc.confirmed", this)));
        } else {
        	add(new Label("trc",getLocalizer().getString("person.trc.notconfirmed", this)));
        }
        personInfoModalWindow = new ModalWindow("personInfoModalWindow");
		PersonInfoPanel personInfoPanel = new PersonInfoPanel(personInfoModalWindow.getContentId(), new CompoundPropertyModel<Person>(model));
		personInfoModalWindow.setTitle(getLocalizer().getString("person.title", this));
		personInfoModalWindow.setContent(personInfoPanel);
		personInfoModalWindow.setCookieName("personInfoModalWindow");
		add(personInfoModalWindow);
		LinkAction moreInfoAction = new LinkAction(new StringResourceModel("person.actions.details", PersonShortInfoPanel.this,null)){
			private static final long serialVersionUID = 3333470659443572764L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				personInfoModalWindow.show(target);
			}
		};
		add(new ActionLinkPanel<LinkAction>("moreInfo",new Model<LinkAction>(moreInfoAction)));
    }
}
