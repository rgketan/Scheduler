package com.neozant.response;

public class GenericResponse {

	private String responseStatus;
	private String detailMessageOnFailure;
	
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getDetailMessageOnFailure() {
		return detailMessageOnFailure;
	}
	public void setDetailMessageOnFailure(String detailMessageOnFailure) {
		this.detailMessageOnFailure = detailMessageOnFailure;
	}
	
	
}
