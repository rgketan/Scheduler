package com.neozant.response;

import java.util.ArrayList;
import java.util.List;

import com.neozant.storage.ScheduledEventObject;

public class ScheduleEventRespose extends GenericResponse{

	private List<ScheduledEventObject> scheduledEventObject;
	
	public ScheduleEventRespose(){
		scheduledEventObject=new ArrayList<ScheduledEventObject>();
	}

	public List<ScheduledEventObject> getScheduledEventObject() {
		return scheduledEventObject;
	}

	public void setScheduledEventObject(
			List<ScheduledEventObject> scheduledEventObject) {
		this.scheduledEventObject = scheduledEventObject;
	}


	
	
	
}
