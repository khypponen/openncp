package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating a medication administration route.
 *
 * @author Akis Papadopoulos
 *
 */
public class Route extends Code {

    /**
     * A constructor initializing an administration route.
     */
    public Route() {
        // Calling the constructor of the super class
        super();
    }

    /**
     * A constructor initializing an administration route.
     *
     * @param code the code of the route.
     * @param display the display name of the route.
     * @param codeSystem the code system.
     * @param codeSysemName the code system name.
     */
    public Route(String code, String display, String codeSystem, String codeSystemName) {
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

        // Opening the route element
        context.append("<route");

        // Checking if the code is provided
        if (code != null) {
            // Setting the code attribute
            context.append(" ")
                    .append("code=\"")
                    .append(code)
                    .append("\"");
        }

        // Checking if the optional route display name is provided
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

        // Closing the route element
        context.append("/>");

        return context.toString();
    }
}
