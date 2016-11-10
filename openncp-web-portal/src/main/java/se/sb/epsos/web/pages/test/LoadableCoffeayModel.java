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
package se.sb.epsos.web.pages.test;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreas
 */
class LoadableCoffeayModel extends LoadableDetachableModel<Coffeay> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadableCoffeayModel.class);
    private String id;
    
    public LoadableCoffeayModel(Coffeay coffeay) {
        LOGGER.info("###### Creating new LoadableCoffeayModel with: " + coffeay.getId());
        this.id = coffeay.getId();
        setObject(coffeay);
    }

    @Override
    protected Coffeay load() {
        LOGGER.info("###### Loading detached coffeay with id: " +this.id);
        return CoffeayMachine.getCup(this.id);
    }
    
}
