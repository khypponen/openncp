/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: andreas Date: 2011-06-30 Time: 15.41 To
 * change this template use File | Settings | File Templates.
 */
public class PatientIdVO implements Serializable {

    private static final long serialVersionUID = -8950333817890598641L;
    private String label;
    private String domain;
    private String value;
    private Integer max;
    private Integer min;

    public PatientIdVO() {
        super();
    }

    public PatientIdVO(String label, String domain, String value) {
        this.label = label;
        this.domain = domain;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    @Override
    public String toString() {
        String result = "";
        result = result + " label: " + label;
        result = result + " domain: " + domain;
        result = result + " value: " + value;
        result = result + " max: " + max;
        result = result + " min: " + min;

        return result;
    }
}
