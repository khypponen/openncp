/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnomon.epsos.model.queries;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author kostaskarkaletsis
 */
@XmlType
public class Document {

    String root;
    String extension;
    String repositoryid;
    String hcid;
    String uuid;

    public String getRoot() {
        return root;
    }

    @XmlElement
    public void setRoot(String root) {
        this.root = root;
    }

    public String getExtension() {
        return extension;
    }

    @XmlElement
    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getRepositoryid() {
        return repositoryid;
    }

    @XmlElement
    public void setRepositoryid(String repositoryid) {
        this.repositoryid = repositoryid;
    }

    public String getHcid() {
        return hcid;
    }

    @XmlElement
    public void setHcid(String hcid) {
        this.hcid = hcid;
    }

    public String getUuid() {
        return uuid;
    }

    @XmlElement
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
