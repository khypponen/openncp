package epsos.ccd.gnomon.cda.parser.beans;

import epsos.ccd.gnomon.cda.parser.enums.Prefix;

import java.util.HashSet;
import java.util.Set;

/**
 * A class encapsulating the full name of a person.
 *
 * @author Akis Papadopoulos
 *
 */
public class Name {

    // Set of family names
    private Set<String> families;
    // Set of given names
    private Set<String> givens;
    // Set of optional prefixes
    private Set<Prefix> prefixes;

    /**
     * A constructor initializing name.
     */
    public Name() {
        // Creating an empty family name set
        this.families = new HashSet<String>();

        // Creating an empty given name set
        this.givens = new HashSet<String>();

        // Crating an empty prefix set
        this.prefixes = new HashSet<Prefix>();
    }
    
    public Name(String family, String given) {
        // Creating an empty family name set
        this.families = new HashSet<String>();

        // Adding the family name
        families.add(family);

        // Creating an empty given name set
        this.givens = new HashSet<String>();

        // Adding the given name
        givens.add(given);

        // Crating an empty prefix set
        this.prefixes = new HashSet<Prefix>();
    }

    /**
     * A method returning the set of the person's family names.
     *
     * @return the set of person's family names.
     */
    public Set<String> getFamilies() {
        return families;
    }

    /**
     * A method adding a new family name into the set of families.
     *
     * @param family a family name.
     */
    public void addFamily(String family) {
        families.add(family);
    }

    /**
     * A method returning the set of the person's given names.
     *
     * @return the set of person's given names.
     */
    public Set<String> getGivens() {
        return givens;
    }

    /**
     * A method adding a new given name into the set of givens.
     *
     * @param given a given name.
     */
    public void addGiven(String given) {
        givens.add(given);
    }

    /**
     * A method returning the set of the person's prefix names.
     *
     * @return the set of person's prefix names.
     */
    public Set<Prefix> getPrefixes() {
        return prefixes;
    }

    /**
     * A method adding a new prefix into the set of prefixes.
     *
     * @param prefix a prefix.
     */
    public void addPrefix(Prefix prefix) {
        prefixes.add(prefix);
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

        // Opening the name element
        context.append("<name>");

        // Checking if any family name is provided
        if (families.size() > 0) {
            // Iterating through the family name set
            for (String family : families) {
                // Checking for a valid family name
                if (family != null) {
                    context.append("<family>")
                            .append(family)
                            .append("</family>");
                } else {
                    // Adding a null flavored family element
                    context.append("<family nullFlavor=\"UNK\"/>");
                }
            }
        }

        // Checking if any given name is provided
        if (givens.size() > 0) {
            // Iterating through the given name set
            for (String given : givens) {
                // Checking for a valid given name
                if (given != null) {
                    context.append("<given>")
                            .append(given)
                            .append("</given>");
                } else {
                    // Adding a null flavored given element
                    context.append("<given nullFlavor=\"UNK\"/>");
                }
            }
        }

        // Checking if any prefix is provided
        if (prefixes.size() > 0) {
            // Iterating through the prefix set
            for (Prefix prefix : prefixes) {
                // Checking for a valid prefix
                if (prefix != null) {
                    // Adding the next prefix element
                    context.append("<prefix>")
                            .append(prefix)
                            .append("</prefix>");
                }
            }
        }

        // Closing the name element
        context.append("</name>");
        
        return context.toString();
    }
}
