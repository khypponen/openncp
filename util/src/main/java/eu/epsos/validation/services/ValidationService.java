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
package eu.epsos.validation.services;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.datamodel.dts.WsUnmarshaller;
import eu.epsos.validation.datamodel.hl7v3.Hl7v3Schematron;
import eu.epsos.validation.reporting.ReportBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;

/**
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public abstract class ValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationService.class);
    private static final String VALIDATION_STATUS_PROPERTY_NAME = "automated.validation";

    /**
     * This method will check is the automated validation is turned on, by the
     * setting of a specific property in the properties database.
     *
     * @return the boolean value, stating if the automated validation is on
     */
    protected static boolean isValidationOn() {

        String validationOnVal = ConfigurationManagerService.getInstance().getProperty(VALIDATION_STATUS_PROPERTY_NAME);

        if (validationOnVal == null) {
            LOG.error("The value of Validation Property in properties database is null.");
            return false;
        }
        if (validationOnVal.isEmpty()) {
            LOG.error("The value of Validation Property in properties database is empty.");
            return false;
        }

        return Boolean.parseBoolean(validationOnVal);
    }

    /**
     * This abstract method defines the operation that will trigger the model
     * based validation of a specific object (e.g. a document or transaction
     * message), using a specific model.
     *
     * @param object  the object to validate (e.g. a document or transaction
     *                message)
     * @param model   the specific model to be used
     * @param ncpSide
     * @return the result of validation execution: false if errors occur.
     */
    public abstract boolean validateModel(String object, String model, NcpSide ncpSide);

    /**
     * This method executes the operation that will trigger the schematron based
     * validation of a specific object (e.g. a document or transaction message),
     * using a specific schematron. This operation is shared by many object
     * types and they all share the same endpoint.
     *
     * @param object     the object to validate (e.g. a document or transaction
     *                   message)
     * @param schematron the specific schematron to be used
     * @param ncpSide    the specific NCP side, either NCP-A or NCP-B.
     * @return the result of validation execution: false if errors occur.
     */
    protected boolean validateSchematron(String object, String schematron, NcpSide ncpSide) {
        String xmlDetails = "";

        if (!ValidationService.isValidationOn()) {
            LOG.info("Automated validation turned off, not validating.");
            return false;
        }

//        try {
//            net.ihe.gazelle.sch.validator.ws.GazelleObjectValidatorService gazelleObjVal = new net.ihe.gazelle.sch.validator.ws.GazelleObjectValidatorService();
//            net.ihe.gazelle.sch.validator.ws.GazelleObjectValidator gazelleObjValPOrt = gazelleObjVal.getGazelleObjectValidatorPort();
//            xmlDetails = gazelleObjValPOrt.validateObject(DatatypeConverter.printBase64Binary(object.getBytes()), schematron, schematron); // Invocation of Web Service.
//        } catch (Exception ex) {
//            LOG.error("An error has occurred during the invocation of remote validation service, please check the stack trace.", ex);
//            return false;
//        }

        if (!xmlDetails.isEmpty()) {
            return ReportBuilder.build(schematron, Hl7v3Schematron.checkSchematron(schematron).getObjectType().toString(), object, WsUnmarshaller.unmarshal(xmlDetails), xmlDetails, ncpSide); // Report generation.
        } else {
            LOG.error("The webservice response is empty.");
            return ReportBuilder.build(schematron, Hl7v3Schematron.checkSchematron(schematron).getObjectType().toString(), object, null, null, ncpSide); // Report generation
        }
    }
}
