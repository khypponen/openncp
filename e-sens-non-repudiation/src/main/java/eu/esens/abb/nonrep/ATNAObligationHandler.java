package eu.esens.abb.nonrep;

import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.herasaf.xacml.core.policy.impl.AttributeAssignmentType;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class ATNAObligationHandler implements ObligationHandler {

	private IHEMessageType messageType;
	private List<ESensObligation> obligations;
	private final static String ATNA_PREFIX = "urn:eSENS:obligations:nrr:ATNA";
	private final HashMap<String, String> auditValueMap = new HashMap<String, String>();
	private Document audit = null;
	private Context context;

	public ATNAObligationHandler(MessageType messageType,
			List<ESensObligation> obligations, 
			Context context) {
		this.messageType = (IHEMessageType)messageType;
		this.obligations = obligations;
		this.context = context;
	}

	/**
	 *  Discharge returns the object discharged, or exception(non-Javadoc)
	 * @throws ObligationDischargeException 
	 * @see eu.esens.abb.nonrep.ObligationHandler#discharge()
	 */

	@Override
	public void discharge() throws ObligationDischargeException {
		/*
		 * Here I need to check the IHE message type. It can be XCA, XCF, whatever
		 */

		if ( messageType instanceof IHEXCARetrieve ) {
			try {
				makeIHEXCARetrieveAudit((IHEXCARetrieve)messageType, obligations);
			} catch (ParserConfigurationException e) {
				throw new ObligationDischargeException(e);
			}
		} else {
			throw new ObligationDischargeException("Unkwnon message type");
		}
	}

	private void makeIHEXCARetrieveAudit(IHEXCARetrieve messageType2,
			List<ESensObligation> obligations2) throws ParserConfigurationException {
		int size = obligations2.size();

		for ( int i=0; i<size; i++ ) {
			ESensObligation eSensObl = obligations2.get(i);

			// Here I am in the ATNA handler, thus I have to check if it is prefixed by ATNA

			if (eSensObl.getObligationID().startsWith(ATNA_PREFIX) ) {
				String outcome = null;
				if ( eSensObl instanceof PERMITEsensObligation ) {
					outcome = "SUCCESS";
				}
				else {
					outcome = "MINOR FAILURE";
				}

				// Here the real mapping happens: are we NRR or NRO? I think both, and it depends on the policy
				List<AttributeAssignmentType> attributeAssignments = eSensObl.getAttributeAssignments();

				if ( attributeAssignments != null ) {
					int attributeSize = attributeAssignments.size();

					for ( int j = 0; j< attributeSize; j++ ) {
						AttributeAssignmentType aat = attributeAssignments.get(j);

						if ( aat.getAttributeId().startsWith(ATNA_PREFIX) ) {
							fillHash(aat.getAttributeId(), aat.getContent());
						}
					}
				}


				makeAuditXml(outcome);
			}
			// else skip, it's not an ATNA obligation
		}

	}

	private void makeAuditXml(String outcome) throws ParserConfigurationException {

		/*
		 * Read the values that you need from the auditValueMap and fill the XML.
		 * 
		 * The idea is to let configurable some parts. The configurable options are defined in the policy
		 * and if they are available in the hashmap, use them, if not, use the presets. 
		 * 
		 * The idea is to call here the OpenNCP implementation. This is just a placeholder
		 */

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document audit = db.newDocument();

		Element auditMessage = audit.createElement("AuditMessage");
		audit.appendChild(auditMessage);
		Element eventIdentification = audit.createElement("EventIdentification");
		eventIdentification.setAttribute("EventActionCode", auditValueMap.get("EventActionCode").trim()!=null?auditValueMap.get("EventActionCode").trim():"N/A");
		eventIdentification.setAttribute("EventDateTime", new DateTime().toString());
		eventIdentification.setAttribute("EventOutcomeIndicator", auditValueMap.get("EventOutcomeIndicator").trim()!=null?auditValueMap.get("EventOutcomeIndicator").trim():"N/A"); // by default, we fail

		Element eventId = audit.createElement("EventID");
		eventIdentification.appendChild(eventId);
		eventId.setAttribute("code", auditValueMap.get("EventID").trim()!=null?auditValueMap.get("EventID").trim():"N/A");
		eventId.setAttribute("codeSystem", "");
		eventId.setAttribute("codeSystemName", "DCM");
		eventId.setAttribute("displayName", "Export");

		//What have been done?
		Element eventTypeCode = audit.createElement("EventTypeCode");
		eventIdentification.appendChild(eventTypeCode);
		eventTypeCode.setAttribute("code", auditValueMap.get("EventTypeCode").trim()!=null?auditValueMap.get("EventTypeCode").trim():"N/A");
		eventTypeCode.setAttribute("codeSystem", "");
		eventTypeCode.setAttribute("codeSystemName", "IHE Transactions");
		eventTypeCode.setAttribute("displayName", "Retrieve Document Set");

		auditMessage.appendChild(eventIdentification);

		// Initiated from where
		Element activeParticipant1 = audit.createElement("ActiveParticipant");
		activeParticipant1.setAttribute("AlternativeUserID", context.getUsername().trim()!=null?context.getUsername().trim():"N/A");
		activeParticipant1.setAttribute("NetworkAccessPointID", context.getCurrentHost().trim()!=null?context.getCurrentHost().trim():"N/A");
		activeParticipant1.setAttribute("NetworkAccessPointTypeCode", "2");
		activeParticipant1.setAttribute("UserID", "1.2.3.4.5.1.1000.990.1.1.1.22");
		activeParticipant1.setAttribute("UserIsRequestor", "false");
		activeParticipant1.setAttribute("UserName", "");
		Element roleIdCode1 = audit.createElement("RoleIDCode");
		activeParticipant1.appendChild(roleIdCode1);
		roleIdCode1.setAttribute("code", "110153");
		roleIdCode1.setAttribute("codeSystem", "");
		roleIdCode1.setAttribute("codeSystemName", "DCM");
		roleIdCode1.setAttribute("displayName", "Source");

		// What is the recipient?
		Element activeParticipant2 = audit.createElement("ActiveParticipant");
		activeParticipant2.setAttribute("NetworkAccessPointID", context.getRemoteHost().trim()!=null?context.getRemoteHost().trim():"N/A");
		activeParticipant2.setAttribute("NetworkAccessPointTypeCode", "2");
		activeParticipant2.setAttribute("UserID", "http://www.w3.org/2005/08/addressing/anonymous");
		activeParticipant2.setAttribute("UserIsRequestor", "true");
		activeParticipant2.setAttribute("UserName", "");
		Element roleIdCode2 = audit.createElement("RoleIDCode");
		activeParticipant2.appendChild(roleIdCode2);
		roleIdCode2.setAttribute("code", "110152");
		roleIdCode2.setAttribute("codeSystem", "");
		roleIdCode2.setAttribute("codeSystemName", "DCM");
		roleIdCode2.setAttribute("displayName", "Destination");

		// Who is the physician?
		Element activeParticipant3 = audit.createElement("ActiveParticipant");
		activeParticipant3.setAttribute("UserID", context.getUsername().trim()!=null?context.getUsername().trim():"N/A");
		activeParticipant3.setAttribute("UserIsRequestor", "true");
		activeParticipant3.setAttribute("UserName", "");
		Element roleIdCode3 = audit.createElement("RoleIDCode");
		activeParticipant3.appendChild(roleIdCode3);
		roleIdCode3.setAttribute("code", "USR");
		roleIdCode3.setAttribute("codeSystem", "1.3.6.1.4.1.21998.2.1.5");
		roleIdCode3.setAttribute("codeSystemName", "Tiani-Spirit Audit Participant Role ID Codes");
		roleIdCode3.setAttribute("displayName", "User");

		auditMessage.appendChild(activeParticipant1);
		auditMessage.appendChild(activeParticipant2);
		auditMessage.appendChild(activeParticipant3);

		// Who is the audit client?
		Element auditSourceIdentification = audit.createElement("AuditSourceIdentification");
		auditSourceIdentification.setAttribute("AuditEnterpriseSiteID", auditValueMap.get("AuditEnterpriseSiteID").trim()!=null?auditValueMap.get("AuditEnterpriseSiteID").trim():"N/A");
		auditSourceIdentification.setAttribute("AuditSourceID", "urn:epsos:ncpa");
		Element auditSourceTypeCode = audit.createElement("AuditSourceTypeCode");
		auditSourceIdentification.appendChild(auditSourceTypeCode);
		auditSourceTypeCode.setAttribute("code", "DOC_REPOSITORY");
		auditSourceTypeCode.setAttribute("codeSystem", "");
		auditSourceTypeCode.setAttribute("codeSystemName", "IHE Actors");
		auditSourceTypeCode.setAttribute("displayName", "IHE Document Repository");

		auditMessage.appendChild(auditSourceIdentification);

		//To which patient?
		Element participantObjectIdentification1 = audit.createElement("ParticipantObjectIdentification");
		participantObjectIdentification1.setAttribute("ParticipantObjectDataLifeCycle", "1");
		participantObjectIdentification1.setAttribute("ParticipantObjectID", "");
		participantObjectIdentification1.setAttribute("ParticipantObjectTypeCode", "1");
		participantObjectIdentification1.setAttribute("ParticipantObjectTypeCodeRole", "1");
		Element participantObjectIdTypeCode1 = audit.createElement("ParticipantObjectIDTypeCode");
		participantObjectIdentification1.appendChild(participantObjectIdTypeCode1);
		participantObjectIdTypeCode1.setAttribute("code", "2");
		participantObjectIdTypeCode1.setAttribute("codeSystem", "");
		participantObjectIdTypeCode1.setAttribute("codeSystemName", "RFC-3881");
		participantObjectIdTypeCode1.setAttribute("displayName", "Patient Number");

		// To which resource?
		Element participantObjectIdentification2 = audit.createElement("ParticipantObjectIdentification");
		participantObjectIdentification2.setAttribute("ParticipantObjectID", "1.2.40.0.13.1.1.2117081378.20130402104335703.34529");
		participantObjectIdentification2.setAttribute("ParticipantObjectTypeCode", "2");
		participantObjectIdentification2.setAttribute("ParticipantObjectTypeCodeRole", "3");
		Element participantObjectIdTypeCode2 = audit.createElement("ParticipantObjectIDTypeCode");
		participantObjectIdentification2.appendChild(participantObjectIdTypeCode2);
		participantObjectIdTypeCode2.setAttribute("code", "9");
		participantObjectIdTypeCode2.setAttribute("codeSystem", "");
		participantObjectIdTypeCode2.setAttribute("codeSystemName", "RFC-3881");
		participantObjectIdTypeCode2.setAttribute("displayName", "Report Number");

		Element participantObjectIdentification3 = audit.createElement("ParticipantObjectIdentification");
		participantObjectIdentification3.setAttribute("ParticipantObjectDataLifeCycle", "9");
		participantObjectIdentification3.setAttribute("ParticipantObjectID", "17d77343-88b5-4aad-8d37-ce68ce720060");
		participantObjectIdentification3.setAttribute("ParticipantObjectTypeCode", "2");
		participantObjectIdentification3.setAttribute("ParticipantObjectTypeCodeRole", "3");
		Element participantObjectIdTypeCode3 = audit.createElement("ParticipantObjectIDTypeCode");
		participantObjectIdentification3.appendChild(participantObjectIdTypeCode3);
		participantObjectIdTypeCode3.setAttribute("code", auditValueMap.get("EventTypeCode").trim()!=null?auditValueMap.get("EventTypeCode").trim():"N/A");
		participantObjectIdTypeCode3.setAttribute("codeSystem", "");
		participantObjectIdTypeCode3.setAttribute("codeSystemName", "IHE Transactions");
		participantObjectIdTypeCode3.setAttribute("displayName", "Retrieve Document Set");
		Element participantObjectName = audit.createElement("ParticipantObjectName");
		participantObjectName.setTextContent("Transaction ID");
		participantObjectIdentification3.appendChild(participantObjectName);
		Element participantObjectDetail = audit.createElement("ParticipantObjectDetail");
		participantObjectDetail.setAttribute("type", "DURATION");
		participantObjectDetail.setAttribute("value", "Mg==");

		participantObjectIdentification3.appendChild(participantObjectDetail);

		auditMessage.appendChild(participantObjectIdentification1);
		auditMessage.appendChild(participantObjectIdentification2);
		auditMessage.appendChild(participantObjectIdentification3);

		setMessage(auditMessage.getOwnerDocument());




	}

	private void fillHash(String attributeId, List<Object> content) {
		// +1 is the colon
		auditValueMap.put(attributeId.substring(ATNA_PREFIX.length()+1), (String)content.get(0));

	}

	private void setMessage(Document audit) {
		this.audit = audit;
	}
	@Override
	public Document getMessage() {
		return audit;
	}

}
