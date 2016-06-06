package epsos.ccd.gnomon.cda.parser.enums;

/**
 * A basic enum coding the uses of a telecommunication contact.
 * 
 * @author Akis Papadopoulos
 * 
 */
public enum ContactUse {
    
    // Communication use code and display name
    AS("AS", "answering service"),
    EC("EC", "emergency contact"),
    H("H", "home"),
    HP("HP", "primary home"),
    HV("HV", "vacation home"),
    MC("MC", "mobile contact"),
    PG("PG", "pager"),
    WP("WP", "work place");

    // Use code
    private String code;
    // Use display name
    private String display;

    /**
     * A basic constructor initializing the contact use.
     * 
     * @param code the code of the contact use.
     * @param display the display name of the contact use.
     */
    private ContactUse(String code, String display) {
        // Setting the code of the contact use
        this.code = code;

        // Setting the display name of the contact use
        this.display = display;
    }

    /**
     * A method returning the code of the contact use.
     * 
     * @return the code of the contact use.
     */
    public String getCode() {
        return code;
    }

    /**
     * A method returning the display name of the contact use.
     * 
     * @return the display name of the contact use.
     */
    public String getDisplay() {
        return display;
    }

    /**
     * A method returning an alphanumeric representation of the object.
     * 
     * @return the code of the contact use.
     */
    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
