package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import com.dapp.docuchain.model.ShipProfileInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ExpiryDocumentRepository extends JpaRepository<ExpiryDocumentInfo, Long> {

    List<ExpiryDocumentInfo> findByShipProfileInfo(ShipProfileInfo shipProfileInfo);

    List<ExpiryDocumentInfo> findByDocumentHolderInfo(DocumentHolderInfo documentHolderInfo);

    List<ExpiryDocumentInfo> findByDocumentHolderInfoAndShipProfileInfo(DocumentHolderInfo documentHolderInfo, ShipProfileInfo shipProfileInfo);

    List<ExpiryDocumentInfo> findByDocumentHolderInfoAndShipProfileInfoAndCurrentVersion(DocumentHolderInfo documentHolderInfo, ShipProfileInfo shipProfileInfo, int currentVersion);

    List<ExpiryDocumentInfo> findByShipProfileInfoAndCurrentVersion(ShipProfileInfo shipProfileInfo, int currentVersion);

    List<ExpiryDocumentInfo> findByDocumentHolderInfoAndShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatus(DocumentHolderInfo documentHolderInfo, ShipProfileInfo shipProfileInfo, Integer currentVersion, String documentStatus, Integer archivedStatus, String status);

    List<ExpiryDocumentInfo> findByDocumentHolderInfoAndShipProfileInfoAndArchiveStatusAndStatus(DocumentHolderInfo documentHolderInfo, ShipProfileInfo shipProfileInfo, Integer archivedStatus, String status);
    
    List<ExpiryDocumentInfo> findByDocumentHolderInfoAndShipProfileInfoAndArchiveStatusAndStatusOrderByModifiedDateDesc(DocumentHolderInfo documentHolderInfo, ShipProfileInfo shipProfileInfo, Integer archivedStatus, String status);

    ExpiryDocumentInfo findById(Long id);

    List<ExpiryDocumentInfo> findByShipProfileInfoAndDocumentStatusAndStatus(ShipProfileInfo shipProfileInfo, String documentStatus, String status);

    List<ExpiryDocumentInfo> findByShipProfileInfoAndApprovedBy(ShipProfileInfo shipProfileInfo, UserProfileInfo userProfileInfo);

    List<ExpiryDocumentInfo> findByDocumentHolderInfoAndShipProfileInfoAndDocumentStatus(DocumentHolderInfo documentHolderInfo, ShipProfileInfo shipProfileInfo, String documentStatus);

    ExpiryDocumentInfo findExpiryDocumentInfoByDocumentHolderInfo(DocumentHolderInfo documentHolderInfo);
    
//    List<ExpiryDocumentInfo> findByShipProfileInfoOrderByModifiedDateDesc(ShipProfileInfo shipProfileInfo);
    
    List<ExpiryDocumentInfo> findByShipProfileInfoAndDocumentStatusAndStatusAndUploadedByNot(ShipProfileInfo shipProfileInfo,String documentStatus,String status,UserProfileInfo userProfileInfo);
    
    List<ExpiryDocumentInfo> findByShipProfileInfoAndApprovedByAndDocumentStatusNot(ShipProfileInfo shipProfileInfo,UserProfileInfo userProfileInfo,String status);
    
    List<ExpiryDocumentInfo> findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateAfter(ShipProfileInfo shipProfileInfo, Integer currentVersion, String documentStatus, Integer archivedStatus, String status, Date renewalDate);
    
    List<ExpiryDocumentInfo> findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateIsNull(ShipProfileInfo shipProfileInfo, Integer currentVersion, String documentStatus, Integer archivedStatus, String status);
    
    List<ExpiryDocumentInfo> findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateBefore(ShipProfileInfo shipProfileInfo, Integer currentVersion, String documentStatus, Integer archivedStatus, String status, Date currentDate);
    
    List<ExpiryDocumentInfo> findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateBetween(ShipProfileInfo shipProfileInfo, Integer currentVersion, String documentStatus, Integer archivedStatus, String status, Date renewalDate, Date currentDate);
    
	List<ExpiryDocumentInfo> findByUploadedBy(UserProfileInfo userProfileInfo);

	List<ExpiryDocumentInfo> findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatus(
			ShipProfileInfo shipProfileInfo, int i, String property, int j, String property2);

	List<ExpiryDocumentInfo> findByDocumentHolderInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatus(
			DocumentHolderInfo documentHolderInfo, int i, String property, Integer archivedStatus, String property2);
}

