package epsos.ccd.netsmart.securitymanager;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.security.cert.X509Certificate;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.SPMSTestKeyStoreManager;
import eu.epsos.configmanager.database.HibernateConfigFile;
import junit.framework.TestCase;

@RunWith(JUnit4.class)
public class MockCertIntegrationTest extends TestCase {

	protected static final Logger LOG = LoggerFactory.getLogger(MockCertTest.class);

	@BeforeClass
	public static void setHibernateConfig() {

		HibernateConfigFile.name = "src/test/resources/configmanager.hibernate.xml";
	}

	@Test
	public void SPMSCertTest() throws IOException {
		try {
			KeyStoreManager ksm = new SPMSTestKeyStoreManager();
			X509Certificate cert = (X509Certificate) ksm.getCertificate("ppt.ncp-signature.epsos.spms.pt");
			boolean[] ku = cert.getKeyUsage();

			CertificateValidator cv = new CertificateValidator(ksm.getTrustStore());
			cv.validateCertificate(cert);
			assertNull(ku);
		} catch (SMgrException ex) {
			LOG.error(null, ex);
			fail(ex.getMessage());
		}
	}
}
