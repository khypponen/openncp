package epsos.ccd.netsmart.securitymanager.key;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;

public class LAMKeystoreManager implements KeyStoreManager {

	private KeyStore keyStore;
	private KeyStore trustStore;
	private PrivateKey key;
	private X509Certificate cert;

	public LAMKeystoreManager(KeyStore ks, KeyStore ts, String keyAlias,
			String password) throws UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException {
		this.keyStore = ks;
		this.trustStore = ts;

		key = (PrivateKey) keyStore.getKey(keyAlias, password.toCharArray());
		cert = (X509Certificate) keyStore.getCertificate(keyAlias);

	}

	@Override
	public KeyPair getDefaultPrivateKey() throws SMgrException {
		return new KeyPair(cert.getPublicKey(), key);
	}

	@Override
	public Certificate getDefaultCertificate() throws SMgrException {
		return cert;
	}

	@Override
	public KeyPair getPrivateKey(String alias, char[] password)
			throws SMgrException {
		try {
			PrivateKey mykey = (PrivateKey) keyStore.getKey(alias, password);
			X509Certificate mycert = (X509Certificate) keyStore
					.getCertificate(alias);
			return new KeyPair(mycert.getPublicKey(), mykey);
		} catch (Exception e) {
			throw new SMgrException(e.getMessage());
		}
	}

	@Override
	public Certificate getCertificate(String alias) throws SMgrException {
		X509Certificate mycert;
		try {
			mycert = (X509Certificate) keyStore
					.getCertificate(alias);
			return mycert;

		} catch (KeyStoreException e) {
			throw new SMgrException(e.getMessage());
		}
	}

	@Override
	public KeyStore getKeyStore() {
		return this.keyStore;
	}

	@Override
	public KeyStore getTrustStore() {
		return this.trustStore;
	}

}
