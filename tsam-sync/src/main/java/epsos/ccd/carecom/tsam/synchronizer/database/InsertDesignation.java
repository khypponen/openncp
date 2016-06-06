package epsos.ccd.carecom.tsam.synchronizer.database;

import java.util.Date;

import org.hibernate.classic.Session;

import epsos.ccd.carecom.tsam.synchronizer.database.model.CodeSystemConceptEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.Constants;
import epsos.ccd.carecom.tsam.synchronizer.database.model.DesignationEntity;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.Designation;

/**
 * Encapsulates a Hibernate unit of work that persists a designation entity.
 */
public class InsertDesignation extends HibernateUnitOfWork {
	
	private Designation designation;
	
	/**
	 * Default constructor
	 * @param designation A source designation object that holds the data that may be persisted.
	 * @param isDebug Indicates if the unit of work should be executed in debug mode.
	 * @param truncateStrings Indicates if string values should be truncated.
	 */
	public InsertDesignation(Designation designation, boolean isDebug, boolean truncateStrings) {
		super(isDebug, truncateStrings);
		this.designation = designation;
	}
	
	@Override
	protected int unitOfWork(Session session) {
		DesignationEntity designationEntity = (DesignationEntity) session.get(DesignationEntity.class, this.designation.getId());
		
		// We cannot count on that the concept that the designation refers to already exists in the LTR, so we only insert 
		// a designation iff the concept is found in LTR
		CodeSystemConceptEntity codeSystemConceptEntity = (CodeSystemConceptEntity) session.get(CodeSystemConceptEntity.class,
				this.designation.getConceptId());
		
		if (codeSystemConceptEntity == null) {
			logEntityDoesNotExist("Designation", this.designation.getId(), "Concept", this.designation.getConceptId());
			return 0;
		}
		
		if (designationEntity == null) {
			designationEntity = new DesignationEntity();
			
			registerEntity(designationEntity);
			
			designationEntity.setId(this.designation.getId());
			designationEntity.setDesignation(safeString(
					"designation", this.designation.getValue(), "", true));
			designationEntity.setLanguageCode(safeString(
					"languageCode", this.designation.getLanguageCode(), "", true));
			designationEntity.setType(safeStringEnum(
					Constants.MAP_DESIGNATION_TYPE, this.designation.getType(),
					""));
			if (Constants.VALUE_PREFERRED_TERM.equals(this.designation.getType())) {
				designationEntity.setIsPreferred(Boolean.TRUE);
			}
			designationEntity
					.setStatus(safeStringEnum(Constants.MAP_DESIGNATION,
							this.designation.getStatus(), ""));
			designationEntity.setStatusDate(safeDate(
					this.designation.getStatusDate(), new Date()));

			// We could pass a code system concept entity as an argument, but it does not seem to save a select query to
			// the database any way.
			designationEntity.setCodeSystemConceptId(codeSystemConceptEntity);
			session.save(designationEntity);
			
			return 1;
		}
		if (this.designation.getStatusDate() != null) {
			if (this.designation.getStatusDate().toGregorianCalendar().getTime().compareTo(designationEntity.getStatusDate()) > 0) {
				registerEntity(designationEntity);
				designationEntity.setStatusDate(this.designation.getStatusDate().toGregorianCalendar().getTime());
				designationEntity.setStatus(safeStringEnum(Constants.MAP_DESIGNATION, this.designation.getStatus(), ""));
				session.update(designationEntity);
				return 1;
			}
		}
		// Designation exists no need for insertion.
		logDuplicateRecord(DesignationEntity.class.getName(), this.designation.getId());
		
		return 0;
	}

}
