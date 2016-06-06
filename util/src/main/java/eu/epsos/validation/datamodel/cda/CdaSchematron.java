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
 * This enumerator gathers all the schematrons used in the CDA Schematron
 * Validator at EVS Client.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public enum CdaSchematron {

    PS_FRIENDLY("Patient Summary - Pivot"),
    PS_PIVOT("epSOS - Patient Summary Pivot"),
    SCANNED_DOCUMENT_FRIENDLY("Scanned Document - Friendly"),
    SCANNED_DOCUMENT_PIVOT("Scanned Document - Pivot"),
    CONSENT_NOSD_FRIENDLY("epSOS - eConsent - no SD - Friendly"),
    CONSENT_SD_FRIENDLY("epSOS - eConsent - with SD - Friendly"),
    CONSENT_SD_PIVOT("epSOS - eConsent - with SD - Pivot"),
    ED_FRIENDLY("eDispensation - Friendly"),
    ED_PIVOT("eDispensation - Pivot"),
    EP_FRIENDLY("ePrescription - Friendly"),
    EP_PIVOT("ePrescription - Pivot");
    private String name;

    public static CdaSchematron checkSchematron(String schematron) {
        for (CdaSchematron m : CdaSchematron.values()) {
            if (schematron.equals(m.toString())) {
                return m;
            }
        }
        return null;
    }

    private CdaSchematron(String s) {
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
     * This helper method will return a specific CDA Schematron based on a
     * document class code (also choosing between friendly or pivot documents).
     *
     * @param classCode The document class code.
     * @param isFriendly The boolean flag stating if the document is pivot or
     * not.
     * @return the correspondent CDA model.
     */
    public static String obtainCdaSchematron(String classCode, boolean isPivot) {

        if (classCode == null || classCode.isEmpty()) {
            return null;
        }
        if (isPivot) {
            if (classCode.equals(Constants.EP_CLASSCODE)) {
                return CdaSchematron.EP_PIVOT.toString();
            }
            if (classCode.equals(Constants.MRO_CLASSCODE)) {
                throw new UnsupportedOperationException("There is no avaliable schematron for the supplied CLASSCODE: " + classCode);
            }
            if (classCode.equals(Constants.PS_CLASSCODE)) {
                return CdaSchematron.PS_PIVOT.toString();
            }
            if (classCode.equals(Constants.ED_CLASSCODE)) {
                return CdaSchematron.ED_PIVOT.toString();
            }
            if (classCode.equals(Constants.HCER_CLASSCODE)) {
                throw new UnsupportedOperationException("There is no avaliable schematron for the supplied CLASSCODE: " + classCode);
            }
            if (classCode.equals(Constants.CONSENT_CLASSCODE)) {
                throw new UnsupportedOperationException("There is no avaliable schematron for the supplied CLASSCODE: " + classCode);
            }
        } else {
            if (classCode.equals(Constants.EP_CLASSCODE)) {
                return CdaSchematron.EP_FRIENDLY.toString();
            }
            if (classCode.equals(Constants.MRO_CLASSCODE)) {
                throw new UnsupportedOperationException("There is no avaliable schematron for the supplied CLASSCODE: " + classCode);
            }
            if (classCode.equals(Constants.PS_CLASSCODE)) {
                return CdaSchematron.PS_FRIENDLY.toString();
            }
            if (classCode.equals(Constants.ED_CLASSCODE)) {
                return CdaSchematron.ED_FRIENDLY.toString();
            }
            if (classCode.equals(Constants.HCER_CLASSCODE)) {
               throw new UnsupportedOperationException("There is no avaliable schematron for the supplied CLASSCODE: " + classCode);
            }
            if (classCode.equals(Constants.CONSENT_CLASSCODE)) {
                throw new UnsupportedOperationException("There is no avaliable schematron for the supplied CLASSCODE: " + classCode);
            }
        }

        return null;
    }
}
