/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.LTR;

import ec.europa.eu.tsamsync2.LTR.model.CodeSystemConcept;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Managing the connection to the db
 *
 * @author bernamp
 */
public class LTRTransactionManager {

    private String unit;
    private EntityManager em;
    List<Object> bucket = new ArrayList<>();

    public LTRTransactionManager() {
        this("LTR");
    }

    public LTRTransactionManager(String persistenceUnit) {
        this.unit = persistenceUnit;
        open();
    }

    public void addToBucket(Object o) {
        bucket.add(o);
    }

    public void cleanBucket() {
        bucket.clear();
    }

    public boolean saveBucket() {
        if (em.isOpen()) {
            if (!em.getTransaction().isActive())
                em.getTransaction().begin();
            try {
                for (Object o : bucket)
                    em.persist(o);
                em.getTransaction().commit();
                cleanBucket();
            } catch (Exception e) {
                Logger.getLogger(LTRTransactionManager.class.getName()).log(Level.SEVERE, null, e);
                em.getTransaction().rollback();
                return false;
            }
        }
        return true;
    }

    private void open() {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory(unit);
        em = emf.createEntityManager();
    }

    public void close() {
        em.close();
    }

    public void addSingleObject(Object o) {
        if (em.isOpen()) {
            if (!em.getTransaction().isActive())
                em.getTransaction().begin();
            try {
                em.persist(o);
                em.getTransaction().commit();
            } catch (Exception e) {
                Logger.getLogger(LTRTransactionManager.class.getName()).log(Level.SEVERE, null, e);
                em.getTransaction().rollback();
            }
        }
    }

    public void deleteSingleObject(Object o) {
        if (em.isOpen()) {
            if (!em.getTransaction().isActive())
                em.getTransaction().begin();
            try {
                em.remove(o);
                em.getTransaction().commit();
            } catch (Exception e) {
                Logger.getLogger(LTRTransactionManager.class.getName()).log(Level.SEVERE, null, e);
                em.getTransaction().rollback();
            }
        }
    }

    public void updateSingleObject(Object o) {
        if (em.isOpen()) {
            if (!em.getTransaction().isActive())
                em.getTransaction().begin();
            try {
                em.merge(o);
                em.getTransaction().commit();
            } catch (Exception e) {
                Logger.getLogger(LTRTransactionManager.class.getName()).log(Level.SEVERE, null, e);
                em.getTransaction().rollback();
            }
        }
    }

    public List<?> getAll(Class clazz) {
        if (em.isOpen()) {
            if (!em.getTransaction().isActive())
                em.getTransaction().begin();
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<?> cq = cb.createQuery(clazz);
                TypedQuery<?> q = em.createQuery(cq.select(cq.from(clazz)));
                em.getTransaction().commit();
                return q.getResultList();

            } catch (Exception e) {
                Logger.getLogger(LTRTransactionManager.class.getName()).log(Level.SEVERE, null, e);
                em.getTransaction().rollback();
                return null;
            }
        }
        return null;
    }

    public CodeSystemConcept getCodeSystemConceptId(String code, String term) {
        if (em.isOpen()) {
            if (!em.getTransaction().isActive())
                em.getTransaction().begin();
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<CodeSystemConcept> cq = cb.createQuery(CodeSystemConcept.class);
                Root<CodeSystemConcept> root = cq.from(CodeSystemConcept.class);
                cq.where(cb.and(cb.equal(root.get("code"), code), cb.equal(root.get("definition"), term)));
                TypedQuery<CodeSystemConcept> query = em.createQuery(cq);
                List<CodeSystemConcept> res = query.getResultList();
                if(res.size() > 0)
                    return res.get(0);
                return null;

            } catch (Exception e) {
                Logger.getLogger(LTRTransactionManager.class.getName()).log(Level.SEVERE, null, e);
                em.getTransaction().rollback();
                return null;
            }
        }
        return null;
    }
}
