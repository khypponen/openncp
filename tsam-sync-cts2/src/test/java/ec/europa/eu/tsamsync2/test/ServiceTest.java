package ec.europa.eu.tsamsync2.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import ec.europa.eu.tsamsync2.search.CodeSystem;
import ec.europa.eu.tsamsync2.search.CodeSystemEntity;
import ec.europa.eu.tsamsync2.search.CodeSystemVersion;
import ec.europa.eu.tsamsync2.search.ValueSet;
import ec.europa.eu.tsamsync2.search.ValueSetVersion;
import ec.europa.eu.tsamsync2.services.AuthorizationServices;
import ec.europa.eu.tsamsync2.services.SearchServices;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author bernamp
 */
public class ServiceTest {


    public ServiceTest() {
    }
 
    @Test
    public void testLogin() {
        try {
           Logger.getLogger(ServiceTest.class.getName()).log(Level.INFO, AuthorizationServices.get().login("berna", "qwertyui"));
        } catch (MalformedURLException | NoSuchAlgorithmException ex) {
            Logger.getLogger(ServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testListCodeSystem() {

        for (CodeSystem cs : SearchServices.get().listCodeSystems())
            for (CodeSystemVersion csv : cs.getCodeSystemVersions())
                 Logger.getLogger(ServiceTest.class.getName()).log(Level.INFO, "VersionId: {0}, {1} - {2}", new Object[]{csv.getVersionId(), cs.getName(), csv.getName()});
    }

    @Test
    public void testListValueSet()  {

        for (ValueSet cs : SearchServices.get().listValueSets())
            for (ValueSetVersion csv : cs.getValueSetVersions())
                  Logger.getLogger(ServiceTest.class.getName()).log(Level.INFO, "VersionId: {0}, {1} - {2}", new Object[]{csv.getVersionId(), cs.getName(), csv.getName()});
   }

    @Test
    public void testListCodeSystemConcepts() throws MalformedURLException, NoSuchAlgorithmException{

        
        for (CodeSystemEntity cs : SearchServices.get().listCodeSystemConcepts(AuthorizationServices.get().login("berna", "qwertyui"), SearchServices.get().listCodeSystems().get(0)))
                   Logger.getLogger(ServiceTest.class.getName()).log(Level.INFO, "VersionId: {0}, {1} - {2}", new Object[]{cs.getCodeSystemEntityVersions(), cs.getId(), cs.getCurrentVersionId()});
    }

    @Test
    public void testListValueSetContents() throws MalformedURLException, NoSuchAlgorithmException {

        for (CodeSystemEntity cs : SearchServices.get().listValueSetContents(AuthorizationServices.get().login("berna", "qwertyui"), SearchServices.get().listValueSets().get(0)))
                    Logger.getLogger(ServiceTest.class.getName()).log(Level.INFO, "VersionId: {0}, {1} - {2}", new Object[]{cs.getCodeSystemEntityVersions().get(0), cs.getId(), cs.getCurrentVersionId()});
    }

}
