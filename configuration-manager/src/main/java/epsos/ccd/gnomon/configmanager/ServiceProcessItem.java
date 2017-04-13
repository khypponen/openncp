package epsos.ccd.gnomon.configmanager;


public class ServiceProcessItem { 
	private String[] eventName;
	private String[] eventNumber;
	
	public ServiceProcessItem(String[] eventName, String[] eventNumber) {
		this.eventName = eventName;
		this.eventNumber = eventNumber;
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ServiceProcessItem: " + eventName + ":" +eventNumber;
	}
	
	
	
	

}
