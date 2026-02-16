package com.dapp.docuchain.service.impl;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.DocumentNotification;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.DocumentNotificationRepository;
import com.dapp.docuchain.repository.ExpiryDocumentRepository;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.service.DocumentService;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.DocumentUtility;
import com.dapp.docuchain.utility.NotificationUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentSerciveImpl implements DocumentService{

	@Autowired
	private ExpiryDocumentRepository expiryDocumentRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Autowired
	private DocumentUtility documentUtility;
	
	@Autowired
	private NotificationUtility notificationUtility;
	
	@Autowired
	private CommonMethodsUtility commonMethodsUtility;
	
	@Autowired
	Environment env;
	
	@Autowired
	DocumentNotificationRepository documentNotificationRepository;
	
	public ExpiryDocumentDTO approveDocument(ExpiryDocumentDTO expiryDocumentDTO) {
		ExpiryDocumentInfo	expiryDocumentInfo = expiryDocumentRepository.findOne(expiryDocumentDTO.getId());
		if (expiryDocumentDTO.getStatus().equals(env.getProperty("document.status.reject"))){
			expiryDocumentInfo.setDocumentStatus(env.getProperty("document.status.reject"));
			UserProfileInfo userProfileInfo = userProfileRepository.findById(expiryDocumentDTO.getApproverId());
			if (userProfileInfo != null) {
				expiryDocumentInfo.setApprovedBy(userProfileInfo);
			}
			if (expiryDocumentDTO.getRemarks() != null)
				expiryDocumentInfo.setRemarks(expiryDocumentDTO.getRemarks());
			expiryDocumentInfo = expiryDocumentRepository.save(expiryDocumentInfo);
			if(expiryDocumentInfo!=null){
                commonMethodsUtility.maintainHistory(expiryDocumentInfo.getId(),expiryDocumentInfo.getDocumentName(),"Document", env.getProperty("history.rejected"), expiryDocumentDTO.getLoginId());
                }
			notificationUtility.notifyDocumentChanges(expiryDocumentDTO.getApproverId(), expiryDocumentInfo);
			expiryDocumentDTO =  documentUtility.convertExpiryDocumentInfotoExpiryDocumentDTO(expiryDocumentInfo);
			
			return expiryDocumentDTO;
		}else{
		System.out.println(expiryDocumentInfo);
		if (expiryDocumentInfo != null){
			List<ExpiryDocumentInfo> expiryDocumentInfos = expiryDocumentRepository.findByDocumentHolderInfoAndShipProfileInfoAndCurrentVersion(expiryDocumentInfo.getDocumentHolderInfo(), expiryDocumentInfo.getShipProfileInfo(), 1);
			if (expiryDocumentInfos != null && expiryDocumentInfos.size() >0){
				for (ExpiryDocumentInfo expiryDocumentInfo2 : expiryDocumentInfos) {
					expiryDocumentInfo2.setCurrentVersion(0);
					expiryDocumentInfo2 = expiryDocumentRepository.save(expiryDocumentInfo2);
					
				}
			}
			UserProfileInfo userProfileInfo = userProfileRepository.findById(expiryDocumentDTO.getApproverId());
			if (userProfileInfo != null) {
				expiryDocumentInfo.setApprovedBy(userProfileInfo);
			}
			if (expiryDocumentDTO.getRemarks() != null)
			expiryDocumentInfo.setRemarks(expiryDocumentDTO.getRemarks());
			expiryDocumentInfo.setDocumentStatus(env.getProperty("document.status.approve"));
			expiryDocumentInfo.setCurrentVersion(1);
			expiryDocumentInfo = expiryDocumentRepository.save(expiryDocumentInfo);
			if(expiryDocumentInfo!=null){
                commonMethodsUtility.maintainHistory(expiryDocumentInfo.getId(),expiryDocumentInfo.getDocumentName(),"Document", env.getProperty("history.approved"), expiryDocumentDTO.getLoginId());
                }
			notificationUtility.notifyDocumentChanges(expiryDocumentDTO.getApproverId(), expiryDocumentInfo);
			expiryDocumentDTO =  documentUtility.convertExpiryDocumentInfotoExpiryDocumentDTO(expiryDocumentInfo);
			
			return expiryDocumentDTO;
		}
		}
		
		return null;
	}

	public List<UserDTO> getNotification() {

		List<UserDTO> userDTOs = new ArrayList<>();
		List<DocumentNotification> documentNotificationInfo = documentNotificationRepository.findAll();
		if (documentNotificationInfo != null && documentNotificationInfo.size() > 0) {

			userDTOs = documentUtility.findNotificationColor(documentNotificationInfo);
			if (userDTOs != null && userDTOs.size() > 0) {
				return userDTOs;
			}
		}

		return null;
	}

}