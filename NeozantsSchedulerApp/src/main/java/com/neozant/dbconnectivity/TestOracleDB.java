package com.neozant.dbconnectivity;

import java.sql.*;  

public class TestOracleDB {

	
	/*
	 * prod db
	 * 
	 * 172.20.0.165 
	 * port : 1625 
	 * SID: PROD
	 * 
	 * login : apps 
	 * password : apps
	 */
	public static void main(String[] args) {
		String driver, 
		   url, 
		   username,
		  // port,
		   password;
		   //host="172.20.0.165";
		try{  
			driver="oracle.jdbc.driver.OracleDriver"; 
			url="jdbc:oracle:thin:@172.20.0.165:1524:PROD";
			
		//	port="1625";
			username="apps";
   		   password="apps";  
			
			//step1 load the driver class  
			Class.forName(driver);  
			  
			//step2 create  the connection object  
			Connection con=DriverManager.getConnection(url,username,password);
			
			  
			//step3 create the statement object  
			Statement stmt=con.createStatement();  
			  
			//step4 execute query  
			ResultSet rs=stmt.executeQuery("select * from FND_USER");  
			while(rs.next())  
			System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
			  
			//step5 close the connection object  
			con.close();  
			  
			}catch(Exception e){ 
				e.printStackTrace();
				System.out.println(e.getMessage());}  
			  
		
		final String destDirectory = System.getenv("DESTINATION_DIRECTORY");
		final String sourceDirectory = System.getenv("SOURCE_DIRECTORY");
		
		System.out.println("SOURCE DIRECTORY WE GET: "+sourceDirectory);
		System.out.println("DEST DIRECTORY WE GET: "+destDirectory);
			}
	
	
	
}
