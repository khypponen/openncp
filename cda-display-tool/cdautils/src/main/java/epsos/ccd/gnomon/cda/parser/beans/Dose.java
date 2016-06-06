package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating a dose of a medication.
 *
 * @author Akis Papadopoulos
 *
 */
public class Dose {

    // Minimum quantity of the dose
    private String low;
    // Maximum quantity of the dose
    private String high;
    // Unit measurement of the dose
    private String unit;

    /**
     * A constructor initializing a null flavored dose of medication.
     */
    public Dose() {
        // Setting a null minimum quantity of the dose
        this.low = null;

        // Setting a null maximum quantity of the dose
        this.high = null;

        // Setting a null unit measurement of the dose
        this.unit = null;
    }

    /**
     * A constructor initializing a dose of medication.
     *
     * @param low the minimum quantity of the dose.
     * @param high the maximum quantity of the dose.
     * @param unit the unit measurement of the dose.
     */
    public Dose(String low, String high, String unit) {
        // Setting the minimum quantity of the dose
        this.low = low;

        // Setting the maximum quantity of the dose
        this.high = high;

        // Setting the unit measurement of the dose
        this.unit = unit;
    }

    /**
     * A method returning the minimum quantity of the dose.
     *
     * @return the minimum quantity of the dose.
     */
    public String getLow() {
        return low;
    }

    /**
     * A method setting the minimum quantity of the dose.
     *
     * @param low the minimum quantity to set.
     */
    public void setLow(String low) {
        this.low = low;
    }

    /**
     * A method returning the maximum quantity of the dose.
     *
     * @return the maximum quantity of the dose.
     */
    public String getHigh() {
        return high;
    }

    /**
     * A method setting the maximum quantity of the dose.
     *
     * @param high the maximum quantity to set.
     */
    public void setHigh(String high) {
        this.high = high;
    }

    /**
     * A method returning the unit measurement of the dose.
     *
     * @return the unit measurement of the dose.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * A method setting the unit of the dose.
     *
     * @param unit the unit to set.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * A parsing method transforms object data into a text-based XML format.
     *
     * @return an XML representation of the object.
     */
    @Override
    public String toString() {
        // Creating a context holder
        StringBuilder context = new StringBuilder();

        // Checking for a null flavored dose
        if (low == null & high == null && unit == null) {
            // Adding a null flavored dose element
            context.append("<doseQuantity nullFlavor=\"UNK\"/>");
        } else {
            // Opening the dose quantity element
            context.append("<doseQuantity>");

            // Opening the low element
            context.append("<low");

            // Checking if the low value is provided
            if (low != null) {
                // Setting the value attribute
                context.append(" ")
                        .append("value=\"")
                        .append(low)
                        .append("\"");
            }

            // Checking if the unit is provided
            if (unit != null) {
                // Setting the unit attribute
                context.append(" ")
                        .append("unit=\"")
                        .append(unit)
                        .append("\"");
            }

            // Closing the low element
            context.append("/>");

            // Opening the high element
            context.append("<high");

            // Checking if the high value is provided
            if (high != null) {
                // Setting the value attribute
                context.append(" ")
                        .append("value=\"")
                        .append(high)
                        .append("\"");
            }

            // Checking if the unit is provided
            if (unit != null) {
                // Setting the unit attribute
                context.append(" ")
                        .append("unit=\"")
                        .append(unit)
                        .append("\"");
            }

            // Closing the high element
            context.append("/>");

            // Closing the dose quantity element
            context.append("</doseQuantity>");
        }

        return context.toString();
    }
}
