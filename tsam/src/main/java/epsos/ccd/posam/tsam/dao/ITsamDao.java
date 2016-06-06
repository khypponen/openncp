package epsos.ccd.posam.tsam.dao;

import java.util.List;

import epsos.ccd.posam.tsam.exception.TSAMException;
import epsos.ccd.posam.tsam.model.CodeSystem;
import epsos.ccd.posam.tsam.model.CodeSystemConcept;
import epsos.ccd.posam.tsam.model.CodeSystemVersion;
import epsos.ccd.posam.tsam.model.Designation;
import epsos.ccd.posam.tsam.model.ValueSetVersion;
import epsos.ccd.posam.tsam.response.RetrievedConcept;

/**
 * Data Acces Object layer
 *
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August
 *
 */
public interface ITsamDao {

    /**
     * Check if association between CodeSystemConcept and ValueSet with
     * valueSetOid exist. If valueSetVersion is providet, checks if
     *
     * @param concept
     * @param valueSetOid
     * @param valueSetVersion - name of ValueSetVersion
     * @return true if assocation exist else false
     */
    public boolean valueSetMatches(CodeSystemConcept concept, String valueSetOid, String valueSetVersion);

    /**
     * Retrieve Designation for Target CodeSystemConcept and selected language
     *
     * @param target
     * @param lang
     * @return
     * @throws TSAMException if designation is not found
     */
    public List<Designation> getDesignation(CodeSystemConcept target, String lang) throws TSAMException;

    /**
     * Retrieve Target CodeSystemConcept from Transcoding association by Source
     * Concept
     *
     * @param sourceConcept
     * @return
     * @throws TSAMException if target concept is not found
     */
    public CodeSystemConcept getTargetConcept(CodeSystemConcept sourceConcept) throws TSAMException;

    /**
     * Retrieve CodeSystemConcept by Code and CodeSystemVersion. Method looks
     * for concept int provided CodeSystemVersion and all its previous versions
     *
     * @param session
     * @param code
     * @param codeSystemVersion
     * @return
     * @throws TSAMException if concept is not found
     */
    public CodeSystemConcept getConcept(String code, CodeSystemVersion codeSystemVersion) throws TSAMException;

    /**
     * Retrieve CodeSystemversion by LocalName and parent CodeSystem. If no
     * version is provided (version is null), method looks for versions of
     * CodeSystem with status "current".
     *
     * @param session
     * @param version
     * @param system
     * @return
     * @throws TSAMException if version is not found
     */
    public CodeSystemVersion getVersion(String version, CodeSystem system) throws TSAMException;

    /**
     * Retrieve CodeSystem by OID
     *
     * @param session
     * @param oid
     * @return CodeSystem
     * @throws TSAMException if CodeSystem is not found
     */
    public CodeSystem getCodeSystem(String oid) throws TSAMException;

    /**
     * Method to retrieve all concepts and their current designations for
     * selected ValueSet ana language. If language is null, default (en)
     * language is used
     *
     * @param valueSetOid
     * @param valueSetVersionName
     * @param language
     * @return
     */
    public List<RetrievedConcept> getConcepts(String valueSetOid, String valueSetVersionName, String language);

    /**
     *
     * @param target
     * @return
     * @throws TSAMException
     */
    public List<Designation> getSourceDesignation(CodeSystemConcept target) throws TSAMException;

    /**
     * This method will return all available languages in the LTR.
     *
     * @return the list of LTR available languages, as a String list, containing
     * the language codes;
     */
    public List<String> getLtrLanguages();
}