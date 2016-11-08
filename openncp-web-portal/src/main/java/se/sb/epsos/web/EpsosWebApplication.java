/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web;

import java.net.URL;

import org.apache.wicket.Page;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.settings.IRequestCycleSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import se.sb.epsos.web.admin.StatusPage;
import se.sb.epsos.web.auth.LoginPage;
import se.sb.epsos.web.pages.ErrorPage;
import se.sb.epsos.web.pages.ExpiredErrorPage;
import se.sb.epsos.web.pages.QueryPersonPage;
import se.sb.epsos.web.service.DocumentCache;
import se.sb.epsos.web.service.NcpServiceConfigManager;
import se.sb.epsos.web.service.NcpServiceFacadeImpl;
import se.sb.epsos.web.service.PersonCache;
import se.sb.epsos.web.util.Feature;
import se.sb.epsos.web.util.FeatureFlagsManager;

public class EpsosWebApplication extends AuthenticatedWebApplication implements ApplicationContextAware {
	boolean isInitialized = false;
    private static final String TLS = "tls";
	private static final Logger LOGGER = LoggerFactory.getLogger(EpsosWebApplication.class);
	private ApplicationContext ctx;
	private String version;

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init() {
		if (!isInitialized) {
			super.init();
			setListeners();
			isInitialized = true;
			setSSL();
			mountBookmarkablePage("/ErrorPage", ErrorPage.class);
			mountBookmarkablePage("/ExpiredErrorPage", ExpiredErrorPage.class);
			mountBookmarkablePage("/QueryPerson", QueryPersonPage.class);
            if(FeatureFlagsManager.check(Feature.EXAMPLE_FEATURE)){
                mountBookmarkablePage("/Status", StatusPage.class);
            }
			getApplicationSettings().setPageExpiredErrorPage(ExpiredErrorPage.class);
			getApplicationSettings().setAccessDeniedPage(ErrorPage.class);
			getApplicationSettings().setInternalErrorPage(ErrorPage.class);
			getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
			getRequestCycleSettings().setRenderStrategy(IRequestCycleSettings.ONE_PASS_RENDER);
			

		}
	}

	private void setListeners() {
		addComponentInstantiationListener(new SpringComponentInjector(this, ctx, true));
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginPage.class;
	}

	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		return EpsosAuthenticatedWebSession.class;
	}

	@Override
	public void sessionDestroyed(String sessionId) {
		super.sessionDestroyed(sessionId);
		DocumentCache.getInstance().flush(sessionId);
		PersonCache.getInstance().flush(sessionId);
	}

	@Override
	public Class<? extends Page> getHomePage() {
		AuthenticatedWebSession session = AuthenticatedWebSession.get();
		if (session == null || !session.isSignedIn()) {
			return getSignInPageClass();
		}
		return QueryPersonPage.class;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}
        
    public ApplicationContext getApplicationContext() {
        return this.ctx;
    }

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setSSL() {
		if (FeatureFlagsManager.check(Feature.ENABLE_SSL)) {
			LOGGER.debug("Enabling SSL configuration for outbound connections");

			System.setProperty("javax.net.ssl.trustStore", NcpServiceConfigManager.getTruststoreLocation(TLS));
	        System.setProperty("javax.net.ssl.trustStorePassword", NcpServiceConfigManager.getTruststorePassword(TLS));

	        System.setProperty("javax.net.ssl.keyStore", NcpServiceConfigManager.getPrivateKeystoreLocation(TLS));
	        System.setProperty("javax.net.ssl.keyStorePassword", NcpServiceConfigManager.getPrivateKeystorePassword(TLS));

	        System.setProperty("javax.net.ssl.key.alias", NcpServiceConfigManager.getPrivateKeyAlias(TLS));
	        System.setProperty("javax.net.ssl.privateKeyPassword", NcpServiceConfigManager.getPrivateKeyPassword(TLS));
		} else {
			LOGGER.debug("SSL configuration for outbound connections disabled");
		}
	}
}
