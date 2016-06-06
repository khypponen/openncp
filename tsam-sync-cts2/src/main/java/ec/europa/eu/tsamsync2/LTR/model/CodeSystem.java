/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.LTR.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author bernamp
 */
@Entity
@Table(name = "code_system")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CodeSystem.findAll", query = "SELECT c FROM CodeSystem c")})
public class CodeSystem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "OID")
    private String oid;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(mappedBy = "parentCodeSystemId")
    private Collection<ValueSet> valueSetCollection;
    @OneToMany(mappedBy = "codeSystemId")
    private Collection<CodeSystemVersion> codeSystemVersionCollection;

    public CodeSystem() {
    }

    public CodeSystem(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @XmlTransient
    public Collection<ValueSet> getValueSetCollection() {
        return valueSetCollection;
    }

    public void setValueSetCollection(Collection<ValueSet> valueSetCollection) {
        this.valueSetCollection = valueSetCollection;
    }

    @XmlTransient
    public Collection<CodeSystemVersion> getCodeSystemVersionCollection() {
        return codeSystemVersionCollection;
    }

    public void setCodeSystemVersionCollection(Collection<CodeSystemVersion> codeSystemVersionCollection) {
        this.codeSystemVersionCollection = codeSystemVersionCollection;
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
        if (!(object instanceof CodeSystem)) {
            return false;
        }
        CodeSystem other = (CodeSystem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.europa.eu.tsamsync2.LTR.CodeSystem[ id=" + id + " ]";
    }
    
}
