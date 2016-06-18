package com.neozant.response;

public class SchedulerResponse extends GenericResponse{

	private String triggerKey;
	
	public SchedulerResponse() {
		
	}

	public String getTriggerKey() {
		return triggerKey;
	}

	public void setTriggerKey(String triggerKey) {
		this.triggerKey = triggerKey;
	}
	
}
