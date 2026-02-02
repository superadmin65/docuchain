package com.dapp.docuchain.service;

import com.dapp.docuchain.dto.TaskDTO;
import com.dapp.docuchain.model.TaskDetailsInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    String validateShipExists(Long shipId);

    String saveShipRelatedTaskInfo(TaskDTO taskDTO) throws Exception;

    String saveNonShipRelatedTaskInfo(TaskDTO taskDTO) throws Exception;

    boolean isTask(Long taskId);

    String UpdateShipTask(TaskDTO taskDTO) throws Exception;

    String UpdateNonShipTask(TaskDTO taskDTO) throws Exception;

    List<TaskDTO> getAllAssignedByUser(Long userId);

    List<TaskDTO> getTaskAssignedTouser(Long userId);

    boolean isUserTaskExits(TaskDTO taskDTO);

    String isupdateTaskStatus(TaskDTO taskDTO);

    List<TaskDTO> getTaskStatusList(Long taskId);

    TaskDetailsInfo deleteTask(TaskDTO taskDTO);

    List<TaskDTO> getAllTaskStatusList();
    
    String taskdeleteValidation(TaskDTO taskDTO);

}
