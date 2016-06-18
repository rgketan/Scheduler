package com.neozant.request;


public class ScheduleDataRequest extends GenericRequestType{

	private String sqlFilePath;
	private String fileFormat; // xls or csv

	private String outputFileName;

	private String fromEmailId;
	
	private String toEmailId;
	
	private TimerData timerData;

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

	
}
