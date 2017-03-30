package com.gnomon.epsos.model;

import epsos.openncp.protocolterminator.clientconnector.PatientDemographics;

import java.io.Serializable;

public class Patient implements Serializable {
    
    private static final long serialVersionUID = 9198440413715960989L;

    private String name;
    private String familyName;
    private String country;
    private String birthDate;
    private String administrativeGender;
    private String city;
    private String root;
    private String extension;
    private String Telephone;
    private String postalCode;
    private String email;
    private String address;
    private PatientDemographics patientDemographics;

    public Patient() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAdministrativeGender() {
        return administrativeGender;
    }

    public void setAdministrativeGender(String administrativeGender) {
        this.administrativeGender = administrativeGender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRoot() {
        return this.root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getTelephone() {
        return this.Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PatientDemographics getPatientDemographics() {
        return patientDemographics;
    }

    public void setPatientDemographics(PatientDemographics patientDemographics) {
        this.patientDemographics = patientDemographics;
    }

}
