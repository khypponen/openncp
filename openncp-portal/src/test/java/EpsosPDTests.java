
import com.gnomon.epsos.model.Country;
import com.gnomon.epsos.model.Identifier;
import com.gnomon.epsos.service.Demographics;
import com.gnomon.epsos.service.EpsosHelperService;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author karkaletsis
 */
public class EpsosPDTests {

    private final static String path = "/home/karkaletsis/dev/servers/6.2/liferay-portal-6.2.0-ce-ga1/tomcat-7.0.42/webapps/epsosportal/";
    private static final Logger log = Logger.getLogger(EpsosPDTests.class.getName());

    public EpsosPDTests() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getCountriesList() {
        List<Country> countries = EpsosHelperService.getCountriesFromCS("el_GR", path);
        for (Country country : countries) {
            log.info("Country: " + country.getName());
        }
        assertTrue("Countries exist: ", countries.size() > 0);
    }

    @Test
    public void ListGreekIdentifiers() {
        List<Identifier> identifiers = EpsosHelperService.getCountryIdentifiers("GR", "el_GR", path, null);
        for (Identifier identifier : identifiers) {
            System.out.println(identifier.getDomain() + " " + identifier.getKey());
        }
        assertEquals(1, identifiers.size());
    }

    @Test
    public void ListGreekDemographics() {
        List<Demographics> demographics = EpsosHelperService.getCountryDemographics("GR", "el_GR", path, null);
        for (Demographics demographic : demographics) {
            System.out.println(demographic.getKey());
        }
        assertEquals(0, demographics.size());
    }

    @Test
    public void PD() {
        assertTrue(true);
    }

    @Test
    public void DQ() {
        assertTrue(true);
    }

    @Test
    public void DR() {
        assertTrue(true);
    }

}
