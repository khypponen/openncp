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

/**
 * This enum describes several organizations used for this scope.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public enum Organization {
    
    HOSPITAL("Hospital"),
    PHARMACY("Pharmacy"),
    RESIDENT_PHYSICIAN("Resident Physician");
    
    private final String designation;

    private Organization(String s) {
        designation = s;
    }

    @Override
    public String toString() {
        return designation;
    }

}
