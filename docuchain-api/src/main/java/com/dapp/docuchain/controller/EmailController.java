package com.dapp.docuchain.controller;

import com.dapp.docuchain.service.EmailService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/docuchain/api/email")
@Api(value = "EmailController", description = "Email Controller API")
@CrossOrigin
public class EmailController {

    private static final Logger LOG = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    EmailService emailService;
    @Autowired
    private Environment env;
	
	
/*	@CrossOrigin
	@PostMapping(value = "/send", produces = {
			"application/json" })
	@ApiOperation(value = "Send email", notes = "Send email with attachment to user")
	public ResponseEntity<String> sendEmailWithAttachment(
			@ApiParam(value = "Required email id", required = true) @RequestBody EmailDTO emailDTO,
			HttpServletRequest request, HttpServletResponse response) {
		StatusResponseDTO statusResponceDTO = new StatusResponseDTO();
			statusResponceDTO.setStatus(env.getProperty("failure"));
			try{
				if( emailDTO.getEmailIds()==null && StringUtils.isNotBlank(emailDTO.getEmailIds())){
					 return new ResponseEntity<String>(new Gson().toJson(statusResponceDTO), HttpStatus.CONFLICT);
				}
				String mailResponse=emailService.SendEmailWithAttachment(emailDTO);
				if(mailResponse.equalsIgnoreCase(env.getProperty("success"))){
					statusResponceDTO.setStatus(env.getProperty("success"));
					statusResponceDTO.setMessage("Email sent successfully");
					return new ResponseEntity<String>(new Gson().toJson(statusResponceDTO), HttpStatus.OK);
				}
				statusResponceDTO.setMessage("Failed to send Email");
				return new ResponseEntity<String>(new Gson().toJson(statusResponceDTO), HttpStatus.PARTIAL_CONTENT);
			}
			
			catch (Exception e) {
				LOG.error("EmailController"+e);
				e.printStackTrace();
				return new ResponseEntity<String>(new Gson().toJson(statusResponceDTO), HttpStatus.PARTIAL_CONTENT);
			}
	}*/


}
