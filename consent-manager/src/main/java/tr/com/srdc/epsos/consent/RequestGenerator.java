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
package tr.com.srdc.epsos.consent;

import urn.oasis.names.tc.xacml3.AttributeType;
import urn.oasis.names.tc.xacml3.AttributeValueType;
import urn.oasis.names.tc.xacml3.AttributesType;
import urn.oasis.names.tc.xacml3.RequestType;

public class RequestGenerator {

	public static RequestType createRequest(String patientId, String countryCode){
		RequestType request = new RequestType();
		
		AttributesType attributesType = new AttributesType();
		request.getAttributes().add(attributesType);
		attributesType.setCategory("urn:oasis:names:tc:xacml:1.0:subject-category:access-subject");
		
		AttributeType attribute = new AttributeType();
		attributesType.getAttribute().add(attribute);
		attribute.setAttributeId("patient-id");
		
		AttributeValueType attributeValue = new AttributeValueType();
		attribute.getAttributeValue().add(attributeValue);
		attributeValue.setDataType("http://www.w3.org/2001/XMLSchema#string");
		attributeValue.getContent().add(patientId);
		
		AttributeType attribute2 = new AttributeType();
		attributesType.getAttribute().add(attribute2);
		attribute2.setAttributeId("country-code");
		
		AttributeValueType attributeValue2 = new AttributeValueType();
		attribute2.getAttributeValue().add(attributeValue2);
		attributeValue2.setDataType("http://www.w3.org/2001/XMLSchema#string");
		attributeValue2.getContent().add(countryCode);
		
		return request;
	}
	
}
