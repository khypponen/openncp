package com.spirit.epsos.cc.adc.db;

import java.sql.Connection;

/**
 * Helper class to handle database connections
 */
public interface EadcDbConnect {

    /**
     * Getter for current connection
     *
     * @return the current connection
     */
    public Connection getConnection();

    /**
     * This method will close the connection
     */
    public void closeConnection();
}
