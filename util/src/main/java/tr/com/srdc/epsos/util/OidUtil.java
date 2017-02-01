/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * <p>
 * This file is part of SRDC epSOS NCP.
 * <p>
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author DG-Sante A4
 *
 */
public class OidUtil {

    private static Logger logger = LoggerFactory.getLogger(OidUtil.class);
    // This configuration service is also responsible for accessing country code
    // <-> OID mappings.
    private static final String pn2oidMapFilePathSubString = "pn-oid.xml";
    private static HashMap<String, String> oid2CountryCodeMap;

    static {
        readCountryOid2CodeMappingFile();
    }

    /**
     *
     * @param countryOid
     *            foreign Home Community Id
     * @return 2-letter ISO code of the country, such as tr, pt, at.
     */
    public static String getCountryCode(String countryOid) {
        return oid2CountryCodeMap.get(countryOid);
    }

    /**
     * Converts a country code into a HomeCommunityId
     *
     * @param countryCode
     *            2-letter ISO code of the country, such as tr, pt, at.
     * @return foreign HomeCommunityId
     */
    public static String getHomeCommunityId(String countryCode) {
        for (Map.Entry<String, String> entry : oid2CountryCodeMap.entrySet()) {
            if (entry.getValue().equals(countryCode)) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     *
     * @param countryOid
     * @return 2-letter ISO code of the country, but in uppercase, such as TR,
     *         PT, AT.
     */
    public static String getCountryCodeUpperCase(String countryOid) {

        String countryCode = getCountryCode(countryOid);
        return countryCode.toUpperCase(Locale.ENGLISH);
    }

    /**
     *
     */
    private static void readCountryOid2CodeMappingFile() {

        DocumentBuilder dBuilder = null;
        Document doc = null;

        oid2CountryCodeMap = new HashMap<String, String>();
        String mapFilePath = Constants.EPSOS_PROPS_PATH + pn2oidMapFilePathSubString;

        File mapFile = new File(mapFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(mapFile);
        } catch (ParserConfigurationException e) {
            logger.error("", e);
        } catch (SAXException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        }

        doc.getDocumentElement().normalize();
        Node mappings = doc.getDocumentElement();

        NodeList childs = mappings.getChildNodes();

        for (int i = 0; i < childs.getLength(); i++) {
            if (childs.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Node mapping = childs.item(i);

                String countryOid = mapping.getAttributes().getNamedItem("domainId").getNodeValue().trim();
                String countryCode = mapping.getAttributes().getNamedItem("country").getNodeValue().trim();

                oid2CountryCodeMap.put(countryOid, countryCode);
            }
        }
    }

    /**
     *
     * @param uuid
     * @return
     */
    public static String convertUuidToOid(String uuid) {

        uuid = uuid.replaceAll("-", "");
        BigInteger integer = new BigInteger(uuid, 16);
        return "2.25." + integer.toString();
    }
}
