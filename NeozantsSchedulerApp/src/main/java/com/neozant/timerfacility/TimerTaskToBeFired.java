package com.neozant.timerfacility;

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
		
		logger.info("TimerTaskToBeFired:: STARTED EXECUTING");
		
		//System.out.println("TimerTaskToBeFired:: STARTED EXECUTING");
		
		JobKey jKey = ctx.getJobDetail().getKey();
		JobDataMap jMap = ctx.getJobDetail().getJobDataMap();

		
		ScheduleDataRequest scheduleData=(ScheduleDataRequest) jMap.get("scheduleData");
		
		logger.info("TimerTaskToBeFired:: ScheduleData FOUND NAME::"+scheduleData.getOutputFileName());
		
		//System.out.println("TimerTaskToBeFired:: ScheduleData FOUND NAME::"+scheduleData.getOutputFileName());
		
		
		boolean responseFlag;
		
		//ServerHelper helper=new ServerHelper();
		
		ServerHelper helper=ServerHelper.getServerHelperObject();
		responseFlag=helper.perfomAction(scheduleData);
		
		//System.out.println("TimerTaskToBeFired:: Firing timer job with Key :: [" + jKey + "]");
		
		logger.info("TimerTaskToBeFired:: Firing timer job with Key :: [" + jKey + "]");
		
		if(responseFlag){
			//System.out.println("TimerTaskToBeFired:: JOB DONE FILE WITH NAME:"+scheduleData.getOutputFileName()+" IS BEEN CREATED ");
			logger.info("TimerTaskToBeFired:: JOB DONE FILE WITH NAME:"+scheduleData.getOutputFileName()+" IS BEEN CREATED ");
			
			String destinationDirectory = System.getenv("DESTINATION_DIRECTORY");
			String outputFilePath=destinationDirectory+"\\"+scheduleData.getOutputFileName()+"."+scheduleData.getFileFormat();
			
			sendEmail(outputFilePath,scheduleData.getToEmailId());
			
		}else{
			//System.out.println("TimerTaskToBeFired:: ERROR WHILE EXECUTING TASK");
			logger.error("TimerTaskToBeFired:: ERROR WHILE EXECUTING TASK");
		}
	}
	
	
	
	private boolean sendEmail(String fileToAttach,String toAddress){
		
		boolean successFlag=true;
		
		try{
		EmailAttachmentSender emailHelper=new EmailAttachmentSender();
		
		emailHelper.sendEmailWithAttachment(fileToAttach,toAddress);
		}catch(Exception ex){
			logger.error("TimerTaskToBeFired:: ERROR WHILE SENDING EMAIL");
			//System.out.println("TimerTaskToBeFired:: ERROR WHILE SENDING EMAIL");
			successFlag=false;
			ex.printStackTrace();
		}
		
		return successFlag;
	}

}
