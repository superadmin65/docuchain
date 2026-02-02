package com.dapp.docuchain.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Subscription_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscriptionInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "subscription_amount")
	private Long subscriptionAmount;

	@Column(name = "shipment_count")
	private Long shipmentCount;

	@Column(name = "user_count")
	private Long userCount;

	@Column(name = "subscription_days")
	private Long subscriptionDays;
	

	@Column(name = "subscription_userratio")
	private Long userVesslesRatio;
	
	@Column(name = "subscription_start_date")
	private Date subscriptionStartDate;

	@Column(name = "subscription_expire_date")
	private Date subscriptionExpireDate;

	@Column(name = "is_subsacription_alive")
	private Integer isStatusAlive;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "subscriptionInfo", cascade = CascadeType.ALL)
	private OrganizationInfo organizationInfo;

}
