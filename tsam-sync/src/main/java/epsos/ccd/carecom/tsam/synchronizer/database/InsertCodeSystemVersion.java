package epsos.ccd.carecom.tsam.synchronizer.database;

import java.util.Date;

import org.hibernate.classic.Session;

import epsos.ccd.carecom.tsam.synchronizer.database.model.CodeSystemEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.CodeSystemVersionEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.Constants;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.CodeSystem;

/**
 * Encapsulates a Hibernate unit of work that persists a code system version entity.
 */
public class InsertCodeSystemVersion extends HibernateUnitOfWork {
	
	private CodeSystem codeSystem;
	
	/**
	 * Default constructor.
	 * @param codeSystem A source code system object that holds the data that may be persisted. 
	 * @param isDebug Indicates if the unit of work should be executed in debug mode.
	 * @param truncateStrings Indicates if string values should be truncated.
	 */
	public InsertCodeSystemVersion(CodeSystem codeSystem, boolean isDebug, boolean truncateStrings) {
		super(isDebug, truncateStrings);
		this.codeSystem = codeSystem;
	}

	@Override
	protected int unitOfWork(Session session) {
		CodeSystemVersionEntity codeSystemVersionEntity = 
			(CodeSystemVersionEntity) session.get(
					CodeSystemVersionEntity.class,
					safeLong(this.codeSystem.getVersionId(), this.codeSystem.getId()));
		
		// This test assumes that if the status and status date has changed so is the ID of the version.
		if (codeSystemVersionEntity == null) {
			// Create new version
			codeSystemVersionEntity = new CodeSystemVersionEntity();
			
			registerEntity(codeSystemVersionEntity);

			codeSystemVersionEntity.setId(safeLong(
					this.codeSystem.getVersionId(), this.codeSystem.getId()));
			codeSystemVersionEntity.setLocalName(safeString(
					"localName", this.codeSystem.getVersionName(), "", true));
			
			CodeSystemEntity codeSystemEntity = (CodeSystemEntity) session.get(CodeSystemEntity.class,this.codeSystem.getId());
			
			codeSystemVersionEntity.setCodeSystem(codeSystemEntity);
			if (this.codeSystem.getPreviousVersionId() != null) {
				CodeSystemVersionEntity prevCodeSystemVersionEntity = (CodeSystemVersionEntity) session
						.get(CodeSystemVersionEntity.class,
								this.codeSystem.getPreviousVersionId());
				if (prevCodeSystemVersionEntity != null)
					codeSystemVersionEntity
							.setPreviousVersion(prevCodeSystemVersionEntity);
			}
			if (this.codeSystem.getEffectiveDate() != null) {
				codeSystemVersionEntity.setEffectiveDate(this.codeSystem
						.getEffectiveDate().toGregorianCalendar().getTime());
			}
			codeSystemVersionEntity.setReleaseDate(safeDate(
					this.codeSystem.getReleaseDate(), new Date()));
			codeSystemVersionEntity.setStatus(safeStringEnum(
					Constants.MAP_CODE_SYSTEM_VERSION,
					this.codeSystem.getVersionStatus(), ""));
			codeSystemVersionEntity.setStatusDate(safeDate(
					this.codeSystem.getStatusDate(), new Date()));

			session.save(codeSystemVersionEntity);
			
			return 1;
		}
		if (this.codeSystem.getStatusDate() != null) {
			if (this.codeSystem.getStatusDate().toGregorianCalendar().getTime().compareTo(codeSystemVersionEntity.getStatusDate()) > 0) {
				registerEntity(codeSystemVersionEntity);
				codeSystemVersionEntity.setStatusDate(this.codeSystem.getStatusDate().toGregorianCalendar().getTime());
				codeSystemVersionEntity.setStatus(safeStringEnum(Constants.MAP_CODE_SYSTEM_VERSION, this.codeSystem.getVersionStatus(), ""));
				session.update(codeSystemVersionEntity);
				return 1;
			}
		}
		// Code System Version is a duplicate exists no need for insertion.
		logDuplicateRecord(
				CodeSystemVersionEntity.class.getName(), safeLong(this.codeSystem.getVersionId(), this.codeSystem.getId()));
		return 0;
	}

}
