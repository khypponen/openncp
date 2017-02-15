package eu.europa.ec.sante.ehdsi.tsam.sync.repository;

import eu.europa.ec.sante.ehdsi.tsam.sync.domain.CodeSystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeSystemEntityRepository extends JpaRepository<CodeSystemEntity, Long> {
}
