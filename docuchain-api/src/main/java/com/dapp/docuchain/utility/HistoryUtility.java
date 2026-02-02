package com.dapp.docuchain.utility;


import org.springframework.stereotype.Service;

import com.dapp.docuchain.dto.HistoryDTO;
import com.dapp.docuchain.model.DataModifiedHistoryInfo;

@Service
public class HistoryUtility {

	public HistoryDTO convertModelToDto(DataModifiedHistoryInfo list){
		HistoryDTO historyDTO=new HistoryDTO();
		historyDTO.setId(list.getId());
		historyDTO.setObjectId(list.getObjectId());
		historyDTO.setObjectName(list.getObjectName());
		historyDTO.setObjectStatus(list.getObjectStatus());
		historyDTO.setModifiedById(list.getModifiedBy());
		historyDTO.setModifiedByName(list.getModifiedByName());
		historyDTO.setSelectType(list.getSelectType());
		historyDTO.setDate(list.getDate());
		return historyDTO;
	}
}
