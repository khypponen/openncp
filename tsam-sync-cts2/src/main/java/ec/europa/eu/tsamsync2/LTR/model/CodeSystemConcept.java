/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.LTR.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author bernamp
 */
@Entity
@Table(name = "code_system_concept")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CodeSystemConcept.findAll", query = "SELECT c FROM CodeSystemConcept c")})
public class CodeSystemConcept implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CODE")
    private String code;
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @Column(name = "STATUS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date statusDate;
    @Column(name = "DEFINITION")
    private String definition;
    @JoinTable(name = "x_concept_value_set", joinColumns = {
        @JoinColumn(name = "CODE_SYSTEM_CONCEPT_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "VALUE_SET_VERSION_ID", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<ValueSetVersion> valueSetVersionCollection;
    @OneToMany(mappedBy = "targetConceptId")
    private Collection<TranscodingAssociation> transcodingAssociationCollection;
    @OneToMany(mappedBy = "sourceConceptId")
    private Collection<TranscodingAssociation> transcodingAssociationCollection1;
    @OneToMany(mappedBy = "codeSystemConceptId")
    private Collection<Designation> designationCollection;
    @JoinColumn(name = "CODE_SYSTEM_VERSION_ID", referencedColumnName = "ID")
    @ManyToOne
    private CodeSystemVersion codeSystemVersionId;

    public CodeSystemConcept() {
    }

    public CodeSystemConcept(Long id) {
        this.id = id;
    }

    public CodeSystemConcept(Long id, Date statusDate) {
        this.id = id;
        this.statusDate = statusDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    @XmlTransient
    public Collection<ValueSetVersion> getValueSetVersionCollection() {
        return valueSetVersionCollection;
    }

    public void setValueSetVersionCollection(Collection<ValueSetVersion> valueSetVersionCollection) {
        this.valueSetVersionCollection = valueSetVersionCollection;
    }

    @XmlTransient
    public Collection<TranscodingAssociation> getTranscodingAssociationCollection() {
        return transcodingAssociationCollection;
    }

    public void setTranscodingAssociationCollection(Collection<TranscodingAssociation> transcodingAssociationCollection) {
        this.transcodingAssociationCollection = transcodingAssociationCollection;
    }

    @XmlTransient
    public Collection<TranscodingAssociation> getTranscodingAssociationCollection1() {
        return transcodingAssociationCollection1;
    }

    public void setTranscodingAssociationCollection1(Collection<TranscodingAssociation> transcodingAssociationCollection1) {
        this.transcodingAssociationCollection1 = transcodingAssociationCollection1;
    }

    @XmlTransient
    public Collection<Designation> getDesignationCollection() {
        return designationCollection;
    }

    public void setDesignationCollection(Collection<Designation> designationCollection) {
        this.designationCollection = designationCollection;
    }

    public CodeSystemVersion getCodeSystemVersionId() {
        return codeSystemVersionId;
    }

    public void setCodeSystemVersionId(CodeSystemVersion codeSystemVersionId) {
        this.codeSystemVersionId = codeSystemVersionId;
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
        if (!(object instanceof CodeSystemConcept)) {
            return false;
        }
        CodeSystemConcept other = (CodeSystemConcept) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.europa.eu.tsamsync2.LTR.CodeSystemConcept[ id=" + id + " ]";
    }
    
}
