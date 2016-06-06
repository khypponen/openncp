package epsos.ccd.gnomon.cda.parser.beans;

import epsos.ccd.gnomon.cda.parser.enums.RoleClass;

/**
 * An abstract class encapsulating a generic participant of a patient summary
 * document.
 *
 * @author Akis Papadopoulos
 *
 */
public abstract class Participant {

    // Participant code type
    protected String typeCode;
    // Participant role class code
    protected RoleClass role;
    // Participant template root
    protected String root;

    /**
     * A constructor initializing the role class of the participant.
     */
    public Participant() {
        // Setting a null type code
        this.typeCode = null;

        // Setting a null class role of the participant
        this.role = null;

        // Setting a null template root
        this.root = null;
    }

    /**
     * A constructor initializing the role class of the participant.
     *
     * @param role the class role code of the participant.
     */
    public Participant(RoleClass role) {
        // Setting a null type code
        this.typeCode = null;

        // Setting the class role of the participant
        this.role = role;

        // Setting a null template root
        this.root = null;
    }

    /**
     * A method returning the type code of the participant.
     *
     * @return the type code of the participant.
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * A method setting the type code of the participant.
     *
     * @param typeCode the type code to set.
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * A method returning the class role of the participant.
     *
     * @return the class role code of the participant.
     */
    public RoleClass getRole() {
        return role;
    }

    /**
     * A method setting the role class of the participant.
     *
     * @param role the role to set.
     */
    public void setRole(RoleClass role) {
        this.role = role;
    }

    /**
     * A method returning the template root.
     *
     * @return the root of the template.
     */
    public String getRoot() {
        return root;
    }

    /**
     * A method setting the template root.
     *
     * @param root the template root to set.
     */
    public void setRoot(String root) {
        this.root = root;
    }
}
