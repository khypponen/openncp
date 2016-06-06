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

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class represents a SchematronValidation object.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
@XmlType(propOrder = {"warnings", "reports", "result", "validationCounters"})
public class SchematronValidation {

    /* PARAMETERS */
    private List<Warning> warnings;
    private List<Report> reports;
    private String result;
    private ValidationCounters validationCounters;


    /* GETTERS AND SETTERS */
    /**
     * @return the warnings
     */
    @XmlElement(name = "Warning")
    public List<Warning> getWarnings() {
        return warnings;
    }

    /**
     * @param warnings the warnings to set
     */
    public void setWarnings(List<Warning> warnings) {
        this.warnings = warnings;
    }

    /**
     * @return the reports
     */
    @XmlElement(name = "Report")
    public List<Report> getReports() {
        return reports;
    }

    /**
     * @param reports the reports to set
     */
    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

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

    /**
     * @return the validationCounters
     */
    @XmlElement(name = "ValidationCounters")
    public ValidationCounters getValidationCounters() {
        return validationCounters;
    }

    /**
     * @param validationCounters the validationCounters to set
     */
    public void setValidationCounters(ValidationCounters validationCounters) {
        this.validationCounters = validationCounters;
    }
}