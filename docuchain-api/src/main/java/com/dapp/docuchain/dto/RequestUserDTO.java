package com.dapp.docuchain.dto;

import com.dapp.docuchain.model.ShipProfileInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUserDTO {

    private Integer numberOfUser;
    private Integer needAnySpecificUser;
    private String remarks;
    private List<String> userList;
    private ShipProfileInfo shipProfileInfo;
    private Long shipId;
    private Long uploadUserId;
    private String requestUserStatus;
    private String shipName;
    private String userName;
    private List<UserDTO> userProfileInfos;
    private Long requestId;
    private Long notifiedToId;
    private Long loginId;
    private String requesterName;
    private String roleName;


}
