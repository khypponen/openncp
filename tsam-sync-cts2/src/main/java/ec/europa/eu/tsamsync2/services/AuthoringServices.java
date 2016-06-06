package ec.europa.eu.tsamsync2.services;

import de.fhdo.terminologie.ws.authoring.Authoring;
import de.fhdo.terminologie.ws.authoring.Authoring_Service;
import de.fhdo.terminologie.ws.authoring.CreateCodeSystemRequestType;
import de.fhdo.terminologie.ws.authoring.CreateCodeSystemResponse;
import de.fhdo.terminologie.ws.authoring.CreateValueSetRequestType;
import de.fhdo.terminologie.ws.authoring.CreateValueSetResponse;
import ec.europa.eu.tsamsync2.MVC.MVCReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName; 
import types.termserver.fhdo.de.CodeSystem;
import types.termserver.fhdo.de.ValueSet;

public class AuthoringServices {

    Authoring_Service service;
    private Authoring port;
    private static AuthoringServices instance;

    public AuthoringServices() {
        try {
            service = new Authoring_Service(new URL(MVCReader.url + "Authoring?wsdl"), new QName("http://authoring.ws.terminologie.fhdo.de/", "Authoring"));
            port = service.getAuthoringPort();
        } catch (MalformedURLException ex) {
            Logger.getLogger(AuthoringServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static AuthoringServices get() {
        if (instance == null)
            instance = new AuthoringServices();
        return instance;

    }

    public CodeSystem createCodeSystem(CodeSystem cs, String token) {
        CreateCodeSystemRequestType params = new CreateCodeSystemRequestType();
        params.setCodeSystem(cs);
        params.setLoginToken(token);
        CreateCodeSystemResponse.Return response = port.createCodeSystem(params);
        Utils.responseMessage(response.getReturnInfos());
        return response.getCodeSystem();
    }

    public ValueSet createValueSet(ValueSet vs, String token) {
        CreateValueSetRequestType params = new CreateValueSetRequestType();
        params.setLoginToken(token);
        params.setValueSet(vs);
        CreateValueSetResponse.Return response = port.createValueSet(params);
        Utils.responseMessage(response.getReturnInfos());
        return response.getValueSet();
    }
}
