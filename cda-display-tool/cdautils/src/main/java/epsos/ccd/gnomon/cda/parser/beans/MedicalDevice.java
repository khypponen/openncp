package epsos.ccd.gnomon.cda.parser.beans;

import java.text.SimpleDateFormat;

/**
 * A class encapsulating a medical device entry.
 *
 * @author Akis Papadopoulos
 *
 */
public class MedicalDevice extends MedicalEntry {

    // Static medical device entries counter
    private static int UUID = 1;
    // Static date and time formatter
    private static SimpleDateFormat dater = new SimpleDateFormat("yyyyMMdd");

    /**
     * A constructor initializing a medical device.
     */
    public MedicalDevice() {
        // Calling the constructor of the super class
        super();

        // Setting a medical device UUID number
        this.uuid = "dev." + String.valueOf(UUID++);
    }

    /**
     * A constructor initializing a medical device entry.
     *
     * @param code the code of the medical device.
     * @param effectiveTime the effective time of the medical device.
     */
    public MedicalDevice(Code code, Time effectiveTime) {
        // Calling the constructor of the super class
        super(code, effectiveTime);

        // Setting the medical device UUID number
        this.uuid = "dev." + String.valueOf(UUID++);
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

        // Checking for a null flavored medical device entry
        if (code == null && description == null && effectiveTime == null) {
            // OPZ
            // Opening the entry element
            context.append("<entry typeCode=\"DRIV\">");

            // Opening a null flavored supply element
            context.append("<supply classCode=\"SPLY\" moodCode=\"EVN\" nullFlavor=\"NI\">");

            // Adding the template root elements
            context.append("<templateId root=\"1.3.6.1.4.1.12559.11.10.1.3.1.3.5\"/>");

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

            // Adding a null flavor effective time element
            context.append("<effectiveTime nullFlavor=\"NI\"/>");

            // Opening the participant element
            context.append("<participant typeCode=\"DEV\">");

            // Opening the participant role element
            context.append("<participantRole classCode=\"MANU\">");

            // Opening the playing device element
            context.append("<playingDevice classCode=\"DEV\" determinerCode=\"INSTANCE\">");

            // Adding a null flavored code
            context.append("<code nullFlavor=\"NI\"/>");

            // Closing the playing device element
            context.append("</playingDevice>");

            // Closing the participant role element
            context.append("</participantRole>");

            // Closing the participant element
            context.append("</participant>");

            // Closing the supply element
            context.append("</supply>");

            // Closing the entry element
            context.append("</entry>");
            // OPZ
        } else {
            // Opening the entry element
            context.append("<entry typeCode=\"DRIV\">");

            // Opening the supply element
            context.append("<supply classCode=\"SPLY\" moodCode=\"EVN\">");

            // Adding the template root elements
            context.append("<templateId root=\"1.3.6.1.4.1.12559.11.10.1.3.1.3.5\"/>");

            // Opening the id element
            context.append("<id");

            // Checking if the uuid is provided
            if (uuid != null) {
                // Setting the extension attribute of the device
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

            // closing the id element
            context.append("/>");

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

            // Checking if the effective time is provided
            if (effectiveTime != null) {
                // Adding the effective time element
                context.append("<effectiveTime")
                        .append(" ")
                        .append("value=\"")
                        .append(dater.format(effectiveTime.getLow()))
                        .append("\"/>");
            }

            // Opening the participant element
            context.append("<participant typeCode=\"DEV\">");

            // Opening the participant role element
            context.append("<participantRole classCode=\"MANU\">");

            // Opening the playing device element
            context.append("<playingDevice classCode=\"DEV\" determinerCode=\"INSTANCE\">");

            // Checking if the code is provided
            if (code != null) {
                // Setting to the code a reference to the narrative text
                ((GenericCode) code).setReference(uuid);

                // Adding the code element
                context.append(code);
            }

            // Closing the playing device element
            context.append("</playingDevice>");

            // Closing the participant role element
            context.append("</participantRole>");

            // Closing the participant element
            context.append("</participant>");

            // Closing the supply element
            context.append("</supply>");

            // Closing the entry element
            context.append("</entry>");
        }

        return context.toString();
    }
}
