package com.dapp.docuchain.model;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scan_delimiter_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ScanDelimiterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "starting_pattern")
    private String startingPattern;

    @Column(name = "ending_pattern")
    private String endingPattern;

    @Column(name = "field_type")
    private String fieldType;

}
