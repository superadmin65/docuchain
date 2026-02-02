package com.dapp.docuchain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dapp.docuchain.dto.SubscriptionDTO;

@Service
public interface SubscriptionService {

	public String saveSubscription(SubscriptionDTO subscriptionDTO);

	public String isSubscriptionValid(SubscriptionDTO subscriptionDTO);

	public String isSubscriptionUpdate(SubscriptionDTO subscriptionDTO);

	public String isSubscriptionDelete(SubscriptionDTO subscriptionDTO);

	public List<SubscriptionDTO> getAllTaskSubscriptionList();

	public List<SubscriptionDTO> getSubscriptionBaseOnId(Long subscriptionId);

	public List<SubscriptionDTO> listAllSubscription();

}
