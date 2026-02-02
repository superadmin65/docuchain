package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.ShipTypesInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ShipTypesRepository extends JpaRepository<ShipTypesInfo, Integer> {

    public List<ShipTypesInfo> findAll();

    public ShipTypesInfo findByShipTypesName(String typeName);

    public ShipTypesInfo findById(Long shipTypeId);
    
    List<ShipTypesInfo> findByOrganizationInfo(OrganizationInfo organizationInfo);
    
    ShipTypesInfo findByIdAndOrganizationInfo(Long vesselsTypeId, OrganizationInfo organizationInfo);
    
    ShipTypesInfo findByShipTypesNameAndOrganizationInfo(String vesselsTypeName, OrganizationInfo organizationInfo);
}
