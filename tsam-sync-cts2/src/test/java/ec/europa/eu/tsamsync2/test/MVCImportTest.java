/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.test;

import ec.europa.eu.tsamsync2.services.AdministrationServices;
import ec.europa.eu.tsamsync2.services.AuthoringServices;
import ec.europa.eu.tsamsync2.services.AuthorizationServices;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.junit.Test;

/**
 *
 * @author bernamp
 */
public class MVCImportTest {

    public MVCImportTest() {
    }

//    @Test
//    public void read() {
//        new Main();
//    }
    @Test
    public void importCodeSystem() throws IOException, MalformedURLException, NoSuchAlgorithmException {
        final String name = "TEST " + new Date();
        final String token = AuthorizationServices.get().login("berna", "qwertyui");

        Files.walkFileTree(Paths.get("C:\\Workspaces\\TSAMSync2\\src\\main\\resources\\Code Systems"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                final types.termserver.fhdo.de.CodeSystem cs = new types.termserver.fhdo.de.CodeSystem();
                cs.setName(file.getFileName().toString());
                types.termserver.fhdo.de.CodeSystemVersion e = new types.termserver.fhdo.de.CodeSystemVersion();
                e.setName("1111" + new Date());
                cs.setDescription("sddsadasds");
                cs.getCodeSystemVersions().add(e);
                types.termserver.fhdo.de.CodeSystem response = AuthoringServices.get().createCodeSystem(cs, token);
                cs.getCodeSystemVersions().clear();
                e = new types.termserver.fhdo.de.CodeSystemVersion();
                e.setName("2222" + new Date());
                cs.setDescription("sddsadasds");
                cs.getCodeSystemVersions().add(e);
                cs.setId(response.getId());
                AdministrationServices.get().importCodeSystem(cs, Files.readAllBytes(file), token, file.getFileName().toString().toLowerCase());
                return FileVisitResult.CONTINUE;
            }
        });
        AuthorizationServices.get().logout(token);
    }
}
