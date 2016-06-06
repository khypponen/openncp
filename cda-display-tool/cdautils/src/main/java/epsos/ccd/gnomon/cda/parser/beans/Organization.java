package epsos.ccd.gnomon.cda.parser.beans;

import epsos.ccd.gnomon.cda.parser.enums.URLScheme;

import java.util.HashSet;
import java.util.Set;

/**
 * A class encapsulating a generic organization entity.
 *
 * @author Akis Papadopoulos
 *
 */
public class Organization {

    // Identification number
    private Identifier id;
    // Optional name of the organization
    private String name;
    // Optional address
    private Address address;
    // Optional phone set
    private Set<Contact> phones;
    // Optional email set
    private Set<Contact> emails;

    /**
     * A constructor initializing an organization entity.
     */
    public Organization() {
        // Setting a null identification number
        this.id = null;

        // Setting a null name of the organization
        this.name = null;

        // Setting a null address
        this.address = null;

        // Creating an empty set of phone contacts
        this.phones = new HashSet<Contact>();

        // Creating an empty set of email contacts
        this.emails = new HashSet<Contact>();
    }

    /**
     * A constructor initializing an organization entity.
     *
     * @param id the identification number of the organization.
     * @param name the name of the organization.
     */
    public Organization(Identifier id, String name) {
        // Setting the identification number
        this.id = id;

        // Setting the name of the organization
        this.name = name;

        // Setting a null address
        this.address = null;

        // Creating an empty set of phone contacts
        this.phones = new HashSet<Contact>();

        // Creating an empty set of email contacts
        this.emails = new HashSet<Contact>();
    }

    /**
     * A method returning the id of the organization.
     *
     * @return the id of the organization.
     */
    public Identifier getId() {
        return id;
    }

    /**
     * A method setting the organization id.
     *
     * @param id the id to set.
     */
    public void setId(Identifier id) {
        this.id = id;
    }

    /**
     * A method returning the name of the organization.
     *
     * @return the name of the organization.
     */
    public String getName() {
        return name;
    }

    /**
     * A method setting the name of the organization.
     *
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * A method returning the address of the organization.
     *
     * @return the address of the organization.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * A method setting the address of the organization.
     *
     * @param address the address to set.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * A method returning the set of phones of the organization.
     *
     * @return the set of phones of the organization.
     */
    public Set<Contact> getPhones() {
        return phones;
    }

    /**
     * A method returning the set of emails of the organization.
     *
     * @return the set of emails of the organization.
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
     * A parsing method transforms object data into a text-based XML format.
     *
     * @return an XML representation of the object.
     */
    @Override
    public String toString() {
        // Creating a context holder
        StringBuilder context = new StringBuilder();

        // Checking for a valid identification number
        if (id != null) {
            // Adding the id element
            context.append(id);
        }

        // Checking if the name of the organization is provided
        if (name != null) {
            // Adding a name element
            context.append("<name>")
                    .append(name)
                    .append("</name>");
        } else {
            // Adding a null flavored name element
            context.append("<name nullFlavor=\"UNK\"/>");
        }

        // Checking if any phone is provided
        if (phones.size() > 0) {
            // Iterating through the set of phones
            for (Contact phone : phones) {
                // Checking for a valid contact element
                if (phone != null) {
                    // Adding the next phone contact element
                    context.append(phone);
                }
            }
        }

        // Checking if any email contact is provided
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

        // Checking if the address is provided
        if (address != null) {
            // Adding the address element
            context.append(address);
        }

        return context.toString();
    }
}
