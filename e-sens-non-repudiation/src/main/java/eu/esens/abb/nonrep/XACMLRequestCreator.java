package eu.esens.abb.nonrep;


import java.util.List;

import org.opensaml.xacml.ctx.ActionType;
import org.opensaml.xacml.ctx.AttributeType;
import org.opensaml.xacml.ctx.AttributeValueType;
import org.opensaml.xacml.ctx.EnvironmentType;
import org.opensaml.xacml.ctx.RequestType;
import org.opensaml.xacml.ctx.ResourceType;
import org.opensaml.xacml.ctx.SubjectType;
import org.opensaml.xacml.ctx.impl.ActionTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.AttributeTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.AttributeValueTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.EnvironmentTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.RequestTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.ResourceTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.SubjectTypeImplBuilder;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.w3c.dom.Element;



public class XACMLRequestCreator {
	static {
		try {
			org.opensaml.DefaultBootstrap.bootstrap();
		} catch (ConfigurationException e) {
			throw new IllegalStateException("Unable to bootstrap opensaml!!! Did you endorse the XML libraries?", e);
		}
	}
	private static final XMLObjectBuilderFactory bf = org.opensaml.xml.Configuration.getBuilderFactory();
	private final AttributeTypeImplBuilder atib;
	private final AttributeValueTypeImplBuilder avtib;
	private Element request;
	
	public XACMLRequestCreator(MessageType messageType, 
			List<XACMLAttributes> subjectAttributes,
			List<XACMLAttributes> resourceAttributes,
			List<XACMLAttributes> actionAttributes,
			List<XACMLAttributes> environmentAttributes) throws TOElementException {
		
		atib = (AttributeTypeImplBuilder)bf.getBuilder(AttributeType.DEFAULT_ELEMENT_NAME);
		avtib = (AttributeValueTypeImplBuilder)bf.getBuilder(AttributeValueType.DEFAULT_ELEMENT_NAME);

		RequestTypeImplBuilder rtb = (RequestTypeImplBuilder)bf.getBuilder(RequestType.DEFAULT_ELEMENT_NAME);
		RequestType request = rtb.buildObject();
	
		SubjectTypeImplBuilder stib = (SubjectTypeImplBuilder)bf.getBuilder(SubjectType.DEFAULT_ELEMENT_NAME);
		SubjectType subject = stib.buildObject();
		
		ResourceTypeImplBuilder rtib = (ResourceTypeImplBuilder)bf.getBuilder(ResourceType.DEFAULT_ELEMENT_NAME);
		ResourceType resource = rtib.buildObject();
		
		ActionTypeImplBuilder actib = (ActionTypeImplBuilder)bf.getBuilder(ActionType.DEFAULT_ELEMENT_NAME);
		ActionType action = actib.buildObject();
		
		EnvironmentTypeImplBuilder etib = (EnvironmentTypeImplBuilder)bf.getBuilder(EnvironmentType.DEFAULT_ELEMENT_NAME);
		EnvironmentType environment = etib.buildObject();
		
		request.getSubjects().add(subject);
		request.getResources().add(resource);
		request.setAction(action);
		request.setEnvironment(environment);
		
		if ( subjectAttributes!=null ) {
			int subjectSize = subjectAttributes.size();
			for ( int i=0; i<subjectSize; i++ ) {
				XACMLAttributes attributeItem = subjectAttributes.get(i);
				AttributeType attribute = atib.buildObject();
				attribute.setAttributeID(attributeItem.getIdentifier().toASCIIString());
				attribute.setDataType(attributeItem.getDataType().toASCIIString());
				
				AttributeValueType attributeValue = avtib.buildObject();
				attributeValue.setValue(attributeItem.getValue());
				attribute.getAttributeValues().add(attributeValue);
				subject.getAttributes().add(attribute);
				
			}
		}
		if ( resourceAttributes!=null ) {
			int resourceSize = resourceAttributes.size();
			for ( int i=0; i<resourceSize; i++ ) {
				XACMLAttributes attributeItem = resourceAttributes.get(i);
				AttributeType attribute = atib.buildObject();
				attribute.setAttributeID(attributeItem.getIdentifier().toASCIIString());
				attribute.setDataType(attributeItem.getDataType().toASCIIString());
				
				AttributeValueType attributeValue = avtib.buildObject();
				attributeValue.setValue(attributeItem.getValue());
				attribute.getAttributeValues().add(attributeValue);
				resource.getAttributes().add(attribute);
				
			}
		}
		
		if ( actionAttributes!= null ) {
			int actionSize = actionAttributes.size();
			for ( int i=0; i<actionSize; i++ ) {
				XACMLAttributes attributeItem = actionAttributes.get(i);
				AttributeType attribute = atib.buildObject();
				attribute.setAttributeID(attributeItem.getIdentifier().toASCIIString());
				attribute.setDataType(attributeItem.getDataType().toASCIIString());
				
				AttributeValueType attributeValue = avtib.buildObject();
				attributeValue.setValue(attributeItem.getValue());
				attribute.getAttributeValues().add(attributeValue);
				action.getAttributes().add(attribute);
				
			}
		}
		
		if ( environmentAttributes!=null ) {
			int environmentSize = environmentAttributes.size();
			for ( int i=0; i<environmentSize; i++ ) {
				XACMLAttributes attributeItem = environmentAttributes.get(i);
				AttributeType attribute = atib.buildObject();
				attribute.setAttributeID(attributeItem.getIdentifier().toASCIIString());
				attribute.setDataType(attributeItem.getDataType().toASCIIString());
				
				AttributeValueType attributeValue = avtib.buildObject();
				attributeValue.setValue(attributeItem.getValue());
				attribute.getAttributeValues().add(attributeValue);
				environment.getAttributes().add(attribute);
				
			}
		}

		
		
		
		
		
		
		
		this.request = Utilities.toElement(request);
	}
	
	public Element getRequest() {
		return this.request;
	}

}
