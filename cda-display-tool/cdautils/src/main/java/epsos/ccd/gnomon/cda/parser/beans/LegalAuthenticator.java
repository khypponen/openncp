package epsos.ccd.gnomon.cda.parser.beans;

import epsos.ccd.gnomon.cda.parser.enums.URLScheme;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A class encapsulating a legal authenticator of a medical document.
 *
 * @author Akis Papadopoulos
 *
 */
public class LegalAuthenticator {

    // Date formatter
    private static SimpleDateFormat dater = new SimpleDateFormat("yyyyMMdd");
    // Authenticating time
    private Date time;
    // Authenticator identification number
    private Identifier id;
    // Authenticator name
    private Name name;
    // Option authenticator address
    private Address address;
    // Optional authenticator phone set
    private Set<Contact> phones;
    // Optional authenticator email set
    private Set<Contact> emails;
    // Optional represented organization
    private Organization organization;

    /**
     * A constructor initializing a legal authenticator entity.
     */
    public LegalAuthenticator() {
        // Setting a null athentication time
        this.time = null;

        // Setting a null identification number
        this.id = null;

        // Setting a null full name
        this.name = null;

        // Setting a null address
        this.address = null;

        // Creating an empty set of phone contacts
        this.phones = new HashSet<Contact>();

        // Creating an empty set of email contacts
        this.emails = new HashSet<Contact>();

        // Setting a null represented organization
        this.organization = null;
    }

    /**
     * A constructor initializing a legal authenticator entity.
     *
     * @param id the identification number of the authenticator.
     * @param name the full name of the authenticator.
     * @param organization the organization represented by the authenticator.
     */
    public LegalAuthenticator(Identifier id, Name name, Organization organization) {
        // Setting a null authenticating time
        this.time = null;

        // Setting the identification number
        this.id = id;

        // Setting the full name
        this.name = name;

        // Setting a null address
        this.address = null;

        // Creating an empty set of phone contacts
        this.phones = new HashSet<Contact>();

        // Creating an empty set of email contacts
        this.emails = new HashSet<Contact>();

        // Setting the represented organization
        this.organization = organization;
    }

    /**
     * A method returning the authenticating date time.
     *
     * @return the authenticating date time.
     */
    public Date getTime() {
        return time;
    }

    /**
     * A method setting the authenticating date time.
     *
     * @param time the authenticating date time to set.
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * A method returning the id of the authenticator.
     *
     * @return the id of the authenticator.
     */
    public Identifier getId() {
        return id;
    }

    /**
     * A method setting the id of the authenticator.
     *
     * @param id the id to set.
     */
    public void setId(Identifier id) {
        this.id = id;
    }

    /**
     * A method returning the name of the authenticator.
     *
     * @return the name of the authenticator.
     */
    public Name getName() {
        return name;
    }

    /**
     * A method setting the name of the authenticator.
     *
     * @param name the name to set.
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * A method returning the address of the authenticator.
     *
     * @return the address of the authenticator.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * A method setting the address of the authenticator.
     *
     * @param address the address to set.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * A method returning the phone set of the authenticator.
     *
     * @return the phone set of the authenticator.
     */
    public Set<Contact> getPhones() {
        return phones;
    }

    /**
     * A method returning the email set of the authenticator.
     *
     * @return the email set of the authenticator.
     */
    public Set<Contact> getEmails() {
        return emails;
    }

    /**
     * A method adding a new generic contact.
     *
     * @param contact a generic contact.
     */
    public void addContact(Contact contact) {
        // Checking the type of contact
        if (contact.getPrefix().equals(URLScheme.TEL)) {
            // Adding contact into the phone set
            phones.add(contact);
        } else if (contact.getPrefix().equals(URLScheme.MAILTO)) {
            // Adding contact into the email set
            emails.add(contact);
        }
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
     * A method setting the the organization represented by the authenticator.
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
        context.append("<!--legal-authenticator-data-->");

        // Opening the legal authenticator element
        context.append("<legalAuthenticator>");

        // Checking if the authenticating date time is provided
        if (time != null) {
            // Adding the time element
            context.append("<time ")
                    .append(" ")
                    .append("value=\"")
                    .append(dater.format(time))
                    .append("\"/>");
        } else {
            // Adding a null flavored time element
            context.append("<time nullFlavor=\"UNK\"/>");
        }

        // Adding a signature code element
        context.append("<signatureCode nullFlavor=\"NI\"/>");

        // Opening the assigned entity element
        context.append("<assignedEntity>");

        // Checking if the authenticator id is provided
        if (id != null) {
            // Adding the id element
            context.append(id);
        }

        // Checking if the address is provided
        if (address != null) {
            // Adding the address element
            context.append(address);
        }

        // Checking if phone contacts are provided
        if (phones.size() > 0) {
            // Iterating through the set of phones
            for (Contact phone : phones) {
                // Checking for a valid contact element
                if (phone != null) {
                    // Adding the next contact element
                    context.append(phone);
                }
            }
        }

        // Checking if email contacts are provided
        if (emails.size() > 0) {
            // Iterating through the email set
            for (Contact email : emails) {
                // Checking for a valid contact element
                if (email != null) {
                    // Adding the next contact element
                    context.append(email);
                }
            }
        }

        // Adding a null flavored assigned person sub-element
        context.append("<assignedPerson nullFlavor=\"NI\"/>");

        // Checking if organization is provided
        if (organization != null) {
            // Adding the represented organization element
            context.append("<representedOrganization>")
                    .append(organization)
                    .append("</representedOrganization>");
        }

        // Closing the assigned entity sub-element
        context.append("</assignedEntity>");

        // Closing the authenticator element
        context.append("</legalAuthenticator>");

        // Adding a closing section comment
        context.append("<!--legal-authenticator-data-->");

        return context.toString();
    }
}
