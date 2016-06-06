package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating an active ingredient ATC code.
 *
 * @author Akis Papadopoulos
 *
 */
public class ActiveIngredient extends Code {

    /**
     * A constructor initializing an active ingredient.
     */
    public ActiveIngredient() {
        // Calling the constructor of the super class
        super();
    }

    /**
     * A constructor initializing an active ingredient.
     *
     * @param code the code.
     * @param display the display name of code.
     * @param codeSystem the code system.
     * @param codeSysemName the code system name.
     */
    public ActiveIngredient(String code, String display, String codeSystem, String codeSystemName) {
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

        // Opening a consumable element
        context.append("<consumable>");

        // opening the manufactured product element
        context.append("<manufacturedProduct>");

        // Adding the template root
        context.append("<templateId root=\"2.16.840.1.113883.10.20.1.53\"/>");

        // Opening the manufactured material element
        context.append("<manufacturedMaterial>");

        // Adding a null flavored generic code element
        context.append(new GenericCode());

        // Opening an epsos ingredient element
        context.append("<epsos:ingredient classCode=\"ACTI\">");

        // Opening an epsos quantity element
        context.append("<epsos:quantity>");

        // Adding a null flavored epsos numerator element
        context.append("<epsos:numerator nullFlavor=\"NI\" xsi:type=\"epsos:PQ\"/>");

        // Adding a null flavored epsos denominator element
        context.append("<epsos:denominator nullFlavor=\"NI\" xsi:type=\"epsos:PQ\"/>");

        // Closing an epsos quantity element
        context.append("</epsos:quantity>");

        // Adding an epsos active ingredient element
        context.append("<epsos:ingredient classCode=\"MMAT\" determinerCode=\"KIND\">");

        // Checking for a null flavored active ingredient code
        if (code == null && display == null && codeSystem == null && codeSystemName == null) {
            // Adding a null flavored code element
            context.append("<epsos:code nullFlavor=\"UNK\"/>");
        } else {
            // Opening an epsos code element
            context.append("<epsos:code");

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

            // Closing the epsos code element
            context.append("/>");
        }

        // Closing the epsos active ingredient
        context.append("</epsos:ingredient>");

        // Closing the epsos ingredient element
        context.append("</epsos:ingredient>");

        // Closing the manufactured material element
        context.append("</manufacturedMaterial>");

        // Closing the manufactured product element
        context.append("</manufacturedProduct>");

        // Closing the consumable element
        context.append("</consumable>");

        return context.toString();
    }
}
