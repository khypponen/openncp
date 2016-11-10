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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import se.sb.epsos.web.model.Person;
import se.sb.epsos.web.pages.PersonInfoPanel;

public class ModalWindowPopupColumn<T extends Person> extends AbstractColumn<T> {
	private static final long serialVersionUID = 2402448274237414275L;
	private ModalWindow modalWindow;

	public ModalWindowPopupColumn(IModel<String> displayModel, ModalWindow modalWindow) {
		super(displayModel);
		this.modalWindow = modalWindow;
	}

	@Override
	public void populateItem(final Item item, String componentId, final IModel rowModel) {
		item.add(new AjaxLink<T>("moreinfolink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Person person = (Person) rowModel.getObject();
				modalWindow.setTitle(person.getCommonName());
				modalWindow.setContent(new PersonInfoPanel(modalWindow.getContentId(), new CompoundPropertyModel(person)));
				modalWindow.show(target);
			}
		});
	}
}
