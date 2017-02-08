package eu.epsos.policymanager;

import eu.epsos.assertionvalidator.*;

import static eu.epsos.assertionvalidator.AssertionHelper.getAttributeFromAssertion;

import java.util.List;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.securityman.exceptions.InsufficientRightsException;
import tr.com.srdc.epsos.securityman.exceptions.InvalidFieldException;
import tr.com.srdc.epsos.securityman.exceptions.MissingFieldException;
import tr.com.srdc.epsos.util.Constants;

/**
 * Created: Date: 2012-11-26 Time: 13:14 By: fredrik.dahlman@cag.se
 */
public class DefaultPolicyManagerImpl implements PolicyManagerInterface {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPolicyManagerImpl.class);

    @Override
    public void HealthcareFacilityValidator(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException {
        String healthcareFacilityType = getAttributeFromAssertion(assertion, URN_EPSOS_NAMES_WP3_4_SUBJECT_HEALTHCARE_FACILITY_TYPE);
        if (healthcareFacilityType.equals(HealthcareFacilityType.HOSPITAL.toString())
                || healthcareFacilityType.equals(HealthcareFacilityType.RESIDENT_PHYSICIAN.toString())
                || healthcareFacilityType.equals(HealthcareFacilityType.PHARMACY.toString())
                || healthcareFacilityType.equals(HealthcareFacilityType.OTHER.toString())) {
            logger.info("HCP Identity Assertion Healthcare Facility Type	: " + healthcareFacilityType);
        } else {
            logger.warn("InvalidFieldException: epSOS Healthcare Facility Type 'urn:epsos:names:wp3.4:subject:healthcare-facility-type' attribute in assertion should be one of followings {'Hospital', 'Resident Physician', 'Pharmacy', 'Other'}.");
            throw new InvalidFieldException("epSOS Healthcare Facility Type 'urn:epsos:names:wp3.4:subject:healthcare-facility-type' attribute in assertion should be one of followings {'Hospital', 'Resident Physician', 'Pharmacy', 'Other'}.");
        }
    }

    @Override
    public void OnBehalfOfValidator(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException {
        String onBehalfOfRole = getAttributeFromAssertion(assertion, URN_EPSOS_NAMES_WP3_4_SUBJECT_ON_BEHALF_OF);
        if (onBehalfOfRole.equals(OnBehalfOf.DENTIST.toString())
                || onBehalfOfRole.equals(OnBehalfOf.NURSE.toString())
                || onBehalfOfRole.equals(OnBehalfOf.PHARMACIST.toString())
                || onBehalfOfRole.equals(OnBehalfOf.PHYSICIAN.toString())
                || onBehalfOfRole.equals(OnBehalfOf.NURSE_MIDWIFE.toString())) {
            logger.info("HCP Identity Assertion OnBehalfOf	: " + onBehalfOfRole);
        } else {
            throw new InvalidFieldException("OnBehalfOf 'urn:epsos:names:wp3.4:subject:on-behalf-of' attribute in assertion should be one of followings {'dentist', 'nurse', 'pharmacist', 'physician', 'nurse midwife'}.");
        }

    }

    @Override
    public void XSPARoleValidator(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException {
        String xspaRole = getAttributeFromAssertion(assertion, URN_OASIS_NAMES_TC_XACML_2_0_SUBJECT_ROLE);
        if (xspaRole.equals(XSPARole.NURSE.toString())
                || xspaRole.equals(XSPARole.PHARMACIST.toString())
                || xspaRole.equals(XSPARole.PHYSICIAN.toString())
                || xspaRole.equals("medical doctor") // TODO: this role is not valid..
                || xspaRole.equals(XSPARole.NURSE_MIDWIFE.toString())
                || xspaRole.equals(XSPARole.PATIENT.toString())) {
            logger.info("HCP Identity Assertion XSPA Role	: " + xspaRole);
        } else if (xspaRole.equals(XSPARole.ANCILLARY_SERVICES.toString())
                || xspaRole.equals(XSPARole.CLINICAL_SERVICES.toString())) {
            logger.info("HCP Identity Assertion XSPA Role	: " + xspaRole);
            OnBehalfOfValidator(assertion, documentClass);
        } else {
            logger.error("Found XSPA Role: " + xspaRole);
            logger.error("XSPA Role 'urn:oasis:names:tc:xacml:2.0:subject:role' attribute in assertion should be one of followings {'nurse', 'pharmacist', 'physician', 'medical doctor', 'nurse midwife', 'ancillary services' , 'clinical services', 'patient'}.");
            throw new InvalidFieldException("The user role is invalid. It should be one of followings {'nurse', 'pharmacist', 'physician', 'medical doctor', 'nurse midwife', 'ancillary services' , 'clinical services', 'patient'}");
        }
    }

    @Override
    public void XSPASubjectValidatorForHCP(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException {
        String subjectId = getAttributeFromAssertion(assertion, URN_OASIS_NAMES_TC_XACML_1_0_SUBJECT_SUBJECT_ID);
        if (subjectId.equals("")) {
            throw new InvalidFieldException("XSPA Subject 'urn:oasis:names:tc:xacml:1.0:subject:subject-id' attribute in assertion should be filled.");
        }
        logger.info("HCP Identity Assertion XSPA Subject: " + subjectId);
    }

    @Override
    public void XSPASubjectValidatorForTRC(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException {
        String resourceId = getAttributeFromAssertion(assertion, URN_OASIS_NAMES_TC_XACML_1_0_RESOURCE_RESOURCE_ID);
        if (resourceId.equals("")) {
            throw new InvalidFieldException("XSPA subject 'uurn:oasis:names:tc:xacml:1.0:resource:resource-id' attribute in assertion should be filled.");
        }
        logger.info("TRC Assertion XSPA subject	: " + resourceId);
    }

    @Override
    public void PurposeOfUseValidator(Assertion assertion, String documentClass) throws MissingFieldException, InsufficientRightsException {
        String resourceId = getAttributeFromAssertion(assertion, URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_PURPOSEOFUSE);
        if (resourceId.equals("TREATMENT") || resourceId.equals("EMERGENCY")) {
            logger.info("HCP Identity Assertion XSPA Purpose of Use	: " + resourceId);
        } else {
            logger.error("InsufficientRightsException");
            throw new InsufficientRightsException();
        }
    }

    @Override
    public void XSPALocalityValidator(Assertion assertion, String documentClass) throws MissingFieldException, InvalidFieldException {
        String environmentLocality = getAttributeFromAssertion(assertion, URN_OASIS_NAMES_TC_XSPA_1_0_ENVIRONMENT_LOCALITY);
        if (environmentLocality.equals("")) {
            throw new InvalidFieldException("XSPA Locality 'urn:oasis:names:tc:xspa:1.0:environment:locality' attribute in assertion should be filled.");
        }
        logger.info("HCP Identity Assertion XSPA Locality	: " + environmentLocality);
    }

    @Override
    public void XCPDPermissionValidator(Assertion assertion) throws InsufficientRightsException {
        List<XMLObject> permissions = AssertionHelper.getPermissionValuesFromAssertion(assertion);
        for (XMLObject permission : permissions) {
            logger.info("HCP Identity Assertion XSPD Permission	: " + permission.getDOM().getTextContent());
            if (permission.getDOM().getTextContent().equals(URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_006)) {
                logger.info("Found permission for PRD-006 (Patient Identification and Lookup)");
                return;
            }
        }
        logger.error("InsufficientRightsException");
        throw new InsufficientRightsException();
    }

    @Override
    public void XCAPermissionValidator(Assertion assertion, String documentClass) throws InsufficientRightsException, MissingFieldException {
        if (documentClass.equals(Constants.PS_CLASSCODE)) {
            XCAPermissionValidatorPS(assertion);
        } else if (documentClass.equals(Constants.EP_CLASSCODE)) {
            XCAPermissionValidatorEP(assertion);
        } else if (documentClass.equals(Constants.MRO_CLASSCODE)) {
            XCAPermissionValidatorMro(assertion);
        } else {
            String errorMsg = "Invalid document class code: " + documentClass;
            logger.error(errorMsg);
            throw new MissingFieldException(errorMsg);  // TODO: What to do when wrong class code
        }

    }

    private void XCAPermissionValidatorPS(Assertion assertion) throws InsufficientRightsException {
        boolean medicalHistory = false;
        boolean vitalSign = false;
        boolean patientMedications = false;
        boolean reviewProblem = false;


        List<XMLObject> permissions = AssertionHelper.getPermissionValuesFromAssertion(assertion);
        String permissionValue;
        String xspaRole;

        //Check allowed roles
        try {
            xspaRole = getAttributeFromAssertion(assertion, URN_OASIS_NAMES_TC_XACML_2_0_SUBJECT_ROLE);
        } catch (MissingFieldException ex) {
            logger.error("A MissingFieldException was caugth. The assertion role could not be obtained.");
            throw new InsufficientRightsException();
        }
        if (!xspaRole.equals(XSPARole.PHYSICIAN.toString()) && !xspaRole.equals("medical doctor") && !xspaRole.equals(XSPARole.PATIENT.toString())) {
            logger.error("InsufficientRightsException - Unsupported role (named: " + xspaRole + ") tried to access Patient Summary documents.");
            throw new InsufficientRightsException();
        }

        //Check required permissions
        for (XMLObject permission : permissions) {
            permissionValue = permission.getDOM().getTextContent();
            logger.info("HCP Identity Assertion XSPA Permission	: " + permissionValue);
            if (permissionValue.equals(URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_003)) {
                medicalHistory = true;
                logger.info("Found permission for PRD-003 (Review Medical History)");
            } else if (permissionValue.equals(URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_005)) {
                vitalSign = true;
                logger.info("Found permission for PRD-005 (Review Vital Signs/Patient Measurements)");
            } else if (permissionValue.equals(URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_010)) {
                patientMedications = true;
                logger.info("Found permission for PRD-010 (Review Patient Medications)");
            } else if (permissionValue.equals(URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_016)) {
                reviewProblem = true;
                logger.info("Found permission for PRD-016 (Review Problems)");
            }
        }

        if (medicalHistory && vitalSign && patientMedications && reviewProblem) {
            return;
        }
        logger.error("InsufficientRightsException");
        throw new InsufficientRightsException();
    }

    /**
     * XCA for order service (ePrescription)
     *
     * @param assertion the SAML Assertion
     * @throws InsufficientRightsException
     */
    private void XCAPermissionValidatorEP(Assertion assertion) throws InsufficientRightsException {
        boolean reviewExistingOrders = false;
        boolean patientMedications = false;

        List<XMLObject> permissions = AssertionHelper.getPermissionValuesFromAssertion(assertion);
        String xspaRole;

        //Check allowed roles
        try {
            xspaRole = getAttributeFromAssertion(assertion, URN_OASIS_NAMES_TC_XACML_2_0_SUBJECT_ROLE);
        } catch (MissingFieldException ex) {
            logger.error("A MissingFieldException was caugth. The assertion role could not be obtained.");
            throw new InsufficientRightsException();
        }
        if (!xspaRole.equals(XSPARole.PHARMACIST.toString())) {
            logger.error("InsufficientRightsException - Unsupported (named: " + xspaRole + ") role tried to access ePrescriptions documents.");
            throw new InsufficientRightsException();
        }

        //Check required permissions
        for (XMLObject permission : permissions) {
            logger.info("HCP Identity Assertion XSPA Permission	: " + permission.getDOM().getTextContent());
            if (permission.getDOM().getTextContent().equals(URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_004)) {
                reviewExistingOrders = true;
                logger.info("Found permission for PRD-004 (Review Existing Orders)");
            } else if (permission.getDOM().getTextContent().equals(URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PRD_010)) {
                patientMedications = true;
                logger.info("Found permission for PRD-010 (Review Patient Medications)");
            }
        }

        if (reviewExistingOrders && patientMedications) {
            return;
        }
        logger.error("InsufficientRightsException");
        throw new InsufficientRightsException();
    }

    /**
     * XCA validator for MRO service, currently using the same validator for eP.
     *
     * @param assertion
     * @throws InsufficientRightsException
     */
    private void XCAPermissionValidatorMro(Assertion assertion) throws InsufficientRightsException {
        XCAPermissionValidatorEP(assertion);
    }

    @Override
    public void XDRPermissionValidator(Assertion assertion, String documentClass) throws InsufficientRightsException, MissingFieldException, InvalidFieldException {
        if (documentClass.equals(Constants.ED_CLASSCODE) || documentClass.equals(Constants.HCER_CLASSCODE)) {
            XDRPermissionValidatorEDOrHCER(assertion);
        } else if (documentClass.equals(Constants.CONSENT_CLASSCODE)) {
            XDRPermissionValidatorConsent(assertion);
        } else {
            String errorMsg = "Invalid document class code: " + documentClass;
            logger.error(errorMsg);
            throw new MissingFieldException(errorMsg);  // TODO: What to do when wrong class code?
        }
    }

    /**
     * XDR for dispensation service (eDispensation) or HCER service TODO: add
     * support for discard operation
     *
     * @param assertion the SAML Assertion
     * @throws InsufficientRightsException
     */
    private void XDRPermissionValidatorEDOrHCER(Assertion assertion) throws InsufficientRightsException {
        boolean recordMedicationAdministrationRecord = false;

        List<XMLObject> permissions = AssertionHelper.getPermissionValuesFromAssertion(assertion);
        String xspaRole;

        //Check allowed roles
        try {
            xspaRole = getAttributeFromAssertion(assertion, URN_OASIS_NAMES_TC_XACML_2_0_SUBJECT_ROLE);
        } catch (MissingFieldException ex) {
            logger.error("A MissingFieldException was caugth. The assertion role could not be obtained.");
            throw new InsufficientRightsException();
        }
        if (!xspaRole.equals(XSPARole.PHARMACIST.toString()) && !xspaRole.equals(XSPARole.PHYSICIAN.toString()) && !xspaRole.equals("medical doctor")) {
            logger.error("InsufficientRightsException - Unsupported role (named: " + xspaRole + ") tried to submit eDispensations or HCER documents.");
            throw new InsufficientRightsException();
        }


        //Check required permissions
        for (XMLObject permission : permissions) {
            logger.info("HCP Identity Assertion XSPA Permission	: " + permission.getDOM().getTextContent());
            if (permission.getDOM().getTextContent().equals(URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PPD_046)) {
                recordMedicationAdministrationRecord = true;
                logger.info("Found permission for PPD-046 (Record Medication Administration Record)");
            }
        }

        if (!recordMedicationAdministrationRecord) {
            logger.error("InsufficientRightsException");
            throw new InsufficientRightsException();
        }
    }

    /**
     * XDR for patient consent TODO: add support for consent discard operation
     *
     * @param assertion the SAML Assertion
     * @throws InsufficientRightsException
     */
    private void XDRPermissionValidatorConsent(Assertion assertion) throws InsufficientRightsException {
        boolean recordMedicationAdministrationRecord = false;

        List<XMLObject> permissions = AssertionHelper.getPermissionValuesFromAssertion(assertion);
        String xspaRole;

        //Check allowed roles
        try {
            xspaRole = getAttributeFromAssertion(assertion, URN_OASIS_NAMES_TC_XACML_2_0_SUBJECT_ROLE);
        } catch (MissingFieldException ex) {
            logger.error("A MissingFieldException was caugth. The assertion role could not be obtained.");
            throw new InsufficientRightsException();
        }
        if (!xspaRole.equals(XSPARole.PHARMACIST.toString()) && !xspaRole.equals(XSPARole.PHYSICIAN.toString()) && !xspaRole.equals("medical doctor")) {
            logger.error("InsufficientRightsException - Unsupported role (named: " + xspaRole + ") tried to submit consent documents.");
            throw new InsufficientRightsException();
        }


        //Check required permissions
        for (XMLObject permission : permissions) {
            logger.info("HCP Identity Assertion XSPA Permission	: " + permission.getDOM().getTextContent());
            if (permission.getDOM().getTextContent().equals(URN_OASIS_NAMES_TC_XSPA_1_0_SUBJECT_HL7_PERMISSION_PPD_032)) {
                recordMedicationAdministrationRecord = true;
                logger.info("Found permission for PPD-032 (New Consents and Authorizations)");
            }
        }

        if (!recordMedicationAdministrationRecord) {
            logger.error("InsufficientRightsException");
            throw new InsufficientRightsException();
        }
    }

    @Override
    public boolean isConsentGiven(String patientId, String countryId) {
        boolean consentGiven = true;
        logger.info("Checking consent of patient " + patientId + " for country " + countryId);
        // TODO: actual check
        logger.info("Consent of patient " + patientId + " valid for country " + countryId + ": " + consentGiven);
        return consentGiven;
    }
}
