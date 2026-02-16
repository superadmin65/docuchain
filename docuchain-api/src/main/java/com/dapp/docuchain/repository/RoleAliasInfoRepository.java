package com.dapp.docuchain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.RoleAliasInfo;
import com.dapp.docuchain.model.RoleInfo;

/**
 * This repository is used save and fetch role alias name information into database
 * 
 * @author Prabakaran
 *
 */
public interface RoleAliasInfoRepository extends JpaRepository<RoleAliasInfo, Long> {
	
	List<RoleAliasInfo> findByOrganizationInfo(OrganizationInfo organizationInfo);
	
	RoleAliasInfo findByRoleAliasNameAndOrganizationInfo(String roleAliasName, OrganizationInfo organizationInfo);
	
	RoleAliasInfo findByRoleIdAndOrganizationInfo(RoleInfo roleInfo, OrganizationInfo organizationInfo);
	
	RoleAliasInfo findByIdAndOrganizationInfo(Long roleAliasId, OrganizationInfo organizationInfo);

}
