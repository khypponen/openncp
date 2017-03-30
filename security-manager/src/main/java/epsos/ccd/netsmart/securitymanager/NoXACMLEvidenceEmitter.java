package epsos.ccd.netsmart.securitymanager;

import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;
import epsos.ccd.netsmart.securitymanager.key.KeyStoreManager;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
 * This class creates the two ETSI REM evidences for the TRC issuance.
 *
 * @author max
 */
public class NoXACMLEvidenceEmitter implements EvidenceEmitter {

    /**
     * This variable contains the velocity engine context.
     */
    private final VelocityContext context;
    private KeyStoreManager ks;

    /**
     * Constructor. It instantiates the velocity context.
     *
     * @throws Exception
     */
    public NoXACMLEvidenceEmitter(KeyStoreManager ks) throws Exception {
        Velocity.init();
        context = new VelocityContext();
        this.ks = ks;
    }

    /* (non-Javadoc)
     * @see eu.esens.abb.eid.EvidenceEmitter#emitNRO(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public final Element emitNRO(String uuid, String policyId,
                                 String issuerCertificate, String authenticationTime,
                                 String authenticationMethod, String senderCertificateDetails,
                                 String recipientCertificateDetails, String evidenceEvent,
                                 String uaMessageIdentifier, String digest) throws Exception {

        context.put("evidenceIdentifier", uuid);
        context.put("evidenceIssuerPolicyId", policyId);
        context.put("evidenceIssuerCertificate", issuerCertificate);
        context.put("authenticationTime", authenticationTime);
        context.put("authenticationMethod", authenticationMethod);
        context.put("senderCertificateDetails", senderCertificateDetails);
        context.put("recipientCertificateDetails", recipientCertificateDetails);
        context.put("evidenceEvent", evidenceEvent);
        context.put("uaMessageIdentifier", uaMessageIdentifier);
        context.put("messageDigestValue", digest);
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
        ve.setProperty("class.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        ve.init();

        Template te = ve.getTemplate("nro.xml", "UTF-8");
        StringWriter sw = new StringWriter();
        te.merge(context, sw);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(sw.toString().getBytes()));

        return sign(doc);
    }

    /* (non-Javadoc)
     * @see eu.esens.abb.eid.EvidenceEmitter#emitNRR(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public final Element emitNRR(String uuid, String policyId,
                                 String issuerCertificate, String authenticationTime,
                                 String authenticationMethod, String senderCertificateDetails,
                                 String recipientCertificateDetails, String evidenceEvent,
                                 String uaMessageIdentifier, String digest) throws Exception {

        context.put("evidenceIdentifier", uuid);
        context.put("evidenceIssuerPolicyId", policyId);
        context.put("evidenceIssuerCertificate", issuerCertificate);
        context.put("authenticationTime", authenticationTime);
        context.put("authenticationMethod", authenticationMethod);
        context.put("senderCertificateDetails", senderCertificateDetails);
        context.put("recipientCertificateDetails", recipientCertificateDetails);
        context.put("evidenceEvent", evidenceEvent);
        context.put("uaMessageIdentifier", uaMessageIdentifier);
        context.put("messageDigestValue", digest);
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty("class.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
        ve.init();

        Template te = ve.getTemplate("nrr.xml", "UTF-8");
        StringWriter sw = new StringWriter();
        te.merge(context, sw);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(sw.toString().getBytes()));

        return sign(doc);
    }


    private Element sign(Document doc) throws SMgrException, IOException {
        SignatureManager m = new SignatureManager(ks);
        m.signXMLWithEnvelopedSig(doc);
        return doc.getDocumentElement();
    }
}
