package epsos.ccd.gnomon.cda.parser.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class encapsulating a date of birth.
 *
 * @author Akis Papadopoulos
 *
 */
public class Birthdate {

    // Date formatter
    private static SimpleDateFormat dater = new SimpleDateFormat("yyyyMMdd");
    // Date and time of birth
    private Date date;

    /**
     * A constructor initializing a birth date.
     */
    public Birthdate() {
        // Setting a null date of birth
        this.date = null;
    }

    /**
     * A constructor initializing a birth date.
     *
     * @param date the date of birth.
     */
    public Birthdate(Date date) {
        // Setting the date of birth
        this.date = date;
    }

    /**
     * A constructor initializing a birth date from a custom text based date.
     *
     * @param date a custom text-form representing a date.
     */
    public Birthdate(String date) {
        try {
            // An expecting date format
            SimpleDateFormat frm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.s");

            // Parsing the text into a date
            this.date = frm.parse(date);
        } catch (ParseException exc) {
            // Setting a null value to the birthdate
            this.date = null;
        }
    }

    /**
     * A method returns the date of birth.
     *
     * @return the date of birth.
     */
    public Date getDate() {
        return date;
    }

    /**
     * A method setting the date of birth.
     *
     * @param date the birth date to set.
     */
    public void setDate(Date date) {
        this.date = date;
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

        // Checking if the date of birth is provided
        if (date != null) {
            // Opening the birth time element
            context.append("<birthTime");

            // Setting the value attribute
            context.append(" ")
                    .append("value=\"")
                    .append(dater.format(date))
                    .append("\"");

            // Closing the birth time element
            context.append("/>");
        } else {
            // Adding a null flavored birth time element
            context.append("<birthTime nullFlavor=\"UNK\"/>");
        }

        return context.toString();
    }
}
