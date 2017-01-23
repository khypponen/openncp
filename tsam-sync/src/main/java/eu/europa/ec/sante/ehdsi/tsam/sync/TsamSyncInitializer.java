package eu.europa.ec.sante.ehdsi.tsam.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TsamSyncInitializer {

    private final Logger logger = LoggerFactory.getLogger(TsamSyncInitializer.class);

    @Value("${application.version}")
    private String version;

    private final TsamSyncManager tsamSyncManager;

    @Autowired
    public TsamSyncInitializer(TsamSyncManager tsamSyncManager) {
        this.tsamSyncManager = tsamSyncManager;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TsamSyncInitializer.class, args);

        TsamSyncInitializer tsamSyncInitializer = applicationContext.getBean(TsamSyncInitializer.class);
        tsamSyncInitializer.run();
    }

    private void run() {
        logger.info("APP: {}", version);
        logger.info("OS: {} ({}, {})", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
        logger.info("JRE: {} ({})", System.getProperty("java.version"), System.getProperty("java.vendor"));
        logger.info("JVM: {} ({})", System.getProperty("java.vm.version"), System.getProperty("java.vm.name"));

        tsamSyncManager.execute();
    }
}
