package epsos.ccd.posam.tm.util;

/**
 * Coded Element List Item.<br>
 * Item from Coded Element list. <br>
 * 
 * @author Frantisek Rudik
 * @author Organization: Posam
 * @author mail:frantisek.rudik@posam.sk
 * @version 1.2, 2010, 20 October
 */
public class CodedElementListItem implements TMConstants{

	/**
	 * Identification of coded element in form of xpath (mandatory)
	 */
	private String xPath;

	/**
	 * Indication, in which pivot document types coded element is used. Each
	 * subelement contains optionality of coded element in a particular document
	 * type {R, RNFA, O, NA} (mandatory)
	 */
	private String usage;

	/**
	 * Relation of coded element to a particular ValueSet from MVC (optional)
	 */
	private String valueSet;

	/**
	 * Version of ValueSet (optional)
	 */
	private String valueSetVersion;

	/**
	 * When translating coded element and there is an exception in translation
	 * of a particular coded element, target language code may be specified.
	 * (optional)
	 */
	private String targetLanguageCode;

	public String getxPath() {
		return xPath;
	}

	public void setxPath(String xPath) {
		this.xPath = xPath;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getValueSet() {
		return valueSet;
	}

	public void setValueSet(String valueSet) {
		this.valueSet = valueSet;
	}

	public String getValueSetVersion() {
		return valueSetVersion;
	}

	public void setValueSetVersion(String valueSetVersion) {
		this.valueSetVersion = valueSetVersion;
	}

	public String getTargetLanguageCode() {
		return targetLanguageCode;
	}

	public void setTargetLanguageCode(String targetLanguageCode) {
		this.targetLanguageCode = targetLanguageCode;
	}
	
	public boolean isRequired() {
		return (usage != null && usage.equals(R));
	}
	
	@Override
	public String toString() {
		return xPath + " ; " + usage + " ; " + valueSet  + " ; " + valueSetVersion + " ; " +targetLanguageCode;
	}
}