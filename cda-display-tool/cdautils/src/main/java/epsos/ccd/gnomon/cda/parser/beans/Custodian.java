package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating a custodian of a medical document.
 *
 * @author Akis Papadopoulos
 *
 */
public class Custodian {

    // Represented organization
    private Organization organization;

    /**
     * A constructor initializing a custodian of medical document.
     */
    public Custodian() {
        // Setting a null represented organization
        this.organization = null;
    }

    /**
     * A constructor initializing a custodian of medical document.
     *
     * @param organization the organization represented by the custodian.
     */
    public Custodian(Organization organization) {
        // Setting the represented organization
        this.organization = organization;
    }

    /**
     * A method returning the represented organization.
     *
     * @return the represented organization.
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * A method setting the organization represented by the custodian.
     *
     * @param organization the organization to set.
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    /**
     * A parsing method transforms object data into a text-based XML format.
     *
     * @return an XML representation of the object.
     */
    @Override
    public String toString() {
        // Creating a context holder
        StringBuilder context = new StringBuilder();

        // Adding an opening section comment
        context.append("<!--custodian-data-->");

        // Opening the custodian element
        context.append("<custodian>");

        // Opening the assigned custodian element
        context.append("<assignedCustodian>");

        // Checking if the represented organization is provided
        if (organization != null) {
            // Adding the represented organization element
            context.append("<representedCustodianOrganization>")
                    .append(organization)
                    .append("</representedCustodianOrganization>");
        }

        // Closing the assigned custodian element
        context.append("</assignedCustodian>");

        // Closing the custodian element
        context.append("</custodian>");

        // Adding a closing section comment
        context.append("<!--custodian-data-->");

        return context.toString();
    }
}
