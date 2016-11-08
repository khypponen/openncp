 /*
 *  This file is part of epSOS OpenNCP implementation
 *  Copyright (C) 2014 iUZ Technologies and Gnomon Informatics
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Contact email: contact@iuz.pt, info@gnomon.com.gr
 */
package com.liferay.portal.stork.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tr.com.srdc.epsos.util.XMLUtil;

/**
 * This class gathers several utility methods to obtain the required attributes
 * to perform a patient identification for a given country. This information is
 * based on the content of the Portal search mask files and in a mapping
 * described at portlet.properties.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public final class PatientSearchAttributes {

    private static Log LOG = LogFactoryUtil.getLog(PatientSearchAttributes.class);

    /**
     *
     * Obtains all the required attributes to search for a patient for a given
     * country.
     *
     * The result is a Map containing ONLY the required attributes, combined in
     * a (key,value), where the Key is the "STORK ID" and Value is "Search Mask
     * id".
     *
     * E.g. "(eIdentifier,personal.patient.search.patient.id)"
     *
     * This information is obtained from Search Mask files, located at
     * EPSOS_PROPS_PATH/portal/forms/.
     *
     * @param countryCode
     * @return
     */
    public static Map<String, String> getRequiredAttributesByCountry(String countryCode) {
        /* PRE-CONDITIONS */
        if (countryCode.isEmpty()) {
            LOG.error("Country code is empty.");
            return null;
        }

        /* ATTRIBUTES */
        final String LABEL_ATTR_NAME = "label";
        final String DOMAIN_ATTR_NAME = "domain";
        final String STORK_ATTR_NAME = "stork";
        final String XPATH_EXPR = "//searchFields/*[@label]";
        final String FILE_FOLDER = "forms";
        final String FILE_PATH;
        final String FILE_NAME;
        final String fileContent;
        final Path filePath;
        final Document searchMaskFileDom;
        final NodeList nl;
        Map<String, String> result;

        FILE_NAME = "InternationalSearch_" + countryCode + ".xml";
        FILE_PATH = System.getenv("EPSOS_PROPS_PATH") + FILE_FOLDER + "/";
        result = new HashMap<String, String>();

        /* READ SEARCH MASK FILE CONTENT TO DOM OBJECT */
        try {
            searchMaskFileDom = XMLUtil.parseContent(Files.readAllBytes(FileSystems.getDefault().getPath(FILE_PATH, FILE_NAME)));
        } catch (ParserConfigurationException ex) {
            LOG.error(ex);
            return null;
        } catch (SAXException ex) {
            LOG.error(ex);
            return null;
        } catch (IOException ex) {
            LOG.error(ex);
            return null;
        }

        /* APPLY XPATH EXPRESSION TO GET NODES WITH LABEL ATTRIBUTE - THE ONES REQUIRED FOR IDENTIFICATION */
        try {
            nl = (NodeList) XPathFactory.newInstance().newXPath().compile(XPATH_EXPR).evaluate(searchMaskFileDom, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            LOG.error(ex);
            return null;
        }

        /* CHECK IF EACH ATTRIBUTE FOUND IN SEARCH MASK FILE HAS A CORRESPONDANT MAPPING (STORK ATTRIBUTE), IF YES FILL RESULT MAP WITH THE MAPPING */
        for (int i = 0; i < nl.getLength(); i++) {

            Element elem = (Element) nl.item(i);
            String searchMaskAttrId = elem.getAttribute(LABEL_ATTR_NAME);
            String domainAttrValue = elem.getAttribute(DOMAIN_ATTR_NAME);
            String storkAttrValue = elem.getAttribute(STORK_ATTR_NAME);

            if (!searchMaskAttrId.isEmpty() & !storkAttrValue.isEmpty()) {
                if (!domainAttrValue.isEmpty()) { // FILL DOMAIN VALUES FOR eIdentifiers (e.g. eIdentifier=2.16.470.1.100.1.1.1000.990.1)
                    result.put(storkAttrValue, domainAttrValue);
                } else { // FILL NORMAL MAPPING (e.g. surname=patient.data.surname)
                    result.put(storkAttrValue, searchMaskAttrId);
                }
            } else {
                LOG.error("There is no STORK attribute defined for required searchmask attribute " + searchMaskAttrId + " present in Search Mask file for country " + countryCode + ".");
            }
        }

        return result;

    }

}
