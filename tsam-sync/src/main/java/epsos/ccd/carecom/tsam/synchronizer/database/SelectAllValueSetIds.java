package epsos.ccd.carecom.tsam.synchronizer.database;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

/**
 * Encapsulates a Hibernate unit of work that fetches a persisted list of value set IDs.
 */
public class SelectAllValueSetIds extends HibernateUnitOfWork {
	
	private List<Long> valueSetIds;
	
	/**
	 * Default constructor.
	 * @param isDebug Indicates if the unit of work should be executed in debug mode.
	 * @param truncateLongStrings Indicates if string values should be truncated.
	 */
	public SelectAllValueSetIds(boolean isDebug, boolean truncateLongStrings) {
		super(isDebug, truncateLongStrings);
	}

	@Override
	protected int unitOfWork(Session session) {
		Query selectQuery = session.createQuery("select id from ValueSetEntity");
		List<?> temp = selectQuery.list();
		this.valueSetIds = new ArrayList<Long>();
		
		// If any unexpected cast errors occur it will be thrown by this piece of code.
		for (Object id : temp) {
			this.valueSetIds.add((Long)id);
		}
		return 0;
	}
	
	/**
	 * @return A list of value set IDs fetched by the {@link #unitOfWork(Session)} method.
	 */
	public List<Long> getValueSetIds() {
		return this.valueSetIds;
	}
}
