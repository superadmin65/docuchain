package com.dapp.docuchain.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document_status_history_info")
public class DocumentStatusInfo {

    @Id
    private Long id;

    @Column(name = "document_approval_status")
    private String status;

    @Column(name = "document_approval_desc")
    private String approvalDesc;

    @Column(name = "status_updated_at")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_user_info", referencedColumnName = "id", nullable = false)
    private UserProfileInfo userProfileInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expiry_document_id", referencedColumnName = "id", nullable = false)
    private ExpiryDocumentInfo expiryDocumentInfo;

}
