package epsos.ccd.carecom.tsam.synchronizer.database;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

/**
 * Encapsulates a Hibernate unit of work that fetches persistent list of code system concept IDs.
 */
public class SelectConceptIdsByValueSetId extends HibernateUnitOfWork {
	private Long valueSetId;
	private List<Long> conceptIds;
	
	/**
	 * Default constructor.
	 * @param isDebug Indicates if the unit of work should be executed in debug mode.
	 * @param truncateLongStrings Indicates if string values should be truncated.
	 * @param valueSetId The value set ID for which to fetch the concepts.
	 */
	public SelectConceptIdsByValueSetId(boolean isDebug,
			boolean truncateLongStrings,
			Long valueSetId) {
		super(isDebug, truncateLongStrings);
		this.valueSetId = valueSetId;
		this.conceptIds = new ArrayList<Long>();
	}

	@Override
	protected int unitOfWork(Session session) {
		Query selectQuery = session.createQuery(
				"select concepts.id from CodeSystemConceptEntity as concepts inner join concepts.valueSetVersions as versions with versions.valueSet.id=:vs");
		selectQuery.setParameter("vs", this.valueSetId);
		List<?> temp = selectQuery.list();
		
		// If any unexpected cast errors occur it will be thrown by this piece of code.
		for (Object concept : temp) {
			this.conceptIds.add((Long)concept);
		}
		
		return 0;
	}

	/**
	 * @return A list of code system concepts IDs fetched by the {@link #unitOfWork(Session)} method.
	 */
	public List<Long> getConceptIds() {
		return this.conceptIds;
	}

}
