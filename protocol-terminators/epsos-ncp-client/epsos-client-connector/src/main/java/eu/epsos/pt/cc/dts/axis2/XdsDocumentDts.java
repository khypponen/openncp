/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
package eu.epsos.pt.cc.dts.axis2;

import epsos.openncp.protocolterminator.clientconnector.DocumentId;
import tr.com.srdc.epsos.data.model.xds.XDSDocument;

/**
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class XdsDocumentDts {

    public static XDSDocument newInstance(DocumentId xdsDocument) {
        if (xdsDocument == null) {
            return null;
        }


        XDSDocument result = new XDSDocument();
        result.setDocumentUniqueId(xdsDocument.getDocumentUniqueId());
        result.setRepositoryUniqueId(xdsDocument.getRepositoryUniqueId());


        return result;
    }

    private XdsDocumentDts() {
    }
}
