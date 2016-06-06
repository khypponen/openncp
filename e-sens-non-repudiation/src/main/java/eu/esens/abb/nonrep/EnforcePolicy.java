package eu.esens.abb.nonrep;

import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.context.RequestMarshaller;
import org.herasaf.xacml.core.context.ResponseMarshaller;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResponseType;
import org.herasaf.xacml.core.context.impl.ResultType;
import org.herasaf.xacml.core.policy.impl.AttributeAssignmentType;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.ObligationType;
import org.herasaf.xacml.core.policy.impl.ObligationsType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EnforcePolicy {

	private PDP pdp;
	private Document responseAsDocument;
	private ResponseType responseAsObject;
	private LinkedList<ESensObligation> obligationList;
	

	public EnforcePolicy(final PDP simplePDP) throws EnforcePolicyException {
		if (simplePDP == null) {
			throw new EnforcePolicyException("PDP is null");
		}
		synchronized (this) {
			this.pdp = simplePDP;
		}

	}

	public void decide(Element request) throws EnforcePolicyException {

		if (request == null) {
			throw new EnforcePolicyException("No request have been passed");
		}
		try {
			RequestType myrequest = RequestMarshaller.unmarshal(request);
			ResponseType response = null;
			synchronized (this) {
				response = pdp.evaluate(myrequest);
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			ResponseMarshaller.marshal(response, doc);
			this.setResponseAsDocument(doc);
			this.setResponseAsObject(response);

			// now check the obligations. We assume in this implementation
			// a single evaluation per request
			List<ResultType> results = response.getResults();
			if (results == null || results.size() != 1) {
				throw new EnforcePolicyException("Wrong results size");
			}

			ResultType result = results.get(0);
			ObligationsType obligationsType = result.getObligations();
			if (obligationsType != null) {
				parseObligations(obligationsType);
			}
		} catch (Exception e) {
			throw new EnforcePolicyException("Unable to evaluate: "
					+ e.getMessage(), e);
		}

	}

	private void parseObligations(ObligationsType obligationsType)
			throws EnforcePolicyException {

		LinkedList<ESensObligation> obligationList = new LinkedList<ESensObligation>();
		List<ObligationType> oblType = obligationsType.getObligations();

		for (int i = 0; i < oblType.size(); i++) {
			ObligationType obl = oblType.get(i);

			ESensObligation eSensObligation = null;
			if (obl.getFulfillOn().compareTo(EffectType.PERMIT) == 0) {
				eSensObligation = new PERMITEsensObligation();
			} else if (obl.getFulfillOn().compareTo(EffectType.DENY) == 0) {
				eSensObligation = new DENYEsensObligation();
			} else {
				throw new EnforcePolicyException("Unkonwn effect type: "
						+ obl.getFulfillOn().name());
			}

			String oblId = obl.getObligationId();

			List<AttributeAssignmentType> attrAssignments = obl
					.getAttributeAssignments();
			for (int j = 0; j < attrAssignments.size(); j++) {
				AttributeAssignmentType assignment = attrAssignments.get(j);
				assignment.getAttributeId();
				assignment.getDataType().getDatatypeURI();
				assignment.getContent();
			}

			
			eSensObligation.setAttributeAssignments(attrAssignments);
			eSensObligation.setObligationID(oblId);
			
			obligationList.add(eSensObligation);
		}
		setObligationList(obligationList);
	}

	private void setObligationList(
			LinkedList<ESensObligation> obligationList) {
		this.obligationList = obligationList;
		
	}
	public List<ESensObligation> getObligationList() {
		return this.obligationList;
	}
	public Document getResponseAsDocument() {
		return responseAsDocument;
	}

	public void setResponseAsDocument(Document responseAsDocument) {
		this.responseAsDocument = responseAsDocument;
	}

	public ResponseType getResponseAsObject() {
		return responseAsObject;
	}

	public void setResponseAsObject(ResponseType responseAsObject) {
		this.responseAsObject = responseAsObject;
	}

}
