package com.neozant.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.neozant.dbconnectivity.ConnectionPool;
import com.neozant.enums.EnumConstants;
import com.neozant.filehelper.FileHelper;
import com.neozant.request.ScheduleDataRequest;


public class ServerHelper {

	final static Logger logger = Logger.getLogger(ServerHelper.class);
	
	private ConnectionPool conn;
	
	
	private static ServerHelper serverHelper=null;
	
	public static ServerHelper getServerHelperObject(){
		
		if(serverHelper==null){
			serverHelper=new ServerHelper();
		}
		
		return serverHelper;
	}
	
	private ServerHelper() {
		try {
			this.conn = new ConnectionPool(EnumConstants.DBPROPERTYFILE.getConstantType());
			logger.info("SERVERHELPER::INTIALIZING HELPER AND DATABASE ...");
			
			System.out.println("SERVERHELPER::INTIALIZING HELPER AND DATABASE ...");
		}catch (Exception e) {
			logger.debug(e.getMessage());
			logger.error("SERVERHELPER::ERROR WE GET IS:"+e.getMessage()+"|| connection object::"+this.conn.toString());
			e.printStackTrace();
		}
	}
	
	public boolean perfomAction(ScheduleDataRequest scheduleData){
		
		boolean successFlag=true;
		
		logger.info("SERVERHELPER:: PERFORMING ACTION");
		try {
			StringBuffer stringBuffer=readFile(scheduleData.getSqlFilePath());
			Statement stmnt= this.conn.getConnection().createStatement();
			
			ResultSet rs = stmnt.executeQuery(stringBuffer.toString());
		
			final String destinationDirectory = System.getenv("DESTINATION_DIRECTORY");
			
			String outputFilePath=destinationDirectory+"\\"+scheduleData.getOutputFileName()+"."+scheduleData.getFileFormat();
			
			logger.info("SERVERHELPER:: OUTPUT FILE WILL BE CREATED AT:"+outputFilePath);
			
			System.out.println("SERVERHELPER:: OUTPUT FILE WILL BE CREATED AT:"+outputFilePath);
			
			
			/*if(!scheduleData.getFileFormat().equalsIgnoreCase(EnumConstants.XSLFILETYPE.getConstantType()) && 
			   !scheduleData.getFileFormat().equalsIgnoreCase(EnumConstants.CSVFILETYPE.getConstantType())){
				
				logger.error("SERVERHELPER::UNSUPPORTED FORMAT+"+scheduleData.getFileFormat());
				System.out.println("SERVERHELPER::UNSUPPORTED FORMAT+"+scheduleData.getFileFormat());
				successFlag=false;
				
			}else{*/
				FileHelper fileHelper=new FileHelper(scheduleData.getFileFormat());
				fileHelper.writeContent(rs, outputFilePath);
			//}
		
		} catch (Exception e) {
			logger.error("SERVERHELPER:: Exception caused while Performing Action:. The error is "+e.getMessage());
			successFlag=false;
			e.printStackTrace();
		}
		logger.info("SERVERHELPER:: PEFORMED ACTION");
		System.out.println("SERVERHELPER:: PEFORMED ACTION");
		
		return successFlag;
	}
	
	
	
	
	public Connection getConnection(){
		Connection dbConn = null;
		try {
			dbConn  = this.conn.getConnection();
		} catch (SQLException e) {
			logger.error("SERVERHELPER::Failed to Get DB Connection. The error is "+e.getMessage());
			e.printStackTrace();
		}	
		logger.info("Got DB Connection  " + this.conn.toString());
		System.out.println("Got DB Connection  " + this.conn.toString());
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
			System.out.println("CONTENT WE GET IS:::" + sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SERVERHELPER::Failed to Execute" + pathOfTheFile
					+ ". The error is" + e.getMessage());
		}
		
		return sb;
	}
}
