package com.dapp.docuchain.model;

import lombok.*;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_details_info")
public class TaskDetailsInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_detail")
    private String taskDetails;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;
    
    @CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id", referencedColumnName = "id")
    private ShipProfileInfo shipProfileInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_user_id", referencedColumnName = "id")
    private UserProfileInfo createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_status_id", referencedColumnName = "id")
    private TaskStatusInfo taskStatusInfo;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy="taskInfo", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<AssignedUserTaskInfo> assignedUserTaskInfos;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy="taskDetailsInfo", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<UserNotificationInfo> userNotificationInfos;
    
    

}
