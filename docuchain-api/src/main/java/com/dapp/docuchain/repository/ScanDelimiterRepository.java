package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.ScanDelimiterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ScanDelimiterRepository extends JpaRepository<ScanDelimiterInfo, Long> {

    Set<ScanDelimiterInfo> findByFieldType(String fieldType);

}
