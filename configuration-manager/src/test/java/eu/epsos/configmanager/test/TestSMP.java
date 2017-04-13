package eu.epsos.configmanager.test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.cert.CertificateException;

import javax.xml.transform.TransformerException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.BeforeClass;
import org.junit.Test;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerSMP;
import epsos.ccd.gnomon.configmanager.SMLSMPClient;
import epsos.ccd.gnomon.configmanager.SMLSMPClientException;
import eu.epsos.configmanager.database.HibernateConfigFile;
import eu.europa.ec.dynamicdiscovery.exception.TechnicalException;

public class TestSMP {
	private static final String ns = "http://busdox.org/serviceMetadata/publishing/1.0/";

	@BeforeClass
	public static void setup() {
		Logger rootLogger = Logger.getRootLogger();
		if (!rootLogger.getAllAppenders().hasMoreElements()) {
			rootLogger.setLevel(Level.OFF);

			Logger hornetLogger = rootLogger.getLoggerRepository().getLogger("org.hornetq.core.server");
			hornetLogger.setLevel(Level.OFF);
			hornetLogger.addAppender(new ConsoleAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN)));

		}
	}

	@Test
	public void testNormalFlow() {
		HibernateConfigFile.name = "src/test/resources/massi.hibernate.xml";
		String endpoint = ConfigurationManagerSMP.getInstance().getProperty("za.PatientIdentificationService.WSE");
		assertNotNull(endpoint); //
	}

	/**
	 * @throws SMLSMPClientException
	 * @throws TransformerException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws CertificateException
	 * @throws TechnicalException
	 * 
	 */
	@Test
	public void testClass() throws SMLSMPClientException, CertificateException, MalformedURLException, IOException,
			TransformerException, TechnicalException {
		try {
			SMLSMPClient client = new SMLSMPClient();
			client.lookup("pt", "ITI-55");
			 assertNotNull(client.getCertificate());
			 assertNotNull(client.getEndpointReference());

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	


}
