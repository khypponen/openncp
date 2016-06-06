package epsos.ccd.gnomon.cda.parser.beans;

import epsos.ccd.gnomon.cda.parser.enums.ContactUse;
import epsos.ccd.gnomon.cda.parser.enums.URLScheme;

/**
 * A class encapsulating a generic telecommunication contact.
 *
 * @author Akis Papadopoulos
 *
 */
public class Contact {

    // Use of the contact
    private ContactUse use;
    // URL scheme of the contact
    private URLScheme prefix;
    // Call value of the contact
    private String value;

    /**
     * A constructor initializing a contact.
     */
    public Contact() {
        // Setting a null use of the contact
        this.use = null;

        // Setting a null URL scheme
        this.prefix = null;

        // Setting a null call value of the contact
        this.value = null;
    }

    /**
     * A constructor initializing a generic contact.
     *
     * @param use the use of the contact.
     * @param prefix the URL scheme of the contact.
     * @param value the call value of the contact.
     */
    public Contact(ContactUse use, URLScheme prefix, String value) {
        // Setting the use of the contact
        this.use = use;

        // Setting the URL scheme of the contact
        this.prefix = prefix;

        // Setting the call value of the contact
        this.value = value;
    }

    /**
     * A method returning the use of the contact.
     *
     * @return the use of the contact.
     */
    public ContactUse getUse() {
        return use;
    }

    /**
     * A method setting the use of the contact.
     *
     * @param use the use to set.
     */
    public void setUse(ContactUse use) {
        this.use = use;
    }

    /**
     * A method returning the URL scheme of the contact.
     *
     * @return the URL scheme of the contact.
     */
    public URLScheme getPrefix() {
        return prefix;
    }

    /**
     * A method setting the URL scheme of the contact.
     *
     * @param prefix the URL scheme to set.
     */
    public void setPrefix(URLScheme prefix) {
        this.prefix = prefix;
    }

    /**
     * A method returning the call value of the contact.
     *
     * @return the call value of the contact.
     */
    public String getValue() {
        return value;
    }

    /**
     * A method setting the value of the contact.
     *
     * @param value the value to set.
     */
    public void setValue(String value) {
        this.value = value;
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

        // Checking if the value of the contact is provided
        if (value != null) {
            // Opening a telecom element
            context.append("<telecom");

            // Checking if the use is provided
            if (use != null) {
                // Setting the attribute use
                context.append(" ")
                        .append("use=\"")
                        .append(use)
                        .append("\"");
            } else {
                // Setting the default use
                context.append(" ")
                        .append("use=\"")
                        .append(ContactUse.H)
                        .append("\"");
            }

            // Checking if the prefix is provided
            if (prefix != null) {
                // Setting the value attribute
                context.append(" ")
                        .append("value=\"")
                        .append(prefix)
                        .append(value)
                        .append("\"");
            } else {
                // Setting the value attribute with the default prefix
                context.append(" ")
                        .append("value=\"")
                        .append(URLScheme.TEL)
                        .append(value)
                        .append("\"");
            }

            // Closing the telecom element
            context.append("/>");
        } else {
            // Adding a null flavored telecom
            context.append("<telecom nullFlavor=\"NI\"/>");
        }

        return context.toString();
    }
}
