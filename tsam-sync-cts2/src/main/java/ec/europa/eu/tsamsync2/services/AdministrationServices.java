package ec.europa.eu.tsamsync2.services;

import de.fhdo.terminologie.ws.administration.Administration;
import de.fhdo.terminologie.ws.administration.Administration_Service;
import de.fhdo.terminologie.ws.administration.ImportCodeSystemRequestType;
import de.fhdo.terminologie.ws.administration.ImportCodeSystemResponse;
import de.fhdo.terminologie.ws.administration.ImportType;
import de.fhdo.terminologie.ws.administration.ImportValueSetRequestType;
import de.fhdo.terminologie.ws.administration.ImportValueSetResponse;
import ec.europa.eu.tsamsync2.MVC.MVCReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import types.termserver.fhdo.de.CodeSystem;
import types.termserver.fhdo.de.ValueSet;

public class AdministrationServices {

    Administration_Service service;
    private Administration port;
    public static AdministrationServices instance;

    public AdministrationServices() {
        try {
            service = new Administration_Service(new URL(MVCReader.url + "Administration?wsdl"), new QName("http://administration.ws.terminologie.fhdo.de/", "Administration"));
            port = service.getAdministrationPort();
        } catch (MalformedURLException ex) {
            Logger.getLogger(AdministrationServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static AdministrationServices get() {
        if (instance == null)
            instance = new AdministrationServices();
        return instance;

    }

    public CodeSystem importCodeSystem(CodeSystem cs, byte[] file, String token, String filename) throws IOException {
        ImportCodeSystemRequestType params = new ImportCodeSystemRequestType();
        ImportType type = new ImportType();
        if (filename.endsWith(".csv"))
            type.setFormatId(1L);
        else
            if (filename.endsWith(".xml"))
                type.setFormatId(2L);

        type.setFilecontent(file);
        params.setImportInfos(type);
        params.setLoginToken(token);
        params.setCodeSystem(cs);
        ImportCodeSystemResponse.Return response = port.importCodeSystem(params);
        Utils.responseMessage(response.getReturnInfos(), filename);
        return response.getCodeSystem();
    }
    

    public ValueSet importValueSet(ValueSet vs, byte[] content, String token, String filename) {
        ImportValueSetRequestType params = new ImportValueSetRequestType();
        params.setLoginToken(token);
        params.setValueSet(vs);
        ImportType type = new ImportType();
        type.setFormatId(2L);
        type.setFilecontent(content);
        params.setImportInfos(type);
        ImportValueSetResponse.Return response = port.importValueSet(params);
        Utils.responseMessage(response.getReturnInfos(), filename);
        return response.getValueSet();
    }
}
