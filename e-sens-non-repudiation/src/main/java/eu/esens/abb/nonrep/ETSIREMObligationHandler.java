package eu.esens.abb.nonrep;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.herasaf.xacml.core.policy.impl.AttributeAssignmentType;
import org.joda.time.DateTime;
import org.w3c.dom.Document;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import eu.esens.abb.nonrep.etsi.rem.AuthenticationDetailsType;
import eu.esens.abb.nonrep.etsi.rem.CertificateDetails;
import eu.esens.abb.nonrep.etsi.rem.EntityDetailsType;
import eu.esens.abb.nonrep.etsi.rem.EntityName;
import eu.esens.abb.nonrep.etsi.rem.EvidenceIssuerPolicyID;
import eu.esens.abb.nonrep.etsi.rem.MessageDetailsType;
import eu.esens.abb.nonrep.etsi.rem.NamePostalAddress;
import eu.esens.abb.nonrep.etsi.rem.NamesPostalAddresses;
import eu.esens.abb.nonrep.etsi.rem.ObjectFactory;
import eu.esens.abb.nonrep.etsi.rem.REMEvidenceType;
import eu.esens.abb.nonrep.etsi.rem.RecipientsDetails;

/**
 * This class is a sample discharge of the Evidence Emitter.
 * 
 * @author max
 *
 */
public class ETSIREMObligationHandler implements ObligationHandler {

	private List<ESensObligation> obligations;

	// Prefixes, that matches the XACML policy
	private static final String REM_NRR_PREFIX = "urn:eSENS:obligations:nrr:ETSIREM";
	private static final String REM_NRO_PREFIX = "urn:eSENS:obligations:nro:ETSIREM";
	private static final String REM_NRD_PREFIX = "urn:eSENS:obligations:nrd:ETSIREM";

	private Document audit = null;
	private Context context;

	static final String SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";

	public ETSIREMObligationHandler(MessageType messageType, List<ESensObligation> obligations, Context context) {
		this.obligations = obligations;
		this.context = context;
	}

	/**
	 * Discharge returns the object discharged, or exception(non-Javadoc)
	 *
	 * @throws ObligationDischargeException
	 * @see eu.esens.abb.nonrep.ObligationHandler#discharge()
	 */
	@Override
	public void discharge() throws ObligationDischargeException {
		/*
		 * Here I need to check the IHE message type. It can be XCA, XCF,
		 * whatever
		 */

		// if (messageType instanceof IHEXCARetrieve) {
		// try {
		// makeIHEXCARetrieveAudit((IHEXCARetrieve) messageType,
		// obligations);
		// } catch (DatatypeConfigurationException e) {
		// throw new ObligationDischargeException(e);
		// }
		// } else {
		// throw new ObligationDischargeException("Unkwnon message type");
		// }

		/*
		 * For the e-SENS pilot we issue the NRO and NRR token to all the
		 * incoming messages -> This is the per hop protocol.
		 */
		try {
			makeETSIREM();
		} catch (Exception e) {
			throw new ObligationDischargeException(e);
		}
	}

	private void makeETSIREM() throws DatatypeConfigurationException, JAXBException, CertificateEncodingException,
			NoSuchAlgorithmException, IOException, SOAPException, ParserConfigurationException, XMLSecurityException {
		int size = obligations.size();
		JAXBContext jc = JAXBContext.newInstance("eu.esens.abb.nonrep.etsi.rem");

		ObjectFactory of = new ObjectFactory();
		REMEvidenceType type = new REMEvidenceType();

		for (int i = 0; i < size; i++) {
			ESensObligation eSensObl = obligations.get(i);
			System.out.println(eSensObl.getObligationID());
			System.out.println(REM_NRO_PREFIX);
			if (eSensObl.getObligationID().equals(REM_NRO_PREFIX)) {
				String outcome = null;
				if (eSensObl instanceof PERMITEsensObligation) {
					outcome = "Acceptance";
				} else {
					outcome = "Rejection";
				}
				List<AttributeAssignmentType> listAttr = eSensObl.getAttributeAssignments();

				type.setVersion(find(REM_NRO_PREFIX + ":version", listAttr));
				type.setEventCode(outcome);

				type.setEvidenceIdentifier(UUID.randomUUID().toString());

				/*
				 * ISO Token mappings
				 */
				// This is the Pol field of the ISO13888 token
				EvidenceIssuerPolicyID eipid = new EvidenceIssuerPolicyID();
				eipid.getPolicyIDs().add(find(REM_NRO_PREFIX + ":PolicyID", listAttr));
				type.setEvidenceIssuerPolicyID(eipid);

				mapToIso(type);

				// Imp is the signature

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.newDocument();

				JAXBElement<REMEvidenceType> back = of.createSubmissionAcceptanceRejection(type);
				Marshaller marshaller = jc.createMarshaller();
				marshaller.marshal(back, doc);

				sign(doc, context.getIssuerCertificate(), context.getSigningKey());
				audit = doc;
			} else if (eSensObl.getObligationID().equals(REM_NRR_PREFIX)) { // it
																			// is
																			// an
																			// NRR,
																			// AcceptanceRejectionByRecipient

				String outcome = null;
				if (eSensObl instanceof PERMITEsensObligation) {
					outcome = "Acceptance";
				} else {
					outcome = "Rejection";
				}
				List<AttributeAssignmentType> listAttr = eSensObl.getAttributeAssignments();

				type.setVersion(find(REM_NRO_PREFIX + ":version", listAttr));
				type.setEventCode(outcome);

				type.setEvidenceIdentifier(UUID.randomUUID().toString());

				/*
				 * ISO Token mappings
				 */
				// This is the Pol field of the ISO13888 token
				EvidenceIssuerPolicyID eipid = new EvidenceIssuerPolicyID();
				eipid.getPolicyIDs().add(find(REM_NRO_PREFIX + ":PolicyID", listAttr));
				type.setEvidenceIssuerPolicyID(eipid);

				mapToIso(type);

				// Imp is the signature

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.newDocument();

				JAXBElement<REMEvidenceType> back = of.createAcceptanceRejectionByRecipient(type);
				Marshaller marshaller = jc.createMarshaller();
				marshaller.marshal(back, doc);

				sign(doc, context.getIssuerCertificate(), context.getSigningKey());
				audit = doc;

			} else if (eSensObl.getObligationID().equals(REM_NRD_PREFIX)) {
				String outcome = null;
				if (eSensObl instanceof PERMITEsensObligation) {
					outcome = "Delivery";
				} else {
					outcome = "DeliveryExpiration";
				}
				List<AttributeAssignmentType> listAttr = eSensObl.getAttributeAssignments();

				type.setVersion(find(REM_NRD_PREFIX + ":version", listAttr));
				type.setEventCode(outcome);

				type.setEvidenceIdentifier(UUID.randomUUID().toString());

				/*
				 * ISO Token mappings
				 */
				// This is the Pol field of the ISO13888 token
				String policyUrl = find(REM_NRD_PREFIX + ":PolicyID", listAttr);
				if (policyUrl != null) {
					EvidenceIssuerPolicyID eipid = new EvidenceIssuerPolicyID();
					eipid.getPolicyIDs().add(policyUrl);
					type.setEvidenceIssuerPolicyID(eipid);
				}
				mapToIso(type);

				// Imp is the signature

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.newDocument();

				JAXBElement<REMEvidenceType> back = of.createDeliveryNonDeliveryToRecipient(type);
				Marshaller marshaller = jc.createMarshaller();
				marshaller.marshal(back, doc);

				sign(doc, context.getIssuerCertificate(), context.getSigningKey());
				audit = doc;
			}
		} // for

	}

	private void mapToIso(REMEvidenceType type)
			throws CertificateEncodingException, NoSuchAlgorithmException, IOException, SOAPException {

		// The flag f1 is the AcceptanceRejection (the evidence type)
		// This is the A field the originator
		EntityDetailsType sedt1 = new EntityDetailsType();

		if (context.getSenderCertificate() != null) {
			CertificateDetails cd1 = new CertificateDetails();
			sedt1.setCertificateDetails(cd1);
			cd1.setX509Certificate(context.getSenderCertificate().getEncoded());
		}
		if (context.getSenderNamePostalAddress() != null) {
			LinkedList<String> slist = context.getSenderNamePostalAddress();
			int size = slist.size();
			
			NamesPostalAddresses snpas = new NamesPostalAddresses();

			for (int i=0; i<size; i++) {
				EntityName sen = new EntityName();
				sen.getNames().add(slist.get(i));
				NamePostalAddress snpa = new NamePostalAddress();
				snpa.setEntityName(sen);
				snpas.getNamePostalAddresses().add(snpa);

			}
			sedt1.setNamesPostalAddresses(snpas);
		}
		
		
		type.setSenderDetails(sedt1); // To check if null sender details is
										// allowed

		// This is the B field, the recipient
		/*
		 * Made optional by a request from the eJustice domain
		 */
		EntityDetailsType edt2 = new EntityDetailsType();

		if (context.getRecipientCertificate() != null) {

			CertificateDetails cd2 = new CertificateDetails();
			edt2.setCertificateDetails(cd2);
			cd2.setX509Certificate(context.getRecipientCertificate().getEncoded());

		}
		if (context.getRecipientNamePostalAddress() != null) {
			LinkedList<String> list = context.getRecipientNamePostalAddress();
			int size = list.size();
			
			NamesPostalAddresses npas = new NamesPostalAddresses();

			for (int i=0; i<size; i++) {
				EntityName en = new EntityName();
				en.getNames().add(list.get(i));
				NamePostalAddress npa = new NamePostalAddress();
				npa.setEntityName(en);
				npas.getNamePostalAddresses().add(npa);

			}
			edt2.setNamesPostalAddresses(npas);
		}

		RecipientsDetails rd = new RecipientsDetails();
		rd.getEntityDetails().add(edt2);
		type.setRecipientsDetails(rd);

		// Evidence Issuer Details is the C field of the ISO token
		EntityDetailsType edt = new EntityDetailsType();
		CertificateDetails cd = new CertificateDetails();
		edt.setCertificateDetails(cd);
		cd.setX509Certificate(context.getIssuerCertificate().getEncoded());
		type.setEvidenceIssuerDetails(edt);

		// This is the T_g field
		DateTime dt = new DateTime();
		type.setEventTime(new XMLGregorianCalendarImpl(dt.toGregorianCalendar()));

		// This is the T_1 field
		type.setSubmissionTime(new XMLGregorianCalendarImpl(context.getSubmissionTime().toGregorianCalendar()));

		// This is mandated by REM. If this is the full message,
		// we can avoid to build up the NROT Token as text||z_1||sa(z_1)
		MessageDetailsType mdt = new MessageDetailsType();
		eu.esens.abb.nonrep.etsi.rem.DigestMethod dm = new eu.esens.abb.nonrep.etsi.rem.DigestMethod();
		dm.setAlgorithm("SHA1");
		mdt.setDigestMethod(dm);

		// do the message digest
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.reset();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if (context.getIncomingMsg() != null) {
			Utilities.serialize(context.getIncomingMsg().getSOAPBody().getOwnerDocument().getDocumentElement(), baos);

		} else if (context.getIncomingMsgAsDocument() != null) {
			Utilities.serialize(context.getIncomingMsgAsDocument().getDocumentElement(), baos);

		} else {
			throw new IllegalStateException("No valid incoming msg passed");
		}
		md.update(baos.toByteArray());
		mdt.setDigestValue(md.digest());

		mdt.setIsNotification(false);
		mdt.setMessageSubject(context.getEvent());
		mdt.setUAMessageIdentifier(context.getMessageUUID());
		mdt.setMessageIdentifierByREMMD(context.getMessageUUID()); // again,
																	// here I
																	// put the
																	// UUID of
																	// the
																	// message,
																	// we don't
																	// handle
																	// the local
																	// parts.
		mdt.setDigestMethod(dm);
		type.setSenderMessageDetails(mdt);

		AuthenticationDetailsType adt = new AuthenticationDetailsType();
		adt.setAuthenticationMethod(context.getAuthenticationMethod());
		// this is the authentication time. I set it as "now", since it is
		// required by the REM, but it is not used here.
		adt.setAuthenticationTime((new XMLGregorianCalendarImpl(new DateTime().toGregorianCalendar())));

		type.setSenderAuthenticationDetails(adt);

	}

	private String find(String string, List<AttributeAssignmentType> listAttr) {
		int size = listAttr.size();
		for (int i = 0; i < size; i++) {
			AttributeAssignmentType att = listAttr.get(i);
			if (att.getAttributeId().equals(string)) {
				return ((String) att.getContent().get(0)).trim();
			}
		}
		return null;
	}

	private void sign(Document doc, X509Certificate cert, PrivateKey key) throws XMLSecurityException {
		String BaseURI = "./";
		XMLSignature sig = new XMLSignature(doc, BaseURI,
				org.apache.xml.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA);
		doc.getDocumentElement().appendChild(sig.getElement());

		doc.appendChild(doc.createComment(" Comment after "));

		Transforms transforms = new Transforms(doc);

		transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
		transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
		sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

		sig.addKeyInfo(cert);
		sig.addKeyInfo(cert.getPublicKey());

		sig.sign(key);

	}

	@Override
	public Document getMessage() {
		return audit;
	}

}
