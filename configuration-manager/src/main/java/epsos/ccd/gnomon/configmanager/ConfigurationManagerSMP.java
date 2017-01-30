package epsos.ccd.gnomon.configmanager;

import eu.epsos.configmanager.database.HibernateUtil;
import eu.epsos.configmanager.database.model.Property;
import org.hibernate.PropertyNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;

/**
 * ConfigurationManagerSMP. Obtain a configuration value. Usage: <br/>
 *
 * <pre>
 * ConfigurationManagerSMP.getInstance().getProperty(sampleKey);
 * </pre>
 *
 * This class uses <a href=
 * "http://docs.oasis-open.org/bdxr/bdx-smp/v1.0/cs01/bdx-smp-v1.0-cs01.html">BDXR-SMP</a>
 * and <a href=
 * "http://docs.oasis-open.org/bdxr/BDX-Location/v1.0/BDX-Location-v1.0.html">BDX-Location</a>
 * standards to obtain the configuration of remote NCPs. A detailed discussion
 * on the implementation strategy of this class can be found <a href=
 * "https://openncp.atlassian.net/wiki/display/ncp/Cache+implementation+through+ConfigurationManager+refactoring">here</a>.
 * <br/>
 * The flow is as follows.
 * <ol>
 * <li>During the startup, the property database is read and added into an
 * HashMap</li>
 * <li>When a third party needs to obtain a key, it calls the
 * {@link #getProperty(String)}. The property is obtained as
 * <ul>
 * <li>from the hashmap, and if not present</li>
 * <li>from the SMP, through a Query, and if not present</li>
 * <li>Run TSLSynchronizer, and if not present</li>
 * <li>Throw error</li>
 * </ul>
 * <li>When there is a system failure of a numerable set of exceptions (e.g.,
 * SSLPeerUnverified), the SMP query is performed</li>
 * </ol>
 * One point of intervention would be to add a TTL to a value in the hashmap.
 *
 * @author massimiliano.masi@bmg.gv.at
 *
 */
public final class ConfigurationManagerSMP implements ConfigurationManagerInt {

	/** This is the Hash Map that holds the configuration entries. */
	private HashMap<String, PropertySearchableContainer> configuration = new HashMap<>();

	/** The hibernate session. Here I may have problems of thread safety. */
	private org.hibernate.classic.Session session;

	/** This class is a singleton, this is the instance. */
	private static volatile ConfigurationManagerSMP instance;

	/** This is the logger. */
	private static final Logger l = LoggerFactory.getLogger(ConfigurationManagerSMP.class);

	private static final HashMap<String, ServiceProcessItem> mapMap = new HashMap<>();

	static {
		mapMap.put("ConsentService",
				new ServiceProcessItem(new String[] { "epsosConsentService::Put", "epsosConsentService::Discard" },
						new String[] { "epsos-51", "epsos-52" }, null));
		mapMap.put("OrderService", new ServiceProcessItem(new String[] { "epsosOrderService::List" },
				new String[] { "epsos-31" }, null));
		mapMap.put("PatientIdentificationService",
				new ServiceProcessItem(new String[] { "epsosIdentityService::FindIdentityByTraits" },
						new String[] { "epsos-11" }, null));
		mapMap.put("PatientService", new ServiceProcessItem(new String[] { "epsosPatientService::List" },
				new String[] { "epsos-21" }, null));
		mapMap.put("DispensationService",
				new ServiceProcessItem(
						new String[] { "epsosDispensationService::Initialize", "epsosDispensationService::Discard" },
						new String[] { "epsos-41", "epsos-42" }, null));
		mapMap.put("VPNGateway", new ServiceProcessItem(new String[] { "urn:ehealth:ncp:vpngateway" },
				new String[] { "epsos-91" }, null)); // not
		// sure
	}

	/** The hibernate session. */

	/**
	 * Get an instance of the ConfigurationManagerSMP.
	 * 
	 * @return an instance of the ConfigurationManagerSMP class.
	 */
	public static ConfigurationManagerSMP getInstance() {
		if (instance == null) {
			synchronized (ConfigurationManagerSMP.class) {
				if (instance == null) {
					l.info("Instatiating a new ConfigurationManagerSMP");
					instance = new ConfigurationManagerSMP();
				}
			}
		}
		return instance;
	}

	/**
	 * Constructor. Here I have to load all the properties from the database.
	 */
	private ConfigurationManagerSMP() {

		long start = System.currentTimeMillis();
		l.info("Loading the Hibernate session object");
		session = HibernateUtil.getSessionFactory().openSession();
		long end = System.currentTimeMillis();
		long total = end - start;
		l.info("Loaded took: " + total);

		populate();
		l.info("Constructor ends");
	}

	/**
	 * Adds the values from the DB to the memory
	 */
	private void populate() {
		l.info("Loading all the values");
		long start = System.currentTimeMillis();
		
		@SuppressWarnings("unchecked")
		List<Property> properties = session.createCriteria(Property.class).list();
		
		long end = System.currentTimeMillis();
		long total = end - start;
		l.info("Getting all the properties took: " + total);
		int size = properties.size();
		l.info("Adding " + size + " properties into the hashmap");

		start = System.currentTimeMillis();
		// Not using streams because of the mandatory requirement to use java7
		for (int i = 0; i < size; i++) {
			Property property = properties.get(i);
			String name = property.getName();
			String value = property.getValue();
			PropertySearchableContainer psc = new PropertySearchableContainer();
			psc.setValue(value);
//			psc.setSearchable(property.isSMP());
			configuration.put(name, psc);
			l.debug("Added the couple (name, value); " + name + ":" + value);
		}
		end = System.currentTimeMillis();
		total = end - start;
		l.info("Loading in memory the full database took: " + total);
	}

	/**
	 * Obtain a property configuration property.
	 * 
	 * @param key
	 *            The key to search.
	 * @return The given property, if found
	 * @throws PropertyNotFoundException
	 *             if the property can't be found either in the hashmap, SMP, or
	 *             after TSLSynchronizer
	 */
	public String getProperty(String key) throws PropertyNotFoundException {
		l.info("Searching for " + key);
		l.info("Trying hashmap first");
		PropertySearchableContainer psc = configuration.get(key);
		
		// Ok, here two things: one is that the entry does not exist, the second is that it is not 
		// Searchable. So, if it does not exist, we try SMP anyway. If it exists, then we use it. 
		// To update we remove first and we re-add it.
		if (psc == null) {
			l.info("Nothing found in the hashmap, let's try to SMP");
				String value = query(key);
				if (value != null) {
					PropertySearchableContainer psc1 = new PropertySearchableContainer();
					psc1.setSearchable(true);
					psc1.setValue(value);
					configuration.put(key, psc1);
					psc = psc1;
				}
		}
		if (psc == null) {
			l.info("Value is still null, let's run TSLSynchronizer");
			// TSLSynchronizer.sync();
		}
		if (psc != null) {
			l.info("Returning the value: " + psc.getValue());
			return psc.getValue();
		}

		// TODO
		return null;
	}

	/**
	 * What query will do. Check firstly if the value is one which is related to
	 * SMP (e.g., it is a value available in the ServiceMetadata). If not,
	 * return null. If yes, then do pack the record and do a DNS query to
	 * discover which SMP (e.g., use the SML). If the SML returns no record,
	 * return null. Else, obtain the service metadata, verify the signature (the
	 * Trust must be established by means of the eIDAS). <br/>
	 * If the trust is ok, obtain all the values, store all of them in the
	 * database and in the hashmap, and return the one requested.
	 * 
	 * @param key
	 *            The key to be searched in the SMP
	 * @return the value if known, null in all the other cases
	 */
	private String query(String key) {

		/*
		 * Participant identifier is: urn:ehealth:lu:ncpb-idp document
		 * identifier is relatd to the transaction
		 * epsos-docid-qns::urn:epsos:services##epsos-21.
		 * 
		 * How the participant identifier is calculated: we split the string in
		 * three (must be three). The first is the country, the second is the to
		 * be mapped into the transaction
		 */

		String[] values = key.split("\\.");
		if (values == null || values.length != 3) {
			throw new RuntimeException("The key to be selected in SMP has a length which is not allowed");
		}

		String countryCode = values[0];
		l.debug("Found country code: " + countryCode);
		ServiceProcessItem spi = map(values[1]);
		String documentType = spi.getEventNumber()[0];
		l.debug("Found documentType" + documentType);
		String endpoints = spi.getEventName()[0];
		l.debug("Searching endpoint: " + endpoints);
		SMLSMPClient client = new SMLSMPClient();
		try {
			l.debug("Doing SML/SMP");
			client.lookup(countryCode, documentType, false);
			l.debug("Founda values!!!!");
			/*
			 * What to do with the property? One is to return to the caller the endpoint, 
			 * the second is to put it into the certificate
			 */
			X509Certificate cert = client.getCertificate();
			if (cert != null) {
				l.debug("Storing certificate in truststore");
//				String subject = cert.getSubjectDN().getName();
//				String value = Base64.encodeBase64String(MessageDigest.getInstance("MD5").digest(subject.getBytes()));
//				TSLUtils.storeCertificateToTrustStore(cert, value);
			}
			URL endpoint = client.getEndpointReference();
			if (endpoint != null) {
				return endpoint.toString();
			} else {
				return null;
			}
		} catch (SMLSMPClientException e) {
			l.error("SMP/SML Exception",e);
			return null;
		}



	}

	private ServiceProcessItem map(String value) {
		return mapMap.get(value); // always return the first, is it ok?????
									// we assume they have the same
									// cetificate for both services
	}

	/**
	 * Get the endpoint URL for a specified country and a service name
	 *
	 * @param ISOCountryCode
	 *            the iso country code
	 * @param ServiceName
	 *            the service name
	 * @return
	 */
	public String getServiceWSE(String ISOCountryCode, String ServiceName) {
		return getProperty(ISOCountryCode + "." + ServiceName + ".WSE");
	}

	@Override
	public void setServiceWSE(String ISOCountryCode, String ServiceName, String URL) {
		throw new IllegalArgumentException("Unable to set WSE: wrong concept in SMP");
	}

	@Override
	public String updateProperty(String key, String value) {
		OLDConfigurationManagerDb.getInstance().updateProperty(key, value);
		return value;
	}

}
