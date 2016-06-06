/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * 
 * This file is part of SRDC epSOS NCP.
 * 
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SRDC epSOS NCP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.consent.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;




public class PatientDBConnector {
	
	private static Logger logger = Logger.getLogger(PatientDBConnector.class);

	private static Connection connection = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;

	private static void getConnection() {
//		try {
//			if(connection == null || connection.isClosed())
//				connection = DBConnection.getConnection(Constants.DB_EPSOS_NAME);
//		} catch (SQLException e) {
//			logger.error("", e);
//		}
            throw new UnsupportedOperationException("Method currently not supported.");
	}

	public static Statement getStatement() {

		getConnection();

		try {
			if (statement == null || statement.isClosed()) {
				statement = connection.createStatement();
			}
		} catch (SQLException e) {
			logger.error("", e);
		}
		
		return statement;
	}

	public static PreparedStatement getPreparedStatement(String sql) {

		getConnection();

		try {
			if (preparedStatement == null || preparedStatement.isClosed()) {
				preparedStatement = connection.prepareStatement(sql);
			}
		} catch (SQLException e) {
			logger.error("", e);
		}
		return preparedStatement;
	}
	
	public static PreparedStatement getPreparedStatement(String sql, int param) {

		getConnection();

		try {
			if (preparedStatement == null || preparedStatement.isClosed()) {
				preparedStatement = connection.prepareStatement(sql, param);
			}
		} catch (SQLException e) {
			logger.error("", e);
		}
		return preparedStatement;
	}
}