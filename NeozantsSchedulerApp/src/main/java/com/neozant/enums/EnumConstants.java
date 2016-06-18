package com.neozant.enums;

public enum EnumConstants {



	DBPROPERTYFILE("db.properties"),
	EMAILPROPERTYFILE("email.properties"),
	
	//SUPPORTED FORMAT
	XLSFILETYPE("xls"),
	CSVFILETYPE("csv"),
	
	//ENVIORMENT VARIABLE
	ENVFORSOURCE("SOURCE_DIRECTORY"),
	ENVFORDEST("DESTINATION_DIRECTORY"),
	
	//AM & PM MARKERS
	AMMARKER("AM"),
	PMMARKER("PM");
	
	private String constantType;  
	
	private EnumConstants(String keyName){
		this.constantType=keyName;
		
	}
	 public String getConstantType() {
		    return constantType;
	  }

}
