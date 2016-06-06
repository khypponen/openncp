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
package eu.epsos.validation.datamodel.hl7v3;

import eu.epsos.validation.datamodel.common.ObjectType;

/**
 * This enumerator gathers all the models used in the HL7v3 Validator at EVS
 * Client.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public enum Hl7v3Model {

    PDQV3_ACCEPT_ACK("PDQv3 - Accept Acknowledgement", ObjectType.PDQ),
    PDQV3_QUERY("PDQv3 - Patient Demographics Query", ObjectType.PDQ),
    PDQV3_HL7V3_CANCELLATION("PDQv3 - Patient Demographics Query HL7V3 Cancellation", ObjectType.PDQ),
    PDQV3_HL7V3_CONTINUATION("PDQv3 - Patient Demographics Query HL7V3 Continuation", ObjectType.PDQ),
    PDQV3_QUERY_RESPONSE("PDQv3 - Patient Demographics Query Response", ObjectType.PDQ),
    XCPD_REQUEST("XCPD - Cross Gateway Patient Discovery Request", ObjectType.XCPD_QUERY_REQUEST),
    XCPD_REQUEST_DEFERRED_OPTION("XCPD - Cross Gateway Patient Discovery Request (Deferred option)", ObjectType.XCPD_QUERY_REQUEST),
    XCPD_PATIENT_LOCATION_QUERY_REQUEST("XCPD - Patient Location Query Request", ObjectType.XCPD_QUERY_REQUEST),
    XCPD_PATIENT_LOCATION_QUERY_RESPONSE("XCPD - Patient Location Query Response", ObjectType.XCPD_QUERY_RESPONSE);

    public static Hl7v3Model checkModel(String model) {
        for (Hl7v3Model m : Hl7v3Model.values()) {
            if (model.equals(m.toString())) {
                return m;
            }
        }
        return null;
    }
    private String name;
    private ObjectType objectType;

    private Hl7v3Model(String s, ObjectType ot) {
        name = s;
        objectType = ot;
    }

    @Override
    public String toString() {
        return name;
    }

    public ObjectType getObjectType() {
        return objectType;
    }
}
