package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.DocumentDataInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentDataRepository extends JpaRepository<DocumentDataInfo, Long> {

}
