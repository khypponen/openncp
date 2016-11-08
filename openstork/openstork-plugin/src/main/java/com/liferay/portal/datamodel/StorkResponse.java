/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferay.portal.datamodel;

import eu.stork.peps.auth.commons.IPersonalAttributeList;
import java.util.Map;

/**
 *
 * @author kostaskarkaletsis
 */
public class StorkResponse {

    private String givenName;
    private String surname;
    private String email;
    private String eIdentifier;
    private String hcpRole;
    private String hcpInfo;
    private String epsosRole;
    private IPersonalAttributeList attrs;
    Map<String, String> representedPersonalData;
    Map<String, String> representedDemographicsData;
    Map<String, String> attributes;

    public String getHcpRole() {
        return hcpRole;
    }

    public void setHcpRole(String hcpRole) {
        this.hcpRole = hcpRole;
    }

    public String getHcpInfo() {
        return hcpInfo;
    }

    public void setHcpInfo(String hcpInfo) {
        this.hcpInfo = hcpInfo;
    }

    public String getEpsosRole() {
        return epsosRole;
    }

    public void setEpsosRole(String epsosRole) {
        this.epsosRole = epsosRole;
    }

    public IPersonalAttributeList getAttrs() {
        return attrs;
    }

    public void setAttrs(IPersonalAttributeList attrs) {
        this.attrs = attrs;
    }

    public Map<String, String> getRepresentedPersonalData() {
        return representedPersonalData;
    }

    public void setRepresentedPersonalData(Map<String, String> representedPersonalData) {
        this.representedPersonalData = representedPersonalData;
    }

    public Map<String, String> getRepresentedDemographicsData() {
        return representedDemographicsData;
    }

    public void setRepresentedDemographicsData(Map<String, String> representedDemographicsData) {
        this.representedDemographicsData = representedDemographicsData;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String geteIdentifier() {
        return eIdentifier;
    }

    public void seteIdentifier(String eIdentifier) {
        this.eIdentifier = eIdentifier;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

}
