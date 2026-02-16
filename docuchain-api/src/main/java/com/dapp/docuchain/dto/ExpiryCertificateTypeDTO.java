package com.dapp.docuchain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpiryCertificateTypeDTO {

    private Long certificateTypeId;
    private String certificateType;
    private String expiryHolderDescription;

}
