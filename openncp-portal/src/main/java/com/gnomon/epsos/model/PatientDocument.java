package com.gnomon.epsos.model;

import epsos.openncp.protocolterminator.clientconnector.GenericDocumentCode;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class PatientDocument implements Serializable {

    private static final Logger log = LoggerFactory.getLogger("PatientDocument");
    private static final long serialVersionUID = 2812911062938264973L;
    private String title;
    private String description;
    private String author;
    private String creationDate;
    private String healthcareFacility;
    private byte[] file;
    private String uuid;
    private StreamedContent streamedFile;
    private GenericDocumentCode formatCode;
    private String repositoryId;
    private String hcid;
    private String docType;

    public PatientDocument() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getHealthcareFacility() {
        return healthcareFacility;
    }

    public void setHealthcareFacility(String healthcareFacility) {
        this.healthcareFacility = healthcareFacility;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {

        this.file = file;

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public StreamedContent getStreamedFile() {

        return streamedFile;
    }

    public void setStreamedFile(StreamedContent streamedFile) {
        this.streamedFile = streamedFile;
    }

    public void downloadDocument() {

    }

    public GenericDocumentCode getFormatCode() {
        return formatCode;
    }

    public void setFormatCode(GenericDocumentCode formatCode) {
        this.formatCode = formatCode;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getHcid() {
        return hcid;
    }

    public void setHcid(String hcid) {
        this.hcid = hcid;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }
}
