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
 * This class represents a Validation Results Overview object.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
@XmlType(propOrder = {"validationDate", "validationTime", "validationServiceName", "validationTestResult"})
public class ValidationResultsOverview {

    /* PARAMETERS */
    private String validationDate;
    private String validationTime;
    private String validationServiceName;
    private String validationTestResult;

    /* GETTERS AND SETTERS */
    /**
     * @return the validationDate
     */
    @XmlElement(name = "ValidationDate")
    public String getValidationDate() {
        return validationDate;
    }

    /**
     * @param validationDate the validationDate to set
     */
    public void setValidationDate(String validationDate) {
        this.validationDate = validationDate;
    }

    /**
     * @return the validationTime
     */
    @XmlElement(name = "ValidationTime")
    public String getValidationTime() {
        return validationTime;
    }

    /**
     * @param validationTime the validationTime to set
     */
    public void setValidationTime(String validationTime) {
        this.validationTime = validationTime;
    }

    /**
     * @return the validationServiceName
     */
    @XmlElement(name = "ValidationServiceName")
    public String getValidationServiceName() {
        return validationServiceName;
    }

    /**
     * @param validationServiceName the validationServiceName to set
     */
    public void setValidationServiceName(String validationServiceName) {
        this.validationServiceName = validationServiceName;
    }

    /**
     * @return the validationTestResult
     */
    @XmlElement(name = "ValidationTestResult")
    public String getValidationTestResult() {
        return validationTestResult;
    }

    /**
     * @param validationTestResult the validationTestResult to set
     */
    public void setValidationTestResult(String validationTestResult) {
        this.validationTestResult = validationTestResult;
    }
}
