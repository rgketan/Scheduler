package com.neozant.interactionapi;

import java.sql.Connection;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.neozant.helper.DataStorageHelper;
import com.neozant.helper.ServerHelper;
import com.neozant.interfaces.IMessageValidator;
import com.neozant.request.ScheduleDataRequest;
import com.neozant.request.ScheduleEventDetailsRequest;
import com.neozant.response.ConfigurationResponse;
import com.neozant.response.GenericResponse;
import com.neozant.response.ScheduleEventDetailsRespose;
import com.neozant.response.ScheduleEventRespose;
import com.neozant.response.SchedulerResponse;
import com.neozant.validator.ScheduleDataValidator;

@Path("schedule")
public class SchedulerApis {

	final static Logger logger = Logger.getLogger(SchedulerApis.class);
	
	private static AtomicLong counter = new AtomicLong(0);
	 
    public static String nextId() {
        
    	//iqueValue=counter.incrementAndGet();
    	return "ID-"+counter.incrementAndGet();     
    } 
    
    
    public static void setCount(long counterValue){
    	counter=new AtomicLong(counterValue);
    }
 
	//TimerTaskManager timerTaskManager;
	
	
	//ServerHelper helper;
	public SchedulerApis(){
		logger.info("CONSTRUCTOR:: INTIALIZED  SchedulerApis");
		 /*String log4jConfigFile = System.getProperty("user.dir")
	                + File.separator + "log4j.xml";
	    System.out.println("CONSTRUCTOR::FILE NAME::"+log4jConfigFile);
	    DOMConfigurator.configure(log4jConfigFile);*/
	        
		/*timerTaskManager=new TimerTaskManager("API");
		timerTaskManager.start();*/
	}
	
	
	//TRIGGER EVENT
	@Path("/triggerEvent")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SchedulerResponse scheduleEvent(ScheduleDataRequest scheduleData) {

		logger.info("Scheduling EVENT is been TRIGERED");

		IMessageValidator validator = new ScheduleDataValidator();

		scheduleData.setMessageType("scheduleEvent");
		GenericResponse response = validator.validateMessage(scheduleData);

		SchedulerResponse schedulerResponse = new SchedulerResponse();
		String successFlag = "success";

		

		if (response.getResponseStatus().equals("success")) {
			//timerTaskManager = new TimerTaskManager("Scheduling EVENT API");
			//timerTaskManager.start();

			logger.info("Timer Task Manager is been initialized");

			try {
				//String triggerKey = timerTaskManager.scheduleTimerTask(scheduleData, nextId());
				
				//logger.info("TRIGGER KEY WE GET IS::" + triggerKey);
				//schedulerResponse.setTriggerKey(triggerKey);
				
				//ADDING DATA TO DATABASE
				ServerHelper helper=ServerHelper.getServerHelperObject();
				
				schedulerResponse=helper.hitTimerTask(scheduleData, nextId());
				
			} catch (Exception ex) {
				logger.error("SchedulerApis:: ERROR UNABLE TU SCHEDULE EVENT: " + ex.getMessage());
				successFlag = "failure";
				schedulerResponse.setDetailMessageOnFailure(ex.getMessage());
				ex.printStackTrace();
			}
		} else {
			successFlag = "failure";
			schedulerResponse.setDetailMessageOnFailure(response.getDetailMessageOnFailure());
		}
		
		schedulerResponse.setResponseStatus(successFlag);
		return schedulerResponse;
	}
	
	
	//TEST DATABASE CONNECTIVITY
	@Path("/testDbConnection")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public SchedulerResponse databaseConnectivity(){
		
		SchedulerResponse schedulerResponse=new SchedulerResponse();
		Connection conn=null;
		
		try{
			ServerHelper helper=ServerHelper.getServerHelperObject();
			conn= helper.getConnection();
		}catch(Exception ex){
			conn=null;
			logger.error("UNABLE TO SCHEDULE EVENT: "+ex.getMessage());
			ex.printStackTrace();
			schedulerResponse.setDetailMessageOnFailure(ex.getMessage());
		}
		
		if(conn!=null){
			schedulerResponse.setResponseStatus("success");
		}else{
			
			//schedulerResponse.setDetailMessageOnFailure("Could not establish the connection with D/B");
			schedulerResponse.setResponseStatus("failure");
		}
		
		return schedulerResponse;
	}
	
	
	//GIVE CONFIG DATA
	@Path("/getConfigData")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ConfigurationResponse getConfigData(){
		logger.info("GETTING Configuration DATA FOR POPULATING FIELDS");
		ConfigurationResponse configurationResponse=new ConfigurationResponse();
		String successFlag = "success";
		
		try{
		ServerHelper helper=ServerHelper.getServerHelperObject();
		configurationResponse=helper.getConfigMetaData();
		}catch(Exception ex){
			logger.error("UNABLE TO SCHEDULE EVENT: "+ex.getMessage());
			ex.printStackTrace();
			successFlag = "failure";
			configurationResponse.setDetailMessageOnFailure(ex.getMessage());
		}
		
		configurationResponse.setResponseStatus(successFlag);
		
		return configurationResponse;
	}
	
	
	//EVENT RELATED
	
	//ALL SCHEDULED EVENTS
	@Path("/getScheduledEvents")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ScheduleEventRespose getScheduleEvent(){
		logger.info("GETTING ALL SCHEDULED EVENTS");
		String successFlag = "success";
		DataStorageHelper dataStorageHelper=DataStorageHelper.getDataStorageHelper();
		
		
		ScheduleEventRespose scheduleEventRespose=new ScheduleEventRespose();
		try{
			scheduleEventRespose=dataStorageHelper.getAllScheduledEvents();
			}catch(Exception ex){
				logger.error("UNABLE TO GET SCHEDULE EVENT: "+ex.getMessage());
				ex.printStackTrace();
				successFlag = "failure";
				scheduleEventRespose.setDetailMessageOnFailure(ex.getMessage());
			}
		scheduleEventRespose.setResponseStatus(successFlag);
		return scheduleEventRespose;
	}
	
	
	//SCHEDULED EVENT DETAILS
	@Path("/getScheduledEventDetails")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ScheduleEventDetailsRespose getScheduleEventDetails(ScheduleEventDetailsRequest scheduleEventDetailsRequest){
		logger.info("GETTING DETAIL SCHEDULED EVENT");
		String successFlag = "success";
		
		scheduleEventDetailsRequest.setMessageType("getScheduledEventDetails");
		
		ScheduleEventDetailsRespose scheduleEventDetailsRespose=new ScheduleEventDetailsRespose();
		
		IMessageValidator validator = new ScheduleDataValidator();

		GenericResponse response = validator.validateMessage(scheduleEventDetailsRequest);

		if (response.getResponseStatus().equals("success")) {

			DataStorageHelper dataStorageHelper=DataStorageHelper.getDataStorageHelper();
			try {
				scheduleEventDetailsRespose = dataStorageHelper.getEventDetails(scheduleEventDetailsRequest.getEventName());
			} catch (Exception ex) {
				logger.error("UNABLE TO GET SCHEDULE EVENT DETAILS: "
						+ ex.getMessage());
				ex.printStackTrace();
				successFlag = "failure";
				scheduleEventDetailsRespose.setDetailMessageOnFailure(ex
						.getMessage());
			}

		} else {

			successFlag = "failure";
			scheduleEventDetailsRespose.setDetailMessageOnFailure(response.getDetailMessageOnFailure());
		}

		
		scheduleEventDetailsRespose.setResponseStatus(successFlag);
		return scheduleEventDetailsRespose;
	}
	
	
	//DELETE SCHEDULED EVENT 
	@Path("/deleteScheduledEvent")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public GenericResponse deleteScheduleEvent(ScheduleEventDetailsRequest scheduleEventDetailsRequest){
		logger.info("SchedulerApis::DELETING SCHEDULED EVENT::"+scheduleEventDetailsRequest.getEventName());
		String successFlag = "success";
		
		scheduleEventDetailsRequest.setMessageType("deleteScheduledEvent");
		
		GenericResponse deleteScheduleResponse=new GenericResponse();
		
		ScheduleEventDetailsRespose scheduleEventDetailsRespose=new ScheduleEventDetailsRespose();
		
		IMessageValidator validator = new ScheduleDataValidator();
		GenericResponse response = validator.validateMessage(scheduleEventDetailsRequest);

		if (response.getResponseStatus().equals("success")) {
			//DELETE TIMER TASK
			ServerHelper helper=ServerHelper.getServerHelperObject();
			if(!helper.deleteTimerTask(scheduleEventDetailsRequest.getUniqueId())){
				logger.error("SchedulerApis::UNABLE TO DELETE SCHEDULE EVENT WITH ID:"+scheduleEventDetailsRequest.getUniqueId()+"DOES NOT EXIST OR ALREADY EXECUTED ");	
			}else{
				logger.error("SchedulerApis::SUCCESSFULLY DELETED SCHEDULE EVENT WITH ID:"+scheduleEventDetailsRequest.getUniqueId());
			}
			//DELETE STORED EVENTS AND DETAILS OF EVENTS
			DataStorageHelper dataStorageHelper=DataStorageHelper.getDataStorageHelper();
			dataStorageHelper.deleteEventRelatedData(scheduleEventDetailsRequest.getEventName());
			//dataStorageHelper.deleteDetailEventFromDb(scheduleEventDetailsRequest.getUniqueId());
			
		} else {

			successFlag = "failure";
			scheduleEventDetailsRespose.setDetailMessageOnFailure(response.getDetailMessageOnFailure());
		}

		deleteScheduleResponse.setResponseStatus(successFlag);
		return deleteScheduleResponse;
	}
	
	public static void main(String[] args) {
		
		//SchedulerApis sch=new SchedulerApis();
		//sch.databaseConnectivity();
		//System.out.println(config.getResponseStatus());
		
		//FtpServerHelper ftpServerHelper=new FtpServerHelper();
		
		//C:\Users\Ketan\Desktop\SCHEDULER_RELATED\SOURCE_DIRECTORY\TDC__ONHAND_CHECK_TWO_DECIMAL.sql
		
		//C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\DESTINATION_DIRECTORY\\EXECUTE_1_2016-10-18_19-34 -04.csv
		
		//String path="C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\DESTINATION_DIRECTORY\\FIVE_DECIMAL_2016-11-01_11-00 -09.xls";
		
		
		// "production", "staging" "costs" "sales"
		
		//ftpServerHelper.uploadFIleToFtpServer(path, "staging", "sales");
		
	}	
	
	

	//FOR TESTING
	@Path("/getData")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ScheduleDataRequest scheduleEvent(){
		logger.info("GETTING ScheduleData from  testing APIS");
		ScheduleDataRequest scheduleDataRequest=new ScheduleDataRequest();
		try{
			ServerHelper helper=ServerHelper.getServerHelperObject();
			scheduleDataRequest=helper.getScheduleData();
			}catch(Exception ex){
				logger.error("UNABLE TO SCHEDULE EVENT: "+ex.getMessage());
				ex.printStackTrace();
			}
		
		return scheduleDataRequest;
	}

	
}
