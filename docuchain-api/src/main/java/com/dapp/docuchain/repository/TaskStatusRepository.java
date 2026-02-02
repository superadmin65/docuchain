package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.TaskStatusInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskStatusRepository extends JpaRepository<TaskStatusInfo, Long> {

}
