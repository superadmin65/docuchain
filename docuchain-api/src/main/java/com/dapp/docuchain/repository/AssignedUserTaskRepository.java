package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.AssignedUserTaskInfo;
import com.dapp.docuchain.model.TaskDetailsInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignedUserTaskRepository extends JpaRepository<AssignedUserTaskInfo, Long> {

    List<AssignedUserTaskInfo> findByTaskInfo(TaskDetailsInfo task);

    AssignedUserTaskInfo findByUserProfileInfoAndTaskInfo(UserProfileInfo userprofileInfo, TaskDetailsInfo task);

    List<AssignedUserTaskInfo> findByUserProfileInfo(UserProfileInfo userprofileInfo);


}
