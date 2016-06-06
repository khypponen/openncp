/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.test;

import ec.europa.eu.tsamsync2.LTR.LTRTransactionManager;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author bernamp
 */
public class LTRTests {

    public LTRTests() {
    }

    @Test
    public void testInsert() {
        LTRTransactionManager man = new LTRTransactionManager();
        for (int i = 0; i < 2; i++) {
            ec.europa.eu.tsamsync2.LTR.model.CodeSystem c = new ec.europa.eu.tsamsync2.LTR.model.CodeSystem();
            c.setName("CODE");
            c.setDescription(new Date().toString());
            c.setOid("" + Math.random());
            man.addSingleObject(c);
            Logger.getLogger(LTRTests.class.getName()).log(Level.INFO, "Added code system with id{0}", c.getId());
        }
        man.close();
    }

    @Test
    public void testDelete() {
        LTRTransactionManager man = new LTRTransactionManager();
        for (Object c : man.getAll(ec.europa.eu.tsamsync2.LTR.model.CodeSystem.class))
            man.deleteSingleObject((ec.europa.eu.tsamsync2.LTR.model.CodeSystem) c);
        man.close();
    }

    @Test
    public void testUpdate() {
        LTRTransactionManager man = new LTRTransactionManager();
        for (Object c : man.getAll(ec.europa.eu.tsamsync2.LTR.model.CodeSystem.class)) {
            ((ec.europa.eu.tsamsync2.LTR.model.CodeSystem) c).setName("UPDATED");
            man.updateSingleObject(c);
        }
        man.close();
    }
}
