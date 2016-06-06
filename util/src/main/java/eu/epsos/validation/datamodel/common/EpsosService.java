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
 * This enumerator represents the multiple epSOS Services.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public enum EpsosService {

    IDENTIFICATION_SERVICE("Identification Service", 3),
    PATIENT_SERVICE_QUERY("Patient Service - Query", 3),
    PATIENT_SERVICE_RETRIEVE("Patient Service - Retrieve", 3),
    ORDER_SERVICE_QUERY("Order Service - Query", 3),
    ORDER_SERVICE_RETRIEVE("Order Service - Retrieve", 3),
    DISPENSATION_SERVICE("Dispensation Service", 4),
    CONSENT_SERVICE("Consent Service", 4),
    HCER_SERVICE("HCER Service", 4),
    PAC_SERVICE("PAC Service", 6),
    MRO_SERVICE("MRO Service", 4);
    private String name;
    private int objectNumber;

    private EpsosService(String s, int objectNumber) {
        name = s;
        this.objectNumber = objectNumber;

    }

    @Override
    public String toString() {
        return name;
    }

    public int getObjectNumber() {
        return objectNumber;
    }
}
