package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating a related document element.
 *
 * @author Akis Papadopoulos
 *
 */
public class RelatedDocument {

    // Related document type code
    private String typeCode;
    // Related document id
    private Identifier id;

    /**
     * A constructor initializing a related document entity.
     */
    public RelatedDocument() {
        // Setting a null type code
        this.typeCode = null;

        // Setting a null identifier
        this.id = null;
    }

    /**
     * A constructor initializing a related document entity.
     *
     * @param typeCode the type code of the relation.
     * @param id the id of the document.
     */
    public RelatedDocument(String typeCode, Identifier id) {
        // Setting teh type code
        this.typeCode = typeCode;

        // Setting the identifier
        this.id = id;
    }

    /**
     * A method returning the type code of the document.
     *
     * @return the type code of the document.
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * A method setting the type code of the document.
     *
     * @param typeCode the type code of the document.
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * A method returning the document id.
     *
     * @return the document id.
     */
    public Identifier getId() {
        return id;
    }

    /**
     * A method setting the document id.
     *
     * @param id the document id to set.
     */
    public void setId(Identifier id) {
        this.id = id;
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

        // Openig the related document element
        context.append("<relatedDocument");

        // Checking if the type code is provided
        if (typeCode != null) {
            context.append(" ")
                    .append("typeCode=\"")
                    .append(typeCode)
                    .append("\"");
        }

        // Closing the related document element
        context.append(">");

        // Opening the parent document element
        context.append("<parentDocument>");

        // Checking if the parent id is provided
        if (id != null) {
            // Adding the id element
            context.append(id);
        }

        // Closing the parent document element
        context.append("</parentDocument>");

        // Closing related document element
        context.append("</relatedDocument>");

        return context.toString();
    }
}
