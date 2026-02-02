package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.ShipProfileInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShipProfileRepository extends JpaRepository<ShipProfileInfo, Long> {

	List<ShipProfileInfo> findAll();

	ShipProfileInfo findByIMO(Long imo);

	ShipProfileInfo findById(Long id);

	ShipProfileInfo findByShipName(String shipName);

	ShipProfileInfo findByShipMaster(UserProfileInfo userProfile);

	// List<ShipProfileInfo> findByCommercialMaster(UserProfileInfo
	// userProfileInfo);

	// List<ShipProfileInfo> findByTechMaster(UserProfileInfo userProfileInfo);
	ShipProfileInfo findByShipMasterAndStatus(UserProfileInfo userProfile, int i);

	List<ShipProfileInfo> findShipProfileInfoByShipMasterAndStatusAndShipOrganizationInfo(UserProfileInfo userProfile, int i , OrganizationInfo organizationInfo);
	

	List<ShipProfileInfo> findByCommercialMasters_UserNameAndStatusAndShipOrganizationInfo(String userName, int status ,OrganizationInfo organizationInfo);

	List<ShipProfileInfo> findByTechMasters_UserNameAndStatusAndShipOrganizationInfo(String userName, int status, OrganizationInfo organizationInfo);

	List<ShipProfileInfo> findByStatus(Integer status);
	
	List<ShipProfileInfo> findByStatusAndShipOrganizationInfo(int status, OrganizationInfo organizationInfo);

	List<ShipProfileInfo> findShipProfileInfoByShipName(String shipName);

	List<ShipProfileInfo> findByShipOrganizationInfo(OrganizationInfo organizationInfo);
	
	List<ShipProfileInfo> findByShipOrganizationInfoAndStatus(OrganizationInfo organizationInfo, int status);
	
	List<ShipProfileInfo> findByShipMaster_UserNameAndStatusAndShipOrganizationInfo(String userName, int status, OrganizationInfo organizationInfo);
	
	ShipProfileInfo findByIdAndShipOrganizationInfo(Long shipProfileId, OrganizationInfo organizationInfo);
	
	List<ShipProfileInfo> findByStatusAndCommercialMasters(int status, UserProfileInfo userProfileInfo);
	
	List<ShipProfileInfo> findByStatusAndTechMasters(int status, UserProfileInfo userProfileInfo);
	
	List<ShipProfileInfo> findByShipOrganizationInfoAndTechMasters(OrganizationInfo organizationInfo, UserProfileInfo userProfileInfo);
	
	List<ShipProfileInfo> findByShipOrganizationInfoAndCommercialMasters(OrganizationInfo organizationInfo, UserProfileInfo userProfileInfo);
}
