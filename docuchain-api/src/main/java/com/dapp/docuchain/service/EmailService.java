package com.dapp.docuchain.service;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.GroupTagDTO;
import com.dapp.docuchain.model.UserReportAnIssueInfo;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    public String SendEmailWithAttachment(ExpiryDocumentDTO expiryDocumentDTO);

    public boolean forgotPasswordNotificationEmail(String mail, String string, String forgetPassword);

    //public String shareExpDoc(GroupTagDTO groupTagDTO);

	public String shareGroupExpiryDocument(GroupTagDTO group);
	
	
	public String sendReportAnIssueNotification(UserReportAnIssueInfo userReportAnIssueInfo);
	
	public boolean sendEmailDEV(String toEamilId, String subject, String content);
	

}
