/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferay.portal.security.auth;

/**
 *
 * @author kostaskarkaletsis
 */
public class StorkProperties {

    private String providerName;
    private String spSector;
    private String spApplication;
    private String spCountry;
    private String spQaLevel;
    private String pepsURL;
    private String loginURL;
    private String spReturnURL;
    private String spPersonalAttributes;
    private String spBusinessAttributes;
    private String spLegalAttributes;

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getSpSector() {
        return spSector;
    }

    public void setSpSector(String spSector) {
        this.spSector = spSector;
    }

    public String getSpApplication() {
        return spApplication;
    }

    public void setSpApplication(String spApplication) {
        this.spApplication = spApplication;
    }

    public String getSpCountry() {
        return spCountry;
    }

    public void setSpCountry(String spCountry) {
        this.spCountry = spCountry;
    }

    public String getSpQaLevel() {
        return spQaLevel;
    }

    public void setSpQaLevel(String spQaLevel) {
        this.spQaLevel = spQaLevel;
    }

    public String getPepsURL() {
        return pepsURL;
    }

    public void setPepsURL(String pepsURL) {
        this.pepsURL = pepsURL;
    }

    public String getLoginURL() {
        return loginURL;
    }

    public void setLoginURL(String loginURL) {
        this.loginURL = loginURL;
    }

    public String getSpReturnURL() {
        return spReturnURL;
    }

    public void setSpReturnURL(String spReturnURL) {
        this.spReturnURL = spReturnURL;
    }

    public String getSpPersonalAttributes() {
        return spPersonalAttributes;
    }

    public void setSpPersonalAttributes(String spPersonalAttributes) {
        this.spPersonalAttributes = spPersonalAttributes;
    }

    public String getSpBusinessAttributes() {
        return spBusinessAttributes;
    }

    public void setSpBusinessAttributes(String spBusinessAttributes) {
        this.spBusinessAttributes = spBusinessAttributes;
    }

    public String getSpLegalAttributes() {
        return spLegalAttributes;
    }

    public void setSpLegalAttributes(String spLegalAttributes) {
        this.spLegalAttributes = spLegalAttributes;
    }

}
