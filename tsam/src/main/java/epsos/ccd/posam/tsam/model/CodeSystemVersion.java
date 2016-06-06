package epsos.ccd.posam.tsam.model;

import java.util.Date;
import java.util.List;

/**
 * Code systems are generally not static entities and change over time. A
 * CodeSystemVersion is a static snapshot of a CodeSystem at a given point of
 * time (and in force for a period until the subsequent version supersedes it),
 * and enables identification of the versions of the code system in which any
 * given concept can be found.
 */
public class CodeSystemVersion {

	public static final String AT_LNAME="localName";
	public static final String AT_CODESYSTEM="codeSystem";
	/**
	 * A version identifier that uniquely identifies each version of a Code
	 * System
	 */
	private long id;

	/**
	 * A name that is the official name of the code system as assigned by the
	 * terminology provider at the time of the version being issued
	 */
	private String fullName;

	/**
	 * A name that the Code system is normally referred to for the life of this
	 * version
	 */
	private String localName;

	/**
	 * Identification of the previous version of the code system, which enables
	 * tracking of sequencing of versions, and identification of missing
	 * versions on a server instance
	 */
	private CodeSystemVersion previousVersion;

	/**
	 * A start date when the version is deemed to be valid for use.
	 */
	private Date effectiveDate;

	/**
	 * A date when the version of the Code System became available within a
	 * particular domain
	 */
	private Date releaseDate;

	/**
	 * A status to identify the state of the Code System Version.<br>
	 * TODO (enumeration OR catalog) List of statuses:<br>
	 * <li>current version of code system currently in use</li> <li>retired
	 * past version of code system</li> <li>not in use  prepared version which
	 * is still not in use</li>
	 */
	private String status;

	/**
	 * A status date to identify the date the status was set to its current
	 * value
	 */
	private Date statusDate;

	/**
	 * A description that describes the Code System Version
	 */
	private String description;

	/**
	 * Copyright information (pertaining to the release (version) of the Code
	 * System
	 */
	private String copyright;

	/**
	 * An attribute that identifies the authority or source of the code system
	 * in this version. i.e. IHTSDO
	 */
	private String source;

	/**
	 * binding to CodeSystem.<br>
	 * Identification of a code system to which version is related
	 */
	private CodeSystem codeSystem;
	private List<CodeSystemConcept> concepts;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public CodeSystem getCodeSystem() {
		return codeSystem;
	}

	public void setCodeSystem(CodeSystem codeSystem) {
		this.codeSystem = codeSystem;
	}

	public void setConcepts(List<CodeSystemConcept> concepts) {
		this.concepts = concepts;
	}

	public List<CodeSystemConcept> getConcepts() {
		return concepts;
	}

	public void setPreviousVersion(CodeSystemVersion previousVersion) {
		this.previousVersion = previousVersion;
	}

	public CodeSystemVersion getPreviousVersion() {
		return previousVersion;
	}

}
