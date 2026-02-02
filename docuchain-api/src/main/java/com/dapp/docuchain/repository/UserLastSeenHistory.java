package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.UserLastSeenInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLastSeenHistory extends JpaRepository<UserLastSeenInfo, Long> {

    List<UserLastSeenInfo> findTop5ByUserNameOrderByUserLastSeenDateTimeDesc(String shipMasterName);

    public List<UserLastSeenInfo> findByUserName(String userName);
}
