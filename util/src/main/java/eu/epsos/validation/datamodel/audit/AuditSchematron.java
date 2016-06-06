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
package eu.epsos.validation.datamodel.audit;

import eu.epsos.validation.datamodel.common.ObjectType;

/**
 * This enumerator gathers all the schematrons used in the Audit Messages Validator
 * at EVS Client.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public enum AuditSchematron {

    EPSOS_ISSUANCE_HCP_ASSERTION("epSOS - HCP Identity Assertion Issuance"),
    EPSOS_ORDER_SERVICE_AUDIT_SC("epSOS - Order Service Audit (Consumer)"),
    EPSOS_ISSUANCE_TRC_ASSERTION("epSOS - Treatment Relationship Confirmation Assertion Issuance"),
    EPSOS_PATIENT_PRIVACY_AUDIT("epSOS - Patient Privacy Audit"),
    EPSOS_OS_AUDIT_SP("epSOS - Order Service Audit (Provider)"),
    EPSOS_PATIENT_SERVICE_AUDIT_SC("epSOS - Patient Service Audit (Consumer)"),
    EPSOS_PIVOT_TRANSLATION("epSOS - Pivot Translation Medical Document"),
    EPSOS_PATIENT_SERVICE_AUDIT_SP("epSOS - Patient Service Audit (Provider)"),
    EPSOS_PATIENT_ID_MAPPING_AUDIT("epSOS - Patient ID Mapping Audit"),
    EPSOS_IMPORT_NCP_TRUSTED_LIST("epSOS - Import NCP Trusted Service List");
    private String name;

    public static AuditSchematron checkSchematron(String model) {
        for (AuditSchematron s : AuditSchematron.values()) {
            if (model.equals(s.toString())) {
                return s;
            }
        }
        return null;
    }

    private AuditSchematron(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }

    public ObjectType getObjectType() {
        return ObjectType.AUDIT;
    }
}
