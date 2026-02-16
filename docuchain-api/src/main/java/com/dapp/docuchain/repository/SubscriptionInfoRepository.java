package com.dapp.docuchain.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.SubscriptionInfo;

public interface SubscriptionInfoRepository extends JpaRepository<SubscriptionInfo, Long> {

	public List<SubscriptionInfo> findById(Long subscriptionId);

	public List<SubscriptionInfo> findByIsStatusAliveAndSubscriptionExpireDateBefore(int i, Date currentDate1);

	public List<SubscriptionInfo> findByIsStatusAliveAndSubscriptionExpireDateBetween(int i, Date currentDate1,
			Date renewalDate);

}
