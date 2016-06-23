package com.neozant.interactionapi;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.neozant.request.UserRequest;
import com.neozant.response.UserResponse;

@Path("user")
public class UserApis {

	final static Logger logger = Logger.getLogger(UserApis.class);
	//http://localhost:8080/NeozantsSchedulerApp/neozant/user/testing
	
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserResponse loginUser(UserRequest user){
		
		Map<String,String> userNamePassword=new HashMap<String,String>();
		
		userNamePassword.put("admin","admin");
		userNamePassword.put("rohan","rohan");
		userNamePassword.put("k","k");
		
		logger.info("USER TRYING TO LOGIN IS::"+user.getUsername());
		
		UserResponse userResponse=new UserResponse();
		
		if(userNamePassword.containsKey(user.getUsername())&&
		   userNamePassword.get(user.getUsername()).equals(user.getPassword()))//user.getUsername().equals("admin") && user.getPassword().equals("admin"))
		{	
			userResponse.setResponseStatus("success");
		}else{
			userResponse.setResponseStatus("failure");
			userResponse.setDetailMessageOnFailure("USERNAME OR PASSWORD IS INVALID");
		}
		
		return userResponse;
	}
	
	
	@Path("/testing")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserRequest testing(){
		logger.info("TESTING USERAPI:");
		UserRequest user1=new UserRequest("ROHAN","1978","admin");
		return user1;
	}
	

	

}
