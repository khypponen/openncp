/*
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. epsos@srdc.com.tr
 *
 * This file is part of SRDC epSOS NCP.
 *
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see http://www.gnu.org/licenses/.
 */
package tr.com.srdc.epsos.data.model;

import org.opensaml.saml2.core.Assertion;

/**
 * @author erdem
 *
 */
public class XdrRequest {

    private Assertion idAssertion;
    private Assertion trcAssertion;
    private String countryCode;
    private String countryName;
    private String cdaId;
    private String cda;
    private GenericDocumentCode documentCode;
    private PatientDemographics patient;

    public PatientDemographics getPatient() {
        return patient;
    }

    public void setPatient(PatientDemographics patient) {
        this.patient = patient;
    }

    public String getCdaId() {
        return cdaId;
    }

    public void setCdaId(String cdaId) {
        this.cdaId = cdaId;
    }

    public String getCda() {
        return cda;
    }

    public void setCda(String cda) {
        this.cda = cda;
    }

    public Assertion getIdAssertion() {
        return idAssertion;
    }

    public void setIdAssertion(Assertion idAssertion) {
        this.idAssertion = idAssertion;
    }

    public Assertion getTrcAssertion() {
        return trcAssertion;
    }

    public void setTrcAssertion(Assertion trcAssertion) {
        this.trcAssertion = trcAssertion;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setcountryCode(String policyCountryCode) {
        this.countryCode = policyCountryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String policyCountryName) {
        this.countryName = policyCountryName;
    }

    /**
     * @return the documentCode
     */
    public GenericDocumentCode getDocumentCode() {
        return documentCode;
    }

    /**
     * @param documentCode the documentCode to set
     */
    public void setDocumentCode(GenericDocumentCode documentCode) {
        this.documentCode = documentCode;
    }
}
