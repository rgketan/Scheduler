package com.neozant.request;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "User")
public class UserRequest {

	String username;
	String password;
	String role;
	
	public UserRequest(){
		
	}

	public UserRequest(String userName, String password, String role) {
		super();
		this.username = userName;
		this.password = password;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
}
