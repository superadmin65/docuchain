package com.dapp.docuchain.controller;

import com.dapp.docuchain.dto.GeoLocationDTO;
import com.dapp.docuchain.dto.RequestUserDTO;
import com.dapp.docuchain.dto.ShipProfileDTO;
import com.dapp.docuchain.dto.StatusResponseDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.service.ShipProfileService;
import com.dapp.docuchain.service.UserService;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.ShipProfileUtility;
import com.dapp.docuchain.utility.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.NameAlreadyBoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/docuchain/api/user")
@Api(value = "UserController", description = "User Controller API")
@CrossOrigin
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserUtils userUtils;

	@Autowired
	ShipProfileUtility shipProfileUtility;

	@Autowired
	private UserService userService;

	@Autowired
	private ShipProfileService shipProfileService;

	@Autowired
	private Environment env;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private CommonMethodsUtility commonMethodsUtility;

	@CrossOrigin
	@PostMapping(value = "/login", produces = { "application/json" })
	@ApiOperation(value = "Login user", notes = "Validate user and allow user to login")
	public ResponseEntity<String> loginUser(
			@ApiParam(value = "Required user mobile number and password ", required = true) @RequestBody UserDTO userDTO,
			HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("inside login controller");
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isValid = userService.isValid(userDTO);
			if (!isValid) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String basicValidation = userService.loginUserBasicValidation(userDTO);
			if(!basicValidation.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(basicValidation);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			UserDTO isLogin = userService.authenticate(userDTO.getUserName(), userDTO.getPassword());
			LOGGER.info("Login Status --->" + isLogin);
			if (isLogin != null) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("login.success"));
				statusResponseDTO.setUserInfos(isLogin);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("login.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/create", produces = { "application/json" })
	@ApiOperation(value = "CREATE USER", notes = "Create new user validate email and passowrd and user")
	public ResponseEntity<String> createUser(
			@ApiParam(value = "User name, passowrd, email, organization is required", required = true) @RequestParam(name = "userInfo", value = "userInfo", required = true) String userInfo,
			@RequestParam(name = "userProfilePicture", value = "userProfilePicture", required = false) MultipartFile userProfilePictureMultipartFile) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			UserDTO userDTO = objectMapper.readValue(userInfo, UserDTO.class);
			boolean isValid = userService.isValidcreate(userDTO);
			if (!isValid) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean checkAdminParam = userUtils.checkAdminParam(userDTO);
			if (!checkAdminParam) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			String verfiyParamResponse = userUtils.verifyParamResponse(userDTO);
			if (!verfiyParamResponse.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(verfiyParamResponse);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			/*if (!userDTO.getBusinessCategory().equalsIgnoreCase("SuperAdmin")
					&& !userDTO.getBusinessCategory().equalsIgnoreCase("Admin")) {
				boolean vaildAdminUserCount = userUtils.vaildAdminUserCount(userDTO);
				if (vaildAdminUserCount) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("user.count.exists"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
			}*/

			boolean isValidEmailId = userUtils.validateEmail(userDTO.getMail().toLowerCase());
			if (!isValidEmailId) {

				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("register.validate.email"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

			}
			/*boolean isRoleValid = userUtils.validRole(userDTO.getBusinessCategory());
			if (!isRoleValid) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("role.exist"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}*/
			boolean isAlreadyExits = userUtils.isAlreadyExits(userDTO.getMail());
			if (isAlreadyExits) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("email.exist"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

			}
			if(userDTO.getShipProfileIds() != null && userDTO.getShipProfileIds().length > 0){
				String isShipExists = userUtils.checkShipProfileIsExists(userDTO);
				if(!isShipExists.equalsIgnoreCase(env.getProperty("success"))){
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(isShipExists);
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
			}

			if(userProfilePictureMultipartFile != null){
				if(!userProfilePictureMultipartFile.isEmpty()){
					if(!commonMethodsUtility.isImage(userProfilePictureMultipartFile)) {
						statusResponseDTO.setStatus(env.getProperty("failure"));
						statusResponseDTO.setMessage(env.getProperty("image.type.invalid"));
						return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
					}
				}
			}
			boolean isCreate = userService.create(userDTO,userProfilePictureMultipartFile);
			if (isCreate) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("create.user.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("create.user.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (NameAlreadyBoundException e) {
			statusResponseDTO.setCodeStatus(100);
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("user.exist"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("exception::::::::" + e.getMessage());
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@GetMapping(value = "/search/{businessCategory}", produces = { "application/json" })
	@ApiOperation(value = "search user", notes = "Validate user and allow user search")
	public ResponseEntity<String> searchUser(
			@ApiParam(value = "Required user ", required = true) @PathVariable String businessCategory,
			HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("inside search user controller");
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isValid = userService.isSearchValid(businessCategory);
			if (!isValid) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			List<UserDTO> userList = userService.search(businessCategory);
			LOGGER.info("searchlist::" + userList.size());
			if (userList != null && userList.size() > 0) {

				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setUserList(userList);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("search.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/modify", produces = { "application/json" })
	@ApiOperation(value = "modify user", notes = "Validate user and allow user modify")
	public ResponseEntity<String> modifyUserPassword(
			@ApiParam(value = "Required user ", required = true) @RequestBody UserDTO userDTO,
			HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("inside create user controller");
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isValid = userService.isValid(userDTO);
			if (!isValid) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			boolean modify = userService.modify(userDTO);
			if (modify) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("update.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("update.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@CrossOrigin
	@PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "DELETE USER", notes = "Delete users information")
	public ResponseEntity<String> deleteUser(@ApiParam(value = "User details required", required = true) @RequestBody UserDTO userDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String deleteUserResult = userService.deleteUserInformation(userDTO);
			if (!deleteUserResult.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(deleteUserResult);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if(deleteUserResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("user.delete.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("user.delete.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@CrossOrigin
	@PostMapping(value = "/changePassword", produces = { "application/json" })
	@ApiOperation(value = "Change Password", notes = "Validate user and allow password change")
	public ResponseEntity<String> changeUserPassword(
			@ApiParam(value = "Required user ", required = true) @RequestBody UserDTO userDTO,
			HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("inside change password user controller");
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isValid = userService.isValidPassword(userDTO);
			if (!isValid) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			boolean modify = userService.modify(userDTO);
			if (modify) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("update.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("update.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/updateUserLists", produces = { "application/json" })
	@ApiOperation(value = "Update UserLists", notes = "Updating UserLists")
	public ResponseEntity<String> updateUserLists(
			@ApiParam(value = "Update UserLists ", required = true) @RequestBody UserDTO userDTO,
			HttpServletRequest request, HttpServletResponse response) {

		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isValid = userService.isValidInput(userDTO);
			if (!isValid) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			boolean update = userService.update(userDTO);
			if (update) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("update.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("update.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/ship/shipprofile", produces = { "application/json" })
	@ApiOperation(value = "add shipprofile", notes = "Add Ship Proile Details ")
	public ResponseEntity<String> addshipProfile(
			@ApiParam(value = "shipprofile", required = true) @RequestBody ShipProfileDTO shipProfileDTO) {

		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {

			boolean isValid = userUtils.validateShipProfileParam(shipProfileDTO);
			if (!isValid) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			String isValidoffNo = userUtils.offNoValidateForShipProfile(shipProfileDTO);
			if (!isValidoffNo.equalsIgnoreCase("Success")) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(isValidoffNo);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isshipprofile = shipProfileService.addShipProfile(shipProfileDTO);
			if (isshipprofile) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.success"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/ship/countrystate", produces = { "application/json" })
	@ApiOperation(value = "Get States", notes = "Getting states based on the country")
	public synchronized ResponseEntity<String> stateList(
			@ApiParam(value = "Get States", required = true) @RequestBody ShipProfileDTO shipProfileDTO) {

		StatusResponseDTO statusResponsedto = new StatusResponseDTO();

		try {

			boolean isValidcountrytostate = userUtils.validateCountryIdorName(shipProfileDTO);
			if (!isValidcountrytostate) {
				statusResponsedto.setStatus(env.getProperty("failure"));
				statusResponsedto.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<>(new Gson().toJson(statusResponsedto), HttpStatus.PARTIAL_CONTENT);
			}
			ShipProfileDTO shipDTO = shipProfileService.stateLists(shipProfileDTO);
			if (shipDTO != null) {
				statusResponsedto.setStateList(shipDTO);
				statusResponsedto.setStatus(env.getProperty("success"));
				statusResponsedto.setMessage(env.getProperty("shipprofile.countrytostate"));
				return new ResponseEntity<>(new Gson().toJson(statusResponsedto), HttpStatus.OK);

			}
			statusResponsedto.setStatus(env.getProperty("failure"));
			statusResponsedto.setMessage(env.getProperty("shipprofile.failure"));
			return new ResponseEntity<>(new Gson().toJson(statusResponsedto), HttpStatus.PARTIAL_CONTENT);
		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponsedto.setStatus(env.getProperty("failure"));
			statusResponsedto.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponsedto), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@GetMapping(value = "/ship/shiptypes", produces = { "application/json" })
	@ApiOperation(value = "Get ship types Lists", notes = "Getting types of shiplist")
	public ResponseEntity<String> shipTypes() {

		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {

			ShipProfileDTO shipDTO = shipProfileService.typesOfShip();

			if (shipDTO == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setOneShipInfo(shipDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.shiptypes"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@GetMapping(value = "/ship/findAll", produces = { "application/json" })
	@ApiOperation(value = "Find All Ship Profile", notes = " Finding all the Ship Profile Info")
	public ResponseEntity<String> shipListNeededAllShipsInfo() {

		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {
			ShipProfileDTO shipDTO = shipProfileService.findAllShip();

			if (shipDTO == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setShipInfo(shipDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.findallship"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/ship/truetofalse", produces = { "application/json" })
	@ApiOperation(value = "True False Status", notes = " Setting true and false")
	public ResponseEntity<String> changeStatus(
			@ApiParam(value = "True False Status", required = true) @RequestBody Long officialNum) {

		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {

			boolean isValidcountrytostate = userUtils.validateOffNo(officialNum);
			if (!isValidcountrytostate) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			Boolean atd = shipProfileService.changeStatus(officialNum);
			if (!atd) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.acttodeact"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/ship/readshipinfo", produces = { "application/json" })
	@ApiOperation(value = "Retrieve Ship Info", notes = "Retrieving Individual Info for Update")
	public ResponseEntity<String> requiredDetailsForUpdateShip(
			@ApiParam(value = "Retrieve  Ship Info", required = true) @RequestBody Long officialNum) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {

			boolean isValidcountrytostate = userUtils.validateOffNo(officialNum);
			if (!isValidcountrytostate) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			ShipProfileDTO shipProfileDTO = shipProfileService.getDetailsForUpdateShip(officialNum);
			if (shipProfileDTO == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			statusResponseDTO.setOneShipInfo(shipProfileDTO);
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.readinfo"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/shipRshipinfo", produces = { "application/json" })
	@ApiOperation(value = "Update Ship Info", notes = "Updating Individual Ship Profile Info")
	public ResponseEntity<String> updateShip(
			@ApiParam(value = "Update Ship Info", required = true) @RequestBody ShipProfileDTO shipProfileDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {

			boolean isValidcountrytostate = userUtils.validateUpdateParam(shipProfileDTO);
			if (!isValidcountrytostate) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			Boolean shipUpdate = shipProfileService.shipUpdate(shipProfileDTO);
			if (!shipUpdate) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.update"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/ship/deleteshipinfo", produces = { "application/json" })
	@ApiOperation(value = "Delete Ship Info", notes = "Delete Individual Ship Info")
	public ResponseEntity<String> shipDelete(
			@ApiParam(value = "Delete Ship  Info", required = true) @RequestBody ShipProfileDTO shipProfileDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {

			boolean isValidcountrytostate = userUtils.validateOffNo(shipProfileDTO.getOfficialNo());
			if (!isValidcountrytostate) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			Boolean atd = shipProfileService.shipDelete(shipProfileDTO);

			if (!atd) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.delete"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@GetMapping(value = "/ship/roleshipname", produces = { "application/json" })
	@ApiOperation(value = "Get Roles and ShipNames", notes = " Getting all the roles and shipnames")
	public ResponseEntity<String> roleShipName() {

		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {
			ShipProfileDTO shipDTO = shipProfileService.roleShip();
			if (shipDTO == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			List<ShipProfileDTO> shipDTOO = shipProfileService.assignShipCount();
			if (shipDTOO == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setShipCount(shipDTOO);
				statusResponseDTO.setRoleShipNames(shipDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.roleshipinfo"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/ship/saveroles", produces = { "application/json" })
	@ApiOperation(value = "Save Roles", notes = "SavingRoles in ShipProfile")
	public ResponseEntity<String> saveRoles(
			@ApiParam(value = "Save Roles", required = true) @RequestBody ShipProfileDTO shipProfileDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			ShipProfileDTO shipDTO = shipProfileService.saveRoles(shipProfileDTO);

			if (shipDTO != null) {
				statusResponseDTO.setStatusCode(shipDTO);
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {

				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.saveroles"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@GetMapping(value = "/ship/getAllUserLists", produces = { "application/json" })
	@ApiOperation(value = "Get All User Lists", notes = "Getting All the UserLists")
	public ResponseEntity<String> getAllUserLists() {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			List<ShipProfileDTO> shipDTO = shipProfileService.getAllUserLists();
			if (shipDTO == null) {

				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setAllUserLists(shipDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.getalluserlists"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@GetMapping(value = "/getAllUser/{userid}", produces = { "application/json" })
	@ApiOperation(value = "search user", notes = "Validate user and allow user get profile")
	public ResponseEntity<String> getAllUser(
			@ApiParam(value = "Required user ", required = true) @PathVariable Long userid, HttpServletRequest request,
			HttpServletResponse response) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			List<UserDTO> userDTO = userService.getAllUser(userid);
			if (userDTO != null) {
				statusResponseDTO.setGetUserList(userDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.getalluserlists"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}
	@CrossOrigin
	@GetMapping(value = "/getadminusetlist/{adminid}", produces = { "application/json" })
	@ApiOperation(value = "search user", notes = "Validate user and allow user get profile")
	public ResponseEntity<String> getAdminUser(
			@ApiParam(value = "Required user ", required = true) @PathVariable Long adminid, HttpServletRequest request,
			HttpServletResponse response) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			List<UserDTO> userDTO = userService.getAdminAllUser(adminid);
			if (userDTO != null) {
				statusResponseDTO.setGetUserList(userDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.getalluserlists"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@GetMapping(value = "/getorganizationuserlist/{adminid}", produces = { "application/json" })
	@ApiOperation(value = "search user", notes = "Validate user and allow user get profile")
	public ResponseEntity<String> getOrganizationUser(
			@ApiParam(value = "Required user ", required = true) @PathVariable Long adminid, HttpServletRequest request,
			HttpServletResponse response) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			List<UserDTO> userDTO = userService.getOrganizationUser(adminid);
			if (userDTO != null) {
				statusResponseDTO.setGetUserList(userDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.getalluserlists"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@GetMapping(value = "/activatealluser/{adminid}", produces = { "application/json" })
	@ApiOperation(value = "search user", notes = "Validate user and allow user get profile")
	public ResponseEntity<String> activatealluser(
			@ApiParam(value = "Required user ", required = true) @PathVariable Long adminid, HttpServletRequest request,
			HttpServletResponse response) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			 boolean isAdminExists = userUtils.isAdminExists(adminid);
	            if (!isAdminExists) {
	                statusResponseDTO.setStatus(env.getProperty("failure"));
	                statusResponseDTO.setMessage("Admin is not exists");
	                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
	            }
			List<UserProfileInfo> userProfileInfoList = userService.activatealluser(adminid);
			if (userProfileInfoList != null &&userProfileInfoList.size()>0) {
				userProfileRepository.save(userProfileInfoList);
				//statusResponseDTO.setGetUserList(userDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage("All User activated succesfully");
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("Problem in acivate all users"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}
	@CrossOrigin
	@GetMapping(value = "/deactivatealluser/{adminid}", produces = { "application/json" })
	@ApiOperation(value = "search user", notes = "Validate user and allow user get profile")
	public ResponseEntity<String> deactivatealluser(
			@ApiParam(value = "Required user ", required = true) @PathVariable Long adminid, HttpServletRequest request,
			HttpServletResponse response) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			 boolean isAdminExists = userUtils.isAdminExists(adminid);
	            if (!isAdminExists) {
	                statusResponseDTO.setStatus(env.getProperty("failure"));
	                statusResponseDTO.setMessage("Admin is not exists");
	                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
	            }
			List<UserProfileInfo> userProfileInfoList = userService.deactivatealluser(adminid);
			if (userProfileInfoList != null &&userProfileInfoList.size()>0) {
				userProfileRepository.save(userProfileInfoList);
				//statusResponseDTO.setGetUserList(userDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage("All User Deactivated succesfully");
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("Problem in acivate all users"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}
	@CrossOrigin
	@PostMapping(value = "/ship/infoforupdateuserlists", produces = { "application/json" })
	@ApiOperation(value = "Info for User Lists", notes = "Retrieve Information for User Profiles")
	public ResponseEntity<String> getUserListsInfoforUpdate(
			@ApiParam(value = "Info for User Lists", required = true) @RequestBody String userName) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			Boolean vaildateUserName = userUtils.vaildateUserName(userName);
			if (!vaildateUserName) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			ShipProfileDTO shipDTO = shipProfileService.getUserProfileListsInfoforUpdate(userName);
			if (shipDTO == null) {

				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setUserInfo(shipDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("userInfoForUpdate"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@GetMapping(value = "/ship/getUserShipCount", produces = { "application/json" })
	@ApiOperation(value = "Get Ship and User Count", notes = "Getting Ship and User Count")
	public ResponseEntity<String> getUserShipCount() {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			ShipProfileDTO shipDTO = shipProfileService.getUserShipCount();
			if (shipDTO == null) {

				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			List<ShipProfileDTO> userLastSeenHistory = shipProfileService.getUserLastSeenAdmin();

			if (userLastSeenHistory.size() < 0) {

				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setAdminLastSeenHistoryList(userLastSeenHistory);
				statusResponseDTO.setUsershipCount(shipDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.usershipcount"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
    @GetMapping(value = "/ship/getLastSeen/{userId}", produces = {"application/json"})
    @ApiOperation(value = "Get LastSeen", notes = "Getting LastSeen")
    public ResponseEntity<String> getLastSeen(
            @ApiParam(value = "Get LastSeen", required = true)@PathVariable Long userId) {

		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			Boolean validUserId = userUtils.validUserID(userId);
			if (!validUserId) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("notavailable"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			List<ShipProfileDTO> shipmasterLastSeenHistory = shipProfileService.getLastSeen(userId);
			if (shipmasterLastSeenHistory.size() == 0) {

				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setShipMasterLastSeen(shipmasterLastSeenHistory);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.usershipcount"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/ship/getExpiryDetails", produces = { "application/json" })
	@ApiOperation(value = "Get ExpiryDetails", notes = "Retrieve ExpiryDetails ")
	public ResponseEntity<String> getExpiryDetailsForShipmaster(
			@ApiParam(value = "Get ExpiryDetails", required = true) @RequestBody ShipProfileDTO shipProfileDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			String validateResponse = shipProfileUtility.validateShipProfileUserAndRoleId(shipProfileDTO);
			if (!validateResponse.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setMessage(validateResponse);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			String checkResponse = shipProfileService.checkUserIdExists(shipProfileDTO.getUserId());
			if (!checkResponse.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setMessage(checkResponse);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			ShipProfileDTO shipDTO = shipProfileService.getExpiryDetails(shipProfileDTO);
			if (shipDTO == null) {

				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setUserInfo(shipDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("userInfoForUpdate"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/ship/get", produces = { "application/json" })
	@ApiOperation(value = "Get ship profile", notes = "Getting ship profile based on role id")
	public ResponseEntity<String> getShipProfileBasedOnRole(
			@ApiParam(value = "Required userId and roleId", required = true) @RequestBody ShipProfileDTO shipProfileDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		statusResponseDTO.setStatus("failure");
		List<ShipProfileDTO> shipProfileList = null;
		try {
			String validateResponse = shipProfileUtility.validateShipProfileUserAndRoleId(shipProfileDTO);
			if (!validateResponse.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setMessage(validateResponse);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String checkResponse = shipProfileService.checkUserIdExists(shipProfileDTO.getUserId());
			if (!checkResponse.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setMessage(checkResponse);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			shipProfileList = shipProfileService.getShipProfileInfoUsingRoleIdAndUserId(shipProfileDTO);
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.list.success"));
			statusResponseDTO.setShipProfileList(shipProfileList);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		} catch (Exception e) {
			LOGGER.info("problem in getShipProfileBasedOnRole method in UserController");
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@PostMapping(value = "/ship/getAssignedUserNames", produces = { "application/json" })
	@ApiOperation(value = "Get Assigned UserNames", notes = "Retrieve Assigned User Information for ShipName")
	public ResponseEntity<String> getAssignedUserNames(
			@ApiParam(value = "Get Assigned UserNames", required = true) @RequestBody Long offNO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			Boolean vaildateUserName = userUtils.validateOffNo(offNO);
			if (!vaildateUserName) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			List<ShipProfileDTO> shipDTO = shipProfileService.getAssignedUserNames(offNO);
			if (shipDTO == null) {

				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setShipProfileList(shipDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.usershipcount"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/request", produces = { "application/json" })
	@ApiOperation(value = "create user", notes = "User requesrt")
	public ResponseEntity<String> requestUser(
			@ApiParam(value = "Required number of user ", required = true) @RequestBody RequestUserDTO requestUserDTO,
			HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("inside request user controller");
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isValid = userUtils.isValidRequest(requestUserDTO);
			if (!isValid) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			boolean isCreate = userService.createRequest(requestUserDTO);
			if (isCreate) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("request.user.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("request.user.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@RequestMapping(value = "/getPendingRequest/{userId}", method = RequestMethod.GET, produces = { "application/json" })
	@ApiOperation(value = "get pending request", notes = "Validate pending request user")
	public ResponseEntity<String> getPendingRequest(
			@ApiParam(value = "Required user ", required = true)@PathVariable Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		LOGGER.info("inside getPendingRequest controller");
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			List<RequestUserDTO> requestUserDTOs = userService.getPendingRequestList(userId);
			LOGGER.info("searchlist::" + requestUserDTOs.size());
			if (requestUserDTOs != null && requestUserDTOs.size() > 0) {

				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setRequestUserDTOs(requestUserDTOs);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/approvel", produces = { "application/json" })
	@ApiOperation(value = "approvel user", notes = "Approvel user")
	public ResponseEntity<String> approvelUser(
			@ApiParam(value = "Required number of user ", required = true) @RequestBody RequestUserDTO requestUserDTO,
			HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("inside request user controller");
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isCreate = userService.approvelUser(requestUserDTO);
			if (isCreate) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				if(requestUserDTO.getRequestUserStatus().equalsIgnoreCase("Rejected")){
					statusResponseDTO.setMessage(env.getProperty("user.approval.reject"));
				}else {
					statusResponseDTO.setMessage(env.getProperty("approvel.user.success"));
				}
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("approvel.user.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@GetMapping(value = "/ship/list", produces = { "application/json" })
	public @ResponseBody ResponseEntity<String> getTaskStatusList() throws Exception {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			ShipProfileDTO shipDTO = shipProfileService.findAllShip();

			if (shipDTO == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setShipInfo(shipDTO);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.findallship"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST, consumes = {
			"application/json" }, produces = { "application/json" })
	@ApiOperation(value = "Forgot password", notes = "regenerate password and send to email id")
	public @ResponseBody ResponseEntity<String> forgotPassword(
			@ApiParam(value = "Required user details", required = true) @RequestBody UserDTO userDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isValid = userUtils.isValidForgerPassParaam(userDTO);
			if (!isValid) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isValidEmailId = userUtils.validateEmail(userDTO.getMail().toLowerCase());
			if (!isValidEmailId) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("register.validate.email"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

			}

			boolean isEmailIdExits = userUtils.isEmailIdExits(userDTO.getMail());
			if (!isEmailIdExits) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("emailId.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

			}
			String forgetPassword = userService.generatePassword();
			System.err.println(forgetPassword);

			boolean userPassword = userService.userPassword(userDTO.getMail(), forgetPassword);
			if (userPassword) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("forgetPassword.success"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("forgetPassword.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Problem in forgot password  : ", e);
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/ship/statusUserProfileLists", produces = { "application/json" })
	@ApiOperation(value = "Status", notes = " Change Status")
	public ResponseEntity<String> statusUserProfileLists(
			@ApiParam(value = "Status", required = true) @RequestBody Long userId) {

		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {

			boolean isValidcountrytostate = userUtils.validateUserId(userId);
			if (!isValidcountrytostate) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			Boolean atd = shipProfileService.statusChangeUserLists(userId);
			if (!atd) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage("User status updation is failed");
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.acttodeact"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@RequestMapping(value = "/getProfile/{userId}", method = RequestMethod.GET, produces = { "application/json" })
	@ApiOperation(value = "search user", notes = "Validate user and allow user get profile")
	public ResponseEntity<String> getProfile(
			@ApiParam(value = "Required user ", required = true) @PathVariable Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		LOGGER.info("inside search user controller");
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			List<UserDTO> userList = userService.searchProfile(userId);
			LOGGER.info("searchlist::" + userList.size());
			if (userList != null && userList.size() > 0) {

				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setUserList(userList);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("search.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/scan", produces = { "application/json" })
	@ApiOperation(value = "upload scanning document", notes = "scan document and read values")
	public ResponseEntity<String> documentScan(
			@ApiParam(value = "Required file attachment", required = true) @RequestParam String userName,
			@RequestParam(name = "scanFile", value = "scanFile", required = true) MultipartFile scanFile,
			HttpServletRequest request, HttpServletResponse response) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		statusResponseDTO.setStatus("failure");
		try {
			LOGGER.info("Scan File is Empty --->" + scanFile.getOriginalFilename());
			if (scanFile.isEmpty()) {
				LOGGER.info("Scan File is Empty --->" + scanFile.getOriginalFilename());
				statusResponseDTO.setMessage(env.getProperty("file.not.exists"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String imageType = Files.getFileExtension(scanFile.getOriginalFilename());
			LOGGER.info("File extension" + imageType);
			if (!scanFile.isEmpty()) {
				// Boolean expiryDocumentDTO = shipProfileService.scanImageFile(userName ,
				// scanFile);
				// if(expiryDocumentDTO){
				// System.out.println("Entered Inside______________");
				// statusResponseDTO.setStatus(env.getProperty("success"));
				// statusResponseDTO.setMessage(env.getProperty("file.upload"));
				// return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO),
				// HttpStatus.OK);
				// }

			}
			System.out.println("CAME OUTSIDE");
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Problem in documentScan  : ", e);
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/ship/getByteData", produces = { "application/json" })
	@ApiOperation(value = "Byte Data File", notes = "Retrieve Byte Information for User Profile Picture")
	public ResponseEntity<String> getByteData(
			@ApiParam(value = "Byte Data File", required = true) @RequestBody String userName) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			Boolean vaildateUserName = userUtils.vaildateUserName(userName);
			if (!vaildateUserName) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("validUsername"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			byte[] encodedValue = shipProfileService.getByteDataForUserProfile(userName);
			if (encodedValue == null) {

				statusResponseDTO.setStatus(env.getProperty("noUserProfile"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.noUserProfile"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			// else if(encodedValue == "101" ){
			//
			// statusResponseDTO.setStatus(env.getProperty("noFile"));
			// statusResponseDTO.setMessage(env.getProperty("shipprofile.noFile"));
			// return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO),
			// HttpStatus.PARTIAL_CONTENT);
			//
			// }
			else {

				statusResponseDTO.setFileArray(encodedValue);
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("succesFile"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/update/statusall", produces = { "application/json" })
	@ApiOperation(value = "Delete Ship Info", notes = "Delete Individual Ship Info")
	public ResponseEntity<String> updateShipStatusAll(
			@ApiParam(value = "Delete Ship  Info", required = true) @RequestBody UserDTO userDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			 String isVaildStatusUpateParam = userUtils.isVaildStatusUpateParam(userDTO);
	            if (!isVaildStatusUpateParam.equalsIgnoreCase("success")) {
	                statusResponseDTO.setStatus(env.getProperty("failure"));
	                statusResponseDTO.setMessage(isVaildStatusUpateParam);
	                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
	            }
	            String checkUserIdExists = userUtils.checkUserIdExists(userDTO);
				if (!checkUserIdExists.equalsIgnoreCase(env.getProperty("success"))) {
					 statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(checkUserIdExists);
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
				Boolean atd = userService.activatealluser(userDTO);

				if (atd) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage("All user status  updated succesfully");
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("Problem in acivate all users"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@ApiOperation(value = "LIST USERS BASED ORGANIZATION", notes = "List User based on organization, Fetch all commerical manager and ship master and technical manager")
	@GetMapping(value = "/list/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  getListShipMasterAndCommericalManagerAndTechMangerBasedOrganization(@ApiParam(value = "User ID is required")@PathVariable(required = true)Long userId ){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		boolean isUserExist = userService.isExistingUser(userId);
		if (!isUserExist){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("noUserProfile"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		UserDTO userDTO = userService.getListShipMasterAndCommericalManagerAndTechMangerBasedOrganization(userId);
		if(userDTO != null){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("user.list.succcess"));
			statusResponseDTO.setUserInfos(userDTO);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("user.list.not.found"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "LIST ALL USERS BASED ORGANIZATION", notes = "List All Users based on organization")
	@GetMapping(value = "/list/all/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  listAllUsersBasedOnOrganization(@ApiParam(value = "User ID is required")@PathVariable(required = true)Long userId ){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		boolean isUserExist = userService.isExistingUser(userId);
		if (!isUserExist){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("noUserProfile"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		List<UserDTO> userDTOs = userService.listAllUserBasedOnOrganization(userId);
		statusResponseDTO.setStatus(env.getProperty("success"));
		statusResponseDTO.setMessage(env.getProperty("user.list.succcess"));
		statusResponseDTO.setUserList(userDTOs);
		return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
	}

	@CrossOrigin
	@ApiOperation(value = "LIST SHIP MASTER USER BASED ORGANIZATION", notes = "List User based on organization, Fetch all ship master profiles")
	@GetMapping(value = "/shipmaster/list/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  listShipMasterUserBasedOnOrganization(@ApiParam(value = "User ID is required")@PathVariable(required = true)Long userId ){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		boolean isUserExist = userService.isExistingUser(userId);
		if (!isUserExist){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("noUserProfile"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		List<UserDTO> userDTOs = userService.listShipMasterUsersBasedOrganization(userId);
		if(userDTOs != null){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("ship.master.list.succcess"));
			statusResponseDTO.setShipMasterInfos(userDTOs);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("ship.master.list.not.found"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}


	@CrossOrigin
	@ApiOperation(value = "LIST COMMERCIAL MANAGER USER BASED ORGANIZATION", notes = "List User based on organization, Fetch all Commercial manager profiles")
	@GetMapping(value = "/commercialmanager/list/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  listCommercialManagerUserBasedOnOrganization(@ApiParam(value = "User ID is required")@PathVariable(required = true)Long userId ){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		boolean isUserExist = userService.isExistingUser(userId);
		if (!isUserExist){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("noUserProfile"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		List<UserDTO> userDTOs = userService.listCommercialManagerUsersBasedOrganization(userId);
		if(userDTOs != null){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("commerical.manager.list.succcess"));
			statusResponseDTO.setCommercialManagerInfos(userDTOs);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("commerical.manager.list.not.found"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "LIST TECHNICAL MANAGER USER BASED ORGANIZATION", notes = "List User based on organization, Fetch all Technical manager profiles")
	@GetMapping(value = "/techmanager/list/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  listTechnicalManagerUserBasedOnOrganization(@ApiParam(value = "User ID is required")@PathVariable(required = true)Long userId ){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		boolean isUserExist = userService.isExistingUser(userId);
		if (!isUserExist){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("noUserProfile"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		List<UserDTO> userDTOs = userService.listTechnicalManagerUsersBasedOrganization(userId);
		if(userDTOs != null){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("tech.manager.list.succcess"));
			statusResponseDTO.setTechnicalManagerInfos(userDTOs);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("tech.manager.list.not.found"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}


	@CrossOrigin
	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "UPDATE USER", notes = "Update User profile info all required information")
	public ResponseEntity<String> updateUserProfileInfo(
			@ApiParam(value = "User id name, passowrd, email, organization is required", required = true) @RequestParam(name = "userInfo", required = true) String userInfo,
			@RequestParam(name = "userProfilePicture", value = "userProfilePicture", required = false) MultipartFile userProfilePictureMultipartFile) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			UserDTO userDTO = objectMapper.readValue(userInfo, UserDTO.class);
			String updateUser = userService.updateUserProfileInfo(userDTO, userProfilePictureMultipartFile);
			if(!updateUser.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(updateUser);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			if(updateUser.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("user.update.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("user.update.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/profile/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "UPDATE ADMIN", notes = "Update Admin user profile information is required")
	public ResponseEntity<String> updateAdministratorProfileInfo(
			@ApiParam(value = "User name, passowrd, email", required = true) @RequestParam(name = "updateProfile", value = "updateProfile", required = true) String userInfo,
			@RequestParam(name = "profilePicture", value = "profilePicture", required = false) MultipartFile userProfilePictureMultipartFile) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			UserDTO userDTO = objectMapper.readValue(userInfo, UserDTO.class);
			if(userProfilePictureMultipartFile != null){
				if(!userProfilePictureMultipartFile.isEmpty()){
					if(!commonMethodsUtility.isImage(userProfilePictureMultipartFile)) {
						statusResponseDTO.setStatus(env.getProperty("failure"));
						statusResponseDTO.setMessage(env.getProperty("image.type.invalid"));
						return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
					}
				}
			}
			String updateAdmin = userService.updateAdminProfileInformation(userDTO, userProfilePictureMultipartFile);
			if(!updateAdmin.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(updateAdmin);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			if(updateAdmin.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("admin.update.success"));
				statusResponseDTO.setUserInfos(userService.getUserInformationDetails(userDTO.getUserId()));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("admin.update.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@DeleteMapping(value = "/admin/delete/{superAdminId}/{adminId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "DELETE ADMIN", notes = "Delete Admin user profile information is required")
	public ResponseEntity<String> deleteAdministratorProfileInfo(
			@ApiParam(value = "Admin and Super Admin ID required", required = true)@PathVariable Long superAdminId, @PathVariable Long adminId) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String deleteAdmin = userService.deleteAdministratorProfileInfo(superAdminId,adminId);
			if(!deleteAdmin.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(deleteAdmin);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			if(deleteAdmin.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("admin.delete.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("admin.delete.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/admin/password/reset", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "RESET ADMIN PASSWORD", notes = "This method is used to reset Administrator password")
	public ResponseEntity<String> resetAdministratorProfilePassword(
			@ApiParam(value = "Super Admin & Admin id and password required", required = true) @RequestBody UserDTO userDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String resetAdminPassword = userService.resetAdministratorProfilePassword(userDTO);
			if(!resetAdminPassword.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(resetAdminPassword);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			if(resetAdminPassword.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("admin.password.reset.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("admin.password.reset.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/password/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "UPDATE USER PASSWORD", notes = "This method is used to update User profile info password")
	public ResponseEntity<String> updateUserProfilePassword(
			@ApiParam(value = "User Id, current password and new password required", required = true) @RequestBody UserDTO userDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String resetAdminPassword = userService.updateUserProfilePassword(userDTO);
			if(!resetAdminPassword.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(resetAdminPassword);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			if(resetAdminPassword.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("password.updated.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("password.update.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/addGeoLocation", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "UPDATE USER PASSWORD", notes = "This method is used to add and update the Geo location")
	public ResponseEntity<String> addGeoLocation(
			@ApiParam(value = "latitude and new longitute required", required = true) @RequestBody GeoLocationDTO geoLocationDTO,
			HttpServletRequest request, HttpServletResponse response) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isValidLocation = userUtils.isValidLocation (geoLocationDTO);
			if (!isValidLocation){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			geoLocationDTO = userService.createGeoLocartion (geoLocationDTO);
			if (geoLocationDTO != null){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("geo.location.success"));
				statusResponseDTO.setGeoLocationDTO(geoLocationDTO);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else{
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("geo.location.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@ApiOperation(value = "LIST TECHNICAL MANAGER USER BASED ORGANIZATION", notes = "List User based on organization, Fetch all Technical manager profiles")
	@GetMapping(value = "/get/geoLocationlist/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  getGeoLocation(@ApiParam(value = "User ID is required")@PathVariable(required = true)Long userId ){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try{
			boolean isUserExist = userService.isExistingUser(userId);
			if (!isUserExist){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("noUserProfile"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			List<UserDTO> userDTOs = userService.getGeoLocations(userId);
			if (userDTOs != null && userDTOs.size() >0){
				statusResponseDTO.setStatus(env.getProperty("geo.location.list.success"));
			}else{
				statusResponseDTO.setStatus(env.getProperty("geo.location.list.success"));
			}
				statusResponseDTO.setUserList(userDTOs);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}
