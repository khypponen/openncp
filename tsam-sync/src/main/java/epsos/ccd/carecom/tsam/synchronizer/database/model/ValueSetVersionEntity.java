package epsos.ccd.carecom.tsam.synchronizer.database.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Implements an annotated value set version entity.
 */
@Entity
@Table(name="VALUE_SET_VERSION")
public class ValueSetVersionEntity implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="VERSION_NAME", nullable=false)
	private String versionName;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PREVIOUS_VERSION_ID")
	private ValueSetVersionEntity previousVersion;
	
	@Column(name="EFFECTIVE_DATE")
	private Date effectiveDate;
	
	@Column(name="RELEASE_DATE", nullable=false)
	private Date releaseDate;
	
	@Column(name="STATUS", nullable=false)
	private String status;
	
	@Column(name="STATUS_DATE", nullable=false)
	private Date statusDate;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="VALUE_SET_ID",nullable=false)
	private ValueSetEntity valueSet;
	
	@ManyToMany(
			targetEntity=CodeSystemConceptEntity.class,
			mappedBy="valueSetVersions"
	)
	private Collection<CodeSystemConceptEntity> concepts;
	
	public ValueSetVersionEntity() {
		// Empty default constructor.
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersionName() {
		return this.versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public ValueSetVersionEntity getPreviousVersion() {
		return this.previousVersion;
	}

	public void setPreviousVersion(ValueSetVersionEntity previousVersion) {
		this.previousVersion = previousVersion;
	}

	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getReleaseDate() {
		return this.releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ValueSetEntity getValueSet() {
		return this.valueSet;
	}

	public void setValueSet(ValueSetEntity valueSet) {
		this.valueSet = valueSet;
	}
	

	public Collection<CodeSystemConceptEntity> getConcepts() {
		return this.concepts;
	}

	public void setConcepts(Collection<CodeSystemConceptEntity> concepts) {
		this.concepts = concepts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.description == null) ? 0 : this.description.hashCode());
		result = prime * result
				+ ((this.effectiveDate == null) ? 0 : this.effectiveDate.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result
				+ ((this.previousVersion == null) ? 0 : this.previousVersion.hashCode());
		result = prime * result
				+ ((this.releaseDate == null) ? 0 : this.releaseDate.hashCode());
		result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
		result = prime * result
				+ ((this.statusDate == null) ? 0 : this.statusDate.hashCode());
		result = prime * result
				+ ((this.valueSet == null) ? 0 : this.valueSet.hashCode());
		result = prime * result
				+ ((this.versionName == null) ? 0 : this.versionName.hashCode());
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
		ValueSetVersionEntity other = (ValueSetVersionEntity) obj;
		if (this.description == null) {
			if (other.description != null)
				return false;
		} else if (!this.description.equals(other.description))
			return false;
		if (this.effectiveDate == null) {
			if (other.effectiveDate != null)
				return false;
		} else if (!this.effectiveDate.equals(other.effectiveDate))
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.previousVersion == null) {
			if (other.previousVersion != null)
				return false;
		} else if (!this.previousVersion.equals(other.previousVersion))
			return false;
		if (this.releaseDate == null) {
			if (other.releaseDate != null)
				return false;
		} else if (!this.releaseDate.equals(other.releaseDate))
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
		if (this.valueSet == null) {
			if (other.valueSet != null)
				return false;
		} else if (!this.valueSet.equals(other.valueSet))
			return false;
		if (this.versionName == null) {
			if (other.versionName != null)
				return false;
		} else if (!this.versionName.equals(other.versionName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ValueSetVersionEntity [id=" + this.id + ", versionName="
				+ this.versionName + ", effectiveDate=" + this.effectiveDate
				+ ", releaseDate=" + this.releaseDate + ", status=" + this.status
				+ ", statusDate=" + this.statusDate + ", description=" + this.description
				+ "]";
	}
}
