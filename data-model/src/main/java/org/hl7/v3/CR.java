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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             A concept qualifier code with optionally named role.
 *             Both qualifier role and value codes must be defined by
 *             the coding system.  For example, if SNOMED RT defines a
 *             concept "leg", a role relation "has-laterality", and
 *             another concept "left", the concept role relation allows
 *             to add the qualifier "has-laterality: left" to a primary
 *             code "leg" to construct the meaning "left leg".
 *          
 * 
 * <p>Java class for CR complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CR">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}ANY">
 *       &lt;sequence>
 *         &lt;element name="name" type="{urn:hl7-org:v3}CV" minOccurs="0"/>
 *         &lt;element name="value" type="{urn:hl7-org:v3}CD" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="inverted" type="{urn:hl7-org:v3}bn" default="false" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CR", propOrder = {
    "name",
    "value"
})
public class CR
    extends ANY
{

    protected CV name;
    protected CD value;
    @XmlAttribute
    protected Boolean inverted;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link CV }
     *     
     */
    public CV getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link CV }
     *     
     */
    public void setName(CV value) {
        this.name = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link CD }
     *     
     */
    public CD getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link CD }
     *     
     */
    public void setValue(CD value) {
        this.value = value;
    }

    /**
     * Gets the value of the inverted property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isInverted() {
        if (inverted == null) {
            return false;
        } else {
            return inverted;
        }
    }

    /**
     * Sets the value of the inverted property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInverted(Boolean value) {
        this.inverted = value;
    }

}
