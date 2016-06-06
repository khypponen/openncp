package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating a generic identifier.
 *
 * @author Akis Papadopoulos
 *
 */
public class Identifier {

    // Extension id number
    private String extension;
    // Template root number
    private String root;

    /**
     * A constructor initializing an identifier.
     */
    public Identifier() {
        // Setting a null extension id number
        this.extension = null;

        // Setting a null template's root
        this.root = null;
    }

    /**
     * A constructor initializing an identifier.
     *
     * @param extension the extension id number.
     * @param root the template's root number.
     */
    public Identifier(String extension, String root) {
        // Setting the extension id number
        this.extension = extension;

        // Setting the template's root
        this.root = root;
    }

    /**
     * A method returning the extension id number.
     *
     * @return the extension id number.
     */
    public String getExtension() {
        return extension;
    }

    /**
     * A method setting the extension.
     *
     * @param extension the extension to set.
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * A method returning the template root number.
     *
     * @return the template's root number.
     */
    public String getRoot() {
        return root;
    }

    /**
     * A method setting the root.
     *
     * @param root the root to set.
     */
    public void setRoot(String root) {
        this.root = root;
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

        // Checking for a null flavored identifier
        if (extension == null & root == null) {
            // Adding a null flavored id element
            context.append("<id nullFlavor=\"UNK\"/>");
        } else {
            // Opening an id element
            context.append("<id");

            // Checking if the extension attribute is provided
            if (extension != null) {
                // Setting the extension attribute
                context.append(" ")
                        .append("extension=\"")
                        .append(extension)
                        .append("\"");
            }

            // Checking if the root attribute is provided
            if (root != null) {
                // Setting the root attribute
                context.append(" ")
                        .append("root=\"")
                        .append(root)
                        .append("\"");
            }

            // Closing the id element
            context.append("/>");
        }

        return context.toString();
    }
}
