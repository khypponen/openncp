package epsos.ccd.gnomon.cda.parser.beans;

import epsos.ccd.gnomon.cda.parser.enums.Profession;
import epsos.ccd.gnomon.cda.parser.enums.URLScheme;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A class encapsulating an author of a medical document.
 *
 * @author Akis Papadopoulos
 *
 */
public class Author {

    // Date formatter
    private static SimpleDateFormat dater = new SimpleDateFormat("yyyyMMdd");
    // Authoring time
    private Date time;
    // Author class code
    private String classCode;
    // Author identification number
    private Identifier id;
    // Optional author name
    private Name name;
    // Optional author address
    private Address address;
    // Optional author phone set
    private Set<Contact> phones;
    // Optional author email set
    private Set<Contact> emails;
    // Optional author profession
    private Profession profession;
    // Optional represented organization
    private Organization organization;

    /**
     * A constructor initializing an author of medical document.
     */
    public Author() {
        // Setting a null authoring time
        this.time = null;

        // Setting a null class code
        this.classCode = null;

        // Setting a null identification number of the author
        this.id = null;

        // Setting a null full name of the author
        this.name = null;

        // Setting a null address of the author
        this.address = null;

        // Creating an empty set of phone contacts
        this.phones = new HashSet<Contact>();

        // Creating an empty set of email contacts
        this.emails = new HashSet<Contact>();

        // Setting a null profession of the author
        this.profession = null;

        // Setting a null represented organization
        this.organization = null;
    }

    /**
     * A constructor initializing an author of medical document.
     *
     * @param id the identification number of the author.
     * @param name the full name of the author.
     * @param organization the organization represented by the author.
     */
    public Author(Identifier id, Name name, Organization organization) {
        // Setting the identification number of the author
        this.id = id;

        // Setting the full name of the author
        this.name = name;

        // Setting a null address of the author
        this.address = null;

        // Creating an empty set of phone contacts
        this.phones = new HashSet<Contact>();

        // Creating an empty set of email contacts
        this.emails = new HashSet<Contact>();

        // Setting a null profession of the author
        this.profession = null;

        // Setting the represented organization
        this.organization = organization;
    }

    /**
     * A method returning the authoring date time.
     *
     * @return the authoring date time.
     */
    public Date getTime() {
        return time;
    }

    /**
     * A method setting the authoring date time.
     *
     * @param time the authoring date time to set.
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * A method returning the author class code.
     *
     * @return the class code of the author.
     */
    public String getClassCode() {
        return classCode;
    }

    /**
     * A method setting the author class code.
     *
     * @param classCode the class code to set.
     */
    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    /**
     * A method returning the id of the author.
     *
     * @return the id of the author.
     */
    public Identifier getId() {
        return id;
    }

    /**
     * A method setting the author id.
     *
     * @param id the id to set.
     */
    public void setId(Identifier id) {
        this.id = id;
    }

    /**
     * A method returning the name of the author.
     *
     * @return the name of the author.
     */
    public Name getName() {
        return name;
    }

    /**
     * A method setting the name of the author.
     *
     * @param name the name to set.
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * A method returning the address of the author.
     *
     * @return the address of the author.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * A method setting the address of the author.
     *
     * @param address the address to set.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * A method returning the phone set of the author.
     *
     * @return the phone set of the author.
     */
    public Set<Contact> getPhones() {
        return phones;
    }

    /**
     * A method returning the email set of the author.
     *
     * @return the email set of the author.
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
     * A method returning the profession of the author.
     *
     * @return the profession of the author.
     */
    public Profession getProfession() {
        return profession;
    }

    /**
     * A method setting the profession of the author.
     *
     * @param profession the profession to set.
     */
    public void setProfession(Profession profession) {
        this.profession = profession;
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
     * A method setting the represented organization.
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
        context.append("<!--author-data-->");

        // Opening the author element
        context.append("<author>");

        // Checking if the profession is provided, otherwise ignore
        if (profession != null) {
            // Opening a function code element
            context.append("<functionCode");

            // Setting the code attribute
            context.append(" ")
                    .append("code=\"")
                    .append(profession)
                    .append("\"");

            // Setting the code system attribute
            context.append(" ")
                    .append("codeSystem=\"")
                    .append(profession.CODE_SYSTEM)
                    .append("\"");

            // Closing the function code element
            context.append("/>");
        }

        // Checking if the authoring date time is provided
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

        // Opening an assigned author element
        context.append("<assignedAuthor");

        // Checking if the class code is provided
        if (classCode != null) {
            // Adding the class code attribute
            context.append(" ")
                    .append("classCode=\"")
                    .append(classCode)
                    .append("\"");
        }

        // Closing the assigned author opening element
        context.append(">");

        // Checking for a valid id element
        if (id != null) {
            // Adding the id element
            context.append(id);
        }

        // Checking for a valid address element, otherwise ignore it
        if (address != null) {
            // Adding the address element
            context.append(address);
        }

        // Checking if any phone contact are provided
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

        // Checking if any email contact are provided
        if (emails.size() > 0) {
            // Iterating through the set of emails
            for (Contact email : emails) {
                // Checking for a valid contact element
                if (email != null) {
                    // Adding the next contact element
                    context.append(email);
                }
            }
        }

        // Checking if the author name is provided, otherwise use a device
        if (name != null) {
            // Adding an assigned person element
            context.append("<assignedPerson>")
                    .append(name)
                    .append("</assignedPerson>");
        } else {
            // Adding a null flavored authoring device element
            context.append("<assignedAuthoringDevice nullFlavor=\"NI\"/>");
        }

        // Checking if the represented organization is provided
        if (organization != null) {
            // Adding the represented organization element
            context.append("<representedOrganization>")
                    .append(organization)
                    .append("</representedOrganization>");
        }

        // Closing the assigned author element
        context.append("</assignedAuthor>");

        // Closing the author element
        context.append("</author>");

        // Adding a closing section comment
        context.append("<!--author-data-->");

        return context.toString();
    }
}
