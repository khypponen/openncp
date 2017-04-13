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
import eu.europa.ec.dynamicdiscovery.model.DocumentIdentifier;
import eu.europa.ec.dynamicdiscovery.model.Endpoint;
import eu.europa.ec.dynamicdiscovery.model.ParticipantIdentifier;
import eu.europa.ec.dynamicdiscovery.model.ServiceMetadata;

/**
 * This is the class which integrates the SMLSMP discovery client from the EU commission 
 * (DG DIGIT) to be used by the Configuration Manager. 
 * 
 * @author max
 *
 */
public class SMLSMPClient {
	
	/** The logger. */
	private final static Logger L = LoggerFactory.getLogger(SMLSMPClient.class);
	
	/** 
	 * This is a hashtable which contains the mapping between the epSOS transactions
	 * to the SMP document identifiers.  
	 */
	private final static ConcurrentHashMap<String, String> docIdentifiers = new ConcurrentHashMap<>();
	
	/**
	 * Table initializer. TODO: make it shared with the SMPEditor and finish it. 
	 */
	static {
		docIdentifiers.put("ITI-55",
				"urn:ehealth:PatientIdentificationAndAuthentication::XCPD::CrossGatewayPatientDiscovery##ITI-55");
	}
	/** The certificate of the remote endpoint. */
	private X509Certificate certificate;
	
	/** The URL address of the remote endpoint. */
	private URL address;

	/**
	 * Lookup in the SMP, using the SML for a given country code and document type. 
	 * And example query for, e.g., Portugal and XCPD is the following
	 * <pre>
	 * lookup("pt", "ITI-55");
	 * </pre>
	 * then the methods {@link #getCertificate()} and {@link #getEndpointReference()} can be used. 
	 * <b>Note</b> that this class is using a keystore to validate the signature of the SMP record, and 
	 * it should use another one to validate the signature. 
	 * <br/><br/>
	 * TODO: Make the keystores configurable. 
	 * <br/><br/>
	 * @param countryCode The country code 
	 * @param documentType The document type. Its format is given by the audit trail table and the {@link #docIdentifiers}.
	 * @throws SMLSMPClientException For any error (including not found)
	 */
	public void lookup(String countryCode, String documentType) throws SMLSMPClientException {
		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

			ks.load(new FileInputStream("/Users/max/Downloads/smp.jks"), "spirit".toCharArray());

			
			
			L.debug("Instantiating the smpClient.");
			L.warn("TODO: make configurable the keystore and the SML domain");
			
			// Instantiate the DIGIT client using our customized signature validator. This is due to 
			// the fact that we need to evaluate our signature as well. 
			DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
					.locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu"))
					.reader(new CustomizedBDXRReader(new CustomizedSignatureValidator(ks))).build();

			
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

			L.debug("Querying for service metadata");
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

		} catch (Exception e) {
			throw new SMLSMPClientException(e);
		}

	}

	/**
	 * Set the certificate of the newly discovered endpoint.
	 * @param certificate The certificate
	 */
	private synchronized void setCertificate(X509Certificate certificate) {
		this.certificate = certificate;
		
	}

	/**
	 * Set the address of the newly discovered endpoint.
	 * @param address the address as URL
	 */
	private synchronized void setAddress(URL address) {
		this.address = address;
	}

	/**
	 * After calling the {@link #lookup(String, String)}, this method returns the certificate 
	 * @return the certificate of the newly discovered endpoint
	 */
	public X509Certificate getCertificate() {
		return this.certificate;
	}

	/**
	 * After calling the {@link #lookup(String, String)}, this method returns the url 
	 * @return the url of the newly discovered endpoint
	 */
	public URL getEndpointReference() {
		return this.address;
	}

}
