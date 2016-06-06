package epsos.ccd.carecom.tsam.synchronizer.database.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Implements an annotated designation entity.
 */
@Entity
@Table(name="DESIGNATION")
public class DesignationEntity implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="DESIGNATION", nullable=false)
	private String designation;
	
	@Column(name="LANGUAGE_CODE", nullable=false)
	private String languageCode;
	
	@Column(name="TYPE")
	private String type;
	
	@Column(name="IS_PREFERRED")
	private Boolean isPreferred;
	
	@Column(name="STATUS", nullable=false)
	private String status;
	
	@Column(name="STATUS_DATE", nullable=false)
	private Date statusDate;
	
	@ManyToOne
	@JoinColumn(name="CODE_SYSTEM_CONCEPT_ID", nullable=false)
	private CodeSystemConceptEntity codeSystemConcept;
	
	public DesignationEntity() {
		// Empty default constructor.
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getLanguageCode() {
		return this.languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsPreferred() {
		return this.isPreferred;
	}

	public void setIsPreferred(Boolean isPreferred) {
		this.isPreferred = isPreferred;
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

	public CodeSystemConceptEntity getCodeSystemConcept() {
		return this.codeSystemConcept;
	}

	public void setCodeSystemConceptId(CodeSystemConceptEntity codeSystemConcept) {
		this.codeSystemConcept = codeSystemConcept;
	}

	@Override
	public String toString() {
		return "DesignationEntity [id=" + this.id + ", designation=" + this.designation
				+ ", languageCode=" + this.languageCode + ", type=" + this.type
				+ ", isPreferred=" + this.isPreferred + ", status=" + this.status
				+ ", statusDate=" + this.statusDate + "]";
	}
}
