package com.dapp.docuchain.service.impl;

import com.dapp.docuchain.dto.DeletedHistoryDTO;
import com.dapp.docuchain.dto.TaskDTO;
import com.dapp.docuchain.model.*;
import com.dapp.docuchain.repository.*;
import com.dapp.docuchain.service.TaskService;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.NotificationUtility;
import com.dapp.docuchain.utility.TaskUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private ShipProfileRepository shipProfileRepository;

    @Autowired
    private TaskUtility taskUtility;

    @Autowired
    private Environment env;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private AssignedUserTaskRepository assignedUserTaskRepository;

    @Autowired
    private NotificationUtility notificationUtility;

    @Autowired
    private UserNotificationRepository userNotificationRepository;
    
    @Autowired
    private CommonMethodsUtility commonMethodsUtility;

    @Override
    public String validateShipExists(Long shipId) {
        boolean shipIdExist = shipProfileRepository.exists(shipId);
        if (!shipIdExist) {
            return "Given ship id not exist";
        }
        return "Success";
    }

    @Override
    public String saveShipRelatedTaskInfo(TaskDTO taskDTO) throws Exception {
        TaskDetailsInfo taskInfo = taskUtility.convertTaskDTOstoTaskInfo(taskDTO);
        taskInfo = taskRepository.saveAndFlush(taskInfo);
        Long i = (long) 2;
        TaskStatusInfo taskStatusInfo = taskStatusRepository.findOne(i);
        List<UserProfileInfo> userProfileInfoSet = taskUtility
                .convertTaskContactDTOsToTaskContactInfos(taskDTO.getUserProfileIds());
        if (taskInfo != null) {
            for (UserProfileInfo userProfileInfo : userProfileInfoSet) {
                AssignedUserTaskInfo assignedUserTaskInfo = new AssignedUserTaskInfo();
                assignedUserTaskInfo.setTaskInfo(taskInfo);
                assignedUserTaskInfo.setUserProfileInfo(userProfileInfo);
                assignedUserTaskInfo.setTaskStatusInfo(taskStatusInfo);
                AssignedUserTaskInfo assignedUserTask =assignedUserTaskRepository.save(assignedUserTaskInfo);
                notificationUtility.taskDetailNotification(taskInfo, userProfileInfo);
                if(assignedUserTask!=null){
                    commonMethodsUtility.maintainHistory(assignedUserTask.getId(),assignedUserTask.getTaskInfo().getTaskName(),"Task", env.getProperty("history.created"), taskDTO.getCreatedBy());
                    }
            }
        }
        return env.getProperty("task.create.success");

    }

    @Override
    public String saveNonShipRelatedTaskInfo(TaskDTO taskDTO) throws Exception {
        TaskDetailsInfo taskInfo = taskUtility.convertTaskDTOtoTaskInfo(taskDTO);
        taskInfo = taskRepository.saveAndFlush(taskInfo);
        Long i = (long) 1;
        TaskStatusInfo taskStatusInfo = taskStatusRepository.findOne(i);
        List<UserProfileInfo> userProfileInfoSet = taskUtility
                .convertTaskContactDTOsToTaskContactInfos(taskDTO.getUserProfileIds());
        if (taskInfo != null) {
            for (UserProfileInfo userProfileInfo : userProfileInfoSet) {
                AssignedUserTaskInfo assignedUserTaskInfo = new AssignedUserTaskInfo();
                assignedUserTaskInfo.setTaskInfo(taskInfo);
                assignedUserTaskInfo.setUserProfileInfo(userProfileInfo);
                assignedUserTaskInfo.setTaskStatusInfo(taskStatusInfo);
                AssignedUserTaskInfo assignedUserTask=assignedUserTaskRepository.save(assignedUserTaskInfo);
                notificationUtility.taskDetailNotification(taskInfo, userProfileInfo);
                if(assignedUserTask!=null){
                    commonMethodsUtility.maintainHistory(assignedUserTask.getId(),assignedUserTask.getTaskInfo().getTaskName(),"Task", env.getProperty("history.created"), taskDTO.getLoginId());
                    }
            }
        }
        return env.getProperty("task.create.success");
    }

    @Override
    public boolean isTask(Long taskId) {
        TaskDetailsInfo taskInfo = taskRepository.findOne(taskId);
        if (taskInfo != null) {
            return true;
        }
        return false;
    }

    @Override
    public String UpdateShipTask(TaskDTO taskDTO) throws Exception {
        TaskDetailsInfo taskInfo = taskRepository.findOne(taskDTO.getId());
        if (taskInfo != null) {
            taskInfo = taskUtility.convertUpdateTaskDTOtoTaskInfo(taskDTO);
            TaskDetailsInfo taskDetailInfo=taskRepository.save(taskInfo);
            if(taskDetailInfo!=null){
                commonMethodsUtility.maintainHistory(taskDetailInfo.getId(),taskDetailInfo.getTaskName(),"Task", env.getProperty("history.updated"), taskDTO.getLoginId());
                }
            return env.getProperty("task.update.success");
        }
        return null;
    }

    @Override
    public String UpdateNonShipTask(TaskDTO taskDTO) throws Exception {
        TaskDetailsInfo taskInfo = taskRepository.findOne(taskDTO.getId());
        if (taskInfo != null) {
            taskInfo = taskUtility.convertUpdateNonShipTasktoTaskInfo(taskDTO);
            TaskDetailsInfo taskDetailInfo= taskRepository.save(taskInfo);
            if(taskDetailInfo!=null){
                commonMethodsUtility.maintainHistory(taskDetailInfo.getId(),taskDetailInfo.getTaskName(),"Task", env.getProperty("history.updated"), taskDTO.getLoginId());
                }
            return env.getProperty("task.update.success");
        }
        return null;
    }

    @Override
    public List<TaskDTO> getAllAssignedByUser(Long userId) {
        List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
        UserProfileInfo userprofileInfo = userProfileRepository.findOne(userId);
        List<TaskDetailsInfo> taskInfo = taskRepository.findByCreatedBy(userprofileInfo);
        if (taskInfo != null && taskInfo.size() > 0) {
            for (TaskDetailsInfo taskDetailsInfo : taskInfo) {
                TaskDTO taskDTO = taskUtility.convertTaskInfotoTaskDTO(taskDetailsInfo);
                taskDTOs.add(taskDTO);
            }
            return taskDTOs;
        }
        return taskDTOs;
    }

    @Override
    public List<TaskDTO> getTaskAssignedTouser(Long userId) {
        List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
        UserProfileInfo userprofileInfo = userProfileRepository.findOne(userId);
        List<AssignedUserTaskInfo> assignedUserTaskInfos = assignedUserTaskRepository
                .findByUserProfileInfo(userprofileInfo);
        if (assignedUserTaskInfos != null && assignedUserTaskInfos.size() > 0) {
            for (AssignedUserTaskInfo assignedUserTaskInfo : assignedUserTaskInfos) {
                TaskDetailsInfo taskInfo = taskRepository.findOne(assignedUserTaskInfo.getTaskInfo().getId());
                TaskDTO taskDTO = taskUtility.convertTaskInfotoTaskDTO(taskInfo, assignedUserTaskInfo);
                taskDTOs.add(taskDTO);
            }
            return taskDTOs;
        }
        return taskDTOs;
    }

    @Override
    public boolean isUserTaskExits(TaskDTO taskDTO) {
        UserProfileInfo userprofileInfo = userProfileRepository.findOne(taskDTO.getCreatedBy());
        TaskDetailsInfo taskInfo = taskRepository.findOne(taskDTO.getTaskId());
        if (userprofileInfo != null) {
            AssignedUserTaskInfo assignedUserTaskInfo = assignedUserTaskRepository
                    .findByUserProfileInfoAndTaskInfo(userprofileInfo, taskInfo);
            if (assignedUserTaskInfo != null) {

                /*
                 * TaskStatusInfo
                 * taskStatusInfo=taskStatusRepository.findOne(taskDTO.getTaskStatusId());
                 * assignedUserTaskInfo.setTaskStatusInfo(taskStatusInfo);
                 * assignedUserTaskRepository.save(assignedUserTaskInfo);
                 */
                return true;
            }
        }
        return false;
    }

    @Override
    public String isupdateTaskStatus(TaskDTO taskDTO) {
        UserProfileInfo userprofileInfo = userProfileRepository.findOne(taskDTO.getCreatedBy());
        TaskDetailsInfo taskInfo = taskRepository.findOne(taskDTO.getTaskId());
        TaskStatusInfo statusInfo = taskStatusRepository.findOne((long) 3);
        TaskStatusInfo taskStatusInProgress = taskStatusRepository.findOne((long) 2);
        TaskStatusInfo reject = taskStatusRepository.findOne((long) 5);
        if (userprofileInfo != null) {
            AssignedUserTaskInfo assignedUserTaskInfo = assignedUserTaskRepository
                    .findByUserProfileInfoAndTaskInfo(userprofileInfo, taskInfo);
            if (assignedUserTaskInfo != null) {
                TaskStatusInfo taskStatusInfo = taskStatusRepository.findOne(taskDTO.getTaskStatusId());
                assignedUserTaskInfo.setTaskStatusInfo(taskStatusInfo);
                assignedUserTaskInfo.setUserAcceptRemark(taskDTO.getUserRemarks());
                assignedUserTaskRepository.save(assignedUserTaskInfo);
                taskInfo.setTaskStatusInfo(taskStatusInProgress);
                taskInfo = taskRepository.save(taskInfo);
                if(taskInfo!=null){
                    commonMethodsUtility.maintainHistory(taskInfo.getId(),taskInfo.getTaskName(),"Task", env.getProperty("history.assigned"), taskDTO.getLoginId());
                    }
                List<AssignedUserTaskInfo> taskUserInfos = assignedUserTaskRepository.findByTaskInfo(taskInfo);
                List<AssignedUserTaskInfo> taskUserInfoList = new ArrayList<>();
                if (taskUserInfos != null && taskUserInfos.size() > 0) {
                    for (AssignedUserTaskInfo assignedUserInfo : taskUserInfos) {
                        if (!assignedUserInfo.getTaskStatusInfo().equals(statusInfo)
                                && !assignedUserInfo.getTaskStatusInfo().equals(reject)) {
                            taskUserInfoList.add(assignedUserInfo);
                        }
                    }
                }
                TaskDetailsInfo taskDetailInfo = taskRepository.save(taskInfo);
              notificationUtility.notificationTaskStatusChanges(taskDetailInfo, taskDTO);
                if (taskUserInfoList.isEmpty()) {
                    taskInfo.setTaskStatusInfo(statusInfo);
                    if(taskDetailInfo!=null){
                        commonMethodsUtility.maintainHistory(taskDetailInfo.getId(),taskDetailInfo.getTaskName(),"Task", env.getProperty("history.assigned"), taskDTO.getLoginId());
                        }
                }
            }
            return "Success";
        }
        return "Failed";
    }

    @Override
    public List<TaskDTO> getTaskStatusList(Long taskId) {
        TaskDetailsInfo taskInfo = taskRepository.findOne(taskId);
        List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
        if (taskInfo != null) {
            List<AssignedUserTaskInfo> assignedUserTaskInfos = assignedUserTaskRepository.findByTaskInfo(taskInfo);
            if (assignedUserTaskInfos != null && assignedUserTaskInfos.size() > 0) {
                for (AssignedUserTaskInfo assignedUserTaskInfo : assignedUserTaskInfos) {
                    TaskStatusInfo taskStatusInfo = taskStatusRepository
                            .findOne(assignedUserTaskInfo.getTaskStatusInfo().getId());
                    TaskDTO taskDTO = new TaskDTO();
                    taskDTO.setTaskId(assignedUserTaskInfo.getTaskInfo().getId());
                    if (assignedUserTaskInfo.getUserAcceptStatus() != null) {
                        taskDTO.setAcceptStatus(assignedUserTaskInfo.getUserAcceptStatus().longValue());
                    }
                    if (assignedUserTaskInfo.getUserAcceptRemark() != null) {
                        taskDTO.setUserRemarks(assignedUserTaskInfo.getUserAcceptRemark());
                    }
                    taskDTO.setUserName(assignedUserTaskInfo.getUserProfileInfo().getUserName());
                    taskDTO.setTaskStatus(taskStatusInfo.getTaskStatus());
                    taskDTOs.add(taskDTO);
                }

            }
        }
        return taskDTOs;
    }

    @Override
    public TaskDetailsInfo deleteTask(TaskDTO taskDTO) {
        TaskDetailsInfo taskInfo = taskRepository.findOne(taskDTO.getTaskId());
        if (taskInfo != null) {
            List<AssignedUserTaskInfo> assignedUserTaskInfos = assignedUserTaskRepository.findByTaskInfo(taskInfo);
            if (assignedUserTaskInfos != null && assignedUserTaskInfos.size() > 0) {
                assignedUserTaskRepository.delete(assignedUserTaskInfos);
            }

            List<UserNotificationInfo> notify = userNotificationRepository.findByTaskDetailsInfo(taskInfo);
            if (notify != null && notify.size() > 0) {
                userNotificationRepository.delete(notify);
            }
            DeletedHistoryDTO deletedHistoryDTO=new DeletedHistoryDTO();
            deletedHistoryDTO.setObjectId(taskInfo.getId());
            deletedHistoryDTO.setObjectOne(taskInfo.getTaskName());
            deletedHistoryDTO.setObjectTwo(taskInfo.getTaskDetails());
            DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
            taskRepository.delete(taskInfo);
            if(dataDeletedHistoryInfo!=null)
            commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),taskInfo.getTaskName(),"Task", env.getProperty("history.deleted"), taskDTO.getCreatedBy());
           
        }

        return taskInfo;
    }

    @Override
    public List<TaskDTO> getAllTaskStatusList() {
        List<TaskDTO> taskStatusDTOs = new ArrayList<>();
        List<TaskStatusInfo> taskStatusInfos = taskStatusRepository.findAll();
        if (taskStatusInfos != null) {
            taskStatusDTOs = taskUtility.convertTaskStatusInfotoTaskStatusDTO(taskStatusInfos);
            return taskStatusDTOs;
        }
        return null;
    }

	@Override
	public String taskdeleteValidation(TaskDTO taskDTO) {
		if(taskDTO.getTaskId() == null)
			return env.getProperty("task.id.null");
		
		if(taskDTO.getCreatedBy() == null)
			return env.getProperty("user.id.null");
		
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(taskDTO.getCreatedBy());
		if(userProfileInfo == null)
			return env.getProperty("user.not.found");
		
		TaskDetailsInfo taskDetailsInfo = taskRepository.findOne(taskDTO.getTaskId());
		if(taskDetailsInfo == null)
			return env.getProperty("task.not.found");
		
		if(!taskDetailsInfo.getCreatedBy().equals(userProfileInfo))
			return env.getProperty("task.creator.only.delete");
			
		return env.getProperty("success");
	}
}
