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

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MasterConfigManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(MasterConfigManager.class);

	public static final String CONFIG_PROPERTY = "epsos-web.master.cfg";
	private static final String DEFAULT_CONFIG_FILE = "config/master-config.xml";
	private static MasterConfigManager instance;

	private Configuration config = null;

	private static MasterConfigManager getInstance() {
		if (instance == null) {
			instance = new MasterConfigManager();
		}
		return instance;
	}

	/**
	 * Flushes all configuration
	 */
	public static void clear() {
		if (instance != null && instance.config != null) {
			instance.config.clear();
		}
		instance = null;
	}

	public static String get(String key) {
		if (getInstance().config == null)
			return null;
		return getInstance().config.getString(key);
	}

	public static int getInt(String key) {
		if (getInstance().config == null)
			return -1;
		return getInstance().config.getInt(key);
	}

	public static Boolean getBoolean(String key) {
		if (getInstance().config == null)
			return null;
		return getInstance().config.getBoolean(key);
	}

	public static Boolean getBoolean(String key, Boolean defaultValue) {
		if (getInstance().config == null)
			return null;
		return getInstance().config.getBoolean(key, defaultValue);
	}

	public static boolean contains(String key) {
		if (getInstance().config == null)
			return false;
		return getInstance().config.containsKey(key);
	}

	@SuppressWarnings("unchecked")
	public static List<String> getList(String key) {
		if (getInstance().config == null)
			return null;
		return getInstance().config.getList(key);
	}

	public static Properties getProperties(String prefix) {
		if (getInstance().config == null)
			return null;
		Properties props = new Properties();
		Iterator<?> keys = getInstance().config.getKeys(prefix);
		while (keys.hasNext()) {
			String key = (String) keys.next();
			props.put(key, get(key));
		}
		return props;
	}

	public static Configuration getConfig() {
		return getInstance().config;
	}

	private MasterConfigManager() {
		super();
		String cfgFilePath = System.getProperty(CONFIG_PROPERTY);
		if (cfgFilePath == null || "".equals(cfgFilePath)) {
			LOGGER.warn("No excplicit master config file found in system property '" + CONFIG_PROPERTY + "', using default bundled config");
			cfgFilePath = getClass().getClassLoader().getResource(DEFAULT_CONFIG_FILE).getPath();
		}
		if (!System.getProperties().containsKey("override-config.xml")) {
			System.setProperty("override-config.xml", "override-config.xml");
		}
		ConfigurationFactory factory = new ConfigurationFactory(cfgFilePath);
		try {
			this.config = factory.getConfiguration();
		} catch (ConfigurationException e) {
			LOGGER.error("Failed to initilaize master config", e);
		}
		if (config != null) {
			LOGGER.debug("Master configuration read sucessfully!");
		}
	}
}
