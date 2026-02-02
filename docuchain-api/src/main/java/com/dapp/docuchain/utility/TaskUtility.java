package com.dapp.docuchain.utility;

import com.dapp.docuchain.dto.TaskDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.*;
import com.dapp.docuchain.repository.*;
import com.dapp.docuchain.service.impl.FileServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TaskUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskUtility.class);

    @Autowired
    Environment env;

    @Autowired
    private UserProfileRepository userProfileRepository;
    
	@Autowired
	private UserRepository userRepository;

    @Autowired
    private ShipProfileRepository shipProfileRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AssignedUserTaskRepository assignedUserTaskRepository;

    @Autowired
    private FileServiceImpl fileServiceImpl;
    
    @Autowired
    private NotificationUtility notificationUtility;

    private static long dayBetween(Date one, Date two) {
        long difference = (one.getTime() - two.getTime()) / 86400000;
        return Math.abs(difference);
    }

    public String validateShipTaskParameter(TaskDTO taskDTO) {

        if (!(taskDTO.getTaskDetails() != null && StringUtils.isNotBlank(taskDTO.getTaskDetails()))) {
            return "Task detail missing";
        }
        if (!(taskDTO.getTaskName() != null && StringUtils.isNotBlank(taskDTO.getTaskName()))) {
            return "Task name missing";
        }
        if (taskDTO.getCheckboxSelectionId() == 1) {
            if (!(taskDTO.getShipId() != null && StringUtils.isNotBlank(taskDTO.getShipId().toString()))) {
                return "Please select ship";
            }
        }
        if (!(taskDTO.getStartDate() != null && StringUtils.isNotBlank(taskDTO.getStartDate()))) {
            return "Start date Missing";
        }

        if (!(taskDTO.getEndDate() != null && StringUtils.isNotBlank(taskDTO.getEndDate()))) {
            return "End date Missing";
        }

        if (!(taskDTO.getUserProfileIds() != null && taskDTO.getUserProfileIds().length > 0)) {
            return "assign user details missing ";
        }

        return env.getProperty("success");
    }

    public boolean isUserExists(Long createdBy) {
        UserProfileInfo userInfo = userProfileRepository.findOne(createdBy);
        if (userInfo != null) {
            return true;
        }
        return false;
    }

    public List<UserProfileInfo> convertTaskContactDTOsToTaskContactInfos(Long[] userProfileIds) {
        List<UserProfileInfo> userProfileInfos = new ArrayList<UserProfileInfo>();
        if (userProfileIds != null && userProfileIds.length > 0) {
            for (int i = 0; i < userProfileIds.length; i++) {
                UserProfileInfo userProfileInfo = userProfileRepository.findOne(userProfileIds[i]);
                if (userProfileInfo != null) {
                    userProfileInfos.add(userProfileInfo);
                }
            }
            return userProfileInfos;
        }
        return null;
    }

    public TaskDetailsInfo convertTaskDTOstoTaskInfo(TaskDTO taskDTO) throws Exception {

        Date startDate = fileServiceImpl.parseDate(taskDTO.getStartDate());
        Date endDate = fileServiceImpl.parseDate(taskDTO.getEndDate());
        UserProfileInfo userInfo = userProfileRepository.findOne(taskDTO.getCreatedBy());
        Long i = (long) 2;
        TaskStatusInfo taskStatusInfo = taskStatusRepository.findOne(i);
        TaskDetailsInfo taskDetailsInfo = new TaskDetailsInfo();
        if (taskDTO.getCheckboxSelectionId() == 1) {
            ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(taskDTO.getShipId());
            taskDetailsInfo.setShipProfileInfo(shipProfileInfo);
        }
        taskDetailsInfo.setCreatedBy(userInfo);
        taskDetailsInfo.setStartDate(startDate);
        taskDetailsInfo.setEndDate(endDate);
        taskDetailsInfo.setTaskName(taskDTO.getTaskName());
        taskDetailsInfo.setTaskDetails(taskDTO.getTaskDetails());
        taskDetailsInfo.setTaskStatusInfo(taskStatusInfo);
        return taskDetailsInfo;
    }

    public String isDateValid(TaskDTO taskDTO) throws Exception {
        LOGGER.info("Start Date --->" + taskDTO.getStartDate());
        LOGGER.info("End Date --->" + taskDTO.getEndDate());


        //SimpleDateFormat dateformet = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        //yyyy-MM-dd'T'HH:mm:ss.SSSZ
        //2018-10-30T11:46:46.632Z
        Date startDate = fileServiceImpl.parseDate(taskDTO.getStartDate());
        Date endDate = fileServiceImpl.parseDate(taskDTO.getEndDate());
        LOGGER.info(String.valueOf(startDate));
        LOGGER.info(String.valueOf(endDate));
        //if()
        // if (!taskDTO.getStartDate().equals(dateformet.format(startDate))
        // && !taskDTO.getEndDate().equals(dateformet.format(endDate))) {
        // return "The date format is invalid.Please enter date MM/dd/yyyy hh:mm:ss a in
        // this format";
        // }
        if (endDate.compareTo(startDate) < 0) {
            LOGGER.info("endDate is before startDate");
            return "End date is before Start date";
        } else if (endDate.compareTo(startDate) == 0) {
            LOGGER.info("startDate is equal to endDate");
            return "Start date is equal to End date";
        } else if (startDate.compareTo(new Date()) < 0) {
            return ("Start date is should not before Today date");
        }

        return "success";
    }

    public TaskDetailsInfo convertTaskDTOtoTaskInfo(TaskDTO taskDTO) throws Exception {
        Date startDate = fileServiceImpl.parseDate(taskDTO.getStartDate());
        Date endDate = fileServiceImpl.parseDate(taskDTO.getEndDate());
        UserProfileInfo userInfo = userProfileRepository.findOne(taskDTO.getCreatedBy());
        Long i = (long) 1;
        TaskStatusInfo taskStatusInfo = taskStatusRepository.findOne(i);
        TaskDetailsInfo taskDetailsInfo = new TaskDetailsInfo();
        taskDetailsInfo.setTaskName(taskDTO.getTaskName());
        taskDetailsInfo.setTaskDetails(taskDTO.getTaskDetails());
        taskDetailsInfo.setCreatedBy(userInfo);
        taskDetailsInfo.setStartDate(startDate);
        taskDetailsInfo.setEndDate(endDate);
        taskDetailsInfo.setTaskStatusInfo(taskStatusInfo);
        return taskDetailsInfo;
    }

    public boolean validateUpdateTagParamForAssignTask(TaskDTO taskDTO) {
        if (taskDTO.getId() != null && StringUtils.isNotBlank(taskDTO.getId().toString())
                && taskDTO.getTaskDetails() != null && StringUtils.isNotBlank(taskDTO.getTaskDetails())
                && taskDTO.getTaskName() != null && StringUtils.isNotBlank(taskDTO.getTaskName())
                && taskDTO.getShipId() != null && StringUtils.isNotBlank(taskDTO.getShipId().toString())
                && taskDTO.getStartDate() != null && taskDTO.getEndDate() != null && taskDTO.getUserProfileIds() != null
                && taskDTO.getUserProfileIds().length > 0) {
            return true;
        }
        return false;
    }

    public TaskDetailsInfo convertUpdateTaskDTOtoTaskInfo(TaskDTO taskDTO) throws ParseException {
        List<AssignedUserTaskInfo> assignedUserTaskInfo = new ArrayList<>();
        TaskDetailsInfo taskInfo = taskRepository.findOne(taskDTO.getId());
        UserProfileInfo userInfo = userProfileRepository.findOne(taskDTO.getCreatedBy());
        taskInfo.setCreatedBy(userInfo);
        taskInfo.setTaskDetails(taskDTO.getTaskDetails());
        taskInfo.setTaskName(taskDTO.getTaskName());
        Date startDate = fileServiceImpl.parseDate(taskDTO.getStartDate());
        Date endDate = fileServiceImpl.parseDate(taskDTO.getEndDate());
        // TaskStatusInfo taskStatusInfo =
        // taskStatusRepository.findOne(taskDTO.getTaskStatusId());
        // if (taskStatusInfo != null) {
        // taskInfo.setTaskStatusInfo(taskStatusInfo);
        // }
        TaskDetailsInfo task = taskRepository.findOne(taskDTO.getId());
        List<AssignedUserTaskInfo> contactInfoList = new ArrayList<>();
        Set<AssignedUserTaskInfo> contactList = new HashSet<>();
        List<AssignedUserTaskInfo> removeContactInfoList = new ArrayList<>();
        List<AssignedUserTaskInfo> taskContactInfoo = assignedUserTaskRepository.findByTaskInfo(task);
        if (taskDTO.getUserProfileIds() != null && taskDTO.getUserProfileIds().length > 0) {

            for (Long userDTO : taskDTO.getUserProfileIds()) {
                boolean isContactAlreadyExists = false;
                for (AssignedUserTaskInfo taskContactInfo : taskContactInfoo) {
                    if (userDTO == taskContactInfo.getUserProfileInfo().getId().intValue()) {
                        isContactAlreadyExists = true;
                    }
                }
                if (isContactAlreadyExists) {
                    UserProfileInfo userprofileInfo = userProfileRepository.findOne(userDTO);
                    AssignedUserTaskInfo taskContactInfo = assignedUserTaskRepository
                            .findByUserProfileInfoAndTaskInfo(userprofileInfo, task);
                    if (taskContactInfo != null) {
                        System.err.println("already contact id :" + taskContactInfo.getUserProfileInfo().getId());
                        taskContactInfo.setUserProfileInfo(taskContactInfo.getUserProfileInfo());
                        taskContactInfo.setTaskInfo(taskInfo);
                        TaskStatusInfo taskStatusIn = taskStatusRepository.findOne(taskDTO.getTaskStatusId());
                        taskContactInfo.setTaskStatusInfo(taskStatusIn);
                        contactInfoList.add(taskContactInfo);
                        contactList.add(taskContactInfo);
                    }
                }
                if (!isContactAlreadyExists) {
                    System.err.println("contact id :" + userDTO);
                    UserProfileInfo userprofileInfo = userProfileRepository.findOne(userDTO);
                    AssignedUserTaskInfo taskContactInfo = new AssignedUserTaskInfo();
                    TaskStatusInfo taskStatusIn = taskStatusRepository.findOne(taskDTO.getTaskStatusId());
                    taskContactInfo.setTaskStatusInfo(taskStatusIn);
                    taskContactInfo.setUserProfileInfo(userprofileInfo);
                    taskContactInfo.setTaskInfo(taskInfo);
                    contactList.add(taskContactInfo);
                }
            }

            for (AssignedUserTaskInfo taskContactInfo : taskContactInfoo) {

                boolean isNotExist = false;
                for (AssignedUserTaskInfo taskContactInfo1 : contactInfoList) {
                    if (taskContactInfo.equals(taskContactInfo1)) {
                        isNotExist = true;
                    }
                }
                if (!isNotExist) {
                    removeContactInfoList.add(taskContactInfo);
                }
            }
            if (removeContactInfoList != null && removeContactInfoList.size() > 0) {
                assignedUserTaskRepository.delete(removeContactInfoList);
            }
        }
        if (taskDTO.getShipId() != null) {
            ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(taskDTO.getShipId());
            taskInfo.setShipProfileInfo(shipProfileInfo);
        }
        taskInfo.setTaskName(taskDTO.getTaskName());
        taskInfo.setStartDate(startDate);
        taskInfo.setEndDate(endDate);
        TaskStatusInfo taskStatusIn = taskStatusRepository.findOne(taskDTO.getTaskStatusId());
        taskInfo.setTaskStatusInfo(taskStatusIn);
        // taskInfo.setAssignedUserTaskInfos(contactList);
        assignedUserTaskRepository.save(contactList);
        if (contactList != null && contactInfoList.size() > 0){
        	notificationUtility.notificationToUpdateNewUserToTask(contactList, taskDTO.getLoginId());
        }
        if (removeContactInfoList != null && removeContactInfoList.size() > 0) {
        	 notificationUtility.notificationToRemoveUserToTask(removeContactInfoList, taskDTO.getLoginId());
        }
        return taskInfo;
    }

    public TaskDetailsInfo convertUpdateNonShipTasktoTaskInfo(TaskDTO taskDTO) throws Exception {
        List<AssignedUserTaskInfo> assignedUserTaskInfo = new ArrayList<>();
        TaskDetailsInfo taskInfo = taskRepository.findOne(taskDTO.getId());
        UserProfileInfo userInfo = userProfileRepository.findOne(taskDTO.getCreatedBy());
        taskInfo.setCreatedBy(userInfo);
        taskInfo.setTaskDetails(taskDTO.getTaskDetails());
        taskInfo.setTaskName(taskDTO.getTaskName());
        Date startDate = fileServiceImpl.parseDate(taskDTO.getStartDate());
        Date endDate = fileServiceImpl.parseDate(taskDTO.getEndDate());
        TaskStatusInfo taskStatusInfo = taskStatusRepository.findOne(taskDTO.getTaskStatusId());
        if (taskStatusInfo != null) {
            taskInfo.setTaskStatusInfo(taskStatusInfo);
        }
        TaskDetailsInfo task = taskRepository.findOne(taskDTO.getId());
        List<AssignedUserTaskInfo> contactInfoList = new ArrayList<>();
        Set<AssignedUserTaskInfo> contactList = new HashSet<>();
        List<AssignedUserTaskInfo> removeContactInfoList = new ArrayList<>();
        List<AssignedUserTaskInfo> taskContactInfoo = assignedUserTaskRepository.findByTaskInfo(task);
        if (taskDTO.getUserProfileIds() != null && taskDTO.getUserProfileIds().length > 0) {

            for (Long userDTO : taskDTO.getUserProfileIds()) {
                boolean isContactAlreadyExists = false;
                for (AssignedUserTaskInfo taskContactInfo : taskContactInfoo) {
                    if (userDTO == taskContactInfo.getUserProfileInfo().getId().intValue()) {
                        isContactAlreadyExists = true;
                    }
                }
                if (isContactAlreadyExists) {
                    UserProfileInfo userprofileInfo = userProfileRepository.findOne(userDTO);
                    AssignedUserTaskInfo taskContactInfo = assignedUserTaskRepository
                            .findByUserProfileInfoAndTaskInfo(userprofileInfo, task);
                    if (taskContactInfo != null) {
                        System.err.println("already contact id :" + taskContactInfo.getUserProfileInfo().getId());
                        taskContactInfo.setUserProfileInfo(taskContactInfo.getUserProfileInfo());
                        taskContactInfo.setTaskInfo(taskInfo);
                        contactInfoList.add(taskContactInfo);
                        contactList.add(taskContactInfo);
                    }
                }
                if (!isContactAlreadyExists) {
                    System.err.println("contact id :" + userDTO);
                    UserProfileInfo userprofileInfo = userProfileRepository.findOne(userDTO);
                    AssignedUserTaskInfo taskContactInfo = new AssignedUserTaskInfo();
                    Long i = (long) 1;
                    TaskStatusInfo taskStatusIn = taskStatusRepository.findOne(i);
                    taskContactInfo.setTaskStatusInfo(taskStatusIn);
                    taskContactInfo.setUserProfileInfo(userprofileInfo);
                    taskContactInfo.setTaskInfo(taskInfo);
                    contactList.add(taskContactInfo);
                }
            }

            for (AssignedUserTaskInfo taskContactInfo : taskContactInfoo) {

                boolean isNotExist = false;
                for (AssignedUserTaskInfo taskContactInfo1 : contactInfoList) {
                    if (taskContactInfo.equals(taskContactInfo1)) {
                        isNotExist = true;
                    }
                }
                if (!isNotExist) {
                    removeContactInfoList.add(taskContactInfo);
                }
            }
            if (removeContactInfoList != null && removeContactInfoList.size() > 0) {
                assignedUserTaskRepository.delete(removeContactInfoList);
            }
        }
        taskInfo.setStartDate(startDate);
        taskInfo.setEndDate(endDate);
        // taskInfo.setAssignedUserTaskInfos(contactList);
        assignedUserTaskRepository.save(contactList);
        if (contactList != null && contactInfoList.size() > 0){
        	notificationUtility.notificationToUpdateNewUserToTask(contactList, taskDTO.getLoginId());
        }
        if (removeContactInfoList != null && removeContactInfoList.size() > 0) {
        	 notificationUtility.notificationToRemoveUserToTask(removeContactInfoList, taskDTO.getLoginId());
        }
        return taskInfo;
    }

    public boolean validateUpdateNonShipParamForAssignTask(TaskDTO taskDTO) {
        if (taskDTO.getTaskDetails() != null && StringUtils.isNotBlank(taskDTO.getTaskDetails())
                && taskDTO.getTaskName() != null && StringUtils.isNotBlank(taskDTO.getTaskName())
                && taskDTO.getStartDate() != null && taskDTO.getEndDate() != null && taskDTO.getUserProfileIds() != null
                && taskDTO.getUserProfileIds().length > 0) {
            return true;
        }
        return false;
    }

    public TaskDTO convertTaskInfotoTaskDTO(TaskDetailsInfo taskDetailsInfo) {
        SimpleDateFormat dateformet = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", new Locale("en", "US"));
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(taskDetailsInfo.getId());
        taskDTO.setTaskName(taskDetailsInfo.getTaskName());
        taskDTO.setTaskDetails(taskDetailsInfo.getTaskDetails());
        taskDTO.setTaskStatusId(taskDetailsInfo.getTaskStatusInfo().getId());
        taskDTO.setTaskStatus(taskDetailsInfo.getTaskStatusInfo().getTaskStatus());
        taskDTO.setUpdateDStartDate(date.format(taskDetailsInfo.getStartDate()));
        taskDTO.setUpdateEndDate(date.format(taskDetailsInfo.getEndDate()));
        taskDTO.setStartDate(dateformet.format(taskDetailsInfo.getStartDate()));
        taskDTO.setEndDate(dateformet.format(taskDetailsInfo.getEndDate()));
        taskDTO.setCreatedBy(taskDetailsInfo.getCreatedBy().getId());
        taskDTO.setUserName(taskDetailsInfo.getCreatedBy().getUserName());
        String colorCode = getTaskColor(dateformet.format(taskDetailsInfo.getStartDate()), dateformet.format(taskDetailsInfo.getEndDate()));
        taskDTO.setStatusColor(colorCode);
        if (taskDetailsInfo.getShipProfileInfo() != null) {
            taskDTO.setShipId(taskDetailsInfo.getShipProfileInfo().getId());
            taskDTO.setShipName(taskDetailsInfo.getShipProfileInfo().getShipName());
        }
        List<AssignedUserTaskInfo> taskUserInfo = assignedUserTaskRepository.findByTaskInfo(taskDetailsInfo);

        List<Long> userIds = new ArrayList<>();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (AssignedUserTaskInfo user : taskUserInfo) {
            UserDTO userDTO = new UserDTO();
			userDTO.setUserId(user.getUserProfileInfo().getId());
			userDTO.setRole(user.getUserProfileInfo().getRoleId().getRoleName().name());
			userDTO.setFirstName(user.getUserProfileInfo().getFirstName());
			userDTO.setLastName(user.getUserProfileInfo().getLastName());
			userDTO.setStatus(user.getUserProfileInfo().getStatus().longValue());
			userDTO.setUserName(user.getUserProfileInfo().getUserName());
			User userInfo = userRepository.findByUsername(user.getUserProfileInfo().getUserName());
			if(userInfo!=null) {
				userDTO.setMail(userInfo.getMail());
			}
			userDTOs.add(userDTO);
        }
        taskDTO.setUserProfileInfos(userDTOs);
        return taskDTO;

    }

    public boolean validateTaskStatusParams(TaskDTO taskDTO) {
        if (taskDTO.getTaskStatusId() != null && StringUtils.isNotBlank(taskDTO.getTaskStatusId().toString())
                && taskDTO.getCreatedBy() != null && StringUtils.isNotBlank(taskDTO.getCreatedBy().toString())
                && taskDTO.getTaskId() != null && StringUtils.isNotBlank(taskDTO.getTaskId().toString())) {
            return true;
        }
        return false;
    }

    public String validateSourceExist(TaskDTO taskDTO) {
        UserProfileInfo userprofileInfo = userProfileRepository.findOne(taskDTO.getCreatedBy());
        TaskDetailsInfo taskDetailsInfo = taskRepository.findByTaskNameAndCreatedBy(taskDTO.getTaskName(),
                userprofileInfo);
        if (taskDetailsInfo != null) {
            return "Task Name Already exist";
        }
        return "Success";
    }

    public List<TaskDTO> convertTaskStatusInfotoTaskStatusDTO(List<TaskStatusInfo> taskStatusInfos) {
        List<TaskDTO> taskStatusDTOs = new ArrayList<>();
        for (TaskStatusInfo taskStatusInfo : taskStatusInfos) {
        	if(!taskStatusInfo.getTaskStatus().equalsIgnoreCase("Yet To Start") && !taskStatusInfo.getTaskStatus().equalsIgnoreCase("Failed")) {
                TaskDTO taskStatusDTO = new TaskDTO();
                taskStatusDTO.setTaskStatusId(taskStatusInfo.getId());
                taskStatusDTO.setTaskStatus(taskStatusInfo.getTaskStatus());
                taskStatusDTOs.add(taskStatusDTO);	
        	}
        }
        return taskStatusDTOs;
    }

    public TaskDTO convertTaskInfotoTaskDTO(TaskDetailsInfo taskDetailsInfo,
                                            AssignedUserTaskInfo assignedUserTaskInfos) {
        SimpleDateFormat dateformet = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", new Locale("en", "US"));
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(taskDetailsInfo.getId());
        taskDTO.setTaskName(taskDetailsInfo.getTaskName());
        taskDTO.setTaskDetails(taskDetailsInfo.getTaskDetails());
        taskDTO.setTaskStatusId(assignedUserTaskInfos.getTaskStatusInfo().getId());
        taskDTO.setTaskStatus(assignedUserTaskInfos.getTaskStatusInfo().getTaskStatus());
        taskDTO.setUpdateDStartDate(date.format(taskDetailsInfo.getStartDate()));
        taskDTO.setUpdateEndDate(date.format(taskDetailsInfo.getEndDate()));
        taskDTO.setStartDate(taskDetailsInfo.getStartDate().toString());
        taskDTO.setEndDate(taskDetailsInfo.getEndDate().toString());
        taskDTO.setCreatedBy(taskDetailsInfo.getCreatedBy().getId());
        taskDTO.setUserName(taskDetailsInfo.getCreatedBy().getUserName());
        String colorCode = getTaskColor(dateformet.format(taskDetailsInfo.getStartDate()), dateformet.format(taskDetailsInfo.getEndDate()));
        taskDTO.setStatusColor(colorCode);
        if (taskDetailsInfo.getShipProfileInfo() != null) {
            taskDTO.setShipId(taskDetailsInfo.getShipProfileInfo().getId());
        }
        if (assignedUserTaskInfos.getUserAcceptRemark() != null) {
            taskDTO.setUserRemarks(assignedUserTaskInfos.getUserAcceptRemark());
        }

        return taskDTO;
    }

    public String getTaskColor(String startDate, String endDate) {
        SimpleDateFormat dateformet = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = dateformet.parse(startDate);
            d2 = dateformet.parse(endDate);
            long diffrentOfDays = dayBetween(d1, d2);
            LOGGER.info("diffrent date :::::::::::::" + diffrentOfDays + "::::::::::::::::::");
            if (diffrentOfDays >= 5)
                return "Green";
            else if (diffrentOfDays < 5 && diffrentOfDays >= 3)
                return "yellow";
            else
                return "Red";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
