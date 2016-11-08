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
import org.apache.wicket.model.StringResourceModel;

import se.sb.epsos.web.model.Person;

/**
 * Created by: andreas 2011-07-01:10.34
 */
public class PersonInfoPanel extends Panel {
	private static final long serialVersionUID = 3404932175822617286L;

	public PersonInfoPanel(String id, IModel<Person> model) {
        super(id, model);
        add(new Label("id"));
        add(new Label("firstname"));
//        add(new Label("middlename"));
        add(new Label("lastname"));
        String countryCode = model.getObject().getCountry();
        add(new Label("country",countryCode));
        //add(new Label("country",countryCode!=null? getLocalizer().getString("country."+countryCode,this):null));
        add(new Label("gender",new StringResourceModel("person.gender."+model.getObject().getGender(), this, null)));
        add(new Label("city"));
        add(new Label("streetAddress"));
        add(new Label("zipcode"));
        add(new Label("state"));
        add(new Label("citizenship"));
        add(new Label("nationality"));
        add(new Label("birthdate"));
        add(new Label("placeofbirth"));
        add(new Label("driverslicensnumber"));
    }
}
