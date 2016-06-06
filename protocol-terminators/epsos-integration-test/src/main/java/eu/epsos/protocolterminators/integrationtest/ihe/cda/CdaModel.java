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
package eu.epsos.protocolterminators.integrationtest.ihe.cda;

/**
 * This enumerator gathers all the models used in the CDA Model Based Validator
 * at EVS Client.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public enum CdaModel {

    BASIC_CDA(new String("BASIC - CDA")),
    HCER(new String("epSOS - HCER HealthCare Encounter Report")),
    MRO(new String("epSOS - MRO Medication Related Overview")),
    PS_FRIENDLY(new String("epSOS - Patient Summary Friendly")),
    PS_PIVOT(new String("epSOS - Patient Summary Pivot")),
    SCANNED_DOCUMENT(new String("epSOS - Scanned Document")),
    CONSENT(new String("epSOS - eConsent")),
    ED_FRIENDLY(new String("epSOS - eDispensation Friendly")),
    ED_PIVOT(new String("epSOS - eDispensation Pivot")),
    EP_FRIENDLY(new String("epSOS - ePrescription Friendly")),
    EP_PIVOT(new String("epSOS - ePrescription Pivot"));
    private String name;

    private CdaModel(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }
}
