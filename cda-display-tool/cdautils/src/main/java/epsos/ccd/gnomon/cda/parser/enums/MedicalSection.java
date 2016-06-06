package epsos.ccd.gnomon.cda.parser.enums;

/**
 * A basic enum coding the medical section of a medical document.
 * 
 * @author Akis Papadopoulos
 * 
 */
public enum MedicalSection {
    
    // Medical section code and display name
    HIST_MEDICATION_USE("10160-0", "History of medication use"),
    HIST_PREGNACIES("10162-6", "History of pregnancies"),
    HIST_PRESENT_ILLNESS("10164-2", "History of present illness"),
    HIST_PAST_ILLNESS("11348-0", "History of past illness"),
    HIST_IMMUNIZATION("11369-6", "History of immunization"),
    PROBLEM_LIST("11450-4", "Problem list"),
    PLAN_TREATMENT("18776-5", "Plan of treatment"),
    PROCEDURES("29554-3", "Procedures"),
    SOCIAL_HISTORY("29762-2", "Social history"),
    DIAGNOSIS_TESTS("30954-2", "Relevant diagnostic tests/laboratory data"),
    HIST_MEDICAL_DEV_USE("46264-8", "History of medical device use"),
    FUNC_STATUS_ASSESMENT("47420-5", "Functional status assessment"),
    HIST_PROCEDURES("47519-4", "History of Procedures"),
    ALLERGIES("48765-2", "Allergies, adverse reactions, alerts"),
    PRESCRIPTIONS("57828-6", "Prescriptions"),
    MEDICATION_DISPENSED("60590-7", "Medication dispensed"),
    PHYSICAL_FINDINGS("8716-3", "Physical findings");

    // Medical section system code
    public static final String CODE_SYSTEM = "2.16.840.1.113883.6.1";
    // Medical section system code name
    public static final String CODE_SYSTEM_NAME = "LOINC";
    // Medical section code
    private String code;
    // Medical section display name
    private String display;

    /**
     * A basic constructor initializing the medical section.
     * 
     * @param code the code of the medical section.
     * @param display the display name of the medical section.
     */
    private MedicalSection(String code, String display) {
        // Setting the code of the medical section
        this.code = code;

        // Setting the display name of the medical section
        this.display = display;
    }

    /**
     * A method returning the code of the medical section.
     * 
     * @return the code of the medical section.
     */
    public String getCode() {
        return code;
    }

    /**
     * A method returning the display name of the medical section.
     * 
     * @return the display name of the medical section.
     */
    public String getDisplay() {
        return display;
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
