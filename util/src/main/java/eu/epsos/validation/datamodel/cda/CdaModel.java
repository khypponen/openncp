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
package eu.epsos.validation.datamodel.cda;

import eu.epsos.validation.datamodel.common.ObjectType;
import tr.com.srdc.epsos.util.Constants;

/**
 * This enumerator gathers all the models used in the CDA Model Based Validator
 * at EVS Client.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public enum CdaModel {

    BASIC_CDA("BASIC - CDA"),
    HCER("epSOS - HCER HealthCare Encounter Report"),
    MRO("epSOS - MRO Medication Related Overview"),
    PS_FRIENDLY("epSOS - Patient Summary Friendly"),
    PS_PIVOT("epSOS - Patient Summary Pivot"),
    SCANNED_DOCUMENT("epSOS - Scanned Document"),
    CONSENT("epSOS - eConsent"),
    ED_FRIENDLY("epSOS - eDispensation Friendly"),
    ED_PIVOT("epSOS - eDispensation Pivot"),
    EP_FRIENDLY("epSOS - ePrescription Friendly"),
    EP_PIVOT("epSOS - ePrescription Pivot");
    private String name;

    public static CdaModel checkModel(String model) {
        for (CdaModel m : CdaModel.values()) {
            if (model.equals(m.toString())) {
                return m;
            }
        }
        return null;
    }

    private CdaModel(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }

    public ObjectType getObjectType() {
        return ObjectType.CDA;
    }
    
    /**
     * This helper method will return a specific CDA model based on a document
     * class code (also choosing between friendly or pivot documents).
     *
     * @param classCode The document class code.
     * @param isFriendly The boolean flag stating if the document is pivot or
     * not.
     * @return the correspondent CDA model.
     */
    public static String obtainCdaModel(String classCode, boolean isPivot) {

        if (classCode == null || classCode.isEmpty()) {
            return null;
        }
        if (isPivot) {
            if (classCode.equals(Constants.EP_CLASSCODE)) {
                return CdaModel.EP_PIVOT.toString();
            }
            if (classCode.equals(Constants.MRO_CLASSCODE)) {
                return CdaModel.MRO.toString();
            }
            if (classCode.equals(Constants.PS_CLASSCODE)) {
                return CdaModel.PS_PIVOT.toString();
            }
            if (classCode.equals(Constants.ED_CLASSCODE)) {
                return CdaModel.ED_PIVOT.toString();
            }
            if (classCode.equals(Constants.HCER_CLASSCODE)) {
                return CdaModel.HCER.toString();
            }
            if (classCode.equals(Constants.CONSENT_CLASSCODE)) {
                return CdaModel.CONSENT.toString();
            }
        } else {
            if (classCode.equals(Constants.EP_CLASSCODE)) {
                return CdaModel.EP_FRIENDLY.toString();
            }
            if (classCode.equals(Constants.MRO_CLASSCODE)) {
                return CdaModel.MRO.toString();
            }
            if (classCode.equals(Constants.PS_CLASSCODE)) {
                return CdaModel.PS_FRIENDLY.toString();
            }
            if (classCode.equals(Constants.ED_CLASSCODE)) {
                return CdaModel.ED_FRIENDLY.toString();
            }
            if (classCode.equals(Constants.HCER_CLASSCODE)) {
                return CdaModel.HCER.toString();
            }
            if (classCode.equals(Constants.CONSENT_CLASSCODE)) {
                return CdaModel.CONSENT.toString();
            }
        }

        return null;
    }
}
