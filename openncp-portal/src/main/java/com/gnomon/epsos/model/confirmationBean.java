package com.gnomon.epsos.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.slf4j.Logger;

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
