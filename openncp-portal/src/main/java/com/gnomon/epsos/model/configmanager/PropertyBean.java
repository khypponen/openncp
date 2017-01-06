package com.gnomon.epsos.model.configmanager;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@SessionScoped
public class PropertyBean {

    private String property;
    private String value;

    private static Logger log = LoggerFactory.getLogger("PropertyBean");
    private List<Property> properties;

    public PropertyBean() {
        getPropertiesFromDB();
    }

    public void removeItem(Property property) {
        CustomResponse cr = PropertyItemDAO.deleteItem(property.getProperty());
        if (cr.getCode().equals("200")) {
            FacesContext.getCurrentInstance().addMessage("test message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleting property", property.getProperty()));
        } else {
            FacesContext.getCurrentInstance().addMessage("error message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deleting property", cr.getDescription()));
        }
        try {
            properties = PropertyItemDAO.getItems();
        } catch (SQLException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        clearForm();

    }

    public String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }

    public String dummyAction() {
        log.info("dummyAction");
//			String url = LiferayUtils.generateURL("view1");
//			username="kostas";
//			log.info(url);
//			try {
//				FacesContext.getCurrentInstance().getExternalContext().
////				redirect("/web/guest/what-we-do?p_auth=3oYbzPPZ&p_p_id=yubico_WAR_yubicoportlet&p_p_lifecycle=1&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_yubico_WAR_yubicoportlet__facesViewIdRender=%2Fview2.xhtml");
//				redirect(url);
//				} catch (Exception e)
//				{
//				// TODO Auto-generated catch block
//				log.error(ExceptionUtils.getStackTrace(e));
//				}
        return "view2";
    }

    private void clearForm() {
        property = null;
        value = null;
    }

    private void getPropertiesFromDB() {
        try {
            properties = PropertyItemDAO.getItems();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

}
