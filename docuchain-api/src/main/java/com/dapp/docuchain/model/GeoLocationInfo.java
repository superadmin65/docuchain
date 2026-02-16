package com.dapp.docuchain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
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
@Table(name = "geo_location_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GeoLocationInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitute")
	private Double longitute;

	@Column(name = "shipId")
	private Long shipId;
	
	@Column (name = "updateDate")
	private Date updateDate;

}
