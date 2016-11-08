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

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

public class OpenOnLoadModalWindow extends ModalWindow implements IHeaderContributor {
	private static final long serialVersionUID = 1L;
	private boolean visible = false;
	public OpenOnLoadModalWindow(String id, boolean visible) {
		super(id);
		this.visible = visible;
	}

	public OpenOnLoadModalWindow(String id, IModel<?> model) {
		super(id, model);
	}

	public void renderHead(IHeaderResponse response) {
		response.renderOnDomReadyJavascript("Wicket.Window.unloadConfirmation = false;");
		if (visible == true) {
			response.renderOnDomReadyJavascript(getWindowOpenJavascript());
		} else {
			response.renderOnDomReadyJavascript(getCloseJavacript());
		}
	}
}
