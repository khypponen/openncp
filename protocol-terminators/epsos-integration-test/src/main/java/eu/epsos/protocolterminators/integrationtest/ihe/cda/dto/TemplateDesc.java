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

package eu.epsos.protocolterminators.integrationtest.ihe.cda.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * This class represents a Template Description object.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
@XmlType(propOrder = {"template"})
public class TemplateDesc {

    /* PARAMETERS */
    private String validation;
    private Template template;

    /* GETTERS AND SETTERS */

    /**
     * @return the validation
     */
    @XmlAttribute
    public String getValidation() {
        return validation;
    }

    /**
     * @param validation the validation to set
     */
    public void setValidation(String validation) {
        this.validation = validation;
    }

    /**
     * @return the template
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(Template template) {
        this.template = template;
    }

}
