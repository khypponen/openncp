/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.LTR.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
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
@Table(name = "code_system_version")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CodeSystemVersion.findAll", query = "SELECT c FROM CodeSystemVersion c")})
public class CodeSystemVersion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "LOCAL_NAME")
    private String localName;
    @Column(name = "PREVIOUS_VERSION_ID")
    private BigInteger previousVersionId;
    @Basic(optional = false)
    @Column(name = "EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;
    @Basic(optional = false)
    @Column(name = "RELEASE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @Column(name = "STATUS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date statusDate;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "COPYRIGHT")
    private String copyright;
    @Column(name = "SOURCE")
    private String source;
    @JoinColumn(name = "CODE_SYSTEM_ID", referencedColumnName = "ID")
    @ManyToOne
    private CodeSystem codeSystemId;
    @OneToMany(mappedBy = "codeSystemVersionId")
    private Collection<CodeSystemConcept> codeSystemConceptCollection;

    public CodeSystemVersion() {
    }

    public CodeSystemVersion(Long id) {
        this.id = id;
    }

    public CodeSystemVersion(Long id, Date effectiveDate, Date releaseDate, Date statusDate) {
        this.id = id;
        this.effectiveDate = effectiveDate;
        this.releaseDate = releaseDate;
        this.statusDate = statusDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public BigInteger getPreviousVersionId() {
        return previousVersionId;
    }

    public void setPreviousVersionId(BigInteger previousVersionId) {
        this.previousVersionId = previousVersionId;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public CodeSystem getCodeSystemId() {
        return codeSystemId;
    }

    public void setCodeSystemId(CodeSystem codeSystemId) {
        this.codeSystemId = codeSystemId;
    }

    @XmlTransient
    public Collection<CodeSystemConcept> getCodeSystemConceptCollection() {
        return codeSystemConceptCollection;
    }

    public void setCodeSystemConceptCollection(Collection<CodeSystemConcept> codeSystemConceptCollection) {
        this.codeSystemConceptCollection = codeSystemConceptCollection;
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
        if (!(object instanceof CodeSystemVersion)) {
            return false;
        }
        CodeSystemVersion other = (CodeSystemVersion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.europa.eu.tsamsync2.LTR.CodeSystemVersion[ id=" + id + " ]";
    }
    
}
