package epsos.ccd.posam.tsam.model;

import java.util.Date;
import java.util.List;

/**
 * Each Designation is a representation of the Concept and is assigned a unique
 * designation identifier. <br/>In context of epSOS project designations are the
 * subject of translation to member state languages. Designations in different
 * languages are used to produce translation of pivot documents.
 * 
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August
 */

public class Designation {
	
	public static final String AT_DESIGNATION="designation";
	public static final String AT_CONCEPT="concept";
	public static final String AT_LANGUAGE="languageCode";

	/**
	 * A unique identifier for the designation
	 */
	private long id;

	/**
	 * A display name for the designation
	 */
	private String designation;

	/**
	 * A code of language in which the designation is expressed
	 */
	private String languageCode;

	/**
	 * A designation type that describes the nature or usage of the designation.<br>
	 * TODO (enumeration or catalog)<br>
	 * List of type values:<br>
	 * <li>unspecified</li> <li>preferred term</li> <li>synonym</li> <li>fully
	 * specified name</li> <li>code</li>
	 */
	private String type;

	/**
	 * An optional attribute that identifies whether an attribute has a type of
	 * usage preference
	 */
	private Boolean preffered;

	/**
	 * A status to identify the state of designation<br>
	 * List of statuses:<br>
	 * <li>current designation of concept currently in use</li> <li>retired
	 * past version of the designation not valid anymore</li>
	 */
	private String status;

	/**
	 * A status date to identify the date the status was set to its current
	 * value
	 */
	private Date statusDate;

	/**
	 * A code system concept to which designation is related
	 */
	private CodeSystemConcept concept; 
	
	public CodeSystemConcept getConcept() {
		return concept;
	}

	public void setConcept(CodeSystemConcept concept) {
		this.concept = concept;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean isPreffered() {
		return preffered;
	}

	public void setPreffered(Boolean preffered) {
		this.preffered = preffered;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(designation.length()+6);
		if (languageCode != null) {
			builder.append(languageCode);
			builder.append(": ");
		}
		if (designation != null) {
			builder.append(designation);
			builder.append(" ");
		}
		builder.append(status);
		return builder.toString();
	}

}
