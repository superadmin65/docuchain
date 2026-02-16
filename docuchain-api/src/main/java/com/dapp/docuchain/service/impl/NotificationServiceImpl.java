package com.dapp.docuchain.service.impl;

import com.dapp.docuchain.dto.DeletedHistoryDTO;
import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.*;
import com.dapp.docuchain.repository.ShipProfileRepository;
import com.dapp.docuchain.repository.UserNotificationRepository;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.service.NotificationService;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.NotificationUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private Environment env;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private NotificationUtility notificationUtility;

    @Autowired
    private CommonMethodsUtility commonMethodsUtility;

    @Autowired
    private ShipProfileRepository shipProfileRepository;

    public List<UserDTO> getNotification(UserDTO userDTObj) {

        UserProfileInfo userProfileInfo = userProfileRepository.findOne(userDTObj.getUserId());
        List<UserDTO> userDTOS = new ArrayList<UserDTO>();
        LocalDate currentDate = LocalDate.now();
        List<UserNotificationInfo> userNotificationInfos = new ArrayList<>();
		LocalDate lastMonth = currentDate.minusDays(30);
		LocalDate lastWeek = currentDate.minusDays(7);
		LocalDate lastWeekOfWeek = currentDate.minusDays(14);
		LocalDate lastMonthOfMonth = currentDate.minusDays(60);
        if (userProfileInfo != null) {
        	if (userDTObj.getFilterByDay().equalsIgnoreCase( "Renewel")){
        		 userNotificationInfos = userNotificationRepository.findByNotifiedToAndNotificationTimeBetween(userProfileInfo,java.sql.Date.valueOf(lastWeek),java.sql.Date.valueOf(currentDate));
        	}
        	if (userDTObj.getFilterByDay().equalsIgnoreCase("Lastweek")){
       		 	userNotificationInfos = userNotificationRepository.findByNotifiedToAndNotificationTimeBetween(userProfileInfo,java.sql.Date.valueOf(lastWeekOfWeek),java.sql.Date.valueOf(lastWeek));
        	}
        	if (userDTObj.getFilterByDay().equalsIgnoreCase("Lastmonth")){
        		userNotificationInfos = userNotificationRepository.findByNotifiedToAndNotificationTimeBetween(userProfileInfo,java.sql.Date.valueOf(lastMonthOfMonth),java.sql.Date.valueOf(lastMonth));
        	}
        	if (userDTObj.getFilterByDay().equalsIgnoreCase("Older")){
        		userNotificationInfos = userNotificationRepository.findByNotifiedToAndNotificationTimeBefore(userProfileInfo,java.sql.Date.valueOf(lastMonthOfMonth));

        	}
             //userNotificationInfos = userNotificationRepository.findByNotifiedToOrderByNotificationTimeDesc(userProfileInfo);
            if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
                LOGGER.info("Is this Coming here");
                for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
                    LOGGER.info("userNotificationInfo time" + userNotificationInfo.getNotificationTime());
                    UserDTO userDTO = new UserDTO();
                   // String notifiedOn = notificationUtility.getDateDifference(userNotificationInfo.getNotificationTime());
                    if (userNotificationInfo.getVesselId() != null){
                        ShipProfileInfo shipProfileInfo = shipProfileRepository.findById(userNotificationInfo.getVesselId());
                        userDTO.setVesselName(shipProfileInfo.getShipName());
                        userDTO.setImo(shipProfileInfo.getIMO());
                        if (shipProfileInfo.getShipProfilePicPath() != null)
                        userDTO.setShipProfilePath( env.getProperty("picture.path")+shipProfileInfo.getShipProfilePicPath());
                        }
                   // userDTO.setNotifiedOn(notifiedOn);
                    userDTO.setNotificationMessage(userNotificationInfo.getDescription());
                    userDTO.setNotificationId(userNotificationInfo.getId());
                    userDTO.setNotificationType(userNotificationInfo.getNotificationType());
                    userDTO.setNotifyDate(userNotificationInfo.getNotificationTime());
                    RequestUserInfo requestUserInfo = userNotificationInfo.getRequestUserInfo();
                    ExpiryDocumentInfo expiryDocumentInfo = userNotificationInfo.getExpiryDocumentInfo();
                    TaskDetailsInfo taskDetailsInfo = userNotificationInfo.getTaskDetailsInfo();

                    if (requestUserInfo != null) {
                        LOGGER.info("Request User");
                        userDTO.setNotificationName(requestUserInfo.getUserRequestShipProfile().getShipName());
                        userDTO.setShipId(requestUserInfo.getUserRequestShipProfile().getId());
                        userDTO.setColor(userNotificationInfo.getColor());
                        userDTO.setNotificationType(userNotificationInfo.getNotificationType());
                    }
                    if (expiryDocumentInfo != null) {
                        LOGGER.info("Expiry Document");
                        Date expiryDate = expiryDocumentInfo.getExpiryDate();
                        userDTO.setNotificationName(expiryDocumentInfo.getShipProfileInfo().getShipName());
                        userDTO.setShipId(expiryDocumentInfo.getShipProfileInfo().getId());
                        userDTO.setColor(userNotificationInfo.getColor());
                        userDTO.setNotificationType(userNotificationInfo.getNotificationType());
                        userDTO.setExpiryDocumentId(expiryDocumentInfo.getId());
                        userDTO.setNotificationType(env.getProperty("notification.document.typenew"));
                        userDTO.setDocumentName(expiryDocumentInfo.getDocumentName());
                        userDTO.setVesselName(expiryDocumentInfo.getShipProfileInfo().getShipName());
                        userDTO.setImo(expiryDocumentInfo.getShipProfileInfo().getIMO());
                    }
                    if (taskDetailsInfo != null) {
                        userDTO.setNotificationName(taskDetailsInfo.getTaskName());
                        userDTO.setNotificationType(env.getProperty("notification.task.typenew"));
                        userDTO.setColor(userNotificationInfo.getColor());
                        userDTO.setTaskId(taskDetailsInfo.getId());
                        userDTO.setTaskName(taskDetailsInfo.getTaskName());
                        if (taskDetailsInfo.getShipProfileInfo() != null){
                        	userDTO.setVesselName(taskDetailsInfo.getShipProfileInfo().getShipName());
                        	userDTO.setImo(taskDetailsInfo.getShipProfileInfo().getIMO());
                        }

                        LOGGER.info("Task Details");
                    }
                    userDTOS.add(userDTO);
                }
            }
        }
        return userDTOS;
    }

    @Override
    public List<UserDTO> getNotificatonCount(Long userId) {

        UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
        List<UserDTO> userDTOS = new ArrayList<UserDTO>();
        if (userProfileInfo != null) {
            List<UserNotificationInfo> userNotificationInfos = userNotificationRepository.findByNotifiedToAndReadStatusOrderByNotificationTimeDesc(userProfileInfo, 0);
            if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
                LOGGER.info("Is this notification count Coming here");
                for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
                    LOGGER.info("userNotificationInfo time" + userNotificationInfo.getNotificationTime());
                    UserDTO userDTO = new UserDTO();
                   // String notifiedOn = notificationUtility.getDateDifference(userNotificationInfo.getNotificationTime());
                   // userDTO.setNotifiedOn(notifiedOn);
                    userDTO.setNotificationMessage(userNotificationInfo.getDescription());

                    RequestUserInfo requestUserInfo = userNotificationInfo.getRequestUserInfo();
                    ExpiryDocumentInfo expiryDocumentInfo = userNotificationInfo.getExpiryDocumentInfo();
                    TaskDetailsInfo taskDetailsInfo = userNotificationInfo.getTaskDetailsInfo();

                    if (requestUserInfo != null) {
                        LOGGER.info("Request User");
                        userDTO.setNotificationName(requestUserInfo.getUserRequestShipProfile().getShipName());
                        userDTO.setColor("Green");
                        userDTO.setNotificationType(env.getProperty("notification.user.type"));
                    }
                    if (expiryDocumentInfo != null) {
                        LOGGER.info("Expiry Document");
                        userDTO.setNotificationName(expiryDocumentInfo.getShipProfileInfo().getShipName());
                        userDTO.setColor("Green");
                        userDTO.setNotificationType(env.getProperty("notification.document.type"));
                    }
                    if (taskDetailsInfo != null) {
                        userDTO.setNotificationName(taskDetailsInfo.getTaskName());
                        userDTO.setNotificationType(env.getProperty("notification.task.type"));
                        userDTO.setColor("Green");
                        LOGGER.info("Task Details");
                    }
                    userDTOS.add(userDTO);
                }
            }
        }
        return userDTOS;
    }

    @Override
    public Boolean setViewedNotification(Long userId) {
        UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
        List<UserDTO> userDTOS = new ArrayList<UserDTO>();
        if (userProfileInfo != null) {
            List<UserNotificationInfo> userNotificationInfos = userNotificationRepository.findByNotifiedToOrderByNotificationTimeDesc(userProfileInfo);
            if (userNotificationInfos != null && userNotificationInfos.size() > 0) {

                for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
                    userNotificationInfo.setReadStatus(1);
                    userNotificationInfo = userNotificationRepository.save(userNotificationInfo);
                }
                return true;
            }
        }

        return false;
    }

	@Override
	public boolean deleteNotification(UserDTO userDTO) {
		try{
			UserNotificationInfo userNotificationInfo = userNotificationRepository.findOne(userDTO.getNotificationId());
			DeletedHistoryDTO deletedHistoryDTO=new DeletedHistoryDTO();
            deletedHistoryDTO.setObjectId(userNotificationInfo.getId());
            deletedHistoryDTO.setObjectOne(userNotificationInfo.getDescription());
            deletedHistoryDTO.setObjectTwo(userNotificationInfo.getNotificationType());
            DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
            userNotificationRepository.delete(userNotificationInfo);
            //if(dataDeletedHistoryInfo!=null)
            //commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),userNotificationInfo.getNotificationType(),"Notification", env.getProperty("history.deleted"), userDTO.getLoginId());

		return true;
		}catch (Exception e ){
			return false;
		}

	}

	@Override
	public boolean deleteAllNotification(UserDTO userDTOs) {
		try{
			if (userDTOs != null ){
				UserProfileInfo userProfileInfo = userProfileRepository.findById(userDTOs.getUserId());
				List<UserNotificationInfo> userNotificationInfos  = userNotificationRepository.findByNotifiedTo(userProfileInfo);
				if (userNotificationInfos != null && userNotificationInfos.size() > 0){
					for (UserNotificationInfo userNotificationInfo : userNotificationInfos){
						DeletedHistoryDTO deletedHistoryDTO=new DeletedHistoryDTO();
			            deletedHistoryDTO.setObjectId(userNotificationInfo.getId());
			            deletedHistoryDTO.setObjectOne(userNotificationInfo.getDescription());
			            deletedHistoryDTO.setObjectTwo(userNotificationInfo.getNotificationType());
			            //DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
			            userNotificationRepository.delete(userNotificationInfo);
					}

				}
			return true;
			}
			}catch (Exception e ){
				return false;
			}


		return false;
	}

	@Override
	public List<UserDTO> getNotificationByCategory(ExpiryDocumentDTO expiryDocumentDTO) {


        UserProfileInfo userProfileInfo = userProfileRepository.findOne(expiryDocumentDTO.getUserId());
        List<UserDTO> userDTOS = new ArrayList<UserDTO>();
        if (userProfileInfo != null) {
            List<UserNotificationInfo> userNotificationInfos = userNotificationRepository.findByNotifiedToOrderByNotificationTimeDesc(userProfileInfo);
            if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
                LOGGER.info("Is this Coming here");
                for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
                	if (userNotificationInfo.getNotificationType().equals(expiryDocumentDTO.getCategory())){
                    LOGGER.info("userNotificationInfo time" + userNotificationInfo.getNotificationTime());
                    UserDTO userDTO = new UserDTO();
                    //String notifiedOn = notificationUtility.getDateDifference(userNotificationInfo.getNotificationTime());
                    //userDTO.setNotifiedOn(notifiedOn);
                    userDTO.setNotificationMessage(userNotificationInfo.getDescription());
                    userDTO.setNotificationId(userNotificationInfo.getId());

                    RequestUserInfo requestUserInfo = userNotificationInfo.getRequestUserInfo();
                    ExpiryDocumentInfo expiryDocumentInfo = userNotificationInfo.getExpiryDocumentInfo();
                    TaskDetailsInfo taskDetailsInfo = userNotificationInfo.getTaskDetailsInfo();

                    if (requestUserInfo != null) {
                        LOGGER.info("Request User");
                        userDTO.setNotificationName(requestUserInfo.getUserRequestShipProfile().getShipName());
                        userDTO.setShipId(requestUserInfo.getUserRequestShipProfile().getId());
                        userDTO.setColor(userNotificationInfo.getColor());
                        userDTO.setNotificationType(userNotificationInfo.getNotificationType());
                    }
                    if (expiryDocumentInfo != null) {
                        LOGGER.info("Expiry Document");
                        userDTO.setNotificationName(expiryDocumentInfo.getShipProfileInfo().getShipName());
                        userDTO.setShipId(expiryDocumentInfo.getShipProfileInfo().getId());
                        userDTO.setColor(userNotificationInfo.getColor());
                        userDTO.setNotificationType(userNotificationInfo.getNotificationType());
                    }
                    if (taskDetailsInfo != null) {
                        userDTO.setNotificationName(taskDetailsInfo.getTaskName());
                        userDTO.setNotificationType(userNotificationInfo.getNotificationType());
                        userDTO.setColor(userNotificationInfo.getColor());
                        LOGGER.info("Task Details");
                    }
                    userDTOS.add(userDTO);
                	}
                }
            }
        }
        return userDTOS;

	}

	@Override
	public boolean updateSnooze(UserDTO userDTO) {
		UserNotificationInfo userNotificationInfo = userNotificationRepository.findOne(userDTO.getNotificationId());
		if(userNotificationInfo != null){
			int daysForIncrement = 0;
			if (userDTO.getSnooze() == 1)
				daysForIncrement = 1;
			if (userDTO.getSnooze() == 2)
				daysForIncrement = 7;
			if (userDTO.getSnooze() == 3)
				daysForIncrement = 14;
			if (userDTO.getSnooze() == 4)
				daysForIncrement = 31;
			String currentDate = userNotificationInfo.getNotificationTime().toString();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(sdf.parse(currentDate));
				c.add(Calendar.DATE, daysForIncrement);
				currentDate = sdf.format(c.getTime());
				SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd");
				Date date2=formatter2.parse(currentDate);
				userNotificationInfo.setNotificationTime(date2);
				userNotificationInfo = userNotificationRepository.saveAndFlush(userNotificationInfo);
				System.out.println("Finally enter in the snooze option"+currentDate+date2);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
