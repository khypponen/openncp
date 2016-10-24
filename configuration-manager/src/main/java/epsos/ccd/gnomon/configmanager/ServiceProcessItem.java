package epsos.ccd.gnomon.configmanager;

import no.difi.vefa.peppol.common.model.TransportProfile;

public class ServiceProcessItem { 
	private String[] eventName;
	private String[] eventNumber;
	private TransportProfile transport;
	
	public ServiceProcessItem(String[] eventName, String[] eventNumber, TransportProfile transport) {
		this.eventName = eventName;
		this.eventNumber = eventNumber;
		this.setTransport(transport);
	}
	public String[] getEventName() {
		return eventName;
	}
	public void setEventName(String[] eventName) {
		this.eventName = eventName;
	}
	public String[] getEventNumber() {
		return eventNumber;
	}
	public void setEventNumber(String[] eventNumber) {
		this.eventNumber = eventNumber;
	}
	public TransportProfile getTransport() {
		return transport;
	}
	public void setTransport(TransportProfile transport) {
		this.transport = transport;
	}
	
	
	
	

}
