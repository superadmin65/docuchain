package com.dapp.docuchain.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dapp.docuchain.dto.HistoryDTO;
import com.dapp.docuchain.dto.StatusResponseDTO;
import com.dapp.docuchain.service.HistoryService;
import com.google.gson.Gson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/docuchain/api/history")
@Api(value = "HistoryController ", description = "History Controller API")
@CrossOrigin
public class HistoryController {

	@Autowired
	private Environment env;

	@Autowired
	private HistoryService historyService;

	@CrossOrigin
	@PostMapping(value = "/getHistorybySuperadmin", produces = { "application/json" })
	@ApiOperation(value = "Get History Based On SuperAdmin", notes = "This Method is used to get history based on superAdmin")
	public @ResponseBody ResponseEntity<String> getHistoryBasedOnSuperAdmin(
			@ApiParam(value = "get history based on SuperAdmin", required = true) @RequestBody HistoryDTO historyDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		List<HistoryDTO> historydto = new ArrayList<HistoryDTO>();
		boolean validationStatus=historyService.isValidId(historyDTO);
		if (validationStatus != true) {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("history.idmust"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		boolean status = historyService.isSuperAdminId(historyDTO);
		if (status != true) {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("history.notsuperadmin"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		historydto = historyService.getHistoryBaseOnSuperAdminId(historyDTO);
		if (historydto != null) {
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setHistoryInfo(historydto);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}
		statusResponseDTO.setStatus(env.getProperty("failure"));
		statusResponseDTO.setMessage(env.getProperty("history.notavailable"));
		return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
	}
	
	@CrossOrigin
	@PostMapping(value = "/getbyselecttype", produces = { "application/json" })
	@ApiOperation(value = "Get SuperAdmin History Based On SelectType", notes = "This Method is used to get superadmin history based on selecttype")
	public @ResponseBody ResponseEntity<String> getSAdminHistoryBasedOnSelecttype(
			@ApiParam(value = "get superadmin history based on Selecttype", required = true) @RequestBody HistoryDTO historyDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		List<HistoryDTO> historydto = new ArrayList<HistoryDTO>();
		boolean validationStatus=historyService.isValidType(historyDTO);
		if (validationStatus != true) {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("history.idmust"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		boolean status = historyService.isSuperAdminId(historyDTO);
		if (status != true) {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("history.notsuperadmin"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		historydto = historyService.getSAdminHistoryBaseOnSelectType(historyDTO);
		if (historydto != null) {
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setHistoryInfo(historydto);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}
		statusResponseDTO.setStatus(env.getProperty("failure"));
		statusResponseDTO.setMessage(env.getProperty("history.notavailable"));
		return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
	}
	
	@CrossOrigin
	@PostMapping(value = "/getHistorybyadmin", produces = { "application/json" })
	@ApiOperation(value = "Get History Based On Admin", notes = "This Method is used to get history based on Admin")
	public @ResponseBody ResponseEntity<String> getHistoryBasedOnAdmin(
			@ApiParam(value = "get history based on Admin", required = true) @RequestBody HistoryDTO historyDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		List<HistoryDTO> historydto = new ArrayList<HistoryDTO>();
		boolean validationStatus=historyService.isValidAdminId(historyDTO);
		if (validationStatus != true) {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("history.adminid"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		boolean status = historyService.isAdminId(historyDTO);
		if (status != true) {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("history.notadmin"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		historydto = historyService.getHistoryBaseOnAdminId(historyDTO);
		if (historydto != null) {
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setHistoryInfo(historydto);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}
		statusResponseDTO.setStatus(env.getProperty("failure"));
		statusResponseDTO.setMessage(env.getProperty("history.notavailable"));
		return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
	}
	
	@CrossOrigin
	@PostMapping(value = "/gethistorybasedonselecttype", produces = { "application/json" })
	@ApiOperation(value = "Get History Based On SelectType", notes = "This Method is used to get history based on SelectType")
	public @ResponseBody ResponseEntity<String> getHistoryBasedOnId(
			@ApiParam(value = "get history based on SelectType", required = true) @RequestBody HistoryDTO historyDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		List<HistoryDTO> historydto = new ArrayList<HistoryDTO>();
		boolean validationStatus=historyService.isValidAdminId(historyDTO);
		if (validationStatus != true) {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("history.adminid"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		boolean status = historyService.isAdminId(historyDTO);
		if (status != true) {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("history.notadmin"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		historydto = historyService.getHistoryBaseOnSelectType(historyDTO);
		if (historydto != null) {
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setHistoryInfo(historydto);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}
		statusResponseDTO.setStatus(env.getProperty("failure"));
		statusResponseDTO.setMessage(env.getProperty("history.notavailable"));
		return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
	}
}
