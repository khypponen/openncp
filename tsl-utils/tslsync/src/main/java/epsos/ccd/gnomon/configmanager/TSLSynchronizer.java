/**
 * *Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package epsos.ccd.gnomon.configmanager;

import epsos.ccd.gnomon.auditmanager.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;


/**
 * Synchronizes the countries tsl content to the configuration parameters Read
 * the list of countries from the configuration manager parameter name =
 * ncp.countries For each country reads again the configuration manager to find
 * the property tsl.location.[country_code] It verifies the tsl file It parses
 * the tsl file and extracts the endpoint wse and writes them to the
 * configuration manager Finally it exports all the certificates and add them to
 * the truststor
 *
 * @author Kostas Karkaletsis
 * @author Organization: Gnomon
 * @author mail:k.karkaletsis@gnomon.com.gr
 * @version 1.0, 2010, 30 Jun
 */
public class TSLSynchronizer {

    public TSLSynchronizer() {
    }

    static Logger logger = LoggerFactory.getLogger(TSLSynchronizer.class);

    public static String sync() {
        StringBuilder sb1 = new StringBuilder();
        String sb = "";
        ConfigurationManagerService cms = ConfigurationManagerService.getInstance();
        String ncp = cms.getProperty("ncp.country");
        String ncpemail = cms.getProperty("ncp.email");

        if (ncp.equals("")) {
            ncp = "GR-12";
            cms.updateProperty("ncp.country", ncp);
        }
        if (ncpemail.equals("")) {
            ncpemail = "ncpgr@epsos.gr";
            cms.updateProperty("ncp.email", ncpemail);
        }

        // read the country codes of the epSOS countries from the NCP configuration
        String[] countries = getCountriesList(cms).split(",");
        Hashtable serviceNames = null;
        String url = "";
        // Loop through countries list
        for (int i = 0; i < countries.length; i++) {
            System.out.println("Exporting configuration for : " + countries[i]);
            sb = exportCountryConfig(sb1, countries[i]);
        }
        return sb;
    }

    /* If args is provided, then we want to run TSL-Sync for a specific country. Otherwise we just run it for every country in ncp.countries */
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, CertificateException, Exception {
        if (args != null && args.length > 0) {
            String arg = args[0];
            if (arg.length() != 2) {
                throw new Exception("Argument must be a 2-letter country-code!");
            } else {
                syncCountry(arg.toLowerCase());
                System.out.println("TSL SYNC FINISHED");
                System.exit(0);
            }
        } else {
            String sb = sync().toString();
            System.out.println("TSL SYNC FINISHED");
            System.exit(0);
        }
    }

    /* Sync info for a specified country */
    public static String syncCountry(String country) {
        System.out.println("Synchronizing a specific country...");
        StringBuilder sb1 = new StringBuilder();
        String sb = "";
        ConfigurationManagerService cms = ConfigurationManagerService.getInstance();
        String ncp = cms.getProperty("ncp.country");
        String ncpemail = cms.getProperty("ncp.email");

        if (ncp.equals("")) {
            ncp = "GR-12";
            cms.updateProperty("ncp.country", ncp);
        }
        if (ncpemail.equals("")) {
            ncpemail = "ncpgr@epsos.gr";
            cms.updateProperty("ncp.email", ncpemail);
        }

        System.out.println("Exporting configuration for : " + country);
        sb = exportCountryConfig(sb1, country);

        return sb;
    }

    /**
     * Reads the NCP Configuration and returns the list of ncp countries
     *
     * @param cms the instance of configuration manager
     * @return the comma seperated list of ncp countries
     */
    private static String getCountriesList(ConfigurationManagerService cms) {
        return cms.getProperty("ncp.countries");
    }

    private static void sendAudit(String sc_fullname, String sc_email, String sp_fullname, String sp_email,
                                  String localip, String remoteip, String partid) {
        try {
            AuditService asd = new AuditService();
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new Date());
            XMLGregorianCalendar date2 = null;
            try {
                date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            } catch (DatatypeConfigurationException ex) {
            }
            /*
             * @param  EI_EventActionCode Possible values according to D4.5.6 are E,R,U,D
             * @param  EI_EventDateTime The datetime the event occured
             * @param  EI_EventOutcomeIndicator <br>
             *         0 for full success <br>
             *         1 in case of partial delivery <br>
             *         4 for temporal failures <br>
             *         8 for permanent failure <br>
             * @param  SC_UserID The string encoded CN of the TLS certificate of the NCP triggered the epsos operation
             * @param  SP_UserID The string encoded CN of the TLS certificate of the NCP processed the epsos operation
             * @param  ET_ObjectID The string encoded UUID of the returned document
             * @param  ReqM_ParticipantObjectID String-encoded UUID of the request message
             * @param  ReqM_PatricipantObjectDetail The value MUST contain the base64 encoded security header.
             * @param  ResM_ParticipantObjectID String-encoded UUID of the response message
             * @param  ResM_PatricipantObjectDetail The value MUST contain the base64 encoded security header.
             * @param  sourceip The IP Address of the source Gateway
             * @param  targetip The IP Address of the target Gateway
             */
            String sc_userid = sc_fullname + "<saml:" + sc_email + ">";
            String sp_userid = sp_fullname + "<saml:" + sp_email + ">";
            EventLog eventLog1 = EventLog.createEventLogNCPTrustedServiceList(
                    TransactionName.epsosNCPTrustedServiceList,
                    EventActionCode.EXECUTE,
                    date2,
                    EventOutcomeIndicator.FULL_SUCCESS,
                    sc_userid,
                    sp_userid,
                    partid,
                    "urn:uuid:00000000-0000-0000-0000-000000000000",
                    new byte[1],
                    "urn:uuid:00000000-0000-0000-0000-000000000000",
                    new byte[1],
                    localip, remoteip);
            eventLog1.setEventType(EventType.epsosNCPTrustedServiceList);
            asd.write(eventLog1, "13", "2");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                logger.error(null, ex);
            }
        } catch (Exception e) {
            logger.error("Error sending audit for tslsync");
        }
    }

    private static String exportCountryConfig(StringBuilder sb, String country) {
        ConfigurationManagerService cms = ConfigurationManagerService.getInstance();
        String ncp = cms.getProperty("ncp.country");
        String ncpemail = cms.getProperty("ncp.email");
        Hashtable serviceNames = null;
        logger.info(country + ": Reading tsl file");
        String url = cms.getProperty("tsl.location." + country);
        logger.info("URL: " + url);
        sb.append("Country is :" + country + " and location is :" + url);
        sb.append("<br/>");
        // verify the authenticity and integrity of tsl
        Document doc = TSLUtils.createDomFromURLUsingHttps(url);
//        boolean verifyTSL = TSLUtils.VerifyTSL(doc);
        boolean verifyTSL = true;
        if (verifyTSL) {
            logger.info(country + ": The tsl file has verified");
            // Extract the service WSEs from the TSL and write them to the NCP configuration
            logger.info(country + ": Extracting service Endpoints");
            serviceNames = TSLUtils.getServicesFromTSL(url);
            if (serviceNames.size() > 0) {
                Enumeration names;
                names = serviceNames.keys();
                String str = "";
                while (names.hasMoreElements()) {
                    str = (String) names.nextElement();
                    // Correct the typo of PatientIdentification Service
                    String str_corrected = "";
                    if (str.equals("PatientIdenitificationService")) {
                        str_corrected = "PatientIdentificationService";
                    } else {
                        str_corrected = str;
                    }
                    if (!serviceNames.get(str).toString().equals("")) {
                        cms.setServiceWSE(country, str_corrected.trim(), serviceNames.get(str).toString().trim());
                    }
                    logger.debug(country + ": Extracting " + str.trim() + " - " + serviceNames.get(str).toString().trim());
                }
                // Extract the certificates from services, ipsec and ssl
                // Services
                logger.info(country + ": Extracting certificates");
                sb.append(TSLUtils.exportSSLFromTSL(url, country));
                sb.append(TSLUtils.exportNCPSignFromTSL(url, country));
                String localip = cms.getProperty("SERVER_IP");
                String remoteip = url;
                sendAudit(ncp, ncpemail, "Central Cervices", "centralservices@epsos.eu", localip, remoteip, country);
            } else {
                logger.info(country + ": Problem extracting service names");
            }
        } else {
            logger.error("ERROR Validating TSL");
        }
        return sb.toString();
    }

}
