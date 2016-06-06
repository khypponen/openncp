package epsos.ccd.gnomon.cda.parser.enums;

/**
 * A basic enum coding the URL schemes of a telecommunication contact.
 * 
 * @author Akis Papadopoulos
 * 
 */
public enum URLScheme {
    
    // Communication URL scheme code and display scheme
    FAX("FAX", "fax:"),
    FILE("FILE", "file:"),
    FTP("FTP", "ftp:"),
    HTTP("HTTP", "http:"),
    MAILTO("MAILTO", "mailto:"),
    MLLP("MLLP", "mllp:"),
    MODEM("MODEM", "modem:"),
    NFS("NFS", "nfs:"),
    TEL("TEL", "tel:"),
    TELNET("TELNET", "telnet:");

    // Use code
    private String code;
    // Use display name
    private String display;

    /**
     * A basic constructor initializing the URL scheme of a contact.
     * 
     * @param code the code of the URL scheme.
     * @param display the display scheme.
     */
    private URLScheme(String code, String display) {
        // Setting the code of the URL scheme
        this.code = code;

        // Setting the display name of the URL scheme
        this.display = display;
    }

    /**
     * A method returning the code of the URL scheme.
     * 
     * @return the code of the URL scheme.
     */
    public String getCode() {
        return code;
    }

    /**
     * A method returning the display scheme.
     * 
     * @return the display name of the scheme.
     */
    public String getDisplay() {
        return display;
    }

    /**
     * A method returning an alphanumeric representation of the object.
     * 
     * @return the display scheme.
     */
    @Override
    public String toString() {
        return String.valueOf(display);
    }
}
