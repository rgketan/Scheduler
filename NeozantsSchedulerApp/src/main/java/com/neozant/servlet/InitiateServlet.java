package com.neozant.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.neozant.enums.EnumConstants;
import com.neozant.helper.DataStorageHelper;
import com.neozant.interactionapi.SchedulerApis;
import com.neozant.request.ScheduleDataRequest;
import com.neozant.request.TimerData;
import com.neozant.response.ScheduleEventRespose;
import com.neozant.response.SchedulerResponse;
import com.neozant.storage.ScheduledEventObject;

public class InitiateServlet extends HttpServlet{

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(InitiateServlet.class);
	
	private static final long serialVersionUID = 4019080652299041192L;

	public void init() throws ServletException
    {
          logger.info("InitiateServlet: INITIALIZING ALL DATA THAT ARE REQUIRED");
          
          try{
        	  createAllTables();
        	  createAllScheduleEvents();
          }catch(Exception ex){
        	  logger.error("InitiateServlet: ERROR while rescheduling all events:"+ex.getMessage());
        	  ex.printStackTrace();
          }
    }
	
	
	private void createAllTables(){
      	  DataStorageHelper.getDataStorageHelper().createAllTable();
      	  logger.info("InitiateServlet: TABLE IS BEEN CREATED");
	}
	
	
	private void createAllScheduleEvents(){
		
		//ScheduleDataRequest scheduleData=new ScheduleDataRequest();
		
		ScheduleEventRespose scheduleEventRespose=DataStorageHelper.getDataStorageHelper().getAllUnfinishedScheduledEvents();
		
		List<ScheduledEventObject> scheduleEventObject=scheduleEventRespose.getScheduledEventObject();
		
		
		SchedulerApis schedulerApis=new SchedulerApis();
		
		for(ScheduledEventObject scheduleEvent:scheduleEventObject) {
			
			//scheduleEvent.getDateAndTimeInString();
			
			ScheduleDataRequest scheduleData=new ScheduleDataRequest();
			
			scheduleData.setTypeOfEvent(scheduleEvent.getTypeOfEvent());
			
			scheduleData.setOutputFileName(scheduleEvent.getNameOfScheduledTask());
			scheduleData.setFileFormat(scheduleEvent.getOutputFileFormat());
			scheduleData.setSqlFilePath(scheduleEvent.getFileToExecute());
			
			//POPULATING TIMER DATA
			scheduleData.setTimerData(getTimerData(scheduleEvent.getDateAndTimeInString(), scheduleEvent.getTimerRepeatOn(),scheduleEvent.getTimerInfo()));
			
			//THIS IS SET TO SKIP FEW VALIDATION
			scheduleData.setAlreadyCreated(true);
			
			
			if(scheduleData.getTypeOfEvent().equalsIgnoreCase(EnumConstants.EMAILTYPEEVENT.getConstantType())){
				//GETTING EMAIL IDS
				String[] emailIds= scheduleEvent.getRecipientAddress().split(";");
				String toEmailId=emailIds[0],
					   listOfaddresses=emailIds[1];
				String recepientAddresses =listOfaddresses.replace("[","").replace("]","");
				ArrayList<String> myList = new ArrayList<String>();
				
				logger.info("InitiateServlet: RecipientAddress we get is :"+recepientAddresses+" and size:"+recepientAddresses.length());
				if(recepientAddresses.length()>0){
					myList.addAll(Arrays.asList(recepientAddresses.split(",")));
				}
				logger.info("InitiateServlet: RecipientAddress we get is :"+myList.toString());
				scheduleData.setToEmailId(toEmailId);
				scheduleData.setRecipientAddress(myList);
			}else{
				logger.info("InitiateServlet: FTP DETAILS we get is :"+scheduleEvent.getFtpRequest().toString());
				scheduleData.setFtpRequest(scheduleEvent.getFtpRequest());
			}
			
			
			
			SchedulerResponse  response=schedulerApis.scheduleEvent(scheduleData);
			
			if(response.getResponseStatus().equalsIgnoreCase("failure")){
				
				DataStorageHelper.getDataStorageHelper().updateEventStatus(scheduleEvent.getNameOfScheduledTask(), 
																		   EnumConstants.EVENTSTATUSERROR.getConstantType());
			}
		}
			
		
	}
	
	
	//creating timer data object from data stored in SQLITE database  
	private TimerData getTimerData(String timerString,String repeatOn,String timerInfo){
		
		String[] splitString= timerString.split(",");
		
		
		 TimerData timerData=new TimerData();
		   
		   
		   String[] date=splitString[0].split(":"),
				    timeContent=splitString[1].split(":");
		   
			   timerData.setDate(Integer.parseInt(date[0]));
			   timerData.setMonth(Integer.parseInt(date[1]));
			   timerData.setYear(Integer.parseInt(date[2]));
			   
			   
			   timerData.setHour(Integer.parseInt(timeContent[0]));
			   timerData.setMinutes(Integer.parseInt(timeContent[1]));
			   timerData.setAmPmMarker(timeContent[3]);
			   
			   
			   timerData.setRepeatOn(repeatOn);
			   
			   timerData.setTimerInfo(timerInfo);
		   
		   return timerData;
	}
}
