/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2013 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.validation.reporting;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.epsos.validation.datamodel.common.NcpSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.util.Constants;

import java.io.File;

/**
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class ValidationReport {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationReport.class);
    private static final String REPORT_FILES_FOLDER = "validation";
    private static final String FILE_NAME_DELIMITER = "_";
    private static final String CLEANUP_VALIDATION_DIR_AFTER_TEST = "automated.validation.cleanup.dir";

    public static String build(NcpSide ncpSide, boolean cleanupDir) {
        /* METHOD ATTRIBUTES */
        String path;
        File folder;
        File[] listOfFiles;
        StringBuilder sb;

        /* ATTRIBUTES INITIALIZATION */
        path = Constants.EPSOS_PROPS_PATH + REPORT_FILES_FOLDER + "/" + ncpSide.getName();
        folder = new File(path);
        listOfFiles = folder.listFiles();
        sb = new StringBuilder();


        /* PRE-CONDITIONS VERIFICATION */
        if (!folder.exists()) {
            LOG.error("The specified folder does not exists. (" + path + ")");
            return null;
        }
        if (!folder.isDirectory()) {
            LOG.error("The specified folder is not a folder. (" + path + ")");
            return null;
        }
        if (!folder.canRead()) {
            LOG.error("Cannot read from specified folder. (" + path + ")");
            return null;
        }
        if (listOfFiles.length == 0) {
            LOG.error("The specified folder is empty. (" + path + ")");
            return null;
        }

        /* BODY */
        sb.append("\u21b3Validation result");

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                sb.append("\n");
                if (i == listOfFiles.length - 1) {
                    sb.append(" \u2514\u2500" + processFile(listOfFiles[i]));
                } else {
                    sb.append(" \u251c\u2500" + processFile(listOfFiles[i]));
                }
            }
        }
        sb.append("\n");

        if (cleanupDir) {
            cleanValidationDir(folder);
        }

        return sb.toString();
    }

    public static void write(NcpSide ncpSide, boolean cleanupDir) {
        /* METHOD ATTRIBUTES */
        String path;
        File folder;
        File[] listOfFiles;

        /* ATTRIBUTES INITIALIZATION */
        path = Constants.EPSOS_PROPS_PATH + REPORT_FILES_FOLDER + "/" + ncpSide.getName();
        folder = new File(path);
        listOfFiles = folder.listFiles();

        /* PRE-CONDITIONS VERIFICATION */
        if (!folder.exists()) {
            LOG.error("The specified folder does not exists. (" + path + ")");
            return;
        }
        if (!folder.isDirectory()) {
            LOG.error("The specified folder is not a folder. (" + path + ")");
            return;
        }
        if (!folder.canRead()) {
            LOG.error("Cannot read from specified folder. (" + path + ")");
            return;
        }
        if (listOfFiles.length == 0) {
            LOG.error("The specified folder is empty. (" + path + ")");
            return;
        }

        /* BODY */
        LOG.info("\u21b3Validation result");

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                if (i == listOfFiles.length - 1) {
                    LOG.info(" \u2514\u2500" + processFile(listOfFiles[i]));
                } else {
                    LOG.info(" \u251c\u2500" + processFile(listOfFiles[i]));
                }
            }
        }

        if (cleanupDir) {
            cleanValidationDir(folder);
        }
    }

    public static void cleanValidationDir(File folder) {

        String cleanUpValidationDir = ConfigurationManagerService.getInstance().getProperty(CLEANUP_VALIDATION_DIR_AFTER_TEST);

        if (cleanUpValidationDir == null) {
            LOG.error("The value of Clean Up Validation Drr in properties database is null.");
            return;
        }
        if (cleanUpValidationDir.isEmpty()) {
            LOG.error("The value of Clean Up Validation Drr Property in properties database is empty.");
            return;
        }
        if (!Boolean.parseBoolean(cleanUpValidationDir)) {
            LOG.info("Not cleaning up the validation dir.");
            return;
        }

        boolean success = true;

        if (!folder.exists()) {
            LOG.error("The specified folder does not exists. (" + folder.getAbsolutePath() + ")");
            return;
        }
        if (!folder.isDirectory()) {
            LOG.error("The specified folder is not a folder. (" + folder.getAbsolutePath() + ")");
            return;
        }
        if (!folder.canRead()) {
            LOG.error("Cannot read from specified folder. (" + folder.getAbsolutePath() + ")");
            return;
        }

        if (folder.listFiles().length != 0) {
            for (File f : folder.listFiles()) {
                if (!f.delete()) {
                    LOG.info("Could not remove the following file: " + f.getAbsolutePath());
                }
            }
        }
    }

    public static void cleanValidationDir(String path) {
        File folder = new File(path);
        if (!folder.isDirectory()) {
            LOG.error("The specified folder is not a folder. (" + path + ")");
            return;
        }
        cleanValidationDir(folder);
    }

    public static void cleanValidationDir(NcpSide ncpSide) {
        cleanValidationDir(new File(Constants.EPSOS_PROPS_PATH + REPORT_FILES_FOLDER + "/" + ncpSide.getName()));
    }

    private static String processFile(File file) {
        final String FILE_EXTENSION = ".xml";
        String fileName;
        String[] fileNameSplit;

        if (!file.isFile()) {
            LOG.error("The supplied file is not a file.");
            return null;
        }

        fileName = file.getName();
        fileNameSplit = fileName.split(FILE_NAME_DELIMITER);

        if (fileNameSplit.length == 4) {
            return fileNameSplit[1] + " [VALIDATOR: " + fileNameSplit[2] + ", STATUS: " + fileNameSplit[3].split(FILE_EXTENSION)[0] + "]";
        } else if (fileNameSplit.length == 3) {
            return fileNameSplit[1] + " [VALIDATOR: " + fileNameSplit[2].split(FILE_EXTENSION)[0] + ", STATUS: VALIDATION NOT PERFORMED]";
        } else {
            LOG.error("The file name does not obey to the defined structure. (" + fileName + ")");
            return null;
        }

    }
}
