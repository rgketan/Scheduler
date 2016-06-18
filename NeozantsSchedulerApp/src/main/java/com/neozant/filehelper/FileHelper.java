package com.neozant.filehelper;

import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.neozant.enums.EnumConstants;
import com.neozant.interfaces.IFileWriter;


public class FileHelper {
	final static Logger logger = Logger.getLogger(FileHelper.class);
	String fileFormat;
	public FileHelper(String fileFormat) {
		this.fileFormat=fileFormat;
	}

	
		
	public void writeContent(ResultSet rs, String fileName){
		
		IFileWriter fileWriter;
		
		logger.info("FileHelper:: WRITING CONTENT TO FILE::");
		System.out.println("FileHelper:: WRITING CONTENT TO FILE::"+fileFormat);
		
		if(fileFormat.equalsIgnoreCase(EnumConstants.XLSFILETYPE.getConstantType())) 
		{
			fileWriter=new WriteExcel();
		
		}else{
			
			fileWriter=new WriteCsv();
		}
		
		fileWriter.exportDataToFile(rs, fileName);
		
		
	}
	
	
	
	
	
	

}
