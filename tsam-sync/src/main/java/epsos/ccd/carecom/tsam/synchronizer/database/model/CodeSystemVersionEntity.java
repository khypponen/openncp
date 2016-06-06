package epsos.ccd.carecom.tsam.synchronizer.database.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Implements an annotated code system version entity.
 */
@Entity
@Table(name="CODE_SYSTEM_VERSION")
public class CodeSystemVersionEntity implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="FULL_NAME")
	private String fullName;
	
	@Column(name="LOCAL_NAME", nullable=false)
	private String localName;
	
	@OneToOne
	@JoinColumn(name="PREVIOUS_VERSION_ID")
	private CodeSystemVersionEntity previousVersion;
	
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
	
	@Column(name="COPYRIGHT")
	private String copyright;
	
	@Column(name="SOURCE")
	private String source;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CODE_SYSTEM_ID", nullable=false)
	private CodeSystemEntity codeSystem;
	
	/**
	 * Default empty constructor.
	 */
	public CodeSystemVersionEntity() {
		// Default empty constructor.
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLocalName() {
		return this.localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public CodeSystemVersionEntity getPreviousVersionId() {
		return this.previousVersion;
	}

	public void setPreviousVersion(CodeSystemVersionEntity previousVersion) {
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

	public String getCopyright() {
		return this.copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public CodeSystemEntity getCodeSystem() {
		return this.codeSystem;
	}

	public void setCodeSystem(CodeSystemEntity codeSystem) {
		this.codeSystem = codeSystem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.codeSystem == null) ? 0 : this.codeSystem.hashCode());
		result = prime * result
				+ ((this.copyright == null) ? 0 : this.copyright.hashCode());
		result = prime * result
				+ ((this.description == null) ? 0 : this.description.hashCode());
		result = prime * result
				+ ((this.effectiveDate == null) ? 0 : this.effectiveDate.hashCode());
		result = prime * result
				+ ((this.fullName == null) ? 0 : this.fullName.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result
				+ ((this.localName == null) ? 0 : this.localName.hashCode());
		result = prime * result
				+ ((this.previousVersion == null) ? 0 : this.previousVersion.hashCode());
		result = prime * result
				+ ((this.releaseDate == null) ? 0 : this.releaseDate.hashCode());
		result = prime * result + ((this.source == null) ? 0 : this.source.hashCode());
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
		CodeSystemVersionEntity other = (CodeSystemVersionEntity) obj;
		if (this.codeSystem == null) {
			if (other.codeSystem != null)
				return false;
		} else if (!this.codeSystem.equals(other.codeSystem))
			return false;
		if (this.copyright == null) {
			if (other.copyright != null)
				return false;
		} else if (!this.copyright.equals(other.copyright))
			return false;
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
		if (this.fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!this.fullName.equals(other.fullName))
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.localName == null) {
			if (other.localName != null)
				return false;
		} else if (!this.localName.equals(other.localName))
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
		if (this.source == null) {
			if (other.source != null)
				return false;
		} else if (!this.source.equals(other.source))
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
		return "CodeSystemVersionEntity [id=" + this.id + ", fullName=" + this.fullName
				+ ", localName=" + this.localName + ", effectiveDate="
				+ this.effectiveDate + ", releaseDate=" + this.releaseDate + ", status="
				+ this.status + ", statusDate=" + this.statusDate + ", description="
				+ this.description + ", copyright=" + this.copyright + ", source="
				+ this.source + "]";
	}
}
