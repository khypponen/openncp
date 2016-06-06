package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating a generic code.
 *
 * @author Akis Papadopoulos
 *
 */
public class GenericCode extends Code {

    // Optional narrative text reference
    private String reference;

    /**
     * A constructor initializing a generic code.
     */
    public GenericCode() {
        // Calling the constructor of the super class
        super();
    }

    /**
     * A constructor initializing a generic code.
     *
     * @param code the code.
     * @param display the display name of code.
     * @param codeSystem the code system.
     * @param codeSysemName the code system name.
     */
    public GenericCode(String code, String display, String codeSystem, String codeSystemName) {
        // Calling the constructor of the super class
        super(code, display, codeSystem, codeSystemName);
    }

    /**
     * A method returning the reference to a narrative text.
     *
     * @return a narrative text reference.
     */
    public String getReference() {
        return reference;
    }

    /**
     * A method setting the narrative text reference.
     *
     * @param reference the reference to set.
     */
    public void setReference(String reference) {
        this.reference = reference;
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

        // Checking for a null flavored generic code
        if (code == null && display == null && codeSystem == null && codeSystemName == null) {
            // Opening a null flavored generic code element
            context.append("<code nullFlavor=\"UNK\"");

            // Checking if the reference to a narrative text is provided
            if (reference != null) {
                // Closing the opening tag of the code element
                context.append(">");

                // Opening an original text element
                context.append("<originalText mediaType=\"text/xml\">");

                // Opening the reference element
                context.append("<reference");

                // Setting the value of the reference
                context.append(" ")
                        .append("value=\"#")
                        .append(reference)
                        .append("\"/>");

                // Closing the original text element
                context.append("</originalText>");

                // Closing the code element
                context.append("</code>");
            } else {
                // Closing the code element as body-less
                context.append("/>");
            }
        } else {
            // Opening the code element
            context.append("<code");

            // Checking if the code is provided
            if (code != null) {
                // Setting the code attribute
                context.append(" ")
                        .append("code=\"")
                        .append(code)
                        .append("\"");
            }

            // Checking if the optional code display name is provided
            if (display != null) {
                // Setting the code display name attribute
                context.append(" ")
                        .append("displayName=\"")
                        .append(display)
                        .append("\"");
            }

            // Checking if the code system is provided
            if (codeSystem != null) {
                // Setting the code system attribute
                context.append(" ")
                        .append("codeSystem=\"")
                        .append(codeSystem)
                        .append("\"");
            }

            // Checking if the code system name is provided
            if (codeSystemName != null) {
                // Setting the code system name attribute
                context.append(" ")
                        .append("codeSystemName=\"")
                        .append(codeSystemName)
                        .append("\"");
            }

            // Checking if the reference to a narrative text is provided
            if (reference != null) {
                // Closing the opening tag of the code element
                context.append(">");

                // Opening an original text element
                context.append("<originalText mediaType=\"text/xml\">");

                // Opening the reference element
                context.append("<reference");

                // Setting the value of the reference
                context.append(" ")
                        .append("value=\"#")
                        .append(reference)
                        .append("\"/>");

                // Closing the original text element
                context.append("</originalText>");

                // Closing the code element
                context.append("</code>");
            } else {
                // Closing the code element as body-less
                context.append("/>");
            }
        }

        return context.toString();
    }
}
