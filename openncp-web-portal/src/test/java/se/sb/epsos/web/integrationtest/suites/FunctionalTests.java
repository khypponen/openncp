/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.integrationtest.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import se.sb.epsos.web.integrationtest.webtests.DispensePrescriptionTest;

import se.sb.epsos.web.integrationtest.webtests.IEHack;
import se.sb.epsos.web.integrationtest.webtests.LoginTest;
import se.sb.epsos.web.integrationtest.webtests.PdfPageTest;
import se.sb.epsos.web.integrationtest.webtests.QueryPersonAsDoctorTest;
import se.sb.epsos.web.integrationtest.webtests.QueryPersonAsPharmacistTest;
import se.sb.epsos.web.integrationtest.webtests.TrcAsDoctorTest;
import se.sb.epsos.web.integrationtest.webtests.TrcAsPharmacistTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	IEHack.class,
    LoginTest.class,
    QueryPersonAsDoctorTest.class,
    QueryPersonAsPharmacistTest.class,
    TrcAsDoctorTest.class,
    TrcAsPharmacistTest.class,
    DispensePrescriptionTest.class,
    PdfPageTest.class
        
})
public class FunctionalTests {

}
