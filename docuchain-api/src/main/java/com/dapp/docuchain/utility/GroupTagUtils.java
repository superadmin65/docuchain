package com.dapp.docuchain.utility;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.GroupTagDTO;
import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import com.dapp.docuchain.model.GroupTagDocumentInfo;
import com.dapp.docuchain.repository.DocumentDataRepository;
import com.dapp.docuchain.repository.DocumentHolderRepository;
import com.dapp.docuchain.repository.ExpiryDocumentRepository;
import com.dapp.docuchain.repository.ShipProfileRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupTagUtils {

	@Autowired
	private ShipProfileRepository shipProfileRepository;

	@Autowired
	private DocumentHolderRepository documentHolderRepository;

	@Autowired
	private DocumentDataRepository documentDataRepository;

	@Autowired
	private ExpiryDocumentRepository expiryDocumentRepository;

	public boolean isValidateTagGroupParam(GroupTagDTO groupTagDTO) {
		if (groupTagDTO.getGroupName() != null && StringUtils.isNotBlank(groupTagDTO.getGroupName())
				&& (groupTagDTO.getUserProfileId() != null&& StringUtils.isNotBlank(groupTagDTO.getUserProfileId().toString()))
				&& (groupTagDTO.getShipIds()!= null &&groupTagDTO.getShipIds().length>0)
				&& (groupTagDTO.getEmailId()!= null && StringUtils.isNotBlank(groupTagDTO.getEmailId().toString()))) {
			return true;
		}
		return false;
	}

	public boolean isValidateUpdateTagGroupParam(GroupTagDTO groupTagDTO) {
		if (groupTagDTO.getGroupName() != null && StringUtils.isNotBlank(groupTagDTO.getGroupName())
				&& groupTagDTO.getId() != null && StringUtils.isNotBlank(groupTagDTO.getId().toString())
				&& (groupTagDTO.getUserProfileId() != null
						&& StringUtils.isNotBlank(groupTagDTO.getUserProfileId().toString()))) {
			return true;
		}
		return false;
	}

	public boolean isValidateExpiryTagGroupParam(GroupTagDTO groupTagDTO) {
		if (groupTagDTO.getDocumentHolderId() != null
				&& StringUtils.isNotBlank(groupTagDTO.getDocumentHolderId().toString())
				&& groupTagDTO.getGroupId() != null && StringUtils.isNotBlank(groupTagDTO.getGroupId().toString())
				&& (groupTagDTO.getUserProfileId() != null
						&& StringUtils.isNotBlank(groupTagDTO.getUserProfileId().toString()))) {
			return true;
		}
		return false;
	}

	public ExpiryDocumentDTO convertExpiryDocumentInfoInToExpiryDocumentDTO(GroupTagDocumentInfo groupTagDocumentInfo,
			ExpiryDocumentInfo expirydoc) {
		ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
		expiryDocumentDTO.setId(expirydoc.getId());
		expiryDocumentDTO.setGroupId(groupTagDocumentInfo.getId());
		expiryDocumentDTO.setDocumentName(expirydoc.getDocumentName());
		expiryDocumentDTO.setDocumentHolderId(expirydoc.getDocumentHolderInfo().getId());
		expiryDocumentDTO.setDocumentDataId(expirydoc.getDocumentDataInfo().getId());
		expiryDocumentDTO.setGroupName(groupTagDocumentInfo.getGroupName());
		expiryDocumentDTO.setCertificateNumber(expirydoc.getCertificateNumber());
		expiryDocumentDTO.setPlaceOfIssue(expirydoc.getPlaceOfIssue());
		expiryDocumentDTO.setIssueDate(expirydoc.getIssueDate());
		expiryDocumentDTO.setExpiryDate(expirydoc.getExpiryDate());
		expiryDocumentDTO.setLastAnnual(expirydoc.getLastAnnual());
		expiryDocumentDTO.setNextAnnual(expirydoc.getNextAnnual());
		return expiryDocumentDTO;
	}

	public ExpiryDocumentDTO convertDocumentHolderInfoToExpiryDocumentDTO(GroupTagDocumentInfo groupTagDocumentInfo,
			DocumentHolderInfo documentHolderInfo) {

		ExpiryDocumentInfo expirydoc = expiryDocumentRepository
				.findExpiryDocumentInfoByDocumentHolderInfo(documentHolderInfo);
		ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
		if (expirydoc != null) {
			 expiryDocumentDTO = new ExpiryDocumentDTO();
			expiryDocumentDTO.setId(expirydoc.getId());
			expiryDocumentDTO.setGroupId(groupTagDocumentInfo.getId());
			expiryDocumentDTO.setDocumentName(expirydoc.getDocumentName());
			expiryDocumentDTO.setDocumentHolderId(expirydoc.getDocumentHolderInfo().getId());
			expiryDocumentDTO.setDocumentDataId(expirydoc.getDocumentDataInfo().getId());
			expiryDocumentDTO.setGroupName(groupTagDocumentInfo.getGroupName());
			expiryDocumentDTO.setCertificateNumber(expirydoc.getCertificateNumber());
			expiryDocumentDTO.setPlaceOfIssue(expirydoc.getPlaceOfIssue());
			expiryDocumentDTO.setIssueDate(expirydoc.getIssueDate());
			expiryDocumentDTO.setExpiryDate(expirydoc.getExpiryDate());
			expiryDocumentDTO.setLastAnnual(expirydoc.getLastAnnual());
			expiryDocumentDTO.setNextAnnual(expirydoc.getNextAnnual());
		}
		return expiryDocumentDTO;
	}

	public boolean isvalidateUpdateExpGroupParam(GroupTagDTO groupTagDTO) {
		if (groupTagDTO.getGroupId() != null && StringUtils.isNotBlank(groupTagDTO.getGroupId().toString())
				&& (groupTagDTO.getUserProfileId() != null
						&& StringUtils.isNotBlank(groupTagDTO.getUserProfileId().toString()))
				&& (groupTagDTO.getDocumentHolderIds()!= null && groupTagDTO.getDocumentHolderIds().length>0)) {
			return true;
		}
		return false;
	}

	public String validateSourceExistForAddExp(GroupTagDTO groupTagDTO) {
		
		
		if (!(groupTagDTO.getGroupName() != null && StringUtils.isNotBlank(groupTagDTO.getGroupName()))) {
			return "Group name is missing";
		}
		
		if (!(groupTagDTO.getUserProfileId() != null&& StringUtils.isNotBlank(groupTagDTO.getUserProfileId().toString()))) {
			return "user detail is missing";
		}
		
		if (!(groupTagDTO.getShipIds()!= null &&groupTagDTO.getShipIds().length>0)) {
			return "Please select ship";
		}
		
		if (!(groupTagDTO.getEmailId()!= null && StringUtils.isNotBlank(groupTagDTO.getEmailId().toString()))) {
			return "Please enter email to create group";
		}
		return "success";
//		if (groupTagDTO.getGroupName() != null && StringUtils.isNotBlank(groupTagDTO.getGroupName())
//				&& (groupTagDTO.getUserProfileId() != null&& StringUtils.isNotBlank(groupTagDTO.getUserProfileId().toString()))
//				&& (groupTagDTO.getShipIds()!= null &&groupTagDTO.getShipIds().length>0)
//				&& (groupTagDTO.getEmailId()!= null && StringUtils.isNotBlank(groupTagDTO.getEmailId().toString()))) {
//			return true;
//		}
//		return false;
	}
}
