package com.dapp.docuchain.controller;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.dapp.docuchain.dto.StatusResponseDTO;
import com.dapp.docuchain.dto.SubscriptionDTO;
import com.dapp.docuchain.service.SubscriptionService;
import com.dapp.docuchain.utility.SubscriptionUtility;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/docuchain/api/subscription")
@Api(value = "SubscriptionController", description = "Subscription Controller API")
@CrossOrigin
public class SubscriptionController {

	private static final Logger LOG = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private Environment env;

	@Autowired
	private SubscriptionUtility subscriptionUtility;

	@Autowired
	private SubscriptionService subscriptionService;


	@CrossOrigin
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "CREATE SUBSCRIPTION", notes = "This Method is used to create new Subscription for an organization")
	public @ResponseBody ResponseEntity<String> createSubscriptionPlan(
			@ApiParam(value = "Required subscription plan creation details", required = true) @RequestBody SubscriptionDTO subscriptionDTO) {

		LOG.info("inside subscription Creation" + subscriptionDTO);
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			String validParam = subscriptionUtility.validateSubscriptionParameter(subscriptionDTO);
			if (!validParam.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(validParam);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String isSubscriptionSave = subscriptionService.saveSubscription(subscriptionDTO);
			if (env.getProperty("success").equals(isSubscriptionSave)) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("subscription.create.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("subscription.create.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}


	/*
	 * This controller is created for organization update details.
	 * once admin finished the subscription plan then they will send one request to superadmin.
	 * Then superadmin update the details in subscription plan for that particular admin.
	 */

	@CrossOrigin
	@PostMapping(value = "/plan/update", produces = { "application/json" })
	@ApiOperation(value = "Update Subscription Plan", notes = "This Mehtod is used to update Subscription Plan")
	public @ResponseBody ResponseEntity<String> updateSubscriptionPlan(
			@ApiParam(value = "Required subscription plan creation details", required = true) @RequestBody SubscriptionDTO subscriptionDTO) {

		LOG.info("inside subscription Update" + subscriptionDTO);
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			String validParam = subscriptionUtility.validateSubscriptionParameter(subscriptionDTO);
			if (!validParam.equalsIgnoreCase("Success")) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(validParam);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String isSubscriptionValid = subscriptionService.isSubscriptionValid(subscriptionDTO);
			if (env.getProperty("failure").equals(isSubscriptionValid)) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("subscription.id.not.exist"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String isSubscriptionUpdate = subscriptionService.isSubscriptionUpdate(subscriptionDTO);
			if (env.getProperty("success").equals(isSubscriptionUpdate)) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("subscription.updated.successfully"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("subscription.updated.failed"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/plan/delete", produces = { "application/json" })
	@ApiOperation(value = "Delete Subscription Plan", notes = "This Mehtord is used to delete Subscription Plan")
	public @ResponseBody ResponseEntity<String> deleteSubscriptionPlan(
			@ApiParam(value = "Required subscription plan delete details", required = true) @RequestBody SubscriptionDTO subscriptionDTO) {

		LOG.info("inside subscription Delete" + subscriptionDTO);
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			String isSubscriptionValid = subscriptionService.isSubscriptionValid(subscriptionDTO);
			if (env.getProperty("failure").equals(isSubscriptionValid)) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("subscription.id.not.exist"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String isSubscriptionDelete = subscriptionService.isSubscriptionDelete(subscriptionDTO);
			if (env.getProperty("success").equals(isSubscriptionDelete)) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("subscription.deleted.successfully"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("subscription.deleted.failed"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}


	@CrossOrigin
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Subscription Plan List", notes = "This Mehtord is used to get subscription list")
	public ResponseEntity<String> getTaskStatusList() {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		List<SubscriptionDTO> subscriptionDTOs = subscriptionService.listAllSubscription();
		if (subscriptionDTOs != null) {
			statusResponseDTO.setMessage(env.getProperty("subscription.list.success"));
		}else {
			statusResponseDTO.setMessage(env.getProperty("subscription.list.failure"));
		}
		statusResponseDTO.setStatus(env.getProperty("success"));
		statusResponseDTO.setSubscriptionInfos(subscriptionDTOs);
		return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
	}


	  @CrossOrigin
	    @GetMapping(value = "/plan/list/{subscriptionId}", produces = {"application/json"})
	    @ApiOperation(value = "Subscription plan list", notes = "This method is used to get particular subscription")
	    public @ResponseBody
	    ResponseEntity<String> getTaskStatusList(@ApiParam(value = "Required subscription id ", required = true) @PathVariable(value = "subscriptionId") Long subscriptionId) throws Exception {
	        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

	        List<SubscriptionDTO> subscriptionList = new ArrayList<>();
	        subscriptionList = subscriptionService.getSubscriptionBaseOnId(subscriptionId);
	        if (subscriptionList != null) {
	            statusResponseDTO.setStatus(env.getProperty("success"));
	            statusResponseDTO.setSubscriptionInfos(subscriptionList);
	            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
	        }
	        statusResponseDTO.setStatus(env.getProperty("failure"));
	        statusResponseDTO.setMessage("Subscription Info Not Available");
	        return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
	    }

}
