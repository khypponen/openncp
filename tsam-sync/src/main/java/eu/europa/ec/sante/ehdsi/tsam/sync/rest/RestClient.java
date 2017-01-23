package eu.europa.ec.sante.ehdsi.tsam.sync.rest;

import eu.europa.ec.sante.ehdsi.termservice.rest.model.Agreement;
import eu.europa.ec.sante.ehdsi.termservice.rest.model.concept.CodeSystemConcept;
import eu.europa.ec.sante.ehdsi.termservice.rest.model.valueset.ValueSetVersion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RestClient {

    Optional<Agreement> retrieveAgreement(LocalDateTime lastAgreementDate);

    List<ValueSetVersion> listValueSetVersions(String valueSetCatalogId);

    List<CodeSystemConcept> listCodeSystemConcepts(String valueSetCatalogId, String valueSetVersionId, int page);
}
