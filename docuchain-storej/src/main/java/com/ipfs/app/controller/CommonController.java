package com.ipfs.app.controller;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;
import com.ipfs.app.dto.FileObjectDTO;
import com.ipfs.app.dto.StatusResponseDTO;
import com.ipfs.app.service.FileService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class CommonController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	FileService fileService;

	
	  @PostMapping(value = "/add/file",produces = {"application/json"})
	  public ResponseEntity<String> addFile(@RequestParam(name = "file", value = "file", required = true) MultipartFile file,
	  	@RequestParam(name = "saveInBlockchain", value = "saveInBlockchain") Long saveInBlockchain){
		  StatusResponseDTO statusResponseDTO = new StatusResponseDTO(); 
		  statusResponseDTO.setStatus("Failure");
		  try{
		  if(file.isEmpty()){
			  statusResponseDTO.setMessage("File does not exists");
			  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
		  }
		  FileObjectDTO fileObjectDTO=fileService.saveFile(file, Long.compare(saveInBlockchain, Long.decode("1")) == 0);
		  if(fileObjectDTO!=null){
			  statusResponseDTO.setStatus("Success");
			  statusResponseDTO.setMessage("File uploaded successfully");
			  statusResponseDTO.setFileObjectDTO(fileObjectDTO);
			  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		  }
		  else{
			  statusResponseDTO.setMessage("File Upload failed");
			  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		  }
		  }
		  catch(Exception e){
			  e.printStackTrace();
			  LOG.error("Error in addFile method");
			  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		  }
	  }
	  
	  @GetMapping("/get/file/{filename}")
	  public ResponseEntity<String> getFile(@PathVariable(value = "filename") String filename) throws IOException {
		  StatusResponseDTO statusResponseDTO = new StatusResponseDTO(); 
		  statusResponseDTO.setStatus("Failure");
		  try {
			  	if(filename== null){
			  		statusResponseDTO.setMessage("File name not exists");
			  	  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
			  	}
				FileObjectDTO fileObjectDTO=fileService.getFile(filename, null);
				  if(fileObjectDTO!=null){
					  statusResponseDTO.setStatus("Success");
					  statusResponseDTO.setMessage("File reterived successfully");
					  String encodedBase64 = new String(Base64.encodeBase64(fileObjectDTO.getFile()));
					  statusResponseDTO.setFileArray(encodedBase64);
					  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
				  }
				  else{
					  statusResponseDTO.setMessage("Failed to reterive the file");
					  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				  }
				
			} catch (Exception e) {
				  e.printStackTrace();
				  LOG.error("Error in getFile method");
				  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
	  
	  }

	  @GetMapping("/get/file/{filename}/{fileHashCode}")
	  public ResponseEntity<String> getFileWithHashCode(
		  @PathVariable(value = "filename") String filename,
	  	@PathVariable(value = "fileHashCode") String fileHashCode
	  ) throws IOException {
		  StatusResponseDTO statusResponseDTO = new StatusResponseDTO(); 
		  statusResponseDTO.setStatus("Failure");
		  try {
			  	if(filename== null){
			  		statusResponseDTO.setMessage("File name not exists");
			  	  	return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
			  	}
				FileObjectDTO fileObjectDTO=fileService.getFile(filename, fileHashCode);
				  if(fileObjectDTO!=null){
					  statusResponseDTO.setStatus("Success");
					  statusResponseDTO.setMessage("File reterived successfully");
					  String encodedBase64 = new String(Base64.encodeBase64(fileObjectDTO.getFile()));
					  statusResponseDTO.setFileArray(encodedBase64);
					  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
				  }
				  else{
					  statusResponseDTO.setMessage("Failed to reterive the file");
					  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				  }
				
			} catch (Exception e) {
				  e.printStackTrace();
				  LOG.error("Error in getFile method");
				  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
	  
	  }

}
