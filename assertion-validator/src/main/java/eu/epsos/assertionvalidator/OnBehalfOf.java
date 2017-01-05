package eu.epsos.assertionvalidator;

/**
 * Created:
 * Date: 2012-11-26
 * Time: 11:20
 * By:fredrik.dahlman@cag.se
 */

/**
 * From WP3.4 Deliberable D.3.4.2
 * Delegated Rights
 * FriendlyName:  OnBehalfOf
 * Name:  urn:epsos:names:wp3.4:subject:on-behalf-of
 */
public enum OnBehalfOf {

    DENTIST("dentist"),
    NURSE("nurse"),
    PHARMACIST("pharmacist"),
    PHYSICIAN("physician"),
    NURSE_MIDWIFE("nurse midwife");

    private final String role;

    OnBehalfOf(final String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
