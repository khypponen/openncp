/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnomon.epsos.util;

import com.gnomon.epsos.model.DictionaryTerm;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author kostaskarkaletsis
 */
public class DictionaryServices {

    // define path of epsos configuration
    private static final String HOME_PATH = System.getenv("EPSOS_PROPS_PATH");
    private static String EPSOS_REPOSITORY_PATH = HOME_PATH + "EpsosRepository";
    private static Map<String, Map<String, String>> problemsDictionary;
    private static Map<String, Map<String, String>> proceduresDictionary;
    private static Map<String, Map<String, String>> allergiesDictionary;
    private static Map<String, Map<String, String>> familyRolesDictionary;

    private static Map<String, List<DictionaryTerm>> problemsDictionaryTerms;
    private static Map<String, List<DictionaryTerm>> allergiesDictionaryTerms;
    private static Map<String, List<DictionaryTerm>> familyRolesDictionaryTerms;
    private static Map<String, List<DictionaryTerm>> proceduresDictionaryTerms;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("DictionaryServices");

    static {

        allergiesDictionary = getDictionaryForNamespace("SNOMEDCT.xml", "SNOMEDCTEntry", "epSOSAllergenNoDrugs");
        proceduresDictionary = getDictionaryForNamespace("SNOMEDCT.xml", "SNOMEDCTEntry", "epSOSProcedures");
        problemsDictionary = getDictionaryForNamespace("ICD-10.xml", "ICD-10Entry", "epSOSIllnessesandDisorders");
        familyRolesDictionary = getDictionaryForNamespace("RoleCode.xml", "RoleCodeEntry", "epSOSPersonalRelationship");

        allergiesDictionaryTerms = getDictionary("SNOMEDCT.xml", "SNOMEDCTEntry", "epSOSAllergenNoDrugs");
        proceduresDictionaryTerms = getDictionary("SNOMEDCT.xml", "SNOMEDCTEntry", "epSOSProcedures");
        problemsDictionaryTerms = getDictionary("ICD-10.xml", "ICD-10Entry", "epSOSIllnessesandDisorders");
        familyRolesDictionaryTerms = getDictionary("RoleCode.xml", "RoleCodeEntry", "epSOSPersonalRelationship");

    }

    public static Map<String, Map<String, String>> getProblemsDictionary() {
        return problemsDictionary;
    }

    public static void setProblemsDictionary(Map<String, Map<String, String>> problemsDictionary) {
        DictionaryServices.problemsDictionary = problemsDictionary;
    }

    public static Map<String, Map<String, String>> getProceduresDictionary() {
        return proceduresDictionary;
    }

    public static void setProceduresDictionary(Map<String, Map<String, String>> proceduresDictionary) {
        DictionaryServices.proceduresDictionary = proceduresDictionary;
    }

    public static Map<String, Map<String, String>> getAllergiesDictionary() {
        return allergiesDictionary;
    }

    public static void setAllergiesDictionary(Map<String, Map<String, String>> allergiesDictionary) {
        DictionaryServices.allergiesDictionary = allergiesDictionary;
    }

    public static Map<String, List<DictionaryTerm>> getDictionary(
            String filename,
            String entrypath,
            String namespace) {
        Map<String, List<DictionaryTerm>> snomedDictionary = new HashMap<String, List<DictionaryTerm>>();

        try {
            //Logging a short info message
            log.info("Trying to read the epsos problem codes from the file: " + EPSOS_REPOSITORY_PATH + "/SNOMEDCT.xml");

            //Reading codes from the local SNOMED CT file
            File snomedct = new File(EPSOS_REPOSITORY_PATH + "/" + filename);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(snomedct);
            String lang = "";
            //Getting all the entries
            NodeList entries = doc.getElementsByTagName(entrypath);
            log.info(entrypath + " ENTRIES SIZE : " + entries.getLength());
            //Iterating through the matched XML entries
            for (int i = 0; i < entries.getLength(); i++) {
                //Getting the next element entry
                Node entry = entries.item(i);

                //Checking if this is an EPSOS CodeProb code
                if (entry.getAttributes().getNamedItem("epsosName").getNodeValue().equals(namespace)) {
                    log.debug("####" + entry.getAttributes().getNamedItem("code").getNodeValue());
                    //Getting the code
                    String code = entry.getAttributes().getNamedItem("code").getNodeValue();

                    //Getting all the display names of the code
                    NodeList childs = entry.getChildNodes();

                    //Iterating through the diplay name list for each language
                    for (int k = 1; k < childs.getLength(); k += 2) {
                        //Getting the next display name entry
                        Node child = childs.item(k);

                        //Getting the language of the display name
                        String language = child.getAttributes().getNamedItem("lang").getNodeValue();

                        //Checking if language comes in correct form
                        if (language.equals("en")) {
                            language = "en-GB";
                        }
                        language = language.replaceAll("-", "_");

                        //Getting the display name of the code
                        String display = child.getTextContent();
                        lang = language;
                        //Checking if the language mapped already
                        if (snomedDictionary.containsKey(lang)) {
                            //Adding the next code into the corresponding map
                            DictionaryTerm term = new DictionaryTerm();
                            term.setCode(code);
                            term.setDescription(display);
                            snomedDictionary.get(lang).add(term);
                        } else {
                            //Creating an empty hash map
                            Map<String, String> map = new HashMap<String, String>();

                            //Adding the next code and display name
                            map.put(display, code);
                            //terms.s //Adding the map into the corresponding language into the dictionary
                            snomedDictionary.put(lang, new ArrayList());
                            DictionaryTerm term = new DictionaryTerm();
                            term.setCode(code);
                            term.setDescription(display);
                            snomedDictionary.get(lang).add(term);
                        }
                    }
                }
            }
        } catch (Exception exc) {
            log.error(new Date() + " ERROR: " + exc.getMessage());
        }
        return snomedDictionary;
    }

    private static Map<String, Map<String, String>> getDictionaryForNamespace(String filename, String entrypath, String namespace) {
        Map<String, Map<String, String>> snomedDictionary = new HashMap<String, Map<String, String>>();

        try {
            //Logging a short info message
            log.info("Trying to read the epsos problem codes from the file: " + EPSOS_REPOSITORY_PATH + "/SNOMEDCT.xml");

            //Reading codes from the local SNOMED CT file
            File snomedct = new File(EPSOS_REPOSITORY_PATH + "/" + filename);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(snomedct);

            //Getting all the entries
            NodeList entries = doc.getElementsByTagName(entrypath);
            log.info(entrypath + " ENTRIES SIZE : " + entries.getLength());
            //Iterating through the matched XML entries
            for (int i = 0; i < entries.getLength(); i++) {
                //Getting the next element entry
                Node entry = entries.item(i);

                //Checking if this is an EPSOS CodeProb code
                if (entry.getAttributes().getNamedItem("epsosName").getNodeValue().equals(namespace)) {
                    log.debug("####" + entry.getAttributes().getNamedItem("code").getNodeValue());
                    //Getting the code
                    String code = entry.getAttributes().getNamedItem("code").getNodeValue();

                    //Getting all the display names of the code
                    NodeList childs = entry.getChildNodes();

                    //Iterating through the diplay name list for each language
                    for (int k = 1; k < childs.getLength(); k += 2) {
                        //Getting the next display name entry
                        Node child = childs.item(k);

                        //Getting the language of the display name
                        String language = child.getAttributes().getNamedItem("lang").getNodeValue();

                        //Checking if language comes in correct form
                        if (language.equals("en")) {
                            language = "en-GB";
                        }
                        language = language.replaceAll("-", "_");
                        //Getting the display name of the code
                        String display = child.getTextContent();

                        //Checking if the language mapped already
                        if (snomedDictionary.containsKey(language)) {
                            //Adding the next code into the corresponding map
                            snomedDictionary.get(language).put(display, code);
                        } else {
                            //Creating an empty hash map
                            Map<String, String> map = new HashMap<String, String>();

                            //Adding the next code and display name
                            map.put(display, code);
                            //terms.s //Adding the map into the corresponding language into the dictionary
                            snomedDictionary.put(language, map);
                        }
                    }
                }
            }
        } catch (Exception exc) {
            //Printing an error message
            log.error(new Date() + " ERROR: " + exc.getMessage());
        }
        return snomedDictionary;
    }

    public static Map<String, Map<String, String>> getFamilyRolesDictionary() {
        return familyRolesDictionary;
    }

    public static void setFamilyRolesDictionary(Map<String, Map<String, String>> familyRolesDictionary) {
        DictionaryServices.familyRolesDictionary = familyRolesDictionary;
    }

    public static Map<String, List<DictionaryTerm>> getProblemsDictionaryTerms() {
        return problemsDictionaryTerms;
    }

    public static void setProblemsDictionaryTerms(Map<String, List<DictionaryTerm>> problemsDictionaryTerms) {
        DictionaryServices.problemsDictionaryTerms = problemsDictionaryTerms;
    }

    public static Map<String, List<DictionaryTerm>> getAllergiesDictionaryTerms() {
        return allergiesDictionaryTerms;
    }

    public static void setAllergiesDictionaryTerms(Map<String, List<DictionaryTerm>> allergiesDictionaryTerms) {
        DictionaryServices.allergiesDictionaryTerms = allergiesDictionaryTerms;
    }

    public static Map<String, List<DictionaryTerm>> getFamilyRolesDictionaryTerms() {
        return familyRolesDictionaryTerms;
    }

    public static void setFamilyRolesDictionaryTerms(Map<String, List<DictionaryTerm>> familyRolesDictionaryTerms) {
        DictionaryServices.familyRolesDictionaryTerms = familyRolesDictionaryTerms;
    }

    public static Map<String, List<DictionaryTerm>> getProceduresDictionaryTerms() {
        return proceduresDictionaryTerms;
    }

    public static void setProceduresDictionaryTerms(Map<String, List<DictionaryTerm>> proceduresDictionaryTerms) {
        DictionaryServices.proceduresDictionaryTerms = proceduresDictionaryTerms;
    }

}
