package com.neozant.response;

import java.util.ArrayList;
import java.util.List;

import com.neozant.request.FtpRequest;

public class ConfigurationResponse extends GenericResponse{

	private ArrayList<SourceFileDetailResponse> listOfSourceFileDetails;
	private String outputFilePath;
	private String sourceFilePath;
	private ArrayList<String> fileFormatSupported;
	private List<String> recipientAddress;
	private FtpRequest ftpRequest;

	public ConfigurationResponse() {

	}

	public ConfigurationResponse(ArrayList<SourceFileDetailResponse> listOfSourceFiles,
			String outputFilePath, ArrayList<String> fileFormatSupported) {
		super();
		this.listOfSourceFileDetails = listOfSourceFiles;
		this.outputFilePath = outputFilePath;
		this.fileFormatSupported = fileFormatSupported;
		
	}


	public String getOutputFilePath() {
		return outputFilePath;
	}

	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	public ArrayList<String> getFileFormatSupported() {
		return fileFormatSupported;
	}

	public void setFileFormatSupported(ArrayList<String> fileFormatSupported) {
		this.fileFormatSupported = fileFormatSupported;
	}


	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	public List<String> getRecipientAddress() {
		return recipientAddress;
	}

	public void setRecipientAddress(List<String> arrayList) {
		this.recipientAddress = arrayList;
	}

	public FtpRequest getFtpRequest() {
		return ftpRequest;
	}

	public void setFtpRequest(FtpRequest ftpRequest) {
		this.ftpRequest = ftpRequest;
	}

	public ArrayList<SourceFileDetailResponse> getListOfSourceFileDetails() {
		return listOfSourceFileDetails;
	}

	public void setListOfSourceFileDetails(
			ArrayList<SourceFileDetailResponse> listOfSourceFileDetails) {
		this.listOfSourceFileDetails = listOfSourceFileDetails;
	}

	
}
