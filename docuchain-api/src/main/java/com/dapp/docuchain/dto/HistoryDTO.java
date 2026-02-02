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
public class HistoryDTO {
private Long superAdminId;
private String objectStatus;
private Long modifiedById;
private Long objectId;
private Long id;
private String date;
private String objectName;
private String modifiedByName;
private String selectType;
private Long adminId;
private String subObjectName;
}
