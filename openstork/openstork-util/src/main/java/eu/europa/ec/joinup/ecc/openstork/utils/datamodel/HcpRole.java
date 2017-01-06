 /*
 *  This file is part of epSOS OpenNCP implementation
 *  Copyright (C) 2014 iUZ Technologies and Gnomon Informatics
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *  Contact email: contact@iuz.pt, info@gnomon.com.gr
 */
package eu.europa.ec.joinup.ecc.openstork.utils.datamodel;

import eu.epsos.assertionvalidator.XSPARole;

/**
 * This ENUM represents all the possible HCP roles in the concerned scope.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public enum HcpRole {

    MEDICAL_DOCTOR("medical doctor", XSPARole.PHYSICIAN),
    PHARMACIST("pharmacist", XSPARole.PHARMACIST),
    NURSE("nurse", XSPARole.NURSE),
    PATIENT("patient", XSPARole.PATIENT),
    ADMINISTRATOR("administrator", XSPARole.ADMISSION_CLERK);

    private final String designation;
    private final XSPARole xspaRole;

    private HcpRole(String s, XSPARole xspaRole) {
        designation = s;
        this.xspaRole = xspaRole;
    }

    @Override
    public String toString() {
        return designation;
    }

    /**
     * @return the xspaRole
     */
    public XSPARole getXspaRole() {
        return xspaRole;
    }
}
