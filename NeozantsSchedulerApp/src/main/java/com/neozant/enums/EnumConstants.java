package com.neozant.enums;

public enum EnumConstants {



	DBPROPERTYFILE("db.properties"),
	EMAILPROPERTYFILE("email.properties"),
	FTPPROPERTYFILE("ftp.properties"),
	
	//SUPPORTED FORMAT
	XLSFILETYPE("xls"),
	CSVFILETYPE("csv"),
	
	//ENVIORMENT VARIABLE
	ENVFORSOURCE("SOURCE_DIRECTORY"),
	ENVFORDEST("DESTINATION_DIRECTORY"),
	
	//AM & PM MARKERS
	AMMARKER("AM"),
	PMMARKER("PM"),
	
	//EVENT STATUS
	EVENTSTATUSINITIATED("INITIATED"),
	EVENTSTATUSFINISHED("FINISHED"),
	EVENTSTATUSEXECUTING("EXECUTING"),
	
	EVENTSTATUSERROR("ERROR"),
	//TIMER REPEAT ON
	DAILY("daily"),
	WEEKDAY("weekday"),
	ONETIME("onetime"),
	
	//ftp details
	FTPHOST("ftphost"),
	FTPUSERNAME("ftpusername"),
	FTPUSERPASSWORD("ftppassword"),
	
	FTPPRODUCTIONENVIORNMENT("production"),
	FTPSTAGINGENVIORNMENT("staging"),
	
	FTPCOSTSREPORT("costs"),
	FTPSALESREPORT("sales"),
	
	FTPPRODUCTIONSALESPATH("productionsalespath"),
	FTPPRODUCTIONCOSTSPATH("productioncostspath"),
	FTPSTAGINGSALESPATH("stagingsalespath"),
	FTPSTAGINGCOSTSPATH("stagingcostspath");
	
	
	
	private String constantType;  
	
	private EnumConstants(String keyName){
		this.constantType=keyName;
		
	}
	 public String getConstantType() {
		    return constantType;
	  }

}
