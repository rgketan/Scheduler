package com.neozant.testing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;

import com.neozant.request.TimerData;

public class TestTimeZone {
public static void main(String[] args) {
	
	Date date=new Date();
	
	System.out.println("ADTE: "+date.toString());
	
	TimerData timerDate=new TimerData(2019, /*year*/
									  05,/*month*/
									  05,/*date*/
									  11,/*hour*/
									  9,/*min*/
									  "am"/*ampm*/
									  );
	System.out.println("VALID: "+checkIfValidDate(timerDate));
	
	javaTimeZone();
	//TestingPurpose testing=new TestingPurpose();
	//testing.testWritingExcel();
}
	

static String checkIfValidDate(TimerData timerData){
	
	
	String detailMessageOnFailure=null;
	try {
		//TimerData timerData = scheduleData.getTimerData();

		String dateInString = timerData.getDate() + "-"
							+ timerData.getMonth() + "-" 
							+ timerData.getYear()  + " " 
							+ timerData.getHour() + ":"
							+ timerData.getMinutes() + ":00"
							+ " "+timerData.getAmPmMarker();
		

		//System.out.println("DATE IN STRING WE GET IS::"+dateInString);
		System.out.println("DATE IN STRING WE GET IS::"+dateInString);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss aa");

		Date dateWeGet = sdf.parse(dateInString),
			 currentDate = new Date();
		
		System.out.println("DATE WE GET FROM CLIENT::"+dateWeGet+"|| CURRENT DATE::"+currentDate);
		
		if(!currentDate.before(dateWeGet)){
			
			detailMessageOnFailure="DATE ENTERED IS NOT VALID";
			System.out.println("ScheduleDataValidator:: DATE ENTERED IS NOT VALID::");
			//System.out.println("ScheduleDataValidator:: DATE ENTERED IS NOT VALID::");
		}
		
		
		
	} catch (ParseException e) {
		System.out.println("ScheduleDataValidator:: ERROR while date parsing:"+e.getMessage());
		//System.out.println("ScheduleDataValidator:: ERROR while date parsing:"+e.getMessage());
		detailMessageOnFailure = "DATE ParseException "+e.getMessage();
		e.printStackTrace();
	}
	
	return detailMessageOnFailure;
	
}

static void javaTimeZone(){
	 String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
        String dateInString = "22-1-2015 10:15:55 AM";
        LocalDateTime ldt = LocalDateTime.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));

        ZoneId singaporeZoneId = ZoneId.of("Asia/Singapore");
        System.out.println("TimeZone : " + singaporeZoneId);

        //LocalDateTime + ZoneId = ZonedDateTime
        ZonedDateTime asiaZonedDateTime = ldt.atZone(singaporeZoneId);
        System.out.println("Date (Singapore) : " + asiaZonedDateTime);

        ZoneId newYokZoneId = ZoneId.of("America/New_York");
        System.out.println("TimeZone : " + newYokZoneId);

        ZonedDateTime nyDateTime = asiaZonedDateTime.withZoneSameInstant(newYokZoneId);
        System.out.println("Date (New York) : " + nyDateTime);

        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);
        System.out.println("\n---DateTimeFormatter---");
        System.out.println("Date (Singapore) : " + format.format(asiaZonedDateTime));
        System.out.println("Date (New York) : " + format.format(nyDateTime));

}
		


	
}
