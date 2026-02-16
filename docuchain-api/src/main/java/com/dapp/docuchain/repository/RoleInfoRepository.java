package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.RoleInfo;
import com.dapp.docuchain.model.UserProfileInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleInfoRepository extends JpaRepository<RoleInfo, Long> {

    List<RoleInfo> findAll();

    public RoleInfo findByRoleName(Role roleName);

    public RoleInfo findById(UserProfileInfo userProfileInfo);
    public RoleInfo findById(Long long1);
}
