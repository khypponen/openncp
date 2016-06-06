package epsos.ccd.gnomon.cda.parser.beans;

import epsos.ccd.gnomon.cda.parser.enums.ContactUse;
import epsos.ccd.gnomon.cda.parser.enums.URLScheme;
import java.util.HashSet;
import java.util.Set;

/**
 * A class encapsulating the demographics of a patient.
 *
 * @author Akis Papadopoulos
 *
 */
public class Patient {

    // Patient type code
    private String typeCode;
    // Patient context control code
    private String contextControlCode;
    // Patient class code
    private String classCode;
    // Patient primary identification number
    private Identifier id;
    // Optional set of secondary identifiers
    private Set<Identifier> secondaries;
    // Patient full name
    private Name name;
    // Optional administrative gender
    private Gender gender;
    // Patient date of birth
    private Birthdate dob;
    // Optional patient address
    private Address address;
    // Optional phone set
    private Set<Contact> phones;
    // Optional email set
    private Set<Contact> emails;
    // Optional guardian of the patient
    private Guardian guardian;

    /**
     * A constructor initializing a patient.
     */
    public Patient() {
        // Setting a null type code
        this.typeCode = null;

        // Setting a null context control code
        this.contextControlCode = null;

        // Setting a null class code
        this.classCode = null;

        // Setting a null primary id
        this.id = null;

        // Creating an emtpy set of secondary ids
        secondaries = new HashSet<Identifier>();

        // Setting a null name
        this.name = null;

        // Setting a null gender
        this.gender = null;

        // Setting a null date of birth
        this.dob = null;

        // Setting a null address
        this.address = null;

        // Creating an empty set of phone contacts
        phones = new HashSet<Contact>();

        // Creating an empty set of email contacts
        emails = new HashSet<Contact>();

        // Setting a nul guardian
        this.guardian = null;
    }

    /**
     * A constructor initializing a patient.
     *
     * @param id the primary id of the patient.
     * @param name the full name of the patient.
     * @param gender the administrative gender.
     * @param dob the date of birth.
     */
    public Patient(Identifier id, Name name, Gender gender, Birthdate dob) {
        // Setting the primary id
        this.id = id;

        // Creating an emtpy set of secondary ids
        secondaries = new HashSet<Identifier>();

        // Setting the name
        this.name = name;

        // Setting the gender
        this.gender = gender;

        // Setting the date of birth
        this.dob = dob;

        // Creating an empty set of phone contacts
        phones = new HashSet<Contact>();

        // Creating an empty set of email contacts
        emails = new HashSet<Contact>();
    }

    /**
     * A method returning the type code.
     *
     * @return the type code of the patient.
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * A method setting the type code of the patient.
     *
     * @param typeCode the type code of the patient.
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * A method returning the context control code.
     *
     * @return the context control code of the patient.
     */
    public String getContextControlCode() {
        return contextControlCode;
    }

    /**
     * A method setting the context control code of the patient.
     *
     * @param contextControlCode the context control code.
     */
    public void setContextControlCode(String contextControlCode) {
        this.contextControlCode = contextControlCode;
    }

    /**
     * A method returning the class code of the patient.
     *
     * @return the class code of the patient.
     */
    public String getClassCode() {
        return classCode;
    }

    /**
     * A method setting the class code of the patient.
     *
     * @param classCode the class code to set.
     */
    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    /**
     * A method returning the primary id of the patient.
     *
     * @return the primary id of the patient.
     */
    public Identifier getId() {
        return id;
    }

    /**
     * A method returning the secondary ids of the patient.
     *
     * @return the secondary ids of the patient.
     */
    public Set<Identifier> getSecondaries() {
        return secondaries;
    }

    /**
     * A method setting an identifier of the patient.
     *
     * @param id the patient identifier.
     */
    public void setId(Identifier id) {
        // Checking if the primary id is already defined
        if (id == null) {
            this.id = id;
        } else {
            // Adding the id into the secondary ids
            secondaries.add(id);
        }
    }

    /**
     * A method returning the name of the patient.
     *
     * @return the name of the patient.
     */
    public Name getName() {
        return name;
    }

    /**
     * A method setting the name of the patient.
     *
     * @param name the name of the patient.
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * A method returning the gender of the patient.
     *
     * @return the gender of the patient.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * A method setting the gender of the patient.
     *
     * @param gender the gender to set.
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * A method returning the birth date of the patient.
     *
     * @return the birth date of the patient.
     */
    public Birthdate getDob() {
        return dob;
    }

    /**
     * A method setting the birth date of the patient.
     *
     * @param dob the birth date to set.
     */
    public void setDob(Birthdate dob) {
        this.dob = dob;
    }

    /**
     * A method returning the address of the patient.
     *
     * @return the address of the patient.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * A method setting the address of the patient.
     *
     * @param address the address of the patient.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * A method returning the phone set of the patient.
     *
     * @return the phone set of the patient.
     */
    public Set<Contact> getPhones() {
        return phones;
    }

    /**
     * A method returning the email set of the patient.
     *
     * @return the email set of the patient.
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
        //Getting the use of the contact
        ContactUse use = contact.getUse();

        //Getting the prefix
        URLScheme prefix = contact.getPrefix();

        //Getting the value of the contact
        String value = contact.getValue();

        // Checking the type of contact
        if (prefix != null && prefix.equals(URLScheme.TEL)) {
            // Adding contact into the phone set
            phones.add(contact);
        } else if (prefix != null && prefix.equals(URLScheme.MAILTO)) {
            // Adding contact into the email set
            emails.add(contact);
        } else if (use == null && prefix == null && value == null) {
            // Adding a null falvor contact into the phone set
            phones.add(contact);
        }
    }

    /**
     * A method returning the guardian of the patient.
     *
     * @return the guardian of the patient.
     */
    public Guardian getGuardian() {
        return guardian;
    }

    /**
     * A method setting the guardian of the patient.
     *
     * @param guardian the guardian to set.
     */
    public void setGuardian(Guardian guardian) {
        this.guardian = guardian;
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
        context.append("<!--patient-data-->");

        // Opening the record target element
        context.append("<recordTarget");

        // Checking if the type code is provided
        if (typeCode != null) {
            // Adding the type code attribute
            context.append(" ")
                    .append("typeCode=\"")
                    .append(typeCode)
                    .append("\"");
        }

        // Checking if the context control code is provided
        if (contextControlCode != null) {
            // Adding the context control code attribute
            context.append(" ")
                    .append("contextControlCode=\"")
                    .append(contextControlCode)
                    .append("\"");
        }

        // Closing the record target opening element
        context.append(">");

        // Opening the patient role element
        context.append("<patientRole");

        // Checking if the class code is provided
        if (classCode != null) {
            // Adding the context class code attribute
            context.append(" ")
                    .append("classCode=\"")
                    .append(classCode)
                    .append("\"");
        }

        // Closing the patient role opening element
        context.append(">");

        // Checking if the primary id is provided
        if (id != null) {
            // Adding the primary id element
            context.append(id);
        }

        // Checking if any secondary identification number is provided
        if (secondaries != null) {
            // Iterating through the set of secondary ids
            for (Identifier sid : secondaries) {
                // Checking for a valid id element, otherwise ignore it
                if (sid != null) {
                    // Adding the next secondary id element
                    context.append(sid);
                }
            }
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

        // Opening the patient element
        context.append("<patient>");

        // Checking if the name is provided
        if (name != null) {
            // Adding the name element
            context.append(name);
        }

        // Checking if the administrative gender is provided
        if (gender != null) {
            // Adding the gender element
            context.append(gender);
        }

        // Checking if the date of birth is provided
        if (dob != null) {
            // Adding the birth time element
            context.append(dob);
        }

        // Checking if any guardian is provided
        if (guardian != null) {
            // Adding the guardian element
            context.append(guardian);
        }

        // Closing the patient sub-element
        context.append("</patient>");

        // Closing the patient role element
        context.append("</patientRole>");

        // Closing the record target element
        context.append("</recordTarget>");

        // Adding a closing section comment
        context.append("<!--patient-data-->");

        return context.toString();
    }
}
