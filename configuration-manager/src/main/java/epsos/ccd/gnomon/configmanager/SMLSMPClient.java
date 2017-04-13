package epsos.ccd.gnomon.configmanager;

import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.dynamicdiscovery.DynamicDiscovery;
import eu.europa.ec.dynamicdiscovery.DynamicDiscoveryBuilder;
import eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator;
import eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader;
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.Endpoint;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;

public class SMLSMPClient {

	private final static Logger L = LoggerFactory.getLogger(SMLSMPClient.class);
	private final static ConcurrentHashMap<String, String> docIdentifiers = new ConcurrentHashMap<>();
	
	
	static {
		docIdentifiers.put("ITI-55",
				"urn:ehealth:PatientIdentificationAndAuthentication::XCPD::CrossGatewayPatientDiscovery##ITI-55");
	}

	private X509Certificate certificate;
	private URL address;

	public void lookup(String countryCode, String documentType, boolean b) throws SMLSMPClientException {
		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//			ks.load(null, null);
			//
			ks.load(new FileInputStream("/Users/max/Downloads/smp.jks"), "spirit".toCharArray());

			
			/*
			 * For this pilot, we do not check the smp signature
			 */
			L.debug("Instantiating the smpClient.");
			L.warn("TODO: make configurable the keystore and the SML domain");
			L.warn("TODO: check the certificate");
			DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
					.locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu"))
					.reader(new CustomizedBDXRReader(new CustomizedSignatureValidator(ks))).build();

			// DynamicDiscovery smpClient =
			// DynamicDiscoveryBuilder.newInstance()
			// .locator(new
			// DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu"))
			// .reader(new DefaultBDXRReader(new
			// CustomizedSignatureValidator(ks))).build();
			String processIdentifier = "urn:ehealth:" + countryCode + ":ncpb-idp";
			L.debug("Querying for process identifier " + processIdentifier);
			ParticipantIdentifier participantIdentifier = new ParticipantIdentifier(processIdentifier,
					"ehealth-participantid-qns");

			String finalDocumentIdentifier = docIdentifiers.get(documentType);
			L.debug("Found documentIdentifier " + finalDocumentIdentifier);
			if (finalDocumentIdentifier == null) {
				throw new SMLSMPClientException(
						"No documentidentifier found in the translation table for: " + documentType);
			}
			L.debug("Found documentIdentifier " + finalDocumentIdentifier);

			ServiceMetadata sm = smpClient.getServiceMetadata(participantIdentifier,
					new DocumentIdentifier(finalDocumentIdentifier, "ehealth-resid-qns"));

			List<Endpoint> endpoints = sm.getEndpoints();

			/*
			 * Constraint: here I think I have just one endpoint
			 */
			int size = endpoints.size();
			if (size != 1) {
				throw new Exception("Invalid number of endpoints found ("+size+"). This implementation works just with 1.");
			}
			
			Endpoint e = endpoints.get(0);
			String address = e.getAddress();
			if (address==null) {
				throw new Exception("No address found for: " + finalDocumentIdentifier + ":" + processIdentifier);
			}
			URL urlAddress = new URL(address);
			
			X509Certificate certificate = e.getCertificate();
			if (certificate == null) {
				throw new Exception("no certificate found for endpoint: " +e.getAddress());
			}
			
			setAddress(urlAddress);
			setCertificate(certificate);
//			for (int i = 0; i < endpoints.size(); i++) {
//				Endpoint e = endpoints.get(i);
//				System.out.println(e.getProcessIdentifier().getIdentifier());
//				System.out.println(e.getTransportProfile().getIdentifier());
//				System.out.println(e.getAddress());
//			}
		} catch (Exception e) {
			throw new SMLSMPClientException(e);
		}

	}

	private synchronized void setCertificate(X509Certificate certificate) {
		this.certificate = certificate;
		
	}

	private synchronized void setAddress(URL address) {
		this.address = address;
	}

	public X509Certificate getCertificate() {
		// TODO Auto-generated method stub
		return this.certificate;
	}

	public URL getEndpointReference() {
		return this.address;
	}

}
