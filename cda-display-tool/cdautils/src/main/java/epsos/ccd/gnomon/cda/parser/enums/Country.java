package epsos.ccd.gnomon.cda.parser.enums;

/**
 * A basic enum coding a country into an ISO code system.
 * 
 * @author Akis Papadopoulos
 * 
 */
public enum Country {
    
    // Country ISO code, locale and display name
    Austria("AT", "nn", "Austria"),
    Belgium("BE", "nn", "Belgium"),
    Switzerland("CH", "nn", "Switzerland"),
    Czech_Republic("CZ", "nn", "Czech Republic"),
    Germany("DE", "nn", "Germany"),
    Denmark("DK", "nn", "Denmark"),
    Estonia("EE", "nn", "Estonia"),
    Spain("ES", "nn", "Spain"),
    Finland("FI", "nn", "Finland"),
    France("FR", "nn", "France"),
    United_Kingdom("GB", "en", "United Kingdom"),
    Greece("GR", "el", "Greece"),
    Hungary("HU", "nn", "Hungary"),
    Italy("IT", "nn", "Italy"),
    Malta("MT", "en", "Malta"),
    Netherlands("NL", "nn", "Netherlands"),
    Norway("NO", "nn", "Norway"),
    Poland("PL", "nn", "Poland"),
    Portugal("PT", "nn", "Portugal"),
    Sweden("SE", "nn", "Sweden"),
    Slovenia("SI", "nn", "Slovenia"),
    Slovakia("SK", "nn", "Slovakia"),
    Turkey("TR", "nn", "Turkey");

    // Country code
    private String code;
    // Country language code
    private String language;
    // Country display name
    private String display;

    /**
     * A basic constructor initializing the country.
     * 
     * @param code the ISO code of the country.
     * @param language the country language code.
     * @param display the display name of the country.
     */
    private Country(String code, String language, String display) {
        // Setting the code of the country
        this.code = code;
        
        // Setting the country language code
        this.language = language;

        // Setting the display name of the country
        this.display = display;
    }

    /**
     * A method returning the code of the country.
     * 
     * @return the code of the country.
     */
    public String getCode() {
        return code;
    }
    
    /**
     * A method returning the language code of the country.
     * 
     * @return the language code of the country.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * A method returning the display name of the country.
     * 
     * @return the display name of the country.
     */
    public String getDisplay() {
        return display;
    }
    
    /**
     * A method returning the locale of the country.
     * 
     * @return the locale of the country.
     */
    public String getLocale() {
        return language + "-" + code;
    }

    /**
     * A method returning an alphanumeric representation of the object.
     * 
     * @return the code of the country.
     */
    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
