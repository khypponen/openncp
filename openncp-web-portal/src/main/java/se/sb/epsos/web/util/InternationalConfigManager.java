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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.model.CountryVO;

public class InternationalConfigManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(InternationalConfigManager.class);
	private static Map<String, XMLConfiguration> config = new HashMap<String, XMLConfiguration>();

	public InternationalConfigManager(List<CountryVO> countryCodes) {
		super();
		String path = System.getProperty("epsos-internationalsearch-config-path");
		LOGGER.debug("epsos-internationalsearch-config-path: " + path);
		if (path == null) {
			final String err = "epsos-internationalsearch-config-path is not set";
			LOGGER.error(err);
			throw new IllegalArgumentException(err);
		}
		for (CountryVO countryCode : countryCodes) {
			File iSearchFile = new File(path + "/InternationalSearch_" + countryCode.getId() + ".xml");
			LOGGER.info("Path: " + iSearchFile.getPath());
			if (iSearchFile.exists() && iSearchFile.isFile()) {
				try {
					config.put(countryCode.getId(), new XMLConfiguration(iSearchFile));
				} catch (ConfigurationException e) {
					LOGGER.error("ConfigurationException", e);
				}
			}
		}
	}

	public static List<Properties> getProperties(String country, String prefix) {
		XMLConfiguration xmlConfig = config.get(country);
		List<Properties> propList = new ArrayList<Properties>();
		if (xmlConfig != null) {
			Iterator<?> keys = xmlConfig.getKeys(prefix);
			while (keys.hasNext()) {
				String key = (String) keys.next();
				List<String> list = getList(country, key);
				int i = 0;
				for (String str : list) {
					Properties props = new Properties();
					props.put(key + i, str);
					propList.add(props);
					i++;
				}
			}
		}
		return propList;
	}

	public static String get(String country, String key) {
		XMLConfiguration xmlConfig = config.get(country);
		return xmlConfig.getString(key);
	}

	public static List<String> getList(String country, String key) {
		XMLConfiguration xmlConfig = config.get(country);
		if (xmlConfig == null)
			return Collections.emptyList();
		return (List<String>) xmlConfig.getList(key);
	}
}
