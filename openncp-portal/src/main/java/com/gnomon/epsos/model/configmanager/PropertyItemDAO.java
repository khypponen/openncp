package com.gnomon.epsos.model.configmanager;

import com.gnomon.epsos.util.ConnectionPool;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PropertyItemDAO {

    private static final Logger log = LoggerFactory.getLogger("PropertyItemDAO");

    public static CustomResponse deleteItem(String property) {
        CustomResponse cr = new CustomResponse();
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement(_DELETE_ITEM);
            ps.setString(1, property);
            ps.executeUpdate();
            cr.setCode("200");
        } catch (Exception e) {
            cr.setCode("300");
            cr.setDescription(e.getMessage());
            log.error(e.getMessage());
        } finally {
            ConnectionPool.cleanUp(con, ps);
        }
        return cr;
    }

    public static Property getItem(String property) throws SQLException {
        Property item = null;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement(_GET_ITEM);
            ps.setString(1, property);
            rs = ps.executeQuery();

            if (rs.next()) {
                item = new Property();

                item.setProperty(rs.getString("name"));
                item.setValue(rs.getString("value"));
            }
        } finally {
            ConnectionPool.cleanUp(con, ps, rs);
        }

        return item;
    }

    public static List getItems() throws SQLException {
        List list = new ArrayList<Property>();
        log.info("Getting properties from database");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement(_GET_ITEMS);
            rs = ps.executeQuery();
            while (rs.next()) {
                Property item = new Property();
                String propname = rs.getString("name");
                item.setProperty(propname);
                item.setValue(rs.getString("value"));
                list.add(item);
            }
        } finally {
            ConnectionPool.cleanUp(con, ps, rs);
        }

        return list;
    }

    public static CustomResponse addItem(String property, String value) {
        CustomResponse cr = new CustomResponse();
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectionPool.getConnection();
            String insertSQL = "INSERT INTO property (name, value) " +
                    "VALUES ('" + property + "','" + value + "');";
            log.info(insertSQL);
            ps = con.prepareStatement(insertSQL);
            ps.execute(insertSQL);
            cr.setCode("200");
        } catch (Exception e) {
            cr.setCode("300");
            cr.setDescription(e.getMessage());
            log.error(e.getMessage());
            log.error(ExceptionUtils.getStackTrace(e));
        } finally {
            ConnectionPool.cleanUp(con, ps, rs);
        }
        return cr;
    }

    public static CustomResponse updateItem(String property, String value) {
        CustomResponse cr = new CustomResponse();
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectionPool.getConnection();
            String insertSQL = "UPDATE property set value= '" + value + "' where name = '" + property + "'";
            ps = con.prepareStatement(insertSQL);
            ps.executeUpdate(insertSQL);
            cr.setCode("200");
        } catch (Exception e) {
            cr.setCode("300");
            cr.setDescription(e.getMessage());
            log.error(e.getMessage());
        } finally {
            ConnectionPool.cleanUp(con, ps, rs);
        }
        return cr;
    }

    private static final String _ADD_ITEM =
            "INSERT INTO property (name,value) " +
                    "VALUES (?,?)";

    private static final String _DELETE_ITEM =
            "DELETE FROM property WHERE name= ?";

    private static final String _GET_ITEM =
            "SELECT name, value FROM property WHERE name = ?";

    private static final String _GET_ITEMS =
            "select name, value from property";

}