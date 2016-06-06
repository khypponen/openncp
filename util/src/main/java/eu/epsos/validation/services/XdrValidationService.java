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

/**
 * This class represents the wrapper for the XDR messages validation.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public class XdrValidationService extends ValidationService {

    private static XdrValidationService instance;

    @Override
    public boolean validateModel(String object, String model, NcpSide ncpSide) {
        return XcaValidationService.getInstance().validateModel(object, model, ncpSide);
    }
    
    public static XdrValidationService getInstance() {
        if (instance == null) {

            instance = new XdrValidationService();
        }
        return instance;
    }
    
    /**
     * Private constructor to avoid instantiation.
     */
    private XdrValidationService() {
    }
}
