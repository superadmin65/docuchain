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
@Table(name = "data_deleted_history")
public class DataDeletedHistoryInfo {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(name = "object_id")
    private Long objectId;

    @Column(name = "object_one")
    private String objectOne;
    
    @Column(name = "object_two")
    private String objectTwo;
    
    @Column(name="date")
    private String date;
}
