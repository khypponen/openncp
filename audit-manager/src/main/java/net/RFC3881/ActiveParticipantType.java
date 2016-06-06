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
**/
package net.RFC3881;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Kostas Karkaletsis
 * @author Organization: Gnomon
 * @author mail:k.karkaletsis@gnomon.com.gr
 * @version 1.0, 2010, 30 Jun
 * <p>Java class for ActiveParticipantType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ActiveParticipantType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="RoleIDCode" type="{}CodedValueType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="UserID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="AlternativeUserID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="UserName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="UserIsRequestor" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="NetworkAccessPointID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="NetworkAccessPointTypeCode">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
 *             &lt;enumeration value="1"/>
 *             &lt;enumeration value="2"/>
 *             &lt;enumeration value="3"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActiveParticipantType", propOrder = {
    "roleIDCode"
})
@XmlSeeAlso({
    net.RFC3881.AuditMessage.ActiveParticipant.class
})
public class ActiveParticipantType implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "RoleIDCode")
    protected List<CodedValueType> roleIDCode;
    @XmlAttribute(name = "UserID", required = true)
    protected String userID;
    @XmlAttribute(name = "AlternativeUserID")
    protected String alternativeUserID;
    @XmlAttribute(name = "UserName")
    protected String userName;
    @XmlAttribute(name = "UserIsRequestor")
    protected Boolean userIsRequestor;
    @XmlAttribute(name = "NetworkAccessPointID")
    protected String networkAccessPointID;
    @XmlAttribute(name = "NetworkAccessPointTypeCode")
    protected Short networkAccessPointTypeCode;

    /**
     * Gets the value of the roleIDCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roleIDCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoleIDCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodedValueType }
     * 
     * 
     */
    public List<CodedValueType> getRoleIDCode() {
        if (roleIDCode == null) {
            roleIDCode = new ArrayList<CodedValueType>();
        }
        return this.roleIDCode;
    }

    /**
     * Gets the value of the userID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserID(String value) {
        this.userID = value;
    }

    /**
     * Gets the value of the alternativeUserID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternativeUserID() {
        return alternativeUserID;
    }

    /**
     * Sets the value of the alternativeUserID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternativeUserID(String value) {
        this.alternativeUserID = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the userIsRequestor property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isUserIsRequestor() {
        if (userIsRequestor == null) {
            return true;
        } else {
            return userIsRequestor;
        }
    }

    /**
     * Sets the value of the userIsRequestor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUserIsRequestor(Boolean value) {
        this.userIsRequestor = value;
    }

    /**
     * Gets the value of the networkAccessPointID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkAccessPointID() {
        return networkAccessPointID;
    }

    /**
     * Sets the value of the networkAccessPointID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkAccessPointID(String value) {
        this.networkAccessPointID = value;
    }

    /**
     * Gets the value of the networkAccessPointTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getNetworkAccessPointTypeCode() {
        return networkAccessPointTypeCode;
    }

    /**
     * Sets the value of the networkAccessPointTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setNetworkAccessPointTypeCode(Short value) {
        this.networkAccessPointTypeCode = value;
    }

}
