package eu.europa.ec.sante.ehdsi.tsam.sync.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "code_system_concept")
@SuppressWarnings("unused")
public class CodeSystemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    private String definition;

    private String status;

    @Column(name = "status_date")
    private LocalDateTime statusDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "code_system_version_id", nullable = false)
    private CodeSystemVersion codeSystemVersion;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "concepts")
    private List<ValueSetVersion> valueSetVersions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "concept")
    private List<Designation> designations = new ArrayList<>();

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

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(LocalDateTime statusDate) {
        this.statusDate = statusDate;
    }

    public CodeSystemVersion getCodeSystemVersion() {
        return codeSystemVersion;
    }

    public void setCodeSystemVersion(CodeSystemVersion codeSystemVersion) {
        this.codeSystemVersion = codeSystemVersion;
    }

    public List<ValueSetVersion> getValueSetVersions() {
        return valueSetVersions;
    }

    public void addValueSetVersion(ValueSetVersion valueSetVersion) {
        valueSetVersions.add(valueSetVersion);
        valueSetVersion.getConcepts().add(this);
    }

    public List<Designation> getDesignations() {
        return designations;
    }

    public void addDesignation(Designation designation) {
        designations.add(designation);
        designation.setConcept(this);
    }
}
