package com.neozant.enums;

import com.neozant.enums.EnumConstants;

public enum EnumQueriesToExecute {

	
	CREATEEVENT("CREATE TABLE IF NOT EXISTS EVENT " +
	                   "(NAME 				 TEXT PRIMARY KEY     NOT NULL," +
	                   " FILE_TO_EXECUTE     TEXT    NOT NULL, " +
	                   " DATE_AND_TIME       TEXT    NOT NULL, " +
	                   " OUTPUT_FILE_FORMAT  TEXT    NOT NULL, " +
	                   " TIME_REPEAT_ON      TEXT    NOT NULL, " +
	                   " UNIQUE_ID           TEXT    NOT NULL, " +
	                   " STATUS		         TEXT    NOT NULL, " +
	                   " JOB_KEY_NAME		 TEXT            , " +
	                   " TYPE_OF_EVENT       TEXT    NOT NULL, " +    //FTP OR EMAIL
	                   " RECIPIENT_ADDRESS   CHAR(50),"+     //NULL IF FTP
	                   " TIMER_INFO 	TEXT)"),            
	
	
	CREATEFTPEVENTDETAILS("CREATE TABLE IF NOT EXISTS FTPEVENTDETAILS " +
	                   "(EVENT_NAME    TEXT    NOT NULL," +
	   			       " HOST_NAME     TEXT    NOT NULL, " +
	   			       " HOST_USERNAME TEXT    NOT NULL, " +
	   			       " HOST_PASSWORD TEXT    NOT NULL, " +
	   			       " PATH_URL      TEXT    NOT NULL, " +
	   			       " FOREIGN KEY(EVENT_NAME) REFERENCES EVENT(NAME))"),                   
	                   
	                   
	                   
	CREATEEVENTDETAILS("CREATE TABLE IF NOT EXISTS EVENTDETAILS " +
			            "(EVENT_NAME 		TEXT    NOT NULL," +
			            " EXECUTED_TIME     TEXT    NOT NULL, " +
			            " RESULT	        TEXT    NOT NULL, " +
			            " OUTPUT_FILE_NAME   TEXT, "+
			            " STATUS      TEXT, "+    //FTP OR MEAIL STATUS
			            " FOREIGN KEY(EVENT_NAME) REFERENCES EVENT(NAME))"),
			            
			            
	INSERTEVENT("INSERT OR REPLACE INTO EVENT (NAME,FILE_TO_EXECUTE,DATE_AND_TIME,OUTPUT_FILE_FORMAT,TIME_REPEAT_ON,UNIQUE_ID,STATUS,JOB_KEY_NAME,TYPE_OF_EVENT,RECIPIENT_ADDRESS,TIMER_INFO) " +
            	"VALUES (?,?,?,?,?,?,?,?,?,?,?)"),		  
    
    INSERTEVENTDETAIL("INSERT OR REPLACE INTO EVENTDETAILS (EVENT_NAME,EXECUTED_TIME,RESULT,OUTPUT_FILE_NAME,STATUS) " +
                    	"VALUES (?,?,?,?,?)"),      
                    	
    INSERTFTPEVENTDETAIL("INSERT OR REPLACE INTO FTPEVENTDETAILS (EVENT_NAME,HOST_NAME,HOST_USERNAME,HOST_PASSWORD,PATH_URL) " +
                            	"VALUES (?,?,?,?,?)"),
                            	
                    	
    GETALLEVENTS("SELECT * FROM EVENT"),                	
    GETEVENTINFO("SELECT * FROM EVENT where NAME=?"),
    GETEVENTDETAILSINFO("SELECT * FROM EVENTDETAILS where EVENT_NAME=?"),         
    
    GETFTPEVENTDETAILSINFO("SELECT * FROM FTPEVENTDETAILS where EVENT_NAME=?"),
    
    
    GETALLUNFINISHEDEVENTS("SELECT * FROM 'EVENT' where status != 'FINISHED'"),
                    	
    DELETEEVENT("DELETE FROM EVENT where NAME=?"),
    
    DELETEFTPEVENTDETAILS("DELETE FROM FTPEVENTDETAILS where EVENT_NAME=?"),
    
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
