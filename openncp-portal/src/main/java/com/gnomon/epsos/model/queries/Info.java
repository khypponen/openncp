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
public class Info {

    String screenname;
    String emailaddress;
    String orgname;
    String orgtype;
    String orgid;
    String rolename;
    String fullname;

    public String getScreenname() {
        return screenname;
    }

    @XmlElement
    public void setScreenname(String screenname) {
        this.screenname = screenname;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    @XmlElement
    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getOrgname() {
        return orgname;
    }

    @XmlElement
    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getOrgtype() {
        return orgtype;
    }

    @XmlElement
    public void setOrgtype(String orgtype) {
        this.orgtype = orgtype;
    }

    public String getOrgid() {
        return orgid;
    }

    @XmlElement
    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getRolename() {
        return rolename;
    }

    @XmlElement
    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getFullname() {
        return fullname;
    }

    @XmlElement
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

}
