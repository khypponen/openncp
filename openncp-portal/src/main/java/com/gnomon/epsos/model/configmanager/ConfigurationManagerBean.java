package com.gnomon.epsos.model.configmanager;

import com.gnomon.LiferayUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ManagedBean
@SessionScoped
public class ConfigurationManagerBean {

    private static Logger log = LoggerFactory.getLogger("ConfigurationManagerBean");
    private List<Property> properties;
    private String property;
    private String value;
    private Property selectedProperty;
    private List<Property> filteredProperties;
    private String hasAdmin;

    public ConfigurationManagerBean() {
        hasAdmin = LiferayUtils.isAdministrator() + "";
//			if (LiferayUtils.checkPermission("ADMIN"))
//				getPropertiesFromDB();
    }

    public void removeItem(Property key) {
        CustomResponse cr = PropertyItemDAO.deleteItem(key.getProperty());
        if (cr.getCode().equals("200")) {
            FacesContext.getCurrentInstance().addMessage("test message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleting property", property));
        } else {
            FacesContext.getCurrentInstance().addMessage("error message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Deleting property" + property, cr.getDescription()));
        }
        try {
            properties = PropertyItemDAO.getItems();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            log.error(ExceptionUtils.getStackTrace(e));
        }
        clearForm();

    }

    public void addItem(ActionEvent actionEvent) throws Exception {
        CustomResponse cr = new CustomResponse();
        cr = PropertyItemDAO.addItem(property, value);
        if (cr.getCode().equals("200")) {
            FacesContext.getCurrentInstance().addMessage("test message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Adding property", property));
        } else {
            FacesContext.getCurrentInstance().addMessage("error message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding property" + property, cr.getDescription()));
        }
        properties = PropertyItemDAO.getItems();
        clearForm();
    }

    public void saveItem(String prop, String propvalue) throws Exception {
        CustomResponse cr = new CustomResponse();
        cr = PropertyItemDAO.updateItem(prop, propvalue);
        if (cr.getCode().equals("200")) {
            FacesContext.getCurrentInstance().addMessage("test message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Updating property " + prop + " with value " + propvalue, prop));
        } else {
            FacesContext.getCurrentInstance().addMessage("error message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Updating property " + prop, cr.getDescription()));
        }
        properties = PropertyItemDAO.getItems();
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

    //		public void saveItem(ActionEvent actionEvent) throws Exception {
//			CustomResponse cr = new CustomResponse();
//			cr = PropertyItemDAO.addItem(property, value);
//			if (cr.getCode().equals("200"))
//				FacesContext.getCurrentInstance().addMessage("test message",new FacesMessage(FacesMessage.SEVERITY_INFO,"Adding property",property));
//			else
//		    	FacesContext.getCurrentInstance().addMessage("error message", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Adding property", cr.getDescription()));
//			properties = PropertyItemDAO.getItems();
//			clearForm();
//		}
    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Property Edited", ((Property) event.getObject()).getProperty());
        try {
            saveItem(((Property) event.getObject()).getProperty(), ((Property) event.getObject()).getValue());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
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

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
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

    public Property getSelectedProperty() {
        return selectedProperty;
    }

    public void setSelectedProperty(Property selectedProperty) {
        this.selectedProperty = selectedProperty;
    }

    public List<Property> getFilteredProperties() {
        return filteredProperties;
    }

    public void setFilteredProperties(List<Property> filteredProperties) {
        this.filteredProperties = filteredProperties;
    }

    public String getHasAdmin() {
        return hasAdmin;
    }

    public void setHasAdmin(String hasAdmin) {
        this.hasAdmin = hasAdmin;
    }

}
