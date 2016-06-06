package epsos.ccd.posam.tsam.model;

/**
 * A code system is resource that makes assertions about a collection of
 * terminological entities. They are described by a collection of uniquely
 * identifiable concepts with associated representations, designations,
 * associations, and meanings. <br/> In epSOS there are code systems from MVC
 * (including WHO ICD-10, SNOMED CT, LOINC, ATC, EDQM, UCUM, ISCO, HL7, IHE, and
 * others), which are used in pivot documents and local code systems used for
 * transcoding in member states.
 * 
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August
 */
public class CodeSystem {

	/**
	 * An internal code system identifier
	 */
	private long id;

	/**
	 * An identifier that uniquely identified the Code System (in the form of an
	 * ISO OID)
	 */
	private String oid;

	/**
	 * A name given in MVC / MTC that is usually the same as official code
	 * system name
	 */
	private String name;

	/**
	 * A description that describes the Code System. This may include the code
	 * system uses and intent
	 */
	private String description;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
