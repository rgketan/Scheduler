package com.neozant.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.neozant.enums.EnumConstants;
import com.neozant.helper.DataStorageHelper;
import com.neozant.interfaces.IMessageValidator;
import com.neozant.request.FtpRequest;
import com.neozant.request.GenericRequestType;
import com.neozant.request.ScheduleDataRequest;
import com.neozant.request.ScheduleEventDetailsRequest;
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
			
		}else if(request.getMessageType().equals("getScheduledEventDetails") || request.getMessageType().equals("deleteScheduledEvent")){
			
			ScheduleEventDetailsRequest scheduleData=(ScheduleEventDetailsRequest) request;
			
			detailMessageOnFailure=earlierProcessValidator(scheduleData);
			
			if(detailMessageOnFailure!=null){
				
				successMessage="failure";
			}
			
			
		}
		
		response.setResponseStatus(successMessage);
		response.setDetailMessageOnFailure(detailMessageOnFailure);
		return response;
	}
	
	
	
	public String earlierProcessValidator(ScheduleEventDetailsRequest scheduleEventDetailsRequest){
		
		String detailMessageOnFailure=null;
		
		
		if(scheduleEventDetailsRequest.getEventName()==null || scheduleEventDetailsRequest.getEventName().isEmpty()){
			
			detailMessageOnFailure="Event Name Cannot be NULL or EMPTY";
		}else{
			if(scheduleEventDetailsRequest.getUniqueId()==null || scheduleEventDetailsRequest.getUniqueId().isEmpty()){
				
				detailMessageOnFailure="Event ID Cannot be NULL or EMPTY";
			}	
			
		}
		
		
		if(detailMessageOnFailure==null){
			DataStorageHelper dataStorageHelper=DataStorageHelper.getDataStorageHelper();
			if(!dataStorageHelper.checkIfEventExist(scheduleEventDetailsRequest.getEventName())){
				
				detailMessageOnFailure="Event Name Does not EXIST";
				System.out.println("ScheduleDataValidator:: ERROR Event Name Does not EXIST::"+scheduleEventDetailsRequest.getEventName());
			}
			
		}
		
		
		
		return detailMessageOnFailure;
		
	}
	
	
	
	public String scheduleMessageValidator(ScheduleDataRequest scheduleData){
		
		String detailMessageOnFailure=checkIfMessageNotNull(scheduleData);
		
		
		if (detailMessageOnFailure == null) {
			if (!scheduleData.getFileFormat().equalsIgnoreCase(EnumConstants.XLSFILETYPE.getConstantType())
			    && !scheduleData.getFileFormat().equalsIgnoreCase(EnumConstants.CSVFILETYPE.getConstantType())) {

				detailMessageOnFailure = "FILE FORMAT NOT SUPPORTED";
				
			}else{

				//detailMessageOnFailure =checkIfEnviormentAndTypeOfReportExist(scheduleData.getEnvironmentName(),scheduleData.getTypeOfReport());
				
				if(detailMessageOnFailure == null &&  
				   scheduleData.getTypeOfEvent().equalsIgnoreCase(EnumConstants.EMAILTYPEEVENT.getConstantType())){
					
					detailMessageOnFailure =checkIfValidEmailId(scheduleData.getToEmailId());
				}
				
				if(detailMessageOnFailure==null){
					
						if(!scheduleData.isAlreadyCreated() || 
						    scheduleData.getTimerData().getRepeatOn().equalsIgnoreCase(EnumConstants.ONETIME.getConstantType())) {
							TimerData timerData = scheduleData.getTimerData();
							detailMessageOnFailure =checkIfValidDate(timerData);
						}
						//DONT CHECK IF SERVER IS CREATING THE TASK
						if(!scheduleData.isAlreadyCreated()){
								DataStorageHelper dataStorageHelper=DataStorageHelper.getDataStorageHelper();
								if(dataStorageHelper.checkIfEventExist(scheduleData.getOutputFileName())){
									logger.error("ScheduleDataValidator:: ERROR EVENT NAME ALREADY EXIST");
									detailMessageOnFailure="EVENT NAME ALREADY EXIST";
								}
						}
				}
			}
		}else{
			logger.error("ScheduleDataValidator:: ERROR found while validating ScheduleDataRequest:"+detailMessageOnFailure);
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
						
						 if(scheduleData.getTimerData().getRepeatOn()==null){
								messageIfnull="RepeatOn attribute cannot be NULL or EMPTY";
						 }else {
							 
							 if(scheduleData.getTypeOfEvent().equalsIgnoreCase(EnumConstants.EMAILTYPEEVENT.getConstantType())){
							    if(scheduleData.getToEmailId()==null ){
									messageIfnull="EMAIL ID Cannot be NULL or EMPTY";
								}
							 }else{
								 messageIfnull=checkFtpRequestIsNotNull(scheduleData.getFtpRequest());	
							 }
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
	
	
	
	private String checkFtpRequestIsNotNull(FtpRequest ftpRequest){
		String detailMessageOnFailure=null;
		if(ftpRequest==null){
			detailMessageOnFailure="FTP REQUEST DETAILS Cannot be NULL or EMPTY";
		}else{
			if(ftpRequest.getFtpFilePath()==null||
			   ftpRequest.getFtpHost()==null||
			   ftpRequest.getFtpPassword()==null||
			   ftpRequest.getFtpUsername()==null){
				detailMessageOnFailure="FTP HOST OR PATH OR USERNAME OR PASSWORD Cannot be NULL or EMPTY";
			}
			
		}
		
		
		return detailMessageOnFailure;
		
	}
	/*private String checkIfEnviormentAndTypeOfReportExist(String enviorment,String typeOfReport){
		
		String detailMessageOnFailure=null;
		
		if (!enviorment.equalsIgnoreCase(EnumConstants.FTPPRODUCTIONENVIORNMENT.getConstantType())
			    && !enviorment.equalsIgnoreCase(EnumConstants.FTPSTAGINGENVIORNMENT.getConstantType())) {

			detailMessageOnFailure = "ENVIRONMENT:"+enviorment+" IS NOT SUPPORTED";
			logger.error("ScheduleDataValidator::ENVIRONMENT:"+enviorment+" IS NOT SUPPORTED");
		}
		
		
		if(detailMessageOnFailure !=null){
			if (!typeOfReport.equalsIgnoreCase(EnumConstants.FTPCOSTSREPORT.getConstantType())
				&& !typeOfReport.equalsIgnoreCase(EnumConstants.FTPSALESREPORT.getConstantType())) {
	
				detailMessageOnFailure = "REPORT:"+enviorment+" IS NOT SUPPORTED";
				logger.error("ScheduleDataValidator::REPORT TYPE:"+typeOfReport+" IS NOT SUPPORTED");
			}
		}
			
		return detailMessageOnFailure;
	}*/
	
	
	
	/*public static void main(String[] args) {
		
		
		ScheduleDataValidator vali=new ScheduleDataValidator();
		
		String emailId="ks@s.co";
		
		//vali.checkIfValidEmailId(emailId);
		TimerData timerData=new TimerData(2016,07,19,12,1,"am");
		//if(vali.checkIfValidDate(timerData)==null?"YES":"NO");
		
		
		System.out.println("VALID DATE:::"+vali.checkIfValidDate(timerData));
	}*/
}
