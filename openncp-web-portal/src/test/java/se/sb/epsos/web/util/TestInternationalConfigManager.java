/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import se.sb.epsos.web.model.CountryVO;

public class TestInternationalConfigManager {

	private InternationalConfigManager config;
	private CountryVO vo;
	
	@Before
	public void setUp() {
		System.setProperty("epsos-internationalsearch-config-path", "src/test/resources/");
		List<CountryVO> list = new ArrayList<CountryVO>();
		vo = new CountryVO("SE", "Sverige");
		list.add(vo);		
		config = new InternationalConfigManager(list);
	}

	@Test
	public void testGetList() {
		List<String> list = config.getList(vo.getId(), "country.searchFields.id[@label]");
		assertNotNull(list);
		assertEquals("patient.search.patient.svnr", list.get(0));
	}
	
	@Test
	public void testGetProperty() {
		List<Properties> list = config.getProperties(vo.getId(), "country.searchFields");
		assertNotNull(list);
		Properties prop = new Properties();
		prop.setProperty("country.searchFields.id[@domain]0","2.16.17.710.807.1000.990.1");
		assertEquals(prop, list.get(0));
	}
}
