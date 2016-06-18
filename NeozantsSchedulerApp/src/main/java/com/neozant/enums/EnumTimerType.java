package com.neozant.enums;

public enum EnumTimerType {

	ONETIME("onetime"),
    REPEATFOREVER("repeatforever");
	
	private String timerType;  
	
	private EnumTimerType(String keyName){
		this.timerType=keyName;
		
	}
	 public String getTimerType() {
		  return timerType;
	  }
}

