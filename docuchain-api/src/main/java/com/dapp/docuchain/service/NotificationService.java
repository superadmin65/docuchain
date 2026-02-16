package com.dapp.docuchain.service;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    List<UserDTO> getNotification(UserDTO userDTObj);

    List<UserDTO> getNotificatonCount(Long userId);

    Boolean setViewedNotification(Long userId);

	boolean deleteNotification(UserDTO userDTO);

	boolean deleteAllNotification(UserDTO userDTO);

	List<UserDTO> getNotificationByCategory(ExpiryDocumentDTO expiryDocumentDTO);

	boolean updateSnooze(UserDTO userDTO);
}
