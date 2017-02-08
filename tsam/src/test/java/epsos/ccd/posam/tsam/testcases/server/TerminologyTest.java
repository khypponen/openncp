package epsos.ccd.posam.tsam.testcases.server;

import epsos.ccd.posam.tsam.exception.ITMTSAMEror;
import epsos.ccd.posam.tsam.exception.TSAMError;
import epsos.ccd.posam.tsam.response.RetrievedConcept;
import epsos.ccd.posam.tsam.response.TSAMResponseStructure;
import epsos.ccd.posam.tsam.service.ITerminologyService;
import epsos.ccd.posam.tsam.util.CodedElement;
import org.h2.tools.RunScript;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.Assert.*;

public class TerminologyTest {

    private static final Logger log = LoggerFactory.getLogger(TerminologyTest.class);

    private static ITerminologyService service;
    private static ApplicationContext beanFactory;

    @BeforeClass
    public static void setUp() throws Exception {
        beanFactory = new ClassPathXmlApplicationContext("tsamContext.xml");

        if (beanFactory == null) {

            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:file:db/tsam;AUTO_SERVER=TRUE", "sa", "");
            try {
                RunScript.execute(conn, new FileReader("src/test/resources/schema.sql"));
                RunScript.execute(conn, new FileReader("src/test/resources/data.sql"));
            } finally {
                conn.close();
            }
        }

        service = (ITerminologyService) beanFactory.getBean(ITerminologyService.class.getName());
    }

    @Test
    public void testTranslationWithVersion() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", "January 2010", null);
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");

        assertNotNull(response.getDesignation());
        assertEquals(response.getDesignation(), "diastolicky arteriovy tlak");
    }

    @Test
    public void testTranslationWithVersionWarning() {
        CodedElement ce = new CodedElement("271650006", "SNOMED C", "2.16.840.1.113883.6.96", "January 2010", null);
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
        assertNotNull(response.getDesignation());
        assertEquals(response.getDesignation(), "diastolicky arteriovy tlak");
        assertTrue(response.getWarnings().size() > 0);
        ITMTSAMEror warn = response.getWarnings().get(0);
        warn.getCode().equals(TSAMError.WARNING_CODE_SYSETEM_NAME_DOESNT_MATCH);
        System.out.println(response.getDesignation());
    }

    @Test
    public void testTranslationWithValueSetOid() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", "January 2010", "1.3.6.1.4.1.12559.11.10.1.3.1.42.18");
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
        assertNotNull(response.getDesignation());
        assertEquals(response.getDesignation(), "diastolicky arteriovy tlak");
    }

    @Test
    public void testTranslationWithValueSetVersion() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", "January 2010", "1.3.6.1.4.1.12559.11.10.1.3.1.42.18", "MVC 1.0");
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
        assertNotNull(response.getDesignation());
        assertEquals(response.getDesignation(), "diastolicky arteriovy tlak");
    }

    @Test
    public void testTranslationWithPrevVersion() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", "July 2010", null);
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
        response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
        assertNotNull(response.getDesignation());
        assertEquals(response.getDesignation(), "diastolicky arteriovy tlak");
    }

    @Test
    public void testTranslationCurrent() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", null, null);
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
        assertNotNull(response.getDesignation());
        assertEquals(response.getDesignation(), "diastolicky arteriovy tlak");
    }

    @Test
    public void testTranslationNoCodeSystem() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "noexist", null, null);
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
        assertTrue(response.getErrors().size() > 0);
        assertTrue(response.getErrors().get(0).getCode() == TSAMError.ERROR_CODE_SYSTEM_NOTFOUND.getCode());
    }

    @Test
    public void testTranslationNoVersion() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", "noexist", null);
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
        assertTrue(response.getErrors().size() > 0);
        assertTrue(response.getErrors().get(0).getCode() == TSAMError.ERROR_CODE_SYSTEM_VERSION_NOTFOUND.getCode());
    }

    @Test
    public void testTranslationNoConcept() {
        CodedElement ce = new CodedElement("noexist", "SNOMED CT", "2.16.840.1.113883.6.96", null, null);
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
        assertTrue(response.getErrors().size() > 0);
        assertTrue(response.getErrors().get(0).getCode() == TSAMError.ERROR_CODE_SYSTEM_CONCEPT_NOTFOUND.getCode());

    }

    @Test
    public void testTranslationNoDesignation() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", null, null);
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "noexist");
        assertTrue(response.getErrors().size() > 0);
        assertTrue(response.getErrors().get(0).getCode() == TSAMError.ERROR_DESIGNATION_NOTFOUND.getCode());
    }

    @Test
    public void testTranslationNoValueSet() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", null, "noexist");
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
        assertTrue(response.getWarnings().size() > 0);
        ITMTSAMEror warn = findWarning(response, TSAMError.WARNING_VS_DOESNT_MATCH);
        assertTrue(warn != null);

    }

    @Test
    public void testTranslationNoCurrentDesignation() {
        CodedElement ce = new CodedElement("271650010", "SNOMED CT", "2.16.840.1.113883.6.96", null, null);
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
        assertTrue(response.getErrors().size() > 0);
        ITMTSAMEror error = findErrors(response, TSAMError.ERROR_NO_CURRENT_DESIGNATIONS);
        assertTrue(error != null);

    }

    @Test
    public void testTranslationMoreDesignationNoCurrent() {
        CodedElement ce = new CodedElement("271650011", "SNOMED CT", "2.16.840.1.113883.6.96", null, null);
        TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
        assertTrue(response.getWarnings().size() > 0);
        ITMTSAMEror warn = findWarning(response, TSAMError.WARNING_MANY_DESIGNATIONS);
        assertTrue(warn != null);

    }

    private ITMTSAMEror findWarning(TSAMResponseStructure response, TSAMError w) {
        ITMTSAMEror vsNoMatch = null;
        for (ITMTSAMEror warning : response.getWarnings()) {
            if (warning.getCode() == w.getCode()) {
                vsNoMatch = warning;
            }
        }
        return vsNoMatch;
    }

    private ITMTSAMEror findErrors(TSAMResponseStructure response, TSAMError w) {
        ITMTSAMEror vsNoMatch = null;
        for (ITMTSAMEror warning : response.getErrors()) {
            if (warning.getCode() == w.getCode()) {
                vsNoMatch = warning;
            }
        }
        return vsNoMatch;
    }

    @Test
    public void testTranscodingWithVersion() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", "January 2010", null);
        TSAMResponseStructure response = service.getEpSOSConceptByCode(ce);
        assertNotNull(response.getDesignation());
        assertEquals(response.getDesignation(), "INTRAVASCULAR DIASTOLIC");
        assertNotNull(response.getCode());
        assertEquals(response.getCode(), "8462-4");
        assertNotNull(response.getCodeSystemVersion());
        assertEquals(response.getCodeSystemVersion(), "June 2009");
        assertNotNull(response.getCodeSystem());
        assertEquals(response.getCodeSystem(), "2.16.840.1.113883.6.1");
    }

    @Test
    public void testTranscodingWithPrevVersion() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", "July 2010", null);
        TSAMResponseStructure response = service.getEpSOSConceptByCode(ce);
        assertNotNull(response.getDesignation());
        assertEquals(response.getDesignation(), "INTRAVASCULAR DIASTOLIC");
        assertNotNull(response.getCode());
        assertEquals(response.getCode(), "8462-4");
        assertNotNull(response.getCodeSystemVersion());
        assertEquals(response.getCodeSystemVersion(), "June 2009");
        assertNotNull(response.getCodeSystem());
        assertEquals(response.getCodeSystem(), "2.16.840.1.113883.6.1");
    }

    @Test
    public void testTranscodingCurrent() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", null, null);
        TSAMResponseStructure response = service.getEpSOSConceptByCode(ce);
        assertNotNull(response.getDesignation());
        assertEquals(response.getDesignation(), "INTRAVASCULAR DIASTOLIC");
        assertNotNull(response.getCode());
        assertEquals(response.getCode(), "8462-4");
        assertNotNull(response.getCodeSystemVersion());
        assertEquals(response.getCodeSystemVersion(), "June 2009");
        assertNotNull(response.getCodeSystem());
        assertEquals(response.getCodeSystem(), "2.16.840.1.113883.6.1");
    }

    @Test
    public void testTranscodingNoCodeSystem() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "noexist", null, null);
        TSAMResponseStructure response = service.getEpSOSConceptByCode(ce);
        assertTrue(response.getErrors().size() > 0);
        assertTrue(response.getErrors().get(0).getCode() == TSAMError.ERROR_CODE_SYSTEM_NOTFOUND.getCode());
    }

    @Test
    public void testTranscodingNoVersion() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", "noexist", null);
        TSAMResponseStructure response = service.getEpSOSConceptByCode(ce);
        assertTrue(response.getErrors().size() > 0);
        assertTrue(response.getErrors().get(0).getCode() == TSAMError.ERROR_CODE_SYSTEM_VERSION_NOTFOUND.getCode());
    }

    @Test
    public void testTranscodingNoConcept() {
        CodedElement ce = new CodedElement("noexist", "SNOMED CT", "2.16.840.1.113883.6.96", null, null);
        TSAMResponseStructure response = service.getEpSOSConceptByCode(ce);
        assertTrue(response.getErrors().size() > 0);
        assertTrue(response.getErrors().get(0).getCode() == TSAMError.ERROR_CODE_SYSTEM_CONCEPT_NOTFOUND.getCode());
    }

    @Test
    public void testTranscodingNoTargedConcept() {
        CodedElement ce = new CodedElement("271650007", "SNOMED CT", "2.16.840.1.113883.6.96", null, null);
        TSAMResponseStructure response = service.getEpSOSConceptByCode(ce);
        assertTrue(response.getErrors().size() > 0);
        assertTrue(response.getErrors().get(0).getCode() == TSAMError.ERROR_TARGET_CONCEPT_NOTFOUND.getCode());
    }

    @Test
    public void testTranscodingNoTargedDesignation() {
        CodedElement ce = new CodedElement("419199007", "SNOMED CT", "2.16.840.1.113883.6.96", null, null);
        TSAMResponseStructure response = service.getEpSOSConceptByCode(ce);
        assertTrue(response.getErrors().size() > 0);
        assertTrue(response.getErrors().get(0).getCode() == TSAMError.ERROR_DESIGNATION_NOTFOUND.getCode());
    }

    @Test
    public void testTranscodingNoTargedConceptUsingSource() {
        CodedElement ce = new CodedElement("271650008", "SNOMED CT", "2.16.840.1.113883.6.96", null, null);
        TSAMResponseStructure response = service.getEpSOSConceptByCode(ce);
        assertTrue(response.getErrors().size() == 0);
        assertEquals(response.getDesignation(), "Diastolic blood pressure");
    }

    @Test
    public void testTranscodingNoValueSet() {
        CodedElement ce = new CodedElement("271650006", "SNOMED CT", "2.16.840.1.113883.6.96", null, "noexist");
        TSAMResponseStructure response = service.getEpSOSConceptByCode(ce);
        assertTrue(response.getWarnings().size() > 0);
        ITMTSAMEror vsNoMatch = findWarning(response, TSAMError.WARNING_VS_DOESNT_MATCH);
        assertTrue(vsNoMatch != null);
    }

    @Test
    public void testGetValueSetConcepts() {
        List<RetrievedConcept> concepts = service.getValueSetConcepts("1.3.6.1.4.1.12559.11.10.1.3.1.42.2", "MVC 1.0", "en");
        for (RetrievedConcept retrievedConcept : concepts) {
            System.out.println(retrievedConcept.getCode());
        }
        //      assertEquals(concepts.size(), 5);
    }

    @Test
    public void testGetValueSetConceptsCurrentVersion() {
        List<RetrievedConcept> concepts = service.getValueSetConcepts("1.3.6.1.4.1.12559.11.10.1.3.1.42.2", null, "en");
        System.out.println(concepts.size());
        CodedElement ce;
        long start = System.currentTimeMillis();
        for (RetrievedConcept concept : concepts) {
            //          System.out.println(concept.getCode());

            String csName = "EDQM";
            String csOid = "1.3.6.1.4.1.12559.11.10.1.3.1.44.1";
            ce = new CodedElement(concept.getCode(), csName, csOid, null, null);
            TSAMResponseStructure response = service.getDesignationByEpSOSConcept(ce, "sk-SK");
            String designation = response.getDesignation();
            if (designation != null) {
                designation = designation.trim();
            }
            //          System.out.println("d: "+designation);
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("time: " + time);
        //      assertEquals(concepts.size(),2);
    }
}
