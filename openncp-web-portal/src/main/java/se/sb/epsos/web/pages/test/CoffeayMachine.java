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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreas
 */
class CoffeayMachine {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoffeayMachine.class);
    private static CoffeayMachine machine;
    private HashMap<String,Coffeay> availableCoffey;
    

    private CoffeayMachine() {
        this.availableCoffey = new HashMap<String,Coffeay>();
        this.availableCoffey.put("1",new Coffeay("1", "Latte", "L",getRandomBytes(20)));
        this.availableCoffey.put("2",new Coffeay("2", "Cuppuchino", "M",getRandomBytes(20)));
        this.availableCoffey.put("3", new Coffeay("3", "Cortado", "S",getRandomBytes(20)));
        this.availableCoffey.put("4", new Coffeay("4", "Espresso", "XS",getRandomBytes(20)));
    }
    
    public static CoffeayMachine getInstance() {
        if (machine==null) {
            machine = new CoffeayMachine();
        }
        return machine;
    }
    
    public static Coffeay getCup(String id) {
        return getInstance().brewCoffey(id);
    }
    
    public static List<Coffeay> getAll() {
        return getInstance().showSelection();
    }
    
    private Coffeay brewCoffey(String id) {
        LOGGER.info("###### Brewing new coffeay: "+id);
        return this.availableCoffey.get(id);
    }
    
    private List<Coffeay> showSelection() {
        LOGGER.info("###### Showing selection of coffeay");
        List<Coffeay> list = new ArrayList<Coffeay>();
        for (Coffeay cup : this.availableCoffey.values()) {
            list.add(cup);
        }
        return list;
    }
    
    private byte[] getRandomBytes(int size) {
        byte[] bytes = new byte[size];
        new Random().nextBytes(bytes);
        return bytes;
    }
    
}
