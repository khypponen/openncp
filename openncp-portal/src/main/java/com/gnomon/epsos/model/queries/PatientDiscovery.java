/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnomon.epsos.model.queries;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author karkaletsis
 */

/*
 <xml>
 <identifiers>
 <identifier root="1.2.3.4" extension="123" />
 <identifier root="1.2.3.4" extension="123" />
 </identifiers>
 <demographics>
 <firstname>12121</firstname>
 <lastname>osdkodskods</lastname>
 <address>doskosdkos</address>
 <city>thessaloniki</city>
 <postalCode>2023920</postalCode>
 <birthdate>1212122</birthdate>
 <country>IT</country>
 </demographics>
 </xml>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "PatientDiscovery")
public class PatientDiscovery {

    Identifiers identifiers;
    Demographics demographics;
    Info info;
    com.gnomon.epsos.model.queries.Document document;

    public Identifiers getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Identifiers identifiers) {
        this.identifiers = identifiers;
    }

    public Demographics getDemographics() {
        return demographics;
    }

    public void setDemographics(Demographics demographics) {
        this.demographics = demographics;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

}
