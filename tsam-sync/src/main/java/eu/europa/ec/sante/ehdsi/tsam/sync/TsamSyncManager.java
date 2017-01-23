package eu.europa.ec.sante.ehdsi.tsam.sync;

import eu.europa.ec.sante.ehdsi.termservice.rest.model.Agreement;
import eu.europa.ec.sante.ehdsi.tsam.sync.converter.CodeSystemConceptConverter;
import eu.europa.ec.sante.ehdsi.tsam.sync.converter.ValueSetVersionConverter;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.CodeSystemConceptEntity;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.HibernateDao;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.ValueSetVersionEntity;
import eu.europa.ec.sante.ehdsi.tsam.sync.rest.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TsamSyncManager {

    private final Logger logger = LoggerFactory.getLogger(TsamSyncManager.class);

    private final RestClient restClient;

    private final HibernateDao hibernateDao;

    @Autowired
    public TsamSyncManager(RestClient restClient, HibernateDao hibernateDao) {
        this.restClient = restClient;
        this.hibernateDao = hibernateDao;
    }

    @Transactional
    void execute() {
        logger.info("Checking for updates...");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Optional<Agreement> result = restClient.retrieveAgreement(null);

        stopWatch.stop();

        if (!result.isPresent()) {
            logger.info("Nothing to synchronize");
        } else {
            Agreement agreement = result.get();
            logger.info("A new value set catalog (name: {}, release date: {}, agreement date: {}) has been found in {} s",
                    agreement.getValueSetCatalog().getName(), agreement.getValueSetCatalog().getReleaseDate(), agreement.getStatusDate(),
                    stopWatch.getLastTaskInfo().getTimeSeconds());


            stopWatch.start();

            String valueSetCatalogId = agreement.getValueSetCatalog().getId();

            ValueSetVersionConverter valueSetVersionConverter = new ValueSetVersionConverter();
            List<ValueSetVersionEntity> valueSetVersions =
                    restClient.listValueSetVersions(valueSetCatalogId)
                            .stream()
                            .map(valueSetVersionConverter::convert)
                            .collect(Collectors.toList());
            hibernateDao.saveAll(valueSetVersions);

            stopWatch.stop();
            logger.info("{} value set version(s) retrieved and saved in {} s", valueSetVersions.size(), stopWatch.getLastTaskInfo().getTimeSeconds());

            int index = 0;
            CodeSystemConceptConverter codeSystemConceptConverter = new CodeSystemConceptConverter();

            for (ValueSetVersionEntity valueSetVersion : valueSetVersions) {
                stopWatch.start();

                String valueSetVersionId = valueSetVersion.getUid();
                logger.debug("Value set version: {}/{}", valueSetVersion.getId(), valueSetVersion.getName());

                boolean hasNext = true;
                int page = 0;
                int total = 0;

                while (hasNext) {
                    List<CodeSystemConceptEntity> codeSystemConcepts =
                            restClient.listCodeSystemConcepts(valueSetCatalogId, valueSetVersion.getUid(), page)
                                    .stream()
                                    .map(codeSystemConceptConverter::convert)
                                    .collect(Collectors.toList());
                    codeSystemConcepts.forEach(codeSystemConceptEntity -> codeSystemConceptEntity.addValueSetVersion(valueSetVersion));

                    hibernateDao.saveAll(codeSystemConcepts);

                    total += codeSystemConcepts.size();
                    if (codeSystemConcepts.size() < 500) {
                        hasNext = false;
                    } else {
                        page++;
                    }
                }

                stopWatch.stop();
                logger.info("[{}/{}] Value set version '{}' processed in {} s ({} concepts saved)", ++index, valueSetVersions.size(), valueSetVersionId, stopWatch.getLastTaskInfo().getTimeSeconds(), total);
            }
        }

        logger.info("{} s", stopWatch.getTotalTimeSeconds());
    }
}
