package epsos.ccd.gnomon.cda.parser.enums;

/**
 * A basic enum coding the patient's prefixes.
 * 
 * @author Akis Papadopoulos
 * 
 */
public enum Prefix {
    
    // Prefix code and display name
    AC("AC", "academic"),
    AD("AD", "adopted"),
    BR("BR", "birth"),
    CL("CL", "callme"),
    IN("IN", "initial"),
    LS("LS", "legal status"),
    NB("NB", "nobility"),
    PR("PR", "professional"),
    SP("SP", "spouse"),
    TITLE("TITLE", "title"),
    VV("VV", "voorvoegsel");

    // Prefix code
    private String code;
    // Prefix display name
    private String display;

    /**
     * A basic constructor initializing the prefix.
     * 
     * @param code the code of the prefix.
     * @param display the display name of the prefix.
     */
    private Prefix(String code, String display) {
        // Setting the code of the prefix
        this.code = code;

        // Setting the display name of the prefix
        this.display = display;
    }

    /**
     * A method returning the code of the prefix.
     * 
     * @return the code of the prefix.
     */
    public String getCode() {
        return code;
    }

    /**
     * A method returning the display name of the prefix.
     * 
     * @return the display name of the prefix.
     */
    public String getDisplay() {
        return display;
    }

    /**
     * A method returning an alphanumeric representation of the object.
     * 
     * @return the code of the prefix.
     */
    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
