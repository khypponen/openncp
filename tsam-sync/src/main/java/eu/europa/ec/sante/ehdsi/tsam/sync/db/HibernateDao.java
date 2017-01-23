package eu.europa.ec.sante.ehdsi.tsam.sync.db;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Repository
public class HibernateDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateDao(SessionFactory sessionFactory) {
        Assert.notNull(sessionFactory, "SessionFactory must not be null");
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void save(Object entity) {
        sessionFactory.getCurrentSession().save(entity);
    }

    @Transactional
    public void saveAll(List<?> entities) {
        for (Object entity : entities) {
            save(entity);
        }
    }
}
