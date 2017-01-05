package com.gnomon.epsos.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.exception.ExceptionUtils;

public class Test {

    public static void main(String[] args) throws Exception {

        String path = "/home/karkaletsis/git/epsos-common-components.openncp-portal.6.2/src/main/webapp/";
        String[] countries = new String[21];
        countries[0] = "en-US";
        countries[1] = "ar";
        countries[2] = "cs-CZ";
        countries[3] = "da-DK";
        countries[4] = "de-AT";
        countries[5] = "de-CH";
        countries[6] = "de-DE";
        countries[7] = "ee-EE";
        countries[8] = "el-GR";
        countries[9] = "en-GB";
        countries[10] = "es-ES";
        countries[11] = "fr-FR";
        countries[12] = "fr-LU";
        countries[13] = "it-IT";
        countries[14] = "lt";
        countries[15] = "nl-NL";
        countries[16] = "pt-PT";
        countries[17] = "sk-SK";
        countries[18] = "sl-SL";
        countries[19] = "sv-SE";
        countries[20] = "tr-TR";
        Properties props = new Properties();
        for (int i = 0; i < countries.length; i++) {
            log.info("########### COUNTRY: " + i + "--" + countries[i]);
            String c = countries[i].replace("-", "_");
            props = populateProperties(path, countries[i]);
            Enumeration e = props.propertyNames();
            PrintWriter writer = new PrintWriter("/home/karkaletsis/Desktop/langfiles/Language_" + c + ".properties", "UTF-8");

            List<String> keys = new ArrayList<String>();
            for (String key : props.stringPropertyNames()) {
                keys.add(key);
            }
            Collections.sort(keys);

            for (int p = 0; p < keys.size(); p++) {
                String key = keys.get(p);
                writer.println(key + "=" + props.getProperty(key));
            }

            writer.close();

        }

    }

    public static Properties populateProperties(String portalPath, String lang) {
        Properties properties = new Properties();
//                String epsosPropsPath = System.getenv("EPSOS_PROPS_PATH");
        String wi = portalPath + "/WEB-INF/";
        // Get the language keys from folder under epsos props path
        String path = wi + "language" + File.separator + "application"
                + File.separator + lang + File.separator
                + "SpiritEhrPortal.xml";
        log.info("Language path is in: " + path);
        String defpath = portalPath + "portal/language" + File.separator + "application"
                + File.separator + "SpiritEhrPortal.xml";

        try {
            FileInputStream fis = new FileInputStream(path);
            try {
                properties.loadFromXML(fis);
            } catch (IOException e) {
                log.error(ExceptionUtils.getStackTrace(e));
                FileInputStream deffis = new FileInputStream(defpath);
                try {
                    properties.loadFromXML(deffis);
                } catch (IOException e1) {
                    log.error(ExceptionUtils.getStackTrace(e1));
                }
                log.info("Problem loading language file" + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            log.info("File not found - language file" + e.getMessage());
        }

        return properties;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("Test");
}
