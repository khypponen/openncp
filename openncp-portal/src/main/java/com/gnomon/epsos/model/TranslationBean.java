package com.gnomon.epsos.model;

import com.gnomon.LiferayUtils;
import com.gnomon.epsos.FacesService;
import com.gnomon.epsos.service.EpsosHelperService;
import com.liferay.portal.model.User;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.Properties;

@ManagedBean
@SessionScoped
public class TranslationBean implements Serializable {


    private static final Logger log = LoggerFactory.getLogger("TranslationBean");
    private static final long serialVersionUID = -421731346650722469L;
    private Properties properties;

    public TranslationBean() {
        String lang = "en-US";
        try {
            User user = LiferayUtils.getPortalUser();
            lang = user.getLanguageId().replace("_", "-");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            populateProperties(externalContext.getRealPath("/"), lang);
        } catch (Exception e) {
            log.error("Problem getting user from context");
        }
    }

    public void setLangProperties(String path, String lang) {
        populateProperties(path, lang);
    }

    public void populateProperties(String portalPath, String lang) {
        String epsosPropsPath = System.getenv("EPSOS_PROPS_PATH");
        String wi = portalPath + "/WEB-INF/";
        // Get the language keys from folder under epsos props path
        String path = wi + "portal/language" + File.separator + "application"
                + File.separator + lang + File.separator
                + "SpiritEhrPortal.xml";
        log.info("Language path is in: " + path);
        String defpath = epsosPropsPath + "portal/language" + File.separator + "application"
                + File.separator + "SpiritEhrPortal.xml";

        log.info("#### " + EpsosHelperService.getConfigProperty(EpsosHelperService.PORTAL_DOCTOR_PERMISSIONS));
        properties = new java.util.Properties();
        try {
            FileInputStream fis = new FileInputStream(path);
            try {
                properties.loadFromXML(fis);
            } catch (IOException e) {
                FileInputStream deffis = new FileInputStream(defpath);
                try {
                    properties.loadFromXML(deffis);
                } catch (IOException e1) {
                    log.error(ExceptionUtils.getStackTrace(e1));
                }
                log.error("Problem loading language file" + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            log.error("File not found - language file" + e.getMessage());
        }
    }

    public String getTranslation(String key, String lang) {
        return LiferayUtils.getPortalTranslation(key, lang);
    }

    public String getTranslation(String key) {
        return LiferayUtils.getPortalTranslation(key, FacesService.getPortalLanguage());
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
