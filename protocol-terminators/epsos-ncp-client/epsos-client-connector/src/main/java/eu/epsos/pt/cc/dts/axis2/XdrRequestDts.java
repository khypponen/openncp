/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
package eu.epsos.pt.cc.dts.axis2;

import epsos.openncp.protocolterminator.clientconnector.EpsosDocument1;
import epsos.openncp.protocolterminator.clientconnector.PatientDemographics;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.opensaml.saml2.core.Assertion;
import tr.com.srdc.epsos.data.model.XdrRequest;
import tr.com.srdc.epsos.util.Constants;
import tr.com.srdc.epsos.util.FileUtil;

/**
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class XdrRequestDts {

    public static XdrRequest newInstance(final EpsosDocument1 document,
                                         final PatientDemographics patient,
                                         final Assertion idAssertion, final Assertion trcAssertion) throws ParseException {
        if (document == null) {
            return null;
        }

        XdrRequest result = new XdrRequest();
        try {
            result.setCda(new String(document.getBase64Binary(), FileUtil.UTF_8));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        result.setCdaId(document.getUuid());


        result.setPatient(eu.epsos.pt.cc.dts.PatientDemographicsDts.newInstance(patient));

        result.setcountryCode(Constants.COUNTRY_CODE);
        result.setCountryName(Constants.COUNTRY_NAME);

        result.setIdAssertion(idAssertion);
        result.setTrcAssertion(trcAssertion);

        return result;
    }
}
