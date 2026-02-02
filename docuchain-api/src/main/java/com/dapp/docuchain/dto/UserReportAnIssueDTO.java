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
public class UserReportAnIssueDTO {
	
	private Long reportIssueId;
	
	private Long userId;
	
	private String name;
	
	private String organizationName;
	
	private String email;
	
	private String phoneNumber;
	
	private String reason;
	
	private Long organizationId;

}
