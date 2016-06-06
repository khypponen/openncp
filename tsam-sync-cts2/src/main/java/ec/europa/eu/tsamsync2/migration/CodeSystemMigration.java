/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.migration;

import ec.europa.eu.tsamsync2.LTR.model.CodeSystemConcept;
import ec.europa.eu.tsamsync2.search.CodeSystem;
import ec.europa.eu.tsamsync2.search.CodeSystemVersion;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bernamp
 */
public class CodeSystemMigration {

    public CodeSystemMigration() {
    }

    CodeSystemVersion getCodeSystemVersions(Long currentVersionId, List<CodeSystemVersion> codeSystemVersions) {
        for (CodeSystemVersion c : codeSystemVersions)
            if (Objects.equals(c.getVersionId(), currentVersionId))
                return c;
        return null;
    }

    public ec.europa.eu.tsamsync2.LTR.model.CodeSystem addCodeSystem(CodeSystem current, String oid) {
        ec.europa.eu.tsamsync2.LTR.model.CodeSystem cs = new ec.europa.eu.tsamsync2.LTR.model.CodeSystem();
        cs.setName(current.getName());
        cs.setOid(oid);
        cs.setDescription(current.getDescription());
        Logger.getLogger(CodeSystemMigration.class.getName()).log(Level.FINE, "Added code system {0}", cs.getName());
        return cs;
    }

    public ec.europa.eu.tsamsync2.LTR.model.CodeSystemVersion addCodeSystemVersion(CodeSystemVersion csv, ec.europa.eu.tsamsync2.LTR.model.CodeSystem cs) {
        Date d = new Date();
        ec.europa.eu.tsamsync2.LTR.model.CodeSystemVersion o = new ec.europa.eu.tsamsync2.LTR.model.CodeSystemVersion();
        o.setCodeSystemId(cs);
//        o.setCodeSystemConceptCollection(codeSystemConceptCollection);
        o.setCopyright(csv.getLicenceHolder());
        o.setDescription(csv.getDescription());
        o.setFullName(csv.getName());
        o.setLocalName(csv.getName());
        o.setReleaseDate(d);
        o.setEffectiveDate(d);
        o.setSource(csv.getSource());
        o.setStatus(csv.getStatus() + "");
        o.setStatusDate(d);
        Logger.getLogger(CodeSystemMigration.class.getName()).log(Level.FINE, "Added CodeSystemVersion {0}", cs.getName());
        return o;
    }

    public CodeSystemConcept addConcept(ec.europa.eu.tsamsync2.search.CodeSystemConcept c, ec.europa.eu.tsamsync2.LTR.model.CodeSystemVersion cs) {
        Date d = new Date();

        CodeSystemConcept concept = new CodeSystemConcept();
        concept.setCode(c.getCode());
        concept.setCodeSystemVersionId(cs);
        concept.setDefinition(c.getTerm());
//            concept.setDesignationCollection();
//            concept.setId(c.);
        concept.setStatus(cs.getStatus() + "");
        concept.setStatusDate(d);
//            concept.setTranscodingAssociationCollection();
//            concept.setTranscodingAssociationCollection1();
//            concept.setValueSetVersionCollection();
        return concept;
    }
}
