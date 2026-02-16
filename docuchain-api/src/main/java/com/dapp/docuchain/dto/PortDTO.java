package com.dapp.docuchain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortDTO {
	
	private Long portId;
	
	private String portName;
	
	private Long countryId;
	
	private Long userId;
	
	private Long[] portIds;
	
	private String countryName;

}
