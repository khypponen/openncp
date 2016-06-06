package pt.spms.epsos.ncpconfigurationutil;

import eu.epsos.configmanager.database.HibernateUtil;
import eu.epsos.configmanager.database.model.Property;
import java.io.File;
import java.util.Iterator;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main class of the OpenNCP Configuration Utility, that contain the
 * runnable methods.
 *
 */
public class ConfigurationUtil {

    /**
     * FirstTime setup properties file name
     */
    public final static String PROPERTIES_FILE = "openncp-configuration.properties";
    /**
     * Class logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationUtil.class);

    /**
     * Main class execution method
     *
     * @param args
     * @throws ConfigurationException
     */
    public static void main(String[] args) throws ConfigurationException {
        processProperties(PROPERTIES_FILE);
    }

    /**
     * This method will process all the properties of a given filename
     *
     * @param propertiesFile
     * @throws ConfigurationException
     */
    private static void processProperties(final String propertiesFile) throws ConfigurationException {
        LOGGER.info("READING CONFIGURATION FILE FROM: " + propertiesFile);

        final PropertiesConfiguration config = new PropertiesConfiguration(new File(propertiesFile));
        config.setReloadingStrategy(new FileChangedReloadingStrategy());

        final Session session = HibernateUtil.getSessionFactory().openSession();

        LOGGER.info("FILLING DATABASE WITH PROPERTIES...");

        session.beginTransaction();

        final Iterator it = config.getKeys();

        if (!it.hasNext()) {
            LOGGER.info("NO PROPERTIES FOUND! PERHAPS FILE MISSING OR EMPTY.");
        }

        while (it.hasNext()) {
            final String key = (String) it.next();
            final String value = (String) config.getString(key);

            if (value.isEmpty()) {
                LOGGER.info("SKIPPING EMPTY PROPERTY: { KEY: " + key + " }");
            } else {
                LOGGER.info("INSERTING: { KEY: " + key + ", VALUE: " + value + " }");
                final Property p = new Property(key, value);
                session.merge(p);
            }
        }

        session.getTransaction().commit();

        session.close();
    }
}
