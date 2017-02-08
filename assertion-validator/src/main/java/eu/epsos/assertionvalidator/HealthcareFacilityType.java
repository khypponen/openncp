package eu.epsos.assertionvalidator;

/**
 * Created:
 * Date: 2012-11-26
 * Time: 11:20
 * By:fredrik.dahlman@cag.se
 */

/**
 * From WP3.4 Deliberable D.3.4.2
 * Type of HCPO
 * FriendlyName:  epSOS Healthcare Facility Type
 * Name:  urn:epsos:names:wp3.4:subject:healthcare-facility-type
 */
public enum HealthcareFacilityType {

    HOSPITAL("Hospital"),
    RESIDENT_PHYSICIAN("Resident Physician"),
    PHARMACY("Pharmacy"),
    OTHER("Other");

    private final String facilityType;

    HealthcareFacilityType(final String facilityType) {
        this.facilityType = facilityType;
    }

    @Override
    public String toString() {
        return facilityType;
    }
}
