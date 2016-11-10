/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/
package se.sb.epsos.web.service;

import epsos.ccd.gnomon.configmanager.OLDConfigurationManagerDb;

public class NcpServiceConfigManager {

	private static final String CONFIG_PREFIX = "NcpServiceConfigManager.";
	private static final String SECURITY = "security.";
	private static final String PRIVATE_KEYSTORE_LOCATION = ".PrivateKeystoreLocation";
	private static final String PRIVATE_KEYSTORE_PASSWORD = ".PrivateKeystorePassword";
	private static final String PRIVATE_KEY_ALIAS = ".PrivateKeyAlias";
	private static final String TRUSTSTORE_LOCATION = ".TruststoreLocation";
	private static final String TRUSTSTORE_PASSWORD = ".TruststorePassword";
	private static final String SERVICE_URL = "service.Url";
	private static final String PRIVATE_KEY_PASSWORD = ".PrivateKeyPassword";
	private static final String DOCUMENTS_DATATABLE_PAGE_SIZE = "DocumentsDatatablePageSize";

	private static String getProperty(String property) {
		return OLDConfigurationManagerDb.getInstance().getProperty(property);
	}

	public static String getPrivateKeystoreLocation(String assertionortls) {
		return getProperty(CONFIG_PREFIX + SECURITY + assertionortls + PRIVATE_KEYSTORE_LOCATION);
	}

	public static String getPrivateKeystorePassword(String assertionortls) {
		return getProperty(CONFIG_PREFIX + SECURITY + assertionortls + PRIVATE_KEYSTORE_PASSWORD);
	}

	public static String getPrivateKeyAlias(String assertionortls) {
		return getProperty(CONFIG_PREFIX + SECURITY + assertionortls + PRIVATE_KEY_ALIAS);
	}

	public static String getTruststoreLocation(String assertionortls) {
		return getProperty(CONFIG_PREFIX + SECURITY + assertionortls + TRUSTSTORE_LOCATION);
	}

	public static String getTruststorePassword(String assertionortls) {
		return getProperty(CONFIG_PREFIX + SECURITY + assertionortls + TRUSTSTORE_PASSWORD);
	}

	public static String getServiceUrl() {
		return getProperty(CONFIG_PREFIX + SERVICE_URL);
	}

	public static String getDocumentsDatatablePageSize() {
		try {
			return getProperty(CONFIG_PREFIX + DOCUMENTS_DATATABLE_PAGE_SIZE);
		} catch (Error e) {
			// Hack for tests. Missing HibernateUtil and Hibernate configuration
			// file for ConfigurationManagerDb.
			// (NoClassDefFoundError and ExceptionInInitializerError)
			return "";
		}
	}

	public static String getPrivateKeyPassword(String assertionortls) {
		return getProperty(CONFIG_PREFIX + SECURITY + assertionortls + PRIVATE_KEY_PASSWORD);
	}
}
