package com.neozant.timerfacility;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import com.neozant.helper.ServerHelper;
import com.neozant.mail.EmailAttachmentSender;
import com.neozant.request.ScheduleDataRequest;

public class TimerTaskToBeFired implements Job{

	final static Logger logger = Logger.getLogger(TimerTaskToBeFired.class);
	
	public TimerTaskToBeFired() {
		// TODO Auto-generated constructor stub
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
		
		try{
			ServerHelper helper=new ServerHelper();
			helper.perfomAction(scheduleData);
			logger.info("TimerTaskToBeFired:: JOB DONE FILE WITH NAME:"+scheduleData.getOutputFileName()+" IS BEEN CREATED ");
			String destinationDirectory = System.getenv("DESTINATION_DIRECTORY");
			String outputFilePath=destinationDirectory+File.separator+scheduleData.getOutputFileName()+"."+scheduleData.getFileFormat();
			logger.info("TimerTaskToBeFired:: Firing timer job with Key :: [" + jKey + "]");
			logger.info("TimerTaskToBeFired:: OUTPUT FILE WE ARE GOING TO ATTACH IS ::" +outputFilePath);
			sendEmail(outputFilePath,scheduleData.getToEmailId(),null,null,scheduleData.getMulipleAddress());
		}catch(Exception ex){
			sendEmail(null,scheduleData.getToEmailId(),"ERROR WHILE FIRING SCHEDULE EVENT","REASON FOR FAILURE IS:"+ex.getMessage(),scheduleData.getMulipleAddress());
			//System.out.println("TimerTaskToBeFired:: ERROR WHILE EXECUTING TASK");
			logger.error("TimerTaskToBeFired:: ERROR WHILE EXECUTING TASK|| SENDING EMAIL TO");
			ex.printStackTrace();
		}
		
	}
	
	
	
	private void sendEmail(String fileToAttach,String toAddress,String subject,String message,ArrayList<String> mulipleAddress){
		
		try {
			EmailAttachmentSender emailHelper = new EmailAttachmentSender();
			if(fileToAttach!=null){
				emailHelper.sendEmailWithAttachment(fileToAttach, toAddress,mulipleAddress);
			}else{
				emailHelper.sendEmailWithMessage(toAddress, subject, message,mulipleAddress);//SEND ERROR MESSAGE
			}
		}catch(Exception ex){
			logger.error("TimerTaskToBeFired:: ERROR WHILE SENDING EMAIL"+ex.getMessage());
			//System.out.println("TimerTaskToBeFired:: ERROR WHILE SENDING EMAIL");
			ex.printStackTrace();
		}
		
	}

}
