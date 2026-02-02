package com.dapp.docuchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dapp.docuchain.model.GeoLocationInfo;

public interface GeoLocationRepository extends JpaRepository<GeoLocationInfo, Long>{

	GeoLocationInfo findByShipId (Long shipId);
}
