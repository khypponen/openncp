package eu.epsos.assertionvalidator;

import org.opensaml.saml2.core.Assertion;
import tr.com.srdc.epsos.securityman.exceptions.InsufficientRightsException;
import tr.com.srdc.epsos.securityman.exceptions.InvalidFieldException;
import tr.com.srdc.epsos.securityman.exceptions.MissingFieldException;

/**
 * Created:
 * Date: 2012-11-26
 * Time: 13:06
 * By: fredrik.dahlman@cag.se
 */
public interface PolicyManagerInterface {

    static final String URN_OASIS_NAMES_TC_XACML_2_0_SUBJECT_ROLE = "urn:oasis:names:tc:xacml:2.0:subject:role";
    static final String URN_EPSOS_NAMES_WP3_4_SUBJECT_HEALTHCARE_FACILITY_TYPE = "urn:epsos:names:wp3.4:subject:healthcare-facility-type";
    static final String URN_EPSOS_NAMES_WP3_4_SUBJECT_ON_BEHALF_OF = "urn:epsos:names:wp3.4:subject:on-behalf-of";
    static final String URN_OASIS_NAMES_TC_XACML_1_0_SUBJECT_SUBJECT_ID = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
    static final String URN_OASIS_NAMES_TC_XACML_1_0_RESOURCE_RESOURCE_ID = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
    static final String URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_PURPOSEOFUSE = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    static final String URN_OASIS_NAMES_TC_XSPA_1_0_ENVIRONMENT_LOCALITY = "urn:oasis:names:tc:xspa:1.0:environment:locality";
    static final String URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION = "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission";
    static final String URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_006 = "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission:PRD-006";
    static final String URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_003 = "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission:PRD-003";
    static final String URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_005 = "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission:PRD-005";
    static final String URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_010 = "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission:PRD-010";
    static final String URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_016 = "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission:PRD-016";
    static final String URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_004 = "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission:PRD-004";
    static final String URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PPD_046 = "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission:PPD-046";
    static final String URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PPD_032 = "urn:oasis:names:tc:xspa:1.0:subject:hl7:permission:PPD-032";

    void XSPASubjectValidatorForHCP(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException;
    void XSPASubjectValidatorForTRC(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException;
    void XSPARoleValidator(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException;
    void OnBehalfOfValidator(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException;
    void HealthcareFacilityValidator(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException;
    void PurposeOfUseValidator(Assertion assertion, String documentClass) throws MissingFieldException, InsufficientRightsException;
    void XSPALocalityValidator(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException;
    void XCPDPermissionValidator(Assertion assertion) throws InsufficientRightsException;
    void XCAPermissionValidator(Assertion assertion, String documentClass) throws InsufficientRightsException, MissingFieldException;
    void XDRPermissionValidator(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException, InsufficientRightsException;
    boolean isConsentGiven(String patientId, String countryId);
}
