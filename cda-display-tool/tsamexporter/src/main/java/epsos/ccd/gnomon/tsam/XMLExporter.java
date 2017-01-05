package epsos.ccd.gnomon.tsam;

import epsos.ccd.gnomon.tsam.configuration.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;

public class XMLExporter {

    public final static Logger LOG = LoggerFactory.getLogger("TSAM.Exporter");
    private static String UserHome = System.getenv("EPSOS_PROPS_PATH");

    public static String export() {
        StringBuffer output = new StringBuffer();
        Statement stat = null;
        ResultSet result = null;
        Statement stat1 = null;
        ResultSet resultcodes = null;
        Statement stat2 = null;
        ResultSet result1 = null;
        Connection conn = null;

        try {
            System.out.println("Starting ...");
            System.out.println("EPSOS Repository in " + UserHome);
            output.append("EPSOS Repository in " + UserHome);
            output.append("<br/>");
            Class.forName(Settings.getInstance().getSettingValue("database.class.name")).newInstance();
            String databaseUrl = Settings.getInstance().getSettingValue("database.url");
            String userName = Settings.getInstance().getSettingValue("database.username");
            String userPassword = Settings.getInstance().getSettingValue("database.password");
            conn = DriverManager.getConnection(databaseUrl, userName, userPassword);

            System.out.println("Connecting to database ...");

            //String codes = Settings.getInstance().getSettingValue("oid.codes");
            //String query = "SELECT * FROM code_system where id in (" + codes +")";
            String query = "SELECT * FROM CODE_SYSTEM";
            stat = conn.createStatement();
            result = stat.executeQuery(query);

            String cs_id = "";
            String cs_oid = "";
            String cs_name = "";

            while (result.next()) {
                StringBuffer sb = new StringBuffer();

                cs_id = result.getString("ID");
                System.out.println("CS_ID IS :" + cs_id);
                cs_oid = result.getString("OID");
                cs_name = result.getString("NAME");
                String cs_name_nopaces = cs_name.replaceAll(" ", "");

                System.out.println("Writing code with OID = " + cs_oid + " and name " + cs_name);
                output.append("Writing code with OID = " + cs_oid + " and name " + cs_name);
                output.append("<br/>");
                sb.append("<?xml version=\'1.0\' encoding=\'utf-8\' ?>\r\n");
                sb.append("<").
                        append(cs_name_nopaces).
                        append("Information>\r\n");

                String distinctquery = "SELECT CODE_SYSTEM_CONCEPT.CODE "
                        + "FROM CODE_SYSTEM_VERSION INNER JOIN "
                        + " CODE_SYSTEM_CONCEPT ON CODE_SYSTEM_VERSION.ID = CODE_SYSTEM_CONCEPT.CODE_SYSTEM_VERSION_ID "
                        + "WHERE CODE_SYSTEM_VERSION.CODE_SYSTEM_ID = '" + cs_id + "'";

                stat1 = conn.createStatement();
                resultcodes = stat1.executeQuery(distinctquery);

                while (resultcodes.next()) {
                    String csdistinct_id = resultcodes.getString("CODE");

                    String subquery = "SELECT VALUE_SET.OID, "
                            + " VALUE_SET.EPSOS_NAME, "
                            + " CODE_SYSTEM_CONCEPT.CODE, "
                            + " DESIGNATION.DESIGNATION, "
                            + " DESIGNATION.LANGUAGE_CODE, "
                            + " CODE_SYSTEM_VERSION.CODE_SYSTEM_ID "
                            + "FROM VALUE_SET JOIN "
                            + " VALUE_SET_VERSION ON VALUE_SET.ID = VALUE_SET_VERSION.VALUE_SET_ID JOIN "
                            + " X_CONCEPT_VALUE_SET ON VALUE_SET_VERSION.ID = X_CONCEPT_VALUE_SET.VALUE_SET_VERSION_ID JOIN "
                            + " CODE_SYSTEM_CONCEPT ON X_CONCEPT_VALUE_SET.CODE_SYSTEM_CONCEPT_ID = CODE_SYSTEM_CONCEPT.ID JOIN "
                            + " DESIGNATION ON DESIGNATION.CODE_SYSTEM_CONCEPT_ID = CODE_SYSTEM_CONCEPT.ID JOIN "
                            + " CODE_SYSTEM_VERSION ON  CODE_SYSTEM_VERSION.ID = CODE_SYSTEM_CONCEPT.CODE_SYSTEM_VERSION_ID "
                            + "WHERE CODE_SYSTEM_CONCEPT.CODE ='" + csdistinct_id + "' AND "
                            + " CODE_SYSTEM_VERSION.CODE_SYSTEM_ID = '" + cs_id + "' AND "
                            + " CODE_SYSTEM_CONCEPT.STATUS = 'Current' AND "
                            + " VALUE_SET_VERSION.STATUS = 'Current' AND "
                            + " DESIGNATION.STATUS='Current' AND "
                            + " DESIGNATION.IS_PREFERRED = 1 AND "
                            + " VALUE_SET.OID IS NOT NULL "
                            + "ORDER BY VALUE_SET.EPSOS_NAME";

                    stat2 = conn.createStatement();
                    result1 = stat2.executeQuery(subquery);

                    String code = resultcodes.getString("CODE");

                    if (result1.next()) {
                        String oid = result1.getString("OID");
                        String epsosName = result1.getString("EPSOS_NAME");
                        String lang_code = result1.getString("LANGUAGE_CODE");
                        String description = result1.getString("DESIGNATION");
                        sb.append("<").
                                append(cs_name_nopaces).
                                append("Entry oid='").append(oid).append("'").
                                append(" epsosName='").append(epsosName).append("'").
                                append(" code='").append(code).append("'").
                                append(">\r\n");

                        sb.append("<displayName").append(" lang='").append(lang_code).append("'>").
                                append(description).
                                append("</displayName>\r\n");

                        while (result1.next()) {

                            lang_code = result1.getString("LANGUAGE_CODE");
                            description = result1.getString("DESIGNATION");
                            sb.append("<displayName").append(" lang='").append(lang_code).append("'>").
                                    append(description).
                                    append("</displayName>\r\n");
                        }

                        sb.append("</").append(cs_name_nopaces).append("Entry>\r\n");
                    }
                }

                sb.append("</").append(cs_name_nopaces).append("Information>\r\n");

                output.append(createFile(sb, cs_name_nopaces));
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                result.close();
                stat.close();
                resultcodes.close();
                stat1.close();
                result1.close();
                stat2.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        LOG.error(null, ex);
                    }
                }
            }
        }
        return output.toString();
    }

    public static void main(String[] args) {
        export();
    }

    /**
     * @param sb
     * @param namespace
     * @return
     */
    public static String createFile(StringBuffer sb, String namespace) {

        boolean created = false;
        StringBuffer output = new StringBuffer();
        BufferedWriter out = null;
        try {
            File f = new File(UserHome + "EpsosRepository");

            if (!f.exists()) {
                f.mkdir();
                System.out.println("EPSOS Repository created");
            } else {
                System.out.println("EPSOS Repository exists");
            }

            String outputFile = UserHome + "EpsosRepository/" + namespace + ".xml";

            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
            out.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return output.toString();
    }
}
