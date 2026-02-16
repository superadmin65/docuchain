package com.dapp.docuchain.model;

import lombok.*;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@Table(name = "task_status_info")
public class TaskStatusInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "task_status")
    private String taskStatus;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy="taskStatusInfo")
    private List<TaskDetailsInfo> taskDetailsInfos;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy="taskStatusInfo")
	private List<AssignedUserTaskInfo> assignedUserTaskInfos;
    
    public TaskStatusInfo() {
	}
    
	public TaskStatusInfo(Long id, String taskStatus) {
		super();
		this.id = id;
		this.taskStatus = taskStatus;
	}
}
