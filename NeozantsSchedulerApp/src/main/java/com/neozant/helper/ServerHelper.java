package com.neozant.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.neozant.dbconnectivity.GetDbConnection;
import com.neozant.enums.EnumConstants;
import com.neozant.filehelper.FileHelper;
import com.neozant.request.FtpRequest;
import com.neozant.request.ScheduleDataRequest;
import com.neozant.request.TimerData;
import com.neozant.response.ConfigurationResponse;
import com.neozant.response.SchedulerResponse;
import com.neozant.response.SourceFileDetailResponse;
import com.neozant.storage.ScheduledEventObject;
import com.neozant.timerfacility.TimerTaskManager;


public class ServerHelper {

	final static Logger logger = Logger.getLogger(ServerHelper.class);
	
	private GetDbConnection conn;
	
	private TimerTaskManager timerTaskManager;
	
	private static ServerHelper serverHelper=null;
	
	
	private DataStorageHelper dataStorageHelper;
	public static ServerHelper getServerHelperObject(){
		
		if(serverHelper==null){
			
			try {
				serverHelper=new ServerHelper();
			} catch (Exception e) {
				
				logger.error("SERVERHELPER::ERROR WHILE INITIATING ServerHelper OBJECT");
				e.printStackTrace();
			}
			
		}
		
		return serverHelper;
	}
	
	private ServerHelper() throws Exception{
		try {
			this.conn = new GetDbConnection(EnumConstants.DBPROPERTYFILE.getConstantType());
			
			dataStorageHelper=DataStorageHelper.getDataStorageHelper();
			
			logger.info("SERVERHELPER::INTIALIZING HELPER AND DATABASE ...");
			
			timerTaskManager = new TimerTaskManager("Scheduling EVENT API");
			timerTaskManager.start();

			//System.out.println("SERVERHELPER::INTIALIZING HELPER AND DATABASE ...");
		}catch (Exception e) {
			logger.debug(e.getMessage());
			logger.error("SERVERHELPER::ERROR WE GET IS:"+e.getMessage()+"|| connection object::"+this.conn.toString());
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public String perfomAction(ScheduleDataRequest scheduleData) throws Exception{
		
		//boolean successFlag=true;
		
		logger.info("SERVERHELPER:: PERFORMING ACTION");
		//try {
			StringBuffer stringBuffer=readFile(scheduleData.getSqlFilePath());
			Statement stmnt= this.conn.getConnection().createStatement();
			
			//String fileContent=stringBuffer.toString();
			//String[] listOfQueries=fileContent.split(";");
			
			ResultSet rs = stmnt.executeQuery(stringBuffer.toString());
			
			
			
			String destinationDirectory = System.getenv("DESTINATION_DIRECTORY");
			
			/*//ONLY WHEN ENVIORNMENT VARIABLE  IS NOT THERE
			if(destinationDirectory==null){
				destinationDirectory ="/Users/Ketan/Desktop/NEOZANT/DESTINATION";
				
			}*/
			 
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm -ss");
			   //get current date time with Date()
			   Date date = new Date();
			   System.out.println(dateFormat.format(date));
			   
			String outputFilePath=destinationDirectory+File.separator+scheduleData.getOutputFileName()+"_"+dateFormat.format(date)+"."+scheduleData.getFileFormat();
			
			logger.info("SERVERHELPER:: OUTPUT FILE WILL BE CREATED AT:"+outputFilePath);
			
			
			FileHelper fileHelper=new FileHelper(scheduleData.getFileFormat());
			fileHelper.writeContent(rs, outputFilePath);
			
		logger.info("SERVERHELPER:: PEFORMED ACTION");
		//System.out.println("SERVERHELPER:: PEFORMED ACTION");
		
		return outputFilePath;
	}
	
	
	
	
	public Connection getConnection()throws SQLException{
		Connection dbConn = null;
		try {
			dbConn  = this.conn.getConnection();
		} catch (SQLException e) {
			logger.error("SERVERHELPER::Failed to Get DB Connection. The error is "+e.getMessage());
			e.printStackTrace();
			throw e;
		}	
		logger.info("Got DB Connection  " + this.conn.toString());
		//System.out.println("Got DB Connection  " + this.conn.toString());
		return dbConn;
	}
	
	
	public StringBuffer readFile(String pathOfTheFile)throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(pathOfTheFile));
			String str;
			while ((str = in.readLine()) != null) {
				sb.append(str + "\n ");
			}
			in.close();
			logger.info("CONTENT WE GET IS:::" + sb.toString());
			//System.out.println("CONTENT WE GET IS:::" + sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SERVERHELPER::Failed to Execute REASON:" + e.getMessage());
			throw e;
		}
		
		return sb;
	}
	
	//todo: add ENVIORNMENT NAME AND TYPE OF REPORT
	public ConfigurationResponse getConfigMetaData(){
		ConfigurationResponse config=new ConfigurationResponse();
		
		///Users/Ketan/Desktop/NEOZANT/SOURCE
		///Users/Ketan/Desktop/NEOZANT/DESTINATION
		
		//SOURCE_DIRECTORY
		//DESTINATION_DIRECTORY
		 String sourceDirectory = System.getenv(EnumConstants.ENVFORSOURCE.getConstantType());
		
		 String destinationDirectory = System.getenv(EnumConstants.ENVFORDEST.getConstantType());
		
		
		
		 //TODO: DELETE (TAKE PATHS FROM PREOPERTY FILE)
		if(sourceDirectory == null || destinationDirectory==null){
			sourceDirectory ="/Users/Ketan/Desktop/NEOZANT/SOURCE";
			destinationDirectory ="/Users/Ketan/Desktop/NEOZANT/DESTINATION";
			
		}
		
		logger.info("Getting PATH of source file::"+sourceDirectory+"| Destination:"+destinationDirectory);
		
		if(sourceDirectory == null || destinationDirectory==null){
			
			logger.error("ENVIORMENT VARIABLE IS NOT SET | SOURCE FILE PATH:"+sourceDirectory+", DESTINATION PATH:"+destinationDirectory );
			config.setResponseStatus("failure");
			config.setDetailMessageOnFailure("SOURCE DIRECTORY WE GET IS:"+sourceDirectory+"|| DESTINATION :"+destinationDirectory);
		}else{
			
			ArrayList<String> listOfFormat=new ArrayList<String>();
			listOfFormat.add(EnumConstants.XLSFILETYPE.getConstantType());
			listOfFormat.add(EnumConstants.CSVFILETYPE.getConstantType());
			config.setFileFormatSupported(listOfFormat);
			
			config.setOutputFilePath(destinationDirectory);
			config.setSourceFilePath(sourceDirectory);
			
			
			String listOfRecipients=readKeyValueFromPropertyFile(EnumConstants.EMAILPROPERTYFILE.getConstantType(),"recipientAddress");
			
			
			String[] recipients=listOfRecipients.split(",");
			
			List<String> arrayList = new ArrayList<String>(); 
			Collections.addAll(arrayList, recipients); 
			
			config.setRecipientAddress(arrayList);
			
			//ArrayList<String> listOfSourceFile=new ArrayList<String>();
			File f1 = new File(sourceDirectory);
			
			File[] fileObjects = f1.listFiles();
			
			ArrayList<SourceFileDetailResponse> listOfSourceFileDetails=new ArrayList<SourceFileDetailResponse>();
			
			 for(File file : fileObjects){
				 
				 if(isFileExtensionSql(file.getName())){
					 
						 SourceFileDetailResponse sourceFileDetailResponse=new SourceFileDetailResponse();
						 
						 sourceFileDetailResponse.setAbsolutePath(file.getAbsolutePath());
						 sourceFileDetailResponse.setFileName(file.getName());
						 
						 listOfSourceFileDetails.add(sourceFileDetailResponse);
						 
						 logger.info("SQL FILE ADDING "+file.getAbsolutePath());
				 }else{
			
					 logger.info( "NOT AN SQL FILE::"+file.getAbsolutePath());
				 }
			 }
			 
			 
			config.setListOfSourceFileDetails(listOfSourceFileDetails);
			
			
			String ftphost=readKeyValueFromPropertyFile(EnumConstants.FTPPROPERTYFILE.getConstantType(),"ftphost"),
					   ftpusername=readKeyValueFromPropertyFile(EnumConstants.FTPPROPERTYFILE.getConstantType(),"ftpusername"),
					   ftppassword=readKeyValueFromPropertyFile(EnumConstants.FTPPROPERTYFILE.getConstantType(),"ftppassword"),
					   ftpFilePath=readKeyValueFromPropertyFile(EnumConstants.FTPPROPERTYFILE.getConstantType(),"productionsalespath");
			
			FtpRequest ftpRequest=new FtpRequest();
			ftpRequest.setFtpFilePath(ftpFilePath);
			ftpRequest.setFtpHost(ftphost);
			ftpRequest.setFtpPassword(ftppassword);
			ftpRequest.setFtpUsername(ftpusername);
			
			config.setFtpRequest(ftpRequest);
			
			
			
			config.setResponseStatus("success");
			
		}
		return config;
	}
	
	//FOR TESTING
	 public ScheduleDataRequest getScheduleData(){
		
		ScheduleDataRequest scheduleData=new ScheduleDataRequest();
		TimerData timerData=new TimerData();
		timerData.setDate(01);
		timerData.setHour(20);
		timerData.setMinutes(17);
		timerData.setMonth(06);
		timerData.setYear(2016);
		
		timerData.setAmPmMarker(EnumConstants.AMMARKER.getConstantType());
		
		String sqlFilePath="/Volumes/DATA/WORK/NEOZANT/EBSSqlReports/Testing.sql";
	    
		final String value = System.getenv("HOME");
		 System.out.println(value);
		 File f1 = new File(value);
		 System.out.println(f1);
		 System.out.println(f1.list());
		 
		scheduleData.setSqlFilePath(sqlFilePath);
		scheduleData.setFileFormat("xls");
		scheduleData.setOutputFileName("ROHAN");
		scheduleData.setTimerData(timerData);
		
		ArrayList<String> mulipleAddress=new ArrayList<String>();
		mulipleAddress.add("steamtechnics@gmail.com");
		mulipleAddress.add("jija.1987@gmail.com");
		
		
		FtpRequest ftpRequest=new FtpRequest();
		ftpRequest.setFtpFilePath("C:");
		ftpRequest.setFtpHost("HOST");
		ftpRequest.setFtpPassword("PASSWORD");
		ftpRequest.setFtpUsername("USERNAME");
		
		scheduleData.setFtpRequest(ftpRequest);
		scheduleData.setRecipientAddress(mulipleAddress);
		return scheduleData;
	}
	
	
	
	private String readKeyValueFromPropertyFile(String propertyFileName,String key ){
		
		String keyValue=null;
		try {
			Properties dbprops = new Properties(); 
			InputStream inputStream=getClass().getClassLoader().getResourceAsStream(propertyFileName);
			dbprops.load(inputStream);
			
			keyValue = dbprops.get(key).toString();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("ConnectionPool:: UNABLE TO FIND PROPERTY FILE::"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ConnectionPool:: UNABLE TO LOAD PROPERTY FILE::"+e.getMessage());
			e.printStackTrace();
		}
		
		return keyValue;
	}	
		
	
	//CHANGES FOR SCHEDULED RELATED
	//logic
	public SchedulerResponse hitTimerTask(ScheduleDataRequest scheduleData, String keyId){
		
		SchedulerResponse schedulerResponse = new SchedulerResponse();
		String successFlag = "success";

		try {
			String triggerKey = timerTaskManager.scheduleTimerTask(scheduleData, keyId);
			
			logger.info("TRIGGER KEY WE GET IS::" + triggerKey);
			schedulerResponse.setTriggerKey(triggerKey);
			
			//ADDING DATA TO DATABASE
			//ServerHelper helper=ServerHelper.getServerHelperObject();
			
			String status=EnumConstants.EVENTSTATUSINITIATED.getConstantType();
			
			logger.info("STATUS:::" + status);
			ScheduledEventObject scheduledEventObject=getScheduledEventObject(scheduleData,keyId,status,triggerKey);
			
			
			dataStorageHelper.addNewEvent(scheduleData.getOutputFileName(), scheduledEventObject);
			
			//ENTER FTP DETAILS ONLY IF FTP TYPE REQUEST
			if(scheduleData.getTypeOfEvent().equalsIgnoreCase(EnumConstants.FTPTYPEEVENT.getConstantType())) {
				dataStorageHelper.addNewFtpEventDetails(scheduleData.getOutputFileName(), scheduleData.getFtpRequest());
			}
			
			
			
		} catch (Exception ex) {
			logger.error("SchedulerApis:: ERROR UNABLE TO SCHEDULE EVENT: " + ex.getMessage());
			successFlag = "failure";
			schedulerResponse.setDetailMessageOnFailure(ex.getMessage());
			ex.printStackTrace();
		}
		
		schedulerResponse.setResponseStatus(successFlag);
		return schedulerResponse;
	}
	
	
	
	public boolean deleteTimerTask(String jobKeyName){
		
		return timerTaskManager.deleteTimerTask(jobKeyName);
	}
	
	
	
	private ScheduledEventObject getScheduledEventObject(ScheduleDataRequest scheduleData, String uniqueId,String status,String jobKeyName){
		
		ScheduledEventObject scheduledEventObject =new ScheduledEventObject();
		
		scheduledEventObject.setDateAndTimeInString(scheduleData.getTimerData().toString());
		
		scheduledEventObject.setFileToExecute(scheduleData.getSqlFilePath());
		
		scheduledEventObject.setJobKeyName(jobKeyName);
		//scheduledEventObject.setListOfDetailScheduledObject(listOfDetailScheduledObject);
		scheduledEventObject.setNameOfScheduledTask(scheduleData.getOutputFileName());
		
		scheduledEventObject.setOutputFileFormat(scheduleData.getFileFormat());
		
		scheduledEventObject.setRecipientAddress(scheduleData.getToEmailId()+";"+scheduleData.getRecipientAddress().toString());
		
		scheduledEventObject.setStatus(status);
		
		scheduledEventObject.setTimerRepeatOn(scheduleData.getTimerData().getRepeatOn());
		
		scheduledEventObject.setUniqueId(uniqueId);
		
		scheduledEventObject.setTypeOfEvent(scheduleData.getTypeOfEvent());
		
		scheduledEventObject.setTimerInfo(scheduleData.getTimerData().getTimerInfo());
		
		return scheduledEventObject;
	}
	
	
	
	
	private boolean isFileExtensionSql(String fileName) {
        boolean isSqlFile = false;
        
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0){
        	
        	String fileExt=fileName.substring(fileName.lastIndexOf(".")+1);
        	
        	System.out.println("EXT::"+fileExt);
        	
           if(fileExt.equalsIgnoreCase("sql")){
        	   
        	   isSqlFile=true;
        	   
        	   System.out.println("EXTENSION FOUND::SQL FILE");
           }else{
        	   
        	   System.out.println("EXTENSION FOUND::BUT AN SQL FILE");
           }
        
        }else{
        	
        	System.out.println("EXTENSION NOT FOUND::::");
        }
        
        return isSqlFile;
    }
	
	//CHANGES
	
}
