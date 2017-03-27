package epsos.ccd.posam.tsam.dao.impl;

import epsos.ccd.posam.tsam.dao.ITsamDao;
import epsos.ccd.posam.tsam.exception.TSAMError;
import epsos.ccd.posam.tsam.exception.TSAMException;
import epsos.ccd.posam.tsam.model.*;
import epsos.ccd.posam.tsam.response.RetrievedConcept;
import epsos.ccd.posam.tsam.util.TsamConfiguration;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.*;

/**
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August
 * @see ITsamDao
 */
public class TsamDao extends HibernateDaoSupport implements ITsamDao {

    public static final String AT_ID = "id";
    public static final String AT_OID = "oid";
    public static final String AT_STATUS = "status";
    private static final String CURRENT_STATUS = "current";
    private static final String VALID_STATUS = "valid";
    private TsamConfiguration config;

    public boolean valueSetMatches(CodeSystemConcept concept, String valueSetOid, String valueSetVersion) {
        if (valueSetOid == null) {
            return false;
        }
        Criteria crt = getSessionFactory().getCurrentSession().createCriteria(ValueSetVersion.class);
        crt.createCriteria(ValueSetVersion.AT_VALUE_SET).add(Restrictions.eq(TsamDao.AT_OID, valueSetOid));
        crt.createCriteria(ValueSetVersion.AT_CONCEPTS).add(Restrictions.idEq(concept.getId()));
        List<ValueSetVersion> result = crt.list();
        if (valueSetVersion == null) {
            return result.size() > 0;
        } else {
            boolean match = false;
            for (ValueSetVersion version : result) {
                if (valueSetVersion.equals(version.getVersionName())) {
                    match = true;
                    break;
                }
            }
            return match;
        }
    }

    public List<Designation> getSourceDesignation(CodeSystemConcept target) throws TSAMException {
        List result = null;
        try {
            result = getDesignation(target, config.getTranscodingLang());
        } catch (TSAMException e) {
            if (e.getReason() == TSAMError.ERROR_DESIGNATION_NOTFOUND) {
                throw new TSAMException(TSAMError.ERROR_TARGET_CONCEPT_NOTFOUND);
            } else {
                throw e;
            }
        }
        return result;
    }

    public List<Designation> getDesignation(CodeSystemConcept target, String lang) throws TSAMException {
        Criteria crt = getSessionFactory().getCurrentSession().createCriteria(Designation.class);
        crt.createCriteria(Designation.AT_CONCEPT)
                .add(Restrictions.eq(CodeSystemConcept.AT_ID, target.getId()));
        crt.add(Restrictions.eq(Designation.AT_LANGUAGE, lang));

        List<Designation> designations = crt.list();
        if (designations.size() == 0) {
            throw new TSAMException(TSAMError.ERROR_DESIGNATION_NOTFOUND);
        }

        List<Designation> filter = new ArrayList<>();

        for (Designation designation : designations) {
            if (CURRENT_STATUS.equalsIgnoreCase(designation.getStatus())) {
                filter.add(designation);
            }

        }

        if (filter.size() == 0) {
            throw new TSAMException(TSAMError.ERROR_NO_CURRENT_DESIGNATIONS);
        }

        designations = filter;

        // sort designations by preffered flag, put preffered on top
        if (designations.size() > 1) {
            Collections.sort(designations, new Comparator<Designation>() {
                public int compare(Designation o1, Designation o2) {
                    if (Boolean.TRUE.equals(o1.isPreffered())) {
                        return -1;
                    } else if (Boolean.TRUE.equals(o2.isPreffered())) {
                        return 1;
                    }
                    return 0;
                }

                public boolean equals(Object obj) {
                    return super.equals(obj);
                }
            });
        }

        return designations;
    }

    public CodeSystemConcept getTargetConcept(CodeSystemConcept sourceConcept) throws TSAMException {
        Criteria crt;
        crt = getSessionFactory().getCurrentSession().createCriteria(TranscodingAssociation.class).createCriteria(
                TranscodingAssociation.AT_SOURCE_CONCEPT).add(
                Restrictions.eq(CodeSystemConcept.AT_ID, sourceConcept.getId()));
        List<TranscodingAssociation> associations = crt.list();
        if (associations.size() == 0) {
            return null;
        }
        CodeSystemConcept target = null;
        for (TranscodingAssociation association : associations) {
            target = association.getTargedConcept();
            String status = association.getStatus();
            if (status == null || !status.toLowerCase().equals(VALID_STATUS)) {
            } else {
                break;
            }
        }
        if (target == null) {
            throw new TSAMException(TSAMError.ERROR_TRANSCODING_INVALID);
        }
        return target;
    }

    public CodeSystemConcept getConcept(String code, CodeSystemVersion codeSystemVersion) throws TSAMException {
        List<CodeSystemConcept> concepts = null;
        List ids = new ArrayList();
        CodeSystemVersion tmp = codeSystemVersion;
        while (tmp != null) {
            ids.add(tmp.getId());
            tmp = tmp.getPreviousVersion();
        }
        Criteria crt = getSessionFactory().getCurrentSession().createCriteria(CodeSystemConcept.class).add(
                Restrictions.eq(CodeSystemConcept.AT_CODE, code));
        crt.createCriteria(CodeSystemConcept.AT_CS_VERSION).add(
                Restrictions.in(TsamDao.AT_ID, ids));

        concepts = crt.list();
        if (concepts.size() == 0) {
            throw new TSAMException(TSAMError.ERROR_CODE_SYSTEM_CONCEPT_NOTFOUND);
        }
        // if more concepts are found, try to pick current one
        if (concepts.size() > 1) {
            for (CodeSystemConcept concept : concepts) {
                if (CURRENT_STATUS.equalsIgnoreCase(concept.getStatus())) {
                    return concept;
                }
            }
        }
        CodeSystemConcept concept = concepts.get(0);
        return concept;
    }

    public CodeSystemVersion getVersion(String version, CodeSystem system) throws TSAMException {
        List<CodeSystemVersion> versions;
        Criteria crt;
        if (version != null) {
            crt = getSessionFactory().getCurrentSession().createCriteria(CodeSystemVersion.class).add(
                    Restrictions.eq(CodeSystemVersion.AT_LNAME, version));
        } else {
            crt = getSessionFactory().getCurrentSession().createCriteria(CodeSystemVersion.class).add(
                    Restrictions.ilike(TsamDao.AT_STATUS, CURRENT_STATUS));
        }
        crt.createCriteria(CodeSystemVersion.AT_CODESYSTEM).add(Restrictions.eq(TsamDao.AT_ID, system.getId()));
        versions = crt.list();
        if (versions.size() == 0) {
            throw new TSAMException(TSAMError.ERROR_CODE_SYSTEM_VERSION_NOTFOUND);
        }
        CodeSystemVersion codeSystemVersion = versions.get(0);
        return codeSystemVersion;
    }

    public CodeSystem getCodeSystem(String oid) throws TSAMException {
        Criteria crt = getSessionFactory().getCurrentSession().createCriteria(CodeSystem.class).add(
                Restrictions.eq(TsamDao.AT_OID, oid));
        List<CodeSystem> systems = crt.list();
        if (systems.size() == 0) {
            throw new TSAMException(TSAMError.ERROR_CODE_SYSTEM_NOTFOUND, oid);
        }
        CodeSystem system = systems.get(0);
        return system;
    }

    public List<RetrievedConcept> getConcepts(String valueSetOid, String valueSetVersionName, String language) {
        ArrayList<RetrievedConcept> result;
        Criteria crtVersion = getSessionFactory().getCurrentSession().createCriteria(ValueSetVersion.class);
        if (valueSetVersionName != null) {
            crtVersion.add(Restrictions.eq(ValueSetVersion.AT_NAME, valueSetVersionName));
        } else {
            crtVersion.add(Restrictions.ilike(TsamDao.AT_STATUS, CURRENT_STATUS));
        }
        Criteria crtVS = crtVersion.createCriteria(ValueSetVersion.AT_VALUE_SET).add(Restrictions.eq(TsamDao.AT_OID, valueSetOid));

        ValueSetVersion version = (ValueSetVersion) crtVersion.uniqueResult();
        if (version == null) {
            return new ArrayList<RetrievedConcept>();
        }
        Criteria crt = getSessionFactory().getCurrentSession().createCriteria(CodeSystemConcept.class);
        crt.createCriteria(CodeSystemConcept.AT_VS_VERSIONS).add(Restrictions.idEq(version.getId()));
        crt.addOrder(Order.asc("id"));
        List<CodeSystemConcept> lc = crt.list();

        Criteria crtd = getSessionFactory().getCurrentSession().createCriteria(Designation.class);
        crt = crtd.createCriteria("concept");
        crt.createCriteria(CodeSystemConcept.AT_VS_VERSIONS).add(Restrictions.idEq(version.getId()));
        crt.addOrder(Order.asc("id"));
        Criteria designCriteria = crtd.add(Restrictions.like(Designation.AT_LANGUAGE, language, MatchMode.START));
        List<Designation> ld = crtd.list();

        /* SELECT *
         FROM
         (SELECT codesystem0_.id idd,
         codesystem0_.code,
         codesystem0_.status
         FROM CODE_SYSTEM_CONCEPT codesystem0_
         LEFT OUTER JOIN X_CONCEPT_VALUE_SET valuesetve1_
         ON codesystem0_.ID=valuesetve1_.CODE_SYSTEM_CONCEPT_ID
         LEFT OUTER JOIN VALUE_SET_VERSION valuesetve2_
         ON valuesetve1_.VALUE_SET_VERSION_ID=valuesetve2_.ID
         WHERE valuesetve2_.ID=81
         )
         LEFT JOIN
         (SELECT codesystem0_.id idd2,
         designation.*
         FROM CODE_SYSTEM_CONCEPT codesystem0_
         LEFT OUTER JOIN X_CONCEPT_VALUE_SET valuesetve1_
         ON codesystem0_.ID=valuesetve1_.CODE_SYSTEM_CONCEPT_ID
         LEFT OUTER JOIN VALUE_SET_VERSION valuesetve2_
         ON valuesetve1_.VALUE_SET_VERSION_ID=valuesetve2_.ID
         LEFT JOIN designation
         ON designation.code_system_concept_id = codesystem0_.id
         WHERE valuesetve2_.ID=81
         AND designation.language_code LIKE 'en%'
         AND designation.status LIKE 'current'
         ) ON idd=idd2
         */

        result = new ArrayList<RetrievedConcept>(lc.size());
        RetrievedConcept retrievedConcept;

        long id1;
        long id2;
        int j = 0;
        for (int i = 0; i < lc.size(); i++) {
            CodeSystemConcept concept = lc.get(i);
            retrievedConcept = new RetrievedConcept(concept);
            result.add(retrievedConcept);
            id1 = concept.getId();
            while (j < ld.size()) {
                Designation designation = ld.get(j);
                id2 = designation.getConcept().getId();
                if (id1 < id2) {
                    break;
                } else if (id1 == id2) {
                    retrievedConcept.setLanguage(designation.getLanguageCode());
                    retrievedConcept.setDesignation(designation.getDesignation());
                    j++;
                    break;
                } else {
                    j++;
                }
            }
        }

        return result;
    }

    public List<String> getLtrLanguages() {
        final List<String> result = new ArrayList<>();
        Criteria crt;
        Iterator<String> iterator;

        //crt = getSession().createCriteria(Designation.class);
        crt = getSessionFactory().getCurrentSession().createCriteria(Designation.class);
        crt.setProjection(Projections.distinct(Projections.property(Designation.AT_LANGUAGE)));
        iterator = crt.list().iterator();

        while (iterator.hasNext()) {
            result.add(iterator.next());
        }

        return result;
    }

    public TsamConfiguration getConfig() {
        return config;
    }

    public void setConfig(TsamConfiguration config) {
        this.config = config;
    }
}
