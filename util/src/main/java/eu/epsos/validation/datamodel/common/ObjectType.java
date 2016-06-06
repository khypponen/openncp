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
package eu.epsos.validation.datamodel.common;

/**
 * This enumerator represents multiple object/messages types.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public enum ObjectType {

    CDA("CDA"),
    AUDIT("AUDIT"),
    PDQ("PDQ"),
    XCPD_QUERY_REQUEST("XCPD-QUERY-REQUEST"),
    XCPD_QUERY_RESPONSE("XCPD-QUERY-RESPONSE"),
    XCA_QUERY_REQUEST("XCA-QUERY-REQUEST"),
    XCA_QUERY_RESPONSE("XCA-QUERY-RESPONSE"),
    XCA_RETRIEVE_REQUEST("XCA-RETRIEVE-REQUEST"),
    XCA_RETRIEVE_RESPONSE("XCA-RETRIEVE-RESPONSE"),
    XDR_SUBMIT_REQUEST("XDR-SUBMIT-REQUEST"),
    XDR_SUBMIT_RESPONSE("XDR-SUBMIT-REPONSE"),
    XCF_REQUEST("XCF-REQUEST"),
    XCF_RESPONSE("XCF-REPONSE"),
    ASSERTION("ASSERION");
    private String name;

    private ObjectType(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }
}
