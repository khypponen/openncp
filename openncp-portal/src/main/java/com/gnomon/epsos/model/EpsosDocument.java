package com.gnomon.epsos.model;

import java.io.Serializable;
import java.util.Calendar;

public class EpsosDocument implements Serializable {

    private static final long serialVersionUID = -5104272838219835358L;
    private String docId;
    private String author;
    private String description;
    private Calendar creationDate;
    private String title;

    public EpsosDocument() {
        super();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
