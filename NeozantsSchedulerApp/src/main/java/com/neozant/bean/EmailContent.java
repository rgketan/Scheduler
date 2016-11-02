package com.neozant.bean;

import java.util.ArrayList;

public class EmailContent {

	String host;
	String port;
	String userName;
	String password; 
	String toAddress;
	String subject;
	String message; 
	String debug;
	String auth;
	String enable;
	
	ArrayList<String> attachFiles;
	
	ArrayList<String> mulipleAddress; 
	
	
	
	public EmailContent(){
		
	}
	
	
	
	public EmailContent(String host, String port, String userName,
			String password, String toAddress, String subject, String message,
			ArrayList<String> attachFiles) {
		super();
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.toAddress = toAddress;
		this.subject = subject;
		this.message = message;
		this.attachFiles = attachFiles;
	}
	
	
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<String> getAttachFiles() {
		
		if(attachFiles==null){
			attachFiles=new ArrayList<String>(); 
		}
		return attachFiles;
	}
	public void setAttachFiles(ArrayList<String> attachFiles) {
		this.attachFiles = attachFiles;
	}



	public ArrayList<String> getMulipleAddress() {
		if(mulipleAddress==null){
			mulipleAddress=new ArrayList<String>(); 
		}
		return mulipleAddress;
	}



	public void setMulipleAddress(ArrayList<String> mulipleAddress) {
		this.mulipleAddress = mulipleAddress;
	}



	public String getDebug() {
		return debug;
	}



	public void setDebug(String debug) {
		this.debug = debug;
	}



	public String getAuth() {
		return auth;
	}



	public void setAuth(String auth) {
		this.auth = auth;
	}



	public String getEnable() {
		return enable;
	}



	public void setEnable(String enable) {
		this.enable = enable;
	}
	
}
