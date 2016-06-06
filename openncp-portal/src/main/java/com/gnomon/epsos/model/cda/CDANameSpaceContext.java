package com.gnomon.epsos.model.cda;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

public class CDANameSpaceContext implements NamespaceContext {

	 public String getNamespaceURI(String prefix) {

		 if (prefix == null) throw new NullPointerException("Null prefix");
	        else if ("xsi".equals(prefix)) return "urn:hl7-org:v3";
	        else if ("xml".equals(prefix)) return XMLConstants.XML_NS_URI;
	        else if ("epsos".equals(prefix)) return "urn:epsos-org:ep:medication";
	        return XMLConstants.NULL_NS_URI;

	        
	    }

	    // This method isn't necessary for XPath processing.
	    public String getPrefix(String uri) {
	        throw new UnsupportedOperationException();
	    }

	    // This method isn't necessary for XPath processing either.
	    public Iterator getPrefixes(String uri) {
	        throw new UnsupportedOperationException();
	    }
	
}
