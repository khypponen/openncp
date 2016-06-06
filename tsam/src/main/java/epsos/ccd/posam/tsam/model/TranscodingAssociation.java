package epsos.ccd.posam.tsam.model;

import java.util.Date;

/**
 * This explicitly defines the mapping (transcoding) between the coded concepts
 * used within the epSOS reference Value Set and the local one for a specific
 * scope. This information is used for pivot document transcoding. This table
 * should be used to represent transcodings from local code systems to epSOS
 * code systems having N:1 relationship only.
 * 
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August
 */
public class TranscodingAssociation {
	
	public static final String AT_SOURCE_CONCEPT="sourceConcept";
	public static final String AT_TARGET_CONCEPT="targedConcept";
	

	/**
	 * An identifier than uniquely identifies the association
	 */
	private long id;

	/**
	 * A reference to a target concept, usually from epSOS code system
	 */
	private CodeSystemConcept targedConcept;

	/**
	 * A reference to a source concept, usually from local code system
	 */
	private CodeSystemConcept sourceConcept;

	/**
	 * A quality attribute that describes if the semantic equivalence is given
	 * or if there are granularity differences still to be resolved (narrower,
	 * broader, equivalent attributes)
	 */
	private String quality;

	/**
	 * A status to identify the state of association<br>
	 * TODO (enumeration or catalog)<br>
	 * List of statuses:<br>
	 * <li>valid</li> <li>invalid</li>
	 */
	private String status;

	/**
	 * A status date to identify the date the status was set to its current
	 * value
	 */
	private Date statusDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CodeSystemConcept getTargedConcept() {
		return targedConcept;
	}

	public void setTargedConcept(CodeSystemConcept targedConcept) {
		this.targedConcept = targedConcept;
	}

	public CodeSystemConcept getSourceConcept() {
		return sourceConcept;
	}

	public void setSourceConcept(CodeSystemConcept sourceConcept) {
		this.sourceConcept = sourceConcept;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
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
}
