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
package oasis.names.tc.ebxml_regrep.xsd.query._3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdhocQueryQueryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdhocQueryQueryType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}RegistryObjectQueryType">
 *       &lt;sequence>
 *         &lt;element name="QueryExpressionBranch" type="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}QueryExpressionBranchType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdhocQueryQueryType", propOrder = {
    "queryExpressionBranch"
})
public class AdhocQueryQueryType
    extends RegistryObjectQueryType
{

    @XmlElement(name = "QueryExpressionBranch")
    protected QueryExpressionBranchType queryExpressionBranch;

    /**
     * Gets the value of the queryExpressionBranch property.
     * 
     * @return
     *     possible object is
     *     {@link QueryExpressionBranchType }
     *     
     */
    public QueryExpressionBranchType getQueryExpressionBranch() {
        return queryExpressionBranch;
    }

    /**
     * Sets the value of the queryExpressionBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryExpressionBranchType }
     *     
     */
    public void setQueryExpressionBranch(QueryExpressionBranchType value) {
        this.queryExpressionBranch = value;
    }

}
