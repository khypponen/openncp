package epsos.ccd.gnomon.cda.parser.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A class encapsulating a generic effective time.
 *
 * @author Akis Papadopoulos
 *
 */
public class Time {

    // Static date and time formatter
    private static SimpleDateFormat dater = new SimpleDateFormat("yyyyMMdd");
    // Optional start date
    private Date low;
    // Optional end date
    private Date high;
    // Type of the effective time
    private String type;

    /**
     * A constructor initializing an effective time.
     */
    public Time() {
        // Setting a null start date and time
        this.low = null;

        // Setting a null end date and time
        this.high = null;

        // Setting a null effective time type
        this.type = null;
    }

    /**
     * A constructor initializing an effective time.
     *
     * @param low the start date of the effective time.
     * @param high the end date of the effective time.
     */
    public Time(Date low, Date high) {
        // Setting the start date of the effective time
        this.low = low;

        // Setting the end date of the effective time
        this.high = high;

        // Setting a null effective time type
        this.type = null;
    }

    /**
     * A constructor initializing an effective time.
     *
     * @param low the start date of the effective time.
     * @param days the extension period of the low date.
     */
    public Time(Date low, int days, String type) {
        // Checking for a valid date
        if (low != null) {
            // Setting the start date of the effective time
            this.low = low;

            // Extending the low date by the given days
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(low);
            calendar.add(Calendar.DATE, days - 1);

            // Setting the end date of the effective time
            this.high = calendar.getTime();
        } else {
            // Setting a null start date and time
            this.low = null;

            // Setting a null end date and time
            this.high = null;
        }

        // Setting a null effective time type
        this.type = null;
    }

    /**
     * A method returns the start date of the effective time.
     *
     * @return the start date of the effective time.
     */
    public Date getLow() {
        return low;
    }

    /**
     * A method setting the start date of the effective time.
     *
     * @param low the start date to set.
     */
    public void setLow(Date low) {
        this.low = low;
    }

    /**
     * A method returns the end date of the effective time.
     *
     * @return the end date of the effective time.
     */
    public Date getHigh() {
        return high;
    }

    /**
     * A method setting the end date of the effective time.
     *
     * @param high the end date to set.
     */
    public void setHigh(Date high) {
        this.high = high;
    }

    /**
     * A method returning the effective time type.
     *
     * @return the effective time type.
     */
    public String getType() {
        return type;
    }

    /**
     * A method setting the effective time type.
     *
     * @param type the type to et.
     */
    public void setType(String type) {
        this.type = type;
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

        // Opening the effective time element
        context.append("<effectiveTime");

        // Checking if the type is provided
        if (type != null) {
            // Setting the xsi type attribute
            context.append(" ")
                    .append("xsi:type=\"")
                    .append(type)
                    .append("\"");

            // Closing the opening effective time element
            context.append(">");
        } else {
            // Closing the opening effective time element
            context.append(">");
        }

        // Checking if both start and end date and time are not provided
        if (low == null && high == null) {
            // Adding a null flavored low element
            context.append("<low nullFlavor=\"UNK\"/>");

            // Adding a null flavored high element
            context.append("<high nullFlavor=\"UNK\"/>");
        } else {
            // Checking if the start date and time is provided
            if (low != null) {
                // Adding the low element
                context.append("<low")
                        .append(" ")
                        .append("value=\"")
                        .append(dater.format(low))
                        .append("\"/>");
            }

            // Checking if the end date and time is provided
            if (high != null) {
                // Adding the high element
                context.append("<high")
                        .append(" ")
                        .append("value=\"")
                        .append(dater.format(high))
                        .append("\"/>");
            }
        }

        // Closing the effective time element
        context.append("</effectiveTime>");

        return context.toString();
    }
}
