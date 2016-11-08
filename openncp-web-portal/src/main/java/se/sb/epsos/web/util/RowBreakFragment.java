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

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.panel.Fragment;


public class RowBreakFragment extends Fragment {
	private static final long serialVersionUID = 2336292306423214851L;

	public RowBreakFragment(String id, String fragmentId, MarkupContainer container) {
		super(id, fragmentId, container);
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		tag.setIgnore(true);
		tag.setName("tr");
		super.onComponentTag(tag);
	}
}