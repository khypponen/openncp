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
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CostumPagingNavigator<T> extends AjaxPagingNavigator {
	private static final long serialVersionUID = -7729571972182960330L;
	protected static Logger LOGGER = LoggerFactory.getLogger(CostumPagingNavigator.class);

	public CostumPagingNavigator(String id, IPageable pageable) {
		super(id, pageable);
	}
	
	/*
	 * Handling everything in the pager except 'first' and 'last'.
	 * @see org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator#onAjaxEvent(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onAjaxEvent(AjaxRequestTarget target) {
		super.onAjaxEvent(target);
	}

	@Override
	protected AjaxPagingNavigationLink newPagingNavigationLink(String id, IPageable pageable, final int pageNumber) {
		return new AjaxPagingNavigationLink(id, pageable, pageNumber) {
			private static final long serialVersionUID = 1L;
			
			/*
			 * Handling 'first' and 'last' of the pager.
			 * @see org.apache.wicket.markup.html.navigation.paging.PagingNavigationLink#onClick()
			 */
			@Override
			public void onClick(AjaxRequestTarget target) {
				super.onClick(target);
			}

			/*
			 * Removing visibility of 'first' or 'last' depending on witch part that is selected.
			 * @see org.apache.wicket.Component#isVisible()
			 */
			@Override
			public boolean isVisible() {
				return !linksTo(getPage());
			}
		};
	}
}
