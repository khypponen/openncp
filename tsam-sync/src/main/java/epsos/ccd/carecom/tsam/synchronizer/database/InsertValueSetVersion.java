package epsos.ccd.carecom.tsam.synchronizer.database;

import java.util.Date;

import org.hibernate.classic.Session;

import epsos.ccd.carecom.tsam.synchronizer.database.model.Constants;
import epsos.ccd.carecom.tsam.synchronizer.database.model.ValueSetEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.ValueSetVersionEntity;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.ValueSet;

/**
 * Encapsulates a Hibernate unit of work that persists a value set version entity.
 */
public class InsertValueSetVersion extends HibernateUnitOfWork {
	
	private final ValueSet valueSet;
	
	/**
	 * Default constructor.
	 * @param valueSet A source value set object that holds the data that may be persisted.
	 * @param isDebug Indicates if the unit of work should be executed in debug mode.
	 * @param truncateStrings Indicates if string values should be truncated.
	 */
	public InsertValueSetVersion(ValueSet valueSet, boolean isDebug, boolean truncateStrings) {
		super(isDebug, truncateStrings);
		this.valueSet = valueSet;
	}

	@Override
	protected int unitOfWork(Session session) {
		ValueSetVersionEntity valueSetVersionEntity = (ValueSetVersionEntity) session.get(ValueSetVersionEntity.class, this.valueSet.getId());
		
		if (valueSetVersionEntity == null) {
			valueSetVersionEntity = new ValueSetVersionEntity();
			
			registerEntity(valueSetVersionEntity);

			valueSetVersionEntity.setId(this.valueSet.getId());
			
			ValueSetEntity valueSetEntity = (ValueSetEntity) session.get(ValueSetEntity.class, this.valueSet.getOriginalId());
			valueSetVersionEntity.setValueSet(valueSetEntity);
			
			valueSetVersionEntity.setVersionName(safeString(
					"versionName", this.valueSet.getVersionName(), "", true));
			
			if (this.valueSet.getEffectiveDate() != null) {
				valueSetVersionEntity.setEffectiveDate(this.valueSet
						.getEffectiveDate().toGregorianCalendar().getTime());
			}
			valueSetVersionEntity.setReleaseDate(safeDate(
					this.valueSet.getReleaseDate(), new Date()));
			
			valueSetVersionEntity.setStatusDate(safeDate(
					this.valueSet.getStatusDate(), new Date()));
			
			valueSetVersionEntity.setStatus(safeStringEnum(
					Constants.MAP_VALUE_SET_VERSION, this.valueSet.getStatus(),
					""));
			
			session.save(valueSetVersionEntity);
			
			return 1;
		}
		if (this.valueSet.getStatusDate() != null) {
			if (this.valueSet.getStatusDate().toGregorianCalendar().getTime().compareTo(valueSetVersionEntity.getStatusDate()) > 0) {
				registerEntity(valueSetVersionEntity);
				valueSetVersionEntity.setStatusDate(this.valueSet.getStatusDate().toGregorianCalendar().getTime());
				valueSetVersionEntity.setStatus(safeStringEnum(Constants.MAP_VALUE_SET_VERSION, this.valueSet.getStatus(), ""));
				session.update(valueSetVersionEntity);
				return 1;
			}
		}
		// Code System Version is a duplicate exists no need for insertion.
		logDuplicateRecord(
				ValueSetVersionEntity.class.getName(),this.valueSet.getId());
		
		return 0;
	}
}
