package com.dapp.docuchain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.SubscriptionInfo;
import com.dapp.docuchain.model.UserProfileInfo;

public interface OrganizationInfoRepository extends JpaRepository<OrganizationInfo, Long> {

	public OrganizationInfo findByOrganizationName(String organizationName);

	public List<OrganizationInfo> findById(Long organizationId);

	public List<OrganizationInfo> findByIsActive(long l);
	
	OrganizationInfo findBySubscriptionInfo(SubscriptionInfo subscriptionInfo);

}
