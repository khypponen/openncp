package eu.europa.ec.sante.ehdsi.tsam.sync.client;

import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.CodeSystemEntityModel;
import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.sync.ValueSetCatalogSyncModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SimpleTermServerClient implements TermServerClient {

    private final RestTemplate template;

    public SimpleTermServerClient() {
        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpContext httpContext = new BasicHttpContext();
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, new BasicCookieStore());

        template = new RestTemplate(new CustomHttpComponentsClientHttpRequestFactory(httpClient, httpContext));
    }

    @Override
    public void authenticate(String username, String password) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("username", username);
        request.add("password", password);

        URI location = this.template.postForLocation("http://localhost:7001/ehealth-termservice-server/login", request);

        if (StringUtils.containsIgnoreCase(location.toString(), "?error")) {
            throw new AuthenticationException(MessageFormat.format("Authentication failed for user ''{0}''", username));
        }
    }

    @Override
    public Optional<ValueSetCatalogSyncModel> retrieveValueSetCatalog(LocalDateTime currentAgreementDate) {
        ResponseEntity<ValueSetCatalogSyncModel> response = template.getForEntity("http://localhost:7001/ehealth-termservice-server/api/sync/valuesetcatalog?agreementDate={agreementDate}", ValueSetCatalogSyncModel.class, currentAgreementDate);
        return Optional.of(response.getBody());
    }

    @Override
    public List<CodeSystemEntityModel> retrieveConcepts(String valueSetId, String valueSetVersionId, int page, int maxToReturn) {
        CodeSystemEntityModel[] codeSystemConcepts =
                template.getForObject(
                        "http://localhost:7001/ehealth-termservice-server/api/sync/valueset/{valuesetid}/valuesetdefinition/{valuesetdefinition}" +
                                "/entries?page={page}&maxtoreturn={maxToReturn}", CodeSystemEntityModel[].class, valueSetId, valueSetVersionId, page, maxToReturn);
        return Arrays.asList(codeSystemConcepts);
    }
}
