package epsos.ccd.posam.tsam.util;

import org.w3c.dom.Element;

/**
 * Data transfer class Encapsulates XML presentation of Coded Concept for
 * transcoding or translation. Provides get method for simple access for code,
 * codesystem, ...
 * 
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 11 August
 */
public class CodedElement {
	private Element originalXMlElement;

	private String code;
	/**
	 * codeSystemName
	 */
	private String codeSystem;

	/**
	 * codeSystem
	 */
	private String oid;

	/**
	 * codeSystemVersion
	 */
	private String version;
	

	/**
	 * valueSet
	 */
	private String vsOid;
	
	/**
	 * valueSetVersion
	 */
	private String valueSetVersion;
	
	private String displayName;

	public CodedElement(Element originalXMlElement) {
		this.originalXMlElement = originalXMlElement;

		code = ((originalXMlElement.getAttribute("code") == null || originalXMlElement
				.getAttribute("code").equals("")) ? null : originalXMlElement
				.getAttribute("code"));
		codeSystem = ((originalXMlElement.getAttribute("codeSystemName") == null || originalXMlElement
				.getAttribute("codeSystemName").equals("")) ? null
				: originalXMlElement.getAttribute("codeSystemName"));
		oid = ((originalXMlElement.getAttribute("codeSystem") == null || originalXMlElement
				.getAttribute("codeSystem").equals("")) ? null
				: originalXMlElement.getAttribute("codeSystem"));
		version = ((originalXMlElement.getAttribute("codeSystemVersion") == null || originalXMlElement
				.getAttribute("codeSystemVersion").equals("")) ? null
				: originalXMlElement.getAttribute("codeSystemVersion"));
		displayName = ((originalXMlElement.getAttribute("displayName") == null || originalXMlElement
				.getAttribute("displayName").equals("")) ? null
				: originalXMlElement.getAttribute("displayName"));
	}

	/**
	 * For testing purpose only
	 * 
	 * @param code
	 * @param codeSystem
	 * @param vsOid
	 */
	public CodedElement(String code, String codeSystem, String oid,
			String version, String vsOid) {
		super();
		this.code = code;
		this.codeSystem = codeSystem;
		this.oid = oid;
		this.version = version;
		this.vsOid = vsOid;
	}

	public CodedElement(String code, String codeSystem, String oid,
			String version, String vsOid, String vsVersion) {
		super();
		this.code = code;
		this.codeSystem = codeSystem;
		this.oid = oid;
		this.version = version;
		this.vsOid = vsOid;
		this.valueSetVersion=vsVersion;
	}

	public Element getOriginalXMlElement() {
		return originalXMlElement;
	}

	public String getCode() {
		return code;
	}

	public String getCodeSystem() {
		return codeSystem;
	}

	public String getOid() {
		return oid;
	}

	public String getVersion() {
		return version;
	}

	public String getVsOid() {
		return vsOid;
	}	

	public String getValueSetVersion() {
		return valueSetVersion;
	}

	public void setValueSetVersion(String valueSetVersion) {
		this.valueSetVersion = valueSetVersion;
	}
	
	public void setVsOid(String valueSet) {
		this.vsOid = valueSet;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CodedElement [");
		if (code != null) {
			builder.append("code=");
			builder.append(code);
		}
		if (codeSystem != null) {
			builder.append(", ");
			builder.append("codeSystemName=");
			builder.append(codeSystem);
		}
		if (oid != null) {
			builder.append(", ");
			builder.append("oid(codeSystem)=");
			builder.append(oid);
		}
		if (version != null) {
			builder.append(", ");
			builder.append("version(codeSystemVersion)=");
			builder.append(version);
		}
		if (vsOid != null) {
			builder.append(", ");
			builder.append("vsOid(valueSet)=");
			builder.append(vsOid);
		}
		if (valueSetVersion != null) {
			builder.append(", ");
			builder.append("valueSetVersion=");
			builder.append(valueSetVersion);
		}
		
		builder.append("]");
		return builder.toString();
	}

}
