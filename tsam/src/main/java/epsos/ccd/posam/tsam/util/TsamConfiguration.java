package epsos.ccd.posam.tsam.util;


/**
 * @author Roman Repiscak
 * @author Organization: Posam
 * @author mail:roman.repiscak@posam.sk
 * @version 1.0, 2010, 5 October  
 * @author repiscak
 *
 */

public class TsamConfiguration {
	/**
	 * Code of a language, which country B uses for designations in pivot
	 * documents created in translation (local language of a country)
	 */
	private String translationLang;
	/**
	 * Code of a language, which country A uses for designations in pivot
	 * documents created in transcoding (epSOS defines it as English)
	 */
	private String transcodingLang;


	private TsamConfiguration() {
	}

	/**
	 * 
	 * @return Translation language
	 */
	public String getTranslationLang() {
		return translationLang;
	}

	public void setTranslationLang(String translationLang) {
		this.translationLang = translationLang;
	}

	/**
	 * @return Transcoding language
	 */
	public String getTranscodingLang() {
		return transcodingLang;
	}

	public void setTranscodingLang(String transcofingLang) {
		this.transcodingLang = transcofingLang;
	}

	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TsamConfiguration [");
		if (transcodingLang != null) {
			builder.append("transcodingLang=");
			builder.append(transcodingLang);
			builder.append(", ");
		}
		if (translationLang != null) {
			builder.append("translationLang=");
			builder.append(translationLang);
		}
		builder.append("]");
		return builder.toString();
	}
}
