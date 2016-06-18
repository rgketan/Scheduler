package com.neozant.response;

import java.util.ArrayList;

public class ConfigurationResponse extends GenericResponse{

	private ArrayList<String> listOfSourceFiles;
	private String outputFilePath;
	private String sourceFilePath;
	private ArrayList<String> fileFormatSupported;

	

	public ConfigurationResponse() {

	}

	public ConfigurationResponse(ArrayList<String> listOfSourceFiles,
			String outputFilePath, ArrayList<String> fileFormatSupported) {
		super();
		this.listOfSourceFiles = listOfSourceFiles;
		this.outputFilePath = outputFilePath;
		this.fileFormatSupported = fileFormatSupported;
		
		
	}

	public ArrayList<String> getListOfSourceFiles() {
		return listOfSourceFiles;
	}

	public void setListOfSourceFiles(ArrayList<String> listOfSourceFiles) {
		this.listOfSourceFiles = listOfSourceFiles;
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

	
}
