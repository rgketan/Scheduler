package com.neozant.storage;

import java.util.ArrayList;

public class DetailsOfScheduledEventList {

	private ArrayList<DetailsOfScheduledEventObject> listOfDetailScheduledObject;
	
	private String uniqueId;
	
	public DetailsOfScheduledEventList(){
		
		listOfDetailScheduledObject=new ArrayList<DetailsOfScheduledEventObject>();
	}

	public ArrayList<DetailsOfScheduledEventObject> getListOfDetailScheduledObject() {
		return listOfDetailScheduledObject;
	}

	public void setListOfDetailScheduledObject(
			ArrayList<DetailsOfScheduledEventObject> listOfDetailScheduledObject) {
		this.listOfDetailScheduledObject = listOfDetailScheduledObject;
	}
	
	public void addDetailScheduledObject(DetailsOfScheduledEventObject detailsOfScheduledEventObject ){
		this.listOfDetailScheduledObject.add(detailsOfScheduledEventObject);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	
}
