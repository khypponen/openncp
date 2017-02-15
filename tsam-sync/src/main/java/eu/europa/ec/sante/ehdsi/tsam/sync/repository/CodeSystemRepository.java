package eu.europa.ec.sante.ehdsi.tsam.sync.repository;

import eu.europa.ec.sante.ehdsi.tsam.sync.domain.CodeSystem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeSystemRepository extends JpaRepository<CodeSystem, Long> {
}
