package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating a medication entry.
 *
 * @author Akis Papadopoulos
 *
 */
public class Medication extends MedicalEntry {

    // Static medication entries counter
    private static int UUID = 1;
    // Frequency of the administration
    private String frequency;
    // Route of the administration
    private Route route;
    // Dose quantity of the medication
    private Dose dose;
    // Mood of the medication
    private String mood;
    // Status of the medication
    private String status;

    /**
     * A constructor initializing a medication.
     */
    public Medication() {
        // Calling the constructor of the super class
        super();

        // Setting the medication UUID number
        this.uuid = "med." + String.valueOf(UUID++);

        // Setting a null frequency of the medication
        this.frequency = null;

        // Setting a null route of the administration
        this.route = null;

        // Setting a null dose of the medication
        this.dose = null;

        // Setting a null mood of the medication
        this.mood = null;

        // Setting a null status of the medication
        this.status = null;
    }

    /**
     * A constructor initializing a medication.
     *
     * @param code the code of the medication product.
     * @param effectiveTime the effective time of the medication.
     */
    public Medication(Code code, Time effectiveTime) {
        // Calling the constructor of the super class
        super(code, effectiveTime);

        // Setting the medication UUID number
        this.uuid = "med." + String.valueOf(UUID++);

        // Setting a null frequency of the medication
        this.frequency = null;

        // Setting a null route of the administration
        this.route = null;

        // Setting a null dose of the medication
        this.dose = null;
    }

    /**
     * A method returning the frequency of the medication.
     *
     * @return the frequency of the medication.
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * A method setting the frequency of the medication.
     *
     * @param frequency the frequency to set.
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * A method returning the route of the administration.
     *
     * @return the route of the administration.
     */
    public Route getRoute() {
        return route;
    }

    /**
     * A method setting the route of the medication.
     *
     * @param route the route to set.
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * A method returning the dose of the medication.
     *
     * @return the dose of the medication.
     */
    public Dose getDose() {
        return dose;
    }

    /**
     * A method setting the dose of the medication.
     *
     * @param dose the dose to set.
     */
    public void setDose(Dose dose) {
        this.dose = dose;
    }

    /**
     * A method returning the mood of the medication.
     *
     * @return the mood of the medication.
     */
    public String getMood() {
        return mood;
    }

    /**
     * A method settings the mood of the medication.
     *
     * @param mood the mood to set.
     */
    public void setMood(String mood) {
        this.mood = mood;
    }

    /**
     * A method returning the status of the medication.
     *
     * @return the status of the medication.
     */
    public String getStatus() {
        return status;
    }

    /**
     * A method setting the status of the medication.
     *
     * @param status the status to set.
     */
    public void setStatus(String status) {
        this.status = status;
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

        // Checking for a null flavored medication entry
        if (code == null && description == null && effectiveTime == null && frequency == null && route == null && dose == null && mood == null && status == null) {
            // OPZ
            // Opening the entry element
            context.append("<entry typeCode=\"DRIV\">");

            // Opening the substance administration element
            context.append("<substanceAdministration classCode=\"SBADM\" moodCode=\"INT\">");

            // Adding the template root elements
            context.append("<templateId root=\"1.3.6.1.4.1.12559.11.10.1.3.1.3.4\"/>");
            context.append("<templateId root=\"2.16.840.1.113883.10.20.1.24\"/>");
            context.append("<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7\"/>");
            context.append("<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7.1\"/>");

            // Adding a null flavored id element
            context.append("<id nullFlavor=\"NI\"/>");

            // Adding a no medication code
            context.append("<code code=\"182904002\" displayName=\"Drug treatment unknown\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED CT\"/>");

            // Opening the status code element
            context.append("<statusCode code=\"completed\"/>");

            // Opening a null flavored onset time element
            context.append("<effectiveTime xsi:type=\"IVL_TS\">");

            // Adding a null flavored low element
            context.append("<low nullFlavor=\"UNK\"/>");

            // Adding a null flavored high element
            context.append("<high nullFlavor=\"UNK\"/>");

            // Closing the onset time element
            context.append("</effectiveTime>");

            // Adding a null flavored frequency effective time element
            context.append("<effectiveTime nullFlavor=\"NA\" operator=\"A\"/>");

            // Adding a null flavored dose element
            context.append("<doseQuantity nullFlavor=\"NA\"/>");

            // Adding a null flavored active ingredient code
            context.append("<consumable>");
            context.append("<manufacturedProduct classCode=\"MANU\" xmlns:epsos=\"urn:epsos-org:ep:medication\">");
            context.append("<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.7.2\"/>");
            context.append("<templateId root=\"2.16.840.1.113883.10.20.1.53\"/>");
            context.append("<templateId root=\"1.3.6.1.4.1.12559.11.10.1.3.1.3.1\"/>");
            context.append("<manufacturedMaterial>");
            context.append("<code nullFlavor=\"NA\">");
            context.append("<originalText>");
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

            context.append("</originalText>");
            context.append("</code>");
            context.append("<epsos:ingredient classCode=\"ACTI\">");
            context.append("<epsos:quantity>");
            context.append("<epsos:numerator nullFlavor=\"NA\" xsi:type=\"epsos:PQ\"/>");
            context.append("<epsos:denominator nullFlavor=\"NA\" xsi:type=\"epsos:PQ\"/>");
            context.append("</epsos:quantity>");
            context.append("<epsos:ingredient classCode=\"MMAT\" determinerCode=\"KIND\">");
            context.append("<epsos:code nullFlavor=\"NA\"/>");
            context.append("</epsos:ingredient>");
            context.append("</epsos:ingredient>");
            context.append("</manufacturedMaterial>");
            context.append("</manufacturedProduct>");
            context.append("</consumable>");

            // Closing the substance administration element
            context.append("</substanceAdministration>");

            // Closing the entry element
            context.append("</entry>");
            // OPZ
        } else {
            // Opening the entry element
            context.append("<entry typeCode=\"DRIV\">");

            // Opening the substance administration element
            context.append("<substanceAdministration classCode=\"SBADM\"");

            // Checking if the mood is provided
            if (mood != null) {
                // Setting the mood of the medication
                context.append(" ")
                        .append("moodCode=\"")
                        .append(mood);
            }

            // Closing the opening substance administration element
            context.append("\">");

            // Adding the template root elements
            context.append("<templateId root=\"1.3.6.1.4.1.12559.11.10.1.3.1.3.4\"/>");
            context.append("<templateId root=\"2.16.840.1.113883.10.20.1.24\"/>");

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

            // Checking if the status is provided
            if (status != null) {
                // Opening the status code element
                context.append("<statusCode");

                // Setting the status code of the medication
                context.append(" ")
                        .append("code=\"")
                        .append(status)
                        .append("\"");

                // Closing the status code element
                context.append("/>");
            }

            // Checking if the effective time is provided
            if (effectiveTime != null) {
                // Adding the effective time element
                context.append(effectiveTime);
            }

            // Opening a null flavored frequency effective time element
            context.append("<effectiveTime nullFlavor=\"UNK\" xsi:type=\"PIVL_TS\">");

            // Adding a null flavored period element
            context.append("<period nullFlavor=\"UNK\"/>");

            // Closing the frequency effective time element
            context.append("</effectiveTime>");

            // Checking if the route is provided, otherwise ignore it
            if (route != null) {
                // Adding the route element
                context.append(route);
            }

            // Checking if the dose is provided
            if (dose != null) {
                // Adding the dose quantity element
                context.append(dose);
            }

            // Checking if the active ingredient code is provided
            if (code != null) {
                // Adding the epsos ingredient element
                context.append(code);
            }

            // Closing the substance administration element
            context.append("</substanceAdministration>");

            // Closing the entry element
            context.append("</entry>");
        }

        return context.toString();
    }
}
