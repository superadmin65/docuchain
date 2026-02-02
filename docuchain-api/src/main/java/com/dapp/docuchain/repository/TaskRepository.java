package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.TaskDetailsInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskDetailsInfo, Long> {

    List<TaskDetailsInfo> findByCreatedBy(UserProfileInfo userprofileInfo);

    TaskDetailsInfo findByTaskNameAndCreatedBy(String taskName, UserProfileInfo userprofileInfo);

}
