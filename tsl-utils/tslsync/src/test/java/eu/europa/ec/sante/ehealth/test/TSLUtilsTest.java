package eu.europa.ec.sante.ehealth.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;

import org.junit.BeforeClass;
import org.junit.Test;

import epsos.ccd.gnomon.configmanager.TSLUtils;

/**
 * Test for the TSLUtils. 
 * 
 * It is worth noticing that the class can be refactored a lot, and designed a bit more for testability. 
 * E.g., it is impossible to perform any tests without having a database. 
 * 
 * @author max
 *
 */
public class TSLUtilsTest {

	private static URI file_prefixed;
	private static URI file;

	@BeforeClass
	public static void init() {
		String filename = "src/test/resources/NCP_Service_Status_List__Austria_AT_.xml";
		File f = new File(filename);
		file = f.toURI();
		String filename_prefixed = "src/test/resources/PREFIXED_NCP_Service_Status_List__Austria_AT_.xml";
		f = new File(filename_prefixed);
		file_prefixed = f.toURI();
	}
	@Test
	public void testParseTSL() {
		
		
		String seqNum = TSLUtils.getTSLId(file.toASCIIString(), "AT");
		assertEquals((long)1, (long)Integer.valueOf(seqNum));
		
		
		seqNum = TSLUtils.getTSLId(file_prefixed.toASCIIString(), "AT");
		assertEquals((long)2, (long)Integer.valueOf(seqNum));
	}
	
	
	// @TODO other additional tests
}
