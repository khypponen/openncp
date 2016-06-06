package eu.epsos.validation.services;

import org.junit.Before;
import org.junit.Test;

import eu.epsos.validation.ValidationTestBase;
import eu.epsos.validation.datamodel.common.NcpSide;
import eu.epsos.validation.datamodel.hl7v3.Hl7v3Model;

public class XcpdValidationServiceTest extends ValidationTestBase {
    XcpdValidationService service;
    String validatedObject;

//    @Before
    public void setUp() throws Exception {
	System.out.println("setUp()");
	service = XcpdValidationService.getInstance();
    }

//    @Test
    public void testValidate() {
	validatedObject = getResource("xcpd_request.xml");
	service.validateModel(validatedObject, Hl7v3Model.XCPD_REQUEST.toString(), NcpSide.NCP_A);
    }


}
