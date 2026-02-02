package com.dapp.docuchain.dto;

import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class TaskDTO {
    List<UserDTO> userProfileInfos;
    private Long id;
    private Long taskId;
    private Long checkboxSelectionId;
    private Long shipId;
    private Long taskStatusId;
    private String taskDetails;
    private String updateDStartDate;
    private String updateEndDate;
    private String startDate;
    private String endDate;
    private String taskName;
    private Long[] userProfileIds;
    private Long createdBy;
    private String taskStatus;
    private String userRemarks;
    private String userName;
    private Long acceptStatus;
    private String assignedBy;
    private String shipName;
    private String statusColor;
    private Long loginId;
}
