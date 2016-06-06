 /*
  *  This file is part of epSOS OpenNCP implementation
  *  Copyright (C) 2013 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
  *
  *  This program is free software: you can redistribute it and/or modify
  *  it under the terms of the GNU General Public License as published by
  *  the Free Software Foundation, either version 3 of the License, or
  *  (at your option) any later version.
  *
  *  This program is distributed in the hope that it will be useful,
  *  but WITHOUT ANY WARRANTY; without even the implied warranty of
  *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  *  GNU General Public License for more details.
  *
  *  You should have received a copy of the GNU General Public License
  *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
  *
  *  Contact email: epsos@iuz.pt
  */
package eu.epsos.validation.datamodel.common;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class represents a Report object.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */

@XmlType(propOrder = {"test", "location", "description"})
public class Report {
    
    /* PARAMETERS */
    private String test;
    private String location;
    private String description;
    /* GETTERS AND SETTERS */

    /**
     * @return the test
     */
    @XmlElement(name = "Test")
    public String getTest() {
        return test;
    }

    /**
     * @param test the test to set
     */
    public void setTest(String test) {
        this.test = test;
    }

    /**
     * @return the location
     */
    @XmlElement(name = "Location")
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the description
     */
    @XmlElement(name = "Description")
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
}