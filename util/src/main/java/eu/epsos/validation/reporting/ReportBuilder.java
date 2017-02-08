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

import eu.epsos.validation.datamodel.common.DetailedResult;
import eu.epsos.validation.datamodel.common.NcpSide;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.DateUtil;

/**
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class ReportBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(ReportBuilder.class);
    private static final String REPORT_FILES_FOLDER = "validation";

    /**
     * This is the main operation in the report building process. It main
     * responsibility is to generate a report based on a supplied model,
     * validation object and detailed result.
     *
     * @param model the model used in the Web Service invocation.
     * @param validationObject the validated object.
     * @param validationResult the validation result.
     *
     * @return A boolean flag, indicating if the reporting process succeed or
     * not.
     */
    public static boolean build(final String model, final String objectType, final String validationObject, final DetailedResult validationResult, String validationResponse, final NcpSide ncpSide) {

        String reportFileName;
        String reportDirName;
        String validationTestResult;
        String validationBody;
        File reportFile;
        FileWriter fw;
        BufferedWriter bw;

        if (model == null) {
            LOG.error("The specified model is null.");
            return false;
        }
        if (model.isEmpty()) {
            LOG.error("The specified model is empty.");
            return false;
        }
        if (objectType == null) {
            LOG.error("The specified objectType is null.");
            return false;
        }
        if (objectType.isEmpty()) {
            LOG.error("The specified object type is empty.");
            return false;
        }
        if (validationObject == null) {
            LOG.error("The specified validation object is null.");
            return false;
        }
        if (validationObject.isEmpty()) {
            LOG.error("The specified validation object is empty.");
            return false;
        }
        if (validationResult == null) {
            LOG.error("The specified validation result object is null. Assigning empty String to validation result.");
            validationTestResult = "";
        } else {
            validationTestResult = validationResult.getValResultsOverview().getValidationTestResult();
        }
        if (validationResponse == null || validationResponse.isEmpty()) {
            validationBody = "<!-- Validation report not available -->";
        } else {
            validationBody = validationResponse.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
        }

        reportDirName = Constants.EPSOS_PROPS_PATH + REPORT_FILES_FOLDER + "/" + ncpSide.getName();
        reportFileName = reportDirName + "/" + buildReportFileName(model, objectType, validationTestResult);

        if (checkReportDir(reportDirName)) {

            LOG.info("Writing validation report in: " + reportFileName);

            reportFile = new File(reportFileName);

            if (!reportFile.exists()) {
                try {
                    reportFile.createNewFile();
                } catch (IOException ex) {
                    LOG.error("An I/O error has occurred while creating the report file, please check the stack trace for more information.", ex);
                    return false;
                }
            }

            try {
                fw = new FileWriter(reportFile.getAbsoluteFile());
            } catch (IOException ex) {
                LOG.error("An I/O error has occurred while creating the report file for writting purposes, please check the stack trace for more information.", ex);
                return false;
            }

            bw = new BufferedWriter(fw);

            try {
                bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
                bw.write("\n");
                bw.write("<validationReport>");
                bw.write("\n");
                bw.write("<validatedObject>");
                bw.write(validationObject.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
                bw.write("</validatedObject>");
                bw.write("\n");
                bw.write("<validationResult>");
                bw.write(validationBody);
                bw.write("</validationResult>");
                bw.write("\n");
                bw.write("</validationReport>");
            } catch (IOException ex) {
                LOG.error("An I/O error has occurred while writting the report file, please check the stack trace for more information.", ex);
                return false;
            }
            try {
                bw.close();
            } catch (IOException ex) {
                LOG.error("An I/O error has occurred while closing the report file, please check the stack trace for more information.", ex);
                return false;
            }

            LOG.info("Validation report written with success");
            return true;
        }
        return false;
    }

    /**
     * This method will generate a file name for the report, based on a set of
     * parameters. Format mask: [timestamp]_[validator model]_[validation
     * result].txt
     *
     * @param model the model used in the validation.
     * @param validationResult the validation result object.
     * @return a report file name.
     */
    private static String buildReportFileName(final String model, final String objectType, final String validationTestResult) {

        final String SEPARATOR = "_";
        final String FILE_EXTENSION = ".xml";
        final String modelNormalized = model.replace(" ", "-");

        StringBuilder fileName = new StringBuilder();

        fileName.append(DateUtil.getCurrentTimeLocal());

        if (objectType != null && !objectType.isEmpty()) {
            fileName.append(SEPARATOR);
            fileName.append(objectType);
        }

        if (modelNormalized != null && !modelNormalized.isEmpty()) {
            fileName.append(SEPARATOR);
            fileName.append(modelNormalized.toUpperCase());
        }

        if (validationTestResult != null && !validationTestResult.isEmpty()) {
            fileName.append(SEPARATOR);
            fileName.append(validationTestResult.toUpperCase());
        }

        fileName.append(FILE_EXTENSION);

        return fileName.toString();

//        return DateUtil.getCurrentTimeLocal() + SEPARATOR + objectType + SEPARATOR + modelNormalized.toUpperCase() + SEPARATOR + validationTestResult.toUpperCase() + FILE_EXTENSION;
    }

    /**
     * This method will check if the report directory exists, if not, it will
     * create it.
     *
     * @param reportDirPath the complete path for the report dir.
     * @return a boolean flag stating the success of the operation.
     */
    private static boolean checkReportDir(final String reportDirPath) {
        File reportDir = new File(reportDirPath);

        if (!reportDir.exists()) {
            LOG.info("Creating validation report folder in: " + reportDirPath);
            if (!reportDir.mkdirs()) {
                LOG.error("An error has occurred during the creation of validation report directory.");
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private ReportBuilder() {
    }
}
