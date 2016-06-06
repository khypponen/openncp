package epsos.ccd.gnomon.cda.parser.enums;

/**
 * A basic enum coding the participant role class codes.
 * 
 * @author Akis Papadopoulos
 * 
 */
public enum RoleClass {
    
    // Role class code and display name
    ECON("ECON", "emergency contact"),
    NOK("NOK", "next of kin"),
    PRS("PRS", "unknown"),
    CAREGIVER("CAREGIVER", "unknown");

    // Use code
    private String code;
    // Use display name
    private String display;

    /**
     * A basic constructor initializing the class role.
     * 
     * @param code the code of the class role.
     * @param display the display name of the class role.
     */
    private RoleClass(String code, String display) {
        // Setting the code of the class role
        this.code = code;

        // Setting the display name of the class role
        this.display = display;
    }

    /**
     * A method returning the code of the class role.
     * 
     * @return the code of the class role.
     */
    public String getCode() {
        return code;
    }

    /**
     * A method returning the display name of the class role.
     * 
     * @return the display name of the class role.
     */
    public String getDisplay() {
        return display;
    }

    /**
     * A method returning an alphanumeric representation of the object.
     * 
     * @return the code of the class role code.
     */
    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
