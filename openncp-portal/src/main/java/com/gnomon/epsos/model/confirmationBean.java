package com.gnomon.epsos.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class confirmationBean {

    private static Logger log = LoggerFactory.getLogger("ConfirmationBean");
    private String purposeOfUse;
    private String confirm;

    public String getPurposeOfUse() {
        return purposeOfUse;
    }

    public void setPurposeOfUse(String purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        confirmationBean.log = log;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
