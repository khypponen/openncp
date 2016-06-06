package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating an alert agent.
 *
 * @author Akis Papadopoulos
 *
 */
public class Agent {

    // Agent code
    private Code code;

    /**
     * A constructor initializing an alert agent.
     */
    public Agent() {
        // Setting a null code of the agent
        this.code = null;
    }

    /**
     * A constructor initializing an alert agent.
     *
     * @param code the code of the agent.
     */
    public Agent(Code code) {
        // Setting the code of the agent
        this.code = code;
    }

    /**
     * A method returning the code of the agent.
     *
     * @return the code of the agent.
     */
    public Code getCode() {
        return code;
    }

    /**
     * A method setting the agent code.
     *
     * @param code the code to set.
     */
    public void setCode(Code code) {
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

        // Opening the participant element
        context.append("<participant typeCode=\"CSM\">");

        // Opening the participant role element
        context.append("<participantRole classCode=\"MANU\">");

        // Opening the playing entity element
        context.append("<playingEntity classCode=\"MMAT\">");

        // Checking if the agent code is provided
        if (code != null) {
            // Adding the code element
            context.append(code);
        }

        // Closing the playing entity element
        context.append("</playingEntity>");

        // Closing the participant role element
        context.append("</participantRole>");

        // Closing the participant element
        context.append("</participant>");

        return context.toString();
    }
}
