package epsos.ccd.carecom.tsam.synchronizer.database;

import epsos.ccd.carecom.tsam.synchronizer.ApplicationController;
import epsos.ccd.carecom.tsam.synchronizer.database.model.Constants;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import javax.persistence.Column;
import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

/**
 * This is a base class that encapsulates a hibernate unit of work.
 */
public abstract class HibernateUnitOfWork {

    /**
     * Holds registered entities.
     */
    private Object registeredEntity;

    /**
     * If true then the unit of work is running in debug mode. This variable is
     * used to decide if the executed business logic is more robust towards unexpected
     * and unwanted values.
     */
    protected boolean isDebug = false;

    /**
     * Indicates if the unit of work should attempt to truncate to long strings.
     */
    protected boolean truncateLongStrings = false;

    /**
     * Default constructor.
     *
     * @param isDebug             Indicates if the unit of work is executed in debug mode.
     * @param truncateLongStrings Indicates if to long strings should be truncated.
     */
    protected HibernateUnitOfWork(boolean isDebug, boolean truncateLongStrings) {
        this.isDebug = isDebug;
        this.truncateLongStrings = truncateLongStrings;
    }

    /**
     * Implements a hibernate unit of work and handles any <code>org.hibernate.HibernateException</code> that is thrown
     * in either this method or <code>unitOfWork</code>. Other exception types are not handled by this method.
     *
     * @return The number of records that where persisted.
     * @see unitOfWork()
     */
    public int executeUnitOfWork() {
        Session session = null;
        int result = 0;
        try {
            session = HibernateSessionFactory.getInstance().openSession();
            session.beginTransaction();

            result = unitOfWork(session);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            // Print error debugging info:
            if (this.registeredEntity != null) {
                ApplicationController.LOG.error(this.registeredEntity.toString());
            }
            ApplicationController.handleSevereError(e, "Execution of Hibernate Unit of work failed.");
        } finally {
            if (session != null && session.isOpen()) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    // This exception is not severe, so we can try to continue.
                    // But a warning is issued because this is unwanted behavior.
                    ApplicationController.LOG.warn("Could not close Hibernate Session.", e);
                }
            }
        }
        return result;
    }

    /**
     * Registers an entity object that is used in the unit of work.
     *
     * @param entity The entity to register.
     */
    protected void registerEntity(Object entity) {
        this.registeredEntity = entity;
    }

    /**
     * If in debug mode and the argument value is null then a default value is returned.
     * Else if the value is null an <code>org.hibernate.HibernateException</code> is thrown.
     * If the value is not null then it is simply returned.
     *
     * @param fieldName      Name of the field.
     * @param value          The value to evaluate.
     * @param defaultValue   Default value to return.
     * @param throwException Indicates if an exception should be thrown if the value is null.
     * @return Either the argument value or a default value.
     */
    protected String safeString(String fieldName, String value, String defaultValue, boolean throwException) {
        String safeValue = safeObject(value, defaultValue, throwException);
        int defaultLength = Constants.DEFAULT_CHARACTER_LENGTH;
        // The code below extracts the number of characters that are allowed in the value string argument.
        // The code is based on reflection, which may be slow. We should consider implementing a UserType
        // that handles the truncating instead.
        if (this.truncateLongStrings && safeValue != null) {
            if (this.registeredEntity != null) {
                Field field = null;
                try {
                    field = this.registeredEntity.getClass().getDeclaredField(fieldName);
                } catch (SecurityException e) {
                    // Do nothing use default length
                } catch (NoSuchFieldException e) {
                    // Do nothing use default length
                }
                if (field != null) {
                    try {
                        Column columnAnnotation = field.getAnnotation(Column.class);
                        defaultLength = columnAnnotation.length();
                    } catch (NullPointerException e) {
                        // Do nothing use default length
                    }
                } else {
                    // Do nothing use default length
                }
            }
            if (safeValue.length() > defaultLength) {
                logTruncateString(
                        this.registeredEntity.getClass().getName(), fieldName, safeValue.length(), defaultLength);
                safeValue = safeValue.substring(0, defaultLength - 1);
            }
        }
        return safeValue;
    }

    /**
     * If in debug mode and the argument value is null then a default value is returned.
     * Else if the value is null an <code>org.hibernate.HibernateException</code> is thrown.
     * If the value is not null then it is simply returned.
     *
     * @param value        The value to evaluate.
     * @param defaultValue Default value.
     * @return Either the argument value or a default value.
     */
    protected Date safeDate(XMLGregorianCalendar value, Date defaultValue) {
        if (value == null && this.isDebug) {
            return defaultValue;
        }
        if (value == null) {
            throw new HibernateException("Unexpected null pointer detected.");
        }
        return value.toGregorianCalendar().getTime();
    }

    /**
     * If in debug mode and any of the arguments or resulting value is null then a default
     * value is returned.
     * Else if the mapping or key is null, or if the key is not recognized, or if the value
     * in the mapping is null an <code>org.hibernate.HibernateException</code> is thrown.
     * If all the not null conditions are met the resulting value is returned.
     *
     * @param mapping      The mapping from which the value will be retrieved.
     * @param key          The key used to retrieve the value from the mapping.
     * @param defaultValue default value.
     * @return Either the retrieved or a default value.
     */
    protected String safeStringEnum(Map<Integer, String> mapping, Integer key, String defaultValue) {
        if ((key == null || mapping == null) && this.isDebug) {
            return defaultValue;
        }
        if (mapping == null) {
            throw new HibernateException("Mapping object is a null pointer");
        }
        if (!mapping.containsKey(key) && this.isDebug) {
            return defaultValue;
        }
        if (mapping.get(key) == null && this.isDebug) {
            return defaultValue;
        }
        if (key == null) {
            throw new HibernateException("Integer key is a null pointer");
        }
        if (!mapping.containsKey(key)) {
            throw new HibernateException("Integer key could not be recognized");
        }
        if (mapping.get(key) == null) {
            throw new HibernateException("Mapped value is a null pointer");
        }
        return mapping.get(key);
    }

    /**
     * If in debug mode and the argument value is null then a default value is returned.
     * Else if the value is null an <code>org.hibernate.HibernateException</code> is thrown.
     * If the value is not null then it is simply returned.
     *
     * @param value        The value to evaluate.
     * @param defaultValue Default value.
     * @return Either the argument value or a default value.
     */
    protected Long safeLong(Long value, Long defaultValue) {
        return safeObject(value, defaultValue, true);
    }

    /**
     * If in debug mode and the argument value is null then a default value is returned.
     * Else if the value is null an <code>org.hibernate.HibernateException</code> is thrown.
     * If the value is not null then it is simply returned.
     *
     * @param value          Object to evaluate.
     * @param defaultValue   Default value.
     * @param throwException Indicates if an exception should be thrown instead of returning a default value.
     * @return Either the argument value or a default value.
     */
    protected <T> T safeObject(T value, T defaultValue, boolean throwException) {
        if (value == null) {
            if (throwException && !this.isDebug) {
                throw new HibernateException("Unexpected null pointer detected");
            }
            return defaultValue;
        }
        return value;
    }

    /**
     * Logs an occurrence of a duplicate record.
     *
     * @param objectName Name of the object that represents the record.
     * @param objectId   The ID of the record.
     */
    protected void logDuplicateRecord(String objectName, Long objectId) {
        if (ApplicationController.LOG.isDebugEnabled()) {
            ApplicationController.LOG.debug(
                    "Insert of " + objectName + " with ID=" + objectId + " skipped, because it is already present in the local repository.");
        }
    }

    protected void logEntityDoesNotExist(String insertEntityType,
                                         Long insertObjectId,
                                         String missingEntityType,
                                         Long missingObjectId) {
        if (ApplicationController.LOG.isDebugEnabled()) {
            ApplicationController.LOG.debug(
                    "Insert of " + insertEntityType + " with ID="
                            + insertObjectId + " skipped, because " + missingEntityType + " with ID="
                            + missingObjectId + " is not present in the local repository.");
        }
    }

    /**
     * Logs truncation of a string.
     *
     * @param objectName           Name of the object which field has been truncated.
     * @param fieldName            Name of the field that is truncated.
     * @param originalFieldLength  The original length of the field value.
     * @param truncatedFieldLemgth The truncated length of the field value.
     */
    protected void logTruncateString(String objectName, String fieldName, int originalFieldLength, int truncatedFieldLemgth) {
        if (ApplicationController.LOG.isInfoEnabled()) {
            ApplicationController.LOG.info(
                    String.format(
                            "Truncated %s.%s from %d to %d.",
                            objectName,
                            fieldName,
                            Integer.valueOf(originalFieldLength),
                            Integer.valueOf(truncatedFieldLemgth)
                    ));
        }
    }

    /**
     * Any sub class has to implement its persistence logic in this method. The only exceptions that can be throw in this
     * method should be of type <code>org.hibernate.HibernateException</code>.
     *
     * @param session Hibernate session used for the unit of work.
     * @return number of records inserted or updated.
     */
    protected abstract int unitOfWork(Session session);
}
