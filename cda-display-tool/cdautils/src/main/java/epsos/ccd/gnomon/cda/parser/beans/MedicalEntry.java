package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A abstract class encapsulating a generic medical entry.
 *
 * @author Akis Papadopoulos
 *
 */
public abstract class MedicalEntry {

    // Code of the medical entry
    protected Code code;
    // Medical entry description
    protected String description;
    // Medical entry OID root
    protected String root;
    // Medical entry UUID
    protected String uuid;
    // Medical entry effective time
    protected Time effectiveTime;

    /**
     * A constructor initializing a generic medical entry.
     */
    public MedicalEntry() {
        // Setting a null code of the medical entry
        this.code = null;

        // Setting a null description of the medical entry
        this.description = null;

        // Setting a null OID root of the medical entry
        this.root = null;

        // Setting a null effective time of the medical entry
        this.effectiveTime = null;
    }

    /**
     * A constructor initializing a generic medical entry.
     *
     * @param code the code of the medical entry.
     * @param effectiveTime the effective time of the medical entry.
     */
    public MedicalEntry(Code code, Time effectiveTime) {
        // Setting the code of the medical entry
        this.code = code;

        // Setting a null description of the medical entry
        this.description = null;

        // Setting a null OID root of medical entry
        this.root = null;

        // Setting the effective of the medical entry
        this.effectiveTime = effectiveTime;
    }

    /**
     * A method returning the medical entry code.
     *
     * @return the medical entry code.
     */
    public Code getCode() {
        return code;
    }

    /**
     * A method setting the code of the medical entry.
     *
     * @param code the code to set.
     */
    public void setCode(Code code) {
        this.code = code;
    }

    /**
     * A method returning the description of the medical entry.
     *
     * @return the description of the medical entry.
     */
    public String getDescription() {
        return description;
    }

    /**
     * A method setting the description of the medical entry.
     *
     * @param description the description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * A method returning the root of the OID of the medical entry.
     *
     * @return the root of the OID of the medical entry.
     */
    public String getRoot() {
        return root;
    }

    /**
     * A method setting the root OID of the entry.
     *
     * @param root the root OID to set.
     */
    public void setRoot(String root) {
        this.root = root;
    }

    /**
     * A method returning the UUID of the medical entry.
     *
     * @return the UUID of the medical entry.
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * A method setting the UUID of the entry.
     *
     * @param uuid the UUID to set.
     */
    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    /**
     * A method returning the effective time of the medical entry.
     *
     * @return the medical entry effective time.
     */
    public Time getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * A method setting the effective time of the medical entry.
     *
     * @param effectiveTime the effective time to set.
     */
    public void setEffectiveTime(Time effectiveTime) {
        this.effectiveTime = effectiveTime;
    }
}
