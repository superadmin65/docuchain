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
public class RoleAliasDTO {
	
	private Long roleAliasId;
	
	private String roleAliasName;
	
	private Long roleId;
	
	private String roleName;
	
	private Long adminId;
	
	private Long[] roleAliasIds;

}
