package eu.europa.ec.sante.ehdsi.tsam.sync.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "code_system")
@SuppressWarnings("unused")
public class CodeSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String oid;

    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codeSystem", orphanRemoval = true)
    private List<CodeSystemVersion> versions = new ArrayList<>();

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

    public List<CodeSystemVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<CodeSystemVersion> versions) {
        this.versions = versions;
    }
}
