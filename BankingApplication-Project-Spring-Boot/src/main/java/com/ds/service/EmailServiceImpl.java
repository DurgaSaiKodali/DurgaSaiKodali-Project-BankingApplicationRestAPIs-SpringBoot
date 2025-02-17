package com.ds.service;

import java.io.File;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ds.dto.EmailDetails;

import jakarta.mail.MessagingException;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class EmailServiceImpl implements IEmailService {
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String senderEmail;
	
	@Override
	public void sendEmailDetails(EmailDetails emailDetails) {
		 
		try {
			SimpleMailMessage mailMessage=new SimpleMailMessage();
			mailMessage.setFrom(senderEmail);
			mailMessage.setTo(emailDetails.getRecipient());
			mailMessage.setText(emailDetails.getMessageBody());
			mailMessage.setSubject(emailDetails.getSubject());
			
			javaMailSender.send(mailMessage);
			
			log.info("Mail Sent Successfully");
		}catch(MailException e) {
			throw new RuntimeException(e);
		}

	}


	@Override
	public void sendEmailWithTransactionAttachment(EmailDetails emailDetails) {
	   MimeMessage myMessage=javaMailSender.createMimeMessage();
	   MimeMessageHelper myMessageHelper;
	   try {
		   myMessageHelper =new MimeMessageHelper(myMessage, true);
		   myMessageHelper.setFrom(senderEmail);
		   myMessageHelper.setTo(emailDetails.getRecipient());
		   myMessageHelper.setText(emailDetails.getMessageBody());
		   myMessageHelper.setSubject(emailDetails.getSubject());
		  
		   FileSystemResource file=new FileSystemResource(new File(emailDetails.getAttachment()));
		   myMessageHelper.addAttachment(file.getFilename(), file);
		   javaMailSender.send(myMessage);
		   
		   log.info(file.getFilename()+" has been sent to user with email  "+emailDetails.getRecipient());
		   
		   
	   }catch( MessagingException me) {
		   me.printStackTrace();
		   
	   }
		
	}

}
