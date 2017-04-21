package com.spirit.epsos.cc.adc.test;

import com.spirit.epsos.cc.adc.db.EadcDbConnect;
import eu.epsos.pt.eadc.util.EadcFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This test will test the database connection
 */
public class DbConnectTest extends BaseEadcTest {

    private static Logger log = LoggerFactory.getLogger(DbConnectTest.class);

    @Test
    public void eadcReceiverTest() throws ClassNotFoundException, SQLException, ParserConfigurationException, NamingException, InterruptedException {

        // TODO: check and validate, adding dummy Thread.sleep(500); in order to prevent the JDBC Exception
        // Unique index or primary key violation: "PRIMARY_KEY_E ON PUBLIC.TEST_ADC(PK)"; SQL statement:
        // during the Unit Test plan execution caused by the System.currentTimeMillis() as a Primary Key
        for (int i = 0; i < 3; i++) {
            new DbConnectTest().tryDbConnect(false);
            Thread.sleep(500);
        }
        Thread.sleep(500);
        new DbConnectTest().tryDbConnect(true);
    }

    private void tryDbConnect(boolean dropTable) throws NamingException, SQLException, ParserConfigurationException, ClassNotFoundException {

        log.info("create new  DBConnect_epSOS");
        EadcDbConnect con = EadcFactory.INSTANCE.createEadcDbConnect(DS_NAME);
        log.info("DBConnect_epSOS created .......");
        try {
            log.info("start test DBConnect_epSOS .......");
            DatabaseMetaData md = con.getConnection().getMetaData();
            ResultSet rs = md.getTables(null, null, "TEST_ADC", null);
            boolean exists = rs.next();
            log.info("TABLE TEST_ADC EXISTS? " + exists);
            rs.close();
            Statement sql = con.getConnection().createStatement();
            if (!exists) {
                String s = "create table TEST_ADC(PK   char( 48 ) not null primary key ,C1 varchar(256))";
                log.info("execute :" + s);
                sql.executeUpdate(s);
            }
            String s = "insert into TEST_ADC (PK,C1) values ('" + System.currentTimeMillis() + "','hallo')";
            log.info("execute :" + s);
            sql.executeUpdate(s);
            s = "select * from TEST_ADC";
            log.info("execute :" + s);
            rs = sql.executeQuery(s);
            while (rs.next()) {
                log.info("queryResult :" + rs.getString(1) + " / " + rs.getString(2));
            }
            rs.close();
            if (dropTable) {
                s = "drop table TEST_ADC";
                log.info("execute :" + s);
                sql.executeUpdate(s);
            }
            log.info("finished test DBConnect_epSOS .......\n\n");
        } finally {
            con.closeConnection();
        }
    }
}
