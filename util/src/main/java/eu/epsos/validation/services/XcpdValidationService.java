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

import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.datamodel.dts.WsUnmarshaller;
import eu.epsos.validation.datamodel.hl7v3.Hl7v3Model;
import eu.epsos.validation.datamodel.hl7v3.Hl7v3Schematron;
import eu.epsos.validation.reporting.ReportBuilder;
import net.ihe.gazelle.validator.mb.ws.ModelBasedValidationWS;
import net.ihe.gazelle.validator.mb.ws.ModelBasedValidationWSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the wrapper for the XCPD messages validation.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class XcpdValidationService extends ValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(XcpdValidationService.class);
    private static XcpdValidationService instance;

    /**
     * Private constructor to avoid instantiation.
     */
    private XcpdValidationService() {
    }

    public static XcpdValidationService getInstance() {
        if (instance == null) {
            instance = new XcpdValidationService();
        }
        return instance;
    }

    @Override
    public boolean validateModel(String object, String model, NcpSide ncpSide) {

        String hl7v3XmlDetails = "";

        if (!ValidationService.isValidationOn()) {
            LOG.info("Automated validation turned off, not validating.");
            return false;
        }

        if (Hl7v3Model.checkModel(model) == null) {
            LOG.error("The specified model is not supported by the WebService.");
            return false;
        }

        //TODO: Fix Gazelle timeout and validation error.
        //        try {
        //        ModelBasedValidationWSService hl7Service = new net.ihe.gazelle.validator.mb.ws.ModelBasedValidationWSService();
        //        ModelBasedValidationWS hl7v3Port = hl7Service.getModelBasedValidationWSPort();
        //            hl7v3XmlDetails = hl7v3Port.validateDocument(object, model); // Invocation of Web Service client.
        //        } catch (Exception ex) {
        //            LOG.error("An error has occurred during the invocation of remote validation service, please check the stack trace.", ex);
        //            //return false;
        //        }

        if (!hl7v3XmlDetails.isEmpty()) {
            return ReportBuilder.build(model, Hl7v3Model.checkModel(model).getObjectType().toString(), object, WsUnmarshaller.unmarshal(hl7v3XmlDetails), hl7v3XmlDetails.toString(), ncpSide); // Report generation.
        } else {
            LOG.error("The webservice response is empty.");
            return ReportBuilder.build(model, Hl7v3Model.checkModel(model).getObjectType().toString(), object, null, null, ncpSide); // Report generation.
        }
    }

    @Override
    public boolean validateSchematron(String object, String schematron, NcpSide ncpSide) {
        if (Hl7v3Schematron.checkSchematron(schematron) == null) {
            LOG.error("The specified schematron is not supported by the WebService.");
            return false;
        }

        return super.validateSchematron(object, schematron, ncpSide);
    }
}
