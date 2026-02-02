package com.dapp.docuchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapp.docuchain.model.DataDeletedHistoryInfo;

public interface DataDeletedHistoryRepository extends JpaRepository<DataDeletedHistoryInfo, Long>{

}
