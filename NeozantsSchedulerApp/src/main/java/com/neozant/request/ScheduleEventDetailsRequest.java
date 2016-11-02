package com.neozant.request;

public class ScheduleEventDetailsRequest extends GenericRequestType{
	
	private String uniqueId;
	private String eventName;
	
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	} 
	


}
