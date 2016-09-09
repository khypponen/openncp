package epsos.ccd.gnomon.configmanager;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import eu.epsos.configmanager.database.HibernateConfigFile;
import eu.epsos.configmanager.database.HibernateUtil;
import eu.epsos.configmanager.database.model.Property;

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
public final class ConfigurationManagerSMP {

    /** This is the Hash Map that holds the configuration entries. */
    private HashMap<String, String> configuration = new HashMap<>();

    /** The hibernate session. Here I may have problems of thread safety. */
    private org.hibernate.classic.Session session;

    /** This class is a singleton, this is the instance. */
    private static volatile ConfigurationManagerSMP instance;

    /** This is the logger. */
    private static final Logger l = Logger
            .getLogger(ConfigurationManagerSMP.class);

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
                    l.debug("Instatiating a new ConfigurationManagerSMP");
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
        l.debug("Loading the Hibernate session object");
        session = HibernateUtil.getSessionFactory().openSession();
        long end = System.currentTimeMillis();
        long total = end-start;
        l.trace("Loaded took: " + total);
        
        
        populate();
        l.debug("Constructor ends");
    }

    
    /**
     * Adds the values from the DB to the memory
     */
    private void populate() {
        l.debug("Loading all the values");
        long start = System.currentTimeMillis();
        List<Property> properties = session.createCriteria(Property.class).list();
        long end = System.currentTimeMillis();
        long total = end-start;
        l.trace("Getting all the properties took: " + total);
        int size = properties.size();
        l.debug("Adding " + size + " properties into the hashmap");

        
        start = System.currentTimeMillis();
        // Not using streams because of the mandatory requirement to use java7
        for (int i=0; i<size; i++) {
            Property property = properties.get(i);
            String name = property.getName();
            String value = property.getValue();
            configuration.put(name, value);
            l.trace("Added the couple (name, value); " + name + ":" + value);
        }
        end = System.currentTimeMillis();
        total = end-start;
        l.trace("Loading in memory the full database took: " + total);        
    }

    /**
     * Obtain a property configuration property.
     * @param key The key to search. 
     * @return The given property, if found
     * @throws PropertyNotFoundException if the property can't be found either in the hashmap, SMP, or after TSLSynchronizer
     */
    public String getProperty(String key) throws PropertyNotFoundException {
        l.debug("Searching for " + key);
        l.debug("Trying hashmap first");
        String value = configuration.get(key);
        if (value == null) {
            l.debug("Nothing found in the hashmap, let's try to SMP");
            key = query(value);
        } 
        if (value == null) {
            l.debug("Value is still null, let's run TSLSynchronizer");
            TSLSynchronizer.sync();
        }
        
        // TODO
        return null;
    }

    /**
     * What query will do. Check firstly if the value is one which is related to SMP (e.g., it 
     * is a value available in the ServiceMetadata). If not, return null. If yes, then do 
     * pack the record and do a DNS query to discover which SMP (e.g., use the SML). 
     * If the SML returns no record, return null. Else, obtain the service metadata, 
     * verify the signature (the Trust must be established by means of the eIDAS). <br/>
     * If the trust is ok, obtain all the values, store all of them in the database and in the hashmap, 
     * and return the one requested.  
     * @param key The key to be searched in the SMP
     * @return the value if known, null in all the other cases
     */
    private String query(String key) {
        
        l.debug("Checking if the key is searchable");

        /*
         * Which kind of keys we can search upon? We can search only
         * the keys that split by "." has three values, and the last is .WSE, the first the
         * country code. 
         */
        
        // The key is searchable if the isSmp bit is valued. 
//        https://openncp.atlassian.net/wiki/display/ncp/20160602+-+Meeting+minutes%2C+Thursday%2C+June+2nd%2C+2016+-+OpenNCP+Technical+Committee+Meeting
        
//        String[] explodedKey = key.split("\\.");
//        
//
//        if (explodedKey.length == 3) {
//            l.trace("The length of the key to be searched is good. ");
//            String countryCode = explodedKey[0];
//            String keyDetail = explodedKey[1];
//            l.trace("Searching for " + countryCode + ":" + keyDetail);
//            // The key to be searched is valid, and results are expected to be returned from the
//            // SMP. Let's now run the SML, according with
//            // https://joinup.ec.europa.eu/svn/cipaedelivery/tags/2.2.4/cipa-core/Implementation/java/cipa-sml-client-library/src/test/java/eu/europa/ec/cipa/sml/client/DNSRegistrationTest.java
////            run sml here
//            
//        }
        l.debug("The key is not seaerchable or it can't be found");
        return null;
        
    }

   
    
    /**
     * Get the endpoint URL for a specified country and a service name
     *
     * @param ISOCountryCode the iso country code
     * @param ServiceName the service name
     * @return
     */
    public String getServiceWSE(
            String ISOCountryCode,
            String ServiceName) {
        return getProperty(ISOCountryCode + "." + ServiceName + ".WSE");
    }
}
