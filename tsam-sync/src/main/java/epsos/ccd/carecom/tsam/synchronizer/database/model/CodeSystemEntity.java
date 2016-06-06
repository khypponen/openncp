package epsos.ccd.carecom.tsam.synchronizer.database.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Implements an annotated code system entity.
 */
@Entity
@Table(name="CODE_SYSTEM")
public class CodeSystemEntity implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="OID")
	private String oid;
	
	@Column(name="NAME",nullable=false)
	private String name;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	/**
	 * Default empty constructor.
	 */
	public CodeSystemEntity() {
		// Default empty constructor.
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


	public String getName() {
		return this.name;
	}


	public void setName(String name) {
		this.name = name;
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
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.oid == null) ? 0 : this.oid.hashCode());
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
		CodeSystemEntity other = (CodeSystemEntity) obj;
		if (this.description == null) {
			if (other.description != null)
				return false;
		} else if (!this.description.equals(other.description))
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.oid == null) {
			if (other.oid != null)
				return false;
		} else if (!this.oid.equals(other.oid))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "CodeSystemEntity [id=" + this.id + ", oid=" + this.oid + ", name=" + this.name
				+ ", description=" + this.description + "]";
	}
	
}
