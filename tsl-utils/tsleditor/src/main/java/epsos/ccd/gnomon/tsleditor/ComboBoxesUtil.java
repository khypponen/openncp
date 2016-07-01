/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 *
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
	public static Vector<String> SERVICE_IDENTIFIER_CODES = new Vector<>();

	public static String SERVICE_IDENTIFIER_VPN_GATEWAY = "http://uri.epsos.eu/Svc/Svctype/VPNGateway";
	public static String SERVICE_IDENTIFIER_NCP = "http://uri.epsos.eu/Svc/Svctype/NCP";
	public static String SERVICE_IDENTIFIER_PATIENT_IDENTIFICATION = "http://uri.epsos.eu/Svc/Svctype/PatientIdenitificationService";
	public static String SERVICE_IDENTIFIER_PATIENT_SERVICE = "http://uri.epsos.eu/Svc/Svctype/PatientService";
	public static String SERVICE_IDENTIFIER_ORDER_SERVICE = "http://uri.epsos.eu/Svc/Svctype/OrderService";
	public static String SERVICE_IDENTIFIER_DISPENSATION_SERVICE = "http://uri.epsos.eu/Svc/Svctype/DispensationService";
	public static String SERVICE_IDENTIFIER_CONSENT_SERVICE = "http://uri.epsos.eu/Svc/Svctype/ConsentService";
	public static String SERVICE_IDENTIFIER_IDENTITY_PROVIDER = "http://uri.epsos.eu/Svc/Svctype/IdV";
	public static String UPLOAD_URL_STRING = "ftp://admin:its1ofus@194.219.31.191/Alfresco/Projects/EPSOS/tsl_el.xml";

	private ComboBoxesUtil() {
		super();
	}

	static {

		File file = null;
		try {
			String propertyFile = "connection.properties";
			if (Thread.currentThread().getContextClassLoader().getResource(propertyFile) == null)
				throw new Exception("Can't find resource " + propertyFile);
			file = new File(Thread.currentThread().getContextClassLoader().getResource(propertyFile).toURI());
			properties.load(new FileInputStream(file));

			if (!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.vpn-gateway")))
				SERVICE_IDENTIFIER_VPN_GATEWAY = properties
						.getProperty("tsleditor.supported.service.identifier.vpn-gateway");

			if (!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.ncp")))
				SERVICE_IDENTIFIER_NCP = properties.getProperty("tsleditor.supported.service.identifier.ncp");

			if (!ValidatorUtil
					.isNull(properties.getProperty("tsleditor.supported.service.identifier.patient-identification")))
				SERVICE_IDENTIFIER_PATIENT_IDENTIFICATION = properties
						.getProperty("tsleditor.supported.service.identifier.patient-identification");

			if (!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.patient-service")))
				SERVICE_IDENTIFIER_PATIENT_SERVICE = properties
						.getProperty("tsleditor.supported.service.identifier.patient-service");

			if (!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.order-service")))
				SERVICE_IDENTIFIER_ORDER_SERVICE = properties
						.getProperty("tsleditor.supported.service.identifier.order-service");

			if (!ValidatorUtil
					.isNull(properties.getProperty("tsleditor.supported.service.identifier.dispensation-service")))
				SERVICE_IDENTIFIER_DISPENSATION_SERVICE = properties
						.getProperty("tsleditor.supported.service.identifier.dispensation-service");

			if (!ValidatorUtil.isNull(properties.getProperty("tsleditor.supported.service.identifier.consent-service")))
				SERVICE_IDENTIFIER_CONSENT_SERVICE = properties
						.getProperty("tsleditor.supported.service.identifier.consent-service");

			if (!ValidatorUtil
					.isNull(properties.getProperty("tsleditor.supported.service.identifier.identity-provider")))
				SERVICE_IDENTIFIER_IDENTITY_PROVIDER = properties
						.getProperty("tsleditor.supported.service.identifier.identity-provider");

			if (!ValidatorUtil.isNull(properties.getProperty("tsleditor.upload.url")))
				UPLOAD_URL_STRING = properties.getProperty("tsleditor.upload.url");

			// initialize the available language and country codes and the
			// locale map
			String localesString = properties.getProperty("tsleditor.supported.locales");
			if (ValidatorUtil.isNotNull(localesString)) {
				String[] locales = localesString.split(",");
				if (locales != null && locales.length > 0) {
					LANGUAGE_CODES = new Vector<>();
					COUNTRY_CODES = new Vector<>();
					localesMap = new HashMap<>();
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
					SERVICE_IDENTIFIER_CODES = new Vector<>();
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
					SERVICE_STATUS_CODES = new Vector<>();
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
		SERVICE_IDENTIFIER_CODES = new Vector<>();
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
		SERVICE_STATUS_CODES = new Vector<>();
		SERVICE_STATUS_CODES.add("http://uri.etsi.org/TrstSvc/Svcstatus/inaccord");
		SERVICE_STATUS_CODES.add("http://uri.etsi.org/TrstSvc/Svcstatus/expired");
		SERVICE_STATUS_CODES.add("http://uri.etsi.org/TrstSvc/Svcstatus/suspended");
		SERVICE_STATUS_CODES.add("http://uri.etsi.org/TrstSvc/Svcstatus/revoked");
		SERVICE_STATUS_CODES.add("http://uri.etsi.org/TrstSvc/Svcstatus/notinaccord");
	}

	private static void defaultLanguagesInitialization() {
		LANGUAGE_CODES = new Vector<>();
		LANGUAGE_CODES.add("BG");
		LANGUAGE_CODES.add("ES");
		LANGUAGE_CODES.add("CS");
		LANGUAGE_CODES.add("DA");
		LANGUAGE_CODES.add("DE");
		LANGUAGE_CODES.add("ET");
		LANGUAGE_CODES.add("EL");
		LANGUAGE_CODES.add("EN");
		LANGUAGE_CODES.add("FR");
		LANGUAGE_CODES.add("GA");
		LANGUAGE_CODES.add("HR");
		LANGUAGE_CODES.add("IT");
		LANGUAGE_CODES.add("LV");
		LANGUAGE_CODES.add("LT");
		LANGUAGE_CODES.add("HU");
		LANGUAGE_CODES.add("MT");
		LANGUAGE_CODES.add("NL");
		LANGUAGE_CODES.add("PL");
		LANGUAGE_CODES.add("PT");
		LANGUAGE_CODES.add("RO");
		LANGUAGE_CODES.add("SK");
		LANGUAGE_CODES.add("SL");
		LANGUAGE_CODES.add("FI");
		LANGUAGE_CODES.add("SV");

		COUNTRY_CODES = new Vector<>();
		COUNTRY_CODES.add("BE");
		COUNTRY_CODES.add("BG");
		COUNTRY_CODES.add("CZ");
		COUNTRY_CODES.add("DK");
		COUNTRY_CODES.add("DE");
		COUNTRY_CODES.add("EE");
		COUNTRY_CODES.add("IE");
		COUNTRY_CODES.add("GR");
		COUNTRY_CODES.add("ES");
		COUNTRY_CODES.add("FR");
		COUNTRY_CODES.add("HR");
		COUNTRY_CODES.add("IT");
		COUNTRY_CODES.add("CY");
		COUNTRY_CODES.add("LV");
		COUNTRY_CODES.add("LT");
		COUNTRY_CODES.add("LU");
		COUNTRY_CODES.add("HU");
		COUNTRY_CODES.add("MT");
		COUNTRY_CODES.add("NL");
		COUNTRY_CODES.add("AT");
		COUNTRY_CODES.add("PL");
		COUNTRY_CODES.add("PT");
		COUNTRY_CODES.add("RO");
		COUNTRY_CODES.add("SI");
		COUNTRY_CODES.add("SK");
		COUNTRY_CODES.add("FI");
		COUNTRY_CODES.add("SE");
		COUNTRY_CODES.add("GB");
		COUNTRY_CODES.add("IS");
		COUNTRY_CODES.add("LI");
		COUNTRY_CODES.add("NO");
		COUNTRY_CODES.add("CH");
		COUNTRY_CODES.add("EU");

		localesMap = new HashMap<String, Locale>();
		localesMap = new HashMap<String, Locale>();
		// localesMap.put("BE", new Locale("fr", "BE"));
		// localesMap.put("BG", new Locale("bg", ""));
		// localesMap.put("CZ", new Locale("cz", ""));
		// localesMap.put("DA", new Locale("da", ""));
		localesMap.put("DE", new Locale("de", "DE"));
		// localesMap.put("EE", new Locale("ee", ""));
		// localesMap.put("GA", new Locale("ga", ""));
		localesMap.put("EL", new Locale("el", "GR"));
		localesMap.put("ES", new Locale("es", "ES"));
		localesMap.put("FR", new Locale("fr", "FR"));
		// localesMap.put("HR", new Locale("hr", ""));
		localesMap.put("IT", new Locale("it", "IT"));
		// localesMap.put("CY", new Locale("cy", ""));
		// localesMap.put("LV", new Locale("", ""));
		// localesMap.put("LT", new Locale("", ""));
		// localesMap.put("LU", new Locale("", ""));
		// localesMap.put("HU", new Locale("", ""));
		// localesMap.put("MT", new Locale("", ""));
		// localesMap.put("NL", new Locale("nl", "NL"));
		// localesMap.put("AT", new Locale("", ""));
		// localesMap.put("PL", new Locale("", ""));
		localesMap.put("PT", new Locale("pt", "PT"));
		// localesMap.put("RO", new Locale("", ""));
		// localesMap.put("SI", new Locale("", ""));
		// localesMap.put("SK", new Locale("", ""));
		// localesMap.put("FI", new Locale("", ""));
		// localesMap.put("SE", new Locale("", ""));
		// localesMap.put("EN", new Locale("en", "GB"));
		// localesMap.put("IS", new Locale("", ""));
		// localesMap.put("LI", new Locale("", ""));
		// localesMap.put("NO", new Locale("", ""));
		// localesMap.put("CH", new Locale("", ""));
		localesMap.put("EU", new Locale("en", "EU"));
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
