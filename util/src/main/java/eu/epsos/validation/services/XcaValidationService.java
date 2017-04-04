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
import eu.epsos.validation.datamodel.xd.XdModel;
import eu.epsos.validation.reporting.ReportBuilder;
import net.ihe.gazelle.xd.ModelBasedValidationWS;
import net.ihe.gazelle.xd.ModelBasedValidationWSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the wrapper for the XCA messages validation.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class XcaValidationService extends ValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(XcaValidationService.class);
    private static XcaValidationService instance;

    /**
     * Private constructor to avoid instantiation.
     */
    private XcaValidationService() {
    }

    public static XcaValidationService getInstance() {
        if (instance == null) {

            instance = new XcaValidationService();
        }
        return instance;
    }

    @Override
    public boolean validateModel(String object, String model, NcpSide ncpSide) {
        String xdXmlDetails = "";

        if (!ValidationService.isValidationOn()) {
            LOG.info("Automated validation turned off, not validating.");
            return false;
        }

        if (XdModel.checkModel(model) == null) {
            LOG.error("The specified model is not supported by the WebService.");
            return false;
        }

        if (object == null) {
            LOG.error("The specified object to validate is null.");
            return false;
        }

        if (object.isEmpty()) {
            LOG.error("The specified object to validate is empty.");
            return false;
        }

        //TODO: Fix Gazelle timeout and validation error.
        //        try {
        //        ModelBasedValidationWSService xdService = new ModelBasedValidationWSService();
        //        ModelBasedValidationWS xdPort = xdService.getModelBasedValidationWSPort();
        //            xdXmlDetails = xdPort.validateDocument(object, model); // Invocation of Web Service client.
        //        } catch (Exception ex) {
        //            LOG.error("An error has occurred during the invocation of remote validation service, please check the stack trace.", ex);
        //        }

        if (!xdXmlDetails.isEmpty()) {
            return ReportBuilder.build(model, XdModel.checkModel(model).getObjectType().toString(), object, WsUnmarshaller.unmarshal(xdXmlDetails), xdXmlDetails.toString(), ncpSide); // Report generation.
        } else {
            LOG.error("The webservice response is empty, writing report without validation part.");
            return ReportBuilder.build(model, XdModel.checkModel(model).getObjectType().toString(), object, null, null, ncpSide); // Report generation.
        }

    }
}
