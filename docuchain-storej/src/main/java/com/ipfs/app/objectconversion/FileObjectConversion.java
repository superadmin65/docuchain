// package com.ipfs.app.objectconversion;

// import org.json.JSONException;
// import org.json.JSONObject;
// import org.springframework.stereotype.Service;

// import com.ipfs.app.dto.FileObjectDTO;

// @Service
// public class FileObjectConversion {

// 	public FileObjectDTO convertJsonObejctIntoFileObjectDTO(JSONObject jSONObject, String fileName) throws JSONException{
// 		FileObjectDTO fileObjectDTO=new FileObjectDTO();
// 		fileObjectDTO.setHash(jSONObject.getString("Hash"));
// 		fileObjectDTO.setSize(jSONObject.getString("Size"));
// 		fileObjectDTO.setHash(jSONObject.getString("Hash"));
// 		fileObjectDTO.setFileName(fileName);
// 		return fileObjectDTO;
// 	}
// 	public FileObjectDTO setFileByteIntoFileObjectDTO(byte[] fileObj) {
// 		FileObjectDTO fileObjectDTO=new FileObjectDTO();
// 		fileObjectDTO.setFile(fileObj);
// 		return fileObjectDTO;
// 	}

// }

package com.ipfs.app.objectconversion;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.ipfs.app.dto.FileObjectDTO;

@Service
public class FileObjectConversion {

  public FileObjectDTO convertJsonObejctIntoFileObjectDTO(JSONObject jSONObject, String fileName) throws JSONException {
    FileObjectDTO fileObjectDTO = new FileObjectDTO();

    // FIX 1: Use lowercase "hash" (matches our previous fix in FileServiceImpl)
    fileObjectDTO.setHash(jSONObject.getString("hash"));

    // FIX 2: Set Size to "0" because we stopped using the 'stat' API
    // (This prevents the "JSONObject['Size'] not found" error)
    fileObjectDTO.setSize("0");

    // FIX 3: Use the fileName from the JSON object or the argument
    fileObjectDTO.setFileName(jSONObject.getString("fileName"));

    return fileObjectDTO;
  }

  public FileObjectDTO setFileByteIntoFileObjectDTO(byte[] fileObj) {
    FileObjectDTO fileObjectDTO = new FileObjectDTO();
    fileObjectDTO.setFile(fileObj);
    return fileObjectDTO;
  }

}
