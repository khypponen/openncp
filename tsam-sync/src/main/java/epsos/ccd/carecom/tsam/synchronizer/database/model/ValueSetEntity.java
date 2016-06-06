package epsos.ccd.carecom.tsam.synchronizer.database.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Implements an annotated value set entity.
 */
@Entity
@Table(name="VALUE_SET")
public class ValueSetEntity implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="OID")
	private String oid;
	
	@Column(name="EPSOS_NAME", nullable=false)
	private String epsosName;
	
	@ManyToOne
	@JoinColumn(name="PARENT_CODE_SYSTEM_ID", nullable=false)
	private CodeSystemEntity parentCodeSystem;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	public ValueSetEntity() {
		// Empty default constructor.
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOid() {
		return this.oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getEpsosName() {
		return this.epsosName;
	}

	public void setEpsosName(String epsosName) {
		this.epsosName = epsosName;
	}

	public CodeSystemEntity getParentCodeSystem() {
		return this.parentCodeSystem;
	}

	public void setParentCodeSystem(CodeSystemEntity parentCodeSystem) {
		this.parentCodeSystem = parentCodeSystem;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.description == null) ? 0 : this.description.hashCode());
		result = prime * result
				+ ((this.epsosName == null) ? 0 : this.epsosName.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.oid == null) ? 0 : this.oid.hashCode());
		result = prime
				* result
				+ ((this.parentCodeSystem == null) ? 0 : this.parentCodeSystem.hashCode());
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
		ValueSetEntity other = (ValueSetEntity) obj;
		if (this.description == null) {
			if (other.description != null)
				return false;
		} else if (!this.description.equals(other.description))
			return false;
		if (this.epsosName == null) {
			if (other.epsosName != null)
				return false;
		} else if (!this.epsosName.equals(other.epsosName))
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.oid == null) {
			if (other.oid != null)
				return false;
		} else if (!this.oid.equals(other.oid))
			return false;
		if (this.parentCodeSystem == null) {
			if (other.parentCodeSystem != null)
				return false;
		} else if (!this.parentCodeSystem.equals(other.parentCodeSystem))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ValueSetEntity [id=" + this.id + ", oid=" + this.oid + ", epsosName="
				+ this.epsosName + ", description=" + this.description + "]";
	}
}
