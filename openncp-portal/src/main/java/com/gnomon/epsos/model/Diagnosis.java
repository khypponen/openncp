package com.gnomon.epsos.model;

import com.gnomon.LiferayUtils;
import com.gnomon.epsos.FacesService;
import com.liferay.portal.model.User;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * A diagnosis bean representing a medical active problem of a patient.
 *
 * @author Akis Papadopoulos
 */
@ManagedBean
@RequestScoped
public class Diagnosis implements Serializable {

    //Messages logger
    private static final Logger log = LoggerFactory.getLogger("Diagnosis");
    //System home path
    private static final String HOME_PATH;
    //Epsos repository path
    private static final String EPSOS_REPOSITORY_PATH;
    //Statuses list of a diagnosis
    private static final List<String> availableStatuses;
    //Problems list of a diagnosis
    private static final Map<String, Map<String, String>> problemsDictionary;
    //Observation list of a diagnosis
    private static final Map<String, Map<String, String>> observationsDictionary;
    //Simple date format
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    private static final long serialVersionUID = -6996983017835718177L;

    static {
        //Building the epsos repository path
        HOME_PATH = System.getenv("EPSOS_PROPS_PATH");
        EPSOS_REPOSITORY_PATH = HOME_PATH + "EpsosRepository";

        //Building the list of statuses of a diagnosis
        availableStatuses = new ArrayList<String>();
        availableStatuses.add("Active");
        availableStatuses.add("Completed");

        //Building a map of diagnosis problem display names and codes for each language
        problemsDictionary = new HashMap<String, Map<String, String>>();

        try {
            //Logging a short info message
            log.info("Trying to read the epsos problem codes from the file: " + EPSOS_REPOSITORY_PATH + "/SNOMEDCT.xml");

            //Reading codes from the local SNOMED CT file
            File snomedct = new File(EPSOS_REPOSITORY_PATH + "/SNOMEDCT.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(snomedct);

            //Getting all the entries
            NodeList entries = doc.getElementsByTagName("SNOMEDCTEntry");
            log.info("SNOMEDCT ENTRIES SIZE : " + entries.getLength());
            //Iterating through the matched XML entries
            for (int i = 0; i < entries.getLength(); i++) {
                //Getting the next element entry
                Node entry = entries.item(i);

                //Checking if this is an EPSOS CodeProb code
                if (entry.getAttributes().getNamedItem("epsosName").getNodeValue().equals("epSOSCodeProb")) {
                    log.info("####" + entry.getAttributes().getNamedItem("code").getNodeValue());
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

                        //Getting the display name of the code
                        String display = child.getTextContent();

                        //Checking if the language mapped already
                        if (problemsDictionary.containsKey(language)) {
                            //Adding the next code into the corresponding map
                            problemsDictionary.get(language).put(display, code);
                        } else {
                            //Creating an empty hash map
                            Map<String, String> map = new HashMap<String, String>();

                            //Adding the next code and display name
                            map.put(display, code);

                            //Adding the map into the corresponding language into the dictionary
                            problemsDictionary.put(language, map);
                        }
                    }
                }
            }
        } catch (Exception exc) {
            //Printing an error message
            log.error(new Date() + " ERROR: " + exc.getMessage());
        }

        //Building a map of diagnosis observation display names nad codes
        observationsDictionary = new HashMap<String, Map<String, String>>();

        try {
            //Logging a short info message
            log.info("Trying to read the IHE ICD-10 codes from the file: " + EPSOS_REPOSITORY_PATH + "/ICD-10.xml");

            //Reading codes from the local ICD-10 file
            File icd10 = new File(EPSOS_REPOSITORY_PATH + "/ICD-10.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(icd10);

            //Getting all the ICD-10 entries
            NodeList entries = doc.getElementsByTagName("ICD-10Entry");

            //Iterating through the matched XML entries
            for (int i = 0; i < entries.getLength(); i++) {
                //Getting the next element entry
                Node entry = entries.item(i);

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

                    //Getting the display name of the code
                    String display = child.getTextContent();

                    //Checking if the language mapped already
                    if (observationsDictionary.containsKey(language)) {
                        //Adding the next code into the corresponding map
                        observationsDictionary.get(language).put(display, code);
                    } else {
                        //Creating an empty hash map
                        Map<String, String> map = new HashMap<String, String>();

                        //Adding the next code and display name
                        map.put(display, code);

                        //Adding the map into the corresponding language into the dictionary
                        observationsDictionary.put(language, map);
                    }
                }
            }
        } catch (Exception exc) {
            //Printing an error message
            log.error(new Date() + " ERROR: " + exc.getMessage());
        }
    }

    //Unique diagnosis key
    private String key;
    //Dislpay name of the diagnosis problem
    private String problem;
    //Display name of the diagnosis status
    private String status;
    //Onset date of the diagnosis
    private Date onset;
    //Resolution date of the diagnosis
    private Date resolution;
    //Display name of the diagnosis observations
    private List<String> observations;
    //Optional description of the diagnosis
    private String description;
    //Resolved diagnosis indicator
    private boolean resolved;
    //Language selected by the user
    private String language = "en-GB";
    //List of current session stored diagnoses
    @ManagedProperty(value = "#{diagnoses}")
    private Diagnoses diagnoses;

    /**
     * A plain constructor setting a unique diagnosis key.
     */
    public Diagnosis() {
        //Setting the unique diagnosis key
        key = UUID.randomUUID().toString();

        //Getting the logged in session user
        User user = LiferayUtils.getPortalUser();

        //Getting the user selected language
        language = user.getLanguageId().replace("_", "-");
    }

    /**
     * A method returning the diagnosis key.
     *
     * @return the unique diagnosis key.
     */
    public String getKey() {
        return key;
    }

    /**
     * A method returning the display name of the diagnosis problem.
     *
     * @return the display name of the diagnosis problem.
     */
    public String getProblem() {
        return problem;
    }

    /**
     * A method setting the display name of the diagnosis problem.
     *
     * @param problem the display name of the diagnosis problem.
     */
    public void setProblem(String problem) {
        this.problem = problem;
    }

    /**
     * A method returning the code of the diagnosis problem.
     *
     * @return the code of the diagnosis problem.
     */
    public String getProblemCode() {
        return problemsDictionary.get(language).get(problem);
    }

    /**
     * A method returning the status of the diagnosis.
     *
     * @return the status of the diagnosis.
     */
    public String getStatus() {
        return status;
    }

    /**
     * A method setting the status of the diagnosis.
     *
     * @param status the status of the diagnosis.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * A method returning the onset date of the diagnosis.
     *
     * @return the onset date of the diagnosis.
     */
    public Date getOnset() {
        return onset;
    }

    /**
     * A method setting the onset date of the diagnosis.
     *
     * @param onset the onset date of the diagnosis.
     */
    public void setOnset(Date onset) {
        this.onset = onset;
    }

    /**
     * A method returning a formatted onset date of the diagnosis.
     *
     * @return the formatted onset date of the diagnosis.
     */
    public String getFormattedOnset() {
        return formatter.format(onset);
    }

    /**
     * A method returning the resolution date of a diagnosis.
     *
     * @return the resolution date of the diagnosis.
     */
    public Date getResolution() {
        return resolution;
    }

    /**
     * A method setting the resolution date of the diagnosis.
     *
     * @param resolution the resolution date of the diagnosis.
     */
    public void setResolution(Date resolution) {
        this.resolution = resolution;
    }

    /**
     * A method returning the formatted resolution date of the diagnosis.
     *
     * @return the formatted resolution date of the diagnosis.
     */
    public String getFormattedResolution() {
        if (resolution != null) {
            return formatter.format(resolution);
        } else {
            return "";
        }
    }

    /**
     * A method returning the display name of the observations.
     *
     * @return the display name of the observations.
     */
    public List<String> getObservations() {
        return observations;
    }

    /**
     * A method setting the display name of the observations.
     *
     * @param observations the display name of the observations.
     */
    public void setObservations(List<String> observations) {
        this.observations = observations;
    }

    /**
     * A method returning the list of the selected observation entries.
     *
     * @return the list of the selected observation entries.
     */
    public List<Map.Entry<String, String>> getSelectedObservations() {
        //Creating an empty map of entries
        Map<String, String> entries = new HashMap<String, String>();

        //Iterating throught the list of stored observations
        for (String observation : observations) {
            //Getting the display name
            String display = observation;

            //Getting the codde
            String code = observationsDictionary.get(language).get(display);

            //Adding entry into the map
            entries.put(display, code);
        }

        //Creating an empty list of map entries
        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(entries.entrySet());

        return list;
    }

    /**
     * A method returning the description of the diagnosis.
     *
     * @return the description of the diagnosis.
     */
    public String getDescription() {
        return description;
    }

    /**
     * A method setting the description of the diagnosis.
     *
     * @param description the description of the diagnosis.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * A method returning if the diagnosis is resolved.
     *
     * @return if diagnosis is resolved.
     */
    public boolean isResolved() {
        return resolved;
    }

    /**
     * A method setting if the diagnosis is resolved.
     *
     * @param resolved if diagnosis is resolved.
     */
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    /**
     * A method setting the reference of the diagnoses list.
     *
     * @param diagnoses the current stored list diagnoses.
     */
    public void setDiagnoses(Diagnoses diagnoses) {
        this.diagnoses = diagnoses;
    }

    /**
     * A method returning the statuses of a diagnosis.
     *
     * @return the list of possible statuses of a diagnosis.
     */
    public List<String> getAvailableStatuses() {
        return availableStatuses;
    }

    /**
     * A method returning the list of diagnosis display name problems.
     *
     * @return the list of display names of the problems.
     */
    public List<String> getAvailableProblems() {
        //Creating an empty list
        List<String> list = new ArrayList<String>();

        //Checking if the dictionary provide such a language, otherwise return empty
        if (problemsDictionary.get(language) != null) {
            //Iterating through the problem codes for the selected language
            for (String display : problemsDictionary.get(language).keySet()) {
                list.add(display);
            }
        }

        return list;
    }

    /**
     * A method search for a matching observation display name within the list
     * of observations.
     *
     * @param query the search query.
     * @return the list of matching display observation names.
     */
    public List<String> complete(String query) {
        List<String> results = new ArrayList<String>();

        //Checking if the dictionary provide such a language, otherwise return empty
        if (observationsDictionary.get(language) != null) {
            //Iterating through the list of the observations
            for (Entry<String, String> entry : observationsDictionary.get(language).entrySet()) {
                String display = entry.getKey();

                //Checking the entry matches the query
                if (display.matches(".*" + query + ".*")) {
                    results.add(display);
                }
            }
        }

        return results;
    }

    /**
     * A method handling the change of the value of the diagnosis status within
     * the input form.
     *
     * @param event the value change event.
     */
    public void statusChangeListener(ValueChangeEvent event) {
        //Getting the status field value
        String value = event.getNewValue().toString();

        //Checking the status of the diagnosis selected by the user
        resolved = value != null && value.equalsIgnoreCase("completed");
    }

    /**
     * A method handling the saving action event of a diagnosis.
     *
     * @param event an action event.
     */
    public void save(ActionEvent event) {
        //Getting the faces context
        FacesContext fc = FacesContext.getCurrentInstance();

        //Getting the request context
        RequestContext rc = RequestContext.getCurrentInstance();

        //Setting the diagnosis validation passed at the beginning
        boolean passed = true;

        //Checking if the problem populated with a valid code
        if (problem == null || problem.isEmpty()) {
            //Setting the diagnosis as not passed
            passed = false;

            //Adding the valdation message into the context
            fc.addMessage(null, new FacesMessage(
                    LiferayUtils.getPortalTranslation("hcer.diagnosis.problem.required",
                            FacesService.getPortalLanguage())));
        }

        //Validating the required onset field
        if (onset == null) {
            //Setting the diagnosis as not passed
            passed = false;

            //Adding the valdation message into the context
            fc.addMessage(null, new FacesMessage(LiferayUtils.getPortalTranslation("hcer.diagnosis.onset.required", FacesService.getPortalLanguage())));
        }

        //Checking if the diagnosis is completed
        if (status.equalsIgnoreCase("completed")) {
            //Validating the resolution field
            if (resolution == null) {
                //Setting the diagnosis as not passed
                passed = false;

                //Adding the valdation message into the context
                fc.addMessage(null, new FacesMessage(LiferayUtils.getPortalTranslation("hcer.diagnosis.resolution.required", FacesService.getPortalLanguage())));
            } else {
                //Validating the interval between the onset and the resolution
                if (!resolution.after(onset)) {
                    //Setting the diagnosis as not passed
                    passed = false;

                    //Adding the valdation message into the context
                    fc.addMessage(null, new FacesMessage(LiferayUtils.getPortalTranslation("hcer.diagnosis.dates.interval.validation", FacesService.getPortalLanguage())));
                }
            }
        } else {
            //Checking if the resolution is provided in case of an active problem
            if (resolution != null) {
                //Setting the diagnosis as not passed
                passed = false;

                //Adding the valdation message into the context
                fc.addMessage(null, new FacesMessage(LiferayUtils.getPortalTranslation("hcer.diagnosis.resolution.not.required", FacesService.getPortalLanguage())));
            }
        }

        //Validating the required observations field
        if (observations == null || observations.isEmpty()) {
            //Setting diagnosis as not passed
            passed = false;

            //Adding the validation message into the context
            fc.addMessage(null, new FacesMessage(LiferayUtils.getPortalTranslation("hcer.diagnosis.observation.required", FacesService.getPortalLanguage())));
        }

        //Checking if the diagnosis validation passed
        if (passed) {
            //Copying the diagnosis instance
            Diagnosis diagnosis = this.clone();

            //Adding the cloned diagnosis to the list of session stored diagnoses
            diagnoses.add(diagnosis);

            //Resetting the diagnosis instance
            this.reset();
        }

        //Adding the validation indicator back to the client
        rc.addCallbackParam("passed", passed);
    }

    /**
     * A method copying a diagnosis instance.
     *
     * @return a clone of the diagnosis instance.
     */
    @Override
    public Diagnosis clone() {
        //Creating a new diagnosis
        Diagnosis diagnosis = new Diagnosis();

        //Copying the diagnosis attributes
        diagnosis.setProblem(problem);
        diagnosis.setStatus(status);
        diagnosis.setOnset(onset);
        diagnosis.setResolution(resolution);
        diagnosis.setResolved(resolved);
        diagnosis.setObservations(observations);
        diagnosis.setDescription(description);

        return diagnosis;
    }

    /**
     * A method resetting the diagnosis.
     */
    public void reset() {
        //Setting the attributes to null
        this.setProblem(null);
        this.setStatus(null);
        this.setOnset(null);
        this.setResolution(null);
        this.setResolved(false);
        this.setObservations(null);
        this.setDescription(null);

        //Updating the user selected language
        User user = LiferayUtils.getPortalUser();
        language = user.getLanguageId().replace("_", "-");
    }
}
