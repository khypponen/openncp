
package epsos.ccd.carecom.tsam.synchronizer.webservice.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "CommonTerminologyService2.0", targetNamespace = "http://cts2.webservice.ht.carecom.dk/", wsdlLocation = "http://localhost:8080/webservice/commonTerminologyService2.0?wsdl")
public class CommonTerminologyService20_Service
    extends Service
{

    private final static URL COMMONTERMINOLOGYSERVICE20_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(epsos.ccd.carecom.tsam.synchronizer.webservice.client.CommonTerminologyService20_Service.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = epsos.ccd.carecom.tsam.synchronizer.webservice.client.CommonTerminologyService20_Service.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:8080/webservice/commonTerminologyService2.0?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:8080/webservice/commonTerminologyService2.0?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        COMMONTERMINOLOGYSERVICE20_WSDL_LOCATION = url;
    }

    public CommonTerminologyService20_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CommonTerminologyService20_Service() {
        super(COMMONTERMINOLOGYSERVICE20_WSDL_LOCATION, new QName("http://cts2.webservice.ht.carecom.dk/", "CommonTerminologyService2.0"));
    }

    /**
     * 
     * @return
     *     returns CommonTerminologyService20
     */
    @WebEndpoint(name = "CommonTerminologyService2.0Port")
    public CommonTerminologyService20 getCommonTerminologyService20Port() {
        return super.getPort(new QName("http://cts2.webservice.ht.carecom.dk/", "CommonTerminologyService2.0Port"), CommonTerminologyService20.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CommonTerminologyService20
     */
    @WebEndpoint(name = "CommonTerminologyService2.0Port")
    public CommonTerminologyService20 getCommonTerminologyService20Port(WebServiceFeature... features) {
        return super.getPort(new QName("http://cts2.webservice.ht.carecom.dk/", "CommonTerminologyService2.0Port"), CommonTerminologyService20.class, features);
    }

}
