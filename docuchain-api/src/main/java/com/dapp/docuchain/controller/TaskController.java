package com.dapp.docuchain.controller;

import com.dapp.docuchain.dto.StatusResponseDTO;
import com.dapp.docuchain.dto.TaskDTO;
import com.dapp.docuchain.model.TaskDetailsInfo;
import com.dapp.docuchain.service.TaskService;
import com.dapp.docuchain.utility.TaskUtility;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/docuchain/api/task")
@Api(value = "TaskController", description = "Task Controller API")
@CrossOrigin
public class TaskController {

    private static final Logger LOG = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    private Environment env;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskUtility taskUtility;

    @CrossOrigin
    @PostMapping(value = "/create/shiprelated", produces = {"application/json"})
    @ApiOperation(value = "Create Ship Task", notes = "This Mehtord is used to create new task")
    public @ResponseBody
    ResponseEntity<String> createShipRelatedTask(
            @ApiParam(value = "Required task creation details", required = true) @RequestBody TaskDTO taskDTO) {

        LOG.info("inside ContactRegister" + taskDTO);
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {

            String validParam = taskUtility.validateShipTaskParameter(taskDTO);
            if (!validParam.equalsIgnoreCase("Success")) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(validParam);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean isUserExists = taskUtility.isUserExists(taskDTO.getCreatedBy());
            if (!isUserExists) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage("User is not exists");
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            String response = taskUtility.validateSourceExist(taskDTO);
            if (!response.equalsIgnoreCase("Success")) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(response);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            String isDateValid = taskUtility.isDateValid(taskDTO);
            if (!isDateValid.equalsIgnoreCase("Success")) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(isDateValid);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            // validate given ship id exit or not
            if (taskDTO.getCheckboxSelectionId() == 1) {
                String shipExists = taskService.validateShipExists(taskDTO.getShipId());
                if (!shipExists.equalsIgnoreCase("Success")) {
                    statusResponseDTO.setStatus(env.getProperty("failure"));
                    statusResponseDTO.setMessage(shipExists);
                    return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
                }
            }
            String saveShipRelatedTaskInfo = taskService.saveShipRelatedTaskInfo(taskDTO);
            if (env.getProperty("task.create.success").equals(saveShipRelatedTaskInfo)) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("task.create.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("task.create.failure"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

        } catch (Exception e) {
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }

    }

    @CrossOrigin
    @PostMapping(value = "/create/nonshiprelated", produces = {"application/json"})
    @ApiOperation(value = "Create Non Ship Task", notes = "This Mehtord is used to create new task")
    public @ResponseBody
    ResponseEntity<String> createNonShipRelatedTask(
            @ApiParam(value = "Required task creation details", required = true) @RequestBody TaskDTO taskDTO) {

        LOG.info("inside ContactRegister" + taskDTO);
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {

            boolean isUserExists = taskUtility.isUserExists(taskDTO.getCreatedBy());
            if (!isUserExists) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage("User is not exists");
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            String response = taskUtility.validateSourceExist(taskDTO);
            if (!response.equalsIgnoreCase("Success")) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(response);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            String isDateValid = taskUtility.isDateValid(taskDTO);
            if (!isDateValid.equalsIgnoreCase("Success")) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(isDateValid);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            String saveShipRelatedTaskInfo = taskService.saveNonShipRelatedTaskInfo(taskDTO);
            if (env.getProperty("task.create.success").equals(saveShipRelatedTaskInfo)) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("task.create.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("task.create.failure"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

        } catch (Exception e) {
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }

    }

    @CrossOrigin
    @PostMapping(value = "/update/shiprelatedtask", produces = {"application/json"})
    @ApiOperation(value = "update task", notes = "This Mehtord is used to update task")
    public @ResponseBody
    ResponseEntity<String> UpdateShipTask(
            @ApiParam(value = "Required task creation details", required = true) @RequestBody TaskDTO taskDTO)
            throws Exception {

        LOG.info("inside TaskUpdate" + taskDTO);
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        String validParam = taskUtility.validateShipTaskParameter(taskDTO);
        if (!validParam.equalsIgnoreCase("Success")) {
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(validParam);
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
        boolean isUserExists = taskUtility.isUserExists(taskDTO.getCreatedBy());
        if (!isUserExists) {
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage("User is not exists");
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
        String isDateValid = taskUtility.isDateValid(taskDTO);
        if (!isDateValid.equalsIgnoreCase("Success")) {
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(isDateValid);
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
        boolean isTask = taskService.isTask(taskDTO.getId());
        if (!isTask) {
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("task.id.failure"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

        }
        // validate given ship id exit or not
        if (taskDTO.getCheckboxSelectionId() == 1) {
            String shipExists = taskService.validateShipExists(taskDTO.getShipId());
            if (!shipExists.equalsIgnoreCase("Success")) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(shipExists);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
        }

        String isRegister = taskService.UpdateShipTask(taskDTO);
        if (env.getProperty("task.update.success").equals(isRegister)) {
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setMessage(env.getProperty("task.update.success"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        }
        statusResponseDTO.setStatus(env.getProperty("failure"));
        statusResponseDTO.setMessage(env.getProperty("task.update.failure"));
        return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
    }

    @CrossOrigin
    @PostMapping(value = "/update/nonshiprelatedtask", produces = {"application/json"})
    @ApiOperation(value = "update task", notes = "This Mehtord is used to update task")
    public @ResponseBody
    ResponseEntity<String> UpdateNonShipTask(
            @ApiParam(value = "Required task creation details", required = true) @RequestBody TaskDTO taskDTO)
            throws Exception {

        LOG.info("inside TaskUpdate" + taskDTO);
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        if (!taskUtility.validateUpdateNonShipParamForAssignTask(taskDTO)) {
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
        boolean isUserExists = taskUtility.isUserExists(taskDTO.getCreatedBy());
        if (!isUserExists) {
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage("User is not exists");
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
        String isDateValid = taskUtility.isDateValid(taskDTO);
        if (!isDateValid.equalsIgnoreCase("Success")) {
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(isDateValid);
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
        boolean isTask = taskService.isTask(taskDTO.getId());
        if (!isTask) {
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("task.id.failure"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

        }

        String isRegister = taskService.UpdateNonShipTask(taskDTO);
        if (env.getProperty("task.update.success").equals(isRegister)) {
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setMessage(env.getProperty("task.update.success"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        }
        statusResponseDTO.setStatus(env.getProperty("failure"));
        statusResponseDTO.setMessage(env.getProperty("task.update.failure"));
        return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
    }

    @CrossOrigin
    @GetMapping(value = "/assignedbyuser/{userid}", produces = {"application/json"})
    @ApiOperation(value = "ASSIGNED BY YOU TASK LIST", notes = "This Mehtord is used to get ship task list and other task list ")
    public @ResponseBody
    ResponseEntity<String> assignedByUserTaskList(
            @ApiParam(value = "Required task details", required = true) @PathVariable(value = "userid") Long userId)
            throws Exception {
        LOG.info("ship task list and other task list" + userId);
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        boolean isUserExists = taskUtility.isUserExists(userId);
        if (!isUserExists) {
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage("User is not exists");
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }

        List<TaskDTO> getAllAssignedByUser = taskService.getAllAssignedByUser(userId);
        statusResponseDTO.setTaskAssignedByUser(getAllAssignedByUser);
        statusResponseDTO.setStatus(env.getProperty("success"));
        return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        
    }

    @CrossOrigin
    @GetMapping(value = "/assignedtouser/{userid}", produces = {"application/json"})
    @ApiOperation(value = "ASSIGNED TO YOU TASK LIST", notes = "This Mehtord is used to get ship task list and other task list ")
    public @ResponseBody
    ResponseEntity<String> assignedtoTaskList(
            @ApiParam(value = "Required task details", required = true) @PathVariable(value = "userid") Long userId)
            throws Exception {
        LOG.info("ship task list and other task list" + userId);
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        boolean isUserExists = taskUtility.isUserExists(userId);
        if (!isUserExists) {
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage("User is not exists");
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
        List<TaskDTO> otherTaskList = taskService.getTaskAssignedTouser(userId);
        statusResponseDTO.setStatus(env.getProperty("success"));
        statusResponseDTO.setTaskAssignedToUser(otherTaskList);
        return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/status/update", produces = {"application/json"})
    @ApiOperation(value = "Assigned status to task", notes = "This Mehtord is used to Assigned status to task")
    public @ResponseBody
    ResponseEntity<String> statusAssignedToTask(
            @ApiParam(value = "Required task id and status", required = true) @RequestBody TaskDTO taskDTO) {
        LOG.info("Required task id and status" + taskDTO);
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {
            boolean isValidParams = taskUtility.validateTaskStatusParams(taskDTO);
            if (!isValidParams) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

            boolean isTask = taskService.isTask(taskDTO.getTaskId());
            if (!isTask) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("task.id.failure"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

            }
            boolean isuserTaskExits = taskService.isUserTaskExits(taskDTO);
            if (!isuserTaskExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage("Given user dont have any task ");
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

            }

            String isupdateTaskStatus = taskService.isupdateTaskStatus(taskDTO);
            if (isupdateTaskStatus.equalsIgnoreCase("Success")) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("task.status.update.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("task.status.update.failure"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("task.status.update.failure"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/status/user/{taskid}", produces = {"application/json"})
    @ApiOperation(value = "TaskStatusList", notes = "This Mehtord is used to get TaskStatusList ")
    public @ResponseBody
    ResponseEntity<String> getTaskStatusList(
            @ApiParam(value = "Required task details", required = true) @PathVariable(value = "taskid") Long taskId)
            throws Exception {

        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {
            boolean isTask = taskService.isTask(taskId);
            if (!isTask) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("task.id.failure"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

            }
            List<TaskDTO> taskStatusList = taskService.getTaskStatusList(taskId);
            if (taskStatusList != null && taskStatusList.size() > 0) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setStatusOfTask(taskStatusList);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage("Task Status Info Not Available");
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("task.status.update.failure"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }

    
    
    @CrossOrigin
    @PostMapping(value = "/delete",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "DELETE TASK", notes = "This Mehtord is used to delete task ")
    public @ResponseBody
    ResponseEntity<String> deleteTask(
            @ApiParam(value = "Required task details", required = true) @RequestBody TaskDTO taskDTO){

        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {
        	String taskDeleteValidation = taskService.taskdeleteValidation(taskDTO);
        	if(!taskDeleteValidation.equalsIgnoreCase(env.getProperty("success"))){
        		 statusResponseDTO.setStatus(env.getProperty("failure"));
                 statusResponseDTO.setMessage(taskDeleteValidation);
                 return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        	}
            TaskDetailsInfo taskStatusList = taskService.deleteTask(taskDTO);
            if (taskStatusList != null) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("task.delete.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("task.delete.failure"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/status/all", produces = {"application/json"})
    @ApiOperation(value = "TaskStatusList", notes = "This Mehtord is used to get TaskStatusList ")
    public @ResponseBody
    ResponseEntity<String> getTaskStatusList() throws Exception {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

        List<TaskDTO> taskStatusList = new ArrayList<>();
        taskStatusList = taskService.getAllTaskStatusList();
        if (taskStatusList != null) {
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setStatusAll(taskStatusList);
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        }
        statusResponseDTO.setStatus(env.getProperty("failure"));
        statusResponseDTO.setMessage("Task Status Info Not Available");
        return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
    }

}
