/**
 * This file is part of epSOS OpenNCP implementation Copyright (C) 2012 Kela
 * (The Social Insurance Institution of Finland)
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact email: epsos@kanta.fi or Konstantin.Hypponen@kela.fi
 */
package eu.epsos.protocolterminators.ws.server.common;

import org.w3c.dom.Element;

/**
 * Superclass for the DocumentSearch, PatientSearch and DocumentSubmit mock
 * implementations.
 *
 * @author gareth
 */
public class NationalConnectorGateway implements NationalConnectorInterface {

    /**
     * Submits the SOAP header to the national infrastructure. The method must
     * be called from service implementations of IHE profiles in OpenNCP.
     * Information from the SOAP header may be used in national connectors for
     * logging, decision making or as data to be included for national service
     * calls.
     *
     * @param shElement DOM Element representing the SOAP header of the request
     * message
     */
    @Override
    public void setSOAPHeader(Element shElement) {
    }
}
