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

import org.apache.log4j.Logger;

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
		logger.info("INTIALIZED  SchedulerApis");
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
			//ServerHelper helper=new ServerHelper();
			
			ServerHelper helper=ServerHelper.getServerHelperObject();
			conn= helper.getConnection();
		}catch(Exception ex){
			conn=null;
			logger.error("UNABLE TU SCHEDULE EVENT: "+ex.getMessage());
			ex.printStackTrace();
			schedulerResponse.setDetailMessageOnFailure(ex.getMessage());
		}
		
		if(conn!=null){
			schedulerResponse.setResponseStatus("success");
		}else{
			schedulerResponse.setDetailMessageOnFailure("UNABLE TO CONNECT TO D/B");
			schedulerResponse.setResponseStatus("fail");
		}
		
		return schedulerResponse;
	}
	
	
	@Path("/getConfigData")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ConfigurationResponse getConfigData(){
		logger.info("GETTING Configuration DATA FOR POPULATING FIELDS");
		return getConfigMetaData();
	}
	
	
	//FOR TESTING
	@Path("/getData")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ScheduleDataRequest scheduleEvent(){
		logger.info("GETTING ScheduleData from  testing APIS");
		return getScheduleData();
	}

	private ConfigurationResponse getConfigMetaData(){
		ConfigurationResponse config=new ConfigurationResponse();
		//SOURCE_DIRECTORY
		//DESTINATION_DIRECTORY
		final String sourceDirectory = System.getenv(EnumConstants.ENVFORSOURCE.getConstantType());
		
		final String destinationDirectory = System.getenv(EnumConstants.ENVFORDEST.getConstantType());
		
		logger.info("Getting PATH of source file::"+sourceDirectory+"| Destination:"+destinationDirectory);
		
		if(sourceDirectory == null || destinationDirectory==null){
			
			logger.error("ENVIORMENT VARIABLE IS NOT SET | SOURCE FILE PATH:"+sourceDirectory+", DESTINATION PATH:"+destinationDirectory );
			config.setResponseStatus("failure");
			config.setDetailMessageOnFailure("SOURCE DIRECTORY WE GET IS:"+sourceDirectory+"|| DESTINATION :"+destinationDirectory);
		}else{
			config.setResponseStatus("success");
			
			ArrayList<String> listOfFormat=new ArrayList<String>();
			listOfFormat.add(EnumConstants.XLSFILETYPE.getConstantType());
			listOfFormat.add(EnumConstants.CSVFILETYPE.getConstantType());
			config.setFileFormatSupported(listOfFormat);
			
			config.setOutputFilePath(destinationDirectory);
			config.setSourceFilePath(sourceDirectory);
			
			
			ArrayList<String> listOfSourceFile=new ArrayList<String>();
			File f1 = new File(sourceDirectory);
			
			File[] fileObjects = f1.listFiles();
			 for(File file : fileObjects){
				 listOfSourceFile.add(file.getAbsolutePath());
				 logger.info("SOURCE FILE PATH WE GET::"+file.getAbsolutePath());
			 }
			config.setListOfSourceFiles(listOfSourceFile);
		}
		return config;
	}
	
	private ScheduleDataRequest getScheduleData(){
		
		ScheduleDataRequest scheduleData=new ScheduleDataRequest();
		TimerData timerData=new TimerData();
		timerData.setDate(01);
		timerData.setHour(20);
		timerData.setMinutes(17);
		timerData.setMonth(06);
		timerData.setYear(2016);
		
		timerData.setAmPmMarker(EnumConstants.AMMARKER.getConstantType());
		
		String sqlFilePath="/Volumes/DATA/WORK/NEOZANT/EBSSqlReports/Testing.sql";
	    
		/*final String value = System.getenv("HOME");
		 System.out.println(value);
		 File f1 = new File(value);
		 System.out.println(f1);
		 System.out.println(f1.list());*/
		 
		scheduleData.setSqlFilePath(sqlFilePath);
		scheduleData.setFileFormat("xls");
		scheduleData.setOutputFileName("ROHAN");
		scheduleData.setTimerData(timerData);
		
		return scheduleData;
	}
	
	
	public static void main(String[] args) {
		
		SchedulerApis sch=new SchedulerApis();
		sch.databaseConnectivity();
		//System.out.println(config.getResponseStatus());
	}	
}
