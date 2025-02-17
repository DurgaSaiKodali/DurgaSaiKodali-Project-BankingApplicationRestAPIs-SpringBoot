package com.ds.service;

import com.ds.dto.EmailDetails;

public interface IEmailService {
   
	public void sendEmailDetails(EmailDetails emailDetails);
	public void sendEmailWithTransactionAttachment(EmailDetails emailDetails);
}
