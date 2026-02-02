package com.dapp.docuchain.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "country_info")
public class CountryInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "country_name")
	@NotNull
	private String countryName;

	@Column(name = "country_code")
	@NotNull
	private String countryCode;

	@Column(name = "isd_code")
	private String isdCode;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "countryInfo")
	private List<PortInfo> portInfos;

	public CountryInfo() {
		super();
	}

	public CountryInfo(Long id, String countryName, String countryCode) {
		super();
		this.id = id;
		this.countryName = countryName;
		this.countryCode = countryCode;
	}
}
