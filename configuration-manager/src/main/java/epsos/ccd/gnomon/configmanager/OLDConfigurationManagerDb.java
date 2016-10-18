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

import eu.epsos.configmanager.database.HibernateUtil;
import eu.epsos.configmanager.database.model.Property;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration Manager Database Version.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class ConfigurationManagerDb implements ConfigurationManagerInt {

    private static Logger logger = LoggerFactory.getLogger(ConfigurationManagerService.class);
    private volatile static ConfigurationManagerDb instance;

    /**
     * Returns a ConfigurationManagerDb instance, newly creating it if
     * necessary.
     *
     * @return a ConfigurationManagerDb instance.
     */
    public static synchronized ConfigurationManagerDb getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManagerDb.class) {
                instance = new ConfigurationManagerDb();
            }
        }
        return instance;
    }

    /**
     * Method responsible for retrieving a property from the database.
     * Removes leading and trailing whitespace from the property.
     *
     * @param key - the property name.
     * @return the property value.
     */
    public String getProperty(final String key) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Property p = (Property) session.get(Property.class, key);

        if (p == null) {
            logger.debug("GETTING PROPERTY = [" + key + "] = " + "NOT FOUND!");
            session.close();
            return "";
        }

        logger.debug("GETTING PROPERTY = [" + key + "] = [" + p.getValue().trim() + "]");

        session.close();
        return p.getValue().trim();
    }

    /**
     * Allows the retrieval of a Web-service endpoint, based on ISO country code
     * and service name.
     *
     *  @param isoCountryCode - the country code in ISO format.
     * @param ServiceName - the specific service name.
     * @return the web-service endpoint url.
     */
    public String getServiceWSE(final String isoCountryCode, final String ServiceName) {
        return getProperty(isoCountryCode + "." + ServiceName + ".WSE");
    }

    /**
     * Allows the setting of an endpoint url, based on ISO Country code and
     * Service name.
     *
     * @param isoCountryCode - the country code in ISO format.
     * @param serviceName - the service name.
     * @param url - the web-service url value to be setted.
     */
    public void setServiceWSE(final String isoCountryCode, final String serviceName, final String url) {
        updateProperty(isoCountryCode + "." + serviceName + ".WSE", url);
    }

    /**
     * Permits the update of a specified property and value.
     *
     * @param key - the name of the property to be updated.
     * @param value - the new value to be set.
     * @return the newly updated value.
     */
    public String updateProperty(final String key, final String value) {
        final Session session = HibernateUtil.getSessionFactory().openSession();

        Property p = (Property) session.get(Property.class, key);

        if (p == null) {

            logger.error("PROPERTY: [" + key + "] NOT FOUND. ADDING NEW ENTRY TO DB.");

            session.beginTransaction();

            p = new Property(key, value);

            session.save(p);

            session.getTransaction().commit();

            session.close();

            return p.getValue();

        } else {

            logger.debug("UPDATING PROPERTY: [" + key + "]=[" + value + "]");

            session.beginTransaction();

            p.setValue(value);

            session.merge(p);

            session.getTransaction().commit();

            session.close();

            return p.getValue();
        }
    }
}
