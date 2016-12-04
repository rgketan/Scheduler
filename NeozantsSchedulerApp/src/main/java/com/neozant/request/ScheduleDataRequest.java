package com.neozant.request;

import java.util.ArrayList;


public class ScheduleDataRequest extends GenericRequestType{

	private String sqlFilePath;
	private String fileFormat; // xls or csv

	private String outputFileName; //IS SAME AS EVENT NAME

	private String fromEmailId;
	
	private String toEmailId;
	
	private boolean alreadyCreated;
	
	private TimerData timerData;

	ArrayList<String> recipientAddress; 
	
	
	private String typeOfEvent;
	
	private FtpRequest ftpRequest;
	
	public ScheduleDataRequest() {

	}

	public ScheduleDataRequest(String sqlFilePath, String fileFormat,
			String outputFileName, String outputFilePath, TimerData timerData) {

		this.sqlFilePath = sqlFilePath;
		this.fileFormat = fileFormat;
		this.outputFileName = outputFileName;
		this.timerData = timerData;
	}

	public String getSqlFilePath() {
		return sqlFilePath;
	}

	public void setSqlFilePath(String sqlFilePath) {
		this.sqlFilePath = sqlFilePath;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	public TimerData getTimerData() {
		return timerData;
	}

	public void setTimerData(TimerData timerData) {
		this.timerData = timerData;
	}

	public String getFromEmailId() {
		return fromEmailId;
	}

	public void setFromEmailId(String fromEmailId) {
		this.fromEmailId = fromEmailId;
	}

	public String getToEmailId() {
		return toEmailId;
	}

	public void setToEmailId(String toEmailId) {
		this.toEmailId = toEmailId;
	}

	
	public ArrayList<String> getRecipientAddress() {
		if(recipientAddress==null){
			recipientAddress=new ArrayList<String>(); 
		}
		return recipientAddress;
	}

	public void setRecipientAddress(ArrayList<String> recipientAddress) {
		this.recipientAddress = recipientAddress;
	}

	public boolean isAlreadyCreated() {
		return alreadyCreated;
	}

	public void setAlreadyCreated(boolean alreadyCreated) {
		this.alreadyCreated = alreadyCreated;
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
