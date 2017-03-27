package com.gnomon.epsos.model;

import com.gnomon.LiferayUtils;
import com.gnomon.epsos.service.Demographics;
import com.gnomon.epsos.service.EpsosHelperService;
import com.gnomon.epsos.service.SearchMask;
import com.liferay.portal.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@ManagedBean
@SessionScoped
public class NcpBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String errorUserAssertion;
    private List<Country> countries;
    private String selectedCountry;
    private boolean showDemographics;
    private List<Identifier> identifiers;
    private List<Demographics> demographics;
    private static final Logger log = LoggerFactory.getLogger("NcpBean");

    public NcpBean() {
        new MyBean();

        log.info("Initializing NcpBean ...");
        selectedCountry = EpsosHelperService.getConfigProperty("ncp.country");
        identifiers = new ArrayList<Identifier>();
        demographics = new ArrayList<Demographics>();
        setIdentifiers(selectedCountry);
    }

    public String getSelectedCountry() {
        return selectedCountry;
    }

    private void setIdentifiers(String selectedCountry) {
        log.info("Setting identifiers for:" + selectedCountry);
        User user = LiferayUtils.getPortalUser();
        LiferayUtils.storeToSession("user", user);

        identifiers = new ArrayList<Identifier>();
        Vector vec = EpsosHelperService
                .getCountryIdsFromCS(selectedCountry);
        for (int i = 0; i < vec.size(); i++) {
            Identifier id = new Identifier();
            id.setKey(EpsosHelperService.getPortalTranslation(
                    ((SearchMask) vec.get(i)).getLabel(),
                    LiferayUtils.getPortalLanguage())
                    + "*");
            id.setDomain(((SearchMask) vec.get(i)).getDomain());
            if (id.getKey().equals("") || id.getKey().equals("*")) {
                id.setKey(((SearchMask) vec.get(i)).getLabel() + "*");
            }
            identifiers.add(id);
            log.info("Identifier: " +id);
        }

        demographics = new ArrayList<Demographics>();
        vec = EpsosHelperService
                .getCountryDemographicsFromCS(this.selectedCountry);
        for (int i = 0; i < vec.size(); i++) {
            Demographics id = new Demographics();
            if (((Demographics) vec.get(i)).getMandatory()) {
                id.setLabel(EpsosHelperService.getPortalTranslation(
                        ((Demographics) vec.get(i)).getLabel(),
                        LiferayUtils.getPortalLanguage())
                        + "*");
            } else {
                id.setLabel(EpsosHelperService.getPortalTranslation(
                        ((Demographics) vec.get(i)).getLabel(),
                        LiferayUtils.getPortalLanguage()));
            }
            id.setLength(((Demographics) vec.get(i)).getLength());
            id.setKey(((Demographics) vec.get(i)).getKey());
            id.setMandatory(((Demographics) vec.get(i)).getMandatory());
            id.setType(((Demographics) vec.get(i)).getType());
            demographics.add(id);
        }
        showDemographics = !demographics.isEmpty();
        LiferayUtils.storeToSession("selectedCountry", selectedCountry);
    }

    public boolean isShowDemographics() {
        return showDemographics;
    }

    public void setShowDemographics(boolean showDemographics) {
        this.showDemographics = showDemographics;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public List<Demographics> getDemographics() {
        return demographics;
    }

    public void setDemographics(List<Demographics> demographics) {
        this.demographics = demographics;
    }
}
