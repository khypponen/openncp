package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating a disease element.
 *
 * @author Akis Papadopoulos
 *
 */
public class Observation extends Code {

    /**
     * A constructor initializing an observation element.
     */
    public Observation() {
        // Calling the constructor of the super class
        super();
    }

    /**
     * A constructor initializing an observation element.
     *
     * @param code the code of the observation.
     * @param display the display name of the observation.
     * @param codeSystem the code system.
     * @param codeSysemName the code system name.
     */
    public Observation(String code, String display, String codeSystem, String codeSystemName) {
        // Calling the constructor of the super class
        super(code, display, codeSystem, codeSystemName);
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

        // Checking for a null flavored disease
        if (code == null && display == null && codeSystem == null && codeSystemName == null) {
            // Adding a null flavored disease
            context.append("<value nullFlavor=\"UNK\" xsi:type=\"CD\"/>");
        } else {
            // Opening the value element
            context.append("<value");

            // Checking if the code is provided
            if (code != null) {
                // Setting the code attribute
                context.append(" ")
                        .append("code=\"")
                        .append(code)
                        .append("\"");
            }

            // Checking if the optional display name is provided
            if (display != null) {
                // Setting the display name attribute
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

            // Setting the xsi:type attribute
            context.append(" ");
            context.append("xsi:type=\"CD\"");

            // Closing the value element
            context.append("/>");
        }

        return context.toString();
    }
}
