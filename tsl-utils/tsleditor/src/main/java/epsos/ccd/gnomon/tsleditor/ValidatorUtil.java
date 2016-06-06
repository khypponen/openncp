/***Licensed to the Apache Software Foundation (ASF) under one
*or more contributor license agreements.  See the NOTICE file
*distributed with this work for additional information
*regarding copyright ownership.  The ASF licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
**/package epsos.ccd.gnomon.tsleditor;

import epsos.ccd.gnomon.tsleditor.model.TrustService;
import epsos.ccd.gnomon.tsleditor.model.TrustServiceList;
import epsos.ccd.gnomon.tsleditor.model.TrustServiceProvider;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Locale;
import javax.swing.JTextField;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.SignatureMethod;
import org.w3._2000._09.xmldsig_.SignatureType;

/**
 * This is a utility class providing static helper methods to perform all the validations needed in the User Interface.
 * It also holds a list of static String definitions for certain options/tags/attributes in the generated xml, which have
 * to be filled in with specific values, according to the EPSOS specification.
 *
 * @author bouzianis
 */
public class ValidatorUtil {
    /**
     * The mandatory fixed Strings to use in tags/attributes/values of xml tags in the TSL, according to the EPSOS specification
     */
    public final static String TSL_TAG="http://uri.etsi.org/02231",
                               TSL_ID="NCPConfiguration-",
                               TSL_VERSION_IDENTIFIER="3",
                               TSL_TYPE="http://uri.etsi.org/TrstSvc/TSLType/generic",
                               TSL_SCHEME_NAME="NCP-Service Status List: ",
                               TSL_STATUS_DETERMINATION_APPROACH="http://uri.etsi.org/TrstSvc/TSLType/StatusDetn/eSigDir-1999-93-EC-TrustedList/appropriate",
                               TSL_EPSOS_SECURITY_POLICY_DOC="http://www.epsos.eu/docs/SecPol.pdf",
                               TSL_EPSOS_FRAMEWORK_AGGREEMENT_DOC="http://www.epsos.eu/docs/fwa.pdf",
                               TSL_ESPSOS_COMMUNITY_RULES="http://www.epsos.eu";


    /**
     * This method returns true if the given String is null, or it is the empty String, or it is a String only containing
     * space characters or the word "null".
     * @param s the String to check if is null or not
     * @return true if the String is null, false otherwise
     */
    public final static boolean isNull(String s)
    {
        if (s == null || s.trim().equals("") || s.equalsIgnoreCase("null"))
            return true;
        else
            return false;
    }

     /**
     * This method returns true if the given String is not null, and not the empty String, and not a String only containing
     * space characters or the word "null".
     * @param s the String to check if is not null
     * @return true if the String is not null, false otherwise
     */
    public final static boolean isNotNull(String s)
    {
        return !isNull(s);
    }

    /**
     * This method returns true if the given text field's text value is null, according to the isNull method above

     * @param s the JTextField to check if its value is null or not
     * @return true if the text field value is null, false otherwise
     */
    public final static boolean isNull(JTextField s)
    {
        if (s == null)
            return true;
        else
            return isNull(s.getText());
    }

    /**
     * This method returns true if the given text field's text value is not null, according to the isNotNull method above

     * @param s the JTextField to check if its value is not null
     * @return true if the text field value is not null, false otherwise
     */
    public final static boolean isNotNull(JTextField s)
    {
        if (s == null)
            return false;
        else
           return !isNull(s.getText());
    }


    /**
     * This method returns a unique subject line String to display to the user, identifying an X509 digital certificate.
     * The String returned contains the name of the X509 Isser Principal, followed by the X509 certificate serial number.
     *
     * @param certificate an X509 digital certificate
     * @return a unique subject line String used in the interface to identify the given certificate, or null if the certificate is null
     */
    public final static String getUniqueSubjectLine(X509Certificate certificate)
    {
        String result = null;
        if (certificate != null)
        {
            result = certificate.getIssuerX500Principal().getName()+" Serial Number #"+certificate.getSerialNumber();
        }
        return result;
    }


    /**
     * A utility method validating according to the EPSOS specification the XML envelope of the TSL.
     * The envelope must be present, and it must have a valid TSLTag and ID attribute, a TSLVersionIdentifier equal to 3,
     * a TSLSequenceNumber, a valid TSLType, a SchemeOperatorName and PostalAddress in both the local language
     * and the English language, a SchemeName, SchemeInformationURIS pointing to the EPSOS security policy and
     * framework agreement documents, a valid StatusDeterminationApproach, valid SchemeTypeCommunityRules,
     * HistoricalInformationPeriod must be 0, a filled in ListIssueDateAndTime, NextUpdate and DistributionPoint,
     * exactly 2 NCP Service providers, NCP-A and NCP-B and no Service Extensions. If any of these conditions are
     * not met, the corresponding error messages are appended to the given StringBuilder errors object
     *
     * @param trustServiceList the TrustServiceList object to validate the envelope of
     * @param errorString the StringBuilder object containing any validation errors found so far, where envelope
     *        validation errors will be appended to, if found.
     */
    public final static void validateEnvelope(TrustServiceList trustServiceList, StringBuilder errorString)
    {
        try {
            if (!TSL_TAG.equals(trustServiceList.getTrustStatusList().getTSLTag()))
                appendErrorMessage(errorString, "TSLTag must be \""+TSL_TAG+"\"");

            if (!trustServiceList.getTrustStatusList().getId().startsWith(TSL_ID))
                 appendErrorMessage(errorString, "@ID should be \""+TSL_ID+"CountryCode\"");

            if (!TSL_VERSION_IDENTIFIER.equals(trustServiceList.getTrustStatusList().getSchemeInformation().getTSLVersionIdentifier().toString()))
                appendErrorMessage(errorString, "TSLVersionIdentifier must be \""+TSL_VERSION_IDENTIFIER+"\"");

            if (trustServiceList.getTrustStatusList().getSchemeInformation().getTSLSequenceNumber() == null)
                appendErrorMessage(errorString, "TSLSequenceNumber must be used");

            if (!trustServiceList.getTrustStatusList().getSchemeInformation().getTSLType().equals(TSL_TYPE))
                appendErrorMessage(errorString, "TSLType must be \""+TSL_TYPE+"\"");

            if (ValidatorUtil.isNull(trustServiceList.getSchemeOperatorName()))
                appendErrorMessage(errorString, "SchemeOperatorName must be provided in the national language");

            if (ValidatorUtil.isNull(trustServiceList.getSchemeOperatorName(Locale.ENGLISH)))
                appendErrorMessage(errorString, "SchemeOperatorName should be provided in English");

            if (trustServiceList.getSchemeOperatorPostalAddress(Locale.getDefault()) == null)
                appendErrorMessage(errorString, "SchemeOperatorAddress must be provided in the national language");

            if (trustServiceList.getSchemeOperatorPostalAddress(Locale.ENGLISH) == null)
                appendErrorMessage(errorString, "SchemeOperatorAddress should be provided in English");

            if (ValidatorUtil.isNull(trustServiceList.getSchemeName()) || 
                    !trustServiceList.getSchemeName().startsWith(TSL_SCHEME_NAME))
                appendErrorMessage(errorString, "SchemeName must be provider in the national language and it must be \""+TSL_SCHEME_NAME+"Countryname (Countrycode)\"");

            List<String> uris = trustServiceList.getSchemeInformationUris();
            if (uris!=null && uris.size()>0)
            {
                for (String uri : uris)
                {
                    if (!uri.equals(TSL_EPSOS_SECURITY_POLICY_DOC) &&
                            !uri.equals(TSL_EPSOS_FRAMEWORK_AGGREEMENT_DOC))
                       appendErrorMessage(errorString, "SchemeInformationURI should refer to the epSOS Security Policy (\""+
                               TSL_EPSOS_SECURITY_POLICY_DOC+"\") and the epSOS framework agreement (\""+
                               TSL_EPSOS_FRAMEWORK_AGGREEMENT_DOC+"\")");
                }
            }

            if (!TSL_STATUS_DETERMINATION_APPROACH.equals(trustServiceList.getStatusDeterminationApproach()))
                appendErrorMessage(errorString, "StatusDeterminationApproach must be \""+TSL_STATUS_DETERMINATION_APPROACH+"\"");

            if (trustServiceList.getTrustStatusList().getSchemeInformation().getSchemeTypeCommunityRules() == null ||
                    trustServiceList.getTrustStatusList().getSchemeInformation().getSchemeTypeCommunityRules().getURI() == null ||
                    trustServiceList.getTrustStatusList().getSchemeInformation().getSchemeTypeCommunityRules().getURI().size() <= 0 ||
                   !TSL_ESPSOS_COMMUNITY_RULES.equals(trustServiceList.getTrustStatusList().getSchemeInformation().getSchemeTypeCommunityRules().getURI().get(0)))
                appendErrorMessage(errorString, "SchemeTypeCommunityRules must be \""+TSL_ESPSOS_COMMUNITY_RULES+"\"");

            if (ValidatorUtil.isNull(trustServiceList.getSchemeTerritory()))
                appendErrorMessage(errorString, "SchemeTerritory must be the country code of the country that operates the NCP");

            if (!BigInteger.ZERO.equals(trustServiceList.getTrustStatusList().getSchemeInformation().getHistoricalInformationPeriod()))
                appendErrorMessage(errorString, "HistoricalInformationPeriod must be 0");

            if (trustServiceList.getListIssueDateTime() == null)
                appendErrorMessage(errorString, "ListIssueDateAndTime must be filled in");

            if (trustServiceList.getNextUpdate() == null)
                appendErrorMessage(errorString, "NextUpdate must be filled in");

            if (trustServiceList.getTrustStatusList().getSchemeInformation().getDistributionPoints() == null ||
                    trustServiceList.getTrustStatusList().getSchemeInformation().getDistributionPoints().getURI() == null ||
                    trustServiceList.getTrustStatusList().getSchemeInformation().getDistributionPoints().getURI().size() <= 0)
                appendErrorMessage(errorString, "DistributionPoint must be filled in");

            if (trustServiceList.getTrustStatusList().getSchemeInformation().getSchemeExtensions() != null &&
                    trustServiceList.getTrustStatusList().getSchemeInformation().getSchemeExtensions().getExtension() != null &&
                    trustServiceList.getTrustStatusList().getSchemeInformation().getSchemeExtensions().getExtension().size() > 0)
                appendErrorMessage(errorString, "SchemeExtensions must NOT be used");

            if (trustServiceList.getTrustServiceProviders() == null ||
                    trustServiceList.getTrustServiceProviders().size() != 2)
                appendErrorMessage(errorString, "Exactly 2 Trust Service Providers must be present, NCP-A and NCP-B");

           if(trustServiceList.getTrustStatusList().getSignature() == null)
               appendErrorMessage(errorString, "Trust Service List must contain a valid Signature");
        } catch (Exception e)
        {

        }
    }

    /**
     * A utility method validating according to the EPSOS specification the XML of the given NCP Provider object.
     * The provider must have a TradeName, a ProviderName, a ProviderAddress and a list of TrustServices containing
     * at least 3 specific service types for NCP-A and 2 for NCP-B, Each of these services will be also validated
     * with a further call to @see validateService. If any of these conditions are
     * not met, the corresponding error messages are appended to the given StringBuilder errors object
     *
     * @param provider the TrustServiceProvider object to validate
     * @param errorString the StringBuilder object containing any validation errors found so far, where NCP provider
     *        validation errors will be appended to, if found.
     */
    public final static void validateNCP(TrustServiceProvider provider, StringBuilder errorString)
    {
        try {
            if (isNull(provider.getTradeName()))
                appendErrorMessage(errorString, "TSPTradeName must be set to either NCP-A or NCP-B respectively");

            if (ValidatorUtil.isNull(provider.getName()))
                appendErrorMessage(errorString, "NCP ProviderName must be provided for "+provider.getTradeName());

            if (provider.getPostalAddress() == null)
                appendErrorMessage(errorString, "NCP ProviderAddress must be provided for "+provider.getName());

            List<TrustService> services = provider.getTrustServices();
            if (services == null)
                appendErrorMessage(errorString, "No Services defined for provider "+provider.getName());
            else
            {
                if ("NCP-A".equals(provider.getTradeName()))
                {
                    // NCP-A specific validation
                    if (services.size() < 3)
                        appendErrorMessage(errorString, "Provider NCP-A must define at least 3 services: epSOS VPN Gateway, epSOS NCP and epSOS Patient Identification");
                    // must have services : gateway, ncp, patient identification
                    boolean gatewayFound = false, ncpFound = false, patientIdentificationFound = false,
                    // must not have services: identity provider
                            identityProviderFound = false;

                    for (TrustService s: services)
                    {
                        if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_VPN_GATEWAY))
                            gatewayFound = true;
                        if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_NCP))
                            ncpFound = true;
                        if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_PATIENT_IDENTIFICATION))
                            patientIdentificationFound = true;
                        if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_IDENTITY_PROVIDER))
                            identityProviderFound = true;
                        validateService(s, errorString, true);
                    }

                    // must have services : gateway, ncp, patient identification
                    if (!gatewayFound)
                        appendErrorMessage(errorString, "Provider NCP-A must define a service for epSOS VPN Gateway");
                    if (!ncpFound)
                        appendErrorMessage(errorString, "Provider NCP-A must define a service for epSOS NCP");
                    if (!patientIdentificationFound)
                        appendErrorMessage(errorString, "Provider NCP-A must define a service for epSOS Patient Identification");
                    // must not have services: identity provider
                    if (identityProviderFound)
                        appendErrorMessage(errorString, "Provider NCP-A must not define a service for HCP Identity Provider");

                }
                else
                {
                    // NCP-B specific validation
                    if (services.size() < 2)
                        appendErrorMessage(errorString, "Provider NCP-B must define at least 2 services: epSOS VPN Gateway and epSOS NCP");

                    // must have services : gateway, ncp
                    boolean gatewayFound = false, ncpFound = false,
                    // must NOT have services : identification, patient, order, dispensation, consent
                            patientIdentificationFound = false, patientServiceFound = false,
                            orderServiceFound = false, dispensationServiceFound = false,
                            consentServiceFound = false;

                    for (TrustService s: services)
                    {
                        if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_VPN_GATEWAY))
                            gatewayFound = true;
                        if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_NCP))
                            ncpFound = true;
                        if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_PATIENT_IDENTIFICATION))
                            patientIdentificationFound = true;
                        if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_PATIENT_SERVICE))
                            patientServiceFound = true;
                        if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_ORDER_SERVICE))
                            orderServiceFound = true;
                        if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_DISPENSATION_SERVICE))
                            dispensationServiceFound = true;
                        if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_CONSENT_SERVICE))
                            consentServiceFound = true;

                        validateService(s, errorString, false);
                    }

                    // must have services : gateway, ncp
                    if (!gatewayFound)
                        appendErrorMessage(errorString, "Provider NCP-B must define a service for epSOS VPN Gateway");
                    if (!ncpFound)
                        appendErrorMessage(errorString, "Provider NCP-B must define a service for epSOS NCP");
                    // must NOT have services : identification, patient, order, dispensation, consent
                    if (patientIdentificationFound)
                        appendErrorMessage(errorString, "Provider NCP-B must not define a service for epSOS Patient Identification");
                    if (patientServiceFound)
                        appendErrorMessage(errorString, "Provider NCP-B must not define a service for epSOS Patient Service");
                    if (orderServiceFound)
                        appendErrorMessage(errorString, "Provider NCP-B must not define a service for epSOS Order Service");
                    if (dispensationServiceFound)
                        appendErrorMessage(errorString, "Provider NCP-B must not define a service for epSOS Dispensation Service");
                    if (consentServiceFound)
                        appendErrorMessage(errorString, "Provider NCP-B must not define a service for epSOS Consent Service");
                }
            }
        } catch (Exception e)
        {

        }
    }


    /**
     * A utility method validating according to the EPSOS specification the XML of the given EPSOS Service object.
     * Each Service object must have a name, a valid status, a status starting time. It must not have s SchemeServiceDefinitionURI,
     * or a ServiceInformationExtension or a TSPServiceDefinitionURI, or ServiceHistory. Moreover, specifically for the
     * VPN Gateway service, at least one digitalid X.509 certificate must be provided, and for the case of NCP-A, Supply Points
     * must also be provided, while for NCP-B they should not be.
     * Specifically for the NCP service there should be at least 2 digital ids and no supply points used.
     * Specifically for the Identity Provider Service at least one digitalid X.509 certificate must be provided, and no supply points used.
     * For the rest of the services no digitals ids should be provided, while there should be supply points provider only for NCP-A.
     * If any of these conditions are not met, the corresponding error messages are appended to the given StringBuilder errors object
     *
     * @param s the TrustService object to validate
     * @param errorString the StringBuilder object containing any validation errors found so far, where Service
     *        validation errors will be appended to, if found.
     * @param ncpA a boolean flag indicating whether the given service is part of ncpA (true) or ncpB (false)
     */
    public final static void validateService(TrustService s, StringBuilder errorString, boolean ncpA)
    {
        String providerTradeName = ncpA? "NCP-A: " : "NCP-B: ";
        try {
            if(isNull(s.getName()))
               appendErrorMessage(errorString, providerTradeName+"ServiceName must be defined for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());

            if (isNull(s.getStatus()))
                appendErrorMessage(errorString, providerTradeName+"ServiceStatus must be defined for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());

            if (s.getStatusStartingTime() == null)
                appendErrorMessage(errorString, providerTradeName+"StatusStartingTime must be defined for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());

            if (s.getTSPService().getServiceInformation().getSchemeServiceDefinitionURI() != null &&
                s.getTSPService().getServiceInformation().getSchemeServiceDefinitionURI().getURI() != null &&
                        s.getTSPService().getServiceInformation().getSchemeServiceDefinitionURI().getURI().size() > 0)
                appendErrorMessage(errorString, providerTradeName+"SchemeServiceDefinitionURI must not be defined for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());

            if (s.getTSPService().getServiceInformation().getServiceInformationExtensions() != null &&
                    s.getTSPService().getServiceInformation().getServiceInformationExtensions().getExtension() != null &&
                    s.getTSPService().getServiceInformation().getServiceInformationExtensions().getExtension().size() > 0)
                appendErrorMessage(errorString, providerTradeName+"ServiceInformationExtension must not be defined for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());

            if (s.getTSPService().getServiceInformation().getTSPServiceDefinitionURI() != null &&
                    s.getTSPService().getServiceInformation().getTSPServiceDefinitionURI().getURI() != null &&
                    s.getTSPService().getServiceInformation().getTSPServiceDefinitionURI().getURI().size() > 0)
                appendErrorMessage(errorString, providerTradeName+"TSPServiceDefinitionURI must not be defined for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());

            if (s.getTSPService().getServiceHistory() != null &&
                    s.getTSPService().getServiceHistory().getServiceHistoryInstance() != null &&
                    s.getTSPService().getServiceHistory().getServiceHistoryInstance().size() > 0)
                appendErrorMessage(errorString, providerTradeName+"ServiceHistory must not be defined for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());

            if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_VPN_GATEWAY))
            {
                // SERVICE_IDENTIFIER_VPN_GATEWAY validation
                // must have at least one digital id (X509)
                if (s.getServiceDigitalIdentity() == null)
                    appendErrorMessage(errorString, providerTradeName+"At least one X.509 digitalId must be defined for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());

                // must have supply points if ncp-a, NOT if ncp-B
                if (s.getTSPService().getServiceInformation().getServiceSupplyPoints() != null &&
                        s.getTSPService().getServiceInformation().getServiceSupplyPoints().getServiceSupplyPoint() != null &&
                        s.getTSPService().getServiceInformation().getServiceSupplyPoints().getServiceSupplyPoint().size() > 0)
                {
                    if (!ncpA)
                    {
                        appendErrorMessage(errorString, providerTradeName+"Service Supply Points must not be provided for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());
                    }
                }
                else if (ncpA)
                {
                    appendErrorMessage(errorString, providerTradeName+"Service Supply Points must be provided for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());
                }
            }
            else if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_NCP))
            {
                // SERVICE_IDENTIFIER_NCP validation
                // must have at least 2 digital ids (X509)
                if (s.getTSPService().getServiceInformation().getServiceDigitalIdentity() == null ||
                    s.getTSPService().getServiceInformation().getServiceDigitalIdentity().getDigitalId() == null ||
                    s.getTSPService().getServiceInformation().getServiceDigitalIdentity().getDigitalId().size() < 2)
                    appendErrorMessage(errorString, providerTradeName+"At least two X.509 digitalId must be defined for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());

                // must have no supply points
                if (s.getTSPService().getServiceInformation().getServiceSupplyPoints() != null &&
                        s.getTSPService().getServiceInformation().getServiceSupplyPoints().getServiceSupplyPoint() != null &&
                        s.getTSPService().getServiceInformation().getServiceSupplyPoints().getServiceSupplyPoint().size() > 0)
                {
                    appendErrorMessage(errorString, providerTradeName+"Service Supply Points must not be provided for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());
                }
            }
            else if (s.getTSPService().getServiceInformation().getServiceTypeIdentifier().equals(ComboBoxesUtil.SERVICE_IDENTIFIER_IDENTITY_PROVIDER))
            {
                // Validation for SERVICE_IDENTIFIER_IDENTITY_PROVIDER

                // must have at least one X509 digitalid
                if (s.getServiceDigitalIdentity() == null)
                    appendErrorMessage(errorString, providerTradeName+"At least one X.509 digitalId must be defined for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());

                 // must have no supply points
                if (s.getTSPService().getServiceInformation().getServiceSupplyPoints() != null &&
                        s.getTSPService().getServiceInformation().getServiceSupplyPoints().getServiceSupplyPoint() != null &&
                        s.getTSPService().getServiceInformation().getServiceSupplyPoints().getServiceSupplyPoint().size() > 0)
                {
                    appendErrorMessage(errorString, providerTradeName+"Service Supply Points must not be provided for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());
                }
            }
            else
            {
                String serviceIdentifier = s.getTSPService().getServiceInformation().getServiceTypeIdentifier();
                if (serviceIdentifier.equals(ComboBoxesUtil.SERVICE_IDENTIFIER_PATIENT_IDENTIFICATION) ||
                        serviceIdentifier.equals(ComboBoxesUtil.SERVICE_IDENTIFIER_PATIENT_SERVICE) ||
                        serviceIdentifier.equals(ComboBoxesUtil.SERVICE_IDENTIFIER_ORDER_SERVICE) ||
                        serviceIdentifier.equals(ComboBoxesUtil.SERVICE_IDENTIFIER_DISPENSATION_SERVICE) ||
                        serviceIdentifier.equals(ComboBoxesUtil.SERVICE_IDENTIFIER_CONSENT_SERVICE))
                {
                    // Validation for
                    // SERVICE_IDENTIFIER_PATIENT_IDENTIFICATION
                    // SERVICE_IDENTIFIER_PATIENT_SERVICE
                    // SERVICE_IDENTIFIER_ORDER_SERVICE
                    // SERVICE_IDENTIFIER_DISPENSATION_SERVICE
                    // SERVICE_IDENTIFIER_CONSENT_SERVICE

                    // must have NO digitalids
                    if (s.getServiceDigitalIdentity() != null)
                        appendErrorMessage(errorString, providerTradeName+"A digitalId must not be defined for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());


                    // must have supply points if ncp-a, NOT if ncp-B
                    if (s.getTSPService().getServiceInformation().getServiceSupplyPoints() != null &&
                            s.getTSPService().getServiceInformation().getServiceSupplyPoints().getServiceSupplyPoint() != null &&
                            s.getTSPService().getServiceInformation().getServiceSupplyPoints().getServiceSupplyPoint().size() > 0)
                    {
                        if (!ncpA)
                        {
                            appendErrorMessage(errorString, providerTradeName+"Service Supply Points must not be provided for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());
                        }
                    }
                    else if (ncpA)
                    {
                        appendErrorMessage(errorString, providerTradeName+"Service Supply Points must be provided for Service Type: "+s.getTSPService().getServiceInformation().getServiceTypeIdentifier());
                    }
                }
            }
        } catch (Exception e)
        {

        }
    }

    /**
     * A utility method validating according to the EPSOS specification the XML signature of the TSL.
     * The signature must be present, and it must have an "EXCLUSIVE" canonicalization method and an
     * "RSA_SHA1" signature method. If not, the corresponding error messages are appending to the errors
     * StringBuilder object passed to this method
     *
     * @param trustServiceList the TrustServiceList object to validate the signature of
     * @param errorString the StringBuilder object containing any validation errors found so far, where signature
     *        validation errors will be appended to, if found.
     */
    public final static void validateSignature(TrustServiceList trustServiceList, StringBuilder errorString)
    {
        SignatureType signature = trustServiceList.getTrustStatusList().getSignature();
        if(signature != null && signature.getSignedInfo() != null)
        {
            if (signature.getSignedInfo().getCanonicalizationMethod() == null ||
                    !CanonicalizationMethod.EXCLUSIVE.equals(signature.getSignedInfo().getCanonicalizationMethod().getAlgorithm()))
                appendErrorMessage(errorString, "TSL Signing Canonicalization Method Algorithm must be: \""+CanonicalizationMethod.EXCLUSIVE+"\"");

            if (signature.getSignedInfo().getSignatureMethod() == null ||
                    !SignatureMethod.RSA_SHA1.equals(signature.getSignedInfo().getSignatureMethod().getAlgorithm()))
                appendErrorMessage(errorString, "TSL Signing Signature Method Algorithm must be: \""+SignatureMethod.RSA_SHA1+"\"");
        }

    }


    /**
     * A utility method to append the given StringBuilder object with an extra String, prepending it with
     * a tab character and a star (*) and post-fixing it with a newline character, in order to produce a
     * "pretty-print" effect of the list of validation error messages.
     *
     * @param errorString the StringBuilder object containing all the error messages so far, where the new error message will be appended
     * @param message the error message to append to the list of error messages
     */
    public final static void appendErrorMessage(StringBuilder errorString, String message)
    {
        errorString.append("\t* "+message+"\n");
    }

}
