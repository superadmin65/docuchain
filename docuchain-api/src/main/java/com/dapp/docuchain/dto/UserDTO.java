package com.dapp.docuchain.dto;

import java.util.Date;
import java.util.List;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Long loginId;
	private Long adminId;
    private String userName;
    private String currentPassword;
    private String password;
    private String businessCategory;
    private String mail;
    private String color;
    private String notification;
    private String notificationMessage;
    private String notifiedOn;
    private String notificationType;
    private String notificationName;
    private String shipName;
    private String documentName;
    private Long userId;
    private Long roleId;
    private Long notificationId;
    private Long shipId;
    private Long taskId;
    private Long status;
    private Long subscriptionId;
    private Long organizationId;
	private String firstName;
	private String lastName;
	private Long expiryDocumentId;
	private String role;
	private Long[] userIds;
	private String confirmPassword;
	private String latitude;
	private String longitute;
	private long maxShipCount;
	private long maxUserCount;
	private String logoPicture;
	private String vesselName;
	private long imo;
	private String taskName;
	private String filterByDay;
	private String shipProfilePath;
	private Date notifyDate;
	private List<UserDTO> shipMasterInfos;
	
	private List<UserDTO> technicalManagerInfos;
	
	private List<UserDTO> commercialManagerInfos;
	
	private List<ShipProfileDTO> shipProfileInfos;
	
	private GeoLocationDTO geoLocationInfos;
	
	private ShipProfileDTO shipProfileDTO;
	
	/*This variable is used to bind the ship profile id while saving user profile*/
	private Long[] shipProfileIds;
	
	/*This variable is used get the profile picture from front end*/
	private String profilePicture;
	
	private String subscriptionExpireStatus;
	
	private String organizationName;
	
	private int snooze;
}
