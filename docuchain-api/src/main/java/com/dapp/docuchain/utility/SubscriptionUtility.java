package com.dapp.docuchain.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.dapp.docuchain.dto.SubscriptionDTO;
import com.dapp.docuchain.model.SubscriptionInfo;
import com.dapp.docuchain.repository.SubscriptionInfoRepository;
import com.dapp.docuchain.service.impl.FileServiceImpl;

@Service
public class SubscriptionUtility {
	
	private final String SUBSCRIPTION_DATE_REGEX = "([0-9]{2})-([0-9]{2})-([0-9]{4})";

	@Autowired
	Environment env;
	
	@Autowired
	SubscriptionInfoRepository subscriptionInfoRepository;
	
	@Autowired
	FileServiceImpl fileServiceImpl;

	public String validateSubscriptionParameter(SubscriptionDTO subscriptionDTO) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (subscriptionDTO.getSubscriptionId() != null) {			
		    Date startDate = fileServiceImpl.parseDate(subscriptionDTO.getFromDate());
	        Date endDate = fileServiceImpl.parseDate(subscriptionDTO.getToDate());
			String fromDate = sdf.format(startDate);
			String toDate = sdf.format(endDate);
			System.out.println(fromDate); 
			System.out.println(toDate);
//			if (fromDate.equalsIgnoreCase(toDate))
//				return env.getProperty("subscription.start.end.date.not.same");
			if (endDate.compareTo(startDate) < 0) {
				return "End date is before Start date";
			} 
			//else if (endDate.compareTo(startDate) == 0) {
//				return "Start date is equal to End date";
//			} 
//			else if (startDate.compareTo(new Date()) < 0) {
//				return ("Start date is should not before Today date");
//			}

		if (subscriptionDTO.getSubscriptionId() != null) {
			if (subscriptionDTO.getSubscriptionId() == null)
				return env.getProperty("subscription.id.not.null");

			if (subscriptionInfoRepository.findOne(subscriptionDTO.getSubscriptionId()) == null)
				return env.getProperty("subscription.not.found");
		}

		if (subscriptionDTO.getShipmentCount() == null
				&& StringUtils.isBlank(subscriptionDTO.getShipmentCount().toString()))
			return env.getProperty("subscription.ship.count.not.null");
		
		if(subscriptionDTO.getFromDate() == null && StringUtils.isBlank(fromDate) && StringUtils.isEmpty(fromDate))
			return  env.getProperty("subscription.start.date.empty");
		
		if(subscriptionDTO.getToDate() == null && StringUtils.isBlank(toDate) && StringUtils.isEmpty(toDate))
			return  env.getProperty("subscription.end.date.empty");
		
		if(!fromDate.matches(SUBSCRIPTION_DATE_REGEX))
			return  env.getProperty("subscription.start.date.worng.format");
		
		if(!toDate.matches(SUBSCRIPTION_DATE_REGEX))
			return  env.getProperty("subscription.end.date.worng.format");
		}
		
	
		return env.getProperty("success");

	}

	public Date addDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	public List<SubscriptionDTO> convertSubscriptionInfotoSubscriptionInfoDTO(List<SubscriptionInfo> subscriptionInfos) {
		List<SubscriptionDTO> ListSubscriptionDTO = new ArrayList<>();
		for (SubscriptionInfo subscriptionInfo : subscriptionInfos) {
			SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
			subscriptionDTO.setSubscriptionId(subscriptionInfo.getId());
			subscriptionDTO.setSubscriptionAmount(subscriptionInfo.getSubscriptionAmount());
			subscriptionDTO.setShipmentCount(subscriptionInfo.getShipmentCount());
			subscriptionDTO.setSubscriptionDays(subscriptionInfo.getSubscriptionDays());
			subscriptionDTO.setUserCount(subscriptionInfo.getUserCount());
			//subscriptionDTO.setCreatedDate(subscriptionInfo.getCreatedDate());
			//subscriptionDTO.setEndDate(subscriptionInfo.getEndDate());
			//subscriptionDTO.setUpdatedDate(subscriptionInfo.getUpdatedDate());
			subscriptionDTO.setIsStatusAlive(subscriptionInfo.getIsStatusAlive());
			ListSubscriptionDTO.add(subscriptionDTO);
		}
		return ListSubscriptionDTO;
	}

}
