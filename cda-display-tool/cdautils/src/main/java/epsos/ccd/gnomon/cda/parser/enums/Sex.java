package epsos.ccd.gnomon.cda.parser.enums;

/**
 * A basic enum coding the sex of an entity.
 * 
 * @author Akis Papadopoulos
 * 
 */
public enum Sex {
    
    // Sex code and display name
    Male("M", "Male"),
    Female("F", "Female"),
    Undifferentiated("UN", "Undifferentiated");

    // Code system
    public static final String CODE_SYSTEM = "2.16.840.1.113883.5.1";
    //
    public static final String CODE_SYSTEM_NAME = "HL7:AdministrativeGender";
    //
    public static final String CODE_SYSTEM_VERSION = "913-20091020";
    // Sex code
    private String code;
    // Sex display name
    private String display;

    /**
     * A basic constructor initializing the sex of an entity.
     * 
     * @param code the code of the sex.
     * @param display the display name of the sex.
     */
    private Sex(String code, String display) {
        // Setting the code of the sex
        this.code = code;

        // Setting the display name of the sex
        this.display = display;
    }

    /**
     * A method returning the code of the sex.
     * 
     * @return the code of the sex.
     */
    public String getCode() {
        return code;
    }

    /**
     * A method returning the display name of the sex.
     * 
     * @return the display name of the sex.
     */
    public String getDisplay() {
        return display;
    }

    /**
     * A method returning an alphanumeric representation of the object.
     * 
     * @return the code of the sex.
     */
    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
