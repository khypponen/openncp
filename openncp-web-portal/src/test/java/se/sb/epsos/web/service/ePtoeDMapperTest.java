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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;

import se.sb.epsos.shelob.ws.client.jaxws.EpsosDocument;
import se.sb.epsos.web.auth.AuthenticatedUser;
import se.sb.epsos.web.model.Dispensation;
import se.sb.epsos.web.model.DispensationRow;
import se.sb.epsos.web.model.Ingredient;
import se.sb.epsos.web.model.Prescription;
import se.sb.epsos.web.model.PrescriptionRow;
import se.sb.epsos.web.model.QuantityVO;
import se.sb.epsos.web.service.mock.DocumentCatalog;

public class ePtoeDMapperTest {
	private MockSettings settings = withSettings().serializable();;
	
	@Before
	public void setUp() throws Exception {
	}

    @After
    public void tearDown() {
    }
    
    /**
     * Test of parsePrescriptionFromDocument method, of class CdaHelper.
     */
    @Test
	public void testCreateDispensationFromPrescription() throws Exception {
    	ePtoeDMapper mapper = new ePtoeDMapper() {
			protected String getConfigurationProperty(String key) {
				return key;
			}
		};
		Dispensation dispensation = mock(Dispensation.class, settings);
		AuthenticatedUser user = mock(AuthenticatedUser.class, settings);
		EpsosDocument dto = new EpsosDocument();
    	dto.setTitle("ePrescription");
    	dto.setDescription("enalapril (CDA)");

        DatatypeFactory f = DatatypeFactory.newInstance();
        XMLGregorianCalendar c = f.newXMLGregorianCalendar("2012-03-12");
    	dto.setCreationDate(c);

    	dto.setAuthor("Lars, Läkare (9000027)");
    	byte [] bytes = DocumentCatalog.get("EP.1.1.1.CDA");
    	Prescription p = new Prescription(new MetaDocument("","",dto), bytes, dto);
        
        List<PrescriptionRow> list = new ArrayList<PrescriptionRow>();
        PrescriptionRow prescriptionRow = new PrescriptionRow();
        prescriptionRow.setFormCode("SOINCA");
        prescriptionRow.setFormName("Injektionsvätska, lösning, cylinderampull");
        prescriptionRow.setAtcCode("A10AB01");
        prescriptionRow.setAtcName("insulin (human)");
        
        prescriptionRow.setProductName("ProductName");
        List<Ingredient> inList = new ArrayList<Ingredient>();
        Ingredient in = new Ingredient();
        in.setActiveIngredient("J01CE02 - phenoxymethylpenicillin");
        in.setStrength("800 mg");
        inList.add(in);
        prescriptionRow.setIngredient(inList);
        QuantityVO voNrOfPackages = new QuantityVO("2", "", "");
        prescriptionRow.setNbrPackages(voNrOfPackages);
        QuantityVO vo = new QuantityVO("20", "st", "1");
        prescriptionRow.setPackageSize(vo);
        list.add(prescriptionRow);  
        p.setRows(list);
        
        List<DispensationRow> dispensationList = new ArrayList<DispensationRow>();
        DispensationRow dispensationRow = new DispensationRow(prescriptionRow);
        dispensationRow.setDispense(true);
        dispensationRow.setNbrPackages(voNrOfPackages);
        QuantityVO voDispensed = new QuantityVO("20", "st", "1");
        dispensationRow.setPackageSize(voDispensed);
        dispensationRow.setProductId("013509");
        dispensationRow.setProductName("Actrapid® Penfill®");
        dispensationList.add(dispensationRow);
        
        when(dispensation.getPrescription()).thenReturn(p);
        when(dispensation.getRows()).thenReturn(dispensationList);
        
		List<String> list2 = new ArrayList<String>();
		list2.add("Tolvans");
		when(user.getRoles()).thenReturn(list2);
		when(user.getUserId()).thenReturn("Tolvan");
		when(user.getOrganizationId()).thenReturn("12");
		when(user.getFamilyName()).thenReturn("Tolvansson");
		when(user.getGivenName()).thenReturn("Tolvan");
		when(user.getOrganizationName()).thenReturn("TolvansOrganisation");
		when(user.getTelecom()).thenReturn("121212");
		when(user.getStreet()).thenReturn("Tolv");
		when(user.getPostalCode()).thenReturn("121212");
		when(user.getCity()).thenReturn("Tolv");
		byte[] doc = mapper.createDispensationFromPrescription(dispensation, user, "cdaIdExtension", "pdfIdExtension");
		assert(doc != null);
	}
}
