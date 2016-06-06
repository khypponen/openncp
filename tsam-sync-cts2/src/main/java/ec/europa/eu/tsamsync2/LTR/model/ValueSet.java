/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.LTR.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author bernamp
 */
@Entity
@Table(name = "value_set")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ValueSet.findAll", query = "SELECT v FROM ValueSet v")})
public class ValueSet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "OID")
    private String oid;
    @Column(name = "EPSOS_NAME")
    private String epsosName;
    @Column(name = "DESCRIPTION")
    private String description;
    @JoinColumn(name = "PARENT_CODE_SYSTEM_ID", referencedColumnName = "ID")
    @ManyToOne
    private CodeSystem parentCodeSystemId;
    @OneToMany(mappedBy = "valueSetId", cascade = CascadeType.PERSIST)
    private Collection<ValueSetVersion> valueSetVersionCollection;
    @OneToMany(mappedBy = "previousVersionId")
    private Collection<ValueSetVersion> valueSetVersionCollection1;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.oid);
        hash = 71 * hash + Objects.hashCode(this.epsosName);
        hash = 71 * hash + Objects.hashCode(this.description);
        hash = 71 * hash + Objects.hashCode(this.parentCodeSystemId);
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
        final ValueSet other = (ValueSet) obj;
        if (!Objects.equals(this.oid, other.oid))
            return false;
        if (!Objects.equals(this.epsosName, other.epsosName))
            return false;
        if (!Objects.equals(this.description, other.description))
            return false;
        if (!Objects.equals(this.parentCodeSystemId, other.parentCodeSystemId))
            return false;
        return true;
    }


    public ValueSet() {
    }

    public ValueSet(Long id) {
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

    public String getEpsosName() {
        return epsosName;
    }

    public void setEpsosName(String epsosName) {
        this.epsosName = epsosName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CodeSystem getParentCodeSystemId() {
        return parentCodeSystemId;
    }

    public void setParentCodeSystemId(CodeSystem parentCodeSystemId) {
        this.parentCodeSystemId = parentCodeSystemId;
    }

    @XmlTransient
    public Collection<ValueSetVersion> getValueSetVersionCollection() {
        return valueSetVersionCollection;
    }

    public void setValueSetVersionCollection(Collection<ValueSetVersion> valueSetVersionCollection) {
        this.valueSetVersionCollection = valueSetVersionCollection;
    }

    @XmlTransient
    public Collection<ValueSetVersion> getValueSetVersionCollection1() {
        return valueSetVersionCollection1;
    }

    public void setValueSetVersionCollection1(Collection<ValueSetVersion> valueSetVersionCollection1) {
        this.valueSetVersionCollection1 = valueSetVersionCollection1;
    }

   

    @Override
    public String toString() {
        return "ec.europa.eu.tsamsync2.LTR.ValueSet[ id=" + id + " ]";
    }
    
}
