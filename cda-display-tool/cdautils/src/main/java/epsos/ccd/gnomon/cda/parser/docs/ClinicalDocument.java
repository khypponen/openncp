package epsos.ccd.gnomon.cda.parser.docs;

import epsos.ccd.gnomon.cda.parser.beans.Author;
import epsos.ccd.gnomon.cda.parser.beans.Code;
import epsos.ccd.gnomon.cda.parser.beans.Custodian;
import epsos.ccd.gnomon.cda.parser.beans.Identifier;
import epsos.ccd.gnomon.cda.parser.beans.LegalAuthenticator;
import epsos.ccd.gnomon.cda.parser.beans.MedicalComponent;
import epsos.ccd.gnomon.cda.parser.beans.Participant;
import epsos.ccd.gnomon.cda.parser.beans.Patient;
import epsos.ccd.gnomon.cda.parser.beans.RelatedDocument;
import epsos.ccd.gnomon.cda.parser.enums.Country;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A class encapsulating a generic clinical document.
 *
 * @author Akis Papadopoulos
 *
 */
public class ClinicalDocument {

    // Date formatter
    protected static SimpleDateFormat dater = new SimpleDateFormat("yyyyMMdd");
    // Document class code
    protected Code code;
    // Document identification number
    protected Identifier id;
    // Document title
    protected String title;
    // Date of document creation
    protected Date created;
    // Last updated of the document
    protected Date updated;
    // Document language code
    protected Country language;
    // Patient of the document
    protected Patient patient;
    // Set of participants
    protected Set<Participant> participants;
    // Author of the document
    protected Author author;
    // Authenticator of the document
    protected LegalAuthenticator authenticator;
    // Custodian of the document
    protected Custodian custodian;
    // Related documents
    protected Set<RelatedDocument> relatedDocuments;
    // Set of medical components
    protected Set<MedicalComponent> components;

    /**
     * A constructor initializing the clinical document.
     */
    public ClinicalDocument() {
        // Setting a null class code of the document
        this.code = null;

        // Setting a null identifier of the document
        this.id = null;

        // Setting a null title
        this.title = null;

        // Setting the creation date of the document
        this.created = new Date();

        // Setting the last updated date of the document
        this.updated = new Date();

        // Setting a null country origin language code
        this.language = null;

        // Setting a null patient demographics
        this.patient = null;

        // Creating an empty set of the participants
        this.participants = new HashSet<Participant>();

        // Setting a null author of the document
        this.author = null;

        // Setting a null authenticator of the document
        this.authenticator = null;

        // Setting a null custodian of the document
        this.custodian = null;

        // Creating an empty set of related document
        this.relatedDocuments = new HashSet<RelatedDocument>();

        // Creating an empty set of medical components
        this.components = new HashSet<MedicalComponent>();
    }

    /**
     * A constructor initializing the clinical document header data.
     *
     * @param code the class code of the document.
     * @param id the document identifier.
     * @param created the creation date of the document.
     * @param updated the last updated date of the document.
     * @param language the language of the document.
     */
    public ClinicalDocument(Code code, Identifier id, Date created, Date updated, Country language) {
        // Setting the class code of the document
        this.code = code;

        // Setting the identifier of the document
        this.id = id;

        // Setting the document's title
        this.title = code.getDisplay();

        // Setting the creation date of the document
        this.created = created;

        // Setting the last updated date of the document
        this.updated = updated;

        // Setting the country origin language code
        this.language = language;

        // Setting a null patient demographics
        this.patient = null;

        // Creating an empty set of the participants
        this.participants = new HashSet<Participant>();

        // Setting a null author of the document
        this.author = null;

        // Setting a null authenticator of the document
        this.authenticator = null;

        // Setting a null custodian of the document
        this.custodian = null;

        // Creating an empty set of related document
        this.relatedDocuments = new HashSet<RelatedDocument>();

        // Creating an empty set of medical components
        this.components = new HashSet<MedicalComponent>();
    }

    /**
     * A method returning the document code class.
     *
     * @return the document code class.
     */
    public Code getCode() {
        return code;
    }

    /**
     * A method setting the code of the document.
     *
     * @param code the code of the document.
     */
    public void setCode(Code code) {
        this.code = code;
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
     * @param id the id to set.
     */
    public void setId(Identifier id) {
        this.id = id;
    }

    /**
     * A method returning the document title.
     *
     * @return the document title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * A method setting the title of the document.
     *
     * @param title the title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * A method returning the document date of creation.
     *
     * @return the document creation date.
     */
    public Date getCreated() {
        return created;
    }

    /**
     * A method setting the creation date of the document.
     *
     * @param created the date to set.
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * A method returning the date of the last update.
     *
     * @return the date of the last update.
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * A method to set the update date of the document.
     *
     * @param updated the date to set.
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    /**
     * A method returning the language of the document.
     *
     * @return the document language.
     */
    public Country getLanguage() {
        return language;
    }

    /**
     * A method setting the language of the document.
     *
     * @param language the language of the document.
     */
    public void setLanguage(Country language) {
        this.language = language;
    }

    /**
     * A method returning the patient of the document.
     *
     * @return the patient of the document.
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * A method setting the patient of the document.
     *
     * @param patient the patient to set.
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * A method returning the participant of the document.
     *
     * @return the participant of the document.
     */
    public Set<Participant> getParticipants() {
        return participants;
    }

    /**
     * A method to add a new participant.
     *
     * @param participant a participant.
     */
    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    /**
     * A method returning the author of the document.
     *
     * @return the author of the document.
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * A method setting the author of the document.
     *
     * @param author the author to set.
     */
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * A method returning the legal authenticator of the document.
     *
     * @return the legal authenticator of the document.
     */
    public LegalAuthenticator getAuthenticator() {
        return authenticator;
    }

    /**
     * A method setting the legal authenticator of the document.
     *
     * @param authenticator the authenticator to set.
     */
    public void setAuthenticator(LegalAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    /**
     * A method returning the custodian of the document.
     *
     * @return the custodian of the document.
     */
    public Custodian getCustodian() {
        return custodian;
    }

    /**
     * A method setting the custodian of the document.
     *
     * @param custodian the custodian to set.
     */
    public void setCustodian(Custodian custodian) {
        this.custodian = custodian;
    }

    /**
     * A method returning the related documents.
     *
     * @return the related documents.
     */
    public Set<RelatedDocument> getRelatedDocuments() {
        return relatedDocuments;
    }

    /**
     * A method to add a new related document.
     *
     * @param relatedDocument a related document to add.
     */
    public void addRelatedDocument(RelatedDocument relatedDocument) {
        relatedDocuments.add(relatedDocument);
    }

    /**
     * A method returning the medical sections of the document.
     *
     * @return the medical sections of the document.
     */
    public Set<MedicalComponent> getComponents() {
        return components;
    }

    /**
     * A method to add a new medical component into the set.
     *
     * @param component a medical component.
     */
    public void addComponent(MedicalComponent component) {
        components.add(component);
    }
}
