package com.dapp.docuchain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "data_modified_history")
public class DataModifiedHistoryInfo {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(name = "object_id")
    private Long objectId;

    @Column(name = "object_status")
    private String objectStatus;
    
    @Column(name = "modified_by")
    private Long modifiedBy;
    
    @Column(name="date")
    private String date;
    
    @Column(name="object_name")
    private String objectName;
    
    @Column(name="who_modified")
    private String modifiedByName;
    
    @Column(name="select_type")
    private String selectType;
}
