package epsos.ccd.carecom.tsam.synchronizer.database;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import epsos.ccd.carecom.tsam.synchronizer.MissingSystemPropertyException;
import epsos.ccd.carecom.tsam.synchronizer.database.model.CodeSystemConceptEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.CodeSystemEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.CodeSystemVersionEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.DesignationEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.TranscodingAssociationEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.ValueSetEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.ValueSetVersionEntity;

/**
 * Encapsulates the creation of a <i>Hibernate</i> session factory.
 */
public class HibernateSessionFactory {
	
	private static final String DEFAULT_SETTING_HIBERNATEFILE = "dk.carecom.cts2.sync.hibernatefile";
	
	private static SessionFactory uniqueInstance;
	
	private static SessionFactory buildSessionFactory() throws HibernateException {
		String configFile = System.getProperty(DEFAULT_SETTING_HIBERNATEFILE);
		if (configFile == null) {
			throw new MissingSystemPropertyException(DEFAULT_SETTING_HIBERNATEFILE);
		}
		
		File cfgFile = new File(configFile);
		
		try {
			return new AnnotationConfiguration().configure(cfgFile)
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
	public static SessionFactory getInstance() throws HibernateException {
		if (uniqueInstance == null) {
			uniqueInstance = buildSessionFactory();
		}
		return uniqueInstance;
	}
}

