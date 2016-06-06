package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating an abstract code.
 *
 * @author Akis Papadopoulos
 *
 */
public abstract class Code {

    // Code
    protected String code;
    // Optional code display name
    protected String display;
    // Code system
    protected String codeSystem;
    // Code system name
    protected String codeSystemName;

    /**
     * A constructor initializing an abstract code.
     */
    public Code() {
        // Setting a null code
        this.code = null;

        // Setting a null display name of the code
        this.display = null;

        // Setting a null code system
        this.codeSystem = null;

        // Setting a null code system name
        this.codeSystemName = null;
    }

    /**
     * A constructor initializing an abstract code.
     *
     * @param code the code.
     * @param display the display name of code.
     * @param codeSystem the code system.
     * @param codeSysemName the code system name.
     */
    public Code(String code, String display, String codeSystem, String codeSystemName) {
        // Setting the code
        this.code = code;

        // Setting the display name of the code
        this.display = display;

        // Setting the code system
        this.codeSystem = codeSystem;

        // Setting the code system name
        this.codeSystemName = codeSystemName;
    }

    /**
     * A method returning the code.
     *
     * @return the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * A method setting the code.
     *
     * @param code the code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * A method returning the display name of the code.
     *
     * @return the display name of the code.
     */
    public String getDisplay() {
        return display;
    }

    /**
     * A method setting the display name of the code.
     *
     * @param display the display name to set.
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * A method returning the code system.
     *
     * @return the code system.
     */
    public String getCodeSystem() {
        return codeSystem;
    }

    /**
     * A method setting the code system.
     *
     * @param codeSystem the code system to set.
     */
    public void setCodeSystem(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * A method returning the code system name.
     *
     * @return the code system name.
     */
    public String getCodeSystemName() {
        return codeSystemName;
    }

    /**
     * A method setting the code system name.
     *
     * @param codeSystemName the code system name to set.
     */
    public void setCodeSystemName(String codeSystemName) {
        this.codeSystemName = codeSystemName;
    }
}
