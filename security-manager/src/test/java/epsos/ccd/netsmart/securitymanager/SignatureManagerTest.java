/*
 *  Copyright 2010 jerouris.
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

import junit.framework.TestCase;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerInt;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.Certificate;

/**
 * The original tested class was not unit-testable. It was fixed so that the
 * class can be constructed without hard dependencies to external resource (at
 * 'init' -method). Class constructor structure has been changed a bit so that a
 * constructor providing the required class parameters exist now.<br/>
 * The tests originally in this class are moved to SignatureManagerIntegratioTest -class.
 * 
 * @author fivilmyrs
 */
@RunWith(MockitoJUnitRunner.class)
public class SignatureManagerTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(SignatureManagerTest.class);

	@BeforeClass
	public static void setUpClass() throws Exception {

	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {

		XMLUnit.setNormalizeWhitespace(true);
		XMLUnit.setIgnoreWhitespace(true);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testConstructing() {

		KeyStoreManager ksmMock = Mockito.mock(KeyStoreManager.class);
		String sigAlg = "SHA-256";
		String digAlg = "SHA-512";
		SignatureManager sm1 = null;
		try {
			sm1 = new SignatureManager(ksmMock, sigAlg, digAlg);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Could not initialize signature manager");
		}
		// Basically just tests that the new constructor injection replacing the
		// old 'init' method is ok.
		assertNotNull(sm1);
		assertEquals(ksmMock, sm1.getKeyManager());
		assertEquals(sigAlg, sm1.getSignatureAlgorithm());
		assertEquals(digAlg, sm1.getDigestAlgorithm());
		
		// Test also that the required properties are asked from the Configuration manager
		ConfigurationManagerService cfmMock = Mockito.mock(ConfigurationManagerService.class);
		Mockito.when(cfmMock.getProperty(SignatureManager.SIG_ALG_PROP)).thenReturn(sigAlg);
		Mockito.when(cfmMock.getProperty(SignatureManager.DGST_ALG_PROP)).thenReturn(digAlg);
		SignatureManager sm2 = null;
		try {
			sm2 = new SignatureManager(ksmMock, cfmMock);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Could not initialize signature manager");
		}
		// Basically just tests that the new constructor injection replacing the
		// old 'init' method is ok.
		assertNotNull(sm2);
		assertEquals(ksmMock, sm2.getKeyManager());
		assertEquals(sigAlg, sm2.getSignatureAlgorithm());
		assertEquals(digAlg, sm2.getDigestAlgorithm());
	}
}
