package com.dapp.docuchain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dapp.docuchain.model.GroupSharedDocumentInfo;
import com.dapp.docuchain.model.GroupTagDocumentInfo;


public interface GroupSharedDocumentRepository extends JpaRepository<GroupSharedDocumentInfo, Long> {

	List<GroupSharedDocumentInfo> findByGroupTagDocumentInfo(GroupTagDocumentInfo groupTagDocumentInfo);
}
