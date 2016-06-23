package com.neozant.testing;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.neozant.enums.EnumConstants;
import com.neozant.request.ScheduleDataRequest;
import com.neozant.request.TimerData;
import com.neozant.timerfacility.TimerTaskManager;

public class Testing {

	//TimerTaskManager timerTaskManager;
	Testing(){
		//timerTaskManager=new TimerTaskManager("API");
		
	
	}
	
	private void testTimerTask(){

		TimerTaskManager timerTaskManager=new TimerTaskManager("API");
		timerTaskManager.start();
		try {
			timerTaskManager.scheduleTimerTask(getScheduleData(),"1");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	private void testEnviornmentVariables(){
		try{
		final String value = System.getenv("ANT_HOMEs");
		 System.out.println(value);
		 /*File f1 = new File(value);
		 
		 System.out.println(f1);
		// System.out.println(f1.list());
		 
		 ArrayList<String> listOfFiles=new ArrayList<String>();
			
		 File[] fileObjects = f1.listFiles();
		 
		 
		 for(File file : fileObjects){
			 listOfFiles.add(file.getAbsolutePath());
			 System.out.println("FILE PATH WE GET::"+file.getAbsolutePath());
		 }*/
		 
		}catch(Exception ex){
			ex.printStackTrace();
		}
		 
		 
	}
	public static void main(String[] args) {
		Testing testing=new Testing();
		
		//testing.testEnviornmentVariables();
		ScheduleDataRequest scheduleData=testing.getScheduleData();
		
		
		
		testing.tesCSV();
		
		
	
	}

	
	private void tesCSV(){
		
			try {
				Connection con=getDBConnection();
		
				Statement stmt = con.createStatement();
				
				ResultSet res=stmt.executeQuery("select * from client"); 
				
				 int colunmCount = res.getMetaData().getColumnCount();
				 
				 FileWriter fw = new FileWriter("TEMP.csv");
				 
				 
				 for(int i=1 ; i<= colunmCount ;i++)
                 {
                     fw.append(res.getMetaData().getColumnName(i));
                     fw.append(",");
          
                 }
                  
                 fw.append(System.getProperty("line.separator"));
                 
                 
                 
                 
                 
                 while(res.next())
                 {
                     for(int i=1;i<=colunmCount;i++)
                     {
                          
                         //you can update it here by using the column type but i am fine with the data so just converting 
                         //everything to string first and then saving
                         if(res.getObject(i)!=null)
                         {
                         String data= res.getObject(i).toString();
                         fw.append(data) ;
                         fw.append(",");
                         }
                         else
                         {
                             String data= "null";
                             fw.append(data) ;
                             fw.append(",");
                         }
                     }
                     //new line entered after each row
                     fw.append(System.getProperty("line.separator"));
                 }
                  fw.flush();
                  fw.close();
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	
	private Connection getDBConnection(){
		
		java.sql.Connection con = null;
        String url = "jdbc:mysql://localhost:3306/";
        String db = "BILLING_SYSTEM";
        String driver = "com.mysql.jdbc.Driver";
        String user = "root";
        String pass = "root";
        
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url+db, user, pass);
        }
        catch (ClassNotFoundException e){
	        System.err.println("Could not load JDBC driver");
	        e.printStackTrace();
        } catch (SQLException e) {
			// TODO Auto-generated catch block
        	System.err.println("SQLException while loading JDBC driver");
			e.printStackTrace();
		} 
		
		
		
		return con;
	}
	
	private void compareDate(ScheduleDataRequest scheduleData){
		try {
			System.out.println("VALID::"+new SimpleDateFormat("MM/yyyy").parse("06/2016").compareTo(new Date()));
			
			
			
			TimerData timerData=scheduleData.getTimerData();
			
			String dateInString = timerData.getDate()+"/"+timerData.getMonth()+"/"+timerData.getYear()+" "+timerData.getHour()+":"+timerData.getMinutes()+":00 "+timerData.getAmPmMarker();
			
			
			System.out.println("DATE IN STRING WE GET ON CONVERTING:"+dateInString);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
		    
			
		    Date date= sdf.parse(dateInString);
		    
		    //System.out.println("Time in 24Hours ="+new SimpleDateFormat("HH:mm").format(date));
		    
		    System.out.println("DATE WE GET IS::"+date  );
		    
		    Date currentDate=new Date();
		    
		    
		    
		    
		    System.out.println("CURRENT DATE WE GET IS::"+currentDate  );
		    System.out.println("==>>VALUE WE GET IS::"+currentDate.before(date));
		    
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String input = "23/12/2014 12:22:12";
		convert12To24(input);
		
	}
	
	private void convert12To24(String input){
		
		
		      //String input = "23/12/2014 10:22:12 PM";
		      //Format of the date defined in the input String
		      DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
		      //Desired format: 24 hour format: Change the pattern as per the need
		      DateFormat outputformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		      
		      
		      Date date = null;
		      
		      
		      String output = null;
		      try{
		         //Converting the input String to Date
		    	 date= df.parse(input);
		         //Changing the format of date and storing it in String
		    	 output = outputformat.format(date);
		         //Displaying the date
		    	 System.out.println(output);
		      }catch(ParseException pe){
		         pe.printStackTrace();
		       }
		  
	}
	
	private Properties readProperty(String propertyFileName){
		
		Properties dbprops = new Properties(); 
  		try {
  			
  			InputStream inputStream=getClass().getClassLoader().getResourceAsStream(propertyFileName);//new FileInputStream(propertyFileName);
  			
			dbprops.load(inputStream);
			
			//dbprops.load(new FileInputStream(propertyFileName));//"db.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("ConnectionPool:: UNABLE TO FIND PROPERTY FILE::"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ConnectionPool:: UNABLE TO LOAD PROPERTY FILE::"+e.getMessage());
			e.printStackTrace();
		}

  		return dbprops;
  		
	}
	
	
	
	
	private ScheduleDataRequest getScheduleData(){
		
		ScheduleDataRequest scheduleData=new ScheduleDataRequest();
		TimerData timerData=new TimerData();
		
		
		timerData.setDate(13);
		
		timerData.setHour(03);
		
		timerData.setMinutes(48);
		
		timerData.setMonth(06);
		
		timerData.setYear(2016);
		
		timerData.setAmPmMarker(EnumConstants.PMMARKER.getConstantType());
		
		String sqlFilePath="C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY\\TEST.sql";
		
		//String outputFilePath = "/Volumes/DATA/WORK/NEOZANT/EBSSqlReports/";
	    
		
		
		scheduleData.setSqlFilePath(sqlFilePath);
		//scheduleData.setOutputFilePath(outputFilePath);
		
		scheduleData.setFileFormat("xls");
		scheduleData.setOutputFileName("ROHAN");
		scheduleData.setTimerData(timerData);
		
		return scheduleData;
	}
}
