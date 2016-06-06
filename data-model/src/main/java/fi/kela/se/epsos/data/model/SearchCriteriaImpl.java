package fi.kela.se.epsos.data.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SearchCriteriaImpl implements SearchCriteria {
	private Map<Criteria, String> criteriaMap = new HashMap<Criteria, String>();
	
	public SearchCriteriaImpl() {
	}

	public SearchCriteria add(Criteria c, String value) {
		if(c != null && value != null) {
			criteriaMap.put(c, value);
		}
		return this;
	}

	public String getCriteriaValue(Criteria c) {
		return criteriaMap.get(c);
	}
	
	public Iterator<Criteria> getSearchCriteriaKeys() {
		return criteriaMap.keySet().iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Criteria c : criteriaMap.keySet()) {
			if(sb.length() == 0) {
				sb.append("SearchCriteria {");
			} else {
				sb.append(", ");
			}
			
			sb.append(c.name() + " = ");
			sb.append(criteriaMap.get(c));
		}
		
		sb.append("}");
		
		return sb.toString();
	}
}
