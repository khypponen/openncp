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
 * This class represents a ValidationCounters object.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
@XmlType(propOrder = {"nrOfChecks", "nrOfValidationErros", "nrOfValidationWarnings", "nrOfValidationNotes", "nrOfValidationReports", "nrOfValidationUnknown"})
public class ValidationCounters {

    /* PARAMETERS */
    private String nrOfChecks;
    private String nrOfValidationErros;
    private String nrOfValidationWarnings;
    private String nrOfValidationNotes;
    private String nrOfValidationReports;
    private String nrOfValidationUnknown;

    /* GETTERS AND SETTERS */
    /**
     * @return the nrOfChecks
     */
    @XmlElement(name = "NrOfChecks")
    public String getNrOfChecks() {
        return nrOfChecks;
    }

    /**
     * @param nrOfChecks the nrOfChecks to set
     */
    public void setNrOfChecks(String nrOfChecks) {
        this.nrOfChecks = nrOfChecks;
    }

    /**
     * @return the nrOfValidationErros
     */
    @XmlElement(name = "NrOfValidationErrors")
    public String getNrOfValidationErros() {
        return nrOfValidationErros;
    }

    /**
     * @param nrOfValidationErros the nrOfValidationErros to set
     */
    public void setNrOfValidationErros(String nrOfValidationErros) {
        this.nrOfValidationErros = nrOfValidationErros;
    }

    /**
     * @return the nrOfValidationWarnings
     */
    @XmlElement(name = "NrOfValidationWarnings")
    public String getNrOfValidationWarnings() {
        return nrOfValidationWarnings;
    }

    /**
     * @param nrOfValidationWarnings the nrOfValidationWarnings to set
     */
    public void setNrOfValidationWarnings(String nrOfValidationWarnings) {
        this.nrOfValidationWarnings = nrOfValidationWarnings;
    }

    /**
     * @return the nrOfValidationNotes
     */
    @XmlElement(name = "NrOfValidationNotes")
    public String getNrOfValidationNotes() {
        return nrOfValidationNotes;
    }

    /**
     * @param nrOfValidationNotes the nrOfValidationNotes to set
     */
    public void setNrOfValidationNotes(String nrOfValidationNotes) {
        this.nrOfValidationNotes = nrOfValidationNotes;
    }

    /**
     * @return the nrOfValidationReports
     */
    @XmlElement(name = "NrOfValidationReports")
    public String getNrOfValidationReports() {
        return nrOfValidationReports;
    }

    /**
     * @param nrOfValidationReports the nrOfValidationReports to set
     */
    public void setNrOfValidationReports(String nrOfValidationReports) {
        this.nrOfValidationReports = nrOfValidationReports;
    }

    /**
     * @return the nrOfValidationUnknown
     */
    @XmlElement(name = "NrOfValidationUnknown")
    public String getNrOfValidationUnknown() {
        return nrOfValidationUnknown;
    }

    /**
     * @param nrOfValidationUnknown the nrOfValidationUnknown to set
     */
    public void setNrOfValidationUnknown(String nrOfValidationUnknown) {
        this.nrOfValidationUnknown = nrOfValidationUnknown;
    }
}