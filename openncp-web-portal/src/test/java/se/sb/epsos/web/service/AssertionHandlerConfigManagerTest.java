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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: andreas
 * Date: 2011-06-27
 * Time: 16.15
 * To change this template use File | Settings | File Templates.
 */
public class AssertionHandlerConfigManagerTest {
    @Test
    public void testGetPersmissionsPrefix() throws Exception {

    }

    @Test
    public void testGetFacilityType_ROLE_PHARMACIST() throws Exception {
        String role  = AssertionHandlerConfigManager.getFacilityType("ROLE_PHARMACIST");
        assertNotNull(role);
        assertEquals("Pharmacy",role);
    }
    
    @Test
    public void testGetFacilityType_ROLE_DOCTOR() throws Exception {
        String role  = AssertionHandlerConfigManager.getFacilityType("ROLE_DOCTOR");
        assertNotNull(role);
        assertEquals("Hospital",role);
    }
    
    @Test
    public void testGetFacilityType_ROLE_NURSE() throws Exception {
        String role  = AssertionHandlerConfigManager.getFacilityType("ROLE_NURSE");
        assertNotNull(role);
        assertEquals("Hospital",role);
    }

    @Test
    public void testGetPurposeOfUse() throws Exception {
        String s  = AssertionHandlerConfigManager.getPurposeOfUse();
        assertEquals("TREATMENT",s);
    }

    @Test
    public void testGetXspaLocality() throws Exception {

    }

    @Test
    public void testGetRoleDisplayName_ROLE_PHARMACIST() throws Exception {
        String role = AssertionHandlerConfigManager.getRoleDisplayName("ROLE_PHARMACIST");
        assertNotNull(role);
        assertEquals("pharmacist",role);
    }
    
    @Test
    public void testGetRoleDisplayName_ROLE_DOCTOR() throws Exception {
        String role = AssertionHandlerConfigManager.getRoleDisplayName("ROLE_DOCTOR");
        assertNotNull(role);
        assertEquals("medical doctor",role);
    }
    
    @Test
    public void testGetRoleDisplayName_ROLE_NURSE() throws Exception {
        String role = AssertionHandlerConfigManager.getRoleDisplayName("ROLE_NURSE");
        assertNotNull(role);
        assertEquals("nurse",role);
    }

    @Test
    public void testGetPersmissions() throws Exception {
        Set<String> roles = AssertionHandlerConfigManager.getPersmissions("ROLE_PHARMACIST");
        assertNotNull(roles);
        assertTrue(roles.size()==4);
    }
}
