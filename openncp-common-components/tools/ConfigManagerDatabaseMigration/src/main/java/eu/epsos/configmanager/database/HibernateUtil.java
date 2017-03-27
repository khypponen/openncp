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

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static final String HIBERNATE_CONFIG_FILE_NAME = "configmanager.hibernate.xml";
    /**
     * Class logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);
    private static String hibernateConfigFile = null;

    static {

        try {
            hibernateConfigFile = System.getenv("EPSOS_PROPS_PATH") + HIBERNATE_CONFIG_FILE_NAME;
        } catch (NullPointerException ex) {
            LOGGER.error("EPSOS_PROPS_PATH not defined!" + ex);
            throw new ExceptionInInitializerError(ex);
        }
        try {
            final File configFile = new File(hibernateConfigFile);
            sessionFactory = new Configuration().configure(configFile).buildSessionFactory();
        } catch (RuntimeException ex) {
            // Log the exception. 
            LOGGER.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Public constructor to avoid instantiation
     */
    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
