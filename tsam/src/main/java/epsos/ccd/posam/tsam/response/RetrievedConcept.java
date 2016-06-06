package epsos.ccd.posam.tsam.response;

import epsos.ccd.posam.tsam.model.CodeSystemConcept;

/**
 * Response structure of method getValueSetConcepts() representing concept and
 * its designation.
 * 
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August *
 */

public class RetrievedConcept extends CodeSystemConcept {
	private String designation;
	private String language;

	public RetrievedConcept(CodeSystemConcept concept) {
		this.id=concept.getId();
		this.code=concept.getCode();
		this.definition=concept.getDefinition();
		this.status=concept.getStatus();
		this.statusDate=concept.getStatusDate();
	}
	
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RetrievedConcept [");
		if (designation != null) {
			builder.append("designation=");
			builder.append(designation);
			builder.append(", ");
		}
		if (language != null) {
			builder.append("language=");
			builder.append(language);
			builder.append(", ");
		}
		if (code != null) {
			builder.append("code=");
			builder.append(code);
			builder.append(", ");
		}
		builder.append("id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
