package com.neozant.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.neozant.enums.EnumConstants;


public class FtpServerHelper {

	final static Logger logger = Logger.getLogger(FtpServerHelper.class);
	
	public boolean uploadFIleToFtpServer(String fileToUpload,String environment,String typeOfReport){
		boolean successFlag=true;
		
		Properties ftpProps = new Properties(); 
		FTPClient client = new FTPClient();
		FileInputStream fis = null;
		
  		try {
  			InputStream inputStream=getClass().getClassLoader().getResourceAsStream(EnumConstants.FTPPROPERTYFILE.getConstantType());
			ftpProps.load(inputStream);
			
			String hostName=ftpProps.getProperty(EnumConstants.FTPHOST.getConstantType()),
				   ftpUserName=ftpProps.getProperty(EnumConstants.FTPUSERNAME.getConstantType()),
				   ftpPassowrd=ftpProps.getProperty(EnumConstants.FTPUSERPASSWORD.getConstantType()),
				   directoryToUploadFile=ftpProps.getProperty(getFileUploaderPath(environment,typeOfReport));
			
			logger.info("FtpServerHelper::PATH OF DIRECTORY TO UPLOAD FILE WE GET IS:"+directoryToUploadFile);
			
			
			logger.info("FtpServerHelper::USERNAME:"+ftpUserName+" PASSWORD::"+ftpPassowrd);
			
			
			client.connect(hostName);

			boolean loginFlag = client.login(ftpUserName,ftpPassowrd);

			if (loginFlag) {
				System.out.println("logged in successfully");
				logger.info("FtpServerHelper:: LOGGED IN SUCCESSFULLY");

				client.setFileType(FTP.BINARY_FILE_TYPE);
				client.changeWorkingDirectory(directoryToUploadFile);

				File file = new File(fileToUpload);
				String fileName = file.getName();

				fis = new FileInputStream(file);

				// Store file on server and logout
				boolean fileStored = client.storeFile(fileName, fis);

				if (!fileStored) {
					successFlag=false;
					System.out.println("unable to store");
					logger.error("FtpServerHelper:: UNABLE TO STORE FILE");
				} else {
					System.out.println("STORED");
					logger.info("FtpServerHelper:: FILE STORED SUCCESSFULLY");
				}
			} else {
				successFlag=false;
				System.out.println("logged in unsuccessfull");
				logger.error("FtpServerHelper:: logged in unsuccessfull");
			}
			client.logout();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("FtpServerHelper:: UNABLE TO FIND PROPERTY FILE::"+e.getMessage());
			e.printStackTrace();
			successFlag=false;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("FtpServerHelper:: UNABLE TO LOAD PROPERTY FILE::"+e.getMessage());
			e.printStackTrace();
			successFlag=false;
		} catch(Exception e){
		
			logger.error("FtpServerHelper:: ERROR WHILE UPLOADING FILE TO FTP SERVER::"+e.getMessage());
			e.printStackTrace();
			successFlag=false;
			
		}finally {
			try {
				if (fis != null) {
					fis.close();
				}
				client.disconnect();
				ftpProps.clear();
			} catch (IOException e) {
				logger.error("FtpServerHelper:: ERROR WHILE CLOSING CONNECTION::"+e.getMessage());
			}
		}
		return successFlag;
	}
	
	
	
	private String getFileUploaderPath(String enviornment,String typeOfReport){
		
		String uploadPath;
			if(EnumConstants.FTPPRODUCTIONENVIORNMENT.getConstantType().equalsIgnoreCase(enviornment)){
				
				if(EnumConstants.FTPCOSTSREPORT.getConstantType().equalsIgnoreCase(typeOfReport)){
					uploadPath=EnumConstants.FTPPRODUCTIONCOSTSPATH.getConstantType();
				}else{
					uploadPath=EnumConstants.FTPPRODUCTIONSALESPATH.getConstantType();
				}
			}else{
				if(EnumConstants.FTPCOSTSREPORT.getConstantType().equalsIgnoreCase(typeOfReport)){
					uploadPath=EnumConstants.FTPSTAGINGCOSTSPATH.getConstantType();
				}else{
					uploadPath=EnumConstants.FTPSTAGINGSALESPATH.getConstantType();
				}
				
			}
		
			logger.info("FtpServerHelper::KEY VALUE OF UPLOADED PATH WE GET IS:"+uploadPath);
		return uploadPath;
	}
	
}
