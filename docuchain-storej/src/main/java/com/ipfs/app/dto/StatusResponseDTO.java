package com.ipfs.app.dto;

public class StatusResponseDTO {
	
	private String status;
	private String message;
	private FileObjectDTO fileObjectDTO;
	private String fileArray;
	
	
	
	public String getFileArray() {
		return fileArray;
	}
	public void setFileArray(String fileArray) {
		this.fileArray = fileArray;
	}
	//	public byte[] getFileArray() {
//		return fileArray;
//	}
//	public void setFileArray(byte[] fileArray) {
//		this.fileArray = fileArray;
//	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public FileObjectDTO getFileObjectDTO() {
		return fileObjectDTO;
	}
	public void setFileObjectDTO(FileObjectDTO fileObjectDTO) {
		this.fileObjectDTO = fileObjectDTO;
	}
	

}
