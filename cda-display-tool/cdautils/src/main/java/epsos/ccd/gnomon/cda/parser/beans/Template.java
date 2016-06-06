package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating a template root element.
 *
 * @author Akis Papadopoulos
 *
 */
public class Template {

    // Root of the template
    private String root;

    /**
     * A constructor initializing a template.
     */
    public Template() {
        // Setting a null root
        this.root = null;
    }

    /**
     * A constructor initializing a template.
     *
     * @param code the root of the template.
     */
    public Template(String root) {
        // Setting the root
        this.root = root;
    }

    /**
     * A method returning the root of the template.
     *
     * @return the root of the template.
     */
    public String getRoot() {
        return root;
    }

    /**
     * A method setting the template root.
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

        // Opening the template element
        context.append("<templateId");

        // Checking if the root is provided
        if (root != null) {
            // Setting the root attribute
            context.append(" ")
                    .append("root=\"")
                    .append(root)
                    .append("\"");
        }

        // Closing the template element
        context.append("/>");

        return context.toString();
    }
}
