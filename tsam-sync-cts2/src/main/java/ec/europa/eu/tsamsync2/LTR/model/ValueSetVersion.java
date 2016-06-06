/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.LTR.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "value_set_version")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ValueSetVersion.findAll", query = "SELECT v FROM ValueSetVersion v")})
public class ValueSetVersion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "VERSION_NAME")
    private String versionName;
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
    @ManyToMany(mappedBy = "valueSetVersionCollection")
    private Collection<CodeSystemConcept> codeSystemConceptCollection;
    @JoinColumn(name = "VALUE_SET_ID", referencedColumnName = "ID")
    @ManyToOne
    private ValueSet valueSetId;
    @JoinColumn(name = "PREVIOUS_VERSION_ID", referencedColumnName = "ID")
    @ManyToOne
    private ValueSet previousVersionId;

    public ValueSetVersion() {
    }

    public ValueSetVersion(Long id) {
        this.id = id;
    }

    public ValueSetVersion(Long id, Date effectiveDate, Date releaseDate, Date statusDate) {
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

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
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

    @XmlTransient
    public Collection<CodeSystemConcept> getCodeSystemConceptCollection() {
        return codeSystemConceptCollection;
    }

    public void setCodeSystemConceptCollection(Collection<CodeSystemConcept> codeSystemConceptCollection) {
        this.codeSystemConceptCollection = codeSystemConceptCollection;
    }

    public ValueSet getValueSetId() {
        return valueSetId;
    }

    public void setValueSetId(ValueSet valueSetId) {
        this.valueSetId = valueSetId;
    }

    public ValueSet getPreviousVersionId() {
        return previousVersionId;
    }

    public void setPreviousVersionId(ValueSet previousVersionId) {
        this.previousVersionId = previousVersionId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.versionName);
        hash = 11 * hash + Objects.hashCode(this.description);
        hash = 11 * hash + Objects.hashCode(this.valueSetId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ValueSetVersion other = (ValueSetVersion) obj;
        if (!Objects.equals(this.versionName, other.versionName))
            return false;
        if (!Objects.equals(this.description, other.description))
            return false;
        if (!Objects.equals(this.valueSetId, other.valueSetId))
            return false;
        return true;
    }



    @Override
    public String toString() {
        return "ec.europa.eu.tsamsync2.LTR.ValueSetVersion[ id=" + id + " ]";
    }
    
}
