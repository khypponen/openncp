package epsos.ccd.gnomon.cda.parser.beans;

import epsos.ccd.gnomon.cda.parser.enums.RoleClass;

/**
 * A class encapsulating a participant as a legal organization.
 *
 * @author Akis Papadopoulos
 *
 */
public class LegalOrganization extends Participant {

    // Organization
    private Organization organization;

    /**
     * A constructor initializing a legal organization.
     */
    public LegalOrganization() {
        // Calling the constructor of the super class
        super();

        // Setting a null organization
        this.organization = null;
    }

    /**
     * A constructor initializing a legal organization.
     *
     * @param role the class role of the legal organization.
     * @param organization the organization.
     */
    public LegalOrganization(RoleClass role, Organization organization) {
        // Calling the constructor of the super class
        super(role);

        // Setting the organization
        this.organization = organization;
    }

    /**
     * A method returning the organization.
     *
     * @return the organization.
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * A method setting the organization.
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
        context.append("<!--participant-hcp-organization-data-->");

        // Opening the participant element
        context.append("<participant");

        // Checking if the type code is provided
        if (typeCode != null) {
            // Setting the type code attribute
            context.append(" ")
                    .append("typeCode=\"")
                    .append(typeCode)
                    .append("\"");
        }
        
        // Closing the participant opening element
        context.append("/>");

        // Checking if the template root is provided
        if (root != null) {
            // Adding the template id element
            context.append("<templateId");

            // Setting the root attribute
            context.append(" ")
                    .append("root=\"")
                    .append(root)
                    .append("\"");

            // Closing the template element
            context.append("/>");
        }

        // Adding the function code element
        context.append("<functionCode code=\"PCP\" codeSystem=\"2.16.840.1.113883.5.88\" codeSystemName=\"HL7 ParticipationFunction\"/>");

        // Opening the associated entity element
        context.append("<associatedEntity");

        // Checking if the class role code is provided
        if (role != null) {
            // Setting the class code attribute
            context.append(" ")
                    .append("classCode=\"")
                    .append(role)
                    .append("\"");
        }

        // Closing the opening associated entity element
        context.append("/>");

        // Opening the organization scoping element
        context.append("<scopingOrganization>");

        // Checking if the organization is provided
        if (organization != null) {
            // Adding the the organization
            context.append(organization);
        }

        // Closing the organization element
        context.append("</scopingOrganization>");

        // Closing the associatedEntity element
        context.append("</associatedEntity>");

        // Closing the participant element
        context.append("</participant>");

        // Adding a closing section comment
        context.append("<!--participant-hcp-organization-data-->");

        return context.toString();
    }
}
