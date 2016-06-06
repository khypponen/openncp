/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.MVC;

import ec.europa.eu.tsamsync2.MVC.enums.OUTPUT_TYPE;
import ec.europa.eu.tsamsync2.MVC.enums.FILE;
import ec.europa.eu.tsamsync2.services.AdministrationServices;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import types.termserver.fhdo.de.CodeSystem;
import types.termserver.fhdo.de.CodeSystemVersion;
import types.termserver.fhdo.de.ValueSet;
import types.termserver.fhdo.de.ValueSetVersion;

/**
 *
 * @author bernamp
 */
public class OutputManagment {

    OUTPUT_TYPE o;
    private final String token;

    public OutputManagment(OUTPUT_TYPE ot, String token) {
        this.o = ot;
        this.token = token;
    }

    void cleanup(String version) throws IOException {
        switch (o) {
            case CSV:
                removeDir(FILE.CODE_SYSTEM.name() + version);
                Files.createDirectory(Paths.get(FILE.CODE_SYSTEM.name() + version));
                removeDir(FILE.VALUE_SET.name() + version);
                Files.createDirectory(Paths.get(FILE.VALUE_SET.name() + version));
                break;
            case TS:
                //TODO cleanup services??
                System.out.println("todo");
        }
    }

    void removeDir(String dir) throws IOException {
        switch (o) {
            case CSV:
                if (Files.exists(Paths.get(dir)))
                    Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                break;
            case TS:
                //TODO cleanup services??
                System.out.println("todo");
        }
    }

    void importDefaultCodeSystems() throws IOException {
        Files.walkFileTree(Paths.get("C:\\Workspaces\\TSAMSync2\\src\\main\\resources\\Code Systems"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                importCodeSystem(file.getFileName().toString(), Files.readAllBytes(file), "1");
                return FileVisitResult.CONTINUE;
            }
        });
    }

    Long write(FILE type, List<String[]> out, String filename, String version) throws IOException {
        CSVPrinter printer;
        CSVFormat f = null;
        switch (type) {
            case CODE_SYSTEM:
                f = CSVFormat.DEFAULT.withAllowMissingColumnNames().withDelimiter(';').withRecordSeparator("\n").withHeader("code", "term");
                break;
            case VALUE_SET:
                f = CSVFormat.DEFAULT.withAllowMissingColumnNames().withDelimiter(';').withRecordSeparator("\n").withHeader("code", "codesystem_version_id");
                break;
        }
        final ByteArrayOutputStream outB = new ByteArrayOutputStream();
        switch (o) {
            case CSV:
                printer = new CSVPrinter(new PrintWriter(Paths.get(type.name() + version, filename + ".csv").toString()), f);
                printer.printRecords(out);
                printer.flush();
                printer.close();
                return 0L;
            case TS:
                printer = new CSVPrinter(new BufferedWriter(new OutputStreamWriter(outB)), f);
                printer.printRecords(out);
                printer.flush();
                printer.close();
                return importCodeSystem(filename, outB.toByteArray(), version);
            default:
                return 0L;
        }

    }

    private Long importCodeSystem(String filename, byte[] content, String version) throws IOException {
        CodeSystem temp = new CodeSystem();
        temp.setName(filename);
//        cs.setDescription("sddsadasds");
        CodeSystemVersion e = new CodeSystemVersion();
        e.setName(version);
//        cs.setDescription("dgxhfxdcznhzdf n");
        temp.getCodeSystemVersions().add(e);
//        AuthoringServices.get().createCodeSystem(temp, token);
        CodeSystem cs = AdministrationServices.get().importCodeSystem(temp, content, token, filename);
        if (cs != null) {
            cs.setName(temp.getName());
            cs.setId(temp.getId());
            cs.getCodeSystemVersions().remove(e);
            cs.getCodeSystemVersions().add(e);
            return temp.getCurrentVersionId();
        } else
            return 0L;
    }

    private Long importValueSet(String filename, byte[] content, String version) {
        ValueSet vs = new ValueSet();
        vs.setName(filename);
        ValueSetVersion e = new ValueSetVersion();
        e.setName(version);
        vs.getValueSetVersions().add(e);
        return AdministrationServices.get().importValueSet(vs, content, token, filename).getCurrentVersionId();
    }

}
