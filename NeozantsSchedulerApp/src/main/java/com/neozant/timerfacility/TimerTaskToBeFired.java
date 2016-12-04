package com.neozant.timerfacility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import com.neozant.enums.EnumConstants;
import com.neozant.helper.DataStorageHelper;
import com.neozant.helper.FtpServerHelper;
import com.neozant.helper.ServerHelper;
import com.neozant.mail.EmailAttachmentSender;
import com.neozant.request.FtpRequest;
import com.neozant.request.ScheduleDataRequest;
import com.neozant.storage.DetailsOfScheduledEventObject;
import com.neozant.storage.ScheduledEventObject;

public class TimerTaskToBeFired implements Job{

	final static Logger logger = Logger.getLogger(TimerTaskToBeFired.class);
	
	public TimerTaskToBeFired() {
	}

	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		
		//System.out.println("TimerTaskToBeFired:: STARTED EXECUTING");
		JobKey jKey = ctx.getJobDetail().getKey();
		JobDataMap jMap = ctx.getJobDetail().getJobDataMap();

		logger.info("TimerTaskToBeFired:: Firing timer job with Key :: [" + jKey + "]");
		
		ScheduleDataRequest scheduleData=(ScheduleDataRequest) jMap.get("scheduleData");
		
		logger.info("TimerTaskToBeFired:: ScheduleData FOUND NAME::"+scheduleData.getOutputFileName());
		
		//System.out.println("TimerTaskToBeFired:: ScheduleData FOUND NAME::"+scheduleData.getOutputFileName());
		//boolean responseFlag;
		//ServerHelper helper=new ServerHelper();
		String outputFilePath=null;
		String result="EXECUTED",
			   status="UPLOADED";
		try{
			ServerHelper helper=ServerHelper.getServerHelperObject();
			outputFilePath=helper.perfomAction(scheduleData);
			
			logger.info("TimerTaskToBeFired:: OUTPUT FILE WE ARE GOING TO ATTACH IS ::" +outputFilePath);
			
			
			if(scheduleData.getTypeOfEvent().equalsIgnoreCase(EnumConstants.FTPTYPEEVENT.getConstantType())){
				
				FtpRequest ftpRequest=scheduleData.getFtpRequest();
				logger.info("TimerTaskToBeFired:: UPLOADING FILE TO FTP SERVER :: HOST:" +ftpRequest.getFtpHost()
						    +"  USERNAME:" +ftpRequest.getFtpUsername()
							+ "	 PASSWORD:" +ftpRequest.getFtpPassword()
							+ "  PATH:"+ftpRequest.getFtpFilePath());
				
				FtpServerHelper ftpServerHelper=new FtpServerHelper();
				boolean fileUploadStatus=ftpServerHelper.uploadFIleToFtpServer(ftpRequest, outputFilePath);
				
				if(!fileUploadStatus){
					status="FAILED TO UPLOAD TO FTP SERVER";
				}else{
					status="UPLOADED";
				}
				
			}else{
			//SENDING EMAIL
				boolean emailStatusFlag=sendEmail(outputFilePath,scheduleData.getToEmailId(),null,null,scheduleData.getRecipientAddress());
				if(!emailStatusFlag){
					status="Failed to send";
				}else{
					status="SENT";
				}
			}
			
			//TODO: REMOVING FOR NOW
			/*//UPLOADING TO FTP SERVER
			logger.info("TimerTaskToBeFired:: UPLOADING FILE TO FTP SERVER ::" +scheduleData.getEnvironmentName()+" TYPE OF REPORT::"+scheduleData.getTypeOfReport());
			
			FtpServerHelper ftpServerHelper=new FtpServerHelper();
			boolean fileUploadStatus=ftpServerHelper.uploadFIleToFtpServer(outputFilePath, scheduleData.getEnvironmentName(), scheduleData.getTypeOfReport());
			
			if(!fileUploadStatus){
				fileUpload="FAILED TO UPLOAD";
			}*/
			
		}catch(Exception ex){
			result="FAILED";
			
			if(scheduleData.getTypeOfEvent().equalsIgnoreCase(EnumConstants.FTPTYPEEVENT.getConstantType()))
				status="FAILED TO UPLOAD TO FTP SERVER";
			else
				status="FAILED TO SEND EMAIL";
			
			sendEmail(null,scheduleData.getToEmailId(),"ERROR WHILE FIRING SCHEDULE EVENT","REASON FOR FAILURE IS:"+ex.getMessage(),scheduleData.getRecipientAddress());
			//System.out.println("TimerTaskToBeFired:: ERROR WHILE EXECUTING TASK");
			logger.error("TimerTaskToBeFired:: ERROR WHILE EXECUTING TIMER TASK|| SENDING EMAIL TO");
			ex.printStackTrace();
		}
		//String eventName,String emailStatus,String executedTime,String ouputFileName,String result
		addDetailsScheduleEvent(scheduleData.getOutputFileName(),outputFilePath,result,status);
		
	}
	
	
	
	private boolean sendEmail(String fileToAttach,String toAddress,String subject,String message,ArrayList<String> mulipleAddress){
		
		EmailAttachmentSender emailHelper = new EmailAttachmentSender();
		boolean successFlag=true;
		try {
			
			if(fileToAttach!=null){
				successFlag=emailHelper.sendEmailWithAttachment(fileToAttach, toAddress,mulipleAddress);
			}else{
				successFlag=emailHelper.sendEmailWithMessage(toAddress, subject, message,mulipleAddress);//SEND ERROR MESSAGE
			}
		}catch(Exception ex){
			successFlag=false;
			logger.error("TimerTaskToBeFired:: ERROR WHILE SENDING EMAIL"+ex.getMessage());
			//System.out.println("TimerTaskToBeFired:: ERROR WHILE SENDING EMAIL");
			emailHelper.sendEmailWithMessage(toAddress, "ERROR WHILE SENDING EMAIL", ex.getMessage(),null);//SEND ERROR MESSAGE
			ex.printStackTrace();
		}
		
		return successFlag;
		
	}

	
	private void addDetailsScheduleEvent(String eventName,String ouputFileName,String result,String eventStatus){
		DataStorageHelper dataStorageHelper=DataStorageHelper.getDataStorageHelper();
		ScheduledEventObject scheduledEventObject=dataStorageHelper.getEvent(eventName);
		if(scheduledEventObject!=null){
			
			   SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy,hh:mm:ss:aa");
		    
		    
			  // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd||HH-mm-ss");
			   //get current date time with Date()
			   Date date = new Date();
			   //System.out.println(dateFormat.format(date));
			   
			   
			DetailsOfScheduledEventObject detailScheduledEventObject=new DetailsOfScheduledEventObject();
			
			
			detailScheduledEventObject.setExecutedTime(dateFormat.format(date));
			detailScheduledEventObject.setOuputFileName(ouputFileName);
			detailScheduledEventObject.setResult(result);
			
			detailScheduledEventObject.setStatus(eventStatus);
			dataStorageHelper.addNewEventDetails(eventName, detailScheduledEventObject);
			
			
			logger.info("TimerTaskToBeFired:: UPDATING EVENT STATUS OF EVENT ::" +eventName+" BY::"+scheduledEventObject.getTimerRepeatOn());
			
			String status=EnumConstants.EVENTSTATUSEXECUTING.getConstantType();
			if(scheduledEventObject.getTimerRepeatOn().equalsIgnoreCase("onetime")){
				status=EnumConstants.EVENTSTATUSFINISHED.getConstantType();
			}
			dataStorageHelper.updateEventStatus(eventName,status);
			
		}
	}
}
