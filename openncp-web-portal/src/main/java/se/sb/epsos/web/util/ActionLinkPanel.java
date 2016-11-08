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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import se.sb.epsos.web.model.LinkAction;
import se.sb.epsos.web.service.MetaDocument;

public class ActionLinkPanel<T extends LinkAction> extends Panel {
	private static final long serialVersionUID = 1L;

	public ActionLinkPanel(String id, final IModel<? extends LinkAction> model) {
		super(id);
		if (model.getObject().isAjax()) {
			AjaxLink<T> link = new AjaxLink<T>("link") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					model.getObject().onClick(target);
				}
			};
			link.add(new Label("label", model.getObject().getAction()));
			link.setVisible(!model.getObject().isDisabled());
			add(link);
		} else if (model.getObject().isResource()) {
			ResourceLink<MetaDocument> link = new ResourceLink<MetaDocument>("link", model.getObject().getDynamicResource()) {
				private static final long serialVersionUID = 4551495809366034555L;

				@Override
				public void onClick() {
					model.getObject().onClick(null);
				}
			};
			if (model.getObject().getPopupSettings() != null) {
				link.setPopupSettings(model.getObject().getPopupSettings());
			}
			link.add(new Label("label", model.getObject().getAction()));
			link.setVisible(!model.getObject().isDisabled());
			add(link);
		} else {
			Link<T> link = new Link<T>("link") {
				private static final long serialVersionUID = 2479499536142009834L;

				@Override
				public void onClick() {
					model.getObject().onClick(null);
				}
			};
			if (model.getObject().getPopupSettings() != null) {
				link.setPopupSettings(model.getObject().getPopupSettings());
			}
			link.add(new Label("label", model.getObject().getAction()));
			link.setVisible(!model.getObject().isDisabled());
			add(link);
		}
	}
}
