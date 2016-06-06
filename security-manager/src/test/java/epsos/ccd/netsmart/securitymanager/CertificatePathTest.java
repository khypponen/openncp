/*
 *  Copyright 2010 Jerry Dimitriou <jerouris at netsmart.gr>.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package epsos.ccd.netsmart.securitymanager;

import java.util.Collections;
import java.security.cert.CRLException;
import java.security.cert.X509CRL;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509CertSelector;
import java.security.cert.PKIXCertPathValidatorResult;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.NSTestKeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.TianiTestKeyStoreManager;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.security.cert.Certificate;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathValidator;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXParameters;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jerry Dimitriou <jerouris at netsmart.gr>
 */
public class CertificatePathTest {

	public CertificatePathTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	// TODO add test methods here.
	// The methods must be annotated with annotation @Test. For example:
	//
	// @Test
	// public void hello() {}

	@Test
	public void TianiCertPathTest() {
		try {

			KeyStoreManager ksm = new TianiTestKeyStoreManager();

			// instantiate a KeyStore with type JKS
			KeyStore ks = ksm.getKeyStore();

			Certificate cert = ks.getCertificate("server1");
			X509CertSelector target = new X509CertSelector();
			target.setCertificate((X509Certificate) cert);
			System.out.println(cert);
			PKIXBuilderParameters builderParams = new PKIXBuilderParameters(
					ksm.getTrustStore(), target);
			builderParams.setRevocationEnabled(false);

			CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");

			CertPathBuilderResult build = builder.build(builderParams);

			CertPath cp = build.getCertPath();

			List<? extends Certificate> certs = cp.getCertificates();
			System.out
					.println("--------------------------- Certificates as built ----------------------------");
			for (Certificate crt : certs) {
				System.out.println(crt);
			}

			System.out
					.println("--------------------------- END ----------------------------------------------");

			CertPathValidator cpv = CertPathValidator.getInstance("PKIX");
			PKIXParameters params = new PKIXParameters(ksm.getTrustStore());
			params.setRevocationEnabled(false);

			PKIXCertPathValidatorResult validationResult = (PKIXCertPathValidatorResult) cpv
					.validate(cp, params);
			System.out.println(validationResult);

		} catch (CertPathBuilderException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (CertPathValidatorException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (InvalidAlgorithmParameterException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());

		} catch (KeyStoreException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());
		}

	}

	@Ignore
	@Test
	public void NSCertPathTest() {
		try {

			KeyStoreManager ksm2 = new NSTestKeyStoreManager();

			// instantiate a KeyStore with type JKS
			KeyStore ks = ksm2.getKeyStore();
			Certificate[] certArray = ks.getCertificateChain("testncp");
			// convert chain to a List
			List<Certificate> certList = Arrays.asList(certArray);

			// instantiate a CertificateFactory for X.509
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			// extract the certification path from
			// the List of Certificates
			CertPath cp = cf.generateCertPath(certList);
			X509CRL crl = (X509CRL) cf.generateCRL(ClassLoader
					.getSystemResourceAsStream("keystores/crl/NSTestCRL.crl"));

			// print each certificate in the path
			List<? extends Certificate> certs = cp.getCertificates();
			for (Certificate cert : certs) {
				System.out.println(cert);
			}

			CertPathValidator cpv = CertPathValidator.getInstance("PKIX");
			PKIXParameters params = new PKIXParameters(ksm2.getTrustStore());
			params.setRevocationEnabled(true);
			params.addCertStore(CertStore.getInstance(
					"Collection",
					new CollectionCertStoreParameters(Collections
							.singletonList(crl))));

			PKIXCertPathValidatorResult validationResult = (PKIXCertPathValidatorResult) cpv
					.validate(cp, params);
			System.out.println(validationResult);

		} catch (CRLException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (CertPathValidatorException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (InvalidAlgorithmParameterException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (CertificateException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());

		} catch (KeyStoreException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());
		}

	}

	@Ignore
	@Test
	public void NSRevokedCertPathTest() {
		try {

			KeyStoreManager ksm2 = new NSTestKeyStoreManager();

			// instantiate a KeyStore with type JKS
			KeyStore ks = ksm2.getKeyStore();
			Certificate[] certArray = ks.getCertificateChain("testrevokedncp");
			// convert chain to a List
			List<Certificate> certList = Arrays.asList(certArray);

			// instantiate a CertificateFactory for X.509
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			// extract the certification path from
			// the List of Certificates
			CertPath cp = cf.generateCertPath(certList);
			X509CRL crl = (X509CRL) cf.generateCRL(ClassLoader
					.getSystemResourceAsStream("keystores/crl/NSTestCRL.crl"));

			// print each certificate in the path
			List<? extends Certificate> certs = cp.getCertificates();
			for (Certificate cert : certs) {
				System.out.println(cert);
			}

			CertPathValidator cpv = CertPathValidator.getInstance("PKIX");
			PKIXParameters params = new PKIXParameters(ksm2.getTrustStore());
			params.setRevocationEnabled(true);
			params.addCertStore(CertStore.getInstance(
					"Collection",
					new CollectionCertStoreParameters(Collections
							.singletonList(crl))));

			PKIXCertPathValidatorResult validationResult = (PKIXCertPathValidatorResult) cpv
					.validate(cp, params);
			System.out.println(validationResult);

		} catch (CRLException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (CertPathValidatorException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			assertEquals("Certificate has been revoked, reason: unspecified",
					ex.getMessage());
		} catch (InvalidAlgorithmParameterException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());
		} catch (CertificateException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());

		} catch (KeyStoreException ex) {
			Logger.getLogger(CertificatePathTest.class.getName()).log(
					Level.SEVERE, null, ex);
			fail(ex.getMessage());
		}

	}
}