package eu.europa.ec.sante.ehdsi.tsam.sync.db.oracle;

import eu.europa.ec.sante.ehdsi.tsam.sync.db.DatabaseBackupTool;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("oracle")
public class OracleDatabaseBackupTool implements DatabaseBackupTool {

    @Override
    public boolean backupDatabase() {
        return true;
    }
}
