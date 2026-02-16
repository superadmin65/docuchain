package com.dapp.docuchain.dto;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {
	
	private Long subscriptionId;
    private Long subscriptionAmount;
    private Long shipmentCount;
    private Long subscriptionDays;
    private Long userCount;
    private Date createdDate;
    private Date endDate;
    private Date updatedDate;
    private Integer isStatusAlive;
    private Long loginId;
    private Long userId;
    private Long numberOfUser;
    private Long numberOfVessels;
    private Long remaingDayOfSubscription;
    
    private String fromDate;
    
    private String toDate;
   
    private String activationDate;
    
    private String expireDate;
    
    private Long organizationId;
    
    private String organizationName;
    
    private String registerNumber;
    
    private String status;
    
    private Set<UserDTO> userInfos;
    
    private Long userVesslesRatio;
}
