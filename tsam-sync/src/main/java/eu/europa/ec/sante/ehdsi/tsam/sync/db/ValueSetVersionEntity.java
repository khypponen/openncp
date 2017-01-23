package eu.europa.ec.sante.ehdsi.tsam.sync.db;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "value_set_version")
@SuppressWarnings("unused")
public class ValueSetVersionEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Transient
    private String uid;

    @Column(name = "version_name")
    @Size(max = 255)
    private String name;

    @Column(name = "effective_date")
    private LocalDateTime effectiveDate;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    @Size(max = 255)
    private String status;

    @Column(name = "status_date")
    private LocalDateTime statusDate;

    @Size(max = 255)
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "value_set_id")
    private ValueSetEntity valueSet;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "x_concept_value_set",
            joinColumns = @JoinColumn(name = "value_set_version_id"),
            inverseJoinColumns = @JoinColumn(name = "code_system_concept_id"))
    private List<CodeSystemConceptEntity> concepts = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ValueSetEntity getValueSet() {
        return valueSet;
    }

    public void setValueSet(ValueSetEntity valueSet) {
        this.valueSet = valueSet;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CodeSystemConceptEntity> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<CodeSystemConceptEntity> concepts) {
        this.concepts = concepts;
    }
}
