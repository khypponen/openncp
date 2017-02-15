package eu.europa.ec.sante.ehdsi.tsam.sync;

import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.ValueSetVersionModel;
import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.sync.ValueSetCatalogSyncModel;
import eu.europa.ec.sante.ehdsi.tsam.sync.client.TermServerClient;
import eu.europa.ec.sante.ehdsi.tsam.sync.converter.CodeSystemEntityConverter;
import eu.europa.ec.sante.ehdsi.tsam.sync.converter.ValueSetVersionConverter;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.DatabaseBackupTool;
import eu.europa.ec.sante.ehdsi.tsam.sync.domain.CodeSystemEntity;
import eu.europa.ec.sante.ehdsi.tsam.sync.domain.ValueSetVersion;
import eu.europa.ec.sante.ehdsi.tsam.sync.repository.CodeSystemEntityRepository;
import eu.europa.ec.sante.ehdsi.tsam.sync.repository.CodeSystemRepository;
import eu.europa.ec.sante.ehdsi.tsam.sync.repository.ValueSetRepository;
import eu.europa.ec.sante.ehdsi.tsam.sync.repository.ValueSetVersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TsamSyncManager {

    private final Logger logger = LoggerFactory.getLogger(TsamSyncManager.class);
    private final ValueSetVersionConverter valueSetVersionConverter = new ValueSetVersionConverter();
    private final CodeSystemEntityConverter codeSystemEntityConverter = new CodeSystemEntityConverter();
    @Autowired
    private TermServerClient termServerClient;
    @Autowired
    private DatabaseBackupTool databaseBackupTool;
    @Autowired
    private CodeSystemRepository codeSystemRepository;
    @Autowired
    private CodeSystemEntityRepository codeSystemEntityRepository;
    @Autowired
    private ValueSetRepository valueSetRepository;
    @Autowired
    private ValueSetVersionRepository valueSetVersionRepository;

    @Transactional
    public void synchronize() {
        StopWatch stopWatch = new StopWatch();
        logger.info("Start synchronization process");

        stopWatch.start("Authentication");
        termServerClient.authenticate();
        stopWatch.stop();
        logger.trace("Task: {} / Elapsed time: {} ms", stopWatch.getLastTaskName(), stopWatch.getLastTaskTimeMillis());
        logger.info("System authenticated to central terminology services");

        stopWatch.start("Checking for updates");
        logger.info("Checking for value set catalog/agreement updates...");
        Optional<ValueSetCatalogSyncModel> updatedValueSetCatalog = termServerClient.retrieveValueSetCatalog(null);
        stopWatch.stop();
        logger.trace("Task: {} / Elapsed time: {} ms", stopWatch.getLastTaskName(), stopWatch.getLastTaskTimeMillis());

        if (!updatedValueSetCatalog.isPresent()) {
            logger.info("Nothing to synchronize");
        } else {
            ValueSetCatalogSyncModel valueSetCatalog = updatedValueSetCatalog.get();
            logger.info("A new value set catalog/agreement has been found [name={},agreementDate={}]", valueSetCatalog.getName(), valueSetCatalog.getAgreementDate());

            logger.info("Backuping database");
            stopWatch.start("Backup database");
            boolean success = databaseBackupTool.backupDatabase();
            stopWatch.stop();
            logger.trace("Task: {} / Elapsed time: {} ms", stopWatch.getLastTaskName(), stopWatch.getLastTaskTimeMillis());
            if (!success) {
                throw new TsamSyncManagerException("Database backup failure");
            }

            logger.info("Clear database content");
            stopWatch.start("Backup database");
            valueSetRepository.deleteAll();
            codeSystemRepository.deleteAll();
            stopWatch.stop();
            logger.trace("Task: {} / Elapsed time: {} ms", stopWatch.getLastTaskName(), stopWatch.getLastTaskTimeMillis());

            int valueSetVersionsCount = valueSetCatalog.getValueSetVersions().size();
            int index = 0;

            for (ValueSetVersionModel valueSetVersionDto : valueSetCatalog.getValueSetVersions()) {
                stopWatch.start("Process value set version");

                ValueSetVersion valueSetVersion = valueSetVersionConverter.convert(valueSetVersionDto);
                valueSetVersionRepository.save(valueSetVersion);

                boolean hasNext = true;
                int page = 0;
                int total = 0;

                while (hasNext) {
                    List<CodeSystemEntity> concepts =
                            termServerClient.retrieveConcepts(valueSetVersionDto.getValueSet().getId(), valueSetVersionDto.getId(), page, 250)
                                    .stream()
                                    .map(codeSystemEntityConverter::convert)
                                    .collect(Collectors.toList());
                    concepts.forEach(concept -> concept.addValueSetVersion(valueSetVersion));

                    codeSystemEntityRepository.save(concepts);

                    total += concepts.size();
                    if (concepts.size() < 500) {
                        hasNext = false;
                    } else {
                        page++;
                    }
                }

                stopWatch.stop();
                logger.trace("Task: {} / Elapsed time: {} ms", stopWatch.getLastTaskName(), stopWatch.getLastTaskTimeMillis());

                logger.info("[{}/{}] Value set version '{}' processed ({} concepts)", ++index, valueSetVersionsCount, valueSetVersionDto.getName(), total);
            }

            logger.info("Synchronization done in {} s", stopWatch.getTotalTimeSeconds());
        }
    }
}
