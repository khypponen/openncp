package com.spirit.epsos.cc.adc.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Helper class to handle database connections
 */
public class EadcDbConnectImpl implements EadcDbConnect {

    private static final Logger LOGGER = LoggerFactory.getLogger(EadcDbConnect.class);
    private Connection connection;

    /**
     * Class constructor
     *
     * @throws NamingException              an naming exception
     * @throws SQLException                 an SQLException
     * @throws ParserConfigurationException an ParserConfigurationException
     * @throws ClassNotFoundException       a ClassNotFoundException
     */
    public EadcDbConnectImpl(String dsName) throws NamingException, SQLException, ParserConfigurationException, ClassNotFoundException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource ds = (DataSource) envContext.lookup(dsName);
        connection = ds.getConnection();
    }

    /* CONNECTION AND CONFIGURATION HELPERS */

    /**
     * Getter for current connection
     *
     * @return the current connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * This method will close the connection
     */
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
            LOGGER.debug("Closing DB connection: (id:" + hashCode() + ")");
        } catch (Exception ex) {
            LOGGER.warn("Unable to close DB connection (id:" + hashCode() + ")", ex);
        }
    }
}
