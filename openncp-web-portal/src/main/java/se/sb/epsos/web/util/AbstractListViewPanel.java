/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.util;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class AbstractListViewPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;
	
	public AbstractListViewPanel(String id, List<? extends T> list) {
		super(id);
		add(new ListView<T>("list",list) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<T> item) {
				AbstractListViewPanel.this.populateItem("item",item);
			}
			
		});
	}	
	
	/**
	 * Implement this method with item.add(new Component<? extends Panel>(componentId))
	 * 
	 * @param componentId the it to use then adding a Panel
	 * @param item the listItem handle to add Panel to
	 */
	public abstract void populateItem(String componentId, ListItem<T> item);
}
