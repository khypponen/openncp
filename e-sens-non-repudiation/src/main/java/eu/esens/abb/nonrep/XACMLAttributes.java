package eu.esens.abb.nonrep;

import java.net.URI;

/**
 * The URIs are for formal checks only
 * @author max
 *
 */
public class XACMLAttributes {
	private String value;
	private URI dataType;
	private URI identifier; 
	
	public final String getValue() {
		return value;
	}
	public final void setValue(final String value) {
		this.value = value;
	}
	public final URI getDataType() {
		return dataType;
	}
	public final void setDataType(final URI dataType) {
		this.dataType = dataType;
	}
	public URI getIdentifier() {
		return identifier;
	}
	public void setIdentifier(URI identifier) {
		this.identifier = identifier;
	}

	
	
	
}
