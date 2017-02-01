/**
 * This file is part of epSOS OpenNCP implementation Copyright (C) 2012 SALAR
 * and Kela (The Social Insurance Institution of Finland)
 * <p>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Contact email: daniel.gronberg@diabol.se (SALAR) epsos@kanta.fi or
 * Konstantin.Hypponen@kela.fi (Kela)
 */
package eu.epsos.protocolterminators.ws.server.xcpd.impl;

import eu.epsos.protocolterminators.ws.server.common.NationalConnectorGateway;
import eu.epsos.protocolterminators.ws.server.exception.NIException;
import eu.epsos.protocolterminators.ws.server.xcpd.PatientSearchInterfaceWithDemographics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.data.model.PatientDemographics;
import tr.com.srdc.epsos.data.model.PatientDemographics.Gender;
import tr.com.srdc.epsos.data.model.PatientId;
import tr.com.srdc.epsos.securityman.exceptions.InsufficientRightsException;
import tr.com.srdc.epsos.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Mock implementation of the PatientSearchInterface, to be replaced nationally.
 *
 * @author danielgronberg
 * @author Konstantin.Hypponen@kela.fi
 */
public class PatientSearchMockImpl extends NationalConnectorGateway
        implements PatientSearchInterfaceWithDemographics {

    private static final Logger LOG = LoggerFactory.getLogger(PatientSearchMockImpl.class);

    private static final String GENDER = "administrativeGender";
    private static final String BIRTH_DATE = "birthDate";
    private static final String BIRTH_DATE_YEAR = BIRTH_DATE + ".year";
    private static final String BIRTH_DATE_MONTH = BIRTH_DATE + ".month";
    private static final String BIRTH_DATE_DAY = BIRTH_DATE + ".day";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String EMAIL = "email";
    private static final String FAMILY_NAME = "familyName";
    private static final String GIVEN_NAME = "givenName";
    private static final String POSTAL_CODE = "postalCode";
    private static final String STREET = "street";
    private static final String TELEPHONE = "telephone";

    @Override
    public String getPatientId(String citizenNumber) throws NIException, InsufficientRightsException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<PatientDemographics> getPatientDemographics(List<PatientId> idList) throws NIException, InsufficientRightsException {

        LOG.info("Searching patients at NI Mock...");

        List<PatientDemographics> result = new ArrayList<>(1);

        /*
         * Patient file
         */
        String patientFile = Constants.EPSOS_PROPS_PATH + "integration/";
        PatientId id = null;

        /* file path */
        for (PatientId aux : idList) {
            File rootDir = new File(patientFile + aux.getRoot());

            if (rootDir.exists()) {
                File extensionFile = new File(patientFile + aux.getRoot() + "/"
                        + aux.getExtension() + ".properties");

                if (extensionFile.exists()) {
                    patientFile += aux.getRoot() + "/"
                            + aux.getExtension() + ".properties";
                    id = aux;
                    break;
                }
            }
        }

        if (id == null) {
            LOG.info("Patient not found: " + idList.get(0));
            return new ArrayList<>(0);
        }

        /* read file */
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(patientFile));

            PatientDemographics patient = new PatientDemographics();
            patient.setIdList(Arrays.asList(id));
            patient.setAdministrativeGender(Gender.parseGender(properties.getProperty(GENDER)));

            Calendar birth;
            Integer year = Integer.valueOf(properties.getProperty(BIRTH_DATE_YEAR));
            Integer month = Integer.valueOf(properties.getProperty(BIRTH_DATE_MONTH));
            Integer day = Integer.valueOf(properties.getProperty(BIRTH_DATE_DAY));
            birth = new GregorianCalendar(year, month, day);
            patient.setBirthDate(birth.getTime());

            patient.setCity(properties.getProperty(CITY));
            patient.setCountry(properties.getProperty(COUNTRY));
            patient.setEmail(properties.getProperty(EMAIL));
            patient.setFamilyName(properties.getProperty(FAMILY_NAME));
            patient.setGivenName(properties.getProperty(GIVEN_NAME));
            patient.setPostalCode(properties.getProperty(POSTAL_CODE));
            patient.setStreetAddress(properties.getProperty(STREET));
            patient.setTelephone(properties.getProperty(TELEPHONE));
            result.add(patient);
        }
        catch (Exception ex) {
            LOG.error(null,ex);
            return new ArrayList<PatientDemographics>(0);
        }

        return result;
    }

    @Override
    public void setPatientDemographics(PatientDemographics pd) {
        LOG.info(pd.toString());
    }
}
