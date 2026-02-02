package com.dapp.docuchain.service;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DocumentService {

    ExpiryDocumentDTO approveDocument(ExpiryDocumentDTO expiryDocumentDTO);

    List<UserDTO> getNotification();
}
