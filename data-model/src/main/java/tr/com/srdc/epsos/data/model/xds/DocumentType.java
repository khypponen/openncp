/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2013  SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
package tr.com.srdc.epsos.data.model.xds;

/**
 * This ENUM gathers all the epSOS transacted documents.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public enum DocumentType {

    PATIENT_SUMMARY("Patient Summary"),
    EDISPENSATION("eDispensation"),
    EPRESCRIPTION("ePrescription"),
    MRO("Medication Summary"),
    HCER("Heatlhcare Encounter Report Summary");
    private String displayName;

    private DocumentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName.toString();
    }
}
