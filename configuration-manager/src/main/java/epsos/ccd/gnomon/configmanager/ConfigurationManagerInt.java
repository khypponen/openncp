/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012  SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact email: epsos@iuz.pt
 */
package epsos.ccd.gnomon.configmanager;

/**
 * Represents an interface for Configuration Service implementations.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public interface ConfigurationManagerInt {

    /**
     * Gets the value of a specific key stored in the properties file. It consists of key/value
     * pairs. First the method searches in the properties file.
     * Allowed syntax: key1=%key2%_value2. In this example the method first searches
     * for key2 in properties file and then to operating system variables
     * @param key the key for which we want to get its value
     * @return the string value of the key
     */
    String getProperty(String key);

    /**
     * Get the endpoint URL for a specified country and a service name
     * @param ISOCountryCode the iso country code
     * @param ServiceName the service name
     * @return
     */
    String getServiceWSE(String ISOCountryCode, String ServiceName);

    void setServiceWSE(String ISOCountryCode, String ServiceName, String URL);

    /**
     * Updates a property of the properties file
     * @param key the key we want to update its value
     * @param value the new value of the property
     * @return
     */
    String updateProperty(String key, String value);
    
}
