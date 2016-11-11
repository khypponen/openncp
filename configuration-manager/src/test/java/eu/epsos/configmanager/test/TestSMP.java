package eu.epsos.configmanager.test;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.net.URLCodec;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.bouncycastle.util.encoders.Base64;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import com.google.common.io.BaseEncoding;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerSMP;
import epsos.ccd.gnomon.configmanager.SMLSMPClient;
import epsos.ccd.gnomon.configmanager.SMLSMPClientException;
import eu.epsos.configmanager.database.HibernateConfigFile;

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
	 * 
	 */
	@Test
	public void testClass() throws SMLSMPClientException, CertificateException, MalformedURLException, IOException, TransformerException {
		
		SMLSMPClient client = new SMLSMPClient();
		client.lookup("at", "epSOS-21", false);
		assertNotNull(client.getCertificate());
		assertNotNull(client.getEndpointReference());
	
		
	}
	
	
	
	@Test
	public void testLookup() throws NoSuchAlgorithmException, UnsupportedEncodingException {
	
		String pid = "urn:ehealth:pt:ncpb-idp"; // TODO: this is wrong. It must
												// be urn:ehealth:pt::ncpb.idp
		String did = "urn::epsos##services:extended:epsos::21"; // TODO: this is
																// wrong, this
																// is peppol. It
																// must be
																// "epsos-docid-qns::urn:epsos:services##epsos-21"

		MessageDigest md = MessageDigest.getInstance("SHA-256"); // TODO: this
																	// is not
																	// BDXL, but
																	// e-SENS
		byte[] back = md.digest(pid.getBytes("UTF-8"));
		String value = BaseEncoding.base32().encode(back) + ".ehealth-actorid-qns.acc.edelivery.tech.ec.europa.eu"; // TODO,
																													// make
																													// it
																													// configurable
		value = value.replace("=", ""); // see
										// http://wiki.ds.unipi.gr/display/ESENS/PR+-+BDXL

		// TODO: add to the spec the link to the PR BDXL. But first check it
		// with adrien THIS IS NOT BDXL.

		try {
			Lookup l = new Lookup(value, Type.NAPTR);

			final Record[] records = l.run();
			if (l.getResult() == Lookup.SUCCESSFUL) {
				int size = records.length;

				if (size != 1) {
					throw new RuntimeException("Invalid count of NAPTR record returned from the flow");
				}

				Record rec = records[0];

				String t = rec.rdataToString();
				/*
				 * The string is 100 10 "U" "Meta:SMP"
				 * "!^.*$!http://EHEALTH-TEST-UPRC.publisher.acc.edelivery.tech.ec.europa.eu!"
				 * .
				 */
				String[] t1 = t.split(" "); // Here we should implement the
											// grammar of rfc4848
				if (t1.length != 6) {
					throw new RuntimeException("I'm confused!");
				}
				String metaSMP = t1[3];
				String url = t1[4];

				if (!metaSMP.equals("\"Meta:SMP\"")) {
					throw new RuntimeException("I'm confused. The tag " + metaSMP + " can't be recognized");
				}
				String url1 = url.substring(url.indexOf("http"), url.length() - 2); // the
																					// minus
																					// 2
																					// is
																					// because
																					// of
																					// the
																					// trailing
																					// !",
																					// which
																					// I
																					// don't
																					// know
																					// what
																					// they
																					// are
				System.out.println(url1);
				/*
				 * HERE SML ENDS, BEGIN SMP
				 */
				// Get the service group,
				// http://ehealth-test-uprc.publisher.acc.edelivery.tech.ec.europa.eu/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp

				String sgName = url1 + "/ehealth-actorid-qns" + new URLCodec().encode("::" + pid); // TODO
																									// WHERE
																									// THIS
																									// IS
																									// DEFINED?
				System.out.println(sgName);

				getServicegroup(sgName, did);

			} else {
				System.out.println(l.getErrorString());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Document getFromHTTP(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(httpGet);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the
		// network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally
		// clause.
		// Please note that if response content is not fully consumed the
		// underlying
		// connection cannot be safely re-used and will be shut down and
		// discarded
		// by the connection manager.
		try {
			System.out.println(response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document doc = dbf.newDocumentBuilder().parse(entity1.getContent());

			EntityUtils.consume(entity1);
			return doc;

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				response1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void getServicegroup(String sgName, String did) throws Exception {
		// get the sg, then each file get it
		Document serviceGroup = getFromHTTP(sgName);

		NodeList sgList = serviceGroup.getElementsByTagNameNS("http://busdox.org/serviceMetadata/publishing/1.0/",
				"ServiceMetadataReference");
		if (sgList == null) {
			throw new RuntimeException("Unable to get the service group xml");

		}

		for (int i = 0; i < sgList.getLength(); i++) {
			Node n = sgList.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				URL url = new URL(((Element) n).getAttribute("href"));
				// Ok, here I split by "/" and I get the last.
				String path = url.getPath();
				String[] p_el = path.split("/");
				String toSplit = p_el[p_el.length - 1];
				String serviceName = new URLCodec().decode(toSplit.substring(
						toSplit.indexOf("ehealth-resid-qns") + "ehealth-resid-qns".length(), toSplit.length()));
				// TODO: the document identifiers here are following PEPPOL. for
				// this testing we will allow PEPPOL, to be changed.
				/*
				 * Check if it contains the string
				 */

				if (serviceName.contains(did)) {
					System.out.println("Found the service which I'm looking for. Fetching the document");
					getSignedServiceInformation(url);
					break;
				}

			}
		}
	}

	private void getSignedServiceInformation(URL url) throws Exception {
		Document signedServiceInfo = getFromHTTP(url.toExternalForm());
		// Document beforeTheFix = signedServiceInfo;

		// Apply Joao fix because of the fact that the SMP record is buggy since
		// it is modified by the SMP
		// SmpXslt.removeSmpSignature(beforeTheFix);
		//
		// Document tbValidated = SmpXslt.applyXsltToSmp(beforeTheFix);
		// final String ns =
		// "http://busdox.org/serviceMetadata/publishing/1.0/";
		// SmpXslt.validateXml(tbValidated, ns, "ServiceInformation");

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(signedServiceInfo), new StreamResult(System.out));
		
		// Here we should go with the jaxb, but for now I will not go, since the 
		// schema is not yet defined (peppol, bdxl, the namespace changes. 
		
		NodeList nl = signedServiceInfo.getElementsByTagNameNS(ns, "Process");
		if (nl == null) {
			throw new Exception("Wrong xml found, no process available");
		}

		URL endpointReference = null;
		X509Certificate cert = null;
		
		Node processNode = nl.item(0);
		NodeList nll = processNode.getChildNodes();
		int size = nll.getLength();
		
		for (int i=0; i<size; i++) {
			Node n = nll.item(i); // process
			
			if (n.getNodeType()==Node.ELEMENT_NODE) {
				String localName = n.getLocalName();
				if (localName.equals("ServiceEndpointList")) {
					Element sel = (Element)n;
					NodeList endpoint = sel.getChildNodes();
					int size1 = endpoint.getLength();
					for (int j=0; j<size1; j++) {
						Node localName1 = endpoint.item(j);
						String localName11 = localName1.getLocalName();
						if (localName11 != null && localName11.equals("Endpoint")) {
							Element endpointEl = (Element)localName1;
							NodeList nl2 = endpointEl.getElementsByTagNameNS("http://www.w3.org/2005/08/addressing", 
									"Address");
							if (nl2 == null || nl2.getLength() == 0) {
								throw new Exception("Unable to guess the Address");
							}
							Element address = (Element)nl2.item(0);
							endpointReference = new URL(address.getTextContent());
							NodeList nl3 = endpointEl.getElementsByTagNameNS(ns, "Certificate");
							if (nl3 == null || nl3.getLength() == 0) {
								throw new Exception("Unable to guess the Certificate");
							}
							Element certificate = (Element)nl3.item(0);
							String tc = certificate.getTextContent();
							byte[] tc1 = Base64.decode(tc);
							cert = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(tc1));
							break;
						}
							
					}
					
				}
			}
		}
		
		
	}

}
