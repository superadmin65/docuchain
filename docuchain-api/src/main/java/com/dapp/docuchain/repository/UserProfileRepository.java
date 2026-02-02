package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.RoleInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfileInfo, Long> {

    UserProfileInfo findByUserName(String userName);
    
    UserProfileInfo findByUserNameAndStatus(String userName, Long status);

    UserProfileInfo findById(Long id);

    List<UserProfileInfo> findByRoleId(RoleInfo roleInfo);
    
	List<UserProfileInfo> findByStatus(Long i);
		
	//List<UserProfileInfo> findByAdminInfo(UserProfileInfo adminInfo);

	List<UserProfileInfo> findByOrganizationInfo(OrganizationInfo organizationInfo);
	
	List<UserProfileInfo> findByOrganizationInfoAndStatus(OrganizationInfo organizationInfo, Long i);
	
	List<UserProfileInfo> findByOrganizationInfoAndRoleIdAndStatus(OrganizationInfo organizationInfo, RoleInfo roleInfo, Long status);

}
