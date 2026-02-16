package com.dapp.docuchain.service;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.GroupTagDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupTagService {

    String validateSourceExist(GroupTagDTO groupTagDTO);

    boolean saveGroupTag(GroupTagDTO groupTagDTO);

    boolean isGroupExts(Long groupId);

    boolean updateGroupTag(GroupTagDTO groupTagDTO);

    boolean isUserProfileIdExits(Long userProfileId);

    List<GroupTagDTO> getAllGroupInfo(Integer userProfileId);

    boolean deleteGroup(GroupTagDTO groupTagDTO);

    String isValidateExpiryTagGroupParamExist(GroupTagDTO groupTagDTO);

    String isExpiryTagGroupExits(GroupTagDTO groupTagDTO);

    boolean saveExpiryTagGroup(GroupTagDTO groupTagDTO);

    String isExpiryTagGroupExitsOrNot(GroupTagDTO groupTagDTO);

    boolean deleteExipryDocumentInGroup(GroupTagDTO groupTagDTO);

    List<ExpiryDocumentDTO> getShipExipryDocumentInGroup(GroupTagDTO groupTagDTO);

    List<ExpiryDocumentDTO> getDocumentDataBasedExpiryList(GroupTagDTO groupTagDTO);

    //List<ExpiryDocumentDTO> getDocumentDataAndShipBasedExpiryList(GroupTagDTO groupTagDTO);

    List<ExpiryDocumentDTO> getAllExpiredocumentinGroup(Long groupId);

    boolean isGroupExpiredocumentExits(Long groupId);

    String validateShipExists(Long shipId);

    List<GroupTagDTO> getAllGroupInfoByShip(GroupTagDTO groupTagDTO);

    String verifyShareDocumentParam(GroupTagDTO groupTagDTO);

    String shareExpiryDocument(GroupTagDTO groupTagDTO);

    GroupTagDTO viewGroup(Long groupId);

	boolean isGroupForwardExpiredocumentExits(Long groupId);

	List<GroupTagDTO> getAllSenItemsInGroup(Long groupId);

	boolean isForwardExts(Long groupId);
	
	List<ExpiryDocumentDTO> checkGroupAlreadyExists(GroupTagDTO groupTagDTO);

	boolean saveGroupwithExp(GroupTagDTO groupTagDTO);

	boolean updateGroupTagwithExp(GroupTagDTO groupTagDTO);

	String validateSourceExistForAddExp(GroupTagDTO groupTagDTO);

	String validateUpdateGroupParam(GroupTagDTO groupTagDTO);

	GroupTagDTO remainingDocument(Long groupId);
	
	



}
