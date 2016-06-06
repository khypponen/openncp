package epsos.ccd.gnomon.cda.parser.beans;

import epsos.ccd.gnomon.cda.parser.enums.URLScheme;

import java.util.HashSet;
import java.util.Set;

/**
 * A class encapsulating the demographics of a patient's guardian.
 *
 * @author Akis Papdopoulos
 *
 */
public class Guardian {

    // Guardian full name
    private Name name;
    // Optional guardian address
    private Address address;
    // Optional phone set
    private Set<Contact> phones;
    // Optional email set
    private Set<Contact> emails;
    // Optional relationship role code of the guardian
    private Code relationship;

    /**
     * A constructor initializing the guardian.
     */
    public Guardian() {
        // Setting a null name
        this.name = null;

        // Setting a null address
        this.address = null;

        // Creating an empty set of phone contacts
        this.phones = new HashSet<Contact>();

        // Creating an empty set of contacts
        this.emails = new HashSet<Contact>();

        // Setting a null relationship
        this.relationship = null;
    }

    /**
     * A constructor initializing the guardian.
     *
     * @param name the name of the guardian.
     * @param relationship the relationship role of the guardian.
     */
    public Guardian(Name name, Code relationship) {
        // Setting the name
        this.name = name;

        // Setting a null address
        this.address = null;

        // Creating an empty set of phone contacts
        this.phones = new HashSet<Contact>();

        // Creating an empty set of contacts
        this.emails = new HashSet<Contact>();

        // Setting the relationship
        this.relationship = relationship;
    }

    /**
     * A method returning the name of the guardian.
     *
     * @return the name of the guardian.
     */
    public Name getName() {
        return name;
    }

    /**
     * A method setting the name of the guardian.
     *
     * @param name the name to set.
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * A method returning the address of the guardian.
     *
     * @return the address of the guardian.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * A method setting the address of the guardian.
     *
     * @param address the address to set.Φ
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * A method returning the phone set of the guardian.
     *
     * @return the phone set of the guardian.
     */
    public Set<Contact> getPhones() {
        return phones;
    }

    /**
     * A method returning the email set of the guardian.
     *
     * @return the email set of the guardian.
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
     * A method returning the relationship of the guardian.
     *
     * @return the relationship of the guardian.
     */
    public Code getRelationship() {
        return relationship;
    }

    /**
     * A method setting the relationship role.
     *
     * @param relationship the relationship role to set.
     */
    public void setRelationship(Code relationship) {
        this.relationship = relationship;
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

        // Opening the guardian element
        context.append("<guardian classCode=\"GUARD\">");

        // Adding the template root element
        context.append("<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.2.4\"/>");

        // Checking if the relationship code is provided
        if (relationship != null) {
            // Adding the relationship type code element
            context.append(relationship);
        }

        // Checking if the address is provided
        if (address != null) {
            // Adding the address element
            context.append(address);
        }

        // Checking if any phone contact is provided
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

        // Opening the guardian person element
        context.append("<guardianPerson>");

        // Checking if the name of the guardian is provided
        if (name != null) {
            // Adding the name element
            context.append(name);
        }

        // Closing the guardian person element
        context.append("</guardianPerson>");

        // Closing the guardian element
        context.append("</guardian>");

        return context.toString();
    }
}
