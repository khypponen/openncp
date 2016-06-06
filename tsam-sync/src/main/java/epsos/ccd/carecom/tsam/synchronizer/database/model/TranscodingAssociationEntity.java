package epsos.ccd.carecom.tsam.synchronizer.database.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Implements an annotated transaction association entity.
 */
@Entity
@Table(name="TRANSCODING_ASSOCIATION")
public class TranscodingAssociationEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="TRANSCODING_ASSOCIATION_ID", nullable=false)
	private Long transcodingAssociasionId;
	
	@ManyToOne
	@JoinColumn(name="SOURCE_CONCEPT_ID", nullable=false)
	private CodeSystemConceptEntity sourceConcept;
	
	@ManyToOne
	@JoinColumn(name="TARGET_CONCEPT_ID", nullable=false)
	private CodeSystemConceptEntity targetConcept;
	
	@Column(name="QUALITY")
	private String quality;
	
	@Column(name="STATUS", nullable=false)
	private String status;
	
	@Column(name="STATUS_DATE", nullable=false)
	private Date statusDate;
	
	public TranscodingAssociationEntity() {
		// Empty default constructor
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTranscodingAssociasionId() {
		return transcodingAssociasionId;
	}

	public void setTranscodingAssociasionId(Long transcodingAssociasionId) {
		this.transcodingAssociasionId = transcodingAssociasionId;
	}

	public CodeSystemConceptEntity getSourceConcept() {
		return this.sourceConcept;
	}

	public void setSourceConcept(CodeSystemConceptEntity sourceConcept) {
		this.sourceConcept = sourceConcept;
	}

	public CodeSystemConceptEntity getTargetConcept() {
		return this.targetConcept;
	}

	public void setTargetConcept(CodeSystemConceptEntity targetConcept) {
		this.targetConcept = targetConcept;
	}

	public String getQuality() {
		return this.quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
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

	@Override
	public String toString() {
		return "TranscodingAssociationEntity [id=" + this.id + ", quality="
				+ this.quality + ", status=" + this.status + ", statusDate=" + this.statusDate
				+ "]";
	}
}
