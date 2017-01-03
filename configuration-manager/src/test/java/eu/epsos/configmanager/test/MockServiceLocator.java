package eu.epsos.configmanager.test;

import java.net.URI;
import java.net.URISyntaxException;

import eu.europa.ec.dynamicdiscovery.core.locator.IMetadataLocator;
import eu.europa.ec.dynamicdiscovery.core.locator.dns.IDNSLookup;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;

public class MockServiceLocator implements IMetadataLocator {

	@Override
	public IDNSLookup getDnsLookup() {
		System.out.println("HERE, but returning null");
		return null;
	}

	@Override
	public URI lookup(ParticipantIdentifier arg0) throws TechnicalException {
		System.out.println("In lookup, but returning null");
		try {
			return new URI("http://smp-digit-mock.publisher.ehealth.acc.edelivery.tech.ec.europa.eu/ehealth-actorid-qns::urn:poland:ncpb/services/epsos-docid-qns%3A%3Aurn%3A%3Aepsos%3Aservices%23%23epsos-21");
		} catch (URISyntaxException e) {
			throw new TechnicalException(e.getMessage()) {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				
			};
		}
	}

	@Override
	public URI lookup(String arg0, String arg1) throws TechnicalException {
		System.out.println("Here in lookup2, but returning null");
		return null;
	}

}
