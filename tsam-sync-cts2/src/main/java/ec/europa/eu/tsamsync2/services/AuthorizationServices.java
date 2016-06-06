package ec.europa.eu.tsamsync2.services;

import de.fhdo.terminologie.ws.authorization.Authorization;
import de.fhdo.terminologie.ws.authorization.Authorization_Service;
import de.fhdo.terminologie.ws.authorization.LoginResponse;
import de.fhdo.terminologie.ws.authorization.LogoutResponseType;
import ec.europa.eu.tsamsync2.MVC.MVCReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.xml.namespace.QName;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;

public class AuthorizationServices {

    Authorization_Service service;
    Authorization port;
    private static AuthorizationServices instance;

    public AuthorizationServices() {
        try {
            service = new Authorization_Service(new URL(MVCReader.url + "Authorization?wsdl"), new QName("http://authorization.ws.terminologie.fhdo.de/", "Authorization"));
            port = service.getAuthorizationPort();
        } catch (MalformedURLException ex) {
            Logger.getLogger(AuthorizationServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static AuthorizationServices get() {
        if (instance == null)
            instance = new AuthorizationServices();
        return instance;

    }

    public String login(String usr, String pwd) throws MalformedURLException, NoSuchAlgorithmException {
        // create webservice reference and port

        // define parameter
        List<String> parameter = new ArrayList<>();
        parameter.add(usr);
        parameter.add(MD5(pwd));

        // invoke method
        LoginResponse.Return response = port.login(parameter);

        Utils.responseMessage(response.getReturnInfos());
        try {
            return response.getParameterList().get(0);
        } catch (Exception ex) {
            return null;
        }
    }

    public void logout(String sessionID) throws MalformedURLException, NoSuchAlgorithmException {
        // define parameter
        List<String> parameter = new ArrayList<>();
        if (sessionID != null)
            parameter.add(sessionID);

        LogoutResponseType response = port.logout(parameter);
        Logger.getLogger(AuthorizationServices.class.getName()).log(Level.WARNING, "Response: {0}", response.getReturnInfos().getMessage());

    }

    private String MD5(String a) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(a.getBytes(Charset.forName("UTF8")));
        final byte[] resultByte = messageDigest.digest();
        return new String(Hex.encodeHex(resultByte));
    }

}
