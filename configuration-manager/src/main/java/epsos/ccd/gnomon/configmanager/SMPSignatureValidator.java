package epsos.ccd.gnomon.configmanager;

import java.io.InputStream;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;

import org.w3c.dom.Element;

import sun.misc.IOUtils;

/**
 * This code was copied&pasted, apologizes for not refactoring.
 * 
 * This methods validates a signature. Note that in the setKeySaved, we should
 * authenticate the key TODO TODO TODO
 * 
 * @author max
 */
public class SMPSignatureValidator {
	private X509Certificate savedKey;
	private static final Object LOCK_1 = new Object() {
	};
	private static final Object LOCK_2 = new Object() {
	};

	public void validateSignature(Element sigPointer) throws Exception {
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

		// Create a DOMValidateContext and specify a KeySelector and document
		// context.
		DOMValidateContext valContext = new DOMValidateContext(new SMPSignatureValidator.X509KeySelector(), sigPointer);

		valContext.setProperty("javax.xml.crypto.dsig.cacheReference", Boolean.TRUE);
		// Unmarshal the XMLSignature.
		XMLSignature signature = fac.unmarshalXMLSignature(valContext);
		// Validate the XMLSignature.
		boolean coreValidity = signature.validate(valContext);

		Iterator i = signature.getSignedInfo().getReferences().iterator();
		for (int j = 0; i.hasNext(); j++) {
			InputStream is = ((Reference) i.next()).getDigestInputStream();
			// Display the data.
			byte[] a = IOUtils.readFully(is, 0, true);
			System.out.println(new String(a));
		}

		// Check core validation status.
		if (coreValidity == false) {
			printErrorDetails(valContext, signature);
			throw new RuntimeException("+++ Signature not valild +++");
		} else {
			System.out.println("+++ Signature passed core validation +++");
		}

	}

	private void printErrorDetails(DOMValidateContext valContext, XMLSignature signature) throws XMLSignatureException {
		System.err.println("Signature failed core validation");
		boolean sv = signature.getSignatureValue().validate(valContext);
		System.out.println("signature validation status: " + sv);
		if (sv == false) {
			// Check the validation status of each Reference.
			Iterator i1 = signature.getSignedInfo().getReferences().iterator();
			for (int j = 0; i1.hasNext(); j++) {
				boolean refValid = ((Reference) i1.next()).validate(valContext);
				System.out.println("ref[" + j + "] validity status: " + refValid);
			}
		}
	}

	public X509Certificate getKeySaved() {
		synchronized (LOCK_2) {
			return savedKey;
		}
	}

	private void setKeySaved(X509Certificate key) {
		synchronized (LOCK_1) {
			this.savedKey = key;
		}
	}

	private class X509KeySelector extends KeySelector {

		public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod method,
				XMLCryptoContext context) throws KeySelectorException {
			Iterator ki = keyInfo.getContent().iterator();
			while (ki.hasNext()) {
				XMLStructure info = (XMLStructure) ki.next();
				if (!(info instanceof X509Data))
					continue;
				X509Data x509Data = (X509Data) info;
				Iterator xi = x509Data.getContent().iterator();
				while (xi.hasNext()) {
					Object o = xi.next();
					if (!(o instanceof X509Certificate))
						continue;
					final PublicKey key = ((X509Certificate) o).getPublicKey();
					setKeySaved((X509Certificate) o);
					// Make sure the algorithm is compatible
					// with the method.
					if (algEquals(method.getAlgorithm(), key.getAlgorithm())) {
						return new KeySelectorResult() {

							public Key getKey() {

								return key;
							}

						};
					}
				}
			}
			throw new KeySelectorException(
					"No key found! Either the namespaces are wrong, or there is a algorithm which is not sha 256.");
		}

		// Massi + Jerome + Joao 13/4/2017. Allow for RSA_SHA1 signatures
		// because of the gazelle CA
		boolean algEquals(String algURI, String algName) {
			if ((algName.equalsIgnoreCase("DSA") && algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1))
					|| (algName.equalsIgnoreCase("RSA") && algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1))
					|| (algName.equalsIgnoreCase("RSA")
							&& algURI.equalsIgnoreCase("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"))) {
				return true;
			} else {
				return false;
			}
		}
	}
}