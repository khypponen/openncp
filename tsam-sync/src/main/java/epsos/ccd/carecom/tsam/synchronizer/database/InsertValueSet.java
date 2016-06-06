package epsos.ccd.carecom.tsam.synchronizer.database;

import org.hibernate.classic.Session;

import epsos.ccd.carecom.tsam.synchronizer.database.model.CodeSystemEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.ValueSetEntity;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.ValueSet;

/**
 * Encapsulates a Hibernate unit of work that persists a value set entity.
 */
public class InsertValueSet extends HibernateUnitOfWork {
	private final ValueSet valueSet;
	
	/**
	 * Default constructor.
	 * @param valueSet A source value set object that holds the data that may be persisted.
	 * @param isDebug Indicates if the unit of work should be executed in debug mode.
	 * @param truncateStrings Indicates if string values should be truncated.
	 */
	public InsertValueSet(ValueSet valueSet, boolean isDebug, boolean truncateStrings) {
		super(isDebug, truncateStrings);
		this.valueSet = valueSet;
	}
	
	@Override
	protected int unitOfWork(Session session) {
		ValueSetEntity valueSetEntity = (ValueSetEntity) session.get(ValueSetEntity.class, this.valueSet.getOriginalId());
		
		if (valueSetEntity == null) {
			valueSetEntity = new ValueSetEntity();
			
			registerEntity(valueSetEntity);

			valueSetEntity.setId(this.valueSet.getOriginalId());
			valueSetEntity.setOid(this.valueSet.getOid());
			valueSetEntity.setEpsosName(this.valueSet.getName());
			CodeSystemEntity parentCodeSystem = (CodeSystemEntity) session.get(
					CodeSystemEntity.class, this.valueSet.getCodeSystemId());
			valueSetEntity.setParentCodeSystem(parentCodeSystem);
			session.save(valueSetEntity);
			
			return 1;
		}
		// Code System exists no need for insertion.
		logDuplicateRecord(ValueSetEntity.class.getName(), this.valueSet.getId());
		
		return 0;
	}

}
