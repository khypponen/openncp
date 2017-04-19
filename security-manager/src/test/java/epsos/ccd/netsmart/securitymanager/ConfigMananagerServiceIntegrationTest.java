package epsos.ccd.netsmart.securitymanager;

import java.io.IOException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.DefaultKeyStoreManager;
import eu.epsos.configmanager.database.HibernateConfigFile;
import junit.framework.TestCase;

@RunWith(JUnit4.class)
public class ConfigMananagerServiceIntegrationTest extends TestCase {

  private ConfigurationManagerService cm;

  @Before
  public void suite() {

	cm = ConfigurationManagerService.getInstance();
  }

  @BeforeClass
  public static void setHibernateConfig() {

	HibernateConfigFile.name = "src/test/resources/configmanager.hibernate.xml";
  }

  @Test
  public void testInititatingKeyStoreManager() {

	KeyStoreManager km;
	try {
	  km = new DefaultKeyStoreManager();
	  assertNotNull(km);
	  assertNotNull(km.getKeyStore());
	  assertNotNull(km.getTrustStore());
	} catch (IOException e) {
	  e.printStackTrace();
	  fail("Could not initialize DefaultKeyStoreManager");
	}
  }

  @Test
  public void configManTests() throws IOException {

	// Properties props = new Properties();
	// props.load(new FileInputStream(System.getProperty("SECMAN_HOME") + "/securitymanager.properties"));

	// Constants Initialization
	String KEYSTORE_LOCATION = cm.getProperty("NCP_SIG_KEYSTORE_PATH");
	System.out.println("Keystore Location: " + KEYSTORE_LOCATION);

	String TRUSTSTORE_LOCATION = cm.getProperty("TRUSTSTORE_PATH");
	System.out.println("Truststore Location:" + TRUSTSTORE_LOCATION);

	String KEYSTORE_PASSWORD = cm.getProperty("NCP_SIG_KEYSTORE_PASSWORD");
	String TRUSTSTORE_PASSWORD = cm.getProperty("TRUSTSTORE_PASSWORD");

	String PRIVATEKEY_ALIAS = cm.getProperty("NCP_SIG_PRIVATEKEY_ALIAS");
	String PRIVATEKEY_PASSWORD = cm.getProperty("NCP_SIG_PRIVATEKEY_PASSWORD");

	// String ERROR_PARAM = cm.getProperty("ERRORRRR");

	assertNotNull(KEYSTORE_LOCATION);
	assertNotNull(TRUSTSTORE_LOCATION);

	assertNotNull(KEYSTORE_PASSWORD);
	assertNotNull(TRUSTSTORE_PASSWORD);

	assertNotNull(PRIVATEKEY_ALIAS);
	assertNotNull(PRIVATEKEY_PASSWORD);

	// The expected behavior is to return a not-null empty string (length==0);
	// assertNotNull(ERROR_PARAM);
	// assert (ERROR_PARAM.length() == 0);
  }
}
