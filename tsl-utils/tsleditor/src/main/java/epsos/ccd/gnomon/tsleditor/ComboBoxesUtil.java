/***Licensed to the Apache Software Foundation (ASF) under one
*or more contributor license agreements.  See the NOTICE file
*distributed with this work for additional information
*regarding copyright ownership.  The ASF licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
**/
package epsos.ccd.gnomon.tsleditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;

/**
 * This is a utility class that reads the TSL Editor application properties from
 * the tsleditor.properties file and initializes variables like the available
 * language, country and locale codes, the available service status codes and
 * service identifier codes, according to the EPSOS specifications. These are
 * used for initializing an empty TSL list and also in order to perform
 * service-specific validations, in the utility class ValidatorUtil. The values
 * initialized here are used as available options in the combo boxes of the UI,
 * hence the class name.
 *
 * @author bouzianis
 */
public class ComboBoxesUtil {
	
	static Properties properties = new Properties();
	
	public static Vector<String> LANGUAGE_CODES;
	public static Vector<String> COUNTRY_CODES;
	private static HashMap<String, Locale> localesMap;
	public static Vector<String> SERVICE_STATUS_CODES;
	public static Vector<String> SERVICE_IDENTIFIER_CODES = new Vector<String>();

	public static String SERVICE_IDENTIFIER_VPN_GATEWAY = "http://uri.epsos.eu/Svc/Svctype/VPNGateway";
	public static String SERVICE_IDENTIFIER_NCP = "http://uri.epsos.eu/Svc/Svctype/NCP";
	public static String SERVICE_IDENTIFIER_PATIENT_IDENTIFICATION = "http://uri.epsos.eu/Svc/Svctype/PatientIdenitificationService";
	public static String SERVICE_IDENTIFIER_PATIENT_SERVICE = "http://uri.epsos.eu/Svc/Svctype/PatientService";
	public static String SERVICE_IDENTIFIER_ORDER_SERVICE = "http://uri.epsos.eu/Svc/Svctype/OrderService";
	public static String SERVICE_IDENTIFIER_DISPENSATION_SERVICE = "http://uri.epsos.eu/Svc/Svctype/DispensationService";
	public static String SERVICE_IDENTIFIER_CONSENT_SERVICE = "http://uri.epsos.eu/Svc/Svctype/ConsentService";
	public static String SERVICE_IDENTIFIER_IDENTITY_PROVIDER ="http://uri.epsos.eu/Svc/Svctype/IdV";
	public static String UPLOAD_URL_STRING = "ftp://admin:its1ofus@194.219.31.191/Alfresco/Projects/EPSOS/tsl_el.xml";

	static {
		File file = null;
		try {
			String propertyFile = "connection.properties";
			if (Thread.currentThread().getContextClassLoader().getResource(propertyFile) == null)
				throw new Exception("Can't find resource " + propertyFile);
			file = new File(Thread.currentThread().getContextClassLoader().getResource(propertyFile).toURI());
			properties.load(new FileInputStream(file));
			
			if(!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.vpn-gateway")))
				SERVICE_IDENTIFIER_VPN_GATEWAY = properties.getProperty("tsleditor.supported.service.identifier.vpn-gateway");
			
			if(!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.ncp")))
				SERVICE_IDENTIFIER_NCP = properties.getProperty("tsleditor.supported.service.identifier.ncp");
			
			if(!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.patient-identification")))
				SERVICE_IDENTIFIER_PATIENT_IDENTIFICATION = properties.getProperty("tsleditor.supported.service.identifier.patient-identification");
			
			if(!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.patient-service")))
				SERVICE_IDENTIFIER_PATIENT_SERVICE = properties.getProperty("tsleditor.supported.service.identifier.patient-service");
			
			if(!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.order-service")))
				SERVICE_IDENTIFIER_ORDER_SERVICE = properties.getProperty("tsleditor.supported.service.identifier.order-service");
			
			if(!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.dispensation-service")))
				SERVICE_IDENTIFIER_DISPENSATION_SERVICE = properties.getProperty("tsleditor.supported.service.identifier.dispensation-service");
			
			if(!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.consent-service")))
				SERVICE_IDENTIFIER_CONSENT_SERVICE = properties.getProperty("tsleditor.supported.service.identifier.consent-service");
			
			if(!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.identity-provider")))
				SERVICE_IDENTIFIER_IDENTITY_PROVIDER = properties.getProperty("tsleditor.supported.service.identifier.identity-provider");
			
			if(!ValidatorUtil.isNull(properties.getProperty("tsleditor.upload.url")))
				UPLOAD_URL_STRING = properties.getProperty("tsleditor.upload.url");
			
			// initialize the available language and country codes and the
			// locale map
			String localesString = properties.getProperty("tsleditor.supported.locales");
			if (ValidatorUtil.isNotNull(localesString)) {
				String[] locales = localesString.split(",");
				if (locales != null && locales.length > 0) {
					LANGUAGE_CODES = new Vector<String>();
					COUNTRY_CODES = new Vector<String>();
					localesMap = new HashMap<String, Locale>();
					for (String l : locales) {
						String lang = l.substring(0, l.indexOf("_"));
						String country = l.substring(l.indexOf("_") + 1);
						Locale loc = new Locale(lang, country);
						LANGUAGE_CODES.add(lang.toUpperCase());
						COUNTRY_CODES.add(country.toUpperCase());
						localesMap.put(lang.toUpperCase(), loc);
					}
				} else
					defaultLanguagesInitialization();
			} else
				defaultLanguagesInitialization();

			// initialize the list of identifiers used by the editor, if none is
			// provided, then the default list
			// of each individual identifier will be used
			String identifiersString = properties.getProperty("tsleditor.supported.service.identifiers");
			if (ValidatorUtil.isNotNull(identifiersString)) {
				String[] identifiers = identifiersString.split(",");
				if (identifiers != null && identifiers.length > 0) {
					SERVICE_IDENTIFIER_CODES = new Vector<String>();
					for (String ident : identifiers)
						SERVICE_IDENTIFIER_CODES.add(ident);
				} else
					defaultIdentifiersInitialization();
			} else
				defaultIdentifiersInitialization();

			// initialize the list of available status strings supported by the
			// editor
			String statussString = properties.getProperty("tsleditor.supported.service.status");
			if (ValidatorUtil.isNotNull(statussString)) {
				String[] statuses = statussString.split(",");
				if (statuses != null && statuses.length > 0) {
					SERVICE_STATUS_CODES = new Vector<String>();
					for (String status : statuses)
						SERVICE_STATUS_CODES.add(status);
				} else
					defaultStatusInitialization();
			} else
				defaultStatusInitialization();
		} catch (IOException e) {
			System.out.println("Can't load file at path: " + file.getAbsolutePath() + " Using default values...");
			defaultLanguagesInitialization();
			defaultIdentifiersInitialization();
			defaultStatusInitialization();
		} catch (Exception e) {
			defaultLanguagesInitialization();
			defaultIdentifiersInitialization();
			defaultStatusInitialization();

		}
	}
	
	private static void defaultIdentifiersInitialization() {
		SERVICE_IDENTIFIER_CODES = new Vector<String>();
		SERVICE_IDENTIFIER_CODES.add(SERVICE_IDENTIFIER_VPN_GATEWAY);
		SERVICE_IDENTIFIER_CODES.add(SERVICE_IDENTIFIER_NCP);
		SERVICE_IDENTIFIER_CODES.add(SERVICE_IDENTIFIER_PATIENT_IDENTIFICATION);
		SERVICE_IDENTIFIER_CODES.add(SERVICE_IDENTIFIER_PATIENT_SERVICE);
		SERVICE_IDENTIFIER_CODES.add(SERVICE_IDENTIFIER_ORDER_SERVICE);
		SERVICE_IDENTIFIER_CODES.add(SERVICE_IDENTIFIER_DISPENSATION_SERVICE);
		SERVICE_IDENTIFIER_CODES.add(SERVICE_IDENTIFIER_CONSENT_SERVICE);
		SERVICE_IDENTIFIER_CODES.add(SERVICE_IDENTIFIER_IDENTITY_PROVIDER);
	}

	private static void defaultStatusInitialization() {
		SERVICE_STATUS_CODES = new Vector<String>();
		SERVICE_STATUS_CODES.add("http://uri.etsi.org/TrstSvc/Svcstatus/inaccord");
		SERVICE_STATUS_CODES.add("http://uri.etsi.org/TrstSvc/Svcstatus/expired");
		SERVICE_STATUS_CODES.add("http://uri.etsi.org/TrstSvc/Svcstatus/suspended");
		SERVICE_STATUS_CODES.add("http://uri.etsi.org/TrstSvc/Svcstatus/revoked");
		SERVICE_STATUS_CODES.add("http://uri.etsi.org/TrstSvc/Svcstatus/notinaccord");
	}

	private static void defaultLanguagesInitialization() {
		LANGUAGE_CODES = new Vector<String>();
		LANGUAGE_CODES.add("EN");
		LANGUAGE_CODES.add("EL");
		LANGUAGE_CODES.add("DE");
		LANGUAGE_CODES.add("IT");
		LANGUAGE_CODES.add("NL");
		LANGUAGE_CODES.add("FR");

		COUNTRY_CODES = new Vector<String>();
		COUNTRY_CODES.add("UK");
		COUNTRY_CODES.add("GR");
		COUNTRY_CODES.add("DE");
		COUNTRY_CODES.add("IT");
		COUNTRY_CODES.add("NL");
		COUNTRY_CODES.add("FR");

		localesMap = new HashMap<String, Locale>();
		localesMap.put("EN", new Locale("en", "UK"));
		localesMap.put("EL", new Locale("el", "GR"));
		localesMap.put("DE", new Locale("de", "DE"));
		localesMap.put("IT", new Locale("it", "IT"));
		localesMap.put("NL", new Locale("nl", "NL"));
		localesMap.put("FR", new Locale("fr", "FR"));
	}

	/**
	 * A method to return a locale, based on the given language code in upper
	 * case letters, as it comes from the UI combo box options. It uses the
	 * locale map created in this class during initialization of
	 * country/language/locale codes from tsleditor.properties to return the
	 * Locale object corresponding to the given language code .
	 *
	 * @param languageCode
	 *            the language code (in upper case letters) to look for in the
	 *            locale map
	 * @return the locale corresponding to the given language code, based on the
	 *         locale map
	 */
	public static Locale getLocaleFromLanguageCode(String languageCode) {
		if (languageCode == null)
			return Locale.getDefault();

		Locale loc = localesMap.get(languageCode);
		if (loc == null)
			return Locale.getDefault();
		else
			return loc;
	}

}
