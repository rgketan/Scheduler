package com.neozant.storage;

import java.util.HashMap;

import org.apache.log4j.Logger;


public class ScheduledEventsDatabase {

	final static Logger logger = Logger.getLogger(ScheduledEventsDatabase.class);
	
	HashMap<String,ScheduledEventObject> scheduleEventMapper;
	
	//uniqueId with detail list
	HashMap<String,DetailsOfScheduledEventList> detailsOfScheduledEventList;
	
	
	
	private static ScheduledEventsDatabase scheduledEventsDatabase;
	
	public static ScheduledEventsDatabase getScheduledEventsDatabase(){
		
		if(scheduledEventsDatabase==null){
			
			try {
				scheduledEventsDatabase=new ScheduledEventsDatabase();
			} catch (Exception e) {
				
				logger.error("ScheduledEventsDatabase::ERROR WHILE INITIATING ScheduledEventsDatabase OBJECT");
				e.printStackTrace();
			}
			
		}
		
		return scheduledEventsDatabase;
	}
	
	private ScheduledEventsDatabase(){
		scheduleEventMapper=new HashMap<String, ScheduledEventObject>();
		detailsOfScheduledEventList=new HashMap<String, DetailsOfScheduledEventList>();
	}
	public HashMap<String, ScheduledEventObject> getScheduleEventMapper() {
		return scheduleEventMapper;
	}

	public void setScheduleEventMapper(
			HashMap<String, ScheduledEventObject> scheduleEventMapper) {
		this.scheduleEventMapper = scheduleEventMapper;
	}

	
	public HashMap<String, DetailsOfScheduledEventList> getDetailsOfScheduledEventList() {
		return detailsOfScheduledEventList;
	}

	public void setDetailsOfScheduledEventList(
			HashMap<String, DetailsOfScheduledEventList> detailsOfScheduledEventList) {
		this.detailsOfScheduledEventList = detailsOfScheduledEventList;
	}
	
	
	
}
