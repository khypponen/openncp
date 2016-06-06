/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012  SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
package eu.epsos.pt.ws.client.xdr.dts;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import tr.com.srdc.epsos.data.model.XdrResponse;

/**
 * This is an Data Transformation Service. This provide functions to transform
 * data into a XdrResponse object.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class XdrResponseDts {

    public static XdrResponse newInstance(RegistryResponseType registryResponse) {
        final XdrResponse result = new XdrResponse();

        if (registryResponse.getStatus() != null) {
            result.setReponseStatus(registryResponse.getStatus());
        }

        return result;
    }

    /**
     * Private constructor to disable class instantiation.
     */
    private XdrResponseDts() {
    }
}
