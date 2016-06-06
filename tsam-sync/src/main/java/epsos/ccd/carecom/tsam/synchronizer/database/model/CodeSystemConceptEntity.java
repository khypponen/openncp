package epsos.ccd.carecom.tsam.synchronizer.database.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Implements an annotated code system concept entity.
 */
@Entity
@Table(name="CODE_SYSTEM_CONCEPT")
public class CodeSystemConceptEntity implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="CODE", nullable=false)
	private String code;
	
	@Column(name="DEFINITION",length=1000)
	private String definition;
	
	@Column(name="STATUS", nullable=false)
	private String status;
	
	@Column(name="STATUS_DATE", nullable=false)
	private Date statusDate;
	
	@ManyToOne
	@JoinColumn(name="CODE_SYSTEM_VERSION_ID", nullable=false)
	private CodeSystemVersionEntity codeSystemVersion;
	
	@ManyToMany(targetEntity=ValueSetVersionEntity.class)
	@JoinTable(
			name="X_CONCEPT_VALUE_SET", 
			joinColumns=@JoinColumn(name="CODE_SYSTEM_CONCEPT_ID"), 
			inverseJoinColumns=@JoinColumn(name="VALUE_SET_VERSION_ID"))
	private Collection<ValueSetVersionEntity> valueSetVersions;
	
	/**
	 * Default empty constructor.
	 */
	public CodeSystemConceptEntity() {
		// Empty default constructor
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDefinition() {
		return this.definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStatusDate() {
		return this.statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public CodeSystemVersionEntity getCodeSystemVersion() {
		return this.codeSystemVersion;
	}

	public void setCodeSystemVersion(CodeSystemVersionEntity codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}
	
	public Collection<ValueSetVersionEntity> getValueSetVersions() {
		return this.valueSetVersions;
	}

	public void setValueSetVersions(Collection<ValueSetVersionEntity> valueSetVersions) {
		this.valueSetVersions = valueSetVersions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.code == null) ? 0 : this.code.hashCode());
		result = prime
				* result
				+ ((this.codeSystemVersion == null) ? 0 : this.codeSystemVersion
						.hashCode());
		result = prime * result
				+ ((this.definition == null) ? 0 : this.definition.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
		result = prime * result
				+ ((this.statusDate == null) ? 0 : this.statusDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodeSystemConceptEntity other = (CodeSystemConceptEntity) obj;
		if (this.code == null) {
			if (other.code != null)
				return false;
		} else if (!this.code.equals(other.code))
			return false;
		if (this.codeSystemVersion == null) {
			if (other.codeSystemVersion != null)
				return false;
		} else if (!this.codeSystemVersion.equals(other.codeSystemVersion))
			return false;
		if (this.definition == null) {
			if (other.definition != null)
				return false;
		} else if (!this.definition.equals(other.definition))
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.status == null) {
			if (other.status != null)
				return false;
		} else if (!this.status.equals(other.status))
			return false;
		if (this.statusDate == null) {
			if (other.statusDate != null)
				return false;
		} else if (!this.statusDate.equals(other.statusDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CodeSystemConceptEntity [id=" + this.id + ", code=" + this.code
				+ ", definition=" + this.definition + ", status=" + this.status
				+ ", statusDate=" + this.statusDate + "]";
	}
	
}
