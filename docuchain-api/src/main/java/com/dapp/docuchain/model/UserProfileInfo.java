package com.dapp.docuchain.model;

import lombok.*;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profile_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserProfileInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "user_name", unique = true)
	private String userName;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "status")
	private Long status;

	@Column(name = "profile_picture_path")
	private String profilePicturePath;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	private RoleInfo roleId;

	@OneToMany(mappedBy = "uploadedBy", fetch = FetchType.LAZY, orphanRemoval = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ExpiryDocumentInfo> uploadedExpiryDocuments;

	@OneToMany(mappedBy = "approvedBy", fetch = FetchType.LAZY, orphanRemoval = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ExpiryDocumentInfo> approvedExpiryDocuments;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", referencedColumnName = "id")
	private OrganizationInfo organizationInfo;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<TaskDetailsInfo> taskDetailsInfos;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userProfileInfo", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<AssignedUserTaskInfo> assignedUserTaskInfos;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "requesterInfo", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<RequestUserInfo> requestUserInfos;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "creatorUserProfileInfo", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<UserReportAnIssueInfo> userReportAnIssueInfos;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "notifiedTo", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<UserNotificationInfo> userNotificationInfos;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userProfileInfo", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<GroupTagDocumentInfo> groupTagDocumentInfos;

	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "uploadedBy", orphanRemoval = false,cascade = CascadeType.PERSIST)
	private List<ExpiryDocumentInfo> uploadExpiryDocumentInfos;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "approvedBy",  orphanRemoval = false,cascade = CascadeType.PERSIST)
	private List<ExpiryDocumentInfo> approvedExpiryDocumentInfos;*/



}
