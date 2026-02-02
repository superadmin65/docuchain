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
public class DeletedHistoryDTO {
	private Long objectId;
	private String objectOne;
	private String objectTwo;
}
