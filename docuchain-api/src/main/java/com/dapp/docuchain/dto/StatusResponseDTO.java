package com.dapp.docuchain.dto;

import com.dapp.docuchain.model.CountryInfo;
import com.dapp.docuchain.model.ShipTypesInfo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class StatusResponseDTO {

    List<TaskDTO> taskAssignedByUser;
    List<TaskDTO> taskAssignedToUser;
    List<TaskDTO> statusOfTask;
    List<TaskDTO> statusAll;
    List<UserDTO> getUserList;
    private OrganizationDTO organizationInfo;
    private String status;
    private String message;
    private List<UserDTO> userList;
    private List<GroupTagDTO> groupList;
    private GroupTagDTO groupInfo;
    private Long userProfileId;
    private ExpiryDocumentDTO expiryDocumentDTOs;
    private String search;
    private String stateStatus;
    private ShipProfileDTO stateList;
    private List<ShipTypesInfo> shipType;
    private ShipProfileDTO shipInfo;
    private List<String> shipNames;
    private List<String> roleNames;
    private ShipProfileDTO oneShipInfo;
    private ShipProfileDTO roleShipNames;
	private List<ShipProfileDTO> allUserLists;
    private List<CountryInfo> countryList;
    private Long country_Id;
    private Long state_Id;
    private Long shipTypeId;
    private UserDTO userInfos;
    private List<ExpiryDocumentDTO> expiryDocumentList;
    private List<DocumentHolderDTO> documentHolderList;
    private List<ShipProfileDTO> shipProfileList;
    private List<GroupTagDTO> groupTagListSentItems;
    private ShipProfileDTO statusCode;
    private ShipProfileDTO userInfo;
    private ShipProfileDTO usershipCount;
    private Integer codeStatus;
    private List<RequestUserDTO> requestUserDTOs;
    private List<ShipProfileDTO> adminLastSeenHistoryList;
    private List<ShipProfileDTO> userLastSeenHistoryList;
    private List<ShipProfileDTO> shipCount;
    private List<ShipProfileDTO> shipMasterLastSeen;
    private byte[] fileArray;
    private List<SubscriptionDTO> subscriptionInfos;
    private List<OrganizationDTO> organizationInfos;
    private List<HistoryDTO> historyInfo;
    private GeoLocationDTO geoLocationDTO;
    
    private List<CountryDTO> countryInfos;
    
    private List<RoleAliasDTO> roleAliasInfos;
    
    private List<VesselsTypeDTO> vesselsTypeInfos;
    
    private List<PortDTO> portInfos;
    
    private List<UserDTO> shipMasterInfos;
	
	private List<UserDTO> technicalManagerInfos;
	
	private List<UserDTO> commercialManagerInfos;
	
	private List<UserDTO> adminInfos;
	
	private ExpiryCertificateTypeDTO expiryCertificateTypeDTO;
	
	private List<ExpiryCertificateTypeDTO> expiryCertificateTypeDTOs;
	
	private List<FaqDTO> faqInfos;
    
}
