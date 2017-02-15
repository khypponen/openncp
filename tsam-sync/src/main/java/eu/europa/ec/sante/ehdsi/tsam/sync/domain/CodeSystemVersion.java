package eu.europa.ec.sante.ehdsi.tsam.sync.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "code_system_version")
@SuppressWarnings("unused")
public class CodeSystemVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "local_name")
    private String localName;

    @Column(name = "effective_date")
    private LocalDateTime effectiveDate;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    private String status;

    @Column(name = "status_date")
    private LocalDateTime statusDate;

    private String description;

    private String copyright;

    private String source;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "code_system_id")
    private CodeSystem codeSystem;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codeSystemVersion", orphanRemoval = true)
    private List<CodeSystemEntity> concepts = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
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

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public CodeSystem getCodeSystem() {
        return codeSystem;
    }

    public void setCodeSystem(CodeSystem codeSystem) {
        this.codeSystem = codeSystem;
    }

    public List<CodeSystemEntity> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<CodeSystemEntity> concepts) {
        this.concepts = concepts;
    }
}
