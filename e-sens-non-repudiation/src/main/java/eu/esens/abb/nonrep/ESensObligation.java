package eu.esens.abb.nonrep;

import java.util.List;

import org.herasaf.xacml.core.policy.impl.AttributeAssignmentType;

/**
 * The four methods will be removed if there is no need (e.g., we don't need to 
 * pack a single NR token)
 * @author max
 *
 */

public class ESensObligation {


	private List<AttributeAssignmentType> unknown;
	private String obligationID;


	public final List<AttributeAssignmentType> getAttributeAssignments() {
		return unknown;
	}
	
	public void setAttributeAssignments(List<AttributeAssignmentType> attrAssignments) {
		this.unknown = attrAssignments;
	}

	public String getObligationID() {
		return obligationID;
	}

	public void setObligationID(String obligationID) {
		this.obligationID = obligationID;
	}



}
