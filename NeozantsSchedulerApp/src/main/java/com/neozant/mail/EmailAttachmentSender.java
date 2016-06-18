package com.neozant.mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.neozant.bean.EmailContent;
import com.neozant.enums.EnumConstants;

public class EmailAttachmentSender {
	final static Logger logger = Logger.getLogger(EmailAttachmentSender.class);
	
	public EmailAttachmentSender() {}
	
		
	private  void sendEmailWithAttachmentsFromProperty(EmailContent emailContent)throws AddressException, MessagingException {
		
		
		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host",emailContent.getHost());
		properties.put("mail.smtp.port", emailContent.getPort());
		
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		
		
		properties.put("mail.user", emailContent.getUserName());
		properties.put("mail.password", emailContent.getPassword());
		
		final String userName=emailContent.getUserName(),
				     passWord=emailContent.getPassword();
		

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, passWord);
			}
		};
		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		Message msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(userName));
		InternetAddress[] toAddresses = { new InternetAddress(emailContent.getToAddress()) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(emailContent.getSubject());
		msg.setSentDate(new Date());

		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(emailContent.getMessage(), "text/html");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// adds attachments
			
			
			for (String filePath : emailContent.getAttachFiles()) {
				MimeBodyPart attachPart = new MimeBodyPart();

				System.out.println("File path adding::"+filePath);
				try {
					attachPart.attachFile(filePath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(attachPart);
			}
		//}

		// sets the multi-part as e-mail's content
		msg.setContent(multipart);

		// sends the e-mail
		Transport.send(msg);

	}
	
	
	
	
	/**
	 * Test sending e-mail with attachments
	 */
	public static void main(String[] args) {
		
		EmailAttachmentSender mail=new EmailAttachmentSender();
		
		String path="C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\DESTINATION_DIRECTORY\\ROHAN.xls";
		
		//mail.sendEmailWithAttachment(path);
	}
	
	
	
	public boolean sendEmailWithAttachment(String filePath,String toAddress){
		
		//"C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\DESTINATION_DIRECTORY\\ROHAN.xls"
		boolean successFlag=true;
		try {
			sendEmailWithAttachmentsFromProperty(getEmailContent(filePath,toAddress));
			System.out.println("Email sent.");
		} catch (AddressException e) {
			successFlag=false;
			e.printStackTrace();
			System.out.println("EmailAttachmentSender:: ERROR while sending EMAIL AddressException:"+e.getMessage());
			logger.error("EmailAttachmentSender:: ERROR while sending EMAIL AddressException:"+e.getMessage());
		} catch (MessagingException e) {
			successFlag=false;
			e.printStackTrace();
			logger.error("EmailAttachmentSender:: ERROR while sending EMAIL MessagingException:"+e.getMessage());
			System.out.println("EmailAttachmentSender:: ERROR while sending EMAIL MessagingException:"+e.getMessage());
		}
		
		return successFlag;
		
		
	}
	
	
	
	private EmailContent getEmailContent(String fileToAttach,String toAddress){
	
			EmailContent emailContent=new EmailContent();
		
			Properties property=readProperty(EnumConstants.EMAILPROPERTYFILE.getConstantType());
			
			emailContent.setHost(property.getProperty("host"));
			emailContent.setPort(property.getProperty("port"));
			
			
			emailContent.setUserName(property.getProperty("user"));
			
			
			emailContent.setToAddress(toAddress);//property.getProperty("to"));
			
			
			emailContent.setPassword(property.getProperty("password"));
			emailContent.setSubject(property.getProperty("subject"));
			
			emailContent.setMessage(property.getProperty("message"));
			
			ArrayList<String> attachedFiles=new ArrayList<String>();
			attachedFiles.add(fileToAttach);
			
			emailContent.setAttachFiles(attachedFiles);
			
		
		return emailContent;
	}
	
	
	
	 private Properties readProperty(String propertyFileName){
		
		Properties dbprops = new Properties(); 
  		try {
  			InputStream inputStream=getClass().getClassLoader().getResourceAsStream(propertyFileName);//new FileInputStream(propertyFileName);
			dbprops.load(inputStream);
			//dbprops.load(new FileInputStream(propertyFileName));//"db.properties"));
		} catch (FileNotFoundException e) {
			System.out.println("EmailAttachmentSender:: ERROR UNABLE TO FIND PROPERTY FILE::"+e.getMessage());
			logger.error("EmailAttachmentSender:: ERROR UNABLE TO FIND PROPERTY FILE::"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("EmailAttachmentSender:: ERROR UNABLE TO LOAD PROPERTY FILE::"+e.getMessage());
			logger.error("EmailAttachmentSender:: ERROR UNABLE TO LOAD PROPERTY FILE::"+e.getMessage());
			e.printStackTrace();
		}

  		return dbprops;
	}
}