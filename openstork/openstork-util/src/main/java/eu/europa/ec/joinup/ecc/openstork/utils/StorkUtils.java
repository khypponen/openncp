/*
 *  This file is part of integration of STORK within OpenNCP
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
package eu.europa.ec.joinup.ecc.openstork.utils;


import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.epsos.assertionvalidator.XSPARole;
import eu.epsos.util.validation.StringPool;
import eu.europa.ec.joinup.ecc.openstork.utils.assertions.HCPIAssertionCreator;
import eu.europa.ec.joinup.ecc.openstork.utils.datamodel.HcpRole;
import eu.europa.ec.joinup.ecc.openstork.utils.datamodel.StorkAttributes;
import eu.europa.ec.joinup.ecc.trilliumsecurityutils.saml.HCPIAssertionBuilder;
import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import tr.com.srdc.epsos.data.model.PatientId;
import tr.com.srdc.epsos.util.XMLUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * This class gathers a set of utilities specifically addressed to the
 * information extraction and conversion of Mandate structures.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class StorkUtils {

    private static final Logger LOG = LoggerFactory.getLogger(StorkUtils.class);

    /*

     CONVERSION METHODS

     */

    /**
     * Converts a given STORK Authentication Response into an epSOS Assertion.
     * In the current implementation only Role (extracted from STORK response)
     * and Permissions (taken from database) are taken into account.
     *
     * @param storkResponse
     * @return
     */
    public static Assertion convertStorkToHcpAssertion(final STORKAuthnResponse storkResponse) {
        return convertStorkToHcpAssertion(storkResponse, obtainPermissionsForRole(obtainHcpRole(storkResponse)));
    }

    /**
     * This method will build an epSOS Assertion, with the inclusion of the
     * On-behalf statement, required for the STORK UC2. It will assign some
     * dummy values to certain variables, which are not critical for this UC.
     *
     * @param storkResponse
     * @return
     */
    public static Assertion convertOnBehalfStorktoHcpAssertion(final STORKAuthnResponse storkResponse) {
        /* PRE-CONDITIONS */
        if (storkResponse == null) {
            return null;
        }

        final String X509_SUBJECT_FORMAT = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";
        final String SUBJECT_EIDENTIFIER = "eIdentifier";
        final String NOT_AVAILABLE = "N/A";
        final String OTHER_FACILITY_TYPE = "Other";
        final String TREATMENT_PURPOSE_OF_USE = "TREATMENT";
        final String DUMMY_POINT_OF_CARE = "PointOfCareName";
        final String DUMMY_HCP_ORG_ID = "1.2.3.4.5";
        final String DUMMY_HCP_ORG_NAME = "HCPOrgName";

        /* BODY */
        Assertion result;

        final HCPIAssertionBuilder assertionBuilder;
        final String hcpRole;
        final String hcpId;
        final String hcpSpecialty;

        final String orgId;
        final String orgName;
        final String healthCareFacilityType;

        final String purposeOfUse;
        final String pointOfCare;

        final String onBehalfOfId;

        final List<String> permissions;

        final String hcpOrgId;
        final String hcpOrgName;

        LOG.info("Converting Assertion.");

        // Initialize Assertions Builder with minimum initial parameters.
        assertionBuilder
                = new HCPIAssertionBuilder("UID=medical doctor", X509_SUBJECT_FORMAT, "sender-vouches")
                .issuer("O=European HCP,L=Europe,ST=Europe,C=EU", X509_SUBJECT_FORMAT)
                .audienceRestrictions("http://ihe.connecthaton.XUA/X-ServiceProvider-IHE-Connectathons")
                .notOnOrAfter(4);

        // MANDATORY: HCP ID and HCP Role
        hcpRole = obtainHcpRole(storkResponse).toString();
        if (getPersonalAttribute(storkResponse, SUBJECT_EIDENTIFIER) != null) {
            hcpId = getPersonalAttribute(storkResponse, SUBJECT_EIDENTIFIER).getValue().get(0);
        } else {
            hcpId = NOT_AVAILABLE;
        }
        assertionBuilder
                .hcpIdentifier(hcpId)
                .hcpRole(hcpRole);

        // MANDATORY: HealthCare Facility Type
        healthCareFacilityType = OTHER_FACILITY_TYPE;
        assertionBuilder
                .healthCareFacilityType(healthCareFacilityType);

        // MANDATORY: Purpose of Use
        purposeOfUse = TREATMENT_PURPOSE_OF_USE;
        assertionBuilder
                .purposeOfUse(purposeOfUse);

        // MANDATORY: Point Of Care
        pointOfCare = DUMMY_POINT_OF_CARE;
        assertionBuilder
                .pointOfCare(pointOfCare);

        // OPTIONAL: HCP Organization Details
        hcpOrgId = DUMMY_HCP_ORG_ID;
        hcpOrgName = DUMMY_HCP_ORG_NAME;
        assertionBuilder.healthCareProfessionalOrganisation(hcpOrgId, hcpOrgName);

        // OPTIONAL: On-behalf
        // The information filled within On-behalf structure is related to the represented person
        if (getRepresentedEidentifiers(storkResponse, null) == null || getRepresentedEidentifiers(storkResponse, null).isEmpty()) {
            onBehalfOfId = NOT_AVAILABLE;
        } else {
            onBehalfOfId = getRepresentedEidentifiers(storkResponse, null).get(0).getRoot();
        }
        assertionBuilder
                .onBehalfOf(XSPARole.PATIENT.name(), onBehalfOfId);

        // OPTIONAL (0..*): Permissions
        permissions = obtainPermissionsForRole(obtainHcpRole(storkResponse));
        assertionBuilder
                .permissions(permissions);

        // BUILD Assertion
        result = assertionBuilder.build();

        return result;
    }

    /**
     * Converts a given STORK Authentication Response into an epSOS Assertion.
     * In the current implementation only Role (extracted from STORK response)
     * and Permissions (taken from database) are taken into account.
     *
     * @param storkResponse
     * @param permissions
     * @return a converted epSOS assertion
     */
    public static Assertion convertStorkToHcpAssertion(STORKAuthnResponse storkResponse, List<String> permissions) {
        /* PRE-CONDITIONS */
        if (storkResponse == null) {
            return null;
        }

        /* BODY */
        Assertion result;
        HcpRole role;

        role = obtainHcpRole(storkResponse);
        result = HCPIAssertionCreator.createHCPIAssertion(permissions, role.getXspaRole());

        if (result != null) {
            LOG.info("Assertion conversion done with success.");
        } else {
            LOG.info("Assertion conversion done unsuccessfully.");
        }

        return result;
    }

    /*

     REPRESENTED PERSON INFORMATION EXTRACTION

     */

    /**
     * Extracts the REPRESENTED person eIdentifier from the Mandate Information,
     * obtained from STORK response.
     *
     * @param storkResponse - The STORK response, containing the on-behalf
     *                      relation.
     * @param countryCode
     * @return a List<PatientId>, containing the eIentifier list for the
     * represented person.
     */
    public static List<PatientId> getRepresentedEidentifiers(final STORKAuthnResponse storkResponse, final String countryCode) {
        final String MANDATE_INFORMATION = "mandateContent";
        final String REPRESENTED_EIDENTIFIER_XPATH = "/mandate/represented/eIdentifier";
        final String EIDENTIFIER = "eIdentifier";
        final String NOT_AVAILABLE = "N/A";

        // PRE-CONDITIONS
        if (storkResponse == null) {
            LOG.error("Provided STORK response is null.");
            return null;
        }
        if (getPersonalAttribute(storkResponse, MANDATE_INFORMATION) == null
                || getPersonalAttribute(storkResponse, MANDATE_INFORMATION).getValue() == null
                || getPersonalAttribute(storkResponse, MANDATE_INFORMATION).getValue().get(0) == null
                || getPersonalAttribute(storkResponse, MANDATE_INFORMATION).getValue().get(0).isEmpty()) {
            LOG.error("Mandate information is missing in the provided STORK response.");
            return null;
        }

        // BODY
        final List<PatientId> result;
        final String mandateInformation;
        final Map<String, String> patientSearchAttributes;

        //Obtain Mandate Information
        result = new ArrayList<>();
        mandateInformation = getPersonalAttribute(storkResponse, MANDATE_INFORMATION).getValue().get(0);

        //Create and add identifier
        if (countryCode != null) {
            patientSearchAttributes = getRequiredAttributesByCountry(countryCode);
            if (patientSearchAttributes.containsKey(EIDENTIFIER)) {
                result.add(new PatientId(patientSearchAttributes.get(EIDENTIFIER), obtainNodeValue(mandateInformation, REPRESENTED_EIDENTIFIER_XPATH))); // Identifier domain obtained from searchmask file
            } else {
                LOG.info("It was not possible to obtain identifier domain from searchmask file for country: " + countryCode + ", using N/A as identifier domain.");
                result.add(new PatientId(NOT_AVAILABLE, obtainNodeValue(mandateInformation, REPRESENTED_EIDENTIFIER_XPATH))); // Identifier domain missing from search mask file
            }
        } else {
            LOG.info("The supplied country code is null, using N/A as identifier domain.");
            result.add(new PatientId(NOT_AVAILABLE, obtainNodeValue(mandateInformation, REPRESENTED_EIDENTIFIER_XPATH))); // Country code not provided (= null)
        }
        return result;
    }

    /**
     * Extracts the REPRESENTED person DEMOGRAPHIC Information from the Mandate
     * Information, obtained from STORK response.
     * <p>
     * The following keys are inserted: - givenName - surname - dateOfBirth
     *
     * @param storkResponse - The STORK response, containing the on-behalf
     *                      relation.
     * @return a Map, containing the demographic data.
     */
    public static Map<String, String> getRepresentedDemographics(final STORKAuthnResponse storkResponse) {
        // PRE-CONDITIONS
        if (storkResponse == null) {
            LOG.error("Provided STORK response is null.");
            return null;
        }

        // BODY
        final Map<String, String> result = new HashMap<>();
        final Map<String, String> representedInformation = getRepresentedPersonInformation(storkResponse);

        //Obtain Mandate Information
        if (representedInformation.containsKey(StorkAttributes.GIVEN_NAME.getStorkDesignation())) {
            result.put(StorkAttributes.GIVEN_NAME.getSearchMaskValue(), representedInformation.get(StorkAttributes.GIVEN_NAME.getStorkDesignation()));
        }
        if (representedInformation.containsKey(StorkAttributes.SURNAME.getStorkDesignation())) {
            result.put(StorkAttributes.SURNAME.getSearchMaskValue(), representedInformation.get(StorkAttributes.SURNAME.getStorkDesignation()));
        }
        if (representedInformation.containsKey(StorkAttributes.DATE_OF_BIRTH.getStorkDesignation())) {
            result.put(StorkAttributes.DATE_OF_BIRTH.getSearchMaskValue(), representedInformation.get(StorkAttributes.DATE_OF_BIRTH.getStorkDesignation()));
        }

        return result;
    }

    /**
     * Obtains the represented person information from Mandate section of STORK
     * response in order to build a map with that information (to be used by the
     * portal)
     * <p>
     * The following keys of information are extracted: - eIdentifier -
     * givenName - surname - dateOfBirth
     *
     * @param storkResponse - the STORK response.
     * @return a Map with the information organized into key-value.
     */
    public static Map<String, String> getRepresentedPersonInformation(final STORKAuthnResponse storkResponse) {
        final String MANDATE_INFORMATION = "mandateContent";
        final String EIDENTIFIER = "eIdentifier";
        final String GIVENNAME = "givenName";
        final String SURNAME = "surname";
        final String DATEOFBIRTH = "dateOfBirth";
        final String REPRESENTED_EIDENTIFIER_XPATH = "/mandate/represented/eIdentifier";
        final String REPRESENTED_GIVENNAME_XPATH = "/mandate/represented/givenName";
        final String REPRESENTED_SURNAME_XPATH = "/mandate/represented/surname";
        final String REPRESENTED_DATEOFBIRTH_XPATH = "/mandate/represented/dateOfBirth";

        // PRE-CONDITIONS
        if (storkResponse == null) {
            LOG.error("Provided STORK response is null.");
            return null;
        }
        if (getPersonalAttribute(storkResponse, MANDATE_INFORMATION) == null
                || getPersonalAttribute(storkResponse, MANDATE_INFORMATION).getValue() == null
                || getPersonalAttribute(storkResponse, MANDATE_INFORMATION).getValue().get(0) == null
                || getPersonalAttribute(storkResponse, MANDATE_INFORMATION).getValue().get(0).isEmpty()) {
            LOG.error("Mandate information is missing in the provided STORK response.");
            return null;
        }

        //BODY
        final Map<String, String> result = new HashMap<>();
        final String eIdentifier;
        final String givenName;
        final String surnameName;
        final String dateOfBirth;
        final String mandateInformation;

        //Obtain information from mandate section and fill Map
        mandateInformation = getPersonalAttribute(storkResponse, MANDATE_INFORMATION).getValue().get(0);
        result.put(EIDENTIFIER, obtainNodeValue(mandateInformation, REPRESENTED_EIDENTIFIER_XPATH));
        result.put(GIVENNAME, obtainNodeValue(mandateInformation, REPRESENTED_GIVENNAME_XPATH));
        result.put(SURNAME, obtainNodeValue(mandateInformation, REPRESENTED_SURNAME_XPATH));
        result.put(DATEOFBIRTH, obtainNodeValue(mandateInformation, REPRESENTED_DATEOFBIRTH_XPATH));

        return result;
    }

    /*

     HELPER METHODS

     */

    /**
     * Utility method to obtain role based on the HCP flag and the citizen's
     * title.
     *
     * @param storkResponse
     * @return
     */
    public static HcpRole obtainHcpRole(STORKAuthnResponse storkResponse) {
        final String IS_HCP_ATTR_NAME = "isHealthCareProfessional";
        final String TITTLE_ATTR_NAME = "Title";
        Boolean isHcpProfessional = false;
        String tittle = StringPool.BLANK;

        if (getPersonalAttribute(storkResponse, IS_HCP_ATTR_NAME) != null) {
            isHcpProfessional = Boolean.parseBoolean(getPersonalAttribute(storkResponse, IS_HCP_ATTR_NAME).getValue().get(0));
        }
        if (getPersonalAttribute(storkResponse, IS_HCP_ATTR_NAME) != null) {
            tittle = getPersonalAttribute(storkResponse, IS_HCP_ATTR_NAME).getValue().get(0);
        }

        return obtainHcpRole(isHcpProfessional, tittle);
    }

    public static HcpRole obtainRole(IPersonalAttributeList attrs) {
        final String IS_HCP_ATTR_NAME = "isHealthCareProfessional";
        final String TITTLE_ATTR_NAME = "Title";
        Boolean isHcpProfessional = false;
        String tittle = "";
        try {
            isHcpProfessional = Boolean.parseBoolean(getPersonalAttribute(attrs, IS_HCP_ATTR_NAME).getValue().get(0));
            tittle = getPersonalAttribute(attrs, IS_HCP_ATTR_NAME).getValue().get(0);
        } catch (Exception e) {
            System.out.println("Problem getting role attrs");
        }

        return obtainHcpRole(isHcpProfessional, tittle);
    }

    /**
     * Utility method to obtain role based on the HCP flag and the citizen's
     * title.
     *
     * @param isHcpProfessional
     * @param title
     * @return
     */
    public static HcpRole obtainHcpRole(boolean isHcpProfessional, String title) {
        HcpRole result = null;
        if (isHcpProfessional) {
            if (title.equals(HcpRole.ADMINISTRATOR.toString())) {
                result = HcpRole.ADMINISTRATOR;
            } else if (title.equals(HcpRole.MEDICAL_DOCTOR.toString())) {
                result = HcpRole.MEDICAL_DOCTOR;
            } else if (title.equals(HcpRole.PHARMACIST.toString())) {
                result = HcpRole.PHARMACIST;
            } else if (title.equals(HcpRole.NURSE.toString())) {
                result = HcpRole.NURSE;
            }
        } else {
            result = HcpRole.PATIENT;
        }
        return result;
    }

    /**
     * This helper method will obtain the user permissions, based on the role
     * and on database properties.
     *
     * @param role
     * @return
     */
    public static List<String> obtainPermissionsForRole(HcpRole role) {

        final String PORTAL_DOCTOR_PERMISSIONS = "PORTAL_DOCTOR_PERMISSIONS";
        final String PORTAL_PHARMACIST_PERMISSIONS = "PORTAL_PHARMACIST_PERMISSIONS";
        final String PORTAL_NURSE_PERMISSIONS = "PORTAL_NURSE_PERMISSIONS";
        final String PORTAL_ADMIN_PERMISSIONS = "PORTAL_ADMIN_PERMISSIONS";
        final String PORTAL_PATIENT_PERMISSIONS = "PORTAL_PATIENT_PERMISSIONS";

        List<String> result = new ArrayList<>();

        switch (role) {
            case ADMINISTRATOR: {
                result = fillRolePermissions(ConfigurationManagerService.getInstance().getProperty(PORTAL_ADMIN_PERMISSIONS));
                break;
            }
            case MEDICAL_DOCTOR: {
                result = fillRolePermissions(ConfigurationManagerService.getInstance().getProperty(PORTAL_DOCTOR_PERMISSIONS));
                break;
            }
            case NURSE: {
                result = fillRolePermissions(ConfigurationManagerService.getInstance().getProperty(PORTAL_NURSE_PERMISSIONS));
                break;
            }
            case PATIENT: {
                result = fillRolePermissions(ConfigurationManagerService.getInstance().getProperty(PORTAL_PATIENT_PERMISSIONS));
                break;
            }
            case PHARMACIST: {
                result = fillRolePermissions(ConfigurationManagerService.getInstance().getProperty(PORTAL_PHARMACIST_PERMISSIONS));
                break;
            }
        }

        return result;
    }

    /**
     * Helper method that will fill a SAML compliant permission entry.
     *
     * @param permissions
     * @return
     */
    public static List<String> fillRolePermissions(String permissions) {
        final List result = new ArrayList<>();

        String[] p = permissions.split(",");

        return Arrays.asList(p);
    }

    public static PersonalAttribute getPersonalAttribute(STORKAuthnResponse storkResponse, String key) {
        return storkResponse.getPersonalAttributeList().get(key);
    }

    public static PersonalAttribute getPersonalAttribute(IPersonalAttributeList attrs, String key) {
        return attrs.get(key);
    }

    /**
     * Obtains a node value from a given XML String and an XPATH Expression
     *
     * @param xmlContent      - The XML content to be parsed.
     * @param xpathExpression - The XPATH expression that leads to the node.
     * @return The node value.
     */
    private static String obtainNodeValue(final String xmlContent, final String xpathExpression) {
        if (xmlContent.isEmpty()) {
            LOG.error("The provided XML content is empty.");
            return null;
        }
        if (xpathExpression.isEmpty()) {
            LOG.error("The provided XPATH Expression is empty.");
            return null;
        }

        final InputSource source;
        final DocumentBuilderFactory dbf;
        final DocumentBuilder db;
        final Document document;
        final XPathFactory xpathFactory;
        String result = null;

        try {
            // Parse XML String
            source = new InputSource(new StringReader(xmlContent));

            // Build Document
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            document = db.parse(source);

            // Evaluate XPATH and assign returning value
            xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            result = xpath.evaluate(xpathExpression, document);

        } catch (ParserConfigurationException ex) {
            LOG.error("An error has orccurred while obtaining the Node value from an XML file.", ex);
        } catch (SAXException ex) {
            LOG.error("An error has orccurred while obtaining the Node value from an XML file.", ex);
        } catch (IOException ex) {
            LOG.error("An error has orccurred while obtaining the Node value from an XML file.", ex);
        } catch (XPathExpressionException ex) {
            LOG.error("An error has orccurred while obtaining the Node value from an XML file.", ex);
        }
        return result;
    }

    /**
     * Obtains all the required attributes to search for a patient for a given
     * country.
     * <p>
     * The result is a Map containing ONLY the required attributes, combined in
     * a (key,value), where the Key is the "STORK ID" and Value is "Search Mask
     * id".
     * <p>
     * E.g. "(eIdentifier,personal.patient.search.patient.id)"
     * <p>
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
        final String FILE_FOLDER = "portal/forms";
        final String FILE_PATH;
        final String FILE_NAME;
        final String fileContent;
        final Path filePath;
        final Document searchMaskFileDom;
        final NodeList nl;
        Map<String, String> result;

        FILE_NAME = "InternationalSearch_" + countryCode + ".xml";
        FILE_PATH = System.getenv("EPSOS_PROPS_PATH") + FILE_FOLDER + "/";
        result = new HashMap<>();

        /* READ SEARCH MASK FILE CONTENT TO DOM OBJECT */
        try {
            searchMaskFileDom = XMLUtil.parseContent(Files.readAllBytes(FileSystems.getDefault().getPath(FILE_PATH, FILE_NAME)));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOG.error(ex.getLocalizedMessage());
            return null;
        }

        /* APPLY XPATH EXPRESSION TO GET NODES WITH LABEL ATTRIBUTE - THE ONES REQUIRED FOR IDENTIFICATION */
        try {
            nl = (NodeList) XPathFactory.newInstance().newXPath().compile(XPATH_EXPR).evaluate(searchMaskFileDom, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            LOG.error(ex.getLocalizedMessage());
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
