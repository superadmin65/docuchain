package com.dapp.docuchain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryDTO {
	
	private Long countryId;
	
	private Long adminId;
	
	private String countryName;
	
	private String countryCode;
	
	private String isdCode;
	
	private Long[] countryIds;

}
