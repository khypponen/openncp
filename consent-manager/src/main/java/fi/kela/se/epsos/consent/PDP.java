package fi.kela.se.epsos.consent;
/**
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 Kela (The Social Insurance Institution of Finland)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Contact email: epsos@kanta.fi or Konstantin.Hypponen@kela.fi
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extremely simplified PDP service implementation
 */
public class PDP {

    private static Logger logger = LoggerFactory.getLogger(PDP.class);

    public static boolean consentGiven(String patientId, String countryId) {
        boolean consentGiven = true;
        logger.info("Checking consent of patient " + patientId + " for country " + countryId);
        // TODO: actual check
        logger.info("Consent of patient " + patientId + " valid for country " + countryId + ": " + consentGiven);
        return consentGiven;
    }
}
