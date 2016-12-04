package com.neozant.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.neozant.dbconnectivity.LocalDbConnectionsPool;
import com.neozant.enums.EnumConstants;
import com.neozant.enums.EnumQueriesToExecute;
import com.neozant.request.FtpRequest;
import com.neozant.response.ScheduleEventDetailsRespose;
import com.neozant.response.ScheduleEventRespose;
import com.neozant.storage.DetailsOfScheduledEventObject;
import com.neozant.storage.ScheduledEventObject;

public class DataStorageHelper {

	final static Logger logger = Logger.getLogger(DataStorageHelper.class);
	
	private static DataStorageHelper datastorageHelper=null;
	
	private LocalDbConnectionsPool localDbConnection;
	
	private DataStorageHelper(){
		try {
			this.localDbConnection = new LocalDbConnectionsPool(EnumConstants.DBPROPERTYFILE.getConstantType());
			
		}catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			logger.error("DataStorageHelper::ERROR WE GET IS:"+e.getMessage()+"|| connection object::"+this.localDbConnection.toString());
		}	
		
		
	}
	
	public static DataStorageHelper getDataStorageHelper(){
		
		if(datastorageHelper==null){
			
			datastorageHelper=new DataStorageHelper();
		}
		
		return datastorageHelper;
		
	}
	
	
	//LOCAL DATABASE
	
	public boolean createAllTable(){
		boolean successFlag=true;
		Connection conn=null;
		Statement stmt = null;
		try {
			conn=localDbConnection.getConnection();
			stmt = conn.createStatement();

			stmt.executeUpdate(EnumQueriesToExecute.CREATEEVENT.getQuery());
			stmt.executeUpdate(EnumQueriesToExecute.CREATEEVENTDETAILS.getQuery());
			stmt.executeUpdate(EnumQueriesToExecute.CREATEFTPEVENTDETAILS.getQuery());

			stmt.close();
			localDbConnection.free(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			successFlag=false;
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
		
		return successFlag; 
	}
	
	
	//String eventName,ScheduledEventObject scheduledEventObject
	
	public boolean addNewEvent(String eventName,ScheduledEventObject scheduledEventObject){
		boolean successFlag=true;
		String fileToExecute=scheduledEventObject.getFileToExecute(),
			   dateAndTime=scheduledEventObject.getDateAndTimeInString(),
			   outputFileFormat=scheduledEventObject.getOutputFileFormat(),
			   timeRepeatOn=scheduledEventObject.getTimerRepeatOn(),
			   receipientAssress=scheduledEventObject.getRecipientAddress(),
			   uniqueId=scheduledEventObject.getUniqueId(),
			   status=scheduledEventObject.getStatus(),
			   jobKeyName=scheduledEventObject.getJobKeyName(),
			   typeOfEvent=scheduledEventObject.getTypeOfEvent();
		   
		   
		
		//INSERTEVENT("INSERT OR REPLACE INTO EVENT (NAME,FILE_TO_EXECUTE,DATE_AND_TIME,OUTPUT_FILE_FORMAT,TIME_REPEAT_ON,UNIQUE_ID,STATUS,JOB_KEY_NAME,TYPE_OF_REPORT,RECIPIENT_ADDRESS) " +
	    //"VALUES (?,?,?,?,?,?,?,?,?,?)"),
		
		//PreparedStatement stmt = c.prepareStatement(sql);
		Connection conn=null;
		PreparedStatement stmt =null;
		try {
			conn=localDbConnection.getConnection();
			stmt = conn.prepareStatement(EnumQueriesToExecute.INSERTEVENT.getQuery());
			
			stmt.setString(1,eventName);
			stmt.setString(2,fileToExecute);
			stmt.setString(3,dateAndTime);
			stmt.setString(4,outputFileFormat);
			stmt.setString(5,timeRepeatOn);
			
			stmt.setString(6,uniqueId);
			stmt.setString(7,status);
			stmt.setString(8,jobKeyName);
			
			stmt.setString(9,typeOfEvent);
			stmt.setString(10,receipientAssress);
			
			
			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			successFlag=false;
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
		
		return successFlag; 
	}
	
	//EVENT_NAME,EXECUTED_TIME,RESULT,EMAIL_STATUS
	
	//String uniqueId,DetailsOfScheduledEventObject detailEventObject
	
	public boolean addNewEventDetails(String eventName,DetailsOfScheduledEventObject detailEventObject){
		boolean successFlag=true;
		
		String executedTime=detailEventObject.getExecutedTime(), 
			  result=detailEventObject.getResult(),
			  outputFileName=detailEventObject.getOuputFileName(),
			  status=detailEventObject.getStatus();
		
		Connection conn=null;
		PreparedStatement stmt =null;
		try {
			conn=localDbConnection.getConnection();
			stmt = conn.prepareStatement(EnumQueriesToExecute.INSERTEVENTDETAIL.getQuery());
			
			//EVENT_NAME,EXECUTED_TIME,RESULT,OUTPUT_FILE_NAME,STATUS
			
			stmt.setString(1,eventName);
			stmt.setString(2,executedTime);
			stmt.setString(3,result);
			stmt.setString(4,outputFileName);
			stmt.setString(5,status);
			
			//ENVIORNMENT_NAME
			//TYPE_OF_REPORT
			
			stmt.executeUpdate();
			
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		successFlag=false;
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
		
		return successFlag; 
	}
	
	
	//FTP RELATED
	public boolean addNewFtpEventDetails(String eventName,FtpRequest ftpRequest){
		boolean successFlag=true;
		
		
		String hostName=ftpRequest.getFtpHost(),
			   hostUserName=ftpRequest.getFtpUsername(),
			   hostUserPassword=ftpRequest.getFtpPassword(),
			   ftpPath=ftpRequest.getFtpFilePath();
		
		Connection conn=null;
		PreparedStatement stmt =null;
		try {
			conn=localDbConnection.getConnection();
			stmt = conn.prepareStatement(EnumQueriesToExecute.INSERTFTPEVENTDETAIL.getQuery());
			
			stmt.setString(1,eventName);
			stmt.setString(2,hostName);
			stmt.setString(3,hostUserName);
			stmt.setString(4,hostUserPassword);
			stmt.setString(5,ftpPath);
			
			
			//ENVIORNMENT_NAME
			//TYPE_OF_REPORT
			
			stmt.executeUpdate();
			
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		successFlag=false;
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
		
		return successFlag; 
	}
	public ScheduleEventRespose getAllScheduledEvents(){
		
		ScheduleEventRespose scheduleEventRespose=new ScheduleEventRespose();
		
		//ScheduledEventObject scheduledEventObject=null;
				//ScheduleEventDetailsRespose scheduleEventDetailsRespose=null;
				//HashMap<String,ScheduledEventObject> scheduleEventMapper=eventDatabase.getScheduleEventMapper();
				//existFlag=scheduleEventMapper.containsKey(eventName);
		Connection conn=null;
		Statement stmt =null;
			   	try {
			   		 conn=localDbConnection.getConnection();
			   		 stmt = conn.createStatement();
			   		
			   		 ResultSet rs = stmt.executeQuery( EnumQueriesToExecute.GETALLEVENTS.getQuery());
			   		
			   								//scheduleEventDetailsRespose = new ScheduleEventDetailsRespose();
						
						ArrayList<ScheduledEventObject> listOfScheduledEventObject = new ArrayList<ScheduledEventObject>();
						while (rs.next()) {
							ScheduledEventObject scheduledEventObject=new ScheduledEventObject();
							scheduledEventObject.setNameOfScheduledTask(rs.getString("NAME"));
					   		scheduledEventObject.setFileToExecute(rs.getString("FILE_TO_EXECUTE"));
					   		scheduledEventObject.setDateAndTimeInString(rs.getString("DATE_AND_TIME"));
					   		scheduledEventObject.setOutputFileFormat(rs.getString("OUTPUT_FILE_FORMAT"));
					   		scheduledEventObject.setTimerRepeatOn(rs.getString("TIME_REPEAT_ON"));
					   		scheduledEventObject.setUniqueId(rs.getString("UNIQUE_ID"));
					   		scheduledEventObject.setStatus(rs.getString("STATUS"));
					   		scheduledEventObject.setJobKeyName(rs.getString("JOB_KEY_NAME"));

					   		scheduledEventObject.setTypeOfEvent(rs.getString("TYPE_OF_EVENT"));
					   		
					   		if(rs.getString("TYPE_OF_EVENT").equalsIgnoreCase(EnumConstants.EMAILTYPEEVENT.getConstantType())){
					   			scheduledEventObject.setRecipientAddress(rs.getString("RECIPIENT_ADDRESS"));
					   		}else{
					   			scheduledEventObject.setFtpRequest(getFtpEventDetail(rs.getString("NAME")));
					   		}
					   		
					   		listOfScheduledEventObject.add(scheduledEventObject);
						}
						
						scheduleEventRespose.setScheduledEventObject(listOfScheduledEventObject);
						rs.close();
			   	  
			   	} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}finally {
					closeStatementAndConnection(conn,stmt);
				}
			   	
			   	
		return scheduleEventRespose;
		
	}
	
	
	
	
	public ScheduleEventRespose getAllUnfinishedScheduledEvents(){
		
		ScheduleEventRespose scheduleEventRespose=new ScheduleEventRespose();
		
		//ScheduledEventObject scheduledEventObject=null;
				//ScheduleEventDetailsRespose scheduleEventDetailsRespose=null;
				//HashMap<String,ScheduledEventObject> scheduleEventMapper=eventDatabase.getScheduleEventMapper();
				//existFlag=scheduleEventMapper.containsKey(eventName);
		Connection conn=null;
		Statement stmt =null;
			   	try {
			   		 conn=localDbConnection.getConnection();
			   		 stmt = conn.createStatement();
			   		
			   		 ResultSet rs = stmt.executeQuery( EnumQueriesToExecute.GETALLUNFINISHEDEVENTS.getQuery());
			   		
			   								//scheduleEventDetailsRespose = new ScheduleEventDetailsRespose();
						
						ArrayList<ScheduledEventObject> listOfScheduledEventObject = new ArrayList<ScheduledEventObject>();
						while (rs.next()) {
							ScheduledEventObject scheduledEventObject=new ScheduledEventObject();
							scheduledEventObject.setNameOfScheduledTask(rs.getString("NAME"));
					   		scheduledEventObject.setFileToExecute(rs.getString("FILE_TO_EXECUTE"));
					   		scheduledEventObject.setDateAndTimeInString(rs.getString("DATE_AND_TIME"));
					   		scheduledEventObject.setOutputFileFormat(rs.getString("OUTPUT_FILE_FORMAT"));
					   		scheduledEventObject.setTimerRepeatOn(rs.getString("TIME_REPEAT_ON"));
					   		scheduledEventObject.setRecipientAddress(rs.getString("RECIPIENT_ADDRESS"));
					   		scheduledEventObject.setUniqueId(rs.getString("UNIQUE_ID"));
					   		scheduledEventObject.setStatus(rs.getString("STATUS"));
					   		scheduledEventObject.setJobKeyName(rs.getString("JOB_KEY_NAME"));
					   		
					   		scheduledEventObject.setTypeOfEvent(rs.getString("TYPE_OF_EVENT"));
					   		
					   		scheduledEventObject.setFtpRequest(getFtpEventDetail(rs.getString("NAME")));
					   		
					   		
					   		if(rs.getString("TYPE_OF_EVENT").equalsIgnoreCase(EnumConstants.EMAILTYPEEVENT.getConstantType())){
					   			scheduledEventObject.setRecipientAddress(rs.getString("RECIPIENT_ADDRESS"));
					   		}else{
					   			scheduledEventObject.setFtpRequest(getFtpEventDetail(rs.getString("NAME")));
					   		}
					   		
					   		
					   		listOfScheduledEventObject.add(scheduledEventObject);
						}
						
						scheduleEventRespose.setScheduledEventObject(listOfScheduledEventObject);
						rs.close();
			   	  
			   	} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}finally {
					closeStatementAndConnection(conn,stmt);
				}
			   	
			   	
		return scheduleEventRespose;
		
	}
	
	
	//GET EVENT
	public ScheduledEventObject getEvent(String eventName){
		
		ScheduledEventObject scheduledEventObject=null;
		//HashMap<String,ScheduledEventObject> scheduleEventMapper=eventDatabase.getScheduleEventMapper();
		//existFlag=scheduleEventMapper.containsKey(eventName);
		Connection conn=null;
		PreparedStatement stmt =null;
	   	try {
	   		 conn=localDbConnection.getConnection();
	   		 stmt = conn.prepareStatement(EnumQueriesToExecute.GETEVENTINFO.getQuery());
	   		 stmt.setString(1, eventName);
	   	  
	   		 if(stmt.execute()){
	   	  
		   		scheduledEventObject=new ScheduledEventObject();
		   		ResultSet rs=stmt.getResultSet();
		   		  
		   		scheduledEventObject.setNameOfScheduledTask(rs.getString("NAME"));
		   		scheduledEventObject.setFileToExecute(rs.getString("FILE_TO_EXECUTE"));
		   		scheduledEventObject.setDateAndTimeInString(rs.getString("DATE_AND_TIME"));
		   		scheduledEventObject.setOutputFileFormat(rs.getString("OUTPUT_FILE_FORMAT"));
		   		scheduledEventObject.setTimerRepeatOn(rs.getString("TIME_REPEAT_ON"));

		   		scheduledEventObject.setUniqueId(rs.getString("UNIQUE_ID"));
		   		scheduledEventObject.setStatus(rs.getString("STATUS"));
		   		scheduledEventObject.setJobKeyName(rs.getString("JOB_KEY_NAME"));
		   		
		   		scheduledEventObject.setTypeOfEvent(rs.getString("TYPE_OF_EVENT"));
		   		
		   		if(rs.getString("TYPE_OF_EVENT").equalsIgnoreCase(EnumConstants.EMAILTYPEEVENT.getConstantType())){
		   			scheduledEventObject.setRecipientAddress(rs.getString("RECIPIENT_ADDRESS"));
		   		}else{
		   			scheduledEventObject.setFtpRequest(getFtpEventDetail(rs.getString("NAME")));
		   		}
		   		
		   		rs.close();
	   		 }
	   	  
	   	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
	  return scheduledEventObject;
	}
	
	public ScheduleEventDetailsRespose getEventDetails(String eventName){
		
		//ScheduledEventObject scheduledEventObject=null;
		ScheduleEventDetailsRespose scheduleEventDetailsRespose=null;
		//HashMap<String,ScheduledEventObject> scheduleEventMapper=eventDatabase.getScheduleEventMapper();
		//existFlag=scheduleEventMapper.containsKey(eventName);
		
		Connection conn=null;
		PreparedStatement stmt =null;
		
	   	try {
	   		conn=localDbConnection.getConnection();
	   	    stmt = conn.prepareStatement(EnumQueriesToExecute.GETEVENTDETAILSINFO.getQuery());
	   	    stmt.setString(1, eventName);
			if (stmt.execute()) {
				// scheduledEventObject=new ScheduledEventObject();
				scheduleEventDetailsRespose = new ScheduleEventDetailsRespose();
				ResultSet rs = stmt.getResultSet();
				ArrayList<DetailsOfScheduledEventObject> listOfDetailScheduledObject = new ArrayList<DetailsOfScheduledEventObject>();
				while (rs.next()) {
					
					DetailsOfScheduledEventObject detailsOfScheduledEventObject = new DetailsOfScheduledEventObject();
					detailsOfScheduledEventObject.setExecutedTime(rs.getString("EXECUTED_TIME"));
					detailsOfScheduledEventObject.setOuputFileName(rs.getString("OUTPUT_FILE_NAME"));
					detailsOfScheduledEventObject.setResult(rs.getString("RESULT"));

					detailsOfScheduledEventObject.setStatus(rs.getString("STATUS"));
					
					
					listOfDetailScheduledObject.add(detailsOfScheduledEventObject);
				}
				scheduleEventDetailsRespose.setListOfDetailScheduledObject(listOfDetailScheduledObject);
				rs.close();
				
			}
	   	  
	   	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
	  return scheduleEventDetailsRespose;
	}
	
	//FTP RELATED
   public FtpRequest getFtpEventDetail(String eventName){
		
	   FtpRequest ftpRequest=null;
		//HashMap<String,ScheduledEventObject> scheduleEventMapper=eventDatabase.getScheduleEventMapper();
		//existFlag=scheduleEventMapper.containsKey(eventName);
		Connection conn=null;
		PreparedStatement stmt =null;
	   	try {
	   		 conn=localDbConnection.getConnection();
	   		 stmt = conn.prepareStatement(EnumQueriesToExecute.GETFTPEVENTDETAILSINFO.getQuery());
	   		 stmt.setString(1, eventName);
	   	  
	   		 if(stmt.execute()){
	   	  
	   			ftpRequest=new FtpRequest();
		   		ResultSet rs=stmt.getResultSet();
		   		  
		   		
		   		ftpRequest.setFtpHost(rs.getString("HOST_NAME"));
		   		
		   		ftpRequest.setFtpUsername(rs.getString("HOST_USERNAME"));
		   		
		   		ftpRequest.setFtpPassword(rs.getString("HOST_PASSWORD"));
		   		
		   		ftpRequest.setFtpFilePath(rs.getString("PATH_URL"));
		   		
		   		rs.close();
	   		 }
	   
	   	  
	   	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
	  return ftpRequest;
	}


	
	public boolean deleteEventRelatedData(String eventName){
		boolean successFlag=true;
		
		//PreparedStatement stmt = c.prepareStatement(sql);
		Connection conn=null;
		PreparedStatement stmt =null;
		try {
			if(checkIfEventExist(eventName)){
				conn=localDbConnection.getConnection();
				stmt = conn.prepareStatement(EnumQueriesToExecute.DELETEEVENT.getQuery());
				stmt.setString(1,eventName);
				stmt.executeUpdate();
				
				if(checkIfEventDetailsExist(eventName)){
					PreparedStatement stmt1 = conn.prepareStatement(EnumQueriesToExecute.DELETEEVENTDETAILS.getQuery());
					stmt1.setString(1,eventName);
					stmt1.executeUpdate();
					stmt1.close();
				}
				
				//FTP related
				if(checkIfEventFtpDetailsExist(eventName)){
					PreparedStatement stmt1 = conn.prepareStatement(EnumQueriesToExecute.DELETEFTPEVENTDETAILS.getQuery());
					stmt1.setString(1,eventName);
					stmt1.executeUpdate();
					stmt1.close();
				}
				
			}
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		successFlag=false;
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
		
		return successFlag; 
	}
	
	
	
	
	
	public boolean updateEventStatus(String eventName,String status){
		boolean successFlag=true;
		//PreparedStatement stmt = c.prepareStatement(sql);
		Connection conn=null;
		Statement stmt =null;
		try {
			if(checkIfEventExist(eventName)){
				conn=localDbConnection.getConnection();
				
				stmt = conn.createStatement();
				
				String updateQuery="UPDATE EVENT set STATUS = '"+status+"' where NAME='"+eventName+"'";
				
				/*String query=QueriesToExecute.UPDATEEVENTSTATUSTOEXECUTING.getQuery();
				
				if(status.equalsIgnoreCase(EnumConstants.ONETIME.getConstantType())){
					query=QueriesToExecute.UPDATEEVENTSTATUSTOFINISHED.getQuery();
				}
				
				query=query+"'"+eventName+"'";*/
				logger.info("DATASTORAGEHELPER:: QUERY WE ARE EXECUTING IS:"+updateQuery);
				stmt.executeUpdate(updateQuery);
				/*PreparedStatement stmt = conn.prepareStatement(QueriesToExecute.UPDATEEVENTSTATUS.getQuery());
				stmt.setString(1,eventName);
				stmt.setString(2,status);
				stmt.executeUpdate();*/
			}
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		successFlag=false;
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
		
		return successFlag; 
	}
	
	
	
	//CHECK IF EVENT EXIST
	public boolean checkIfEventExist(String eventName){
		
		boolean existFlag=false;	
		//HashMap<String,ScheduledEventObject> scheduleEventMapper=eventDatabase.getScheduleEventMapper();
		//existFlag=scheduleEventMapper.containsKey(eventName);
		Connection conn=null;
		PreparedStatement stmt =null;
	   	try {
	   		conn=localDbConnection.getConnection();
	   	    stmt =conn.prepareStatement(EnumQueriesToExecute.GETEVENTINFO.getQuery());
		   	stmt.setString(1, eventName);
		   	logger.info("EVENT WE ARE SEARCHING FOR IS::"+eventName);
	   	try (ResultSet rs = stmt.executeQuery()) {
	   		
	   		if (rs.next()) {
                /*boolean found = rs.getBoolean(1); // "found" column
                if (!found) {*/
                	existFlag=true;
                /*} else {
                	existFlag=false;
                }*/
            }
	   		rs.close();
	   		
	   	}
	   	  
	   	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//existFlag=false;
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
	  return existFlag;
	}
	
	
	//CHECK EVENT DETAILS EXIST
	public boolean checkIfEventDetailsExist(String eventName) {
		
		boolean existFlag=false;	
		Connection conn=null;
		PreparedStatement stmt =null;
		 
		 
	   	try {
	   		 conn=localDbConnection.getConnection();
	   	     stmt = conn.prepareStatement(EnumQueriesToExecute.GETEVENTDETAILSINFO.getQuery());
	   	     stmt.setString(1, eventName);
	   	  
	   	  try (ResultSet rs = stmt.executeQuery()) {
	   		
	   		if (rs.next()) {
                existFlag=true;
            }
	   		rs.close();
	   	}
	   	
	   	} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
	  return existFlag;
	}
	
	
	//FTP related
	public boolean checkIfEventFtpDetailsExist(String eventName) {
		
		boolean existFlag=false;	
		Connection conn=null;
		PreparedStatement stmt =null;
		 
		 
	   	try {
	   		 conn=localDbConnection.getConnection();
	   	     stmt = conn.prepareStatement(EnumQueriesToExecute.GETFTPEVENTDETAILSINFO.getQuery());
	   	     stmt.setString(1, eventName);
	   	  
	   	  try (ResultSet rs = stmt.executeQuery()) {
	   		
	   		if (rs.next()) {
                existFlag=true;
            }
	   		rs.close();
	   	}
	   	
	   	} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
	  return existFlag;
	}
	
	
	
	public boolean updateEventUniqueKey(String eventName,String uniqueKey){
		boolean successFlag=true;
		//PreparedStatement stmt = c.prepareStatement(sql);
		Connection conn=null;
		Statement stmt =null;
		try {
			if(checkIfEventExist(eventName)){
				conn=localDbConnection.getConnection();
				
				stmt = conn.createStatement();
				
				String updateQuery="UPDATE EVENT set UNIQUE_ID = '"+uniqueKey+"' where NAME='"+eventName+"'";
				
				logger.info("DATASTORAGEHELPER:: QUERY WE ARE EXECUTING IS:"+updateQuery);
				stmt.executeUpdate(updateQuery);
			}
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			successFlag=false;
		}finally {
			closeStatementAndConnection(conn,stmt);
		}
		
		return successFlag; 
		
	}
	
	
	//CLOSE CONNECTION AND STATEMENT
	private void closeStatementAndConnection(Connection conn, Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				localDbConnection.free(conn);
			}
		} catch (SQLException e) {
			logger.error("DATABASEHELPER:: ERROR WHILE CLOSING STATEMENT AND CONNECTION::"+e.getMessage());
			e.printStackTrace();
		}

	}
}
