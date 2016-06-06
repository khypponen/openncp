package epsos.ccd.posam.tm.testcases;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import epsos.ccd.posam.tm.util.ModelBasedValidator;
import epsos.ccd.posam.tm.util.ModelValidatorResult;
import epsos.ccd.posam.tm.util.TMConstants;

import junit.framework.TestCase;
import net.ihe.gazelle.epsos.utils.ProjectDependencies;
import net.ihe.gazelle.epsos.validator.GazelleValidatorCore;
import net.ihe.gazelle.epsos.validator.Validators;

public class ModelValidatorTest extends TBase{

	public void testValidator() {
		ProjectDependencies.CDA_XSD="src\\test\\resources\\validator_res\\xsd\\CDA.xsd";
		ProjectDependencies.CDA_EPSOS_XSD="src\\test\\resources\\validator_res\\xsd\\CDA_extended.xsd";
		ProjectDependencies.CDA_XSL_TRANSFORMER="src\\test\\resources\\validator_res\\mbvalidatorDetailedResult.xsl";
		ProjectDependencies.VALUE_SET_REPOSITORY="src\\test\\resources\\validator_res\\valueSets\\";
		
		try {
			File docFile=new File("src\\test\\resources\\samples\\schPassed\\PatientSummary-pivot.xml");
			char[] buffer=new char[(int)docFile.length()];
			
			FileReader fr=new FileReader(docFile);
			fr.read(buffer);
			fr.close();
			
			String xmlStr=new String(buffer);
			
			ModelBasedValidator mdaValidator=ModelBasedValidator.getInstance();
			
			ModelValidatorResult result = mdaValidator.validate(xmlStr, TMConstants.PATIENT_SUMMARY3, true);
			assertNotNull(result);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testSupportedDocs() {
		List<String> listOfValidators = GazelleValidatorCore.getListOfValidators();
		System.out.println(listOfValidators);
	}
	
}
