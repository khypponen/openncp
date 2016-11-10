/*
 *  StorkAttributes.java
 *  Created on 24/11/2014, 3:20:09 PM
 *  
 *  OPENSTORK.UTILS
 *  eu.europa.ec.joinup.ecc.openstork.utils.datamodel
 *  
 *  Copyright (C) 2014 iUZ Technologies, Lda - http://www.iuz.pt
 */
package eu.europa.ec.joinup.ecc.openstork.utils.datamodel;

/**
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public enum StorkAttributes {

    EIDENTIFIER("eIdentifier", "patient.search.patient.id"),
    GIVEN_NAME("givenName", "patient.data.givenname"),
    SURNAME("surname", "patient.data.surname"),
    DATE_OF_BIRTH("dateOfBirth", "patient.data.birth.date");

    private final String storkDesignation;
    private final String searchMaskValue;

    private StorkAttributes(String storkDesignation, String searchMaskValue) {
        this.searchMaskValue = searchMaskValue;
        this.storkDesignation = storkDesignation;
    }

    @Override
    public String toString() {
        return getSearchMaskValue();
    }

    /**
     * @return the storkDesignation
     */
    public String getStorkDesignation() {
        return storkDesignation;
    }

    /**
     * @return the searchMaskValue
     */
    public String getSearchMaskValue() {
        return searchMaskValue;
    }
}
