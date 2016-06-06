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
 * This class represents a DocumentWellFormed object.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
@XmlType(propOrder = {"result"})
public class DocumentWellFormed {

    /* PARAMETERS */
    private String result;

    /* GETTERS AND SETTERS */

    /**
     * @return the result
     */
    @XmlElement(name = "Result")
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }
}
