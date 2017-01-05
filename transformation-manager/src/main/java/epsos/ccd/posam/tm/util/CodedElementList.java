package epsos.ccd.posam.tm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Coded Element List.<br>
 * Serves as Filter for determining transcoded / translated Coded Elements from
 * input CDA document.<br>
 * Reads list from xml structure. (Path to Input xml file is configured through property <i>tm.codedelementlist.path</i>)<br>
 * <br>
 * <p>
 * To follow relation between coded element and valueset, configuration file
 * called CodedElementList will be used. It will contain list of all coded
 * elements, and for each of them:<br>
 * <li> name of coded element (xpath),</li> <li> indication, in which pivot
 * document types coded element is used (Patient Summary, ePrescription,
 * eDispensation, and for each of them if it is in CDA level 3 or CDA level 1
 * embedding pdf and whether element is required or optional),</li> <li>
 * related value set (and value set version) [optional],</li> <li> language to
 * which element should be translated [optional].</li>
 *
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.6, 2010, 20 October
 */
public class CodedElementList implements InitializingBean, TMConstants {

    private static final Logger log = LoggerFactory.getLogger(CodedElementList.class);

    private boolean isInitialized = false;

    /**
     * path to xml file with Coded Element List
     */
    private String codedElementListPath;

    /**
     * true means configurable Element identification is used; false means no
     */
    private boolean configurableElementIdentification;

    // collections of CodedElementListItem for CDA document types
    /**
     * Collection of Elements contained in Patient Summary CDA document (level
     * 3)
     */
    private static Collection<CodedElementListItem> patientSummaryl3;
    /**
     * Collection of Elements contained in header of Patient Summary CDA
     * document (level 1 with pdf)
     */
    private static Collection<CodedElementListItem> patientSummaryl1;

    /**
     * Collection of Elements contained in header of ePrescription CDA document
     * (level 1 with pdf)
     */
    private static Collection<CodedElementListItem> ePrescriptionl1;

    /**
     * Collection of Elements contained in ePrescription CDA document (level 3)
     */
    private static Collection<CodedElementListItem> ePrescriptionl3;

    /**
     * Collection of Elements contained in eDispensation CDA document (level 3)
     */
    private static Collection<CodedElementListItem> eDispensationl1;

    /**
     * Collection of Elements contained in header of eDispensation CDA document
     * (level 1 with pdf)
     */
    private static Collection<CodedElementListItem> eDispensationl3;

    private static Collection<CodedElementListItem> hcerl3;
    private static Collection<CodedElementListItem> hcerl1;
    private static Collection<CodedElementListItem> mrol3;
    private static Collection<CodedElementListItem> mrol1;

    private static CodedElementList instance = null;

    private static HashMap<String, Collection<CodedElementListItem>> hmDocAndLists = new HashMap<String, Collection<CodedElementListItem>>();

    private CodedElementList() {
    }

    public static CodedElementList getInstance() {
        if (instance == null) {
            instance = new CodedElementList();
        }
        return instance;
    }

    public void setCodedElementListPath(String codedElementListPath) {
        this.codedElementListPath = codedElementListPath;
    }

    public boolean isConfigurableElementIdentification() {
        return configurableElementIdentification;
    }

    public void setConfigurableElementIdentification(
            boolean configurableElementIdentification) {
        this.configurableElementIdentification = configurableElementIdentification;
    }

    public String getCodedElementListPath() {
        return codedElementListPath;
    }

    public Collection<CodedElementListItem> getPatientSummaryl3() {
        return patientSummaryl3;
    }

    public Collection<CodedElementListItem> getPatientSummaryl1() {
        return patientSummaryl1;
    }

    public Collection<CodedElementListItem> getePrescriptionl1() {
        return ePrescriptionl1;
    }

    public Collection<CodedElementListItem> getePrescriptionl3() {
        return ePrescriptionl3;
    }

    public Collection<CodedElementListItem> geteDispensationl1() {
        return eDispensationl1;
    }

    public Collection<CodedElementListItem> geteDispensationl3() {
        return eDispensationl3;
    }

    public void afterPropertiesSet() throws Exception {
        // read xml file (coded_element_list.xml)
        if (configurableElementIdentification && !isInitialized) {
            log
                    .info("Coded Element List - configurableElementIdentification USED");
            Document doc = XmlUtil.getDocument(new File(codedElementListPath), true);
            log.info("Coded Element List - read from xml BEGIN ");

            Element root = doc.getDocumentElement();
            NodeList codedElements = root.getElementsByTagName(CODED_ELEMENT);
            log.info("Coded Element count: " + codedElements.getLength());
            // fill collections

            Element codedElement;
            Element usageElement;
            Element docTypeElement;
            for (int i = 0; i < codedElements.getLength(); i++) {
                codedElement = (Element) codedElements.item(i);

                NodeList usageNL = codedElement.getElementsByTagName(USAGE);
                // exactly 1 element usage expected
                usageElement = (Element) usageNL.item(0);

                NodeList docTypesUsageNL = usageElement.getChildNodes();
                for (int j = 0; j < docTypesUsageNL.getLength(); j++) {
                    Node node = docTypesUsageNL.item(j);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        docTypeElement = (Element) node;

                        if (docTypeElement.getNodeName().equals(
                                PATIENT_SUMMARY1)) {
                            patientSummaryl1 = addItem(docTypeElement, codedElement,
                                    patientSummaryl1);
                        } else if (docTypeElement.getNodeName().equals(
                                PATIENT_SUMMARY3)) {
                            patientSummaryl3 = addItem(docTypeElement, codedElement,
                                    patientSummaryl3);
                        } else if (docTypeElement.getNodeName().equals(
                                EPRESCRIPTION1)) {
                            ePrescriptionl1 = addItem(docTypeElement, codedElement,
                                    ePrescriptionl1);
                        } else if (docTypeElement.getNodeName().equals(
                                EPRESCRIPTION3)) {
                            ePrescriptionl3 = addItem(docTypeElement, codedElement,
                                    ePrescriptionl3);
                        } else if (docTypeElement.getNodeName().equals(
                                EDISPENSATION1)) {
                            eDispensationl1 = addItem(docTypeElement, codedElement,
                                    eDispensationl1);
                        } else if (docTypeElement.getNodeName().equals(
                                EDISPENSATION3)) {
                            eDispensationl3 = addItem(docTypeElement, codedElement,
                                    eDispensationl3);
                        } else if (docTypeElement.getNodeName().equals(
                                HCER3)) {
                            hcerl3 = addItem(docTypeElement, codedElement,
                                    hcerl3);
                        } else if (docTypeElement.getNodeName().equals(
                                HCER1)) {
                            hcerl1 = addItem(docTypeElement, codedElement,
                                    hcerl1);
                        } else if (docTypeElement.getNodeName().equals(
                                MRO3)) {
                            mrol3 = addItem(docTypeElement, codedElement,
                                    mrol3);
                        } else if (docTypeElement.getNodeName().equals(
                                MRO1)) {
                            mrol1 = addItem(docTypeElement, codedElement,
                                    mrol1);
                        }
                    }
                }
            }

            hmDocAndLists.put(PATIENT_SUMMARY1, patientSummaryl1);
            hmDocAndLists.put(PATIENT_SUMMARY3, patientSummaryl3);
            hmDocAndLists.put(EDISPENSATION1, eDispensationl1);
            hmDocAndLists.put(EDISPENSATION3, eDispensationl3);
            hmDocAndLists.put(EPRESCRIPTION1, ePrescriptionl1);
            hmDocAndLists.put(EPRESCRIPTION3, ePrescriptionl3);

            hmDocAndLists.put(HCER1, hcerl1);
            hmDocAndLists.put(HCER3, hcerl3);
            hmDocAndLists.put(MRO1, mrol1);
            hmDocAndLists.put(MRO3, mrol3);

            isInitialized = true;

            log.info("Coded Element List - read from xml END ");
        } else {
            log.info("Coded Element List - configurableElementIdentification NOT used");
        }
    }

    private Collection<CodedElementListItem> addItem(Element docTypeElement, Element codedElement,
                                                     Collection<CodedElementListItem> collection) {
        String usage = docTypeElement.getTextContent();
        if (collection == null) {
            collection = new ArrayList<CodedElementListItem>();
        }
        try {
            if (isNeeded(usage)) {
                CodedElementListItem item = new CodedElementListItem();

                String xPath = ((Element) codedElement.getElementsByTagName(
                        ELEMENT_PATH).item(0)).getTextContent();
                String valueSet = ((Element) codedElement.getElementsByTagName(
                        VALUE_SET).item(0)).getTextContent();
                String valueSetVersion = ((Element) codedElement
                        .getElementsByTagName(VALUE_SET_VERSION).item(0))
                        .getTextContent();
                String targetLanguageCode = ((Element) codedElement
                        .getElementsByTagName(TARGET_LANGUAGE_CODE).item(0))
                        .getTextContent();

                item.setxPath(xPath);
                item.setUsage(usage);
                item.setValueSet(valueSet);
                item.setValueSetVersion(valueSetVersion);
                item.setTargetLanguageCode(targetLanguageCode);
                if (!collection.contains(item)) {
                    collection.add(item);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return collection;
    }

    private boolean isNeeded(String usage) {
        return (usage != null && usage.length() > 0 && (usage.equals(R)
                || usage.equals(RNFA) || usage.equals(O)));
    }

    /**
     * For input cda Document type returns correct List of CodedElementListItems
     *
     * @param cdaDocumentType
     * @return List of CodedElementListItems
     */
    public Collection<CodedElementListItem> getList(String cdaDocumentType) {
        return hmDocAndLists.get(cdaDocumentType);
    }
}
