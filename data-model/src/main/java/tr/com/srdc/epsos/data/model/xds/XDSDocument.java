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
package tr.com.srdc.epsos.data.model.xds;

import tr.com.srdc.epsos.data.model.GenericDocumentCode;

/**
 * This classe encapsulats a set of properties related to a document, but not
 * it's content.
 */
public class XDSDocument {

    private String id;
    private String hcid;
    private String repositoryUniqueId;
    private String documentUniqueId;
    private String name;
    private String description;
    private String creationTime;
    private String healthcareFacility;
    private String authorPerson;
    private boolean isPDF;
    private GenericDocumentCode formatCode;
    private GenericDocumentCode classCode;

    /**
     * @return the hcid
     */
    public String getHcid() {
        return hcid;
    }

    /**
     * @param hcid the hcid to set
     */
    public void setHcid(String hcid) {
        this.hcid = hcid;
    }

    public String getRepositoryUniqueId() {
        return repositoryUniqueId;
    }

    public void setRepositoryUniqueId(String repositoryUniqueId) {
        this.repositoryUniqueId = repositoryUniqueId;
    }

    public String getDocumentUniqueId() {
        return documentUniqueId;
    }

    public void setDocumentUniqueId(String documentUniqueId) {
        this.documentUniqueId = documentUniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getHealthcareFacility() {
        return healthcareFacility;
    }

    public void setHealthcareFacility(String healthcareFacility) {
        this.healthcareFacility = healthcareFacility;
    }

    public boolean isPDF() {
        return isPDF;
    }

    public void setPDF(boolean isPDF) {
        this.isPDF = isPDF;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * @return the formatCode
     */
    public GenericDocumentCode getFormatCode() {
        return formatCode;
    }

    /**
     * @param formatCode the formatCode to set
     */
    public void setFormatCode(GenericDocumentCode formatCode) {
        this.formatCode = formatCode;
    }

    /**
     * @param schema the formatCode schema
     * @param value the formatCode value
     */
    public void setFormatCode(String schema, String value) {
        GenericDocumentCode formatCode = new GenericDocumentCode();
        formatCode.setSchema(schema);
        formatCode.setValue(value);
        this.formatCode = formatCode;
    }

    /**
     * @return the classCode
     */
    public GenericDocumentCode getClassCode() {
        return classCode;
    }

    /**
     * @param classCode the classCode to set
     */
    public void setClassCode(GenericDocumentCode classCode) {
        this.classCode = classCode;
    }

    /**
     * @param schema the classCode schema
     * @param value the classCode value
     */
    public void setClassCode(String schema, String value) {
        GenericDocumentCode classCode = new GenericDocumentCode();
        classCode.setSchema(schema);
        classCode.setValue(value);
        this.classCode = classCode;
    }

    /**
     * @return the authorPerson
     */
    public String getAuthorPerson() {
        return authorPerson;
    }

    /**
     * @param authorPerson the authorPerson to set
     */
    public void setAuthorPerson(String authorPerson) {
        this.authorPerson = authorPerson;
    }
}
