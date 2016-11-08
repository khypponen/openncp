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

import org.apache.wicket.PageParameters;
import org.apache.wicket.model.CompoundPropertyModel;

import se.sb.epsos.web.BasePage;
import se.sb.epsos.web.model.LoadablePersonModel;
import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.service.PersonCache;
import se.sb.epsos.web.service.PersonCacheKey;

public class PrintDispensationPage extends BasePage {
	public PrintDispensationPage(PageParameters parameters) {
		PersonCacheKey key = new PersonCacheKey(getSession().getId(), parameters.getKey("personId"));
		final Person person = PersonCache.getInstance().get(key);
		add(new PersonShortInfoPanel("personinfo",new CompoundPropertyModel<Person>(new LoadablePersonModel(person))));
	}
}
