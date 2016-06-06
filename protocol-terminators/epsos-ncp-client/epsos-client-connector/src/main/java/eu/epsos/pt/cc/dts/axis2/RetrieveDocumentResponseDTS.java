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

import epsos.openncp.protocolterminator.clientconnector.RetrieveDocumentResponse;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

/**
 * This is an Data Transformation Service. This provide functions to transform
 * data into a RetrieveDocumentResponseDTS object.
 *
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class RetrieveDocumentResponseDTS {

    public static RetrieveDocumentResponse newInstance(DocumentResponse documentResponse) {
        
        /*
         * PRE-CONDITIONS
         */
        
        if(documentResponse == null)
        {
            return null;
        }
        
        /*
         * BODY
         */
        
        final RetrieveDocumentResponse result = RetrieveDocumentResponse.Factory.newInstance();

        result.setReturn(DocumentDts.newInstance(documentResponse));
        
        return result;
    }

    /**
     * Private constructor to disable class instantiation.
     */
    private RetrieveDocumentResponseDTS() {
    }
}
