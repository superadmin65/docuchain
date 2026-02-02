package com.dapp.docuchain.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.dapp.docuchain.dto.DeletedHistoryDTO;
import com.dapp.docuchain.dto.SubscriptionDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.DataDeletedHistoryInfo;
import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.SubscriptionInfo;
import com.dapp.docuchain.model.User;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.OrganizationInfoRepository;
import com.dapp.docuchain.repository.SubscriptionInfoRepository;
import com.dapp.docuchain.repository.UserRepository;
import com.dapp.docuchain.service.SubscriptionService;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.SubscriptionUtility;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
	
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	
	private final String SUBSCRIPTION_DATE_REGEX = "([0-9]{2})-([0-9]{2})-([0-9]{4})";
	
	private final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	@Autowired
	private Environment env;
	
	@Autowired
	private SubscriptionUtility subscriptionUtility;
	
	@Autowired
	private SubscriptionInfoRepository subscriptionInfoRepository;
	
	@Autowired
	private CommonMethodsUtility commonMethodsUtility;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FileServiceImpl fileServiceImpl;
	
	@Autowired
	private OrganizationInfoRepository organizationInfoRepository;
	

	@Override
	public String saveSubscription(SubscriptionDTO subscriptionDTO) {
		// TODO Auto-generated method stub
		SubscriptionInfo subscriptionInfo = new SubscriptionInfo();
		subscriptionInfo.setSubscriptionAmount(subscriptionDTO.getSubscriptionAmount());
		subscriptionInfo.setShipmentCount(subscriptionDTO.getShipmentCount());
		subscriptionInfo.setSubscriptionDays(subscriptionDTO.getSubscriptionDays());
		subscriptionInfo.setUserCount(subscriptionDTO.getUserCount());
		//subscriptionInfo.setCreatedDate(new Date());
		//subscriptionInfo.setEndDate(subscriptionUtility.addDays(new Date(), subscriptionDTO.getSubscriptionDays().intValue()));
		subscriptionInfo.setIsStatusAlive(subscriptionDTO.getIsStatusAlive());
		SubscriptionInfo subscription=subscriptionInfoRepository.save(subscriptionInfo);
		if(subscription!=null){
			commonMethodsUtility.maintainHistory(subscription.getId(),subscription.getSubscriptionAmount().toString(),"Subscription", env.getProperty("history.created"), subscriptionDTO.getLoginId());
		}
		
		return env.getProperty("success");
	}

	@Override
	public String isSubscriptionValid(SubscriptionDTO subscriptionDTO) {		
		if(subscriptionDTO.getSubscriptionId() ==null)
		{
			return env.getProperty("failure");
		}				
		SubscriptionInfo subscriptionInfo = subscriptionInfoRepository.findOne(subscriptionDTO.getSubscriptionId());
		if(subscriptionInfo!=null)
		{
			return env.getProperty("success");
		}
		return env.getProperty("failure");
	}

	@Override
	public String isSubscriptionUpdate(SubscriptionDTO subscriptionDTO) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		SubscriptionInfo subscriptionInfo = subscriptionInfoRepository.findOne(subscriptionDTO.getSubscriptionId()); 
		if(subscriptionInfo!=null)
		{					
			if (subscriptionInfo != null) {			
				subscriptionInfo.setShipmentCount(subscriptionDTO.getShipmentCount());
				subscriptionInfo.setUserVesslesRatio(subscriptionDTO.getUserVesslesRatio());
				long usercount=subscriptionDTO.getShipmentCount()*subscriptionDTO.getUserVesslesRatio();
				subscriptionInfo.setUserCount(usercount);
				//subscriptionInfo.setSubscriptionAmount(subscriptionDTO.getSubscriptionAmount());
				subscriptionInfo.setIsStatusAlive(1);
				try {
					  Date startDate = fileServiceImpl.parseDate(subscriptionDTO.getFromDate());
				        Date endDate = fileServiceImpl.parseDate(subscriptionDTO.getToDate());
						String fromDate = sdf.format(startDate);
						String toDate = sdf.format(endDate);
						System.out.println("fromDate::::::::::"+fromDate); //15/10/2013
						System.out.println("toDate::::::::::"+toDate);
					subscriptionInfo.setSubscriptionDays(Long.valueOf(daysBetweenTwoDates(sdf.parse(fromDate), sdf.parse(toDate))));
					subscriptionInfo.setSubscriptionStartDate(sdf.parse(fromDate));
					subscriptionInfo.setSubscriptionExpireDate(sdf.parse(toDate));
					/*subscriptionInfo.setSubscriptionExpireDate(DATE_FORMAT.parse(DATE_FORMAT
							.format(getDaysInDate(organizationDTO.getSubscriptionInfo().getSubscriptionDays()))));*/
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			SubscriptionInfo subscription=subscriptionInfoRepository.save(subscriptionInfo);
			if(subscription!=null){
				OrganizationInfo organizationInfo = organizationInfoRepository.findBySubscriptionInfo(subscription);
				if(organizationInfo != null){
					commonMethodsUtility.maintainHistory(subscription.getId(),organizationInfo.getOrganizationName(),"Subscription", env.getProperty("history.updated"), subscriptionDTO.getUserId());
				}
				//commonMethodsUtility.maintainHistory(subscription.getId(),subscription.getSubscriptionDays().toString(),"Subscription", env.getProperty("history.updated"), subscriptionDTO.getUserId());
			}
			return env.getProperty("success");
		}
		return env.getProperty("failure");
	}
	
	private int daysBetweenTwoDates(Date startDate, Date endDate){
		return Days.daysBetween(new LocalDate(startDate.getTime()), new LocalDate(endDate.getTime())).getDays();
    }

	@Override
	public String isSubscriptionDelete(SubscriptionDTO subscriptionDTO) {
		// TODO Auto-generated method stub
		
		SubscriptionInfo subscriptionInfo = subscriptionInfoRepository.findOne(subscriptionDTO.getSubscriptionId());
		if(subscriptionInfo!=null)
		{
			OrganizationInfo organizationInfo = organizationInfoRepository.findBySubscriptionInfo(subscriptionInfo);
			
			 DeletedHistoryDTO deletedHistoryDTO=new DeletedHistoryDTO();
	            deletedHistoryDTO.setObjectId(subscriptionInfo.getId());
	            deletedHistoryDTO.setObjectOne(subscriptionInfo.getShipmentCount().toString());
	            deletedHistoryDTO.setObjectTwo(subscriptionInfo.getSubscriptionAmount().toString());
	            DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
	            subscriptionInfoRepository.delete(subscriptionInfo);
	            if(dataDeletedHistoryInfo!=null){
	            	if(organizationInfo != null){
	    				commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),subscriptionInfo.getSubscriptionAmount().toString(),"Subscription", env.getProperty("history.Deleted"), subscriptionDTO.getLoginId());
	    			}
	            }
	            //commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),subscriptionInfo.getSubscriptionAmount().toString(),"Subscription", env.getProperty("history.Deleted"), subscriptionDTO.getLoginId());
			return env.getProperty("success");
		}
		return env.getProperty("failure");
	}

	@Override
	public List<SubscriptionDTO> getAllTaskSubscriptionList() {
		 List<SubscriptionDTO> subscriptionDTOs = new ArrayList<>();
	        List<SubscriptionInfo> subscriptionInfos = subscriptionInfoRepository.findAll();
	        if (subscriptionInfos != null) {
	        	subscriptionDTOs = subscriptionUtility.convertSubscriptionInfotoSubscriptionInfoDTO(subscriptionInfos);
	            return subscriptionDTOs;
	        }
	        return null;
	    }

	@Override
	public List<SubscriptionDTO> getSubscriptionBaseOnId(Long subscriptionId) {
		List<SubscriptionDTO> organizationDTOs = new ArrayList<>();
        List<SubscriptionInfo> subscriptionInfos =  subscriptionInfoRepository.findById(subscriptionId);
        if (subscriptionInfos != null) {
        	organizationDTOs = subscriptionUtility.convertSubscriptionInfotoSubscriptionInfoDTO(subscriptionInfos);
            return organizationDTOs;
        }
        return null;
	}

	@Override
	public List<SubscriptionDTO> listAllSubscription() {
		return convertSubscriptionInfosToSubscriptionDTOs(subscriptionInfoRepository.findAll());
	}
	
	private List<SubscriptionDTO> convertSubscriptionInfosToSubscriptionDTOs(List<SubscriptionInfo> subscriptionInfos){
		List<SubscriptionDTO> subscriptionDTOs = new ArrayList<>();
		Set<UserDTO> userDTOs = new HashSet<>();
		for(SubscriptionInfo subscriptionInfo : subscriptionInfos){
			SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
			subscriptionDTO.setSubscriptionId(subscriptionInfo.getId());
			subscriptionDTO.setShipmentCount(subscriptionInfo.getShipmentCount());
			subscriptionDTO.setUserCount(subscriptionInfo.getUserCount());
			subscriptionDTO.setActivationDate(DATE_FORMAT.format(subscriptionInfo.getSubscriptionStartDate()));
			subscriptionDTO.setExpireDate(DATE_FORMAT.format(subscriptionInfo.getSubscriptionExpireDate()));
			subscriptionDTO.setOrganizationId(subscriptionInfo.getOrganizationInfo().getId());
			subscriptionDTO.setOrganizationName(subscriptionInfo.getOrganizationInfo().getOrganizationName());
			subscriptionDTO.setRegisterNumber(subscriptionInfo.getOrganizationInfo().getRegistrationNumber());
			subscriptionDTO.setUserVesslesRatio(subscriptionInfo.getUserVesslesRatio());
			subscriptionDTO.setFromDate(DATE_FORMAT.format(subscriptionInfo.getSubscriptionStartDate()));
			subscriptionDTO.setToDate(DATE_FORMAT.format(subscriptionInfo.getSubscriptionExpireDate()));
			DateTime currentDate = new DateTime();
			DateTime expireMinusDate = new DateTime(subscriptionInfo.getSubscriptionExpireDate()).minusDays(Integer.parseInt(env.getProperty("subscription.alert.day.count")));
			if(currentDate.toDate().compareTo(new DateTime(subscriptionInfo.getSubscriptionExpireDate()).toDate()) > 0){
				subscriptionDTO.setStatus(env.getProperty("subscription.status.expired"));
				//System.out.println("Expired Date  :"+currentDate.toDate());
			}else if(currentDate.toDate().compareTo(expireMinusDate.toDate()) < 0){
				subscriptionDTO.setStatus(env.getProperty("subscription.status.active"));
				//System.out.println("Active Date  :"+currentDate.toDate());
			}else if(currentDate.toDate().compareTo(expireMinusDate.toDate()) > 0 && currentDate.toDate().compareTo(new DateTime(subscriptionInfo.getSubscriptionExpireDate()).toDate()) < 0){
				subscriptionDTO.setStatus(env.getProperty("subscription.status.expire.soon"));
				//System.out.println("Expiring Soon Date  :"+currentDate.toDate());
			}
			if(subscriptionInfo.getOrganizationInfo().getUserProfileInfo() != null && subscriptionInfo.getOrganizationInfo().getUserProfileInfo().size() >0){
				for(UserProfileInfo userProfileInfo : subscriptionInfo.getOrganizationInfo().getUserProfileInfo()){
					UserDTO userDTO = new UserDTO();
					if(userProfileInfo.getRoleId().getRoleName().equals(Role.Admin)){
						userDTO.setUserId(userProfileInfo.getId());
						userDTO.setRole(userProfileInfo.getRoleId().getRoleName().name());
						userDTO.setRoleId(userProfileInfo.getRoleId().getId());
						userDTO.setFirstName(userProfileInfo.getFirstName());
						userDTO.setLastName(userProfileInfo.getLastName());
						userDTO.setUserName(userProfileInfo.getUserName());
						User userInfo = userRepository.findByUsername(userProfileInfo.getUserName());
						if(userInfo!=null){
							userDTO.setMail(userInfo.getMail());
						} else {
							userDTO.setMail("");
						}
						userDTO.setStatus(userProfileInfo.getStatus().longValue());
						userDTOs.add(userDTO);
					}
				}
			}
			subscriptionDTO.setUserInfos(userDTOs);
			subscriptionDTOs.add(subscriptionDTO);
		}
		return subscriptionDTOs;
	}
	

}
