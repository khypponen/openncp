package epsos.ccd.posam.tm.testcases;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;

import epsos.ccd.posam.tm.exception.TMError;
import epsos.ccd.posam.tm.service.ITransformationService;
import epsos.ccd.posam.tm.util.TMConfiguration;
import epsos.ccd.posam.tm.util.XmlUtil;
import epsos.ccd.posam.tsam.exception.ITMTSAMEror;

/**
 * Base Class for TM JUnit tests
 * 
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.17, 2010, 20 October
 */
public class TBase extends TestCase {
	
	protected ApplicationContext beanFactory;
	protected ITransformationService tmService = null;
	protected TMConfiguration tmConfig;
	protected String samplesDir = ".//src//test//resources//samples//";
	
	protected TBase() {		
		
	}	

	@Override
	protected void setUp() throws Exception {
		beanFactory=new ClassPathXmlApplicationContext("ctx_tm_test.xml");
		tmService=(ITransformationService) beanFactory.getBean(ITransformationService.class.getName());	
		tmConfig=(TMConfiguration) beanFactory.getBean("tmConfiguration");
	}

	@Override
	protected void tearDown() throws Exception {
	}

	public ITransformationService getTmService() {
		return tmService;
	}
	
	public TMConfiguration getTmConfig() {
		return tmConfig;
	}
	
	protected Document getDocument(File file){
	    return XmlUtil.getDocument(file, true);
	}
	
	protected Document  getInvalidDocument() {
		Document document = null;
		try {
			// non valid document
			document = getDocument(new File(samplesDir + "invalidCDA_schema.xml"));
		} catch (Exception e) {
			//DO nothing, junit tests checks if not null is returned
		}
		return document;		
	}
	
	protected Document getDocument() {
		Document document = null;
		try {			
			document = getDocument(new File(samplesDir
					+ "validCDA.xml"));
		} catch (Exception e) {
			//DO nothing, junit tests checks if not null is returned
		}
		return document;
	}	
	
	protected boolean containsError(List<ITMTSAMEror> list, TMError error) {
		for (ITMTSAMEror er : list) {
			if(er.getCode()==error.getCode()) {
				return true;
			}
		}
		return false;
	}
}
