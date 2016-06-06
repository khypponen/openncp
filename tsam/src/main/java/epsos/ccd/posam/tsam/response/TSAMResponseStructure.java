package epsos.ccd.posam.tsam.response;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import epsos.ccd.posam.tsam.exception.ITMTSAMEror;
import epsos.ccd.posam.tsam.exception.TSAMError;
import epsos.ccd.posam.tsam.exception.TSAMErrorCtx;
import epsos.ccd.posam.tsam.util.CodedElement;

/**
 * Data transfer class. Encapsulates XML presentation of ResponseStructure for
 * transcoding or translation. Provides set method for simple response
 * manipulating
 * 
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August *
 */

public class TSAMResponseStructure {

	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_FAILURE = "failure";

	/**
	 * target Concept Code
	 */
	private String code;

	/**
	 * target Concept designation
	 */
	private String designation;

	/**
	 * target Concept designation language (optional)
	 */
	private String language;

	/**
	 * target Concept code system OID
	 */
	private String codeSystem;
	
	/**
	 * targed Concept code system Name
	 */
	private String codeSystemName;

	/**
	 * target Concept Code system version (optional)
	 */
	private String codeSystemVersion;

	private CodedElement inputCodedElement;
	private Element responseElement;

	/**
	 * List od Exceptions 
	 */
	private List<ITMTSAMEror> errors;

	/**
	 * List od Warnings
	 */
	private List<ITMTSAMEror> warnings;

	/**
	 * failure or success
	 */
	private String status;

	public TSAMResponseStructure(CodedElement inputCodedElement) {
		super();
		this.inputCodedElement = inputCodedElement;
		//status is SUCCESS until Error is added
		this.status = STATUS_SUCCESS;
		errors=new ArrayList<ITMTSAMEror>();
		warnings=new ArrayList<ITMTSAMEror>();
	}

	public TSAMResponseStructure(CodedElement inputCodedElement, String status) {
		super();
		this.inputCodedElement = inputCodedElement;
		this.status = status;
		errors=new ArrayList<ITMTSAMEror>();
		warnings=new ArrayList<ITMTSAMEror>();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCodeSystem() {
		return codeSystem;
	}

	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}

	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}

	public CodedElement getInputCodedElement() {
		return inputCodedElement;
	}

	public void setInputCodedElement(CodedElement inputCodedElement) {
		this.inputCodedElement = inputCodedElement;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	/** 
	 * @return Response Element (translated or transcoded)
	 */
	public Element getResponseElement() {
		return responseElement;
	}

	/** 
	 * @return List of TSAM errors
	 */
	public List<ITMTSAMEror> getErrors() {
		return (errors == null ? new ArrayList<ITMTSAMEror>() : errors);
	}

	/** 
	 * @return List of TSAM warnings
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
		//if error is added, status is changed to failure
		if (status.equals(STATUS_SUCCESS)) {
			status = STATUS_FAILURE;
		}
	}
	
	public void addError(TSAMError newError, String ctx) {
		TSAMErrorCtx errCtx=new TSAMErrorCtx(newError,ctx);
		addError(errCtx);
	}
	public void addWarning(ITMTSAMEror newWarning) {
		if (!warnings.contains(newWarning)) {
			warnings.add(newWarning);
		}
	}
	
	public void addWarning(TSAMError newWarning,String ctx) {
		TSAMErrorCtx warnCtx=new TSAMErrorCtx(newWarning,ctx);
		addWarning(warnCtx);
	}
	/**
	 * 
	 * @return true if status is SUCCESS otherwise false
	 */
	public boolean isStatusSuccess() {
		return (status != null && status.equals(STATUS_SUCCESS));
	}
	
	/**
	 * Returns xml presentation of Response Structure in form:
	<pre>     
	&lt;responseStructure&gt;
	  &lt;responseElement&gt;
	    &lt;!-- Translated/transcoded Coded Element--&gt;
	  &lt;/responseElement&gt;
	  &lt;responseStatus&gt;
	    &lt;status result="success/failure"/&gt;
	    &lt;!-- optional --&gt;
	    &lt;errors&gt;
	      &lt;error code="..." description=".."/&gt;
	      &lt;error code="..." description=".."/&gt;
	    &lt;/errors&gt;
	    &lt;!-- optional --&gt;
	    &lt;warnings&gt;
	      &lt;warning code="..." description=".."/&gt;
	      &lt;warning code="..." description=".."/&gt;
	    &lt;/warnings&gt;
	  &lt;/responseStatus&gt;
	&lt;/responseStructure&gt;
	</pre>
	 * @return Document
	 */
	public Document getDocument() {
		return null;
	}

	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}

	public String getCodeSystemName() {
		return codeSystemName;
	}	
}
