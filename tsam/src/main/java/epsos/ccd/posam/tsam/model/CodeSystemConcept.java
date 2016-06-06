package epsos.ccd.posam.tsam.model;

import java.util.Date;
import java.util.List;

/**
 * A Code System Concept defines a unitary mental representation of a real or
 * abstract thing within the context of a specific Code System; an atomic unit
 * of thought.
 * 
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August
 */
public class CodeSystemConcept {
	
	public final static String AT_ID="id";
	public final static String AT_CODE="code";
	public final static String AT_VS_VERSIONS="valueSetVersions";
	public final static String AT_CS_VERSION="codeSystemVersion";
	public final static String AT_DESIGNATION="designations";

	/**
	 * An identifier that uniquely identified the Concept within Code System
	 */
	protected long id;

	/**
	 * A concept code that according to terminology best practices is unique
	 * within the context of the Code System, although some code systems do
	 * allow reuse of codes over time
	 */
	protected String code;

	/**
	 * A definition of concept's meaning described in text
	 */
	protected String definition;

	/**
	 * A status to identify the state of the Code System Concept<br>
	 * TODO (enumeration or catalog). List of statuses:<br>
	 * <li>Current</li> <li>Non-Current</li> <li>Duplicated</li> <li>Outdated</li>
	 * <li>Erroneous</li> <li>Limited</li> <li>Inappropriate</li> <li>Concept
	 * non-current</li> <li>Moved elsewhere</li> <li>Pending move</li>
	 */
	protected String status;

	/**
	 * A status date to identify the date the status was set to its current
	 * value
	 */
	protected Date statusDate;

	
	/**
	 * ValueSetVersion binding
	 */
	protected List<ValueSetVersion> valueSetVersions;
	
	/**
	 * Code system version in which concept was introduced as first
	 */
	protected CodeSystemVersion codeSystemVersion;
	private List<Designation> designations;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public List<ValueSetVersion> getValueSetVersions() {
		return valueSetVersions;
	}

	public void setValueSetVersions(List<ValueSetVersion> valueSetVersions) {
		this.valueSetVersions = valueSetVersions;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCodeSystemVersion(CodeSystemVersion codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}

	public CodeSystemVersion getCodeSystemVersion() {
		return codeSystemVersion;
	}

	public void setDesignations(List<Designation> designations) {
		this.designations = designations;
	}

	public List<Designation> getDesignations() {
		return designations;
	}
}
