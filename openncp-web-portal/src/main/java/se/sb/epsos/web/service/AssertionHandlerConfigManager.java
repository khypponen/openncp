/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import se.sb.epsos.web.util.MasterConfigManager;

/**
 * Created by IntelliJ IDEA.
 * User: andreas
 * Date: 2011-06-27
 * Time: 13.23
 * To change this template use File | Settings | File Templates.
 */
public class AssertionHandlerConfigManager implements Serializable {
	private static final long serialVersionUID = -968106064536729654L;
	public static final String CONFIG_PREFIX = "AssertionHandlerConfigManager.";
    public static final String ROLE_PERMISSIONS_PREFIX = "RolePermissions.";

    private static final String PERSMISSIONS_PREFIX = "PermissionsPrefix";
    private static final String FACILITY_TYPE = "FacilityType";
    private  static final String PURPOSE_OF_USE = "PurposeOfUse";
    public static final String XSPA_LOCALITY = "XspaLocality";

    public static String getPersmissionsPrefix() {
        return MasterConfigManager.get(CONFIG_PREFIX+ROLE_PERMISSIONS_PREFIX+PERSMISSIONS_PREFIX);
    }

    public static String getFacilityType(String role) {
        return MasterConfigManager.get(CONFIG_PREFIX+ROLE_PERMISSIONS_PREFIX+role+"[@workPlace]");
    }

    public static String getPurposeOfUse() {
        return MasterConfigManager.get(CONFIG_PREFIX+PURPOSE_OF_USE);
    }

    public static String getXspaLocality() {
        return MasterConfigManager.get(CONFIG_PREFIX + XSPA_LOCALITY);
    }

    public static String getRoleDisplayName(String role) {
        return MasterConfigManager.get(CONFIG_PREFIX+ROLE_PERMISSIONS_PREFIX+role+"[@displayName]");
    }

    public static Set<String> getPersmissions(String role) {
        String permissionsString = MasterConfigManager.get(CONFIG_PREFIX+ROLE_PERMISSIONS_PREFIX+role);
        Set<String> permissions = new HashSet<String>();
        if (permissionsString!=null) {
            String[] splittedPersmissions = permissionsString.split(",");
            permissions.addAll(Arrays.asList(splittedPersmissions));
        }
        return permissions;
    }

}
