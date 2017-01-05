/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012  SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact email: epsos@iuz.pt
 */

package eu.epsos.protocolterminators.integrationtest.cda;

import java.text.ParseException;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import tr.com.srdc.epsos.data.model.PatientDemographics;
import tr.com.srdc.epsos.data.model.PatientDemographics.Gender;

/**
 * This class gathers several utilities methods used in the Integration testing process.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class CdaUtils {
    
    public static Logger logger = LoggerFactory.getLogger(CdaUtils.class);
    
    private static NamespaceContext hl7 = new NamespaceContext() {
        public String getNamespaceURI(String prefix) {
            String uri;
            if (prefix.equals("hl7")) {
                uri = "urn:hl7-org:v3";
            } else {
                uri = null;
            }
            return uri;
        }

        public Iterator getPrefixes(String val) {
            return null;
        }

        public String getPrefix(String uri) {
            return null;
        }
    };
    
    /**
     * Returns PatientDemographics information from a CDA document
     *
     * @param doc CDA document
     * @return PatientDemographics object extracted from the patientRole in
     * Header
     */
    public static PatientDemographics getPatientDemographicsFromXMLDocument(Document doc) {
        PatientDemographics pd = new PatientDemographics();

        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        xPath.setNamespaceContext(hl7);
        try {
            String extension = xPath.evaluate("/hl7:ClinicalDocument/hl7:recordTarget/hl7:patientRole/hl7:id/@extension", doc);
            pd.setId(extension);
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            logger.warn("Could not find patient's id in the CDA document");
        }

        try {
            NodeList givenNames = (NodeList) xPath.evaluate("/hl7:ClinicalDocument/hl7:recordTarget/hl7:patientRole/hl7:patient/hl7:name/hl7:given", doc, XPathConstants.NODESET);
            for (int i = 0; i < givenNames.getLength(); i++) {
                // Skip the given names with attributes such as "call me"
                if (givenNames.item(i).hasAttributes()) {
                    continue;
                }
                if (pd.getGivenName() == null) {
                    pd.setGivenName(givenNames.item(i).getTextContent());
                } else {
                    pd.setGivenName(pd.getGivenName() + " " + givenNames.item(i).getTextContent());
                }
            }
        } catch (XPathExpressionException e) {
            logger.warn("Could not find patient's given name in the CDA document");
        }

        try {
            NodeList familyNames = (NodeList) xPath.evaluate("/hl7:ClinicalDocument/hl7:recordTarget/hl7:patientRole/hl7:patient/hl7:name/hl7:family", doc, XPathConstants.NODESET);
            for (int i = 0; i < familyNames.getLength(); i++) {
                if (pd.getFamilyName() == null) {
                    pd.setFamilyName(familyNames.item(i).getTextContent());
                } else {
                    pd.setFamilyName(pd.getFamilyName() + " " + familyNames.item(i).getTextContent());
                }
            }
        } catch (XPathExpressionException e) {
            logger.warn("Could not find patient's family name in the CDA document");
        }


        try {
            String administrativeGenderCode = xPath.evaluate("/hl7:ClinicalDocument/hl7:recordTarget/hl7:patientRole/hl7:patient/hl7:administrativeGenderCode/@code", doc);
            pd.setAdministrativeGender( Gender.parseGender( administrativeGenderCode ));
        } catch (XPathExpressionException e) {
            logger.warn("Could not find patient's administrative gender code in the CDA document");
        } catch(ParseException pe) {
        	logger.error("Error parsing patient administrative gender code.");
        }

        return pd;
    }
 }
