package com.neozant.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.neozant.dbconnectivity.GetDbConnection;
import com.neozant.enums.EnumConstants;
import com.neozant.filehelper.FileHelper;
import com.neozant.request.ScheduleDataRequest;
import com.neozant.request.TimerData;
import com.neozant.response.ConfigurationResponse;


public class ServerHelper {

	final static Logger logger = Logger.getLogger(ServerHelper.class);
	
	private GetDbConnection conn;
	
	
	/*private static ServerHelper serverHelper=null;
	
	public static ServerHelper getServerHelperObject(){
		
		if(serverHelper==null){
			serverHelper=new ServerHelper();
		}
		
		return serverHelper;
	}*/
	
	public ServerHelper() throws Exception{
		try {
			this.conn = new GetDbConnection(EnumConstants.DBPROPERTYFILE.getConstantType());
			logger.info("SERVERHELPER::INTIALIZING HELPER AND DATABASE ...");
			
			//System.out.println("SERVERHELPER::INTIALIZING HELPER AND DATABASE ...");
		}catch (Exception e) {
			logger.debug(e.getMessage());
			logger.error("SERVERHELPER::ERROR WE GET IS:"+e.getMessage()+"|| connection object::"+this.conn.toString());
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public void perfomAction(ScheduleDataRequest scheduleData) throws Exception{
		
		//boolean successFlag=true;
		
		logger.info("SERVERHELPER:: PERFORMING ACTION");
		//try {
			StringBuffer stringBuffer=readFile(scheduleData.getSqlFilePath());
			Statement stmnt= this.conn.getConnection().createStatement();
			
			ResultSet rs = stmnt.executeQuery(stringBuffer.toString());
		
			final String destinationDirectory = System.getenv("DESTINATION_DIRECTORY");
			
			String outputFilePath=destinationDirectory+"\\"+scheduleData.getOutputFileName()+"."+scheduleData.getFileFormat();
			
			logger.info("SERVERHELPER:: OUTPUT FILE WILL BE CREATED AT:"+outputFilePath);
			
			//System.out.println("SERVERHELPER:: OUTPUT FILE WILL BE CREATED AT:"+outputFilePath);
			
			
			/*if(!scheduleData.getFileFormat().equalsIgnoreCase(EnumConstants.XSLFILETYPE.getConstantType()) && 
			   !scheduleData.getFileFormat().equalsIgnoreCase(EnumConstants.CSVFILETYPE.getConstantType())){
				
				logger.error("SERVERHELPER::UNSUPPORTED FORMAT+"+scheduleData.getFileFormat());
				System.out.println("SERVERHELPER::UNSUPPORTED FORMAT+"+scheduleData.getFileFormat());
				successFlag=false;
				
			}else{*/
				FileHelper fileHelper=new FileHelper(scheduleData.getFileFormat());
				fileHelper.writeContent(rs, outputFilePath);
			//}
		
		/*} catch (Exception e) {
			logger.error("SERVERHELPER:: Exception caused while Performing Action:. The error is "+e.getMessage());
			successFlag=false;
			e.printStackTrace();
			throw e;
		}*/
		logger.info("SERVERHELPER:: PEFORMED ACTION");
		//System.out.println("SERVERHELPER:: PEFORMED ACTION");
		
		//return successFlag;
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
	
	
	public StringBuffer readFile(String pathOfTheFile) {
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
		}
		
		return sb;
	}
	
	
	public ConfigurationResponse getConfigMetaData(){
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
	    
		/*final String value = System.getenv("HOME");
		 System.out.println(value);
		 File f1 = new File(value);
		 System.out.println(f1);
		 System.out.println(f1.list());*/
		 
		scheduleData.setSqlFilePath(sqlFilePath);
		scheduleData.setFileFormat("xls");
		scheduleData.setOutputFileName("ROHAN");
		scheduleData.setTimerData(timerData);
		
		ArrayList<String> mulipleAddress=new ArrayList<String>();
		mulipleAddress.add("steamtechnics@gmail.com");
		mulipleAddress.add("jija.1987@gmail.com");
		
		scheduleData.setMulipleAddress(mulipleAddress);
		return scheduleData;
	}
	
}
