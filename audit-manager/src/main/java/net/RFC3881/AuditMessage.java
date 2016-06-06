/***Licensed to the Apache Software Foundation (ASF) under one
*or more contributor license agreements.  See the NOTICE file
*distributed with this work for additional information
*regarding copyright ownership.  The ASF licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
**/package net.RFC3881;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author Kostas Karkaletsis
 * @author Organization: Gnomon
 * @author mail:k.karkaletsis@gnomon.com.gr
 * @version 1.0, 2010, 30 Jun
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EventIdentification" type="{}EventIdentificationType"/>
 *         &lt;element name="ActiveParticipant" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{}ActiveParticipantType">
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AuditSourceIdentification" type="{}AuditSourceIdentificationType" maxOccurs="unbounded"/>
 *         &lt;element name="ParticipantObjectIdentification" type="{}ParticipantObjectIdentificationType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "eventIdentification",
    "activeParticipant",
    "auditSourceIdentification",
    "participantObjectIdentification"
})
@XmlRootElement(name = "AuditMessage")
public class AuditMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "EventIdentification", required = true)
    protected EventIdentificationType eventIdentification;
    @XmlElement(name = "ActiveParticipant", required = true)
    protected List<AuditMessage.ActiveParticipant> activeParticipant;
    @XmlElement(name = "AuditSourceIdentification", required = true)
    protected List<AuditSourceIdentificationType> auditSourceIdentification;
    @XmlElement(name = "ParticipantObjectIdentification")
    protected List<ParticipantObjectIdentificationType> participantObjectIdentification;

    /**
     * Gets the value of the eventIdentification property.
     * 
     * @return
     *     possible object is
     *     {@link EventIdentificationType }
     *     
     */
    public EventIdentificationType getEventIdentification() {
        return eventIdentification;
    }

    /**
     * Sets the value of the eventIdentification property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventIdentificationType }
     *     
     */
    public void setEventIdentification(EventIdentificationType value) {
        this.eventIdentification = value;
    }

    /**
     * Gets the value of the activeParticipant property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the activeParticipant property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActiveParticipant().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AuditMessage.ActiveParticipant }
     * 
     * 
     */
    public List<AuditMessage.ActiveParticipant> getActiveParticipant() {
        if (activeParticipant == null) {
            activeParticipant = new ArrayList<AuditMessage.ActiveParticipant>();
        }
        return this.activeParticipant;
    }

    /**
     * Gets the value of the auditSourceIdentification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the auditSourceIdentification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuditSourceIdentification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AuditSourceIdentificationType }
     * 
     * 
     */
    public List<AuditSourceIdentificationType> getAuditSourceIdentification() {
        if (auditSourceIdentification == null) {
            auditSourceIdentification = new ArrayList<AuditSourceIdentificationType>();
        }
        return this.auditSourceIdentification;
    }

    /**
     * Gets the value of the participantObjectIdentification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the participantObjectIdentification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParticipantObjectIdentification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParticipantObjectIdentificationType }
     * 
     * 
     */
    public List<ParticipantObjectIdentificationType> getParticipantObjectIdentification() {
        if (participantObjectIdentification == null) {
            participantObjectIdentification = new ArrayList<ParticipantObjectIdentificationType>();
        }
        return this.participantObjectIdentification;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{}ActiveParticipantType">
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class ActiveParticipant extends ActiveParticipantType implements Serializable {

    	private static final long serialVersionUID = 1L;

    }
}
