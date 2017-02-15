package eu.europa.ec.sante.ehdsi.tsam.sync.db.mysql;

import eu.europa.ec.sante.ehdsi.tsam.sync.db.DatabaseBackupTool;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.DatabaseToolException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Profile("mysql")
public class MySqlDatabaseBackupTool implements DatabaseBackupTool {

    @Value("${application.database.host}")
    private String host;

    @Value("${application.database.port}")
    private String port;

    @Value("${application.database.username}")
    private String username;

    @Value("${application.database.password}")
    private String password;

    @Value("${application.database.database-name}")
    private String database;

    @Override
    public boolean backupDatabase() {
        String output = database + "." + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + ".sql";

        StringBuilder command =
                new StringBuilder("mysqldump --user=" + username + " --password=" + password + " --result-file=" + output);

        if (StringUtils.isNotBlank(host)) {
            command
                    .append(" --host=")
                    .append(host);
        }
        if (StringUtils.isNotBlank(port)) {
            command
                    .append(" --port=")
                    .append(port);
        }
        command.append(" ").append(database);

        try {
            Process process = Runtime.getRuntime().exec(command.toString());
            int exitValue = process.waitFor();
            return exitValue == 0;
        } catch (IOException e) {
            throw new DatabaseToolException("Mysqldump command error", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DatabaseToolException("Database backup process has been interrupted", e);
        }
    }
}
