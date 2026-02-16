package com.dapp.docuchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapp.docuchain.model.FaqInfo;

public interface FaqRepository extends JpaRepository<FaqInfo, Long>{

}
