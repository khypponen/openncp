package epsos.ccd.gnomon.cda.parser.beans;

import epsos.ccd.gnomon.cda.parser.enums.MedicalSection;

import java.util.HashSet;
import java.util.Set;

/**
 * A class encapsulating a generic medical component for medical documents.
 *
 * @author Akis Papadopoulos
 *
 */
public class MedicalComponent {

    // Optional medical component id
    private Identifier id;
    // Template root set
    private Set<String> roots;
    // Medical section code of the component
    private MedicalSection section;
    // Title of the medical component
    private String title;
    // Narrative text of the medical component
    private NarrativeText text;
    // Set of medical entries
    private Set<MedicalEntry> entries;

    /**
     * A constructor initializing a generic medical component.
     */
    public MedicalComponent() {
        // Setting a null id of the medical component
        this.id = null;

        // Creating an empty set of template roots
        this.roots = new HashSet<String>();

        // Setting a null medical section code
        this.section = null;

        // Setting a null title of the medical component
        this.title = null;

        // Creating an empty narrative text of the medical component
        this.text = new NarrativeText();

        // Creating an empty set of medical entries
        this.entries = new HashSet<MedicalEntry>();
    }

    /**
     * A constructor initializing a generic medical component.
     *
     * @param section the medical section code of the component.
     * @param title the title of the medical component.
     */
    public MedicalComponent(MedicalSection section, String title) {
        // Setting a null id of the medical component
        this.id = null;

        // Creating an empty set of template roots
        this.roots = new HashSet<String>();

        // Setting the medical section code
        this.section = section;

        // Setting the title of the medical component
        this.title = title;

        // Creating an empty narrative text of the medical component
        this.text = new NarrativeText();

        // Creating set of medical entries
        this.entries = new HashSet<MedicalEntry>();
    }

    /**
     * A method returning the id of the medical component.
     *
     * @return the id of the medical component.
     */
    public Identifier getId() {
        return id;
    }

    /**
     * A method setting the id of the medical component.
     *
     * @param id the id to set.
     */
    public void setId(Identifier id) {
        this.id = id;
    }

    /**
     * A method returning the template roots of the component.
     *
     * @return the template roots of the component.
     */
    public Set<String> getRoots() {
        return roots;
    }

    /**
     * A method adding a template root.
     *
     * @param root a template root.
     */
    public void addRoot(String root) {
        roots.add(root);
    }

    /**
     * A method returning the medical section code.
     *
     * @return the section code of the component.
     */
    public MedicalSection getSection() {
        return section;
    }

    /**
     * A method setting the medical section code of the component.
     *
     * @param section the section code to set.
     */
    public void setSection(MedicalSection section) {
        this.section = section;
    }

    /**
     * A method returning the title of the medical component.
     *
     * @return the title of the component.
     */
    public String getTitle() {
        return title;
    }

    /**
     * A method setting the title of the medical component.
     *
     * @param title the title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * A method returning the narrative text of the component.
     *
     * @return the narrative text of the component.
     */
    public NarrativeText getText() {
        return text;
    }

    /**
     * A method setting the narrative text of the component.
     *
     * @param text the text to set.
     */
    public void setText(NarrativeText text) {
        this.text = text;
    }

    /**
     * A method returning the medical entries of the component.
     *
     * @return the medical entries of the component.
     */
    public Set<MedicalEntry> getEntries() {
        return entries;
    }

    /**
     * A method to add a medical entry into the component.
     *
     * @param entry a medical entry.
     */
    public void addEntry(MedicalEntry entry) {
        // Adding the entry into the list of medical entries
        entries.add(entry);

        // Adding the entry to the narrative text
        text.addEntry(entry);
    }

    /**
     * A method returns if the component has an empty set of entries.
     *
     * @return if the component has an empty set of entries.
     */
    public boolean isEmpty() {
        return (entries.isEmpty());
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

        // Adding an opening section comment
        context.append("<!--");
        context.append(title.toLowerCase().replaceAll(" ", "-").replaceAll(",", ""));
        context.append("-->");

        // Opening the component element
        context.append("<component>");

        // Opening the section element
        context.append("<section>");

        // Checking if the template roots are provided
        if (roots.size() > 0) {
            // Iterating through the set of roots
            for (String root : roots) {
                // Creating the next template
                Template template = new Template(root);

                // Adding the template element
                context.append(template);
            }
        }

        // Checking if the medical component id is provided
        if (id != null) {
            // Adding the id element
            context.append(id);
        }

        // Checking if the medical section code is provided
        if (section != null) {
            // Creating a medical section code element
            Code code = new GenericCode(section.getCode(), section.getDisplay(), MedicalSection.CODE_SYSTEM, MedicalSection.CODE_SYSTEM_NAME);

            // Adding the medical section code element
            context.append(code);
        }

        // Checking if the title is provided
        if (title != null) {
            // Adding the title element
            context.append("<title>")
                    .append(title)
                    .append("</title>");
        }

        // Checking if the text is provided
        if (text != null) {
            // Adding the narrative text
            context.append(text);
        } else {
            // Adding a null flavored text element
            context.append("<text/>");
        }

        // Checking if any medical entry provided
        if (entries.size() > 0) {
            // Iterating through medical entries
            for (MedicalEntry entry : entries) {
                // Checking for a valid entry
                if (entry != null) {
                    // Adding the next medical entry
                    context.append(entry);
                }
            }
        }

        // Closing the section element
        context.append("</section>");

        // Closing the component element
        context.append("</component>");

        // Adding an closing section comment
        context.append("<!--");
        context.append(title.toLowerCase().replaceAll(" ", "-").replaceAll(",", ""));
        context.append("-->");

        return context.toString();
    }
}
