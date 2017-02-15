package eu.europa.ec.sante.ehdsi.tsam.sync.repository;

import eu.europa.ec.sante.ehdsi.tsam.sync.domain.ValueSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValueSetRepository extends JpaRepository<ValueSet, Long> {

}
