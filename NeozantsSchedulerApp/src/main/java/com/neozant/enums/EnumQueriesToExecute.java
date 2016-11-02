package com.neozant.enums;

import com.neozant.enums.EnumConstants;

public enum EnumQueriesToExecute {

	
	CREATEEVENT("CREATE TABLE IF NOT EXISTS EVENT " +
	                   "(NAME 				 TEXT PRIMARY KEY     NOT NULL," +
	                   " FILE_TO_EXECUTE     TEXT    NOT NULL, " +
	                   " DATE_AND_TIME       TEXT    NOT NULL, " +
	                   " OUTPUT_FILE_FORMAT  TEXT    NOT NULL, " +
	                   " TIME_REPEAT_ON      TEXT    NOT NULL, " +
	                   " RECIPIENT_ADDRESS   CHAR(50), " +
	                   " UNIQUE_ID           TEXT    NOT NULL, " +
	                   " STATUS		         TEXT    NOT NULL, " +
	                   " ENVIORNMENT_NAME    TEXT    NOT NULL, " +
	                   " TYPE_OF_REPORT      TEXT    NOT NULL, " +
	                   " JOB_KEY_NAME        TEXT)"),
	
	
	CREATEEVENTDETAILS("CREATE TABLE IF NOT EXISTS EVENTDETAILS " +
			            "(EVENT_NAME 		TEXT    NOT NULL," +
			            " EXECUTED_TIME     TEXT    NOT NULL, " +
			            " RESULT	        TEXT    NOT NULL, " +
			            " EMAIL_STATUS      TEXT, "+
			            " FTP_UPLOAD_STATUS TEXT, "+
			            " OUTPUT_FILE_NAME   TEXT, "+
			            " FOREIGN KEY(EVENT_NAME) REFERENCES EVENT(NAME))"),
			            
	INSERTEVENT("INSERT OR REPLACE INTO EVENT (NAME,FILE_TO_EXECUTE,DATE_AND_TIME,OUTPUT_FILE_FORMAT,TIME_REPEAT_ON,RECIPIENT_ADDRESS,UNIQUE_ID,STATUS,ENVIORNMENT_NAME,TYPE_OF_REPORT,JOB_KEY_NAME) " +
            	"VALUES (?,?,?,?,?,?,?,?,?,?,?)"),		  
    
    INSERTEVENTDETAIL("INSERT OR REPLACE INTO EVENTDETAILS (EVENT_NAME,EXECUTED_TIME,RESULT,EMAIL_STATUS,FTP_UPLOAD_STATUS,OUTPUT_FILE_NAME) " +
                    	"VALUES (?,?,?,?,?,?)"),      
                    	
    
                    	
    GETALLEVENTS("SELECT * FROM EVENT"),                	
    GETEVENTINFO("SELECT * FROM EVENT where NAME=?"),
    GETEVENTDETAILSINFO("SELECT * FROM EVENTDETAILS where EVENT_NAME=?"),                	
    
    GETALLUNFINISHEDEVENTS("SELECT * FROM 'EVENT' where status != 'FINISHED'"),
                    	
    DELETEEVENT("DELETE FROM EVENT where NAME=?"),
    DELETEEVENTDETAILS("DELETE FROM EVENTDETAILS where EVENT_NAME=?"),

    //UPDATEEVENTSTATUS("UPDATE EVENT set STATUS =? where NAME=?");
	UPDATEEVENTSTATUSTOFINISHED("UPDATE EVENT set STATUS ='"+EnumConstants.EVENTSTATUSFINISHED.getConstantType()+"' where NAME="),
	UPDATEEVENTSTATUSTOEXECUTING("UPDATE EVENT set STATUS ='"+EnumConstants.EVENTSTATUSEXECUTING.getConstantType()+"' where NAME=");
	
	
	private String query;  
	
	private EnumQueriesToExecute(String query){
		this.query=query;
		
	}
	 public String getQuery() {
		    return query;
	  }
}
