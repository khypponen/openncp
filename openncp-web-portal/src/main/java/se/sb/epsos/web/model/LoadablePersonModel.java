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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sb.epsos.web.model;

import org.apache.wicket.model.LoadableDetachableModel;

import se.sb.epsos.web.service.PersonCache;
import se.sb.epsos.web.service.PersonCacheKey;

/**
 *
 * @author andreas
 */
public class LoadablePersonModel extends LoadableDetachableModel<Person> {
	private static final long serialVersionUID = 7110377123211052278L;
	private PersonCacheKey key;

    public LoadablePersonModel(Person person) {
        this.key = person.getCacheKey();
        setObject(person);
    }  
    
    @Override
    protected Person load() {
        return PersonCache.getInstance().get(key);
    }
    
}
