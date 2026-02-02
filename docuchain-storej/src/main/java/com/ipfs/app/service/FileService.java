package com.ipfs.app.service;


import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import org.web3j.crypto.CipherException;

import com.ipfs.app.dto.FileObjectDTO;
import com.ipfs.app.solidity.SimpleStorage;

public interface FileService {
	
	public FileObjectDTO saveFile(MultipartFile file, Boolean saveInBlockchain) throws CipherException;
	
	public SimpleStorage loadSolidity() throws IOException, CipherException;
	
	public FileObjectDTO getFile(String fileName, String fileHashCode)throws Exception;

}
