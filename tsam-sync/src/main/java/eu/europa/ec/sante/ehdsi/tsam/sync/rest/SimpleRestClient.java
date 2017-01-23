package eu.europa.ec.sante.ehdsi.tsam.sync.rest;

import eu.europa.ec.sante.ehdsi.termservice.rest.model.Agreement;
import eu.europa.ec.sante.ehdsi.termservice.rest.model.concept.CodeSystemConcept;
import eu.europa.ec.sante.ehdsi.termservice.rest.model.valueset.ValueSetVersion;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SimpleRestClient implements RestClient {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public Optional<Agreement> retrieveAgreement(LocalDateTime lastAgreementDate) {
        return Optional.ofNullable(restTemplate.getForObject("http://localhost:7001/ehealth-termservice-server/services/rs/sync/agreement?authorityid=1", Agreement.class));
    }

    @Override
    public List<ValueSetVersion> listValueSetVersions(String valueSetCatalogId) {
        ValueSetVersion[] valueSetVersions =
                restTemplate.getForObject("http://localhost:7001/ehealth-termservice-server/services/rs/valuesetcatalog/{valuesetcatalogid}/definitions",
                        ValueSetVersion[].class, valueSetCatalogId);
        return Arrays.asList(valueSetVersions);
    }

    @Override
    public List<CodeSystemConcept> listCodeSystemConcepts(String valueSetCatalogId, String valueSetVersionId, int page) {
        CodeSystemConcept[] codeSystemConcepts =
                restTemplate.getForObject("http://localhost:7001/ehealth-termservice-server/services/rs/valuesetcatalog/{valuesetcatalogid}/definition/{valuesetdefinitionid}/entries?page={page}", CodeSystemConcept[].class, valueSetCatalogId, valueSetVersionId, page);
        return Arrays.asList(codeSystemConcepts);
    }
}
