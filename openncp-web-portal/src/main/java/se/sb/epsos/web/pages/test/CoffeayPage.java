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
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author andreas
 */
public class CoffeayPage extends WebPage {

    public CoffeayPage() {
        this(new CompoundPropertyModel(new LoadableCoffeayModel(CoffeayMachine.getCup("1"))));
    }
    
    
    public CoffeayPage(final IModel<Coffeay> coffeayModel) {
        setDefaultModel(coffeayModel);
        add(new Label("id"));
        add(new Label("type"));
        add(new Label("size"));
        add(new Label("bytes"));
        Link popup1 = new Link("popup1") {
            @Override
            public void onClick() {
                setResponsePage(new CoffeayPage(coffeayModel));
            }
        };
        popup1.setPopupSettings(new PopupSettings());
        add(popup1);
        Link popup2 = new Link("popup2") {
            @Override
            public void onClick() {
                setResponsePage(new CoffeayPage(coffeayModel));
            }
        };
        final List<Coffeay> coffeayList = CoffeayMachine.getAll();
        SortableDataProvider<Coffeay> provider = new CoffeayDataProvider(wrapCoffeayListInModel(coffeayList));
        final DataView<Coffeay> dataView = new DataView<Coffeay>("list", provider)
        {
            @Override
            protected void populateItem(final Item<Coffeay> item)
            {
                Coffeay c = item.getModelObject();
                item.add(new Label("id", c.getId()));
                item.add(new Label("type", c.getType()));
                item.add(new Label("size", c.getSize()));
            }
        };
        add(dataView);
        popup2.setPopupSettings(new PopupSettings());
        add(popup2);
        add(new Link("cuppuchino") {
            @Override
            public void onClick() {
                setResponsePage(new CoffeayPage(new CompoundPropertyModel(new LoadableCoffeayModel(CoffeayMachine.getCup("2")))));
            }
        });
        add(new Link("cortado") {
            @Override
            public void onClick() {
                setResponsePage(new CoffeayPage(new CompoundPropertyModel(new LoadableCoffeayModel(CoffeayMachine.getCup("3")))));
            }
        });
        add(new Link("espresso") {
            @Override
            public void onClick() {
                setResponsePage(new CoffeayPage(new CompoundPropertyModel(new LoadableCoffeayModel(CoffeayMachine.getCup("4")))));
            }
        });
    }
    
    private List<LoadableCoffeayModel> wrapCoffeayListInModel(List<Coffeay> list) {
        List<LoadableCoffeayModel> newList = new ArrayList<LoadableCoffeayModel>();
        for (Coffeay c : list) {
            newList.add(new LoadableCoffeayModel(c));
        }
        return newList;
    }
    
    public class CoffeayDataProvider extends SortableDataProvider<Coffeay> {

        List<LoadableCoffeayModel> coffeayList;
        
        public CoffeayDataProvider(List<LoadableCoffeayModel> coffeayList) {
            this.coffeayList = coffeayList;
        }

        @Override
        public Iterator<Coffeay> iterator(int first, int count) {
            List<LoadableCoffeayModel> subList = coffeayList.subList(first, first+count);
            List<Coffeay> newList  = new ArrayList<Coffeay>();
            for(LoadableCoffeayModel cm : subList) {
                newList.add(cm.getObject());
            }
            return newList.iterator();
        }

        @Override
        public int size() {
            return coffeayList.size();
        }

        @Override
        public IModel<Coffeay> model(Coffeay object) {
            return new LoadableCoffeayModel(object);
        }
    }
}
