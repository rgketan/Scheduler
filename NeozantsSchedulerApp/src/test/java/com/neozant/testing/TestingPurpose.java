package com.neozant.testing;

import java.io.IOException;
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

import com.neozant.bean.EmailContent;
import com.neozant.enums.EnumConstants;
import com.neozant.mail.EmailAttachmentSender;
import com.neozant.request.ScheduleDataRequest;
import com.neozant.request.TimerData;

public class TestingPurpose {

	
	public static void main(String[] args) {
		
		TestingPurpose testing=new TestingPurpose();
		
		
		testing.testEmail();
	}
	
	
	private void testEmail(){
		
		ScheduleDataRequest schedule=getScheduleData();
		
		
		EmailContent emailContent=new EmailContent();
		
		emailContent.setHost("mail.lao.ten.fujitsu.com");
		emailContent.setPort("587");
		emailContent.setUserName("oraprod@lao.ten.fujitsu.com");
		emailContent.setPassword("ftcamis");
		
		emailContent.setToAddress(schedule.getToEmailId());
		emailContent.setMulipleAddress(schedule.getMulipleAddress());
		
		emailContent.setSubject("FU");
		emailContent.setMessage("FU MESSAGE");
		
		try {
			sendEmailWithEmailContent(emailContent);
		} catch (AddressException e) {
			System.err.print("AddressException::ERROR WHILE SENDING MESSAGE:::"+e.getMessage());
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
			System.err.print("MessagingException::ERROR WHILE SENDING MESSAGE:::"+e.getMessage());
		}
		
		/*EmailAttachmentSender sender=new EmailAttachmentSender();
		
		sender.sendEmailWithMessage(schedule.getToEmailId(), "TESTING", "TESTING", schedule.getMulipleAddress());*/
	}
	
	
	
private ScheduleDataRequest getScheduleData(){
		
		ScheduleDataRequest scheduleData=new ScheduleDataRequest();
		TimerData timerData=new TimerData();
		timerData.setDate(01);
		timerData.setHour(20);
		timerData.setMinutes(17);
		timerData.setMonth(06);
		timerData.setYear(2016);
		
		timerData.setAmPmMarker(EnumConstants.AMMARKER.getConstantType());
		
		String sqlFilePath="/Volumes/DATA/WORK/NEOZANT/EBSSqlReports/Testing.sql";
	    
		/*final String value = System.getenv("HOME");
		 System.out.println(value);
		 File f1 = new File(value);
		 System.out.println(f1);
		 System.out.println(f1.list());*/
		 
		scheduleData.setSqlFilePath(sqlFilePath);
		scheduleData.setFileFormat("xls");
		scheduleData.setOutputFileName("ROHAN");
		scheduleData.setTimerData(timerData);
		
		
		scheduleData.setToEmailId("ketan0405@gmail.com");
		ArrayList<String> mulipleAddress=new ArrayList<String>();
		mulipleAddress.add("steamtechnics@gmail.com");
		mulipleAddress.add("jija.1987@gmail.com");
		
		scheduleData.setMulipleAddress(mulipleAddress);
		
		
		
		return scheduleData;
	}
	

private  void sendEmailWithEmailContent(EmailContent emailContent)throws AddressException, MessagingException {
	

	
	
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
	
	
	InternetAddress[] toAddresses = { 
			new InternetAddress(emailContent.getToAddress()) 
	};
	
	
	
	msg.setRecipients(Message.RecipientType.TO, toAddresses);
	
	
	if (emailContent.getMulipleAddress() != null
			&& emailContent.getMulipleAddress().size() > 0) {

		InternetAddress[] recipientAddress = new InternetAddress[emailContent.getMulipleAddress().size()];
		int counter = 0;
		for (String recipient : emailContent.getMulipleAddress()) {
			recipientAddress[counter] = new InternetAddress(recipient.trim());
			counter++;
		}
		msg.setRecipients(Message.RecipientType.CC, recipientAddress);

	}
	
	
	
	
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

			//System.out.println("File path adding::"+filePath);
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
	
}
