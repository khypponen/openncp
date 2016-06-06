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
package eu.epsos.configmanager.test;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.epsos.configmanager.database.HibernateConfigFile;
import eu.epsos.configmanager.database.HibernateUtil;
import eu.epsos.configmanager.database.model.Property;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import junit.extensions.TestSetup;
import org.apache.commons.configuration.ConfigurationException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class DatabaseAccessTest {

    /**
     * Class logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(DatabaseAccessTest.class);

    @BeforeClass
    public static void setUpClass() {
        HibernateConfigFile.name = "src/test/resources/configmanager.hibernate.xml";
    }

    @AfterClass
    public static void tearDown() {
//        LOGGER.info("TEAR DOWN: Cleaning up local database");
//        final Session session = HibernateUtil.getSessionFactory().openSession();
//
//        Property p = (Property) session.get(Property.class, "TEST");
//
//        session.beginTransaction();
//
//        session.delete(p);
//
//        session.getTransaction().commit();
//
//        session.close();
    }

    @Test
    public void testWrite() {
        LOGGER.info("START: Writing Properties");

        try {
            ConfigurationManagerService.getInstance().updateProperty("TEST", "TEST");
        } catch (RuntimeException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }

        LOGGER.info("END: Writing Properties");
    }

    @Test
    public void testRead() {
        LOGGER.info("START: Read Properties");

        String value = null;

        try {
            value = ConfigurationManagerService.getInstance().getProperty("TEST");
        } catch (RuntimeException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
        assertEquals("TEST", value);

        LOGGER.info("END: Read Properties");
    }

    @Test
    public void testUpdate() {
        LOGGER.info("START: Update Property");

        try {
            ConfigurationManagerService.getInstance().updateProperty("TEST", "TEST1");
        } catch (RuntimeException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }

        String value = null;

        try {
            value = ConfigurationManagerService.getInstance().getProperty("TEST");
        } catch (RuntimeException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
        assertEquals("TEST1", value);

        LOGGER.info("END: Update Property");
    }
    
    @Test
    public void testCommaSeparatedProperties()
    {
        LOGGER.info("START: Store Properties With Comma");
        
        String value="";
        
        try {
            ConfigurationManagerService.getInstance().updateProperty("COMMAPROP", "at,cz,dk,ee,fi,fr,de,gr,ih,it,pt,sk,es,se,ch,tr");
        } catch (RuntimeException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
        
        try {
            value = ConfigurationManagerService.getInstance().getProperty("COMMAPROP");
        } catch (RuntimeException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
        assertEquals("at,cz,dk,ee,fi,fr,de,gr,ih,it,pt,sk,es,se,ch,tr", value);
        
        LOGGER.info("END: Store Properties With Comma");
    }

    private String getPropertiesPath() {
        String path = getEnvKey("EPSOS_PROPS_PATH") + "epsos.properties";
        return path;
    }

    /**
     * This method returns the value of an operating system variable
     *
     * @param key1
     * @return the string value of the variable
     */
    private String getEnvKey(String key1) {
        String value = "";
        Map map = System.getenv();
        Set keys = map.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (key.equals(key1)) {
                value = (String) map.get(key);
                break;
            }
        }
        return value;
    }
}
