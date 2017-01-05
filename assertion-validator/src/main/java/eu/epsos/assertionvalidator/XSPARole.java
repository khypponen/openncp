package eu.epsos.assertionvalidator;

/**
 * Created:
 * Date: 2012-11-26
 * Time: 10:58
 * By: Fredrik.dahlman@cag.se
 */

/**
 * From WP3.4 Deliberable D.3.4.2
 * Structural Role of the HCP
 * FriendlyName:  XSPA Role
 * Name:  urn:oasis:names:tc:xacml:2.0:subject:role
 */
public enum XSPARole {

    DENTIST("dentist"),
    NURSE("nurse"),
    PHARMACIST("pharmacist"),
    PHYSICIAN("physician"),
    NURSE_MIDWIFE("nurse midwife"),
    ADMISSION_CLERK("admission clerk"),
    ANCILLARY_SERVICES("ancillary services"),
    CLINICAL_SERVICES("clinical services"),
    PATIENT("patient"); //PAC Service Role

    private final String role;

    XSPARole(final String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
