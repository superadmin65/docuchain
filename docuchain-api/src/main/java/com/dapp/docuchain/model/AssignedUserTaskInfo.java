package com.dapp.docuchain.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assigned_task_info")
public class AssignedUserTaskInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", referencedColumnName = "id")
    private UserProfileInfo userProfileInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private TaskDetailsInfo taskInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_status_id", referencedColumnName = "id")
    private TaskStatusInfo taskStatusInfo;

    @Column(name = "user_accept_status")
    private Integer userAcceptStatus;

    @Column(name = "user_accept_remark")
    private String userAcceptRemark;

}
