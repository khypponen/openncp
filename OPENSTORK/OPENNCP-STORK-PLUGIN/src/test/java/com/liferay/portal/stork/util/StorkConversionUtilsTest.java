/*
 *  StorkConversionUtilsTest.java
 *  Created on 19/06/2014, 1:23:03 PM
 *
 *  stork-plugin
 *  com.liferay.portal.stork.util
 *
 *  Copyright (C) 2014 iUZ Technologies, Lda - http://www.iuz.pt
 */
package com.liferay.portal.stork.util;

import eu.epsos.configmanager.database.HibernateConfigFile;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.services.AssertionValidationService;
import eu.europa.ec.joinup.ecc.openstork.utils.StorkUtils;
import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.PersonalAttributeList;
import eu.stork.peps.auth.commons.STORKAuthnRequest;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.commons.STORKStatusCode;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.transform.TransformerException;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.util.XMLUtil;

/**
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class StorkConversionUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(StorkConversionUtilsTest.class);

    public StorkConversionUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        HibernateConfigFile.name = "src/test/resources/configmanager.hibernate.xml";
        /*
         * Initialize required properties to perform the tests in in-memory database
         */
//        try {
//            ConfigurationManagerService.getInstance().updateProperty("PORTAL_DOCTOR_PERMISSIONS", "PRD-006,PRD-003,PRD-004,PRD-005,PRD-010,PRD-016,PPD-032,PPD-033");
//            ConfigurationManagerService.getInstance().updateProperty("PORTAL_PHARMACIST_PERMISSIONS", "PRD-006,PRD-004,PRD-010,PPD-046");
//            ConfigurationManagerService.getInstance().updateProperty("PORTAL_NURSE_PERMISSIONS", "PRD-006,PRD-004,PRD-010");
//            ConfigurationManagerService.getInstance().updateProperty("PORTAL_ADMIN_PERMISSIONS", "PRD-006,PRD-003,PRD-004,PRD-005,PRD-010,PRD-016,PPD-032,PPD-033");
//            ConfigurationManagerService.getInstance().updateProperty("PORTAL_PATIENT_PERMISSIONS", "PRD-006,PRD-003,PRD-004,PRD-005,PRD-010,PRD-016,PPD-032,PPD-033");
//            ConfigurationManagerService.getInstance().updateProperty("SC_KEYSTORE_PATH", "src/test/resources/keystore/keystore.jks");
//            ConfigurationManagerService.getInstance().updateProperty("SC_KEYSTORE_PASSWORD", "spirit");
//            ConfigurationManagerService.getInstance().updateProperty("SC_PRIVATEKEY_ALIAS", "epsos.min-saude.pt_1");
//            ConfigurationManagerService.getInstance().updateProperty("SC_PRIVATEKEY_PASSWORD", "spirit");
//            ConfigurationManagerService.getInstance().updateProperty("automated.validation", "true");
//        } catch (RuntimeException ex) {
//            Assert.fail(ex.getLocalizedMessage());
//        }
    }

    /**
     * Test of convertStorkToHcpAssertion method, of class StorkConversionUtils.
     */
    @Test
    @Ignore
    public void testConvertStorkToHcpAssertion_STORKAuthnResponse() {
        LOG.info("----------------------------------------------");
        LOG.info(" Starting assertion conversion test");
        LOG.info("----------------------------------------------");

        final String VALIDATION_SCHEMATRON = "epSOS - HCP Identity Assertion";

        STORKAuthnResponse storkResponse = null;
        Assertion expResult = null;

        storkResponse = generateStorkResponse();
        STORKSAMLEngine engine = STORKSAMLEngine.getInstance("SP");

        Assertion result = StorkUtils.convertStorkToHcpAssertion(storkResponse);

        LOG.info("----------------------------------------------");
        LOG.info(" Validating converted assertion at IHE  ");
        LOG.info("----------------------------------------------");

        try {
            assertTrue(new AssertionValidationService().validateSchematron(XMLUtil.prettyPrint(result.getDOM()), VALIDATION_SCHEMATRON, NcpSide.NCP_A));
        } catch (TransformerException ex) {
            LOG.error(null, ex);
        }
    }

    /**
     * This method generates a generic and signed STORK Authentication Response.
     *
     * @return
     */
    private STORKAuthnResponse generateStorkResponse() {
        STORKAuthnResponse result = null;

        STORKSAMLEngine engine = STORKSAMLEngine.getInstance("SP");

        /**
         * The destination.
         */
        String destination;

        /**
         * The service provider name.
         */
        String spName;

        /**
         * The service provider sector.
         */
        String spSector;

        /**
         * The service provider institution.
         */
        String spInstitution;

        /**
         * The service provider application.
         */
        String spApplication;

        /**
         * The service provider country.
         */
        String spCountry;

        /**
         * The service provider id.
         */
        String spId;

        /**
         * The quality authentication assurance level.
         */
        final int QAAL = 3;

        /**
         * The state.
         */
        String state = "ES";

        /**
         * The town.
         */
        String town = "Madrid";

        /**
         * The municipality code.
         */
        String municipalityCode = "MA001";

        /**
         * The postal code.
         */
        String postalCode = "28038";

        /**
         * The street name.
         */
        String streetName = "Marchamalo";

        /**
         * The street number.
         */
        String streetNumber = "3";

        /**
         * The apartament number.
         */
        String apartamentNumber = "5º E";

        /**
         * The List of Personal Attributes.
         */
        IPersonalAttributeList pal;

        /**
         * The assertion consumer URL.
         */
        String assertConsumerUrl;

        /**
         * The authentication request.
         */
        byte[] authRequest;

        /**
         * The authentication response.
         */
        byte[] authResponse = null;
        /**
         * The authentication request.
         */
        STORKAuthnRequest authenRequest = null;

        String ipAddress;

        pal = new PersonalAttributeList();

        PersonalAttribute isAgeOver = new PersonalAttribute();
        isAgeOver.setName("isAgeOver");
        isAgeOver.setIsRequired(false);
        ArrayList<String> ages = new ArrayList<String>();
        ages.add("16");
        ages.add("18");
        isAgeOver.setValue(ages);
        pal.add(isAgeOver);

        PersonalAttribute dateOfBirth = new PersonalAttribute();
        dateOfBirth.setName("dateOfBirth");
        dateOfBirth.setIsRequired(false);
        pal.add(dateOfBirth);

        PersonalAttribute eIDNumber = new PersonalAttribute();
        eIDNumber.setName("eIdentifier");
        eIDNumber.setIsRequired(true);
        pal.add(eIDNumber);

        final PersonalAttribute givenName = new PersonalAttribute();
        givenName.setName("givenName");
        givenName.setIsRequired(true);
        pal.add(givenName);

        PersonalAttribute canRessAddress = new PersonalAttribute();
        canRessAddress.setName("canonicalResidenceAddress");
        canRessAddress.setIsRequired(true);
        pal.add(canRessAddress);

        destination = "http://C-PEPS.gov.xx/PEPS/ColleagueRequest";
        assertConsumerUrl = "http://S-PEPS.gov.xx/PEPS/ColleagueResponse";
        spName = "University Oxford";

        spName = "University of Oxford";
        spSector = "EDU001";
        spInstitution = "OXF001";
        spApplication = "APP001";
        spCountry = "EN";

        spId = "EDU001-APP001-APP001";

        final STORKAuthnRequest request = new STORKAuthnRequest();

        request.setDestination(destination);
        request.setProviderName(spName);
        request.setQaa(QAAL);
        request.setPersonalAttributeList(pal);
        request.setAssertionConsumerServiceURL(assertConsumerUrl);
        // news parameters
        request.setSpSector(spSector);
        request.setSpInstitution(spInstitution);
        request.setSpApplication(spApplication);
        request.setSpCountry(spCountry);
        request.setSPID(spId);
        request.setCitizenCountryCode("ES");

        try {
            authRequest = engine.generateSTORKAuthnRequest(request).getTokenSaml();
            authenRequest = engine.validateSTORKAuthnRequest(authRequest);

        } catch (STORKSAMLEngineException e) {
            fail("Error create STORKAuthnRequest");
        }

        ipAddress = "111.222.333.444";

        pal = new PersonalAttributeList();
        isAgeOver = new PersonalAttribute();
        isAgeOver.setName("isAgeOver");
        isAgeOver.setIsRequired(true);
        ages = new ArrayList<String>();
        ages.add("16");
        ages.add("18");
        isAgeOver.setValue(ages);
        isAgeOver.setStatus(STORKStatusCode.STATUS_AVAILABLE.toString());
        pal.add(isAgeOver);

        dateOfBirth = new PersonalAttribute();
        dateOfBirth.setName("dateOfBirth");
        dateOfBirth.setIsRequired(false);
        final ArrayList<String> date = new ArrayList<String>();
        date.add("16/12/2008");
        dateOfBirth.setValue(date);
        dateOfBirth.setStatus(STORKStatusCode.STATUS_AVAILABLE.toString());
        pal.add(dateOfBirth);

        eIDNumber = new PersonalAttribute();
        eIDNumber.setName("eIdentifier");
        eIDNumber.setIsRequired(true);
        final ArrayList<String> idNumber = new ArrayList<String>();

        idNumber.add("123456789PÑ");
        eIDNumber.setValue(idNumber);

        eIDNumber.setStatus(STORKStatusCode.STATUS_AVAILABLE.toString());
        pal.add(eIDNumber);

        canRessAddress = new PersonalAttribute();

        canRessAddress.setName("canonicalResidenceAddress");
        canRessAddress.setIsRequired(true);
        canRessAddress.setStatus(STORKStatusCode.STATUS_AVAILABLE.toString());
        final HashMap<String, String> address = new HashMap<String, String>();

        address.put("state", state);
        address.put("municipalityCode", municipalityCode);
        address.put("town", town);
        address.put("postalCode", postalCode);
        address.put("streetName", streetName);
        address.put("streetNumber", streetNumber);
        address.put("apartamentNumber", apartamentNumber);

        canRessAddress.setComplexValue(address);

        pal.add(canRessAddress);

        final STORKAuthnResponse response = new STORKAuthnResponse();
        response.setPersonalAttributeList(pal);

        final STORKAuthnResponse storkResponse;
        try {
            storkResponse = engine.generateSTORKAuthnResponse(authenRequest, response, ipAddress, false);
            result = storkResponse;
        } catch (STORKSAMLEngineException ex) {
            LOG.error("An error has occurred during STORK Authentication Response building process.", ex);
        }

        return result;
    }

}
