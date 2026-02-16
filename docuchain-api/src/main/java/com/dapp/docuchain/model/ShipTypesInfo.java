package com.dapp.docuchain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ship_type")
public class ShipTypesInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ship_types_name")
    private String shipTypesName;
    
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "organization_id")
	private OrganizationInfo organizationInfo;

}
