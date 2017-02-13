package eu.europa.ec.sante.ehdsi.tsam.sync.db;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "value_set")
@SuppressWarnings("unused")
public class ValueSetEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 255)
    private String oid;

    @Column(name = "epsos_name")
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

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
}
