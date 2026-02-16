package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import com.dapp.docuchain.model.GroupTagDocumentInfo;
import com.dapp.docuchain.model.ShipProfileInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupTagDocumentRepository extends JpaRepository<GroupTagDocumentInfo, Long> {

    GroupTagDocumentInfo findByGroupNameAndUserProfileInfoAndShipProfileInfo(String groupName, UserProfileInfo userProfileInfo, ShipProfileInfo shipProfileInfo);

    List<GroupTagDocumentInfo> findByUserProfileInfo(UserProfileInfo userProfileInfo);

    List<GroupTagDocumentInfo> findById(Long groupId);

    //GroupTagDocumentInfo findByIdAndExpiryDocumentInfosAndUserProfileInfo(Long groupId, ExpiryDocumentInfo expirydoc, UserProfileInfo userProfileInfo);

    //GroupTagDocumentInfo findByIdAndExpiryDocumentInfos(Long groupId, ExpiryDocumentInfo expirydoc);

    List<GroupTagDocumentInfo> findByShipProfileInfoAndUserProfileInfo(ShipProfileInfo shipProfileInfo, UserProfileInfo userProfileInfo);

	GroupTagDocumentInfo findByIdAndDocumentHolderInfo(Long groupId, DocumentHolderInfo documentHolderInfo);
	
	GroupTagDocumentInfo findByShipProfileInfoAndEmailIdAndMode(ShipProfileInfo shipProfileInfo,String email,String mode);

	GroupTagDocumentInfo findByGroupNameAndUserProfileInfoAndShipProfileInfoAndEmailId(String groupName,UserProfileInfo userProfileInfo, ShipProfileInfo shipProfileInfo, String emailId);

	GroupTagDocumentInfo findByUserProfileInfoAndShipProfileInfoAndEmailId(UserProfileInfo userProfileInfo,
			ShipProfileInfo shipProfileInfo, String emailId);
}
