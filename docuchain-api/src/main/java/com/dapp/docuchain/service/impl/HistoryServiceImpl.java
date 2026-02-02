package com.dapp.docuchain.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dapp.docuchain.dto.HistoryDTO;
import com.dapp.docuchain.model.DataModifiedHistoryInfo;
import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.RoleInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.DataModifiedHistoryRepository;
import com.dapp.docuchain.repository.RoleInfoRepository;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.service.HistoryService;
import com.dapp.docuchain.utility.HistoryUtility;

@Service
public class HistoryServiceImpl implements HistoryService {
	@Autowired
	UserProfileRepository userProfileRepository;

	@Autowired
	RoleInfoRepository roleInfoRepository;

	@Autowired
	DataModifiedHistoryRepository dataModifiedHistoryRepository;

	@Override
	public List<HistoryDTO> getHistoryBaseOnSuperAdminId(HistoryDTO historyDTO) {
		List<DataModifiedHistoryInfo> list = new ArrayList<DataModifiedHistoryInfo>();
		list = dataModifiedHistoryRepository.findBySelectTypeOrSelectType("Organization", "Subscription");
		List<HistoryDTO> historyList = new ArrayList<HistoryDTO>();
		if (list != null) {
			HistoryUtility historyUtility = new HistoryUtility();
			for (DataModifiedHistoryInfo info : list) {
				HistoryDTO history = historyUtility.convertModelToDto(info);
				historyList.add(history);
			}
		}
		return historyList;
	}

	@Override
	public List<HistoryDTO> getSAdminHistoryBaseOnSelectType(HistoryDTO historyDTO) {
		List<DataModifiedHistoryInfo> list = new ArrayList<DataModifiedHistoryInfo>();
		if (historyDTO.getObjectStatus() == null || historyDTO.getObjectStatus().isEmpty()) {
			list = dataModifiedHistoryRepository.findBySelectType(historyDTO.getSelectType());
		} else {
			list = dataModifiedHistoryRepository.findBySelectTypeAndObjectStatus(historyDTO.getSelectType(),
					historyDTO.getObjectStatus());
		}
		List<HistoryDTO> historyList = new ArrayList<HistoryDTO>();
		if (list != null) {
			HistoryUtility historyUtility = new HistoryUtility();
			for (DataModifiedHistoryInfo info : list) {
				HistoryDTO history = historyUtility.convertModelToDto(info);
				historyList.add(history);
			}
		}
		return historyList;
	}

	@Override
	public List<HistoryDTO> getHistoryBaseOnAdminId(HistoryDTO historyDTO) {
		List<DataModifiedHistoryInfo> list = new ArrayList<DataModifiedHistoryInfo>();
		list = dataModifiedHistoryRepository.findByModifiedBy(historyDTO.getAdminId());
		List<HistoryDTO> historyList = new ArrayList<HistoryDTO>();
		if (list != null) {
			HistoryUtility historyUtility = new HistoryUtility();
			for (DataModifiedHistoryInfo info : list) {
				HistoryDTO history = historyUtility.convertModelToDto(info);
				historyList.add(history);
			}
		}
		return historyList;
	}

	@Override
	public List<HistoryDTO> getHistoryBaseOnSelectType(HistoryDTO historyDTO) {
		List<DataModifiedHistoryInfo> list = new ArrayList<DataModifiedHistoryInfo>();
		if (StringUtils.isNotBlank(historyDTO.getObjectName()) && StringUtils.isBlank(historyDTO.getSubObjectName())) {
			list = dataModifiedHistoryRepository.findByModifiedByAndSelectType(historyDTO.getAdminId(),
					historyDTO.getObjectName());
		} else {
			list = dataModifiedHistoryRepository.findByModifiedByAndSelectTypeAndObjectStatus(historyDTO.getAdminId(),
					historyDTO.getObjectName(), historyDTO.getSubObjectName());
		}
		List<HistoryDTO> historyList = new ArrayList<HistoryDTO>();
		if (list != null) {
			HistoryUtility historyUtility = new HistoryUtility();
			for (DataModifiedHistoryInfo info : list) {
				HistoryDTO history = historyUtility.convertModelToDto(info);
				historyList.add(history);
			}
		}
		return historyList;
	}

	@Override
	public boolean isSuperAdminId(HistoryDTO historyDTO) {
		UserProfileInfo userProfileInfo = userProfileRepository.findById(historyDTO.getSuperAdminId());
		if (userProfileInfo != null) {
			RoleInfo roleId = userProfileInfo.getRoleId();
			RoleInfo roleInfo = roleInfoRepository.findById(roleId.getId());
			if (roleInfo != null && roleInfo.getRoleName().equals(Role.SuperAdmin)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isAdminId(HistoryDTO historyDTO) {
		UserProfileInfo userProfileInfo = userProfileRepository.findById(historyDTO.getAdminId());
		if (userProfileInfo != null) {
			RoleInfo roleId = userProfileInfo.getRoleId();
			RoleInfo roleInfo = roleInfoRepository.findById(roleId.getId());
			if (roleInfo != null && roleInfo.getRoleName().equals(Role.Admin)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isValidId(HistoryDTO historyDTO) {
		if (historyDTO.getSuperAdminId() != null && StringUtils.isNotBlank(historyDTO.getSuperAdminId().toString())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isValidType(HistoryDTO historyDTO) {
		if (historyDTO.getSuperAdminId() != null && StringUtils.isNotBlank(historyDTO.getSuperAdminId().toString())
				&& StringUtils.isNotBlank(historyDTO.getSelectType().toString())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isValidAdminId(HistoryDTO historyDTO) {
		if (historyDTO.getAdminId() != null && StringUtils.isNotBlank(historyDTO.getAdminId().toString())) {
			return true;
		} else {
			return false;
		}
	}

}
