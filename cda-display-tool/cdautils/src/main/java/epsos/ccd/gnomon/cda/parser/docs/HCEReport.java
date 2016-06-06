package epsos.ccd.gnomon.cda.parser.docs;

import epsos.ccd.gnomon.cda.parser.beans.Code;
import epsos.ccd.gnomon.cda.parser.beans.Identifier;
import epsos.ccd.gnomon.cda.parser.beans.MedicalComponent;
import epsos.ccd.gnomon.cda.parser.beans.RelatedDocument;
import epsos.ccd.gnomon.cda.parser.beans.Time;
import epsos.ccd.gnomon.cda.parser.enums.Country;
import java.util.Date;

/**
 * A class encapsulating a health care encounter report clinical document.
 *
 * @author Akis Papadopoulos
 *
 */
public class HCEReport extends ClinicalDocument {

    /**
     * A constructor initializing a HCER clinical document.
     */
    public HCEReport() {
        // Calling the constructor of the super class
        super();
    }

    /**
     * A constructor initializing a HCER clinical document.
     *
     * @param code the class code of the document.
     * @param id the document identifier.
     * @param created the creation date of the document.
     * @param updated the last updated date of the document.
     * @param language the language of the document.
     */
    public HCEReport(Code code, Identifier id, Date created, Date updated, Country language) {
        // Calling the constructor of the super class
        super(code, id, created, updated, language);
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

        // Adding the XML header
        context.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");

        // Opening the root clinical document element
        context.append("<ClinicalDocument");

        // Adding the XML name spaces of the document
        context.append(" ");
        String ns = "xmlns=\"urn:hl7-org:v3\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:epsos=\"urn:epsos-org:ep:medication\" "
                + "xsi:schemaLocation=\"urn:hl7-org:v3 CDA_extended.xsd\" "
                + "classCode=\"DOCCLIN\" "
                + "moodCode=\"EVN\"";
        context.append(ns);
        context.append(">");

        // Adding an opening section comment
        context.append("<!--header-section-->");

        // Adding the type id element
        context.append("<typeId extension=\"POCD_HD000040\" root=\"2.16.840.1.113883.1.3\"/>");

        // Adding the template id element
        context.append("<templateId root=\"1.3.6.1.4.1.12559.11.10.1.3.1.1.3\"/>");

        // Checking if the id of the document is provided
        if (id != null) {
            // Adding the id element
            context.append(id);
        }

        // Checking if the class code of the document is provided
        if (code != null) {
            // Adding the document code
            context.append(code);
        }

        // Checking if the title of the document is provided
        if (title != null) {
            // Adding the title element
            context.append("<title>")
                    .append(title)
                    .append("</title>");
        }

        // Checking if the creation date of the document is provided
        if (created != null) {
            // Adding the effective time element
            context.append("<effectiveTime")
                    .append(" ")
                    .append("value=\"")
                    .append(dater.format(created))
                    .append("\"/>");
        }

        // Adding the confidentiality code
        context.append("<confidentialityCode code=\"N\" displayName=\"normal\" codeSystem=\"2.16.840.1.113883.5.25\" codeSystemName=\"HL7:Confidentiality\"/>");

        // Checking if the document language is provided
        if (language != null) {
            // Adding the language code element
            context.append("<languageCode")
                    .append(" ")
                    .append("code=\"")
                    .append(language.getLocale())
                    .append("\"/>");
        }

        // Checking for an invalid patient value
        if (patient != null) {
            // Adding the patient element
            context.append(patient);
        }

        // Checking for an invalid author value
        if (author != null) {
            // Adding the author element
            context.append(author);
        }

        // Checking if the custodian is provided
        if (custodian != null) {
            // Adding the custodian element section
            context.append(custodian);
        }

        // Checking for an invalid authenticator value
        if (authenticator != null) {
            // Adding the authenticator element
            context.append(authenticator);
        }

        // Opening the documentation element
        context.append("<documentationOf>");

        // Opening the service event element
        context.append("<serviceEvent classCode=\"PCPR\">");

        // Checking if the last update date is provided
        if (created != null && updated != null) {
            // Creating an effective element
            Time time = new Time(created, updated);

            // Adding the effective time element
            context.append(time);
        }

        // Oppening the performer element
        context.append("<performer typeCode=\"PRF\">");

        // Adding the function code element
        context.append("<functionCode code=\"222\" codeSystem=\"2.16.840.1.113883.2.9.6.2.7\" codeSystemName=\"epSOSHealthcareProfessionalRoles\" displayName=\"Nursing and midwifery professionals\"/>");

        // Opening the assigned entity element
        context.append("<assignedEntity>");

        // Adding the id element
        context.append("<id root=\"1.3.6.1.4.1.28284.6.2.4.32\"/>");

        // Opening the assigned person element
        context.append("<assignedPerson>");

        // Opening the name element
        context.append("<name>");

        // Adding the given element
        context.append("<given>Patient Summary</given>");

        // Adding the family name
        context.append("<family>Fetcher</family>");

        // Closing the name element
        context.append("</name>");

        // Closing the assigned person element
        context.append("</assignedPerson>");

        // Closing the assigned entity element
        context.append("</assignedEntity>");

        // Closing the performer element
        context.append("</performer>");

        // Closing the service event element
        context.append("</serviceEvent>");

        // Closing the documentation of element
        context.append("</documentationOf>");

        // Checking if any related document is provided
        if (relatedDocuments.size() > 0) {
            // Iterating throught the realted documents
            for (RelatedDocument doc : relatedDocuments) {
                // Checking if the related document is valid
                if (doc != null) {
                    context.append(doc);
                }
            }
        }

        // Adding a closing section comment
        context.append("<!--header-section-->");

        // Adding an opening section comment
        context.append("<!--medical-sections-->");

        // Opening the component element
        context.append("<component>");

        // Opening the structured body element
        context.append("<structuredBody>");

        // Checking if any medical component provided
        if (components.size() > 0) {
            // Iterate through the medical components
            for (MedicalComponent component : components) {
                // Checking for a valid medical component
                if (component != null) {
                    // Adding the next medical component
                    context.append(component);
                }
            }
        }

        // Closing the structured body element
        context.append("</structuredBody>");

        // Closing the component element
        context.append("</component>");

        // Adding a closing section comment
        context.append("<!--medical-sections-->");

        // Closing the record target element
        context.append("</ClinicalDocument>");

        return context.toString();
    }
}
