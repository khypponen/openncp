/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnomon.epsos.model;

import java.util.List;

/**
 *
 * @author kostaskarkaletsis
 */
public class DocumentExt {

    private Patient patient;
    private List<PatientDocument> documents;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<PatientDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<PatientDocument> documents) {
        this.documents = documents;
    }

}
