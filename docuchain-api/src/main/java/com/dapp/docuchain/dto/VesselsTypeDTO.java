package com.dapp.docuchain.dto;

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
public class VesselsTypeDTO {
	
	private Long vesselsTypeId;
	
	private String vesselsTypeName;
	
	private Long userId;
	
	private Long[] vesselsTypeIds;

}
