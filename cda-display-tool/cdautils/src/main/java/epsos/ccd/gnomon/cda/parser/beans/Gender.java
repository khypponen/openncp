package epsos.ccd.gnomon.cda.parser.beans;

import epsos.ccd.gnomon.cda.parser.enums.Sex;

/**
 * A class encapsulating a administrative gender.
 *
 * @author Akis Papadopoulos
 *
 */
public class Gender {

    // Gender sex code
    private Sex code;

    /**
     * A constructor initializing a gender.
     */
    public Gender() {
        // Setting a null gender sex code
        this.code = null;
    }

    /**
     * A constructor initializing a gender.
     *
     * @param code the gender sex code.
     */
    public Gender(Sex code) {
        // Setting the gender sex code
        this.code = code;
    }

    /**
     * A constructor initializing a gender.
     *
     * @param code the administrative gender code.
     */
    public Gender(String code) {
        // Checking for a valid gender code
        if (code.equalsIgnoreCase(Sex.Male.toString())) {
            // Setting the administrative code
            this.code = Sex.Male;
        } else if (code.equalsIgnoreCase(Sex.Female.toString())) {
            // Setting the administrative code
            this.code = Sex.Female;
        } else {
            // Setting the administrative code
            this.code = Sex.Undifferentiated;
        }
    }

    /**
     * A method returning the gender sex code.
     *
     * @return the gender sex code.
     */
    public Sex getCode() {
        return code;
    }

    /**
     * A method setting the gender sex code.
     *
     * @param code the gender sex code to set.
     */
    public void setCode(Sex code) {
        this.code = code;
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

        // Checking if the gender sex code is provided
        if (code != null) {
            // Opening an administrative gender code element
            context.append("<administrativeGenderCode");

            // Setting the attribute code
            context.append(" ")
                    .append("code=\"")
                    .append(code)
                    .append("\"");

            // Setting the attribute display name
            context.append(" ")
                    .append("displayName=\"")
                    .append(code.getDisplay())
                    .append("\"");

            // Setting the attribute code system
            context.append(" ")
                    .append("codeSystem=\"")
                    .append(Sex.CODE_SYSTEM)
                    .append("\"");

            // Setting the attribute code system name
            context.append(" ")
                    .append("codeSystemName=\"")
                    .append(Sex.CODE_SYSTEM_NAME)
                    .append("\"");

            // Setting the attribute code system version
            context.append(" ")
                    .append("codeSystemVersion=\"")
                    .append(Sex.CODE_SYSTEM_VERSION)
                    .append("\"");

            // Closing the administrative gender code element
            context.append("/>");
        } else {
            // Adding a null flavored administrative gender code element
            context.append("<administrativeGenderCode nullFlavor=\"UNK\"/>");
        }

        return context.toString();
    }
}
