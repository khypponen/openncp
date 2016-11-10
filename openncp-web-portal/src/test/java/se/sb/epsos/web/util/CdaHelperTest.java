/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sb.epsos.web.util;

import org.apache.wicket.model.StringResourceModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import se.sb.epsos.shelob.ws.client.jaxws.EpsosDocument;
import se.sb.epsos.web.model.Prescription;
import se.sb.epsos.web.model.PrescriptionRow;
import se.sb.epsos.web.service.MetaDocument;
import se.sb.epsos.web.service.mock.DocumentCatalog;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 *
 * @author andreas
 */
public class CdaHelperTest {
    
    @Before
    public void setUp() {
    	
    }
    
    @After
    public void tearDown() {
    	
    }
    
    /**
     * Test of parsePrescriptionFromDocument method, of class CdaHelper.
     */
    @Test
    public void testParsePrescriptionFromDocument() throws ParserConfigurationException, SAXException, IOException {
        byte[] cdaBytes = DocumentCatalog.get("EP.1.1.1.CDA");
        assert(cdaBytes!=null && cdaBytes.length>0);
        Prescription prescription = new Prescription(new MetaDocument("","",new EpsosDocument()),cdaBytes, new EpsosDocument());
        CdaHelper cdaHelper = new CdaHelper();
        cdaHelper.parsePrescriptionFromDocument(prescription);
        assert(prescription.getPerformer().equals("Dr. Läkare, Lars"));
        assert("Medical doctors".equals(prescription.getProfession()));
        assert(prescription.getAddress().startsWith("V\u00e5rdcentralen TC"));
        assert(prescription.getAddress().startsWith("V\u00e5rdcentralen TC"));
        assert(prescription.getRows().size()==1);
        PrescriptionRow row = prescription.getRows().get(0);
        assert(row.getDescription().startsWith("A10AB01 - insulin (human)"));
        assert(row.getIngredient().get(0).getActiveIngredient().equals("A10AB01 - insulin (human)"));
        assert(row.getIngredient().get(0).getStrength().equals("100 IU/ml"));
        assert(row.getPackageSize().getQuantityValue().equals("3"));
        assert(row.getPackageSize().getQuantityUnit().equals("ml"));
        assert(row.getNbrPackages().getQuantityValue().equals("3"));
        assert(row.getFormCode().equals("SOINCA"));
        assert(row.getFormName().equals("Injektionsvätska, lösning, cylinderampull"));
        assert(row.getStartDate().equals("2011-04-07"));
        assert(row.getEndDate().equals("2012-04-07"));
        assert(row.getFrequency().equals("Unknown"));
        assert(row.getDosage().equals("Unknown"));
        assert(row.getProductName().equals("Actrapid® Penfill®"));
        assert(row.getRoute().equals(""));
        assert(row.getProductId().equals("013509"));
        assert(row.getPrescriptionId().equals("1.133427"));
        assert(row.getSubstitutionPermittedText().equals(new StringResourceModel("prescription.substitute.null", null, "").getString()));
        assert(row.isSubstitutionPermitted() == false);
    }
}
