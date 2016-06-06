package epsos.ccd.gnomon.cda.parser.enums;

/**
 * A basic enum coding relationship role.
 * 
 * @author Akis Papadopoulos
 * 
 */
public enum RelationshipRole {
    
    // Relationship role code and display name
    AUNT("AUNT", "aunt"),
    CHILD("CHILD", "child"),
    CHLDADOPT("CHLDADOPT", "adopted child"),
    CHLDFOST("CHLDFOST", "foster child"),
    CHLDINLAW("CHLDINLAW", "child in-law"),
    COUSN("COUSN", "cousin"),
    DAU("DAU", "natural daughter"),
    DAUADOPT("DAUADOPT", "adopted daughter"),
    DAUC("DAUC", "daughter"),
    DAUFOST("DAUFOST", "foster daughter"),
    DAUINLAW("DAUINLAW", "daughter in-law"),
    DOMPART("DOMPART", "domestic partner"),
    FAMMEMB("FAMMEMB", "family member"),
    FRND("FRND", "unrelated friend"),
    FTH("FTH", "father"),
    FTHINLAW("FTHINLAW", "father-in-law"),
    GGRPRN("GGRPRN", "great grandparent"),
    GRNDCHILD("GRNDCHILD", "grandchild"),
    GRPRN("GRPRN", "grandparent"),
    MTH("MTH", "mother"),
    MTHINLAW("MTHINLAW", "mother-in-law"),
    NBOR("NBOR", "neighbor"),
    NCHILD("NCHILD", "natural child"),
    NIENEPH("NIENEPH", "niece/nephew"),
    PRN("PRN", "parent"),
    PRNINLAW("PRNINLAW", "parent in-law"),
    ROOM("ROOM", "roomate"),
    SIB("SIB", "sibling"),
    SIGOTHR("SIGOTHR", "significant other"),
    SON("SON", "natural son"),
    SONADOPT("SONADOPT", "adopted son"),
    SONC("SONC", "son"),
    SONFOST("SONFOST", "foster son"),
    SONINLAW("SONINLAW", "son in-law"),
    SPS("SPS", "spouse"),
    STPCHLD("STPCHLD", "step child"),
    STPDAU("STPDAU", "stepdaughter"),
    STPSON("STPSON", "stepson"),
    UNCLE("UNCLE", "uncle");

    // System code
    public static final String CODE_SYSTEM = "2.16.840.1.113883.5.111";
    // Relationship role code
    private String code;
    // Relationship role display name
    private String display;

    /**
     * A basic constructor initializing the relationship role.
     * 
     * @param code the code of the relationship role.
     * @param display the display name of the relationship role.
     */
    private RelationshipRole(String code, String display) {
        // Setting the code of the relationship role
        this.code = code;

        // Setting the display name of the relationship role
        this.display = display;
    }

    /**
     * A method returning the code of the relationship role.
     * 
     * @return the code of the relationship role.
     */
    public String getCode() {
        return code;
    }

    /**
     * A method returning the display name of the relationship role.
     * 
     * @return the display name of the relationship role.
     */
    public String getDisplay() {
        return display;
    }

    /**
     * A method returning an alphanumeric representation of the object.
     * 
     * @return the code of the relationship role.
     */
    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
