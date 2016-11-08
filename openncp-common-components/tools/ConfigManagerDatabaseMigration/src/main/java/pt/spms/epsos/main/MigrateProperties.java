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
package pt.spms.epsos.main;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.hibernate.Session;
import eu.epsos.configmanager.database.HibernateUtil;
import eu.epsos.configmanager.database.model.Property;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MigrateProperties main class, that includes the main operation methods.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 *
 */
public final class MigrateProperties {

    /**
     * Epsos properties file name
     */
    public final static String EPSOS_PROPS = "epsos.properties";
    /**
     * Epsos SRDC properties file name
     */
    public final static String SRDC_PROPS = "epsos-srdc.properties";
    /**
     * Class logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MigrateProperties.class);

    /**
     * Main Runnable method.
     *
     * @param args the program arguments
     * @throws ConfigurationException
     */
    public static void main(final String[] args) throws ConfigurationException, FileNotFoundException, IOException {

        if (args.length != 2) {
            LOGGER.warn("USAGE: java ConfigManagerMigrationTool.java -f < srdc / epsos >");
            return;
        }

        String epsosPropsFile = null;

        if (args[1].equals("epsos")) {
            epsosPropsFile = getPropertiesPath(EPSOS_PROPS);
        } else if (args[1].equals("srdc")) {
            epsosPropsFile = getPropertiesPath(SRDC_PROPS);
        } else {
            LOGGER.warn("USAGE: java ConfigManagerMigrationTool.java -f < srdc / epsos >");
            return;
        }

        LOGGER.info("STARTING MIGRATION PROCESS...");

        if (epsosPropsFile.equals("")) {
            return;
        }

        processProperties(epsosPropsFile);


        LOGGER.info("MIGRATION PROCESS SUCCESSFULLY ENDED!");
    }

    /**
     * This method will process the properties and insert them in the database,
     * based on the specified properties file name.
     *
     * @param epsosPropsFile the properties file name.
     * @throws ConfigurationException if the properties file reading wen wrong.
     */
    private static void processProperties(final String epsosPropsFile) throws ConfigurationException, FileNotFoundException, IOException {
        LOGGER.info("READING CONFIGURATION FILE FROM: " + epsosPropsFile);
        
        File propsFile = new File(epsosPropsFile);
        
        processCommaProperties(propsFile, epsosPropsFile);
        
        propsFile = new File(epsosPropsFile);
        
        final PropertiesConfiguration config = new PropertiesConfiguration();
        config.setReloadingStrategy(new FileChangedReloadingStrategy());

        final Session session = HibernateUtil.getSessionFactory().openSession();

        LOGGER.info("INSERTING PROPERTIES INTO DATABASE...");

        session.beginTransaction();

        final Iterator it = config.getKeys();

        while (it.hasNext()) {
            final String key = (String) it.next();
            final String value = (String) config.getString(key);

            LOGGER.info("INSERTING: { KEY: " + key + ", VALUE: " + value + " }");

            final Property p = new Property(key, value);
            session.save(p);
        }

        session.getTransaction().commit();

        session.close();
    }

    /**
     * This method will build the properties path, based on the EPSOS_PROPS_PATH
     * configuration.
     *
     * @param fileName the properties file name.
     * @return the properties file with full path.
     */
    private static String getPropertiesPath(final String fileName) {
        final String envKey = getEnvKey("EPSOS_PROPS_PATH");

        if (envKey.isEmpty()) {
            LOGGER.error("EPSOS PROPERTIES PATH NOT FOUND!");
            return null;
        }

        return getEnvKey("EPSOS_PROPS_PATH") + fileName;
    }

    /**
     * This method returns the value of an operating system variable
     *
     * @param key the key name
     * @return the string value of the variable
     */
    private static String getEnvKey(final String key) {
        String value = "";
        final Map map = System.getenv();
        final Set keys = map.keySet();
        final Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            final String key1 = (String) iterator.next();
            if (key1.equals(key)) {
                value = (String) map.get(key1);
                break;
            }
        }
        return value;
    }

    /**
     * This method will replace the commas with "\," to improve the processing by Apache-Commons
     * 
     * @param propertiesFile
     * @param filePath
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private static void processCommaProperties(File propertiesFile, String filePath) throws FileNotFoundException, IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(propertiesFile));
            String line = "", oldtext = "";
            while ((line = reader.readLine()) != null) {
                oldtext += line + "\r\n";
            }
            reader.close();
            // replace a word in a file
            String newtext = oldtext.replaceAll(",", "\\,");

            FileWriter writer = new FileWriter(filePath);
            writer.write(newtext);
            writer.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Private constructor to disable instantiation.
     */
    private MigrateProperties() {
    }
}
