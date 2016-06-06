package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating a surgical procedure entry.
 *
 * @author Akis Papadopoulos
 *
 */
public class Procedure extends MedicalEntry {

    // Static procedure entries counter
    private static int UUID = 1;
    // Mood of the procedure
    private String mood;

    /**
     * A constructor initializing a surgical procedure entry.
     */
    public Procedure() {
        // Calling the constructor of the super class
        super();

        // Setting the procedure UUID number
        this.uuid = "proc." + String.valueOf(UUID++);

        // Setting a null mood of the surgical procedure
        this.mood = null;
    }

    /**
     * A constructor initializing a surgical procedure entry.
     *
     * @param code the code of the procedure.
     * @param effectiveTime the effective time of the procedure.
     * @param mood the mood of the procedure.
     */
    public Procedure(Code code, Time effectiveTime, String mood) {
        // Calling the constructor of the super class
        super(code, effectiveTime);

        // Setting the procedure UUID number
        this.uuid = "proc." + String.valueOf(UUID++);

        // Setting the mood of the surgical procedure
        this.mood = mood;
    }

    /**
     * A method returning the mood of the procedure.
     *
     * @return the mood of the procedure.
     */
    public String getMood() {
        return mood;
    }

    /**
     * A method setting the mood of the procedure.
     *
     * @param mood the mood to set.
     */
    public void setMood(String mood) {
        this.mood = mood;
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

        // Checking for a null flavored procedure entry
        if (code == null && description == null && effectiveTime == null && mood == null) {
            // OPZ
            // Opening the entry element
            context.append("<entry typeCode=\"DRIV\">");

            // Opening a null flavored procedure element
            context.append("<procedure classCode=\"PROC\" moodCode=\"EVN\" nullFlavor=\"NA\">");

            // Adding the template root elements
            context.append("<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.19\"/>");
            context.append("<templateId root=\"2.16.840.1.113883.10.20.1.29\"/>");

            // Adding a null flavored id element
            context.append("<id nullFlavor=\"NI\"/>");

            // Adding a null flavored code element
            context.append("<code nullFlavor=\"NI\"/>");

            // Opening the text element
            context.append("<text>");

            // Adding the reference element
            context.append("<reference");

            // Checking if the uuid is provided
            if (uuid != null) {
                // Setting the value attribute
                context.append(" ")
                        .append("value=\"#")
                        .append(uuid)
                        .append("\"");
            }

            // Closing the reference element
            context.append("/>");

            // Closing the text element
            context.append("</text>");

            // Adding the status code element
            context.append("<statusCode nullFlavor=\"NI\"/>");

            // Closing the procedure element
            context.append("</procedure>");

            // Closing the entry element
            context.append("</entry>");
            // OPZ
        } else {
            // Opening the entry element
            context.append("<entry typeCode=\"DRIV\">");

            // Opening the procedure element
            context.append("<procedure classCode=\"PROC\"");

            // Checking if the mood is provided
            if (mood != null) {
                // Setting the mood attribute
                context.append(" ")
                        .append("moodCode=\"")
                        .append("\"")
                        .append(mood);
            }

            // Closing the opening procedure element
            context.append(">");

            // Adding the template root elements
            context.append("<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.19\"/>");
            context.append("<templateId root=\"2.16.840.1.113883.10.20.1.29\"/>");

            // Opening the id element
            context.append("<id");

            // Checking if the uuid is provided
            if (uuid != null) {
                // Setting the extension attribute of the allergy
                context.append(" ")
                        .append("extension=\"")
                        .append(uuid)
                        .append("\"");
            }

            // Checking if the root is provided
            if (root != null) {
                // Setting the root attribute
                context.append(" ")
                        .append("root=\"")
                        .append(root)
                        .append("\"");
            }

            // Closing the id element
            context.append("/>");

            // Checking if the code is provided
            if (code != null) {
                // Setting to the code a reference to the narrative text
                ((GenericCode) code).setReference(uuid);

                // Adding the code element
                context.append(code);
            }

            // Opening the text element
            context.append("<text>");

            // Adding the reference element
            context.append("<reference");

            // Checking if the uuid is provided
            if (uuid != null) {
                // Setting the value attribute
                context.append(" ")
                        .append("value=\"#")
                        .append(uuid)
                        .append("\"");
            }

            // Closing the opening reference element
            context.append("/>");

            // Closing the text element
            context.append("</text>");

            // Adding the status code element
            context.append("<statusCode code=\"completed\"/>");

            // Checking if the effective time is provided
            if (effectiveTime != null) {
                // Adding the effective time element
                context.append(effectiveTime);
            }

            // Closing the procedure element
            context.append("</procedure>");

            // Closing the entry element
            context.append("</entry>");
        }

        return context.toString();
    }
}
