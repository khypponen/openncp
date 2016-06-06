package epsos.ccd.gnomon.utils;

import java.io.Serializable;

import net.RFC3881.AuditMessage;

public class SerializableMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private AuditMessage message;
	private String facility;
	private String severity;

	public SerializableMessage(AuditMessage message, String facility, String severity) {
		this.message = message;
		this.facility = facility;
		this.severity = severity;
	}
	
	public AuditMessage getMessage() {
		return message;
	}
	
	public void setMessage(AuditMessage message) {
		this.message = message;
	}
	
	public String getFacility() {
		return facility;
	}
	
	public void setFacility(String facility) {
		this.facility = facility;
	}
	
	public String getSeverity() {
		return severity;
	}
	
	public void setSeverity(String severity) {
		this.severity = severity;
	}
}
