package epsos.ccd.gnomon.configmanager;

import java.util.List;

import org.apache.log4j.Logger;

import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.Endpoint;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;

/**
 * @author max
 *
 */
public class SMLSMPClient2 {
	final private DynamicDiscovery smpClient;

	private String processIdentifier;
	
	/** Logger. */
	private final static Logger L = Logger.getLogger(SMLSMPClient.class);
	
	/** Default SML domain. */
	private final static String DEFAULT_SML_DOMAIN = "ehealth.acc.edelivery.tech.ec.europa.eu";
	
	/** This is the default prefix. It is PEPPOL. TODO: to be changed */
	private final static String SMP_PREFIX = "urn::epsos##services:extended:";
	
	public SMLSMPClient2() {
		//TODO make it configuratble, the DNS
		smpClient = DynamicDiscoveryBuilder.newInstance()
				.locator(new DefaultBDXRLocator(DEFAULT_SML_DOMAIN)).build();

	}
	public void lookup(String countryCode, String epsosAction, boolean verifySignature) throws TechnicalException {
//		.ehealth-actorid-qns.acc.edelivery.tech.ec.europa.eu
	
		L.debug("Creating the SML hostname (party identifier)");
		processIdentifier = "urn:ehealth:" + countryCode + ":ncpb-idp";

		L.debug("Caluclated process identifier " + processIdentifier);
		
		ParticipantIdentifier participantIdentifier = new ParticipantIdentifier(processIdentifier,
				"ehealth-actorid-qns");

		List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(participantIdentifier);
		
		
		for (int i=0; i<documentIdentifiers.size(); i++) {
			DocumentIdentifier documentIdentifier = documentIdentifiers.get(i);
			System.out.println(documentIdentifier.getFullIdentifier());
		}
		L.debug("Creating the epSOS action");
		String ep1 = epsosAction.replace("-", "::");
		String finalDocumentIdentifier = SMP_PREFIX + ep1.toLowerCase();

		L.debug("Final document identifier is: " + finalDocumentIdentifier);
		
		ServiceMetadata sm = smpClient.getServiceMetadata(participantIdentifier,
				new DocumentIdentifier(finalDocumentIdentifier, "epsos-docid-qns"));
		
		
		List<Endpoint> endpoints = sm.getEndpoints();
		
		for (int i=0; i<endpoints.size();i++) {
			Endpoint e = endpoints.get(i);
			System.out.println(e.getProcessIdentifier().getIdentifier());
			System.out.println(e.getTransportProfile().getIdentifier());
		}
		
	}
}
