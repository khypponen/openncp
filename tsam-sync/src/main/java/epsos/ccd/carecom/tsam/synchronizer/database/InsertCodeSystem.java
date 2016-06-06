package epsos.ccd.carecom.tsam.synchronizer.database;

import org.hibernate.classic.Session;

import epsos.ccd.carecom.tsam.synchronizer.database.model.CodeSystemEntity;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.CodeSystem;

/**
 * Encapsulates a Hibernate unit of work that persists a code system entity.
 */
public class InsertCodeSystem extends HibernateUnitOfWork {
	private final CodeSystem codeSystem;
	
	/**
	 * Default constructor.
	 * @param codeSystem A source code system object that holds the data that may be persisted.
	 * @param isDebug Indicates if the unit of work should be executed in debug mode.
	 * @param truncateStrings Indicates if string values should be truncated.
	 */
	public InsertCodeSystem(CodeSystem codeSystem, boolean isDebug, boolean truncateStrings) {
		super(isDebug, truncateStrings);
		this.codeSystem = codeSystem;
	}
	
	@Override
	protected int unitOfWork(Session session) {
		CodeSystemEntity codeSystemEntity = (CodeSystemEntity) session.get(CodeSystemEntity.class, this.codeSystem.getId());
		
		if (codeSystemEntity == null) {
			// New Code System, insert it into local repository.
			codeSystemEntity = new CodeSystemEntity();
			
			registerEntity(codeSystemEntity);

			codeSystemEntity.setId(this.codeSystem.getId());
			codeSystemEntity.setName(safeString("name", this.codeSystem.getName(), "", true));
			codeSystemEntity.setOid(this.codeSystem.getOid());
			codeSystemEntity.setDescription(safeString(
					"description", this.codeSystem.getDescription(), "", true));
			session.save(codeSystemEntity);

			return 1;
		}
		// Code System exists no need for insertion.
		logDuplicateRecord(CodeSystemEntity.class.getName(), this.codeSystem.getId());
		return 0;
	}

}
