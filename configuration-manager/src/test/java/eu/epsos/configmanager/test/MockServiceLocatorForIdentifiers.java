package eu.epsos.configmanager.test;

import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.dom.Element;

import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;

public class MockServiceLocatorForIdentifiers implements IMetadataLocator {

	@Override
	public IDNSLookup getDnsLookup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI lookup(ParticipantIdentifier arg0) throws TechnicalException {
		try {
			return new URI("http://smp-digit-mock.publisher.ehealth.acc.edelivery.tech.ec.europa.eu/ehealth-actorid-qns%3A%3Aurn%3Apoland%3Ancpb");
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public URI lookup(String arg0, String arg1) throws TechnicalException {
		// TODO Auto-generated method stub
		return null;

	}

}
