package epsos.ccd.posam.tm.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import epsos.ccd.posam.tm.util.TMConstants;
import epsos.ccd.posam.tsam.exception.ITMTSAMEror;

/**
 * Data transfer class.<br>
 * Contains: 
 * <li>processed (transcoded or translated) CDA document</li>
 * <li>status (succes or failure)</li>
 * <li>list of Errors</li>
 * <li>list of Warnings</li>
 * 
 * <br>
 * Provides set & get method for simple response manipulating.
 * 
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.11, 2010, 20 October
 */

public class TMResponseStructure implements TMConstants {

	/**
	 * Target / response CDA document. Options are: <li>transcoded CDA pivot
	 * document</li> <li>translated CDA document</li> <li>CDA unstructured
	 * document with embedded pdf file</li> <li>In case of ERROR original/input
	 * CDA document</li>
	 */
	private Document responseCDA;

	/**
	 * List of TM Errors
	 */
	private List<ITMTSAMEror> errors;

	/**
	 * List of TM Warnings
	 */
	private List<ITMTSAMEror> warnings;

	/**
	 * failure or success
	 */
	private String status;

	public TMResponseStructure(Document responseCDA, String status,
			List<ITMTSAMEror> errors, List<ITMTSAMEror> warnings) {
		this.responseCDA = responseCDA;
		this.errors = errors;
		this.warnings = warnings;
		this.status = status;
	}

	/**
	 * Creates XML Document presenting Response structure.<br>
	 * Suitable for logging, testing.
	 * 
	 * @return Document - XML presentation of entire ResponseStructure
	 */
	private Document getResponseStructureAsXmlDoc() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

			Element root = (Element) document
					.createElement("responseStructure");
			document.appendChild(root);
			root.appendChild(document.createElement("responseElement"));
			root.appendChild(document.createElement("responseStatus"));

			Node responseElement = document.getElementsByTagName(
					RESPONSE_ELEMENT).item(0);
			Node tempNode = document.importNode(this.responseCDA
					.getDocumentElement(), true);
			responseElement.appendChild(tempNode);

			// write status/ errors/ warnings into responseStatus
			Node responseStatus = document
					.getElementsByTagName(RESPONSE_STATUS).item(0);
			Element status = document.createElement(STATUS);
			status.setAttribute(RESULT, this.status);
			responseStatus.appendChild(status);

			// errors
			Element errorsElement = document.createElement(ERRORS);
			if (!getErrors().isEmpty()) {
				Iterator<ITMTSAMEror> iErrors = uniqueList(getErrors()).iterator();
				while (iErrors.hasNext()) {
					ITMTSAMEror tmException = iErrors.next();
					Element errorElement = document.createElement(ERROR);
					errorElement.setAttribute(CODE, tmException.getCode());
					errorElement.setAttribute(DESCRIPTION, tmException
							.getDescription());
					errorsElement.appendChild(errorElement);
				}
			}
			root.appendChild(errorsElement);

			// warnings
			Element warningsElement = document.createElement(WARNINGS);
			if (!getWarnings().isEmpty()) {
				Iterator<ITMTSAMEror> iErrors = uniqueList(getWarnings()).iterator();
				while (iErrors.hasNext()) {
					ITMTSAMEror tmException = iErrors.next();
					Element warningElement = document.createElement(WARNING);
					warningElement.setAttribute(CODE, tmException.getCode());
					warningElement.setAttribute(DESCRIPTION, tmException
							.getDescription());
					warningsElement.appendChild(warningElement);
				}
			}
			root.appendChild(warningsElement);

			return document;
		} catch (Exception e) {
			throw e;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	private ArrayList<ITMTSAMEror> uniqueList(List<ITMTSAMEror> list){
		ArrayList<ITMTSAMEror> result=new ArrayList<ITMTSAMEror>();
		for (ITMTSAMEror itmtsamEror : list) {
			if(!result.contains(itmtsamEror)) {
				result.add(itmtsamEror);
			}
		}
		return result;
	}

	/**
	 * @return target / response CDA document. Options are: <li>transcoded CDA
	 *         pivot document</li> <li>translated CDA document</li> <li>CDA
	 *         unstructured document with embedded pdf file</li> *
	 */
	public Document getResponseCDA() {
		return responseCDA;
	}

	public void setResponseCDA(Document responseCDA) {
		this.responseCDA = responseCDA;
	}

	/**
	 * @return List of TMErrors
	 */
	public List<ITMTSAMEror> getErrors() {
		return (errors == null ? new ArrayList<ITMTSAMEror>() : errors);
	}

	/**
	 * 
	 * @return List of TMWarnings
	 */
	public List<ITMTSAMEror> getWarnings() {
		return (warnings == null ? new ArrayList<ITMTSAMEror>() : warnings);
	}

	public void setErrors(List<ITMTSAMEror> errors) {
		this.errors = errors;
	}

	public void setWarnings(List<ITMTSAMEror> warnings) {
		this.warnings = warnings;
	}

	public void addError(ITMTSAMEror newError) {
		if (!errors.contains(newError)) {
			errors.add(newError);
		}
	}

	public void addWarning(ITMTSAMEror newWarning) {
		if (!warnings.contains(newWarning)) {
			warnings.add(newWarning);
		}
	}

	/**
	 * 
	 * @return true id status is SUCCESS otherwise false
	 */
	public boolean isStatusSuccess() {
		return (status != null && status.equals(STATUS_SUCCESS));
	}

	/**
	 * Method is used for logging, testing.<br>
	 * (To obtain CDA document use #getResponseCDA() method)<br>
	 * Returns xml presentation of Response Structure in form:
	 * 
	 * <pre>
	 * &lt;responseStructureDoc&gt;
	 * 	  &lt;responseElement&gt;
	 * 	  &lt;!--One of these:--&gt;
	 * 	    &lt;!-- epSOS CDA Pivot Document (result of transcoding in country A)--&gt;
	 * 	    &lt;!-- Translated epSOS CDA Pivot Document (result of translation in country B)--&gt;
	 * 		&lt;!-- epSOS CDA unstructured document with embedded pdf file--&gt;
	 * 	  &lt;/responseElement&gt;
	 * 	  &lt;responseStatus&gt;
	 * 	    &lt;status result="success/failure"/&gt;
	 * 	    &lt;!-- optional --&gt;
	 * 	    &lt;errors&gt;
	 * 	      &lt;error code="..." description=".."/&gt;
	 * 	      &lt;error code="..." description=".."/&gt;
	 * 	    &lt;/errors&gt;
	 * 	    &lt;!-- optional --&gt;
	 * 	    &lt;warnings&gt;
	 * 	      &lt;warning code="..." description=".."/&gt;
	 * 	      &lt;warning code="..." description=".."/&gt;
	 * 	    &lt;/warnings&gt;
	 * 	  &lt;/responseStatus&gt;
	 * 	&lt;/responseStructureDoc&gt;
	 * </pre>
	 * 
	 * @see #getResponseCDA()
	 * @return Document - xml presentation of TMResponseStructure
	 */
	public Document getDocument() throws Exception {
		return getResponseStructureAsXmlDoc();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("TMResponseStructure.toString() BEGIN : ").append(NEWLINE);
//		sb.append(DOCUMENT).append(NEWLINE);
//		sb.append(XmlUtil.xmlToString(getResponseCDA()));
//		sb.append(NEWLINE);
		sb.append(STATUS).append(COLON).append(status).append(NEWLINE);
		sb.append(ERRORS).append(COLON).append(NEWLINE);
		for (Iterator<ITMTSAMEror> iterator = errors.iterator(); iterator.hasNext();) {
			ITMTSAMEror tmError = (ITMTSAMEror) iterator.next();
			sb.append(tmError.toString()).append(NEWLINE);
		}
		sb.append(WARNINGS).append(COLON).append(NEWLINE);
		for (Iterator<ITMTSAMEror> iterator = warnings.iterator(); iterator
				.hasNext();) {
			ITMTSAMEror tmError = (ITMTSAMEror) iterator.next();
			sb.append(tmError.toString()).append(NEWLINE);
		}
		sb.append("TMResponseStructure.toString() END");
		return sb.toString();
	}
}
