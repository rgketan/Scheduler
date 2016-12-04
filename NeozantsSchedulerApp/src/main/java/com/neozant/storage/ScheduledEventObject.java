package com.neozant.storage;

import com.neozant.request.FtpRequest;


public class ScheduledEventObject {

	
	private String nameOfScheduledTask;
	private String fileToExecute;
	private String dateAndTimeInString;
	
	private String outputFileFormat;
	
	private String timerRepeatOn;
	
	
	private String recipientAddress;
	
	//Details of scheduleEvent will be save against this Id
	private String uniqueId; 
	
	//Details of scheduleEvent
	//private ArrayList<DetailsOfScheduledEventObject> listOfDetailScheduledObject;
	
	//Status will be INITIATED/EXECUTING/FINISHED
	private String status;
	
	//TO UNSCHEDULE THE TASK
	private String jobKeyName;
	
	//FTP OR EMAIL
	private String typeOfEvent;
	
	//FTP DETAILS
	private FtpRequest ftpRequest;
	
	
	public ScheduledEventObject(){
		
		//listOfDetailScheduledObject=new ArrayList<DetailsOfScheduledEventObject>();
	}
	
	public String getNameOfScheduledTask() {
		return nameOfScheduledTask;
	}

	public void setNameOfScheduledTask(String nameOfScheduledTask) {
		this.nameOfScheduledTask = nameOfScheduledTask;
	}

	public String getFileToExecute() {
		return fileToExecute;
	}

	public void setFileToExecute(String fileToExecute) {
		this.fileToExecute = fileToExecute;
	}

	public String getDateAndTimeInString() {
		return dateAndTimeInString;
	}

	public void setDateAndTimeInString(String dateAndTimeInString) {
		this.dateAndTimeInString = dateAndTimeInString;
	}

	public String getOutputFileFormat() {
		return outputFileFormat;
	}

	public void setOutputFileFormat(String outputFileFormat) {
		this.outputFileFormat = outputFileFormat;
	}

	public String getTimerRepeatOn() {
		return timerRepeatOn;
	}

	public void setTimerRepeatOn(String timerRepeatOn) {
		this.timerRepeatOn = timerRepeatOn;
	}

	public String getRecipientAddress() {
		return recipientAddress;
	}

	public void setRecipientAddress(String recipientAddress) {
		this.recipientAddress = recipientAddress;
	}

	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

/*	public ArrayList<DetailsOfScheduledEventObject> getListOfDetailScheduledObject() {
		return listOfDetailScheduledObject;
	}

	public void setListOfDetailScheduledObject(
			ArrayList<DetailsOfScheduledEventObject> listOfDetailScheduledObject) {
		this.listOfDetailScheduledObject = listOfDetailScheduledObject;
	}

	public void addDetailScheduledObject(DetailsOfScheduledEventObject detailsOfScheduledEventObject ){
		this.listOfDetailScheduledObject.add(detailsOfScheduledEventObject);
	}*/
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJobKeyName() {
		return jobKeyName;
	}

	public void setJobKeyName(String jobKeyName) {
		this.jobKeyName = jobKeyName;
	}

	public String getTypeOfEvent() {
		return typeOfEvent;
	}

	public void setTypeOfEvent(String typeOfEvent) {
		this.typeOfEvent = typeOfEvent;
	}

	public FtpRequest getFtpRequest() {
		return ftpRequest;
	}

	public void setFtpRequest(FtpRequest ftpRequest) {
		this.ftpRequest = ftpRequest;
	}
	
	
	
}
