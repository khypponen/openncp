package epsos.ccd.carecom.tsam.synchronizer.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.hibernate.classic.Session;

import epsos.ccd.carecom.tsam.synchronizer.database.model.CodeSystemConceptEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.CodeSystemVersionEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.Constants;
import epsos.ccd.carecom.tsam.synchronizer.database.model.ValueSetVersionEntity;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.CodeSystemConcept;

/**
 * Encapsulates a Hibernate unit of work that persists a code system concept entity.
 */
public class InsertConcept extends HibernateUnitOfWork {
	private Long codeSystemVersionId;
	private Long valueSetVersionId;
	private CodeSystemConcept concept;
	
	/**
	 * Default constructor.
	 * @param codeSystemVersionId ID of the code system version that the concept relates to.
	 * @param valueSetVersionId ID of the value set version that the concept relates to.
	 * @param concept A source code system concept object that holds the data that may be persisted. 
	 * @param isDebug Indicates if the unit of work should be executed in debug mode.
	 * @param truncateStrings Indicates if string values should be truncated.
	 */
	// TODO: Is it even necessary to pass the code system version id, because the concept object holds the value as well.
	public InsertConcept(Long codeSystemVersionId, 
			Long valueSetVersionId, 
			CodeSystemConcept concept,
			boolean isDebug, boolean truncateStrings) {
		super(isDebug, truncateStrings);
		this.codeSystemVersionId = codeSystemVersionId;
		this.valueSetVersionId = valueSetVersionId;
		this.concept = concept;
	}
	
	@Override
	protected int unitOfWork(Session session) {
		CodeSystemConceptEntity conceptEntity = (CodeSystemConceptEntity) session.get(CodeSystemConceptEntity.class, this.concept.getId());
		Collection<ValueSetVersionEntity> listOfValueSetVersions = null;
		ValueSetVersionEntity valueSetVersionEntity = (ValueSetVersionEntity) session.get(ValueSetVersionEntity.class, this.valueSetVersionId);
		int result = 0;
		if (conceptEntity == null) {
			conceptEntity = new CodeSystemConceptEntity();

			registerEntity(conceptEntity);
			
			conceptEntity.setId(this.concept.getId());
			conceptEntity.setCode(safeString("code", this.concept.getExternalCode(), "", true));
			conceptEntity.setDefinition(safeString("definition", this.concept
							.getFullySpecifiedName(), "", true));
			conceptEntity.setStatus(safeStringEnum(Constants.MAP_CONCEPT,
					this.concept.getStatus(), ""));
			conceptEntity.setStatusDate(safeDate(this.concept.getStatusDate(), new Date()));

			CodeSystemVersionEntity codeSystemVersionEntity = (CodeSystemVersionEntity) session
						.get(CodeSystemVersionEntity.class,
								this.codeSystemVersionId);
			conceptEntity.setCodeSystemVersion(codeSystemVersionEntity);
			listOfValueSetVersions = new ArrayList<ValueSetVersionEntity>(1);
			
			result = 1;
		} else {
			registerEntity(conceptEntity);
			if (this.concept.getStatusDate() != null) {
				if (this.concept.getStatusDate().toGregorianCalendar().getTime().compareTo(conceptEntity.getStatusDate()) > 0) {
					conceptEntity.setStatusDate(this.concept.getStatusDate().toGregorianCalendar().getTime());
					conceptEntity.setStatus(safeStringEnum(Constants.MAP_CONCEPT, this.concept.getStatus(), ""));
					result = 1;
				}
			}

			listOfValueSetVersions = conceptEntity.getValueSetVersions();
			logDuplicateRecord(CodeSystemConceptEntity.class.getName(), this.concept.getId());
		}
		if (!listOfValueSetVersions.contains(valueSetVersionEntity)) {
			listOfValueSetVersions.add(valueSetVersionEntity);
			conceptEntity.setValueSetVersions(listOfValueSetVersions);
		}
		
		
		session.saveOrUpdate(conceptEntity);
		
		return result;
	}
}
