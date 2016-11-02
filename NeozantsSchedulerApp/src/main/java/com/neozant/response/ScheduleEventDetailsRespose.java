package com.neozant.response;

import java.util.ArrayList;

import com.neozant.storage.DetailsOfScheduledEventObject;

public class ScheduleEventDetailsRespose extends GenericResponse{
	
	
	private ArrayList<DetailsOfScheduledEventObject> listOfDetailScheduledObject;

	public ArrayList<DetailsOfScheduledEventObject> getListOfDetailScheduledObject() {
		return listOfDetailScheduledObject;
	}

	public void setListOfDetailScheduledObject(
			ArrayList<DetailsOfScheduledEventObject> listOfDetailScheduledObject) {
		this.listOfDetailScheduledObject = listOfDetailScheduledObject;
	}
	
		
	

}
