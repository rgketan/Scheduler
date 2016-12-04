package com.neozant.storage;

import java.math.BigInteger;


public class DetailsOfScheduledEventObject {

	
	private BigInteger uniqueId;
	private String executedTime;
	
	private String result;
	private String ouputFileName;
	private String status;
	
	
	public BigInteger getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(BigInteger uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public String getExecutedTime() {
		return executedTime;
	}
	public void setExecutedTime(String executedTime) {
		this.executedTime = executedTime;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getOuputFileName() {
		return ouputFileName;
	}
	public void setOuputFileName(String ouputFileName) {
		this.ouputFileName = ouputFileName;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
