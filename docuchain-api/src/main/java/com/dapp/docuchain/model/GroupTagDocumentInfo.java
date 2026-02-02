package com.dapp.docuchain.model;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_tag_document_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupTagDocumentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "keyword")
    private String keyword;
    
    @Column(name = "email")
    private String emailId;
    
    @Column(name = "mode")
    private String mode;
   
    @CreationTimestamp
   	@Temporal(TemporalType.TIMESTAMP)
   	@Column(name = "create_date")
   	private Date createDate;

   	@UpdateTimestamp
   	@Temporal(TemporalType.TIMESTAMP)
   	@Column(name = "modify_date")
   	private Date modifyDate;
   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserProfileInfo userProfileInfo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id", referencedColumnName = "id")
    private ShipProfileInfo shipProfileInfo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "group_xref_expiry_document",
            joinColumns = {@JoinColumn(name = "group_tag_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "document_holder_id", referencedColumnName = "id")})
    private Set<DocumentHolderInfo> documentHolderInfo;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "groupTagDocumentInfo", cascade = CascadeType.ALL)
    private Set<GroupSharedDocumentInfo> groupSharedDocumentInfos;


}
