package com.neozant.dbconnectivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class GetDbConnection {

	final static Logger logger = Logger.getLogger(GetDbConnection.class);
	
	private String driver, url, username, password;
	
	private Connection availableConnection;
	
	
	public GetDbConnection(String propertyFileName)
		      throws Exception {
		

  		Properties dbprops = new Properties(); 
  		try {
  			InputStream inputStream=getClass().getClassLoader().getResourceAsStream(propertyFileName);
			dbprops.load(inputStream);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("ConnectionPool:: UNABLE TO FIND PROPERTY FILE::"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ConnectionPool:: UNABLE TO LOAD PROPERTY FILE::"+e.getMessage());
			e.printStackTrace();
		}
	    this.driver = dbprops.get("connection.driver").toString();
	    this.url = dbprops.get("connection.url").toString();
	    this.username = dbprops.get("user").toString();
	    this.password = dbprops.get("password").toString();
	     
	    logger.info("GetDbConnection::InitialConnections POOL::USER:"+this.username+"||PASSWORD::"+this.password);
	   
	    
	   // logger.info("ConnectionPool::NEW IS ESTABLISHED");
	    
	    //System.out.println("CONNECTION ESTABLISHED::"+toString());
  
	}
	
	 private Connection makeNewConnection()
		      throws SQLException {
		    try {
		      // Load database driver if not already loaded
		      Class.forName(driver);
		      // Establish network connection to database
		      Connection connection =
		        DriverManager.getConnection(url, username, password);
		      return(connection);
		    } catch(ClassNotFoundException cnfe) {
		      // Simplify try/catch blocks of people using this by
		      // throwing only one exception type.
		    	logger.error("GetDbConnection::Can't find class for driver: "+cnfe.getMessage());
		    	cnfe.printStackTrace();
		      throw new SQLException("Can't find class for driver: "+driver);
		    }catch(SQLException ex){
		    	logger.error("GetDbConnection::Could not connect to D/B : "+url+"| with User Name:"+username);
		    	ex.printStackTrace();
		    	throw new SQLException("Could not connect to D/B:" +url);
		    }
	}
	 
	 public synchronized Connection getConnection()
		      throws SQLException {
		 
		 this.availableConnection=makeNewConnection();
		 
		 return this.availableConnection;
		 
	 }
	 
	 
	 
	 
}
