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

import java.io.FileInputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.XSURI;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.pages.KeyStoreInitializationException;
import se.sb.epsos.web.pages.KeyStoreManager;
import se.sb.epsos.web.pages.KeyStoreManagerImpl;
import se.sb.epsos.web.util.CdaHelper.Validator;
import epsos.ccd.gnomon.auditmanager.AuditService;
import epsos.ccd.gnomon.auditmanager.EventActionCode;
import epsos.ccd.gnomon.auditmanager.EventLog;
import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.ccd.gnomon.auditmanager.EventType;
import epsos.ccd.gnomon.auditmanager.TransactionName;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;

public class AssertionHandler implements Serializable {
	private static final long serialVersionUID = 5209063407337843010L;

	private static Logger LOGGER = LoggerFactory.getLogger(AssertionHandler.class);
	private AssertionHandlerConfigManager configHandler;
	private Assertion assertion;

	public AssertionHandler(AssertionHandlerConfigManager config) {
		this.configHandler = config;
	}

	public AssertionHandler() {
		this(new AssertionHandlerConfigManager());
	}

	public Assertion createSAMLAssertion(AuthenticatedUser userDetails) throws ConfigurationException, AssertionException {
		LOGGER.debug("################################################");
		LOGGER.debug("# createSAMLAssertion() - start                #");
		LOGGER.debug("################################################");
		DefaultBootstrap.bootstrap();
		XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

		@SuppressWarnings("unchecked")
		SAMLObjectBuilder<Assertion> nameIdBuilder = (SAMLObjectBuilder<Assertion>) builderFactory.getBuilder(NameID.DEFAULT_ELEMENT_NAME);
		NameID nameId = (NameID) nameIdBuilder.buildObject();
		nameId.setValue(userDetails.getUsername());
		nameId.setFormat(NameID.UNSPECIFIED);

		assertion = create(Assertion.class, Assertion.DEFAULT_ELEMENT_NAME);

		String assertionId = "_" + UUID.randomUUID();
		assertion.setID(assertionId);
		assertion.setVersion(SAMLVersion.VERSION_20);
		assertion.setIssueInstant(new DateTime().minusHours(3));

		Subject subject = create(Subject.class, Subject.DEFAULT_ELEMENT_NAME);
		assertion.setSubject(subject);
		subject.setNameID(nameId);

		SubjectConfirmation subjectConf = create(SubjectConfirmation.class, SubjectConfirmation.DEFAULT_ELEMENT_NAME);
		subjectConf.setMethod(SubjectConfirmation.METHOD_SENDER_VOUCHES);
		assertion.getSubject().getSubjectConfirmations().add(subjectConf);

		Conditions conditions = create(Conditions.class, Conditions.DEFAULT_ELEMENT_NAME);
		DateTime now = new DateTime();
		conditions.setNotBefore(now.minusHours(2));
		conditions.setNotOnOrAfter(now.plusHours(2));
		assertion.setConditions(conditions);

		Issuer issuer = new IssuerBuilder().buildObject();
		issuer.setValue("urn:idp:countryB");
		issuer.setNameQualifier("urn:epsos:wp34:assertions");
		assertion.setIssuer(issuer);

		AuthnStatement authnStatement = create(AuthnStatement.class, AuthnStatement.DEFAULT_ELEMENT_NAME);
		authnStatement.setAuthnInstant(now.minusHours(2));
		assertion.getAuthnStatements().add(authnStatement);

		AuthnContext authnContext = create(AuthnContext.class, AuthnContext.DEFAULT_ELEMENT_NAME);
		AuthnContextClassRef authnContextClassRef = create(AuthnContextClassRef.class, AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
		authnContextClassRef.setAuthnContextClassRef(AuthnContext.X509_AUTHN_CTX);
		authnContext.setAuthnContextClassRef(authnContextClassRef);
		authnStatement.setAuthnContext(authnContext);

		AttributeStatement attributeStatement = create(AttributeStatement.class, AttributeStatement.DEFAULT_ELEMENT_NAME);

		Attribute attrPID = createAttribute(builderFactory, "XSPA subject", 
				"urn:oasis:names:tc:xacml:1.0:subject:subject-id", userDetails.getCommonName(), "", "");
		attributeStatement.getAttributes().add(attrPID);

		//TODO fix multiple roles??
		String role = AssertionHandlerConfigManager.getRoleDisplayName(userDetails.getRoles().get(0));
		Attribute attrPID_1 = createAttribute(builderFactory, "XSPA role", "urn:oasis:names:tc:xacml:2.0:subject:role", role, "", "");
		attributeStatement.getAttributes().add(attrPID_1);

		Attribute attrPID_3 = createAttribute(builderFactory, "XSPA Organization", 
				"urn:oasis:names:tc:xspa:1.0:subject:organization", userDetails.getOrganizationName(), "", "");
		attributeStatement.getAttributes().add(attrPID_3);

		Attribute attrPID_4 = createAttribute(builderFactory, "XSPA Organization ID", 
				"urn:oasis:names:tc:xspa:1.0:subject:organization-id", userDetails.getOrganizationId(), "AA", "");
		attributeStatement.getAttributes().add(attrPID_4);
               
        Attribute attrPID_5 = createAttribute(builderFactory, "epSOS Healthcare Facility Type",
				"urn:epsos:names:wp3.4:subject:healthcare-facility-type", AssertionHandlerConfigManager.getFacilityType(userDetails.getRoles().get(0)), "", "");
		attributeStatement.getAttributes().add(attrPID_5);

		Attribute attrPID_6 = createAttribute(builderFactory, "XSPA Purpose Of Use", 
				"urn:oasis:names:tc:xspa:1.0:subject:purposeofuse", AssertionHandlerConfigManager.getPurposeOfUse(), "", "");
		attributeStatement.getAttributes().add(attrPID_6);

		Attribute attrPID_7 = createAttribute(builderFactory, "XSPA Locality", 
				"urn:oasis:names:tc:xspa:1.0:environment:locality", userDetails.getOrganizationName(), "", "");
		attributeStatement.getAttributes().add(attrPID_7);

		Attribute attrPID_8 = createAttribute(builderFactory, "Hl7 Permissions", 
				"urn:oasis:names:tc:xspa:1.0:subject:hl7:permission");
		Set<String> permissions = new HashSet<String>();
		for (String r : userDetails.getRoles()) {
			permissions.addAll(AssertionHandlerConfigManager.getPersmissions(r));
		}
		
		String permisssionPrefix = AssertionHandlerConfigManager.getPersmissionsPrefix();
		for (String permission : permissions) {
			attrPID_8 = AddAttributeValue(builderFactory, attrPID_8, permisssionPrefix + permission, "", "");
		}
		
		attributeStatement.getAttributes().add(attrPID_8);

		assertion.getStatements().add(attributeStatement);

		LOGGER.debug("# createSAMLAssertion() - stop ");
		
		try {
			sendAuditEpsos91(userDetails, assertion);
		} catch(Exception e) {
			// Is this fatal?
		}
		
		return assertion;
	}

	protected ConfigurationManagerService getConfigurationManagerService() {
		return ConfigurationManagerService.getInstance();
	}
	
	protected AuditService getAuditService() {
		return new AuditService();
	}
	
	protected String getPrivateKeyAlias() {
		return NcpServiceConfigManager.getPrivateKeyAlias("assertion");
	}
	
	protected String getPrivateKeystoreLocation() {
		return NcpServiceConfigManager.getPrivateKeystoreLocation("assertion");
	}
	
	protected String getPrivateKeyPassword() {
		return NcpServiceConfigManager.getPrivateKeyPassword("assertion");
	}
	
	protected void sendAuditEpsos91(AuthenticatedUser userDetails, Assertion assertion) throws KeyStoreException, CertificateException, NoSuchAlgorithmException {
		String KEY_ALIAS = getPrivateKeyAlias();
		String KEYSTORE_LOCATION = getPrivateKeystoreLocation();
		String KEY_STORE_PASS = getPrivateKeyPassword();

		ConfigurationManagerService cms = getConfigurationManagerService();
		if (Validator.isNull(KEY_ALIAS)) {
			LOGGER.error("Problem reading configuration parameters");
			return;
		}
		java.security.cert.Certificate cert = null;
		String name = "";
		try {
			FileInputStream is = new FileInputStream(KEYSTORE_LOCATION);
			KeyStore keystore = KeyStore.getInstance("JKS");
			keystore.load(is, KEY_STORE_PASS.toCharArray());
			// Get certificate
			cert = keystore.getCertificate(KEY_ALIAS);

			// List the aliases
			Enumeration<String> enum1 = keystore.aliases();
			for (; enum1.hasMoreElements();) {
				String alias = (String) enum1.nextElement();

				if (cert instanceof X509Certificate) {
					X509Certificate x509cert = (X509Certificate) cert;

					// Get subject
					Principal principal = x509cert.getSubjectDN();
					String subjectDn = principal.getName();
					name = subjectDn;

					// Get issuer
					principal = x509cert.getIssuerDN();
					String issuerDn = principal.getName();
				}
			}
		} catch (KeyStoreException e) {
			LOGGER.error(e.getMessage());
		} catch (java.security.cert.CertificateException e) {
			LOGGER.error(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		} catch (java.io.IOException e) {
			LOGGER.error(e.getMessage());
		}

		String secHead = "No security header provided";
		String basedSecHead = new String( Base64.encodeBase64(secHead.getBytes()) );
		String reqm_participantObjectID = "urn:uuid:" + assertion.getID();
		String resm_participantObjectID = "urn:uuid:" + assertion.getID();

		InetAddress sourceIP = null;
		try {
			sourceIP = InetAddress.getLocalHost();
		} catch (UnknownHostException ex) {
			LOGGER.error(ex.getMessage());
		}

		String email = userDetails.getUserId() + "@" + cms.getProperty("ncp.country");

		String PC_UserID = userDetails.getOrganizationName() + "<saml:" + email + ">";
		String PC_RoleID = "Other";
		String HR_UserID = userDetails.getCommonName() + "<saml:" + email + ">";
		String HR_RoleID = AssertionHandlerConfigManager.getRoleDisplayName( userDetails.getRoles().get(0) );
		String HR_AlternativeUserID = userDetails.getCommonName();
		String SC_UserID = name;
		String SP_UserID = name;

		String AS_AuditSourceId = cms.getProperty("COUNTRY_PRINCIPAL_SUBDIVISION");
		String ET_ObjectID = "urn:uuid:" + assertion.getID();
		byte[] ResM_PatricipantObjectDetail = new byte[1];

		AuditService asd = getAuditService();
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date());
		XMLGregorianCalendar date2 = null;
		try {
			date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException ex) {
			LOGGER.error(ex.getMessage());
		}
		EventLog eventLog = EventLog.createEventLogHCPIdentity(
				TransactionName.epsosHcpAuthentication,
				EventActionCode.EXECUTE, date2,
				EventOutcomeIndicator.FULL_SUCCESS, PC_UserID, PC_RoleID,
				HR_UserID, HR_RoleID, HR_AlternativeUserID, SC_UserID,
				SP_UserID, AS_AuditSourceId, ET_ObjectID,
				reqm_participantObjectID, basedSecHead.getBytes(),
				resm_participantObjectID, ResM_PatricipantObjectDetail,
				sourceIP.getHostAddress(), "N/A");
		eventLog.setEventType(EventType.epsosHcpAuthentication);
		asd.write(eventLog, "13", "2");
	}

	public Attribute createAttribute(XMLObjectBuilderFactory builderFactory, String FriendlyName, String oasisName) {
		Attribute attrPID = create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
		attrPID.setFriendlyName(FriendlyName);
		attrPID.setName(oasisName);
		attrPID.setNameFormat(Attribute.URI_REFERENCE);
		return attrPID;
	}

	public Attribute AddAttributeValue(XMLObjectBuilderFactory builderFactory, Attribute attribute, String value, String namespace, String xmlschema) {
		@SuppressWarnings("unchecked")
		XMLObjectBuilder<Assertion> stringBuilder = builderFactory.getBuilder(XSString.TYPE_NAME);
		XSString attrValPID = (XSString) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		attrValPID.setValue(value);
		attribute.getAttributeValues().add(attrValPID);
		return attribute;
	}

	public Attribute createAttribute(XMLObjectBuilderFactory builderFactory, String FriendlyName, String oasisName, String value, String namespace,
			String xmlschema) {
		Attribute attrPID = create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
		attrPID.setFriendlyName(FriendlyName);
		attrPID.setName(oasisName);
		attrPID.setNameFormat(Attribute.URI_REFERENCE);

		XMLObjectBuilder<Assertion> stringBuilder = null;

		if (namespace.equals("")) {
			XSString attrValPID = null;
			stringBuilder = builderFactory.getBuilder(XSString.TYPE_NAME);
			attrValPID = (XSString) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
			attrValPID.setValue(value);
			attrPID.getAttributeValues().add(attrValPID);
		} else {
			XSURI attrValPID = null;
			stringBuilder = builderFactory.getBuilder(XSURI.TYPE_NAME);
			attrValPID = (XSURI) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSURI.TYPE_NAME);
			attrValPID.setValue(value);
			attrPID.getAttributeValues().add(attrValPID);
		}

		return attrPID;
	}

	public <T> T create(Class<T> cls, QName qname) {
		return (T) ((XMLObjectBuilder) Configuration.getBuilderFactory().getBuilder(qname)).buildObject(qname);
	}

	public void signSAMLAssertion(SignableSAMLObject assertion) throws KeyStoreInitializationException, KeyStoreException, UnrecoverableKeyException,
			NoSuchAlgorithmException, SecurityException, MarshallingException, SignatureException {
		LOGGER.debug("################################################");
		LOGGER.debug("# signSAMLAssertion() - start                  #");
		LOGGER.debug("################################################");
		KeyStoreManager keyManager = new KeyStoreManagerImpl();
		X509Certificate certificate = (X509Certificate) keyManager.getCertificate();
		KeyPair privateKeyPair = keyManager.getPrivateKey();

		PrivateKey privateKey = privateKeyPair.getPrivate();

		Signature signature = (Signature) Configuration.getBuilderFactory().getBuilder(Signature.DEFAULT_ELEMENT_NAME)
				.buildObject(Signature.DEFAULT_ELEMENT_NAME);
		Credential signingCredential = SecurityHelper.getSimpleCredential(certificate, privateKey);
		signature.setSigningCredential(signingCredential);
		signature.setSignatureAlgorithm("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
		signature.setCanonicalizationAlgorithm("http://www.w3.org/2001/10/xml-exc-c14n#");

		SecurityConfiguration securityConfiguration = Configuration.getGlobalSecurityConfiguration();
		SecurityHelper.prepareSignatureParams(signature, signingCredential, securityConfiguration, null);

		assertion.setSignature(signature);
		Configuration.getMarshallerFactory().getMarshaller(assertion).marshall(assertion);
		Signer.signObject(signature);
		LOGGER.debug("# signSAMLAssertion() - stop ");
	}
	
	public Assertion getAssertion() {
		return assertion;
	}
        
        public String getOrganizationId(){
            return getAttributeValue("urn:oasis:names:tc:xspa:1.0:subject:organization-id");
        }
        
        public String getFacilityType() {
            return getAttributeValue("urn:epsos:names:wp3.4:subject:healthcare-facility-type");
        }
        
        private String getAttributeValue(String key){
            String returnValue = null;
            List<AttributeStatement> attrStatementents = assertion.getAttributeStatements();
            for(AttributeStatement stat: attrStatementents){
                List<Attribute> attributes = stat.getAttributes();
                for(Attribute attr: attributes){
                    if(attr.getName().equals(key)){
                        return attr.getAttributeValues().get(0).getDOM().getTextContent();
                    }
                }
            }
            return returnValue;
        }
}
