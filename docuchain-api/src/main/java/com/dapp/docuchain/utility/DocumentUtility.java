package com.dapp.docuchain.utility;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.DocumentNotification;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import com.dapp.docuchain.model.ShipProfileInfo;
import com.dapp.docuchain.repository.ExpiryDocumentRepository;
import com.dapp.docuchain.repository.ShipProfileRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DocumentUtility {

    @Autowired
    ExpiryDocumentRepository expiryDocumentRepository;

    @Autowired
    ShipProfileRepository shipProfileRepository;

    public ExpiryDocumentDTO convertExpiryDocumentInfotoExpiryDocumentDTO(ExpiryDocumentInfo expiryDocumentInfo) {

        ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
        expiryDocumentDTO.setId(expiryDocumentInfo.getId());
        expiryDocumentDTO.setDocumentName(expiryDocumentInfo.getDocumentName());
        expiryDocumentDTO.setStatus(expiryDocumentInfo.getStatus());
        return expiryDocumentDTO;
    }

    public List<UserDTO> findNotificationColor(List<DocumentNotification> documentNotificationInfo) {

        List<UserDTO> userDTOs = new ArrayList<>();
        //DocumentNotificationDTO documentNotificationDTO = new DocumentNotificationDTO();
        Date currentDate = new Date();
        for (DocumentNotification documentNotification : documentNotificationInfo) {
            UserDTO userDTO = new UserDTO();
            ExpiryDocumentInfo expiryDocumentInfo = expiryDocumentRepository.findOne(documentNotification.getDocumentId());
            Date expiryDate = expiryDocumentInfo.getExpiryDate();
            long diff = expiryDate.getTime() - currentDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            System.out.print("Days count" + diffDays);
            if (diffDays >= 30) {
                ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(documentNotification.getShipProfileId());
                //UserProfileInfo userProfileInfo = userProfileRepository.findOne(documentNotification.getUploadedUserId());
                if (shipProfileInfo != null) {
                    //userDTO.setUserName(userProfileInfo.getUserName());
                    userDTO.setShipName(shipProfileInfo.getShipName());
                    userDTO.setColor("Green");
                    userDTO.setNotificationMessage(documentNotification.getDescription());
                }
            } else if (diffDays < 30 && diffDays >= 15) {
                ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(documentNotification.getShipProfileId());
                //UserProfileInfo userProfileInfo = userProfileRepository.findOne(documentNotification.getUploadedUserId());
                if (shipProfileInfo != null) {
                    //userDTO.setUserName(userProfileInfo.getUserName());
                    userDTO.setShipName(shipProfileInfo.getShipName());
                    userDTO.setColor("Yellow");
                    userDTO.setNotificationMessage(documentNotification.getDescription());
                }
            } else if (diffDays < 15 && diffDays >= 0) {
                ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(documentNotification.getShipProfileId());
                //UserProfileInfo userProfileInfo = userProfileRepository.findOne(documentNotification.getUploadedUserId());
                if (shipProfileInfo != null) {
                    //userDTO.setUserName(userProfileInfo.getUserName());
                    userDTO.setShipName(shipProfileInfo.getShipName());
                    userDTO.setColor("Red");
                    userDTO.setNotificationMessage(documentNotification.getDescription());
                }
            }
            userDTOs.add(userDTO);

        }
        return userDTOs;

    }

    public boolean isValid(ExpiryDocumentDTO expiryDocumentDTO) {

        if (expiryDocumentDTO.getId() != null && expiryDocumentDTO.getStatus() != null && StringUtils.isNotEmpty(expiryDocumentDTO.getStatus())) {
            return true;
        }
        return false;
    }

}
