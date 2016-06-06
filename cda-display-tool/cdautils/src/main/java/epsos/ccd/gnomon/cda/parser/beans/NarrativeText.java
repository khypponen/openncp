package epsos.ccd.gnomon.cda.parser.beans;

import java.util.HashSet;
import java.util.Set;

/**
 * A class encapsulating medical entries in an narrative text in XML/HTML table
 * form.
 *
 * @author Akis Papadopoulos
 *
 */
public class NarrativeText {

    // Entries set
    private Set<MedicalEntry> entries;

    /**
     * A constructor initializing an narrative text.
     */
    public NarrativeText() {
        // Creating an empty set of medical entries
        this.entries = new HashSet<MedicalEntry>();
    }

    /**
     * A method to add a medical entry.
     *
     * @param entry a medical entry.
     */
    public void addEntry(MedicalEntry entry) {
        entries.add(entry);
    }

    /**
     * A parsing method transforms object data into a text-based XML/HTML
     * format.
     *
     * @return an XML/HTML representation of the object.
     */
    @Override
    public String toString() {
        // Creating a context holder
        StringBuilder context = new StringBuilder();

        // Checking if at least one medical entry is provided
        if (!entries.isEmpty()) {
            // Opening the text element
            context.append("<text>");

            // Opening the table element
            context.append("<table border=\"1\">");

            // Opening the body element
            context.append("<tbody>");

            // Opening the header row element
            context.append("<tr>");

            // Adding the code header
            context.append("<th>Code</th>");

            // Adding the description header
            context.append("<th>Description</th>");

            // closing the header row element
            context.append("</tr>");

            // Iterating through the medical entry list
            for (MedicalEntry entry : entries) {
                // Checking for a valid medical entry
                if (entry != null) {
                    // Opening next row element
                    context.append("<tr");

                    // Setting the reference id of the row
                    context.append(" ")
                            .append("ID=\"");

                    // Checking for a null UUID
                    if (entry.getUUID() != null) {
                        // Setting the medical entry UUID
                        context.append(entry.getUUID());
                    } else {
                        // Setting an unknown UUID
                        context.append("UNK");
                    }

                    // Closing the row element
                    context.append("\">");

                    // Opening the next column element
                    context.append("<td>");

                    // Checking for a null code
                    if (entry.getCode() != null) {
                        // Setting the medical entry code
                        context.append(entry.getCode().getCode());
                    } else {
                        // Setting an unknown code
                        context.append("UNK");
                    }

                    // Closing the column element
                    context.append("</td>");

                    // Opening the next column element
                    context.append("<td>");

                    // Checking for a null description
                    if (entry.getCode().getDisplay() != null) {
                        // Setting the medical entry description
                        context.append(entry.getCode().getDisplay() + "-" + entry.getDescription());
                    } else {
                        // Setting an unknown description
                        context.append("UNK");
                    }

                    // Closing the column element
                    context.append("</td>");

                    // closing the row element
                    context.append("</tr>");
                }
            }

            // Closing the body element
            context.append("</tbody>");

            // Closing the table element
            context.append("</table>");

            // Closing the text element
            context.append("</text>");
        } else {
            // Adding a null flavored text
            context.append("<text/>");
        }

        return context.toString();
    }
}
