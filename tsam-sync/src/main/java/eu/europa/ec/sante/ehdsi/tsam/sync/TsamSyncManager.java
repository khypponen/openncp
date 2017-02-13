package eu.europa.ec.sante.ehdsi.tsam.sync;

import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.ValueSetVersionModel;
import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.sync.ValueSetCatalogSyncModel;
import eu.europa.ec.sante.ehdsi.tsam.sync.client.TermServerClient;
import eu.europa.ec.sante.ehdsi.tsam.sync.converter.CodeSystemConceptConverter;
import eu.europa.ec.sante.ehdsi.tsam.sync.converter.ValueSetVersionConverter;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.CodeSystemConceptEntity;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.HibernateDao;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.ValueSetVersionEntity;
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

    private static final String USERNAME = "system";

    private static final String PASSWORD = "123456";

    private final Logger logger = LoggerFactory.getLogger(TsamSyncManager.class);

    private final TermServerClient termServerClient;

    private final HibernateDao hibernateDao;

    @Autowired
    public TsamSyncManager(TermServerClient termServerClient, HibernateDao hibernateDao) {
        this.termServerClient = termServerClient;
        this.hibernateDao = hibernateDao;
    }

    @Transactional
    public void execute() {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        termServerClient.authenticate(USERNAME, PASSWORD);
        stopWatch.stop();
        logger.info("User '{}' authenticated successfully", USERNAME);

        stopWatch.start();
        logger.info("Checking for updates...");
        Optional<ValueSetCatalogSyncModel> updatedValueSetCatalog = termServerClient.retrieveValueSetCatalog(null);
        stopWatch.stop();

        if (!updatedValueSetCatalog.isPresent()) {
            logger.info("Nothing new to synchronize");
        } else {
            ValueSetCatalogSyncModel valueSetCatalog = updatedValueSetCatalog.get();
            int nbValueSetVersions = valueSetCatalog.getValueSetVersions().size();
            logger.info("Value set catalog '{}' retrieved from the server ({} value set versions to synchronize)", valueSetCatalog.getId(), nbValueSetVersions);

            ValueSetVersionConverter valueSetVersionConverter = new ValueSetVersionConverter();
            CodeSystemConceptConverter codeSystemConceptConverter = new CodeSystemConceptConverter();

            int index = 0;
            for (ValueSetVersionModel valueSetVersion : valueSetCatalog.getValueSetVersions()) {
                stopWatch.start();

                logger.info("[{}/{}] Start processing value set version '{}'...", ++index, nbValueSetVersions, valueSetVersion.getName());

                ValueSetVersionEntity valueSetVersionEntity = valueSetVersionConverter.convert(valueSetVersion);
                hibernateDao.save(valueSetVersionEntity);

                boolean hasNext = true;
                int page = 0;
                int total = 0;

                while (hasNext) {
                    List<CodeSystemConceptEntity> conceptEntities =
                            termServerClient.retrieveConcepts(valueSetVersion.getValueSet().getId(), valueSetVersion.getId(), page, 250)
                                    .stream()
                                    .map(codeSystemConceptConverter::convert)
                                    .collect(Collectors.toList());
                    conceptEntities.forEach(codeSystemConceptEntity -> codeSystemConceptEntity.addValueSetVersion(valueSetVersionEntity));

                    hibernateDao.saveAll(conceptEntities);

                    total += conceptEntities.size();
                    if (conceptEntities.size() < 500) {
                        hasNext = false;
                    } else {
                        page++;
                    }
                }

                stopWatch.stop();
                logger.info("[{}/{}] Value set version '{}' processed: {} concepts saved", index, nbValueSetVersions, valueSetVersion.getName(), total);
            }

            logger.info("Synchronization done in {} s", stopWatch.getTotalTimeSeconds());
        }
    }
}
