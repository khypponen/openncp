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

import se.sb.epsos.web.util.MasterConfigManager;

public class AuditHandlerConfigManager implements Serializable {
    private static final long serialVersionUID = -968106064536729654L;
    public static final String CONFIG_PREFIX = "AuditHandlerConfigManager.";
    public static final String NCP_IP = "NcpIp";
    public static final String SeCountryCode = "SECountryCode";

    public static String getNcpIp() {
        return MasterConfigManager.get(CONFIG_PREFIX + NCP_IP);
    }
    
    public static String getSeCountryCode(){
        return MasterConfigManager.get(CONFIG_PREFIX + SeCountryCode);
    }

}
