/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.LTR.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bernamp
 */
@Entity
@Table(name = "transcoding_association")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TranscodingAssociation.findAll", query = "SELECT t FROM TranscodingAssociation t")})
public class TranscodingAssociation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "TRANSCODING_ASSOCIATION_ID")
    private long transcodingAssociationId;
    @Column(name = "QUALITY")
    private String quality;
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @Column(name = "STATUS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date statusDate;
    @JoinColumn(name = "TARGET_CONCEPT_ID", referencedColumnName = "ID")
    @ManyToOne
    private CodeSystemConcept targetConceptId;
    @JoinColumn(name = "SOURCE_CONCEPT_ID", referencedColumnName = "ID")
    @ManyToOne
    private CodeSystemConcept sourceConceptId;

    public TranscodingAssociation() {
    }

    public TranscodingAssociation(Long id) {
        this.id = id;
    }

    public TranscodingAssociation(Long id, long transcodingAssociationId, Date statusDate) {
        this.id = id;
        this.transcodingAssociationId = transcodingAssociationId;
        this.statusDate = statusDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTranscodingAssociationId() {
        return transcodingAssociationId;
    }

    public void setTranscodingAssociationId(long transcodingAssociationId) {
        this.transcodingAssociationId = transcodingAssociationId;
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

    public CodeSystemConcept getTargetConceptId() {
        return targetConceptId;
    }

    public void setTargetConceptId(CodeSystemConcept targetConceptId) {
        this.targetConceptId = targetConceptId;
    }

    public CodeSystemConcept getSourceConceptId() {
        return sourceConceptId;
    }

    public void setSourceConceptId(CodeSystemConcept sourceConceptId) {
        this.sourceConceptId = sourceConceptId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TranscodingAssociation)) {
            return false;
        }
        TranscodingAssociation other = (TranscodingAssociation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.europa.eu.tsamsync2.LTR.TranscodingAssociation[ id=" + id + " ]";
    }
    
}
