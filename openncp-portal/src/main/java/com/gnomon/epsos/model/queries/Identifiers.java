/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnomon.epsos.model.queries;

import java.util.List;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author karkaletsis
 */
@XmlType
public class Identifiers {

    private List<Identifier> identifier;

    public List<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }

}
