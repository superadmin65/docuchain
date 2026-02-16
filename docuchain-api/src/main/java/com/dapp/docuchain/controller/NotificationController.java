package com.dapp.docuchain.controller;


import com.dapp.docuchain.dto.ShipProfileDTO;
import com.dapp.docuchain.dto.StatusResponseDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.DocumentNotification;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import com.dapp.docuchain.repository.DocumentHolderRepository;
import com.dapp.docuchain.repository.ExpiryDocumentRepository;
import com.dapp.docuchain.repository.ShipProfileRepository;
import com.dapp.docuchain.service.NotificationService;
import com.dapp.docuchain.service.ShipProfileService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/docuchain/api/notification")
@Api(value = "NotificationController ", description = "Notification Controller API")
@CrossOrigin
public class NotificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);
    @Autowired
    private Environment env;
    @Autowired
    private ShipProfileService shipProfileService;

    @Autowired
    private ShipProfileRepository shipProfileRepository;

    @Autowired
    private ExpiryDocumentRepository expiryDocumentRepository;

    @Autowired
    private DocumentHolderRepository documentHolderRepository;

    @Autowired
    private NotificationService notificationService;

    @CrossOrigin
    @GetMapping(value = "/add/notification", produces = {"application/json"})
    @ApiOperation(value = "add new Group tag", notes = "This Mehtord is used to add new CustomerGroup ")
    public @ResponseBody
    ResponseEntity<String> addNotification(@ApiParam(value = "Required Partners Details", required = true) @RequestBody UserDTO userDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus(env.getProperty("failure"));
        try {
            List<ShipProfileDTO> shipProfileDTOS = shipProfileService.getShipProfileInfoUsingRoleIdAndUserId(ShipProfileDTO.builder().userId(new Long(1)).roleId(new Long(2)).build());
            for (ShipProfileDTO shipProfileDTO : shipProfileDTOS) {
                List<ExpiryDocumentInfo> expiryDocumentInfos = expiryDocumentRepository.findByShipProfileInfoAndCurrentVersion(shipProfileRepository.findById(new Long(1)), 1);
                LOGGER.info("listSize::::" + expiryDocumentInfos.size());
                Date currentDate = new Date();
                if (expiryDocumentInfos != null && expiryDocumentInfos.size() > 0) {
                    for (ExpiryDocumentInfo expiryDocumentInfo : expiryDocumentInfos) {
                        DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(expiryDocumentInfo.getDocumentHolderInfo().getId());
                        LOGGER.info("expiryDocumentName" + expiryDocumentInfo.getDocumentName());
                        DocumentNotification documentNotification = new DocumentNotification();
                        documentNotification.setDocumentId(expiryDocumentInfo.getId());
                        Date expiryDate = expiryDocumentInfo.getExpiryDate();
                        long diff = expiryDate.getTime() - currentDate.getTime();
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                        if (diffDays >= 30)
                            documentNotification.setDescription(documentHolderInfo.getDocumentHolderName() + "Still have time to till date" + expiryDocumentInfo.getExpiryDate());
                        else if (diffDays < 30 && diffDays >= 15)
                            documentNotification.setDescription(documentHolderInfo.getDocumentHolderName() + " yet to expiry_on" + expiryDocumentInfo.getExpiryDate());
                        else if (diffDays < 15 && diffDays >= 0)
                            documentNotification.setDescription(documentHolderInfo.getDocumentHolderName() + " already expiryed on" + expiryDocumentInfo.getExpiryDate());
                        if (expiryDocumentInfo.getUploadedBy() != null)
                            documentNotification.setShipProfileId(expiryDocumentInfo.getShipProfileInfo().getId());
                        documentNotification.setUploadedUserId(expiryDocumentInfo.getUploadedBy().getId());
                        //documentNotificationRepository.save(documentNotification);
                    }

                }
            }
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            LOGGER.info("Exception is" + e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/notification", method = RequestMethod.GET, produces = {
            "application/json"})
    @ApiOperation(value = "document notification", notes = "get document notification")
    public @ResponseBody
    ResponseEntity<String> getNotification(@ApiParam(value = "Required Partners Details", required = true) @RequestBody UserDTO userDTO) throws ParseException {
        LOGGER.info("inside notification method");
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        List<UserDTO> userDTOs;
        try {
            userDTOs = notificationService.getNotification(userDTO);
            if (userDTOs != null && userDTOs.size() > 0) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setUserList(userDTOs);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.ACCEPTED);
            }
            statusResponseDTO.setStatus(env.getProperty("failure"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
