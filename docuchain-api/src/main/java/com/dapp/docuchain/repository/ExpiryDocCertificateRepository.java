package com.dapp.docuchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapp.docuchain.model.ExpiryCertificateTypeInfo;

public interface ExpiryDocCertificateRepository extends JpaRepository<ExpiryCertificateTypeInfo, Long>{

}
