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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.saml2.core.Advice;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AssertionIDRef;
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
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.XSURI;

import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;
import epsos.ccd.netsmart.securitymanager.key.impl.DefaultKeyStoreManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The TRC Assertion issuer is a subcomponent that issues Treatment Relationship
 * Assertions as specified in D3.4.2. It makes use of the Signature Manager for
 * signing the assertions. An audit trail entry is written after the successful
 * issuance of a TRC assertion.
 *
 * @author Jerry Dimitriou <jerouris at netsmart.gr>
 *
 */
public class SamlTRCIssuer {

    private static final Logger logger = LoggerFactory.getLogger(SamlTRCIssuer.class);

    KeyStoreManager ksm;
    HashMap<String, String> auditDataMap;

    public SamlTRCIssuer() throws IOException {
        ksm = new DefaultKeyStoreManager();
        auditDataMap = new HashMap<String, String>();
    }

    public SamlTRCIssuer(KeyStoreManager ksm) {
        this.ksm = ksm;
    }

    /**
     * @param idaReference 
     * @param sessionNotOnOrAfter 
     * @param authnInstant 
     * @param notOnOrAfter 
     * @param notBefore 
     * @param doctorId 
     * @param patientID 
     * @param purposeOfUse 
     *
     */
    public Assertion issueTrcTokenUnsigned(String purposeOfUse, String patientID, String doctorId, DateTime notBefore, DateTime notOnOrAfter, DateTime authnInstant, DateTime sessionNotOnOrAfter, String idaReference) throws SMgrException {

        try {
            //initializing the map
            XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

           

            // Create the assertion
            Assertion trc = create(Assertion.class, Assertion.DEFAULT_ELEMENT_NAME);
            if (patientID == null) {
                throw new SMgrException("Patiend ID cannot be null");
            }


            trc.setIssueInstant(new DateTime());
            trc.setID("_" + UUID.randomUUID());

            trc.setVersion(SAMLVersion.VERSION_20);

            // Create and add the Subject
            Subject subject = create(Subject.class, Subject.DEFAULT_ELEMENT_NAME);

            trc.setSubject(subject);
            Issuer issuer = new IssuerBuilder().buildObject();

            String confIssuer = "urn:initgw:countryB";
            
            issuer.setValue(confIssuer);
            trc.setIssuer(issuer);

            NameID nameid = create(NameID.class, NameID.DEFAULT_ELEMENT_NAME);
            nameid.setFormat(NameID.UNSPECIFIED);
            nameid.setValue(doctorId);
            
            trc.getSubject().setNameID(nameid);
          

            //Create and add Subject Confirmation
            SubjectConfirmation subjectConf = create(SubjectConfirmation.class, SubjectConfirmation.DEFAULT_ELEMENT_NAME);
            subjectConf.setMethod(SubjectConfirmation.METHOD_SENDER_VOUCHES);
            trc.getSubject().getSubjectConfirmations().add(subjectConf);

            //Create and add conditions
            Conditions conditions = create(Conditions.class, Conditions.DEFAULT_ELEMENT_NAME);
            DateTime now = new DateTime();
            conditions.setNotBefore(now);
            conditions.setNotOnOrAfter(now.plusHours(2)); // According to Spec
            trc.setConditions(conditions);


            //Create and add Advice
            Advice advice = create(Advice.class, Advice.DEFAULT_ELEMENT_NAME);
            trc.setAdvice(advice);

            //Create and add AssertionIDRef
            AssertionIDRef aIdRef = create(AssertionIDRef.class, AssertionIDRef.DEFAULT_ELEMENT_NAME);
            aIdRef.setAssertionID(idaReference);
            advice.getAssertionIDReferences().add(aIdRef);

            //Add and create the authentication statement
            AuthnStatement authStmt = create(AuthnStatement.class, AuthnStatement.DEFAULT_ELEMENT_NAME);
            authStmt.setAuthnInstant(now);
            trc.getAuthnStatements().add(authStmt);

            //Creata and add AuthnContext
            AuthnContext ac = create(AuthnContext.class, AuthnContext.DEFAULT_ELEMENT_NAME);
            AuthnContextClassRef accr = create(AuthnContextClassRef.class, AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
            accr.setAuthnContextClassRef(AuthnContext.PREVIOUS_SESSION_AUTHN_CTX);
            ac.setAuthnContextClassRef(accr);
            authStmt.setAuthnContext(ac);

            // Create the Saml Attribute Statement
            AttributeStatement attrStmt = create(AttributeStatement.class, AttributeStatement.DEFAULT_ELEMENT_NAME);
            trc.getStatements().add(attrStmt);

            //Creating the Attribute that holds the Patient ID
            Attribute attrPID = create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
            attrPID.setFriendlyName("XSPA Subject");

            // TODO: Is there a constant for that urn??
            attrPID.setName("urn:oasis:names:tc:xacml:1.0:resource:resource-id");
            attrPID.setNameFormat(Attribute.URI_REFERENCE);

            //Create and add the Attribute Value
            XMLObjectBuilder stringBuilder = builderFactory.getBuilder(XSString.TYPE_NAME);
            XSString attrValPID = (XSString) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
            attrValPID.setValue(patientID);
            attrPID.getAttributeValues().add(attrValPID);
            attrStmt.getAttributes().add(attrPID);

            //Creating the Attribute that holds the Purpose of Use
            Attribute attrPoU = create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
            attrPoU.setFriendlyName("XSPA Purpose Of Use");

            // TODO: Is there a constant for that urn??
            attrPoU.setName("urn:oasis:names:tc:xspa:1.0:subject:purposeofuse");
            attrPoU.setNameFormat(Attribute.URI_REFERENCE);
            if (purposeOfUse == null) {
                attrPoU = createAttribute(purposeOfUse, "Purpose Of Use", Attribute.NAME_FORMAT_ATTRIB_NAME,  "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse");
                if (attrPoU == null) {
                    throw new SMgrException("Puprose of use not found in the assertion and is not passed as a parameter");
                }
            } else {
                XSString attrValPoU = (XSString) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
                attrValPoU.setValue(purposeOfUse);
                attrPoU.getAttributeValues().add(attrValPoU);
            }
            attrStmt.getAttributes().add(attrPoU);

           
            return trc;
        }
        catch (Throwable ex) {
            logger.error(null, ex);
            throw new SMgrException(ex.getMessage());
        }
    }
    
    /**
     * Issues a SAML TRC Assertion that proves the treatment relationship
     * between the HCP and the Patient. The Identity of the HCP is provided by
     * the hcpIdentityAssertion. The identity of the patient is inferred from
     * the patiendID.
     *
     * @param hcpIdentityAssertion The health care professional Identity SAML
     * Assertion. The method validates the assertion using the
     * {@link SignatureManager#verifySAMLAssestion(org.opensaml.saml2.core.Assertion)}.
     *
     * @param patientID The Patient Id that is required for the TRC Assertion
     * @param purposeOfUse Purpose of use Variables (e.g. TREATMENT)
     * @param attrValuePair SAML {@link Attribute} that will be added to the
     * assertion
     *
     * @return the SAML TRC Assertion
     * @throws IOException 
     */
    public Assertion issueTrcToken(final Assertion hcpIdentityAssertion, String patientID,
            String purposeOfUse,
            List<Attribute> attrValuePair) throws SMgrException, IOException {

        try {
            //initializing the map
            auditDataMap.clear();
            XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
            SecureRandomIdentifierGenerator randomGen = new SecureRandomIdentifierGenerator();

            //Doing an indirect copy so, because when cloning, signatures are lost.
            SignatureManager sman = new SignatureManager(ksm);

            try {
                sman.verifySAMLAssestion(hcpIdentityAssertion);
            }
            catch (SMgrException ex) {
                logger.error(null, ex);
                throw new SMgrException("SAML Assertion Validation Failed: " + ex.getMessage());
            }
            if (hcpIdentityAssertion.getConditions().getNotBefore().isAfterNow()) {
                String msg = "Identity Assertion with ID " + hcpIdentityAssertion.getID() + " can't ne used before " + hcpIdentityAssertion.getConditions().getNotBefore();
                logger.error(msg);
                throw new SMgrException(msg);
            }
            if (hcpIdentityAssertion.getConditions().getNotOnOrAfter().isBeforeNow()) {
                String msg = "Identity Assertion with ID " + hcpIdentityAssertion.getID() + " can't be used after " + hcpIdentityAssertion.getConditions().getNotOnOrAfter();
                logger.error(msg);
                throw new SMgrException(msg);
            }

            auditDataMap.put("hcpIdAssertionID", hcpIdentityAssertion.getID());

            // Create the assertion
            Assertion trc = create(Assertion.class, Assertion.DEFAULT_ELEMENT_NAME);
            if (patientID == null) {
                throw new SMgrException("Patiend ID cannot be null");
            }

            auditDataMap.put("patientID", patientID);

            trc.setIssueInstant(new DateTime());
            trc.setID("_" + UUID.randomUUID());
            auditDataMap.put("trcAssertionID", trc.getID());

            trc.setVersion(SAMLVersion.VERSION_20);

            // Create and add the Subject
            Subject subject = create(Subject.class, Subject.DEFAULT_ELEMENT_NAME);

            trc.setSubject(subject);
            Issuer issuer = new IssuerBuilder().buildObject();

            String confIssuer = "urn:initgw:countryB";
            
            issuer.setValue(confIssuer);
            trc.setIssuer(issuer);

            NameID nameid = getXspaSubjectFromAttributes(hcpIdentityAssertion.getAttributeStatements());
            trc.getSubject().setNameID(nameid);
            auditDataMap.put("humanRequestorNameID", hcpIdentityAssertion.getSubject().getNameID().getValue());
            auditDataMap.put("humanRequestorSubjectID", nameid.getValue());

            //Create and add Subject Confirmation
            SubjectConfirmation subjectConf = create(SubjectConfirmation.class, SubjectConfirmation.DEFAULT_ELEMENT_NAME);
            subjectConf.setMethod(SubjectConfirmation.METHOD_SENDER_VOUCHES);
            trc.getSubject().getSubjectConfirmations().add(subjectConf);

            //Create and add conditions
            Conditions conditions = create(Conditions.class, Conditions.DEFAULT_ELEMENT_NAME);
            DateTime now = new DateTime();
            conditions.setNotBefore(now);
            conditions.setNotOnOrAfter(now.plusHours(2)); // According to Spec
            trc.setConditions(conditions);


            //Create and add Advice
            Advice advice = create(Advice.class, Advice.DEFAULT_ELEMENT_NAME);
            trc.setAdvice(advice);

            //Create and add AssertionIDRef
            AssertionIDRef aIdRef = create(AssertionIDRef.class, AssertionIDRef.DEFAULT_ELEMENT_NAME);
            aIdRef.setAssertionID(hcpIdentityAssertion.getID());
            advice.getAssertionIDReferences().add(aIdRef);

            //Add and create the authentication statement
            AuthnStatement authStmt = create(AuthnStatement.class, AuthnStatement.DEFAULT_ELEMENT_NAME);
            authStmt.setAuthnInstant(now);
            trc.getAuthnStatements().add(authStmt);

            //Creata and add AuthnContext
            AuthnContext ac = create(AuthnContext.class, AuthnContext.DEFAULT_ELEMENT_NAME);
            AuthnContextClassRef accr = create(AuthnContextClassRef.class, AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
            accr.setAuthnContextClassRef(AuthnContext.PREVIOUS_SESSION_AUTHN_CTX);
            ac.setAuthnContextClassRef(accr);
            authStmt.setAuthnContext(ac);

            // Create the Saml Attribute Statement
            AttributeStatement attrStmt = create(AttributeStatement.class, AttributeStatement.DEFAULT_ELEMENT_NAME);
            trc.getStatements().add(attrStmt);

            //Creating the Attribute that holds the Patient ID
            Attribute attrPID = create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
            attrPID.setFriendlyName("XSPA Subject");

            // TODO: Is there a constant for that urn??
            attrPID.setName("urn:oasis:names:tc:xacml:1.0:resource:resource-id");
            attrPID.setNameFormat(Attribute.URI_REFERENCE);

            //Create and add the Attribute Value
            XMLObjectBuilder stringBuilder = builderFactory.getBuilder(XSString.TYPE_NAME);
            XSString attrValPID = (XSString) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
            attrValPID.setValue(patientID);
            attrPID.getAttributeValues().add(attrValPID);
            attrStmt.getAttributes().add(attrPID);

            //Creating the Attribute that holds the Purpose of Use
            Attribute attrPoU = create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);
            attrPoU.setFriendlyName("XSPA Purpose Of Use");

            // TODO: Is there a constant for that urn??
            attrPoU.setName("urn:oasis:names:tc:xspa:1.0:subject:purposeofuse");
            attrPoU.setNameFormat(Attribute.URI_REFERENCE);
            if (purposeOfUse == null) {
                attrPoU = findStringInAttributeStatement(hcpIdentityAssertion.getAttributeStatements(), "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse");
                if (attrPoU == null) {
                    throw new SMgrException("Puprose of use not found in the assertion and is not passed as a parameter");
                }
            } else {
                XSString attrValPoU = (XSString) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
                attrValPoU.setValue(purposeOfUse);
                attrPoU.getAttributeValues().add(attrValPoU);
            }
            attrStmt.getAttributes().add(attrPoU);

            String poc = ( (XSString) findStringInAttributeStatement(hcpIdentityAssertion.getAttributeStatements(), "urn:oasis:names:tc:xspa:1.0:subject:organization").getAttributeValues().get(0) ).getValue();

            logger.info("Point of Care: {0}", poc);
            auditDataMap.put("pointOfCare", poc);

            String pocId = ( (XSURI) findURIInAttributeStatement(hcpIdentityAssertion.getAttributeStatements(), "urn:oasis:names:tc:xspa:1.0:subject:organization-id").getAttributeValues().get(0) ).getValue();

            logger.info("Point of Care id: {0}", pocId);
            auditDataMap.put("pointOfCareID", pocId);

            String hrRole = ( (XSString) findStringInAttributeStatement(hcpIdentityAssertion.getAttributeStatements(), "urn:oasis:names:tc:xacml:2.0:subject:role").getAttributeValues().get(0) ).getValue();

            logger.info("HR Role {0}", hrRole);
            auditDataMap.put("humanRequestorRole", hrRole);

            String facilityType = ( (XSString) findStringInAttributeStatement(hcpIdentityAssertion.getAttributeStatements(),
                    "urn:epsos:names:wp3.4:subject:healthcare-facility-type").getAttributeValues().get(0) ).getValue();

            logger.info("Facility Type {0}", facilityType);
            auditDataMap.put("facilityType", facilityType);

            sman.signSAMLAssertion(trc);
            return trc;
        }
        catch (NoSuchAlgorithmException ex) {
            logger.error(null, ex);
            throw new SMgrException(ex.getMessage());
        }
    }

    /**
     * Verifies the signature of the TRC Assertion. The TRC Assertion should be
     * signed and will be validated also against both the patient ID and the HCP
     * Identity that is provided by the IdentityAssertion.
     *
     * @param trcAssertion The Assertion that is to be validated.
     * @param hcpIdentityAssertion The health care professional Identity SAML
     * Assertion. The method validates the assertion using the
     * {@link SignatureManager#verifySAMLAssestion(org.opensaml.saml2.core.Assertion)}.
     * @param patientID The Patient Id that is required for the TRC Assertion
     * @throws SMgrException when the verification fails.
     * @throws IOException 
     */
    public void verifyTrcToken(Assertion trcAssertion,
            Assertion hcpIdentityAssertion,
            String patientID)
            throws SMgrException, IOException {

        SignatureManager sm = new SignatureManager(ksm);

        sm.verifySAMLAssestion(trcAssertion);
        sm.verifySAMLAssestion(hcpIdentityAssertion);

    }
    
    protected Attribute createAttribute(String value, String friendlyName, String nameFormat, String name) {
        Attribute attr = create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);

        attr.setFriendlyName(friendlyName);
        attr.setName(name);
        attr.setNameFormat(nameFormat);


        XMLObjectBuilder stringBuilder = Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
        XSString attrVal = (XSString) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
        attrVal.setValue(value);
        attr.getAttributeValues().add(attrVal);
        return attr;
    }

    protected Attribute findStringInAttributeStatement(List<AttributeStatement> statements, String attrName) {
        logger.info("Size:{0}", statements.size());
        for (AttributeStatement stmt : statements) {
            for (Attribute attribute : stmt.getAttributes()) {
                if (attribute.getName().equals(attrName)) {
                    logger.info("Attribute Name:{0}", attribute.getName());
                    Attribute attr = create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);

                    attr.setFriendlyName(attribute.getFriendlyName());
                    attr.setName(attribute.getName());
                    attr.setNameFormat(attribute.getNameFormat());


                    XMLObjectBuilder stringBuilder = Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
                    XSString attrVal = (XSString) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
                    attrVal.setValue(( (XSString) attribute.getAttributeValues().get(0) ).getValue());
                    attr.getAttributeValues().add(attrVal);

                    return attr;
                }

            }

        }

        return null;
    }

    protected Attribute findURIInAttributeStatement(List<AttributeStatement> statements, String attrName) {
        logger.info("Size:{0}", statements.size());
        for (AttributeStatement stmt : statements) {
            for (Attribute attribute : stmt.getAttributes()) {
                if (attribute.getName().equals(attrName)) {
                    logger.info("Attribute Name:{0}", attribute.getName());
                    Attribute attr = create(Attribute.class, Attribute.DEFAULT_ELEMENT_NAME);

                    attr.setFriendlyName(attribute.getFriendlyName());
                    attr.setName(attribute.getNameFormat());
                    attr.setNameFormat(attribute.getNameFormat());


                    XMLObjectBuilder uriBuilder = Configuration.getBuilderFactory().getBuilder(XSURI.TYPE_NAME);
                    XSURI attrVal = (XSURI) uriBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSURI.TYPE_NAME);

                    attrVal.setValue(( (XSURI) attribute.getAttributeValues().get(0) ).getValue());
                    attr.getAttributeValues().add(attrVal);

                    return attr;
                }
            }
        }
        return null;
    }

    protected NameID findProperNameID(Subject subject) throws SMgrException {

        String format = subject.getNameID().getFormat();
        logger.info("is email?: {0}", format.equals(NameID.EMAIL));
        logger.info("is x509 subject?: {0}", format.equals(NameID.X509_SUBJECT));
        logger.info("is Unspecified?: {0}", format.equals(NameID.UNSPECIFIED));

        //if (format.equals(NameID.EMAIL) || format.equals(NameID.X509_SUBJECT) || format.equals(NameID.UNSPECIFIED)) {
        //        logger.info("Compatible NameID found");
        NameID n = create(NameID.class, NameID.DEFAULT_ELEMENT_NAME);
        n.setFormat(format);
        n.setValue(subject.getNameID().getValue());
        return n;

        // }
    }

    /**
     * Helper Funciton that makes it easy to create a new OpenSAML Obejct, using
     * the default namespace prefixes.
     *
     * @param <T> The Type of OpenSAML Class that will be created
     * @param cls the openSAML Class
     * @param qname The Qname of the Represented XML element.
     * @return the new OpenSAML object of type T
     */
    public static <T> T create(Class<T> cls, QName qname) {
        return (T) ( (XMLObjectBuilder) Configuration.getBuilderFactory().getBuilder(qname) ).buildObject(qname);
    }

    protected NameID getXspaSubjectFromAttributes(List<AttributeStatement> stmts) {
        Attribute xspaSubjectAttribute = findStringInAttributeStatement(stmts, "urn:oasis:names:tc:xacml:1.0:subject:subject-id");

        NameID n = create(NameID.class, NameID.DEFAULT_ELEMENT_NAME);
        n.setFormat(NameID.UNSPECIFIED);
        n.setValue(( (XSString) xspaSubjectAttribute.getAttributeValues().get(0) ).getValue());

        return n;

    }

    public String getPointofCare() {
        logger.info("Point Of Care: {0}", auditDataMap.get("pointOfCare"));
        return auditDataMap.get("pointOfCare");
    }

    public String getPointofCareID() {
        logger.info("Point Of Care ID: {0}", auditDataMap.get("pointOfCareID"));
        return auditDataMap.get("pointOfCareID");
    }

    public String getHumanRequestorNameId() {
        logger.info("human Requestor NameID: {0}", auditDataMap.get("humanRequestorNameID"));
        return auditDataMap.get("humanRequestorNameID");
    }

    public String getHumanRequestorSubjectId() {
        logger.info("human Requestor subjectID: {0}", auditDataMap.get("humanRequestorSubjectID"));
        return auditDataMap.get("humanRequestorSubjectID");
    }

    public String getHRRole() {
        logger.info("human Requestor Role: {0}", auditDataMap.get("humanRequestorRole"));
        return auditDataMap.get("humanRequestorRole");
    }
    
    public String getFacilityType() {
        logger.info("Facility Type: {0}", auditDataMap.get("facilityType"));
        return auditDataMap.get("facilityType");
    }
}
