package com.dapp.docuchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapp.docuchain.model.DualApprovalInfo;
import com.dapp.docuchain.model.OrganizationInfo;

public interface DualApprovalRepository extends JpaRepository<DualApprovalInfo, Long>{
	
	DualApprovalInfo findByOrganizationInfo(OrganizationInfo organizationInfo);
}
