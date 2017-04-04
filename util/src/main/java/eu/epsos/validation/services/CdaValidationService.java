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

import eu.epsos.validation.datamodel.cda.CdaModel;
import eu.epsos.validation.datamodel.cda.CdaSchematron;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.datamodel.dts.WsUnmarshaller;
import eu.epsos.validation.reporting.ReportBuilder;
import net.ihe.gazelle.document.ModelBasedValidationWS;
import net.ihe.gazelle.document.ModelBasedValidationWSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the wrapper for the CDA documents validation.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class CdaValidationService extends ValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(XcaValidationService.class);
    private static CdaValidationService instance;

    /**
     * Private constructor to avoid instantiation.
     */
    private CdaValidationService() {
    }

    public static CdaValidationService getInstance() {
        if (instance == null) {

            instance = new CdaValidationService();
        }
        return instance;
    }

    @Override
    public boolean validateModel(String object, String model, NcpSide ncpSide) {
        String cdaXmlDetails = "";

        if (!ValidationService.isValidationOn()) {
            LOG.info("Automated validation turned off, not validating.");
            return false;
        }

        if (CdaModel.checkModel(model) == null) {
            LOG.error("The specified model is not supported by the WebService.");
            return false;
        }

        //TODO: Fix Gazelle timeout and validation error.
        //        try {
        //        ModelBasedValidationWSService cdaService = new ModelBasedValidationWSService();
        //        ModelBasedValidationWS cdaPort = cdaService.getModelBasedValidationWSPort();
        //            cdaXmlDetails = cdaPort.validateDocument(object, model); // Invocation of Web Service client.
        //        } catch (SOAPException_Exception ex) {
        //            LOG.error("An error has occurred during the invocation of remote validation service, please check the stach trace.", ex);
        //        }

        if (!cdaXmlDetails.isEmpty()) {
            return ReportBuilder.build(model, CdaModel.checkModel(model).getObjectType().toString(), object, WsUnmarshaller.unmarshal(cdaXmlDetails), cdaXmlDetails.toString(), ncpSide); // Report generation.
        } else {
            LOG.error("The webservice response is empty.");
            return ReportBuilder.build(model, CdaModel.checkModel(model).getObjectType().toString(), object, null, null, ncpSide); // Report generation.
        }

    }

    @Override
    public boolean validateSchematron(String object, String schematron, NcpSide ncpSide) {
        if (CdaSchematron.checkSchematron(schematron) == null) {
            LOG.error("The specified schematron is not supported by the WebService.");
            return false;
        }

        return super.validateSchematron(object, schematron, ncpSide);
    }
}
