package com.dapp.docuchain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


//import org.springframework.data.jpa.repository.JpaRepository;



import com.dapp.docuchain.model.DataModifiedHistoryInfo;


public interface DataModifiedHistoryRepository extends JpaRepository<DataModifiedHistoryInfo, Long>{
	List<DataModifiedHistoryInfo> findBySelectType(String selectType);
	List<DataModifiedHistoryInfo> findByModifiedBy(Long id);
	List<DataModifiedHistoryInfo> findByModifiedByAndSelectType(Long adminId,String objectName);
	List<DataModifiedHistoryInfo> findByModifiedByAndSelectTypeAndObjectStatus(Long adminId,String objectName,String subObjectName);
	List<DataModifiedHistoryInfo> findBySelectTypeOrSelectType(String organization,String subscription);
	List<DataModifiedHistoryInfo> findBySelectTypeAndObjectStatus(String selectType,String objectStatus);
}
