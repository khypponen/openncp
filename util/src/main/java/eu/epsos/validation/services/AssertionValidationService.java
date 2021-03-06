 /*
 *  This file is part of epSOS OpenNCP implementation
 *  Copyright (C) 2014 iUZ Technologies and Gnomon Informatics
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *  Contact email: contact@iuz.pt, info@gnomon.com.gr
 */
package eu.epsos.validation.services;

import eu.epsos.validation.datamodel.common.NcpSide;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Assertion Validation Service
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class AssertionValidationService extends ValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(AssertionValidationService.class);

    @Override
    public boolean validateModel(String object, String model, NcpSide ncpSide) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean validateSchematron(String object, String schematron, NcpSide ncpSide) {

        net.ihe.gazelle.sch.validator.ws.GazelleObjectValidatorService gazelleObjVal;
        net.ihe.gazelle.sch.validator.ws.GazelleObjectValidator gazelleObjValPOrt;
        String xmlDetails = "";

        if (!ValidationService.isValidationOn()) {
            LOG.info("Automated validation turned off, not validating.");
            return false;
        }
        gazelleObjVal = new net.ihe.gazelle.sch.validator.ws.GazelleObjectValidatorService();
        gazelleObjValPOrt = gazelleObjVal.getGazelleObjectValidatorPort();

        try {
            xmlDetails = gazelleObjValPOrt.validateObject(DatatypeConverter.printBase64Binary(object.getBytes()), schematron, schematron); // Invocation of Web Service.
        } catch (Exception ex) {
            LOG.error("An error has occurred during the invocation of remote validation service, please check the stack trace.", ex);
            return false;
        }

        LOG.info("epSOS Assertion validation result, using " + schematron + " schematron:");
        LOG.info(xmlDetails);
        return true;
    }

}
