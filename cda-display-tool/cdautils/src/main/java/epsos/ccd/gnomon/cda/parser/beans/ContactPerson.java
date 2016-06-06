package epsos.ccd.gnomon.cda.parser.beans;

import epsos.ccd.gnomon.cda.parser.enums.RelationshipRole;
import epsos.ccd.gnomon.cda.parser.enums.RoleClass;
import epsos.ccd.gnomon.cda.parser.enums.URLScheme;

import java.util.HashSet;
import java.util.Set;

/**
 * A class encapsulating a participant as a contact person.
 *
 * @author Akis Papadopoulos
 *
 */
public class ContactPerson extends Participant {

    // Name of the contact person
    private Name name;
    // Optional phone set
    private Set<Contact> phones;
    // Optional email set
    private Set<Contact> emails;
    // Optional address
    private Address address;
    // Relationship role code of the contact person
    private RelationshipRole relationship;

    /**
     * A constructor initializing a contact person.
     */
    public ContactPerson() {
        // Calling the constructor of the super class
        super();

        // Setting a null name of the contact person
        this.name = null;

        // Creating an empty set of phone contacts
        this.phones = new HashSet<Contact>();

        // Creating an empty set of email contacts
        this.emails = new HashSet<Contact>();

        // Setting a null address of the participant
        this.address = null;

        // Setting a null relationship role of the contact person
        this.relationship = null;
    }

    /**
     * A constructor initializing a contact person.
     *
     * @param role the role class of the participant contact person.
     * @param name the name of the contact person.
     * @param relationship the contact person relationship role.
     */
    public ContactPerson(RoleClass role, Name name, RelationshipRole relationship) {
        // Calling the constructor of the super class
        super(role);

        // Setting the name of the contact person
        this.name = name;

        // Creating an empty set of phone contacts
        this.phones = new HashSet<Contact>();

        // Creating an empty set of email contacts
        this.emails = new HashSet<Contact>();

        // Setting a null address of the participant
        this.address = null;

        // Setting the relationship role of the contact person
        this.relationship = relationship;
    }

    /**
     * A method returning the name of the contact person.
     *
     * @return the name of the contact person.
     */
    public Name getName() {
        return name;
    }

    /**
     * A method setting the name of the person.
     *
     * @param name the name to set.
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * A method returning the set of phones of the contact person.
     *
     * @return the set of phones of the contact person.
     */
    public Set<Contact> getPhones() {
        return phones;
    }

    /**
     * A method returning the set of emails of the contact person.
     *
     * @return the set of emails of the contact person.
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
     * A method returning the address of the contact person.
     *
     * @return the address of the contact person.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * A method setting the address of the person.
     *
     * @param address the address to set.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * A method returning the relationship of the contact person.
     *
     * @return the relationship of the contact person.
     */
    public RelationshipRole getRelationship() {
        return relationship;
    }

    /**
     * A method setting the relationship of the contact.
     *
     * @param relationship the relationship to set.
     */
    public void setRelationship(RelationshipRole relationship) {
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

        // Checking if the relationship code is provided
        if (relationship != null) {
            // Opening the relationship type code element
            context.append("<code");

            // Setting the code attribute
            context.append(" ")
                    .append("code=\"")
                    .append(relationship)
                    .append("\"");

            // Setting the display name attribute
            context.append(" ")
                    .append("displayName=\"")
                    .append(relationship.getDisplay())
                    .append("\"");

            // Setting the code system attribute
            context.append(" ")
                    .append("codeSystem=\"")
                    .append(relationship.CODE_SYSTEM)
                    .append("\"");

            // Closing the code element
            context.append("/>");
        }

        // Checking if the address is provided
        if (address != null) {
            // Adding the address sub-element
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

        // Closing the associatedPerson sub-element
        context.append("<associatedPerson>");

        // Checking for a valid name element
        if (name != null) {
            // Adding the name element
            context.append(name);
        }

        // Closing the associatedPerson element
        context.append("</associatedPerson>");

        // Closing the associatedEntity element
        context.append("</associatedEntity>");

        // Closing the participant element
        context.append("</participant>");

        return context.toString();
    }
}
