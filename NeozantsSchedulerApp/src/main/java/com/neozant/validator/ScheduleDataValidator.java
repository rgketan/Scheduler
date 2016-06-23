package com.neozant.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import com.neozant.enums.EnumConstants;
import com.neozant.interfaces.IMessageValidator;
import com.neozant.request.GenericRequestType;
import com.neozant.request.ScheduleDataRequest;
import com.neozant.request.TimerData;
import com.neozant.response.GenericResponse;

public class ScheduleDataValidator implements IMessageValidator{

	final static Logger logger = Logger.getLogger(ScheduleDataValidator.class);
	
	public ScheduleDataValidator(){
		
	}

	@Override
	public GenericResponse validateMessage(GenericRequestType request) {
		GenericResponse response=new GenericResponse();
		
		String successMessage="success",detailMessageOnFailure=null;
		
		
		if(request.getMessageType().equals("scheduleEvent")){
			
			ScheduleDataRequest scheduleData=(ScheduleDataRequest) request;
			
			detailMessageOnFailure=scheduleMessageValidator(scheduleData);
			
			if(detailMessageOnFailure!=null){
				
				successMessage="failure";
			}
			
		}
		
		response.setResponseStatus(successMessage);
		response.setDetailMessageOnFailure(detailMessageOnFailure);
		return response;
	}
	
	
	
	public String scheduleMessageValidator(ScheduleDataRequest scheduleData){
		
		String detailMessageOnFailure=checkIfMessageNotNull(scheduleData);
		
		
		if (detailMessageOnFailure == null) {
			if (!scheduleData.getFileFormat().equalsIgnoreCase(EnumConstants.XLSFILETYPE.getConstantType())
			    && !scheduleData.getFileFormat().equalsIgnoreCase(EnumConstants.CSVFILETYPE.getConstantType())) {

				detailMessageOnFailure = "FILE FORMAT NOT SUPPORTED";
				
			}else{
				
				TimerData timerData = scheduleData.getTimerData();
				
				detailMessageOnFailure =checkIfValidDate(timerData);
				
				if(detailMessageOnFailure == null){
					
					detailMessageOnFailure =checkIfValidEmailId(scheduleData.getToEmailId());
				}
				
				
			}
		}
		
		
		return detailMessageOnFailure;
	}
	
	
	private String checkIfMessageNotNull(ScheduleDataRequest scheduleData){
		
		String messageIfnull=null;
		
		if(scheduleData.getFileFormat()==null || scheduleData.getFileFormat().isEmpty()){
			
			messageIfnull="File Formate Cannot be NULL or EMPTY";
		} else{
			if(scheduleData.getOutputFileName()==null || scheduleData.getOutputFileName().isEmpty()){
				
				messageIfnull="Ouput File Name Cannot be NULL or EMPTY";
			}else{
				if(scheduleData.getSqlFilePath()==null || scheduleData.getSqlFilePath().isEmpty()){
					
					messageIfnull="SQL File TO BE EXECUTED Cannot be NULL or EMPTY";
				}else{
					if(scheduleData.getTimerData()==null ){
						
						messageIfnull="TIMER DATE Cannot be NULL or EMPTY";
					}else{
						
						if(scheduleData.getToEmailId()==null ){
							
							messageIfnull="EMAIL ID Cannot be NULL or EMPTY";
						}
						
					}
				}	
			}
		}
		
		return messageIfnull;
	}
	
	
	
	private String checkIfValidDate(TimerData timerData){
		
		
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
			logger.info("DATE IN STRING WE GET IS::"+dateInString);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss aa");

			Date dateWeGet = sdf.parse(dateInString),
				 currentDate = new Date();
			
			logger.info("DATE WE GET FROM CLIENT::"+dateWeGet+"|| CURRENT DATE::"+currentDate);
			
			if(!currentDate.before(dateWeGet)){
				
				detailMessageOnFailure="DATE ENTERED IS NOT VALID";
				logger.info("ScheduleDataValidator:: DATE ENTERED IS NOT VALID::");
				//System.out.println("ScheduleDataValidator:: DATE ENTERED IS NOT VALID::");
			}
			
			
		} catch (ParseException e) {
			logger.error("ScheduleDataValidator:: ERROR while date parsing:"+e.getMessage());
			//System.out.println("ScheduleDataValidator:: ERROR while date parsing:"+e.getMessage());
			detailMessageOnFailure = "DATE ParseException "+e.getMessage();
			e.printStackTrace();
		}
		
		return detailMessageOnFailure;
		
	}
	
	
	private String checkIfValidEmailId(String emailId){
		
		 Pattern pattern;
		 Matcher matcher;
		
		String detailMessageOnFailure=null;
		
		final String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		pattern = Pattern.compile(EMAIL_PATTERN);
		
		matcher = pattern.matcher(emailId);
		
		if(!matcher.matches()){
			logger.error("ScheduleDataValidator:: ERROR INVALID EMAIL ID");
			System.out.println("ScheduleDataValidator:: ERROR INVALID EMAIL ID");
			
			detailMessageOnFailure="INVALID EMAIL ID";
		}
		logger.info("ScheduleDataValidator::Email VALIDATION COMPLETE");
		//System.out.println("VALIDATION COMPLETE");
		return detailMessageOnFailure;
		
	}
	
	
	/*public static void main(String[] args) {
		
		
		ScheduleDataValidator vali=new ScheduleDataValidator();
		
		String emailId="ks@s.co";
		
		//vali.checkIfValidEmailId(emailId);
		TimerData timerData=new TimerData(2016,07,19,12,1,"am");
		//if(vali.checkIfValidDate(timerData)==null?"YES":"NO");
		
		
		System.out.println("VALID DATE:::"+vali.checkIfValidDate(timerData));
	}*/
}
