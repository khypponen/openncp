/*
 *  HCPIAssertionBuilder.java
 *  Created on 24/09/2014, 12:10:11 PM
 *  
 *  TRILLIUM.SECURITY.UTILS
 *  eu.europa.ec.joinup.ecc.trilliumsecurityutils.saml
 *  
 *  Copyright (C) 2014 iUZ Technologies, Lda - http://www.iuz.pt
 */
package eu.europa.ec.joinup.ecc.trilliumsecurityutils.saml;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.*;
import org.opensaml.saml2.core.impl.AudienceBuilder;
import org.opensaml.saml2.core.impl.AudienceRestrictionBuilder;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.Namespace;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.schema.XSAny;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.saml.SAML;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.namespace.QName;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static eu.europa.ec.joinup.ecc.trilliumsecurityutils.saml.HCPIAssertionCreator.LOG;

/**
 * {Insert Class Description Here}
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class HCPIAssertionBuilder {

    private final org.opensaml.saml2.core.Subject subject;
    private final SAML saml;
    private final Assertion assertion;
    private final AttributeStatement attributeStatement;
    private final Namespace ns1;
    private final Namespace ns2;
    private XMLSignatureFactory factory;
    private KeyStore keyStore;
    private KeyPair keyPair;
    private KeyInfo keyInfo;

    public HCPIAssertionBuilder(final String subjectUsername, final String subjectFormat, final String subjectConfirmationMethod) {
        saml = new SAML();
        // Subject
        subject = saml.createSubject(subjectUsername, subjectFormat, subjectConfirmationMethod);
        // Assertion structure
        assertion = saml.createAssertion(subject);

        // FIXED ATTRIBUTES
        // Version
        SAMLVersion version = SAMLVersion.VERSION_20;
        assertion.setVersion(version);
        // Create AttributeStatement
        attributeStatement = saml.create(AttributeStatement.class, AttributeStatement.DEFAULT_ELEMENT_NAME);

        // Namespaces
        ns1 = new Namespace();
        ns1.setNamespacePrefix("xs");
        ns1.setNamespaceURI("http://www.w3.org/2001/XMLSchema");

        ns2 = new Namespace();
        ns2.setNamespacePrefix("xsi");
        ns2.setNamespaceURI("http://www.w3.org/2001/XMLSchema-instance");

    }

    public HCPIAssertionBuilder issuer(final String value, final String format) {
        Issuer issuer = saml.create(Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setValue(value);
        issuer.setFormat(format);
        assertion.setIssuer(issuer);
        return this;
    }

    public HCPIAssertionBuilder audienceRestrictions(final String uri) {
        AudienceRestrictionBuilder arb = new AudienceRestrictionBuilder();
        AudienceRestriction ar = arb.buildObject();
        AudienceBuilder ab = new AudienceBuilder();
        Audience a = ab.buildObject();
        a.setAudienceURI(uri);
        ar.getAudiences().add(a);
        assertion.getConditions().getAudienceRestrictions().add(ar);
        return this;
    }

    public HCPIAssertionBuilder notOnOrAfter(int numberOfHours) {
        // Set AuthnStatement
        DateTime dateTime = new DateTime();
        AuthnStatement authnStatement = saml.create(AuthnStatement.class, AuthnStatement.DEFAULT_ELEMENT_NAME);
        authnStatement.setAuthnInstant(dateTime);
        authnStatement.setSessionNotOnOrAfter(dateTime.plusHours(numberOfHours));
        // Set AuthnStatement/AuthnContext
        AuthnContext authnContext = saml.create(AuthnContext.class, AuthnContext.DEFAULT_ELEMENT_NAME);
        AuthnContextClassRef authnContextClassRef = saml.create(AuthnContextClassRef.class, AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
        authnContextClassRef.setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:X509");
        authnContext.setAuthnContextClassRef(authnContextClassRef);
        authnStatement.setAuthnContext(authnContext);
        assertion.getAuthnStatements().add(authnStatement);
        return this;
    }

    public HCPIAssertionBuilder hcpIdentifier(final String hcpIdentifier) {
        Attribute att = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
        att.setFriendlyName("XSPA Subject");
        att.setName("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
        att.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");

        XMLObjectBuilder<?> builder = Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);

        XSAny attVal = (XSAny) builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attVal.setTextContent(hcpIdentifier);

        attVal.getNamespaceManager().registerNamespace(ns1);
        attVal.getNamespaceManager().registerNamespace(ns2);
        QName attributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
        attVal.getUnknownAttributes().put(attributeName, "xs:string");

        att.getAttributeValues().add(attVal);
        attributeStatement.getAttributes().add(att);
        return this;
    }

    public HCPIAssertionBuilder hcpRole(final String hcpRole) {
        Attribute att = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
        att.setFriendlyName("XSPA Role");
        att.setName("urn:oasis:names:tc:xacml:2.0:subject:role");
        att.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");

        XMLObjectBuilder<?> builder = Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);

        XSAny attVal = (XSAny) builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attVal.setTextContent(hcpRole);

        attVal.getNamespaceManager().registerNamespace(ns1);
        attVal.getNamespaceManager().registerNamespace(ns2);
        QName attributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
        attVal.getUnknownAttributes().put(attributeName, "xs:string");

        att.getAttributeValues().add(attVal);
        attributeStatement.getAttributes().add(att);
        return this;
    }

    public HCPIAssertionBuilder hcpSpecialty(final String hcpSpecialty) {
        Attribute att = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
        att.setFriendlyName("HITSP Clinical Speciality");
        att.setName("urn:epsos:names:wp3.4:subject:clinical-speciality");
        att.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");

        XMLObjectBuilder<?> builder = Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);

        XSAny attVal = (XSAny) builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attVal.setTextContent(hcpSpecialty);

        attVal.getNamespaceManager().registerNamespace(ns1);
        attVal.getNamespaceManager().registerNamespace(ns2);
        QName attributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
        attVal.getUnknownAttributes().put(attributeName, "xs:string");

        att.getAttributeValues().add(attVal);
        attributeStatement.getAttributes().add(att);
        return this;
    }

    public HCPIAssertionBuilder healthCareProfessionalOrganisation(final String hcpOrgId, final String hcpOrgName) {
        Attribute att;
        XMLObjectBuilder<?> builder;
        XSAny attVal;
        QName attributeName;

        // Set Healthcare Professional Organisation Name
        att = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
        att.setFriendlyName("XSPA Organization");
        att.setName("urn:oasis:names:tc:xspa:1.0:subject:organization");
        att.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");

        builder = Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);

        attVal = (XSAny) builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attVal.setTextContent(hcpOrgName);

        attVal.getNamespaceManager().registerNamespace(ns1);
        attVal.getNamespaceManager().registerNamespace(ns2);
        attributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
        attVal.getUnknownAttributes().put(attributeName, "xs:string");

        att.getAttributeValues().add(attVal);
        attributeStatement.getAttributes().add(att);

        // Set Healthcare Professional Organisation ID
        att = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
        att.setFriendlyName("XSPA Organization Id");
        att.setName("urn:oasis:names:tc:xspa:1.0:subject:organization-id");
        att.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");

        builder = Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);

        attVal = (XSAny) builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attVal.setTextContent(hcpOrgId);

        attVal.getNamespaceManager().registerNamespace(ns1);
        attVal.getNamespaceManager().registerNamespace(ns2);
        attributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
        attVal.getUnknownAttributes().put(attributeName, "xs:anyURI");

        att.getAttributeValues().add(attVal);
        attributeStatement.getAttributes().add(att);

        return this;
    }

    public HCPIAssertionBuilder healthCareFacilityType(final String HealthCareFacilityType) {
        Attribute att;
        XMLObjectBuilder<?> builder;
        XSAny attVal;
        QName attributeName;

        // Set Type of HCPO
        att = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
        att.setFriendlyName("epSOS Healthcare Facility Type");
        att.setName("urn:epsos:names:wp3.4:subject:healthcare-facility-type");
        att.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");

        builder = Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);

        attVal = (XSAny) builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attVal.setTextContent(HealthCareFacilityType);

        attVal.getNamespaceManager().registerNamespace(ns1);
        attVal.getNamespaceManager().registerNamespace(ns2);
        attributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
        attVal.getUnknownAttributes().put(attributeName, "xs:string");

        att.getAttributeValues().add(attVal);
        attributeStatement.getAttributes().add(att);

        return this;
    }

    public HCPIAssertionBuilder purposeOfUse(final String purposeOfUse) {
        Attribute att = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
        att.setFriendlyName("XSPA Purpose of Use");
        att.setName("urn:oasis:names:tc:xspa:1.0:subject:purposeofuse");
        att.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");

        XMLObjectBuilder<?> builder = Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);

        XSAny attVal = (XSAny) builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attVal.setTextContent(purposeOfUse);

        attVal.getNamespaceManager().registerNamespace(ns1);
        attVal.getNamespaceManager().registerNamespace(ns2);
        QName attributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
        attVal.getUnknownAttributes().put(attributeName, "xs:string");

        att.getAttributeValues().add(attVal);
        attributeStatement.getAttributes().add(att);
        return this;
    }

    public HCPIAssertionBuilder pointOfCare(final String pointOfCare) {
        Attribute att = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
        att.setFriendlyName("XSPA Locality");
        att.setName("urn:oasis:names:tc:xspa:1.0:environment:locality");

        XMLObjectBuilder<?> builder = Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);

        XSAny attVal = (XSAny) builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attVal.setTextContent("Aveiro, Portugal");

        attVal.getNamespaceManager().registerNamespace(ns1);
        attVal.getNamespaceManager().registerNamespace(ns2);
        QName attributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
        attVal.getUnknownAttributes().put(attributeName, "xs:string");

        att.getAttributeValues().add(attVal);
        attributeStatement.getAttributes().add(att);
        return this;
    }

    public HCPIAssertionBuilder onBehalfOf(final String role, final String representeeId) {
        Attribute att = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
        att.setFriendlyName("OnBehalfOf");
        att.setName("urn:epsos:names:wp3.4:subject:on-behalf-of");
        att.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");

        XMLObjectBuilder<?> resourceIdBuilder = Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);

        // Add Representee  Role
        XSAny resourceIdVal = (XSAny) resourceIdBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        resourceIdVal.setTextContent(role);

        resourceIdVal.getNamespaceManager().registerNamespace(ns1);
        resourceIdVal.getNamespaceManager().registerNamespace(ns2);
        QName resourceIdributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
        resourceIdVal.getUnknownAttributes().put(resourceIdributeName, "xs:string");

        att.getAttributeValues().add(resourceIdVal);

        // Add Representee Id
        XSAny representeeIdVal = (XSAny) resourceIdBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        representeeIdVal.setTextContent(representeeId);

        representeeIdVal.getNamespaceManager().registerNamespace(ns1);
        representeeIdVal.getNamespaceManager().registerNamespace(ns2);
        resourceIdributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
        representeeIdVal.getUnknownAttributes().put(resourceIdributeName, "xs:string");

        att.getAttributeValues().add(representeeIdVal);

        // Add information to Assertion (global) attribute statement
        attributeStatement.getAttributes().add(att);

        return this;
    }

    public HCPIAssertionBuilder permissions(final List<String> permissions) {
        Attribute att = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
        att.setFriendlyName("XSPA permissions according with Hl7");
        att.setName("urn:oasis:names:tc:xspa:1.0:subject:hl7:permission");
        att.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");

        for (String s : permissions) {
            XMLObjectBuilder<?> builder = Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);

            XSAny attVal = (XSAny) builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);

            attVal.setTextContent("urn:oasis:names:tc:xspa:1.0:subject:hl7:permission:" + s);

            attVal.getNamespaceManager().registerNamespace(ns1);
            attVal.getNamespaceManager().registerNamespace(ns2);
            QName attributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
            attVal.getUnknownAttributes().put(attributeName, "xs:string");

            att.getAttributeValues().add(attVal);
            attributeStatement.getAttributes().add(att);
        }
        return this;
    }

    public HCPIAssertionBuilder patientId(final String patientId) {
        Attribute resourceId = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
        resourceId.setFriendlyName("Resource Id");
        resourceId.setName("urn:oasis:names:tc:xacml:2.0:resource:resource-id");

        XMLObjectBuilder<?> resourceIdBuilder = Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);

        XSAny resourceIdVal = (XSAny) resourceIdBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        resourceIdVal.setTextContent(patientId);

        resourceIdVal.getNamespaceManager().registerNamespace(ns1);
        resourceIdVal.getNamespaceManager().registerNamespace(ns2);
        QName resourceIdributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
        resourceIdVal.getUnknownAttributes().put(resourceIdributeName, "xs:string");

        resourceId.getAttributeValues().add(resourceIdVal);
        attributeStatement.getAttributes().add(resourceId);
        return this;
    }

    public HCPIAssertionBuilder homeCommunityId(final String homeCommunityId) {
        Attribute homeCommunityIdAttr = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
        homeCommunityIdAttr.setFriendlyName("Home Community Id");
        homeCommunityIdAttr.setName("urn:nhin:names:saml:homeCommunityId");

        XMLObjectBuilder<?> homeCommunityIdBuilder = Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);

        XSAny homeCommunityIdVal = (XSAny) homeCommunityIdBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        homeCommunityIdVal.setTextContent(homeCommunityId);

        homeCommunityIdVal.getNamespaceManager().registerNamespace(ns1);
        homeCommunityIdVal.getNamespaceManager().registerNamespace(ns2);
        QName homeCommunityIdributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
        homeCommunityIdVal.getUnknownAttributes().put(homeCommunityIdributeName, "xs:string");

        homeCommunityIdAttr.getAttributeValues().add(homeCommunityIdVal);
        attributeStatement.getAttributes().add(homeCommunityIdAttr);
        return this;
    }

    // Build Assertion
    public Assertion build() {
        // Set AttributeStatement
        assertion.getAttributeStatements().add(attributeStatement);

        // Set Signature
        try {
            // Set assertion.DOM
            Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(assertion);
            Element elem;
            elem = marshaller.marshall(assertion);
            assertion.setDOM(elem);

            // Set factory
            String providerName = System.getProperty("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
            factory = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());

            try ( // Set keyStore
                  FileInputStream is = new FileInputStream(Constants.SC_KEYSTORE_PATH.split(Constants.EPSOS_PROPS_PATH)[0])) {
                keyStore = KeyStore.getInstance("JKS");
                keyStore.load(is, Constants.SC_KEYSTORE_PASSWORD.toCharArray());
            }

            KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection(Constants.SC_PRIVATEKEY_PASSWORD.toCharArray());
            KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(Constants.SC_PRIVATEKEY_ALIAS, pp);

            // Set keyPair
            keyPair = new KeyPair(entry.getCertificate().getPublicKey(), entry.getPrivateKey());

            // Set keyInfo
            KeyInfoFactory kFactory = factory.getKeyInfoFactory();
            keyInfo = kFactory.newKeyInfo(Collections.singletonList(kFactory.newX509Data(Collections.singletonList(entry.getCertificate()))));

            // Create Signature/SignedInfo/Reference
            List<Transform> lst = new ArrayList<>();
            lst.add(factory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));
            lst.add(factory.newTransform(CanonicalizationMethod.EXCLUSIVE, (TransformParameterSpec) null));
            Reference ref = factory.newReference("#" + assertion.getID(), factory.newDigestMethod(DigestMethod.SHA1, null), lst, null, null);

            // Set Signature/SignedInfo
            SignedInfo signedInfo = factory.newSignedInfo(factory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec) null),
                    factory.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

            // Sign Assertion
            XMLSignature signature = factory.newXMLSignature(signedInfo, keyInfo);
            DOMSignContext signContext = new DOMSignContext(keyPair.getPrivate(), assertion.getDOM());
            signature.sign(signContext);
        } catch (MarshallingException | ClassNotFoundException | InstantiationException | IllegalAccessException | KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableEntryException | InvalidAlgorithmParameterException | MarshalException | XMLSignatureException e) {
            LOG.error("Signature element not created!", e.getLocalizedMessage());
        }

        // Set Signature's place
        org.w3c.dom.Node signatureElement = assertion.getDOM().getLastChild();

        boolean foundIssuer = false;
        org.w3c.dom.Node elementAfterIssuer = null;
        NodeList children = assertion.getDOM().getChildNodes();
        for (int c = 0; c < children.getLength(); ++c) {
            org.w3c.dom.Node child = children.item(c);

            if (foundIssuer) {
                elementAfterIssuer = child;
                break;
            }

            if (child.getNodeType() == Node.ELEMENT_NODE && child.getLocalName().equals("Issuer")) {
                foundIssuer = true;
            }
        }

        // Place after the Issuer, or as first element if no Issuer:
        if (!foundIssuer || elementAfterIssuer != null) {
            assertion.getDOM().removeChild(signatureElement);
            assertion.getDOM().insertBefore(signatureElement, foundIssuer ? elementAfterIssuer : assertion.getDOM().getFirstChild());
        }

        return assertion;
    }
}
