package epsos.ccd.posam.tsam.model;

import java.util.Date;
import java.util.List;

/**
 * A Value Set Version represents a point in time view of a Value Set. The Value
 * Set Version identifies the set of concepts that are available in the value
 * set for any specific version of the value set.
 * 
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August
 */
public class ValueSetVersion {
	
	public static final String AT_VALUE_SET = "valueSet";
	public static final String AT_CONCEPTS = "concepts";
	public static final String AT_NAME = "versionName";


	/**
	 * A version identifier that uniquely identifies each version of a value set
	 */
	private long id;

	/**
	 * A name that is assigned to the value set by the terminology provider at
	 * the time of the version being issued
	 */
	private String versionName;

	/**
	 * Identification of the previous version of the value set, which enables
	 * tracking of sequencing of versions, and identification of missing
	 * versions on a server instance
	 */
	private ValueSetVersion previousVersion;

	/**
	 * A start date when the version is deemed to be valid for use.
	 */
	private Date effectiveDate;

	/**
	 * A date when the version became available within a particular domain
	 */
	private Date releaseDate;


	/**
	 * A status to identify the state of the Value Set Version<br>
	 * TODO (enumeration or catalog) List of statuses:<br>
	 * <li>current version of value set currently in use</li> <li>retired 
	 * past version of value set</li> <li>not in use prepared version which is
	 * still not in use</li>
	 */
	private String status;

	/**
	 * A status date to identify the date the status was set to its current
	 * value
	 */
	private Date statusDate;

	/**
	 * A description that describes the Value Set Version
	 */
	private String description;

	/**
	 * ValueSet binding<br>
	 * Identification of a code system to which version is related
	 */
	private ValueSet valueSet;

	/**
	 * CodeSystemConcept binding
	 * TODO check binding
	 */
	private List<CodeSystemConcept> concepts;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
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

	public ValueSet getValueSet() {
		return valueSet;
	}

	public void setValueSet(ValueSet valueSet) {
		this.valueSet = valueSet;
	}

	public List<CodeSystemConcept> getConcepts() {
		return concepts;
	}

	public void setConcepts(List<CodeSystemConcept> concepts) {
		this.concepts = concepts;
	}

	public void setPreviousVersion(ValueSetVersion previousVersion) {
		this.previousVersion = previousVersion;
	}

	public ValueSetVersion getPreviousVersion() {
		return previousVersion;
	}

}
