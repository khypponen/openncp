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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeatureFlagsManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(FeatureFlagsManager.class);

	private static final String PROPERTY_PREFIX = "FeatureFlagsManager.features.";

	/**
	 * Checks a feaure for on/off. if it not set at all it will the features 'defaultOn' setting
	 * 
	 * @param feature
	 * @return
	 */
	public static boolean check(Feature feature) {
		boolean result = MasterConfigManager.getBoolean(PROPERTY_PREFIX + feature, Boolean.valueOf(feature.defaultOn));
		LOGGER.debug("Feature check: " + feature + "=" + result);
		return result;
	}

}
