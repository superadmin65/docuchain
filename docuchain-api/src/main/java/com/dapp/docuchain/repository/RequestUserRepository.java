package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.RequestUserInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequestUserRepository extends JpaRepository<RequestUserInfo, Long> {

    List<RequestUserInfo> findByRequestUserStatus(String status);
    
    List<RequestUserInfo> findByRequesterInfo(UserProfileInfo userProfileInfo);
    
    List<RequestUserInfo> findByOrganizationInfo(OrganizationInfo organizationInfo);
    
}
