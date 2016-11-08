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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class ConfirmationDialog extends AttributeModifier {
	private static final long serialVersionUID = -3591656109102720432L;

	public ConfirmationDialog(final IModel<String> message) {
		super("onclick", true, new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = -2616146729785362311L;

			public String getObject() {
				return "return confirm('" + message.getObject() + "');";
			}
		});
	}
}
