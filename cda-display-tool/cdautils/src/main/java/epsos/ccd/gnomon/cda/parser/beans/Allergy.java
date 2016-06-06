package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating an allergy medical entry.
 *
 * @author Akis Papadopoulos
 *
 */
public class Allergy extends MedicalEntry {

    // Static allergy entries counter
    private static int UUID = 1;
    // Agent of the allergy
    private Agent agent;
    // Allergy status
    private String status;

    /**
     * A constructor initializing an allergy medical entry.
     */
    public Allergy(String root) {
        // Calling the constructor of the super class
        super();

        // Setting the allergy UUID number
        this.uuid = "allergy." + String.valueOf(UUID++);

        // Setting a null agent of the alert
        this.agent = null;

        // Setting a null status of the allergy
        this.status = null;
    }

    /**
     * A constructor initializing an allergy medical entry.
     *
     * @param code the code of the allergy.
     * @param effectiveTime the effective time of the allergy.
     * @param agent the agent causing the allergy.
     * @param status the status of the allergy.
     */
    public Allergy(Code code, Time effectiveTime, Agent agent, String status) {
        // Calling the constructor of the super class
        super(code, effectiveTime);

        // Setting the allergy UUID number
        this.uuid = "allergy." + String.valueOf(UUID++);

        // Setting the agent of the alert
        this.agent = agent;

        // Setting the status of the allergy
        this.status = status;
    }

    /**
     * A method returning the agent of the allergy.
     *
     * @return the agent of the allergy.
     */
    public Agent getAgent() {
        return agent;
    }

    /**
     * A method setting the agent of the allergy.
     *
     * @param agent the agent to set.
     */
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    /**
     * A method returning the status of the allergy.
     *
     * @return the status of the allergy.
     */
    public String getStatus() {
        return status;
    }

    /**
     * A method setting the status of the allergy.
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

        // Opening the entry element
        context.append("<entry typeCode=\"DRIV\">");

        // Opening the act element
        context.append("<act classCode=\"ACT\" moodCode=\"EVN\">");

        // Adding the template root elements
        context.append("<templateId root=\"2.16.840.1.113883.10.20.1.27\"/>");
        context.append("<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.1\"/>");
        context.append("<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.3\"/>");

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

        // Adding a null flavored code element
        context.append("<code nullFlavor=\"NA\"/>");


        // Checking if the status is provided
        if (status != null) {
            // Opening the status code element
            context.append("<statusCode ");

            // Setting the attribute code of the status
            context.append(" ")
                    .append(" code=\"")
                    .append(status)
                    .append("\"");

            // Closing the status element
            context.append("/>");
        }

        // Checking if the effective time is provided
        if (effectiveTime != null) {
            // Adding the effective time element
            context.append(effectiveTime);
        }

        // Opening the entry relationship element
        context.append("<entryRelationship inversionInd=\"false\" typeCode=\"SUBJ\">");

        // Opening the observation element
        context.append("<observation classCode=\"OBS\" moodCode=\"EVN\">");

        // Adding the template roots
        context.append("<templateId root=\"2.16.840.1.113883.10.20.1.18\"/>");
        context.append("<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.6\"/>");
        context.append("<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5\"/>");
        context.append("<templateId root=\"2.16.840.1.113883.10.20.1.28\"/>");

        // Opening an observation id element
        context.append("<id");

        // Checking if the uuid is provided
        if (uuid != null) {
            // Setting the extension attribute of the observation
            context.append(" ")
                    .append("extension=\"")
                    .append(uuid).append(".obs.1")
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

        // Checking if medical entry code is provided
        if (code != null) {
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

        // Closing the reference element
        context.append("/>");

        // Closing the text element
        context.append("</text>");

        // Adding the status code element
        context.append("<statusCode code=\"completed\"/>");

        // Checking if the effectiveTime time is provided
        if (effectiveTime != null) {
            // Adding the effective time element
            context.append(effectiveTime);
        }

        // OPZ
        // Checking for a non null flavored allergy entry
        if (code == null && effectiveTime == null && agent == null) {
            // Adding a unknown allergy code
            context.append("<value code=\"160244002\" displayName=\"No known allergies\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED CT\" xsi:type=\"CD\"/>");
        } else {
            // Adding a null flavor value element
            context.append("<value nullFlavor=\"NI\" xsi:type=\"CD\"/>");
        }
        // OPZ

        // Checking if the agent provided
        if (agent != null) {
            // Setting a reference to the allergy in the narrative text
            ((GenericCode) agent.getCode()).setReference(uuid);

            // Adding the agent element
            context.append(agent);
        }

        // Closing the observation element
        context.append("</observation>");

        // Closing the entry relationship element
        context.append("</entryRelationship>");

        // Closing the act element
        context.append("</act>");

        // Closing the entry element
        context.append("</entry>");

        return context.toString();
    }
}
