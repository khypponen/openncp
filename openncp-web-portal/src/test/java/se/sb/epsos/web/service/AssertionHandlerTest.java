/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.service;

import static org.mockito.Mockito.*;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mockito.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.signature.SignatureException;

import epsos.ccd.gnomon.auditmanager.AuditService;
import epsos.ccd.gnomon.auditmanager.EventLog;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;

import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.pages.KeyStoreInitializationException;

public class AssertionHandlerTest extends TestCase {
	private AssertionHandler assertionHandler;
	private AuthenticatedUser userDetailsMock;
	private Assertion assertionMock;
	private ConfigurationManagerService cms;
	private AuditService as;
	private MockSettings settings = withSettings().serializable();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		cms = mock(ConfigurationManagerService.class, settings);
		as = mock(AuditService.class, settings);
		
		assertionHandler = new AssertionHandler() {
			private static final long serialVersionUID = 1L;

			protected ConfigurationManagerService getConfigurationManagerService() {
				return cms;
			}
			
			protected AuditService getAuditService() {
				return as;
			}
			
			protected String getPrivateKeyAlias() {
				return "alias";
			}
			
			protected String getPrivateKeystoreLocation() {
				return "location";
			}
			
			protected String getPrivateKeyPassword() {
				return "passwd";
			}
		};
		userDetailsMock = mock(AuthenticatedUser.class, settings);
		assertionMock = mock(Assertion.class, settings);

		when(as.write(any(EventLog.class), eq("13"), eq("2"))).thenAnswer(
				new Answer<Boolean>() {
					public Boolean answer(InvocationOnMock invocation) throws Throwable {
						EventLog el = (EventLog) invocation.getArguments()[0];
						assertEquals("identityProvider::HPAuthentication", el.getEI_TransactionName());
						assertEquals("pharmacist", el.getHR_RoleID());
						assertTrue(el.getReqM_ParticipantObjectID().startsWith("urn:uuid:"));
						assertTrue(el.getResM_ParticipantObjectID().startsWith("urn:uuid:"));
						return false;
					}
				});
		
		when(userDetailsMock.getUsername()).thenReturn("username");
		when(userDetailsMock.getOrganizationId()).thenReturn("organizationId");
		when(userDetailsMock.getOrganizationName()).thenReturn("organizationName");
		when(userDetailsMock.getRoles()).thenReturn(Arrays.asList(new String[]{"ROLE_PHARMACIST"}));
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateAssertion() throws ConfigurationException, AssertionException, UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException, KeyStoreInitializationException, SecurityException, MarshallingException, SignatureException {
		Assertion assertion = assertionHandler.createSAMLAssertion(userDetailsMock);
		assertNotNull(assertion);
		assertNotNull(assertion.getIssuer().getValue());
		assertEquals(assertion.getIssuer().getValue(), "urn:idp:countryB");
		assertNotNull(assertion.getSubject().getNameID().getValue());
		assertEquals(assertion.getSubject().getNameID().getValue(), "username");
		assertEquals(assertion.getID().startsWith("_"), true);
		assertEquals(assertion.getVersion(), SAMLVersion.VERSION_20);

//		assertionHandler.signSAMLAssertion(assertion);
//		assertNotNull(assertion.getSignature());
//		assertNotNull(assertion.getSignature().getKeyInfo().getX509Datas().get(0).getX509Certificates().get(0).getValue());
//		assertEquals(assertion.getSignature().getSigningCredential().getPrivateKey().getAlgorithm(), "RSA");
	}

	public void testSendAuditEpsos91() throws Exception {
		assertionHandler.sendAuditEpsos91(userDetailsMock, assertionMock);
	}
}
