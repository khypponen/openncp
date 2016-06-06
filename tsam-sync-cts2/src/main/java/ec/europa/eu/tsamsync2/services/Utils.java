/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.services;

import de.fhdo.terminologie.ws.authoring.ReturnType;
import de.fhdo.terminologie.ws.authoring.Status;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bernamp
 */
public class Utils {

    public static void responseMessage(ReturnType r) {
        if (r.getStatus() == Status.OK)
            Logger.getLogger(AuthoringServices.class.getName()).log(Level.INFO, "Count: {0}", r.getCount());
        else
            Logger.getLogger(AuthoringServices.class.getName()).log(Level.WARNING, "Response: {0}", r.getMessage());
    }

    public static void responseMessage(ec.europa.eu.tsamsync2.search.ReturnType r) {
        if (r.getStatus() == ec.europa.eu.tsamsync2.search.Status.OK)
            Logger.getLogger(AuthoringServices.class.getName()).log(Level.INFO, "Count: {0}", r.getCount());
        else
            Logger.getLogger(AuthoringServices.class.getName()).log(Level.WARNING, "Response: {0}", r.getMessage());
    }

    public static void responseMessage(de.fhdo.terminologie.ws.authorization.ReturnType r) {
        if (r.getStatus() == de.fhdo.terminologie.ws.authorization.Status.OK)
            Logger.getLogger(AuthoringServices.class.getName()).log(Level.INFO, "Count: {0}", r.getCount());
        else
            Logger.getLogger(AuthoringServices.class.getName()).log(Level.WARNING, "Response: {0}", r.getMessage());
    }

    static void responseMessage(de.fhdo.terminologie.ws.administration.ReturnType r, String name) {
        if (r.getStatus() == de.fhdo.terminologie.ws.administration.Status.OK)
            Logger.getLogger(AuthoringServices.class.getName()).log(Level.INFO, "Count: {0} for file {1}", new Object[]{r.getCount(), name});
        else
            Logger.getLogger(AuthoringServices.class.getName()).log(Level.WARNING, "Response: {0}for file {1} ", new Object[]{r.getCount(), name});
    }
}
