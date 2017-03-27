package epsos.ccd.posam.tsam.service;

import epsos.ccd.posam.tsam.response.RetrievedConcept;
import epsos.ccd.posam.tsam.response.TSAMResponseStructure;
import epsos.ccd.posam.tsam.util.CodedElement;

import java.util.List;

/**
 * Component responsibilities:<br>
 * <li>1. Translating a given concept designation into the requested target
 * language using the information present in the Terminology Repository.</li>
 * <li>
 * 2. Transcoding a given local coded concept into the appropriate epSOS coded
 * concept using the information present in the Terminology Repository.</li>
 *
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August
 */
public interface ITerminologyService {

    /**
     * When this component receives a getEpSOSConceptByCode () request, it uses
     * all the data extracted from the LocalConcept structure in order to search
     * within the Terminology Repository for the best matching epSOS Concept,
     * according to the local information provided (e.g., if no code system
     * version is indicated, the latest version will be provided). All
     * information retrieved is finally returned to the requesting component.<br>
     * <br>
     * <p>
     * <i>Exceptions:</i> if there is no transcoding or a processing error
     * occurs, the responseStatusStructure will be used to convey this
     * information to the calling component with an appropriated error and
     * warning code. A detailed list of the managed exceptions will be provided
     * in the Detail Design Specification document. Each exception condition
     * occurred will be logged (standard and audit), reporting both the
     * exception code and its English description.
     *
     * @param localConcept Structure used to convey the concept derived from the epSOS
     *                     Original Data. It shall include at least the concept code and
     *                     the concept code system. Code System Version, Country Code and
     *                     value set OID - if available - should be provided.
     * @return TranscodingResponseStructure - Response structure including:<br>
     * <li>1. the epSOS Reference Concept: this means the Concept Code,
     * the English designation, the concept code system (OID), Code
     * System Version, Value Set OID; Value Set Version,</li> <li>2. The
     * responseStatusStructure, providing information about operation
     * result, including possible errors and warning.</li>
     */
    TSAMResponseStructure getEpSOSConceptByCode(CodedElement localConcept);

    /**
     * When this component receives a getDesignationByEpSOSConcept () request,
     * it uses all the data extracted from the EpSOSRefConcept structure in
     * order to search within the Terminology Repository for the target language
     * epSOS Designation, according to the local information provided (e.g., if
     * no code system version is indicated, the latest version will be
     * provided). All information retrieved are finally returned to the
     * requesting component.<br>
     * <br>
     * <i>Exceptions:</i> if there is no translation or a processing error
     * occurs, the responseStatusStructure will be used to convey this
     * information to the calling component with an appropriated error and
     * warning code. A detailed list of the managed exceptions will be provided
     * in the Detail Design Specification document. Each exception condition
     * occurred will be logged (standard and audit), reporting both the
     * exception code and its English description.
     *
     * @param epSOSRefConcept    Structure used to convey the concept derived from the epSOS
     *                           pivot CDA. It shall include at least the concept code and the
     *                           concept code system. Code System Version, Country Code and
     *                           value set OID - if available - should be provided.
     * @param targetLanguageCode identifier (code) of the target language.
     * @return TranslatingResponseStructure - Response structure including:<br>
     * <li>1. the target language concept designation;</li><li>2. the
     * responseStatusStructure providing information about operation
     * result, including possible errors and warning.</li>
     */
    TSAMResponseStructure getDesignationByEpSOSConcept(CodedElement epSOSRefConcept,
                                                       String targetLanguageCode);

    /**
     * Additional method for providing of all concepts and their designations
     * from a specified value set
     *
     * @param valueSetOid
     * @param valueSetVersionName
     * @param language
     * @return
     */
    List<RetrievedConcept> getValueSetConcepts(String valueSetOid, String valueSetVersionName, String language);

    /**
     * Additional method for retrieving the list of all available languages in the LTR
     *
     * @return the list of LTR available languages, as a String list, containing the language codes;
     */
    List<String> getLtrLanguages();
}
