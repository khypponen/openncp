package epsos.ccd.carecom.tsam.synchronizer.database;

import epsos.ccd.carecom.tsam.synchronizer.MissingSystemPropertyException;
import epsos.ccd.carecom.tsam.synchronizer.database.model.*;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

/**
 * Encapsulates the creation of a <i>Hibernate</i> session factory.
 */
public class HibernateSessionFactory {

    private static final String DEFAULT_SETTING_HIBERNATEFILE = "dk.carecom.cts2.sync.hibernatefile";

    private static SessionFactory uniqueInstance;

    private HibernateSessionFactory() {
    }

    private static SessionFactory buildSessionFactory() {
        String configFile = System.getProperty(DEFAULT_SETTING_HIBERNATEFILE);
        if (configFile == null) {
            throw new MissingSystemPropertyException(DEFAULT_SETTING_HIBERNATEFILE);
        }

        File cfgFile = new File(configFile);

        try {
            return new Configuration().configure(cfgFile)
                    .addAnnotatedClass(CodeSystemEntity.class)
                    .addAnnotatedClass(CodeSystemVersionEntity.class)
                    .addAnnotatedClass(ValueSetEntity.class)
                    .addAnnotatedClass(ValueSetVersionEntity.class)
                    .addAnnotatedClass(CodeSystemConceptEntity.class)
                    .addAnnotatedClass(DesignationEntity.class)
                    .addAnnotatedClass(TranscodingAssociationEntity.class)
                    .buildSessionFactory();
        } catch (HibernateException e) {
            throw e;
        }
    }

    /**
     * @return A unique instance of a Hibernate session factory.
     * @throws HibernateException re-thrown exception from the Hibernate.
     */
    public static SessionFactory getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = buildSessionFactory();
        }
        return uniqueInstance;
    }
}
