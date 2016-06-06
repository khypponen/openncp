/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.migration;

import ec.europa.eu.tsamsync2.LTR.LTRTransactionManager;
import ec.europa.eu.tsamsync2.search.ValueSetVersion;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author bernamp
 */
public class ValueSetMigration {

    private final LTRTransactionManager man;

    ValueSetMigration(LTRTransactionManager man) {
        this.man = man;
    }

    public ValueSetVersion getValueSetVersions(Long currentVersionId, List<ValueSetVersion> codeSystemVersions) {
        for (ValueSetVersion c : codeSystemVersions)
            if (Objects.equals(c.getVersionId(), currentVersionId))
                return c;
        return null;
    }

    public ec.europa.eu.tsamsync2.LTR.model.ValueSet createValueSet(Long currentVersionId, ValueSetVersion current, ec.europa.eu.tsamsync2.LTR.model.CodeSystem cs) {
        ec.europa.eu.tsamsync2.LTR.model.ValueSet vs = new ec.europa.eu.tsamsync2.LTR.model.ValueSet();
        vs.setEpsosName(current.getName());
        vs.setOid(currentVersionId + "");
//        vs.setDescription(current.getValueSet().getDescription());
        vs.setParentCodeSystemId(cs);
        vs.setValueSetVersionCollection(new ArrayList<ec.europa.eu.tsamsync2.LTR.model.ValueSetVersion>());
        return vs;
    }

    public ec.europa.eu.tsamsync2.LTR.model.ValueSetVersion createValueSetVersion(ValueSetVersion csv, ec.europa.eu.tsamsync2.LTR.model.ValueSet cs) {
        Date d = new Date();
        ec.europa.eu.tsamsync2.LTR.model.ValueSetVersion o = new ec.europa.eu.tsamsync2.LTR.model.ValueSetVersion();
        o.setValueSetId(cs);
//        o.setCodeSystemConceptCollection(codeSystemConceptCollection);
//        o.setDescription(csv.getValueSet().getDescription());
        o.setEffectiveDate(d);
        o.setReleaseDate(d);
        o.setStatus(csv.getStatus() + "");
        o.setVersionName(csv.getName());
        o.setStatusDate(d);
        return o;
   }

}
