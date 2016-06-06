/*
 * This file is part of epSOS OpenNCP implementation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package epsos.openncp.protocolterminator;

import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Provider;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Advice;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AssertionIDRef;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.Namespace;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.schema.XSAny;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

        import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.saml.SAML;

public class TRCAssertionCreator {
	
	public Assertion createTRCAssertion()
	{
		XMLSignatureFactory factory;
	    KeyStore keyStore;
	    KeyPair keyPair;
	    KeyInfo keyInfo;
	    
		SAML saml = new SAML();
		
		org.opensaml.saml2.core.Subject subject = saml.createSubject("physician the", 
				"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified", "sender-vouches");
		
		// Create assertion
		Assertion assertion = saml.createAssertion(subject);
		
		Issuer issuer = saml.create(Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
		issuer.setValue("urn:initgw:countryB");
		issuer.setFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
		issuer.setNameQualifier("urn:epsos:wp34:assertions");
		assertion.setIssuer(issuer);
		
		// Set version
		SAMLVersion version = SAMLVersion.VERSION_20;
		assertion.setVersion(version);
		
		// Set AuthnStatement
		DateTime dateTime = new DateTime();
		AuthnStatement authnStatement = saml.create(AuthnStatement.class, AuthnStatement.DEFAULT_ELEMENT_NAME);
		authnStatement.setAuthnInstant(dateTime);
		authnStatement.setSessionNotOnOrAfter(dateTime.plusHours(2));
		
		// Set AuthnStatement/AuthnContext
		AuthnContext authnContext = saml.create(AuthnContext.class, AuthnContext.DEFAULT_ELEMENT_NAME);
		AuthnContextClassRef authnContextClassRef = saml.create(AuthnContextClassRef.class, AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
		authnContextClassRef.setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:PreviousSession");
		authnContext.setAuthnContextClassRef(authnContextClassRef);
		authnStatement.setAuthnContext(authnContext);
		assertion.getAuthnStatements().add(authnStatement);
		
		// Set Advice
		Advice advice = saml.create(Advice.class, Advice.DEFAULT_ELEMENT_NAME);
		AssertionIDRef aidr = saml.create(AssertionIDRef.class, AssertionIDRef.DEFAULT_ELEMENT_NAME);
		aidr.setAssertionID(assertion.getID());
		advice.getAssertionIDReferences().add(aidr);
		assertion.setAdvice(advice);
		
		// Create AttributeStatement
		AttributeStatement attributeStatement = saml.create(AttributeStatement.class, AttributeStatement.DEFAULT_ELEMENT_NAME);
		
		// Namespaces
		Namespace ns1 = new Namespace();
		ns1.setNamespacePrefix("xs");
		ns1.setNamespaceURI("http://www.w3.org/2001/XMLSchema");
		
		Namespace ns2 = new Namespace();
		ns2.setNamespacePrefix("xsi");
		ns2.setNamespaceURI("http://www.w3.org/2001/XMLSchema-instance");
	
		// Set patient Identifier
		if(true)
		{
			Attribute att = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
			att.setFriendlyName("Patient ID");
			att.setName("urn:oasis:names:tc:xacml:1.0:resource:resource-id");
			att.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");
			
			XMLObjectBuilder<?> builder = Configuration.getBuilderFactory ()
			.getBuilder (XSAny.TYPE_NAME);

			XSAny attVal = (XSAny) builder.buildObject (AttributeValue.DEFAULT_ELEMENT_NAME);
			attVal.setTextContent ("6212122451^^^&2.16.17.710.815.1000.990.1&ISO");
			
			attVal.addNamespace(ns1);
			attVal.addNamespace(ns2);
			QName attributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
			attVal.getUnknownAttributes().put(attributeName, "xs:string");
			
			att.getAttributeValues ().add (attVal);	
			attributeStatement.getAttributes().add(att);
		}
		
		// Set Purpose of Use
		if(true)
		{
			Attribute att = saml.create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
			att.setFriendlyName("XSPA Purpose of Use");
			att.setName("urn:oasis:names:tc:xspa:1.0:subject:purposeofuse");
			att.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");
			
			XMLObjectBuilder<?> builder = Configuration.getBuilderFactory ()
			.getBuilder (XSAny.TYPE_NAME);

			XSAny attVal = (XSAny) builder.buildObject (AttributeValue.DEFAULT_ELEMENT_NAME);
			attVal.setTextContent ("TREATMENT");
			
			attVal.addNamespace(ns1);
			attVal.addNamespace(ns2);
			QName attributeName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
			attVal.getUnknownAttributes().put(attributeName, "xs:string");
			
			att.getAttributeValues ().add (attVal);	
			attributeStatement.getAttributes().add(att);
		}
		
		// Set AttributeStatement
		assertion.getAttributeStatements().add(attributeStatement);
		
		// Set Signature
		try
		{
			// Set assertion.DOM
			Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(assertion);
		    Element elem;
			elem = marshaller.marshall(assertion);
			assertion.setDOM(elem);
			
            // Set factory
            String providerName = System.getProperty
            						("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
            factory = XMLSignatureFactory.getInstance ("DOM", 
            						(Provider) Class.forName (providerName).newInstance ());
            
            // Set keyStore
            FileInputStream is = new FileInputStream(Constants.SC_KEYSTORE_PATH);
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(is, Constants.SC_KEYSTORE_PASSWORD.toCharArray());
            is.close();
            
            PasswordProtection pp = new PasswordProtection(Constants.SC_PRIVATEKEY_PASSWORD.toCharArray());
            PrivateKeyEntry entry = (PrivateKeyEntry)keyStore.getEntry(Constants.SC_PRIVATEKEY_ALIAS, pp);
            
            // Set keyPair
            keyPair = new KeyPair (entry.getCertificate ().getPublicKey (),entry.getPrivateKey ());
            
            // Set keyInfo
            KeyInfoFactory kFactory = factory.getKeyInfoFactory ();
            keyInfo = kFactory.newKeyInfo(Collections.singletonList (kFactory.newX509Data 
                    				(Collections.singletonList (entry.getCertificate ()))));
            
            // Create Signature/SignedInfo/Reference
            List<Transform> lst = new ArrayList<Transform>();
            lst.add(factory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));
            lst.add(factory.newTransform(CanonicalizationMethod.EXCLUSIVE, (TransformParameterSpec) null));
            Reference ref = factory.newReference("#" + assertion.getID(), 
            						factory.newDigestMethod (DigestMethod.SHA1, null),
            						lst,null, null);
            
            // Set Signature/SignedInfo
            SignedInfo signedInfo = factory.newSignedInfo(factory.newCanonicalizationMethod
            						(CanonicalizationMethod.EXCLUSIVE_WITH_COMMENTS, 
            						(C14NMethodParameterSpec) null), factory.newSignatureMethod 
            						(SignatureMethod.RSA_SHA1, null), Collections.singletonList (ref));
            
            // Sign Assertion
            XMLSignature signature = factory.newXMLSignature (signedInfo, keyInfo);
            DOMSignContext signContext = new DOMSignContext
                (keyPair.getPrivate (), assertion.getDOM());
            signature.sign (signContext);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// Set Signature's place
		org.w3c.dom.Node signatureElement = assertion.getDOM().getLastChild ();

        boolean foundIssuer = false;
        org.w3c.dom.Node elementAfterIssuer = null;
        NodeList children = assertion.getDOM().getChildNodes ();
        for (int c = 0; c < children.getLength (); ++c)
        {
            org.w3c.dom.Node child = children.item (c);
            
            if (foundIssuer)
            {
                elementAfterIssuer = child;
                break;
            }
            
            if (child.getNodeType () == Node.ELEMENT_NODE &&
                    child.getLocalName ().equals ("Issuer"))
                foundIssuer = true;
        }
        
        // Place after the Issuer, or as first element if no Issuer:
        if (!foundIssuer || elementAfterIssuer != null)
        {
        	assertion.getDOM().removeChild (signatureElement);
        	assertion.getDOM().insertBefore (signatureElement, 
                foundIssuer
                    ? elementAfterIssuer
                    : assertion.getDOM().getFirstChild ());
        }
        
		return assertion;
	}

}
