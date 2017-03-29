package fi.kela.se.epsos.data.model;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
        
        public Document asXml() {
            Document doc = null;
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                StringBuilder sb = new StringBuilder();
                sb.append("<SearchCriteria>");
                for (Criteria c : criteriaMap.keySet()) {
                    sb.append("<");
                    sb.append(c.name());
                    sb.append(">");
                    sb.append(criteriaMap.get(c));
                    sb.append("</");
                    sb.append(c.name());
                    sb.append(">");
                }
                sb.append("</SearchCriteria>");
                doc = builder.parse(new InputSource(new StringReader(sb.toString())));
                return doc;
            } catch (SAXException ex) {
                Logger.getLogger(SearchCriteriaImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SearchCriteriaImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(SearchCriteriaImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return doc;
        }
}
