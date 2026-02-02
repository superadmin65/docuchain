package com.dapp.docuchain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dapp.docuchain.model.CountryInfo;
import com.dapp.docuchain.model.PortInfo;

public interface PortInfoRepository extends JpaRepository<PortInfo, Long> {
	
	List<PortInfo> findByCountryInfo(CountryInfo countryInfo);
	
	PortInfo findByPortName(String portName);
	
	PortInfo findByIdAndCountryInfo(Long portId, CountryInfo countryInfo);
	
	PortInfo findByPortNameAndCountryInfo(String portName, CountryInfo countryInfo);

}
