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

public class NullFlavorManager {
	public static final String CONFIG_PREFIX = "value.";
	
	public static String getNullFlavor(String key) {
		String locale = MasterConfigManager.get("ApplicationConfigManager.locale");
		if (locale != null && locale.contains("_")) {
			String[] localeSplit = locale.split("_");
            if (localeSplit.length == 2) {
            	return MasterConfigManager.get(CONFIG_PREFIX + key + "[@" + localeSplit[1] + "]");
            }
		}     
		return MasterConfigManager.get(CONFIG_PREFIX + key + "[@GB]");
	}
}
