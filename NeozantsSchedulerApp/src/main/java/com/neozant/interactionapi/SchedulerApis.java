package com.neozant.interactionapi;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import com.neozant.enums.EnumConstants;
import com.neozant.helper.ServerHelper;
import com.neozant.interfaces.IMessageValidator;
import com.neozant.request.ScheduleDataRequest;
import com.neozant.request.TimerData;
import com.neozant.response.ConfigurationResponse;
import com.neozant.response.GenericResponse;
import com.neozant.response.SchedulerResponse;
import com.neozant.timerfacility.TimerTaskManager;
import com.neozant.validator.ScheduleDataValidator;

@Path("schedule")
public class SchedulerApis {

	final static Logger logger = Logger.getLogger(SchedulerApis.class);
	
	private static AtomicLong counter = new AtomicLong(0);
	 
    public static String nextId() {
        
    	//iqueValue=counter.incrementAndGet();
    	return "ID-"+counter.incrementAndGet();     
    } 
 
	TimerTaskManager timerTaskManager;
	
	
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
	
	@Path("/event")
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
			timerTaskManager = new TimerTaskManager("Scheduling EVENT API");
			timerTaskManager.start();

			logger.info("Timer Task Manager is been initialized");

			try {
				String triggerKey = timerTaskManager.scheduleTimerTask(scheduleData, nextId());
				
				logger.info("TRIGGER KEY WE GET IS::" + triggerKey);
				schedulerResponse.setTriggerKey(triggerKey);
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
	
	
	@Path("/testDbConnection")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public SchedulerResponse databaseConnectivity(){
		
		SchedulerResponse schedulerResponse=new SchedulerResponse();
		Connection conn=null;
		
		try{
			ServerHelper helper=new ServerHelper();
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
	
	
	@Path("/getConfigData")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ConfigurationResponse getConfigData(){
		logger.info("GETTING Configuration DATA FOR POPULATING FIELDS");
		ConfigurationResponse configurationResponse=new ConfigurationResponse();
		String successFlag = "success";
		
		try{
		ServerHelper helper=new ServerHelper();
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
	
	
	//FOR TESTING
	@Path("/getData")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ScheduleDataRequest scheduleEvent(){
		logger.info("GETTING ScheduleData from  testing APIS");
		ScheduleDataRequest scheduleDataRequest=new ScheduleDataRequest();
		try{
			ServerHelper helper=new ServerHelper();
			scheduleDataRequest=helper.getScheduleData();
			}catch(Exception ex){
				logger.error("UNABLE TO SCHEDULE EVENT: "+ex.getMessage());
				ex.printStackTrace();
			}
		
		return scheduleDataRequest;
	}

	
	
	
	public static void main(String[] args) {
		
		SchedulerApis sch=new SchedulerApis();
		sch.databaseConnectivity();
		//System.out.println(config.getResponseStatus());
	}	
}
