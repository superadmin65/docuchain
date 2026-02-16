package com.dapp.docuchain.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "document_notification_info")
public class DocumentNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "ship_profile_id")
    private Long shipProfileId;

    @Column(name = "uploaded_user_id")
    private Long uploadedUserId;

    @Column(name = "doc_description")
    private String description;

}
