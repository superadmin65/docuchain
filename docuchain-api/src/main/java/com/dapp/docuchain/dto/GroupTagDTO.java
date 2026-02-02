package com.dapp.docuchain.dto;

import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupTagDTO {


    private Long id;
    private Long groupId;
    private String groupName;
    private String shipName;
    private Long userProfileId;
    private Long expiryDocId;
    private Long shipId;
    private String keyword;
    private String title;
    private String subject;
    private Long documentHolderId;
    private String emailId;
    private String mode;
    private Set<DocumentHolderInfo> documentHolderInfos;
    private String fileExtension;
    private List<ExpiryDocumentDTO> expiryDocumentDtos;
    private List<ExpiryDocumentDTO> remainingExpiryDocumentList;
    private Long[] documentHolderIds;
    private Long[] shipIds;
    private Long loginId;


}
