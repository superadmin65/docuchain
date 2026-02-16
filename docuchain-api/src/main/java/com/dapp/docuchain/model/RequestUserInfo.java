package com.dapp.docuchain.model;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request_user_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestUserInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "no_of_user")
	private int numberOfUser;

	@Column(name = "remarks")
	private String remarks;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = true)
	private OrganizationInfo organizationInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ship_profile_id", referencedColumnName = "id", nullable = false)
	private ShipProfileInfo userRequestShipProfile;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "specific_request_user_info", joinColumns = {
			@JoinColumn(name = "request_user_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "user_profile_id", referencedColumnName = "id", unique = false) })
	private Set<UserProfileInfo> requestedUserList;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requester_user_id", referencedColumnName = "id", nullable = false)
	private UserProfileInfo requesterInfo;

	@Column(name = "request_user_status")
	private String requestUserStatus;

}
