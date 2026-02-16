package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.CountryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryInfoRepository extends JpaRepository<CountryInfo, Long> {

    CountryInfo findByCountryName(String countryName);
    
    CountryInfo findByCountryCode(String countryCode);
    
  //CountryInfo findByCountryNameOrCountryCodeAndOrganizationInfo(String countryName, String countryCode, OrganizationInfo organizationInfo);

    CountryInfo findByCountryNameOrCountryCode(String countryName, String countryCode);
    
    CountryInfo findByCountryNameAndCountryCode(String countryName, String countryCode);
    
    //public List<StateInfo> findByState(Integer countryId);

    public CountryInfo findById(Long id);
    
    //public CountryInfo findByIdAndOrganizationInfo(Long countryId, OrganizationInfo organizationInfo);
    
    //public List<CountryInfo> findByOrganizationInfo(OrganizationInfo organizationInfo);

}
