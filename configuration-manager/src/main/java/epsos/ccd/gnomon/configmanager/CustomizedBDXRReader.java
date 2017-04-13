package epsos.ccd.gnomon.configmanager;

import javax.xml.bind.JAXBContext;

import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.core.security.AbstractSignatureValidator;

public class CustomizedBDXRReader extends DefaultBDXRReader {

	public CustomizedBDXRReader(AbstractSignatureValidator signatureValidator) {
		super(signatureValidator);
//		JAXBContextImpl a
		// TODO Auto-generated constructor stub
	}

}
