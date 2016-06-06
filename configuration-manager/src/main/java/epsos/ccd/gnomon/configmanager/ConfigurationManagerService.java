/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package epsos.ccd.gnomon.configmanager;

import eu.epsos.configmanager.database.HibernateConfigFile;

/**
 *
 * This service provides access to the system defined properties
 *
 * @author Kostas Karkaletsis
 * @author Organization: Gnomon
 * @author mail:k.karkaletsis@gnomon.com.gr
 * @version 1.0, 2010, 30 Jun
 *
 */
public class ConfigurationManagerService {

    private volatile static ConfigurationManagerService instance;

    public static synchronized ConfigurationManagerService getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManagerService.class) {
                instance = new ConfigurationManagerService();
            }
        }
        return instance;


    }

    public static synchronized ConfigurationManagerService getInstance(String hibernateFilePath) {
        if (instance == null) {
            synchronized (ConfigurationManagerService.class) {
                HibernateConfigFile.name = hibernateFilePath;
                instance = new ConfigurationManagerService();
            }
        }
        return instance;


    }
    /**
     * Gets the value of a specific key stored in the properties file. It
     * consists of key/value pairs. First the method searches in the properties
     * file. Allowed syntax: key1=%key2%_value2. In this example the method
     * first searches for key2 in properties file and then to operating system
     * variables
     *
     * @param key the key for which we want to get its value
     * @return the string value of the key
     */
    public String getProperty(String key) {
        return ConfigurationManagerDb.getInstance().getProperty(key);
    }

    /**
     * Updates a property of the properties file
     *
     * @param key the key we want to update its value
     * @param value the new value of the property
     * @return
     */
    public String updateProperty(
            String key,
            String value) {
        return ConfigurationManagerDb.getInstance().updateProperty(key, value);
    }

    /**
     * Get the endpoint URL for a specified country and a service name
     *
     * @param ISOCountryCode the iso country code
     * @param ServiceName the service name
     * @return
     */
    public String getServiceWSE(
            String ISOCountryCode,
            String ServiceName) {
        return getProperty(ISOCountryCode + "." + ServiceName + ".WSE");
    }

    /**
     * This method updates in the properties file for a given country code and
     * service name the service endpoint
     *
     * @param ISOCountryCode the country code
     * @param ServiceName the service name
     * @param URL the endpoint url to be set
     */
    public void setServiceWSE(
            String ISOCountryCode,
            String ServiceName,
            String URL) {
        updateProperty(ISOCountryCode + "." + ServiceName + ".WSE", URL);
    }
}
