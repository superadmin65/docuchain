package com.dapp.docuchain.service;

import java.util.List;

import com.dapp.docuchain.dto.HistoryDTO;

public interface HistoryService {
	List<HistoryDTO> getHistoryBaseOnSuperAdminId(HistoryDTO historyDTO);
	boolean isSuperAdminId(HistoryDTO historyDTO);
	boolean isAdminId(HistoryDTO historyDTO);
	boolean isValidId(HistoryDTO historyDTO);
	boolean isValidAdminId(HistoryDTO historyDTO);
	boolean isValidType(HistoryDTO historyDTO);
	List<HistoryDTO> getSAdminHistoryBaseOnSelectType(HistoryDTO historyDTO);
	List<HistoryDTO> getHistoryBaseOnAdminId(HistoryDTO historyDTO);
	List<HistoryDTO> getHistoryBaseOnSelectType(HistoryDTO historyDTO);
	
}
