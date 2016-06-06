/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * 
 * This file is part of SRDC epSOS NCP.
 * 
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SRDC epSOS NCP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObservationEligibilityIndicatorValue.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObservationEligibilityIndicatorValue">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ELSTAT"/>
 *     &lt;enumeration value="ADOPT"/>
 *     &lt;enumeration value="BTHCERT"/>
 *     &lt;enumeration value="CCOC"/>
 *     &lt;enumeration value="DRLIC"/>
 *     &lt;enumeration value="FOSTER"/>
 *     &lt;enumeration value="MRGCERT"/>
 *     &lt;enumeration value="MIL"/>
 *     &lt;enumeration value="PASSPORT"/>
 *     &lt;enumeration value="MEMBER"/>
 *     &lt;enumeration value="STUDENRL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObservationEligibilityIndicatorValue")
@XmlEnum
public enum ObservationEligibilityIndicatorValue {

    ELSTAT,
    ADOPT,
    BTHCERT,
    CCOC,
    DRLIC,
    FOSTER,
    MRGCERT,
    MIL,
    PASSPORT,
    MEMBER,
    STUDENRL;

    public String value() {
        return name();
    }

    public static ObservationEligibilityIndicatorValue fromValue(String v) {
        return valueOf(v);
    }

}
