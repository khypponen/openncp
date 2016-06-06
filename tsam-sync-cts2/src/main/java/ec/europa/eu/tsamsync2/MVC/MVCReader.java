/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.MVC;

import ec.europa.eu.tsamsync2.MVC.enums.FILE;
import ec.europa.eu.tsamsync2.MVC.enums.OUTPUT_TYPE;
import ec.europa.eu.tsamsync2.MVC.enums.SHEET;
import ec.europa.eu.tsamsync2.search.CodeSystem;
import ec.europa.eu.tsamsync2.services.AuthorizationServices;
import ec.europa.eu.tsamsync2.services.SearchServices;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

/**
 *
 * @author bernamp
 */
public class MVCReader {

    private final String filename;
    List<String[]> snomed_cs = new ArrayList<>();
    List<String[]> edqm_cs = new ArrayList<>();
    List<String[]> snomed_vs = new ArrayList<>();
    List<String[]> edqm_vs = new ArrayList<>();
    private final String version;
    OutputManagment out;
    public static String url = "http://localhost:8080/TermServer/";

    public MVCReader(String filename, String version, OUTPUT_TYPE o, String url, String token) {
        this.filename = filename;
        this.version = version;
        this.out = new OutputManagment(o, token);
    }

    public static void main(String[] args) {
        try {
            String usr = "test";
            String pwd = "test";
            String token = AuthorizationServices.get().login(usr, pwd);
            String file = "C:\\Users\\bernamp\\Documents\\epSOS MVC 1_8.xls";
            String version = "1.81";
            OUTPUT_TYPE type = OUTPUT_TYPE.TS;
            MVCReader m = new MVCReader(file, version, type, url, token);
            if (type == OUTPUT_TYPE.TS)
                m.importDefaultCodeSystems();
            HSSFWorkbook wb = m.readExcel();
            for (int i = 0; i < wb.getNumberOfSheets(); i++)
                m.processSheet(wb.getSheetAt(i));
            m.addSNOMED_CT();
            m.addEDQM();
            AuthorizationServices.get().logout(token);
        } catch (IOException | NoSuchAlgorithmException ex) {
            Logger.getLogger(MVCReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HSSFWorkbook readExcel() throws IOException {
        out.cleanup(version);
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filename));
        return new HSSFWorkbook(fs);
    }

    void processSheet(HSSFSheet sheet) throws IOException {
        Logger.getLogger(MVCReader.class.getName()).log(Level.INFO, "Processing {0}", sheet.getSheetName());
        Long codeSystemVersion = addCodeSystem(sheet);
        addValueset(sheet, codeSystemVersion);
    }

    void addSNOMED_CT() throws IOException {
        out.write(FILE.CODE_SYSTEM, snomed_cs, "SNOMED_CT", version);
        out.write(FILE.VALUE_SET, snomed_vs, "SNOMED_CT", version);
    }

    void addEDQM() throws IOException {
        out.write(FILE.CODE_SYSTEM, edqm_cs, "EDQM", version);
        out.write(FILE.VALUE_SET, edqm_vs, "EDQM", version);
    }

    private List<String[]> readData(HSSFSheet sheet, FILE type, String code_system_version_id) {
        List<String[]> current_import = new ArrayList<>();
        for (int i = 14; i < sheet.getLastRowNum() + 1; i++) {
            HSSFRow row = sheet.getRow(i);
            if (type == FILE.CODE_SYSTEM)
                current_import.add(new String[]{convertCellToString(row.getCell(1)).trim(), convertCellToString(row.getCell(2)).trim()});
            else
                current_import.add(new String[]{convertCellToString(row.getCell(1)).trim(), code_system_version_id});
        }
        return current_import;
    }

    private String convertCellToString(HSSFCell c) {
        if (c == null)
            return "";
        switch (c.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return c.getBooleanCellValue() + "";
            case Cell.CELL_TYPE_ERROR:
                return "";
            case Cell.CELL_TYPE_FORMULA:
                return "";
            case Cell.CELL_TYPE_NUMERIC:
                return Long.toString((long) c.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                return c.getStringCellValue();
            default:
                return "";
        }
    }

    private Long addCodeSystem(HSSFSheet sheet) throws IOException {
        if (SHEET.isSheetInList(sheet.getSheetName()) && SHEET.valueOf(sheet.getSheetName()).isCodeSystem()) {
            List<String[]> data = readData(sheet, FILE.CODE_SYSTEM, "");
            String parent_code_system = sheet.getRow(0).getCell(1).getStringCellValue();
            switch (parent_code_system) {
                case "SNOMED CT":
                    snomed_cs.addAll(data);
                    break;
                case "EDQM":
                    edqm_cs.addAll(data);
                    break;
                default:
                    return out.write(FILE.CODE_SYSTEM, data, sheet.getSheetName(), version);

            }
            Logger.getLogger(MVCReader.class
                    .getName()).log(Level.INFO, parent_code_system);
        }
        return 0L;
    }

    private Long addValueset(HSSFSheet sheet, Long code_system_version_id) throws IOException {
        if (SHEET.isSheetInList(sheet.getSheetName())) {
            String cs_name = sheet.getRow(0).getCell(1).getStringCellValue();
            if (code_system_version_id == 0)
                for (CodeSystem cs : SearchServices.get().listCodeSystems())
                    if (cs.getName().equals(cs_name))
                        code_system_version_id = cs.getCurrentVersionId();
            List<String[]> data = readData(sheet, FILE.VALUE_SET, code_system_version_id + "");
            switch (sheet.getRow(0).getCell(1).getStringCellValue()) {
                case "SNOMED CT":
                    snomed_vs.addAll(data);
                    break;
                case "EDQM":
                    edqm_vs.addAll(data);
                    break;
                default:
                    return out.write(FILE.VALUE_SET, data, sheet.getSheetName(), version);
            }
        }
        return 0L;
    }

    void importDefaultCodeSystems() throws IOException {
        out.importDefaultCodeSystems();
    }

}
