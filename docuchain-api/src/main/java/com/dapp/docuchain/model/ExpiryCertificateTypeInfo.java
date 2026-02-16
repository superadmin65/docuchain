package com.dapp.docuchain.model;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Getter
@Setter
@Table(name = "expiry_certificate_type_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ExpiryCertificateTypeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "certificate_type")
    private String certificateType;

    @Column(name = "holder_description")
    private String expiryHolderDescription;

}
