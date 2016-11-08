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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.protocol.http.BufferedWebResponse;
import org.apache.wicket.protocol.http.MockHttpServletRequest;
import org.apache.wicket.protocol.http.MockHttpServletResponse;
import org.apache.wicket.protocol.http.MockHttpSession;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.request.WebErrorCodeResponseTarget;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.target.component.BookmarkablePageRequestTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageConverter {
        private static final Logger LOGGER = LoggerFactory.getLogger(PageConverter.class) ;
    
	/**
	 * Renders given page
	 * 
	 * @param pageClass to render
	 * @param pageParameters to render with
	 * @return String of HTML produced
	 */
	public static String renderPageToString(final Class<? extends Page> pageClass, final PageParameters pageParameters) {
        LOGGER.debug("rednerPageToString: " +pageClass.getSimpleName() + "?"+pageParameters.toString());
		WebApplication webApplication = WebApplication.get();
		ServletContext servletContext = webApplication.getServletContext();
		MockHttpSession servletSession = new MockHttpSession(servletContext);
		servletSession.setTemporary(true);

		MockHttpServletRequest servletRequest = new MockHttpServletRequest(webApplication, servletSession, servletContext);
		MockHttpServletResponse servletResponse = new MockHttpServletResponse(servletRequest);
		servletRequest.initialize();
		servletResponse.initialize();

		WebRequest webRequest = new ServletWebRequest(servletRequest);

		BufferedWebResponse webResponse = new BufferedWebResponse(servletResponse);
		webResponse.setAjax(true);

		WebRequestCycle htmlRequestCycle = new WebRequestCycle(webApplication, webRequest, webResponse);

		BookmarkablePageRequestTarget htmlTarget = new BookmarkablePageRequestTarget(pageClass, pageParameters);

		htmlRequestCycle.setRequestTarget(htmlTarget);

		try {
			htmlRequestCycle.getProcessor().respond(htmlRequestCycle);
			if (htmlRequestCycle.wasHandled() == false) {
				htmlRequestCycle.setRequestTarget(new WebErrorCodeResponseTarget(HttpServletResponse.SC_NOT_FOUND));
			}
			htmlRequestCycle.detach();
		} finally {
			htmlRequestCycle.getResponse().close();
		}

		return webResponse.toString();
	}

}
