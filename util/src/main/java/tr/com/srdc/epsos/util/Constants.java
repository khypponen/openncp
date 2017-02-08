/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * <p>
 * This file is part of SRDC epSOS NCP.
 * <p>
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * SRDC epSOS NCP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.util;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final Logger logger = LoggerFactory.getLogger(Constants.class);

    public static final String UUID_PREFIX = "urn:uuid:";
    public static final String OID_PREFIX = "urn:oid:";

    public static final String EP_CLASSCODE = "57833-6";
    public static final String PS_CLASSCODE = "60591-5";
    public static final String ED_CLASSCODE = "60593-1";
    public static final String MRO_CLASSCODE = "56445-0";
    public static final String CONSENT_CLASSCODE = "57016-8";
    public static final String HCER_CLASSCODE = "34133-9";
    public static final int FORMAT_XML = 1;
    public static final int FORMAT_PDF = 2;

    public static final String PatientIdentificationService = "PatientIdentificationService";
    public static final String PatientService = "PatientService";
    public static final String MroService = "MroService";
    public static final String ConsentService = "ConsentService";
    public static final String DispensationService = "DispensationService";
    public static final String OrderService = "OrderService";

    public static final String PS_TITLE = "Patient Summary";
    public static final String MRO_TITLE = "MRO Summary";
    public static final String EP_TITLE = "ePrescription";
    public static final String ED_TITLE = "eDispensation";
    public static final String CONSENT_TITLE = "Privacy Policy Acknowledgement Document";

    public static final String CONSENT_PUT_SUFFIX = ".2.4.1.1";
    public static final String CONSENT_DISCARD_SUFFIX = ".2.4.1.2";

    public static final String NOT_USED_FIELD = "Not Used";
    public static final String MIME_TYPE = "text/xml";

    public static String SERVER_IP = "127.0.0.1"; // default value
    public static String HOME_COMM_ID;
    public static String COUNTRY_CODE;
    public static String COUNTRY_NAME;
    public static String COUNTRY_PRINCIPAL_SUBDIVISION; // ISO 3166-2
    public static String LANGUAGE_CODE;

    public static String HR_ID_PREFIX = "SPProvidedID";


    /**
     * Path to the folder containing the configuration files.
     */
    public static String EPSOS_PROPS_PATH;

    public static String TRUSTSTORE_PATH;
    public static String TRUSTSTORE_PASSWORD;

    public static String SP_KEYSTORE_PATH;
    public static String SP_KEYSTORE_PASSWORD;
    public static String SP_PRIVATEKEY_ALIAS;
    public static String SP_PRIVATEKEY_PASSWORD;

    public static String SC_KEYSTORE_PATH;
    public static String SC_KEYSTORE_PASSWORD;
    public static String SC_PRIVATEKEY_ALIAS;
    public static String SC_PRIVATEKEY_PASSWORD;

    public static String NCP_SIG_KEYSTORE_PATH;
    public static String NCP_SIG_KEYSTORE_PASSWORD;
    public static String NCP_SIG_PRIVATEKEY_ALIAS;
    public static String NCP_SIG_PRIVATEKEY_PASSWORD;

    public static String WRITE_TEST_AUDITS;


    /**
     * Name of the System Variable containing the path to the folder containing the configuration files.
     */
    private static final String PROPS_ENV_VAR = "EPSOS_PROPS_PATH";

    static {
        String epsosPath = System.getenv(PROPS_ENV_VAR);
        if (!epsosPath.endsWith(System.getProperty("file.separator"))) {
            epsosPath += System.getProperty("file.separator");
        }
        EPSOS_PROPS_PATH = epsosPath;

        fill();
    }

    private static void fill() {
        SERVER_IP = ConfigurationManagerService.getInstance().getProperty("SERVER_IP");

        HOME_COMM_ID = ConfigurationManagerService.getInstance().getProperty("HOME_COMM_ID");
        COUNTRY_CODE = ConfigurationManagerService.getInstance().getProperty("COUNTRY_CODE");
        COUNTRY_NAME = ConfigurationManagerService.getInstance().getProperty("COUNTRY_NAME");
        COUNTRY_PRINCIPAL_SUBDIVISION = ConfigurationManagerService.getInstance().getProperty("COUNTRY_PRINCIPAL_SUBDIVISION");
        LANGUAGE_CODE = ConfigurationManagerService.getInstance().getProperty("LANGUAGE_CODE");

        TRUSTSTORE_PATH = globalizePath(ConfigurationManagerService.getInstance().getProperty("TRUSTSTORE_PATH"));
        TRUSTSTORE_PASSWORD = ConfigurationManagerService.getInstance().getProperty("TRUSTSTORE_PASSWORD");

        SP_KEYSTORE_PATH = globalizePath(ConfigurationManagerService.getInstance().getProperty("SP_KEYSTORE_PATH"));
        SP_KEYSTORE_PASSWORD = ConfigurationManagerService.getInstance().getProperty("SP_KEYSTORE_PASSWORD");
        SP_PRIVATEKEY_ALIAS = ConfigurationManagerService.getInstance().getProperty("SP_PRIVATEKEY_ALIAS");
        SP_PRIVATEKEY_PASSWORD = ConfigurationManagerService.getInstance().getProperty("SP_PRIVATEKEY_PASSWORD");

        SC_KEYSTORE_PATH = globalizePath(ConfigurationManagerService.getInstance().getProperty("SC_KEYSTORE_PATH"));
        SC_KEYSTORE_PASSWORD = ConfigurationManagerService.getInstance().getProperty("SC_KEYSTORE_PASSWORD");
        SC_PRIVATEKEY_ALIAS = ConfigurationManagerService.getInstance().getProperty("SC_PRIVATEKEY_ALIAS");
        SC_PRIVATEKEY_PASSWORD = ConfigurationManagerService.getInstance().getProperty("SC_PRIVATEKEY_PASSWORD");

        NCP_SIG_KEYSTORE_PATH = globalizePath(ConfigurationManagerService.getInstance().getProperty("NCP_SIG_KEYSTORE_PATH"));
        NCP_SIG_KEYSTORE_PASSWORD = ConfigurationManagerService.getInstance().getProperty("NCP_SIG_KEYSTORE_PASSWORD");
        NCP_SIG_PRIVATEKEY_ALIAS = ConfigurationManagerService.getInstance().getProperty("NCP_SIG_PRIVATEKEY_ALIAS");
        NCP_SIG_PRIVATEKEY_PASSWORD = ConfigurationManagerService.getInstance().getProperty("NCP_SIG_PRIVATEKEY_PASSWORD");

        WRITE_TEST_AUDITS = ConfigurationManagerService.getInstance().getProperty("WRITE_TEST_AUDITS");

    }

    /**
     * check whether the input path is global, modify accordingly (global paths
     * can come within linux)
     *
     * @param path
     * @return
     */
    private static String globalizePath(String path) {
        if (path == null) {
            return null;
        }

        if (!(path.startsWith("/") || path.matches("[A-Z]:.*"))) {
            path = EPSOS_PROPS_PATH + path;
        }

        return path;
    }

    public static void main(String[] args) {
        Constants c = new Constants();

    }
}
