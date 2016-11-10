/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.service;

import java.util.List;

import junit.framework.TestCase;
import se.sb.epsos.web.model.CountryVO;
import se.sb.epsos.web.model.PatientIdVO;


public class TestCountryConfigManager extends TestCase {

    public void testGetHomeCommunityId() {
    	String test = CountryConfigManager.getHomeCommunityId("SE");
    	assertNotNull(test);
    	assertEquals(test, "2.16.17.710.807.1000.990.1");
    }
        
    public void testGetText() {
    	CountryVO vo = new CountryVO("SE", "Sverige");
    	String test = CountryConfigManager.getText(vo);
    	assertNotNull(test);
    	assertTrue(test.startsWith("Ange"));
    }
    
    public void testGetCountries() {
		List<CountryVO> countries = CountryConfigManager.getCountries();
		assertNotNull(countries);
		assertEquals("AT", countries.get(0).getId());
       }
    
    public void testGetPatientIdentifiers() {
    	CountryVO se = new CountryVO("SE", "Sverige");
    	List<PatientIdVO> patIds = CountryConfigManager.getPatientIdentifiers(se);
    	assertNotNull(patIds);
    	assertFalse(patIds.isEmpty());
    	assertTrue(patIds.size()==1);
        assertEquals("2.16.17.710.807.1000.990.1", patIds.get(0).getDomain());
    	assertNull(patIds.get(0).getValue());
    	assertEquals("patient.search.patient.svnr", patIds.get(0).getLabel());
    }
    
}
