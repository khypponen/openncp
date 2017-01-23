package eu.europa.ec.sante.ehdsi.tsam.sync.db;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "code_system_concept")
@SuppressWarnings("unused")
public class CodeSystemConceptEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String code;

    private String definition;

    private String status;

    @Column(name = "status_date")
    private LocalDateTime statusDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "code_system_version_id", nullable = false)
    private CodeSystemVersionEntity codeSystemVersion;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "concepts")
    private List<ValueSetVersionEntity> valueSetVersions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "codeSystemConcept")
    private List<DesignationEntity> designations = new ArrayList<>();

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

    public CodeSystemVersionEntity getCodeSystemVersion() {
        return codeSystemVersion;
    }

    public void setCodeSystemVersion(CodeSystemVersionEntity codeSystemVersion) {
        this.codeSystemVersion = codeSystemVersion;
    }

    public List<ValueSetVersionEntity> getValueSetVersions() {
        return valueSetVersions;
    }

    public void addValueSetVersion(ValueSetVersionEntity valueSetVersion) {
        valueSetVersions.add(valueSetVersion);
        valueSetVersion.getConcepts().add(this);
    }

    public List<DesignationEntity> getDesignations() {
        return designations;
    }

    public void addDesignation(DesignationEntity designation) {
        designations.add(designation);
        designation.setCodeSystemConcept(this);
    }
}
