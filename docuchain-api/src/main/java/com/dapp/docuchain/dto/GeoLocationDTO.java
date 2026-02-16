package com.dapp.docuchain.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocationDTO {

	private Double latitude;
	private Double longitute;
	private Long userId;
	private Long shipId;
	private Long id;
	private Date updateDate;
}
