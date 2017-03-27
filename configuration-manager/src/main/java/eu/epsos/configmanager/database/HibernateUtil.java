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
package eu.epsos.configmanager.database;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Hibernate Utility class with a convenient method to get Session Factory object.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public final class HibernateUtil {

    /**
     * Holds the Session Factory
     */
    private static final SessionFactory SESSION_FACTORY;
    /**
     * Class logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);

    static {

        // Holds the Hibernate Configuration File name
        String hibernateConfigFile;

        if (!HibernateConfigFile.name.contains("/")) {
            try {
                hibernateConfigFile = System.getenv("EPSOS_PROPS_PATH") + HibernateConfigFile.name;
            } catch (NullPointerException ex) {
                LOGGER.error("EPSOS_PROPS_PATH not defined!" + ex);
                throw new ExceptionInInitializerError(ex);
            }
        } else {
            hibernateConfigFile = HibernateConfigFile.name;
        }
        try {
            final File configFile = new File(hibernateConfigFile);
            SESSION_FACTORY = new Configuration().configure(configFile).buildSessionFactory();
        } catch (RuntimeException ex) {
            // Log the exception. 
            LOGGER.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Private constructor to avoid instantiation
     */
    private HibernateUtil() {
    }

    /**
     * Create a {@link SessionFactory} using the properties and mappings in the OpenNCP configuration file.
     *
     * @return The build {@link SessionFactory}.
     */
    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void closeFactory() {
        if (SESSION_FACTORY != null) {
            try {
                SESSION_FACTORY.close();
            } catch (HibernateException ignored) {
                LOGGER.error("Couldn't close SessionFactory", ignored);
            }
        }
    }
}
