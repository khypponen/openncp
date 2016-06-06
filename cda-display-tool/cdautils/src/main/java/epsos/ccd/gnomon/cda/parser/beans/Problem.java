package epsos.ccd.gnomon.cda.parser.beans;

import java.util.HashSet;
import java.util.Set;

/**
 * A class encapsulating a medical problem entry.
 *
 * @author Akis Papadopoulos
 *
 */
public class Problem extends MedicalEntry {

    // Static medical problem entries counter
    private static int UUID = 1;
    // Problem status
    private String status;
    // Set of observation related to the problem
    private Set<Observation> observations;

    /**
     * A constructor initializing a medical problem entry.
     */
    public Problem() {
        // Calling the constructor of the super class
        super();

        // Setting the problem UUID number
        this.uuid = "problem." + String.valueOf(UUID++);

        // Setting a null status of the medical problem
        this.status = null;

        // Setting a null set of observations related to the problem
        this.observations = new HashSet<Observation>();
    }

    /**
     * A constructor initializing a medical problem entry.
     *
     * @param code the code of the allergy.
     * @param effectiveTime the effective time of the problem.
     * @param status the status of the medical problem.
     */
    public Problem(Code code, Time effectiveTime, String status) {
        // Calling the constructor of the super class
        super(code, effectiveTime);

        // Setting the problem UUID number
        this.uuid = "problem." + String.valueOf(UUID++);

        // Setting the status of the medical problem
        this.status = status;

        // Creating an empty set of observations related to the problem
        this.observations = new HashSet<Observation>();
    }

    /**
     * A method returning the medical problem status.
     *
     * @return the medical problem status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * A method setting the status of the problem.
     *
     * @param status the status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * A method returning the set of observations related to the problem.
     *
     * @return the observations related of the problem.
     */
    public Set<Observation> getObservations() {
        return observations;
    }

    /**
     * A method adding an observation related to the problem.
     *
     * @param observation the observation to add.
     */
    public void addObservation(Observation observation) {
        this.observations.add(observation);
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
        context.append("<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5.2\"/>");

        // Opening the id element
        context.append("<id");

        // Checking if the uuid is provided
        if (uuid != null) {
            // Setting the extension attribute of the problem
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
            context.append("<statusCode");

            // Adding the code attribute
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

        // Current observation id
        int observationId = 1;

        // Iterating through the list of observations
        for (Observation observation : observations) {
            // Opening the entry relationship element
            context.append("<entryRelationship inversionInd=\"false\" typeCode=\"SUBJ\">");

            // Opening the observation element
            context.append("<observation classCode=\"OBS\" moodCode=\"EVN\">");

            // Adding the template roots
            context.append("<templateId root=\"2.16.840.1.113883.10.20.1.28\"/>");
            context.append("<templateId root=\"1.3.6.1.4.1.19376.1.5.3.1.4.5\"/>");

            // Opening the id element
            context.append("<id");

            // Checking if the uuid is provided
            if (uuid != null) {
                // Setting the extension attribute of the observation
                context.append(" ")
                        .append("extension=\"")
                        .append(uuid).append(".obs.").append(observationId++)
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

            // Checking if the code is provided
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

            // Closing the reference opening element
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

            // Checking if the observation is provided
            if (observation != null) {
                // Adding the observation element
                context.append(observation);
            }

            // Closing the observation element
            context.append("</observation>");

            // Closing the entry relationship element
            context.append("</entryRelationship>");
        }

        // Closing the act element
        context.append("</act>");

        // Closing the entry element
        context.append("</entry>");

        return context.toString();
    }
}
