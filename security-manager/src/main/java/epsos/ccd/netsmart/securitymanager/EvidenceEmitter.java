package epsos.ccd.netsmart.securitymanager;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import epsos.ccd.netsmart.securitymanager.exceptions.SMgrException;

public interface EvidenceEmitter {

	/**
	 * @param uuid
	 * @param policyId
	 * @param issuerCertificate
	 * @param authenticationTime
	 * @param authenticationMethod
	 * @param senderCertificateDetails
	 * @param recipientCertificateDetails
	 * @param evidenceEvent
	 * @param uaMessageIdentifier
	 * @param digest 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws SMgrException 
	 * @throws Exception 
	 * 
	 */
	public Element emitNRO(String uuid, String policyId,
			String issuerCertificate, String authenticationTime,
			String authenticationMethod, String senderCertificateDetails,
			String recipientCertificateDetails, String evidenceEvent,
			String uaMessageIdentifier, String digest)
			throws ParserConfigurationException, SAXException, IOException,
			SMgrException, Exception;

	/**
	 * 
	 * @param uuid
	 * @param policyId
	 * @param issuerCertificate
	 * @param authenticationTime
	 * @param authenticationMethod
	 * @param senderCertificateDetails
	 * @param recipientCertificateDetails
	 * @param evidenceEvent
	 * @param uaMessageIdentifier
	 * @param digest
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SMgrException
	 * @throws Exception 
	 */
	public Element emitNRR(String uuid, String policyId,
			String issuerCertificate, String authenticationTime,
			String authenticationMethod, String senderCertificateDetails,
			String recipientCertificateDetails, String evidenceEvent,
			String uaMessageIdentifier, String digest)
			throws ParserConfigurationException, SAXException, IOException,
			SMgrException, Exception;

}