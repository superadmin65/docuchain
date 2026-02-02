package com.dapp.docuchain.model;

import lombok.*;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document_data_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentDataInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "document_format")
    private String documentFormat;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_hash_code")
    private String documentHashCode;

    @CreationTimestamp
  	@Temporal(TemporalType.TIMESTAMP)
  	@Column(name = "create_date")
  	private Date createDate;

  	@UpdateTimestamp
  	@Temporal(TemporalType.TIMESTAMP)
  	@Column(name = "modify_date")
  	private Date modifyDate;

    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = "documentDataInfo", cascade=CascadeType.ALL)
    private ExpiryDocumentInfo expiryDocumentInfo;


}
