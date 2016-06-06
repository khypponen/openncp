package epsos.ccd.gnomon.cda.parser.enums;

/**
 * A basic enum coding the professions of an author.
 * 
 * @author Akis Papadopoulos
 * 
 */
public enum Profession {
    
    // Profession code and display name
    HP("22", "Health professionals"),
    MD("221", "Medical doctors"),
    GMP("2211", "Generalist medical practitioners"),
    SPEMEDPR("2212", "Specialist medical practitioners"),
    NURSMIDWPRO("222", "Nursing and midwifery professionals"),
    NURS("2221", "Nursing professionals"),
    MIDWPROF("2222", "Midwifery professionals"),
    TRADMEDPROF("223", "Traditional and complementary medicine professionals"),
    PARAMED("224", "Paramedical practitioners"),
    VET("225", "Veterinarians"),
    OTHHPROF("226", "Other health professionals"),
    DENT("2261", "Dentists"),
    PHAR("2262", "Pharmacists"),
    ENVHYGPRO("2263", "Environmental and occupational health and hygiene professionals"),
    PHSY("2264", "Physiotherapists"),
    DIET("2265", "Dieticians and nutritionists"),
    AUDTHER("2266", "Audiologists and speech therapists"),
    OPTMETR("2267", "Optometrists and ophthalmic opticians"),
    HPRONEC("2269", "Health professionals not elsewhere classified"),
    HEALTHAPRO("32", "Health associate professionals"),
    MEDPHARTECH("321", "Medical and pharmaceutical technicians"),
    MEDIMGTECH("3211", "Medical imaging and therapeutic equipment technicians"),
    MEDPATHLAB("3212", "Medical and pathology laboratory technicians"),
    PHATECH("3213", "Pharmaceutical technicians and assistants"),
    MEDDENTPTECH("3214", "Medical and dental prosthetic technicians"),
    NURSMIDAP("322", "Nursing and midwifery associate professionals"),
    NURSAP("3221", "Nursing associate professionals"),
    MIDAP("3222", "Midwifery associate professionals"),
    TRADMASSP("323", "Traditional and complementary medicine associate professionals"),
    OTHHAP("325", "Other health associate professionals"),
    DENTAS("3251", "Dental assistants and therapists"),
    MDRECTECH("3252", "Medical records and health information technicians"),
    COMHW("3253", "Community health workers"),
    DESOPT("3254", "Dispensing opticians"),
    PHSTECH("3255", "Physiotherapy technicians and assistants"),
    MDASS("3256", "Medical assistants"),
    ENVHINS("3257", "Environmental and occupational health inspectors and associates"),
    AMBW("3258", "Ambulance workers"),
    HAPNEC("3259", "Health associate professionals not elsewhere classified");

    // System code
    public static final String CODE_SYSTEM = "2.16.840.1.113883.2.9.6.2.7";
    // Profession code
    private String code;
    // Profession display name
    private String display;

    /**
     * A basic constructor initializing the profession.
     * 
     * @param code the code of the profession.
     * @param display the display name of the profession.
     */
    private Profession(String code, String display) {
        // Setting the code of the profession
        this.code = code;

        // Setting the display name of the profession
        this.display = display;
    }

    /**
     * A method returning the code of the profession.
     * 
     * @return the code of the profession.
     */
    public String getCode() {
        return code;
    }

    /**
     * A method returning the display name of the profession.
     * 
     * @return the display name of the profession.
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
