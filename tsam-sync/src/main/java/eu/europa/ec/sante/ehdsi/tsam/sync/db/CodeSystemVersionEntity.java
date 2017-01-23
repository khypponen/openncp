package eu.europa.ec.sante.ehdsi.tsam.sync.db;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "code_system_version")
@SuppressWarnings("unused")
public class CodeSystemVersionEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Transient
    private String uid;

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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "code_system_id")
    private CodeSystemEntity codeSystem;

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

    public CodeSystemEntity getCodeSystem() {
        return codeSystem;
    }

    public void setCodeSystem(CodeSystemEntity codeSystem) {
        this.codeSystem = codeSystem;
    }
}
