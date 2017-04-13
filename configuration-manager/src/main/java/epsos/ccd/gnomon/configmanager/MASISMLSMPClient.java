package epsos.ccd.gnomon.configmanager;

import com.google.common.io.BaseEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is used to implement the SML / SMP client in the OpenNCP. <br/>
 * <br/>
 * The goal of this class is to implement a simple SML / SMP client used by the
 * configuration manager in order to retrieve the capabilities of a remote
 * endpoint (NPCeH). It performs first an SML query, and then an SMP query.
 * <br/>
 *
 * <h3>The SML Query</h3> The SML query is a DNS query looking for the NAPTR
 * record type, according with rfc4848. The NAPTR will return the link to the
 * specific SMP server which contains the ServiceGroup, a list of entries for
 * each service.
 *
 *
 * <h3>The SMP Query</h3> The SMP query is a HTTP GET towards the given SML URL,
 * to retrieve the service group. After that, each service is again fetched
 * using HTTP GET. Then the Signed Information is retrieved. Here two signatures
 * are validated: the first is the signature of the SMP (which assures the SMP
 * record to be genuine, from a trusted SMP), and the second is the signature of
 * the National Infrastructure, to enforce the epSOS Brokered Trust model.
 *
 * <h3>Relevant specifications</h3>
 * <ul>
 * <li>Service Metadata Lookup:
 *
 * @see <a href=
 *      "http://docs.oasis-open.org/bdxr/BDX-Location/v1.0/BDX-Location-v1.0.html">
 *      SML</a>
 *      <li>Service Metadata Publishing:
 * @see <a href=
 *      "http://docs.oasis-open.org/bdxr/bdx-smp/v1.0/cs01/bdx-smp-v1.0-cs01.html">
 *      SMP</a>
 *      <li>rfc 4848</li>
 *      <li>e-SENS Implementing guideline:
 * @see <a href="http://wiki.ds.unipi.gr/display/ESENS/PR+-+BDXL">PR BDXL</a>
 *      </li>
 *
 *      </ul>
 *
 *      <b>It is worth noticing that this class is in constant evolution, as the
 *      available server implementations moves from PEPPOL SMP to OASIS</b>
 *      <br/>
 *      <br/>
 *      Due to licensing problems, the available SML / SMP clients can't be
 *      used. They are usually licensed under EUPL or MPL, which clashes with
 *      the OpenNCP licensing. <br/>
 * @deprecated because of the usage of SMLSMPClient wrapping DIGIT implementation (@see SMLSMPClient.java)
 * @author max
 *
 */
@Deprecated
public class MASISMLSMPClient {

	/** This contains that message digester, used to create the lookup. */
	private MessageDigest md = null;

	/**
	 * This is the SML domain. It tells which DNS to use, that will have the
	 * NAPTR.
	 */
	private String smlDomain;

	/**
	 * This is the SML process identifier. It is a field, since it is shared by
	 * the methods.
	 */
	private String processIdentifier;

	/** This is the endpoint of the action requested, obtained from the SMP- */
	private URL endpointReference;

	/**
	 * This is the certificate of the action requested, obtained from the SMP.
	 */
	private X509Certificate cert;
	private final static ConcurrentHashMap<String, String> docIdentifiers = new ConcurrentHashMap<>();

	static {
		docIdentifiers.put("ITI-55",
				"urn:ehealth:PatientIdentificationAndAuthentication::XCPD::CrossGatewayPatientDiscovery##ITI-55");
	}
	
	/**
	 * This is the default SML domain. If nothing is configured, then we fall
	 * back to this.
	 */
//	private final static String DEFAULT_SML_DOMAIN = ".ehealth-actorid-qns.acc.edelivery.tech.ec.europa.eu";
	private final static String DEFAULT_SML_DOMAIN =".ehealth-participantid-qns.ehealth.acc.edelivery.tech.ec.europa.eu";
	/** Logger. */
	private final static Logger L = LoggerFactory.getLogger(MASISMLSMPClient.class);

	/** This is the default prefix. It is PEPPOL. TODO: to be changed */
	private final static String SMP_PREFIX = "urn::epsos##services:extended:";

	/**
	 * After performed the lookup, this is the remote endpoint.
	 *
	 * @see {@link #lookup(String, String, boolean)}
	 * @return the endpoint
	 */
	public URL getEndpointReference() {
		return endpointReference;
	}

	/**
	 * After performed the lookup, this is the certificate
	 *
	 * @see {@link #lookup(String, String, boolean)}
	 *
	 * @return the certificate of the endpoint
	 *
	 */
	public X509Certificate getCertificate() {
		return this.cert;
	}

	/**
	 * Constructor.
	 */
	public MASISMLSMPClient() {
		try {
			L.debug("In SMPSMLClient constructor");
			md = MessageDigest.getInstance("SHA-256");

		} catch (NoSuchAlgorithmException e) {
			L.error("Uh, message digest not found?", e);
			throw new IllegalStateException("Unable to instantiate the message digest, ", e);
		}
	}

	/**
	 * Performs the SML / SMP lookup flow. The results obtained from the SMP
	 * query can be obtained by using the get/set methods.
	 *
	 * @param countryCode
	 *            The two digits code of the country which we're searching for
	 *            capabilities. Example: "pt", "at".
	 * @param epsosAction
	 *            The action for which we need to obtain capabilities. For
	 *            instance, "epSOS-21"
	 * @param verifySignature
	 *            true if all the signatures must be validated.
	 * @throws SMLSMPClientException
	 *             If the lookup is wrong.
	 */
	public void lookup(String countryCode, String epsosAction, boolean verifySignature) throws SMLSMPClientException {
		L.debug("In lookup");
		try {
			long start = System.currentTimeMillis();
			L.debug("SML begins ...");
			String hostToLookup = prepareSmlQuery(countryCode);
			String serviceGroupUrl = doDNSQuery(hostToLookup);
			long end = System.currentTimeMillis();
			long total = end - start;
			L.debug("SML ends. Took " + total + " msec");
			L.debug("SMP begins");
			start = System.currentTimeMillis();
			URL signedInfoURL = getServiceGroup(serviceGroupUrl, epsosAction);
			getSignedServiceInformation(signedInfoURL);
			end = System.currentTimeMillis();
			total = end - start;
			L.debug("SMP ends. Took " + total + " msec");
		} catch (TransformerException | UnsupportedEncodingException | DecoderException | TextParseException
				| EncoderException e) {
			L.error("Should never been thrown. We're OpenNCP, we don't run in a device" + e);
			throw new SMLSMPClientException(e);
		} catch (IOException e) {
			L.error("I/O Exception. The SMP SML service is down: " + e.getMessage(), e);
			throw new SMLSMPClientException(e);
		} catch (CertificateException e) {
			L.error("A certificate exception occurred. The certificate for the party " + countryCode + " is corrupted",
					e);
			throw new SMLSMPClientException(e);
		}
	}

	/**
	 * This method obtains the ServiceGroup from the given URL, and returns the
	 * SignedServiceInformation file containing the values to be retrieved.
	 *
	 * @param serviceGroupUrl
	 *            The url of the service group.
	 * @param epSOSaction
	 *            the epsos action for which we search data.
	 * @return The signed service information url.
	 * @throws IOException
	 *             If the connection is down!
	 * @throws DecoderException
	 *             If the URL is not properl formed
	 * @throws SMLSMPClientException
	 *             If no servicegroup can't be found
	 */
	private URL getServiceGroup(String serviceGroupUrl, String epSOSaction)
			throws IOException, DecoderException, SMLSMPClientException {
		L.debug("Creating the epSOS action");
//		String ep1 = epSOSaction.replace("-", "::");
		String ep1 = epSOSaction;
		
		
//		String finalDocumentIdentifier = SMP_PREFIX + ep1.toLowerCase();
		
		String finalDocumentIdentifier = docIdentifiers.get(ep1);

		L.debug("Found documentIdentifier " + finalDocumentIdentifier);
		if (finalDocumentIdentifier == null) {
			throw new SMLSMPClientException(
					"No documentidentifier found in the translation table for: " + ep1);
		}
		L.debug("Found documentIdentifier " + finalDocumentIdentifier);

		
		
		
		
		L.debug("Final document identifier is: " + finalDocumentIdentifier);

		L.debug("Obtaining the Service Group");
		// get the sg, then each file get it
		Document serviceGroup = getFromHTTP(serviceGroupUrl);

		// TODO: implement with Jaxb, as soon as we all switch to DICOM
		NodeList sgList = serviceGroup.getElementsByTagNameNS("http://docs.oasis-open.org/bdxr/ns/SMP/2016/05",
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

				if (serviceName.contains(finalDocumentIdentifier)) {
					L.info("Found the service which I'm looking for. Fetching the document");
					return url;
				}
			}
		}
		throw new SMLSMPClientException("No service group can be found");
	}
	public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}
	/**
	 * Obtain a Document from HTTP. This code is coming from an example of
	 * apache httpclient. TODO: here we can improve a lot
	 *
	 * @param url
	 *            The url of the DOM.
	 * @return the Document returned from the GET.
	 * @throws IOException
	 */
	private Document getFromHTTP(String url) throws IOException {
		L.debug("Doing HTTP to " + url);

		// TODO HTTP
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(httpGet);
		} catch (IOException e) {

			e.printStackTrace();
			L.error("Unable to get the SMP record", e);
			throw e;
		}

		try {
			L.debug("HTTP Response: " + response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();

			L.debug("Building the document");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document doc = dbf.newDocumentBuilder().parse(entity1.getContent());
			printDocument(doc, System.out);
			EntityUtils.consume(entity1);
			return doc;

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				response1.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * This is SML. This is the DNS Query to obtain the NAPTR record pointing to
	 * the service group.
	 *
	 * @param hostToLookup
	 *            The hostname ready for SML.
	 * @return The URL of the service group.
	 * @throws EncoderException
	 *             Should never been thrown.
	 * @throws TextParseException
	 *             Should never been thrown.
	 */
	private String doDNSQuery(String hostToLookup) throws EncoderException, TextParseException {
		L.debug("In lookup. Using ONLY configured DNS. Looking up: " + hostToLookup);
		Lookup l = new Lookup(hostToLookup, Type.NAPTR);

		L.debug("Executing ...");
		final Record[] records = l.run();
		if (l.getResult() == Lookup.SUCCESSFUL) {
			L.debug("Succesfull DNS query, looking if there are some results");
			int size = records.length;
			L.debug("Found " + size + " records. It should be just 1");
			if (size != 1) {
				throw new RuntimeException("Invalid count of NAPTR record returned from the flow");
			}

			Record rec = records[0];

			String t = rec.rdataToString();
			L.debug("Obtained RAW data: " + t);
			/*
			 * The string is 100 10 "U" "Meta:SMP"
			 * "!^.*$!http://EHEALTH-TEST-UPRC.publisher.acc.edelivery.tech.ec.europa.eu!"
			 * .
			 */

			// TODO: implement the grammar of rfc4848 with APARSE.
			L.debug("Buggy things happen here... ");
			String[] t1 = t.split(" ");

			if (t1.length != 6) {
				throw new RuntimeException(
						"I'm confused! The SML record should be only 6 elements after the split with namespace");
			}
			String metaSMP = t1[3];
			String url = t1[4];

			if (!metaSMP.equals("\"Meta:SMP\"")) {
				throw new RuntimeException("I'm confused. The tag " + metaSMP + " can't be recognized");
			}

			// the minus 2 is because of the trailing !", and I don't know what
			// it is.
			String url1 = url.substring(url.indexOf("http"), url.length() - 2);
			L.debug("The full record is: " + url1);
			/*
			 * HERE SML ENDS, BEGIN SMP
			 */
			// Get the service group,
			// http://ehealth-test-uprc.publisher.acc.edelivery.tech.ec.europa.eu/ehealth-actorid-qns%3A%3Aurn%3Aehealth%3Apt%3Ancpb-idp

			// TODO: how this sgName is defined?
			String sgName = url1 + "/ehealth-participantid-qns" + new URLCodec().encode("::" + processIdentifier);

			L.debug("Calculated Service Group Name: " + sgName);
			return sgName;

		} else {
			L.error("DNS query failed: " + l.getErrorString());
			throw new RuntimeException("DNS Query Failed");
		}
	}

	/**
	 * Set the SML domain. This value will be appended to the calculated hash.
	 * This should be the DNS for which the SMP has fed the data.
	 *
	 * @param domain
	 *            The DNS domain. It MUST be preceded with a dot ".". Example
	 *            is: .ehealth-actorid-qns.acc.edelivery.tech.ec.europa.eu
	 */
	public void setSMLDomain(String domain) {
		this.smlDomain = domain;
	}

	/**
	 * Creates the SML record according with
	 * http://wiki.ds.unipi.gr/display/ESENS/PR+-+BDXL.
	 * <ol>
	 * <li>The party identifier is processed using the SHA256 algorithm</li>
	 * <li>The digest obtained in (1) is BASE32 encoded</li>
	 * <li>Any trailing '=' padding characters added in (2) in the encoded
	 * digest are removed</li>
	 * </ol>
	 *
	 * @param countryCode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String prepareSmlQuery(String countryCode) throws UnsupportedEncodingException {

		L.debug("Creating the SML hostname (party identifier)");
		processIdentifier = "urn:ehealth:" + countryCode + ":ncpb-idp";

		L.debug("Caluclated process identifier " + processIdentifier);

		byte[] back = md.digest(processIdentifier.getBytes("UTF-8"));

		if (this.smlDomain == null) {
			this.smlDomain = DEFAULT_SML_DOMAIN;
		}

		L.debug("Encoding base32");
		String value = BaseEncoding.base32().encode(back) + smlDomain;

		L.debug("Removing trailing =");
		value = value.replace("=", "");

		L.debug("Applied the record according with the specifications. Host evaluated is: " + value);

		return value;
	}

	/**
	 * This method retrieves the service information, verify its signatures, and
	 * then fills the values.
	 *
	 * @param url
	 *            the url used to fetch the signedServiceInformation
	 * @throws IOException
	 *             In case the SMP service is down.
	 * @throws TransformerException
	 *             Only if debug is true
	 * @throws SMLSMPClientException
	 *             If the file is malformed
	 * @throws CertificateException
	 *             if the certificate is not encoded correctly
	 */
	public void getSignedServiceInformation(URL url)
			throws IOException, TransformerException, SMLSMPClientException, CertificateException {
		Document signedServiceInfo = getFromHTTP(url.toExternalForm());

		/*
		 * TODO THIS IS THE JOAO FIX ON THE BUG OF THE SMP SERVER. TO ADD
		 */
		// Document beforeTheFix = signedServiceInfo;

		// Apply Joao fix because of the fact that the SMP record is buggy since
		// it is modified by the SMP
		// SmpXslt.removeSmpSignature(beforeTheFix);
		//
		// Document tbValidated = SmpXslt.applyXsltToSmp(beforeTheFix);
		// final String ns =
		// "http://busdox.org/serviceMetadata/publishing/1.0/";
		// SmpXslt.validateXml(tbValidated, ns, "ServiceInformation");

		/*
		 * TODO: after the JOAO you MUST verify all the signatures.
		 */
		if (L.isDebugEnabled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trans = tf.newTransformer();
			trans.transform(new DOMSource(signedServiceInfo), new StreamResult(baos));
			L.debug("Received: " + new String(baos.toByteArray()));
		}

		// Here we should go with the jaxb, but for now I will not go, since the
		// schema is not yet defined (peppol, bdxl, the namespace changes.
		String ns = "http://docs.oasis-open.org/bdxr/ns/SMP/2016/05";
		NodeList nl = signedServiceInfo.getElementsByTagNameNS(ns, "Process");
		if (nl == null) {
			throw new SMLSMPClientException("Wrong xml found, no process available");
		}

		Node processNode = nl.item(0);
		NodeList nll = processNode.getChildNodes();
		int size = nll.getLength();

		for (int i = 0; i < size; i++) {
			Node n = nll.item(i); // process

			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String localName = n.getLocalName();
				if (localName.equals("ServiceEndpointList")) {
					Element sel = (Element) n;
					NodeList endpoint = sel.getChildNodes();
					int size1 = endpoint.getLength();
					for (int j = 0; j < size1; j++) {
						Node localName1 = endpoint.item(j);
						String localName11 = localName1.getLocalName();
						if (localName11 != null && localName11.equals("Endpoint")) {
							Element endpointEl = (Element) localName1;
							NodeList nl2 = endpointEl.getElementsByTagNameNS(ns,
									"EndpointURI");
							if (nl2 == null || nl2.getLength() == 0) {
								throw new SMLSMPClientException("Unable to guess the Address");
							}
							Element address = (Element) nl2.item(0);
							endpointReference = new URL(address.getTextContent());
							NodeList nl3 = endpointEl.getElementsByTagNameNS(ns, "Certificate");
							if (nl3 == null || nl3.getLength() == 0) {
								throw new SMLSMPClientException("Unable to guess the Certificate");
							}
							Element certificate = (Element) nl3.item(0);
							String tc = certificate.getTextContent();
							byte[] tc1 = Base64.decode(tc);
							cert = (X509Certificate) CertificateFactory.getInstance("X.509")
									.generateCertificate(new ByteArrayInputStream(tc1));
							break;
						}
					}
				}
			}
		}
	}
}
