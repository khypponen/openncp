/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.migration;

import ec.europa.eu.tsamsync2.LTR.LTRTransactionManager;
import ec.europa.eu.tsamsync2.LTR.model.Designation;
import ec.europa.eu.tsamsync2.search.CodeSystem;
import ec.europa.eu.tsamsync2.search.CodeSystemConcept;
import ec.europa.eu.tsamsync2.search.CodeSystemEntity;
import ec.europa.eu.tsamsync2.search.CodeSystemEntityVersion;
import ec.europa.eu.tsamsync2.search.CodeSystemVersion;
import ec.europa.eu.tsamsync2.search.ValueSet;
import ec.europa.eu.tsamsync2.search.ValueSetVersion;
import ec.europa.eu.tsamsync2.services.AuthorizationServices;
import ec.europa.eu.tsamsync2.services.SearchServices;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bernamp
 */
public class Migration {
    
    List<ec.europa.eu.tsamsync2.LTR.model.ValueSet> allValueSets = new ArrayList<>();
        
    public static void main(String[] args) {
        try {
            new Migration("http://localhost:8080/TermServer/", "test", "test");
        } catch (MalformedURLException ex) {
            Logger.getLogger(Migration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Migration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Migration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    Migration(String url, String usr, String pwd) throws MalformedURLException, NoSuchAlgorithmException, Exception {
        String loginToken = AuthorizationServices.get().login(usr, pwd);
        if (loginToken == null)
            throw new Exception("Login issue...");
        LTRTransactionManager man = new LTRTransactionManager();
        
        CodeSystemMigration csmig = new CodeSystemMigration();
        for (CodeSystem cs : SearchServices.get().listCodeSystems()) {
            CodeSystemVersion current_version = csmig.getCodeSystemVersions(cs.getCurrentVersionId(), cs.getCodeSystemVersions());
            ec.europa.eu.tsamsync2.LTR.model.CodeSystem currentCodeSystem = csmig.addCodeSystem(cs, current_version.getOid());
            man.addToBucket(currentCodeSystem);
            for (CodeSystemVersion csv : cs.getCodeSystemVersions()) {
                ec.europa.eu.tsamsync2.LTR.model.CodeSystemVersion codeSystemVersion = csmig.addCodeSystemVersion(csv, currentCodeSystem);
                man.addToBucket(codeSystemVersion);
                for (CodeSystemEntity cse : SearchServices.get().listCodeSystemConcepts(loginToken, cs))
                    for (CodeSystemEntityVersion csev : cse.getCodeSystemEntityVersions())
                        for (ec.europa.eu.tsamsync2.search.CodeSystemConcept c : csev.getCodeSystemConcepts()) {
                            ec.europa.eu.tsamsync2.LTR.model.CodeSystemConcept concept = csmig.addConcept(c, codeSystemVersion);
                            man.addToBucket(concept);
                        }
            }
        }
        man.saveBucket();

        
        ValueSetMigration vsmig = new ValueSetMigration(man);
        for (ValueSet vs : SearchServices.get().listValueSets()) {
            List<CodeSystemEntity> vsc = SearchServices.get().listValueSetContents(loginToken, vs);
            for (CodeSystemEntity cse : vsc)
                for (CodeSystemEntityVersion csev : cse.getCodeSystemEntityVersions())
                    for (CodeSystemConcept csc : csev.getCodeSystemConcepts()) {
                        Designation d = new Designation();
                        d.setDesignation(csc.getTerm());
                        d.setIsPreferred(Boolean.TRUE);
                        d.setLanguageCode("EN");
                        d.setStatus(STATUS.CURRENT.getName());
                        d.setStatusDate(new Date());
                        d.setType("Preferred term");
                        d.setCodeSystemConceptId(man.getCodeSystemConceptId(csc.getCode(), csc.getTerm()));
                        ValueSetVersion currentValueSet_version = vsmig.getValueSetVersions(vs.getCurrentVersionId(), vs.getValueSetVersions());
                        man.addToBucket(d);
                        ec.europa.eu.tsamsync2.LTR.model.ValueSet currentValueSet = vsmig.createValueSet(currentValueSet_version.getVersionId(), currentValueSet_version, d.getCodeSystemConceptId().getCodeSystemVersionId().getCodeSystemId());
                        currentValueSet = getThis(currentValueSet);
                        if(!allValueSets.contains(currentValueSet))
                            allValueSets.add(currentValueSet);
                        List<ec.europa.eu.tsamsync2.LTR.model.ValueSetVersion> allValueSetVersions = new ArrayList<>();
                        if (currentValueSet.getParentCodeSystemId() != null)
                            for (ValueSetVersion csv : vs.getValueSetVersions())
                                allValueSetVersions.add(vsmig.createValueSetVersion(csv, currentValueSet));
                        currentValueSet.getValueSetVersionCollection().addAll(allValueSetVersions);
                    }
        }
        for (ec.europa.eu.tsamsync2.LTR.model.ValueSet vs_ : allValueSets) {
            man.addToBucket(vs_);
            Logger.getLogger(ValueSetMigration.class.getName()).log(Level.INFO, "Added Value Set  {0}", vs_.getEpsosName());
        }
        man.saveBucket();
        AuthorizationServices.get().logout(loginToken);
        man.close();
    }
    
    private ec.europa.eu.tsamsync2.LTR.model.ValueSet getThis(ec.europa.eu.tsamsync2.LTR.model.ValueSet curr){
    for(ec.europa.eu.tsamsync2.LTR.model.ValueSet v : allValueSets)
        if(v.equals(curr))
            return v;
    return curr;
}
}
