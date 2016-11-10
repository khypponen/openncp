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
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public abstract class CheckBoxPanel extends Panel {
	private static final long serialVersionUID = 7040178732710491350L;
	public CheckBoxPanel(String id, IModel<?> model) {
		super(id, model);
		CheckBox checkbox = new CheckBox("checkbox");
		checkbox.add(new AjaxFormSubmitBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				CheckBoxPanel.this.onSubmit();
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				
			}
			
		});
		add(checkbox);
	}
	
	public abstract void onSubmit();
}
