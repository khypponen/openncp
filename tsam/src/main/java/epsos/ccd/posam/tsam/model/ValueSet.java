package epsos.ccd.posam.tsam.model;

import java.util.List;

/**
 * A value set represents a uniquely identifiable set of valid concept
 * representations (codes), where any concept representation can be tested to
 * determine whether or not it is a member of the value set. <br>In epSOS project
 * reference value sets are specified in MVC. These value sets will be used in
 * pivot documents. Member states can potentially define their own value sets
 * used for transcoding to epSOS MVC value sets.
 * 
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August
 */
public class ValueSet {

	/**
	 * An internal value set identifier
	 */
	private long id;

	/**
	 * An identifier that uniquely identifies the value set (in the form of an
	 * ISO OID)
	 */
	private String oid;

	/**
	 * A name for the value set assigned in MVC
	 */
	private String epsosName;

	/**
	 * A description for the value set
	 */
	private String description;

	/**
	 * ValueSetVersion binding<br>
	 * TODO check
	 * 
	 */
	private List<ValueSetVersion> versions;

	/**
	 * CodeSystemVersion binding<br>
	 * TODO check
	 * 
	 */
	private CodeSystem codeSystem;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getEpsosName() {
		return epsosName;
	}

	public void setEpsosName(String espsosName) {
		this.epsosName = espsosName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setVersions(List<ValueSetVersion> versions) {
		this.versions = versions;
	}

	public List<ValueSetVersion> getVersions() {
		return versions;
	}

	public CodeSystem getCodeSystem() {
		return codeSystem;
	}

	public void setCodeSystem(CodeSystem codeSystem) {
		this.codeSystem = codeSystem;
	}

}
