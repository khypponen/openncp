package eu.europa.ec.sante.ehdsi.tsam.sync.client;

import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.CodeSystemEntityModel;
import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.sync.ValueSetCatalogSyncModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TermServerClient {

    void authenticate(String username, String password) throws AuthenticationException;

    Optional<ValueSetCatalogSyncModel> retrieveValueSetCatalog(LocalDateTime currentAgreementDate);

    List<CodeSystemEntityModel> retrieveConcepts(String valueSetId, String valueSetVersionId, int page, int maxToReturn);
}
