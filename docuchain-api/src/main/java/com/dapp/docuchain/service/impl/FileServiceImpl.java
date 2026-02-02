package com.dapp.docuchain.service.impl;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.ShipProfileDTO;
import com.dapp.docuchain.model.Config;
import com.dapp.docuchain.model.ScanDelimiterInfo;
import com.dapp.docuchain.model.ScanFieldType;
import com.dapp.docuchain.repository.ConfigInfoRepository;
import com.dapp.docuchain.repository.ScanDelimiterRepository;
import com.dapp.docuchain.service.FileService;
import com.google.common.io.Files;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);
    @Autowired
    private Environment env;
    @Autowired
    private ScanDelimiterRepository scanDelimiterRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
	ConfigInfoRepository configInfoRepository;
    public static Date parseDate(String inputDate) {

        Date outputDate = null;
        String[] possibleDateFormats = {
                "yyyy.MM.dd G 'at' HH:mm:ss z",
                "EEE, MMM d, ''yy",
                "h:mm a",
                "hh 'o''clock' a, zzzz",
                "K:mm a, z",
                "yyyyy.MMMMM.dd GGG hh:mm aaa",
                "EEE, d MMM yyyy HH:mm:ss Z",
                "yyMMddHHmmssZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "YYYY-'W'ww-u",
                "EEE, dd MMM yyyy HH:mm:ss z",
                "EEE, dd MMM yyyy HH:mm zzzz",
                "yyyy-MM-dd'T'HH:mm:ssZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSSzzzz",
                "yyyy-MM-dd'T'HH:mm:sszzzz",
                "yyyy-MM-dd'T'HH:mm:ss z",
                "yyyy-MM-dd'T'HH:mm:ssz",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd'T'HHmmss.SSSz",
                "yyyyMMdd",
                "dd/MM/yy",
                "dd/MM/yyyy",
                "dd MMM yyyy",
                "dd-MM-yyyy"
        };

        try {

            outputDate = DateUtils.parseDate(inputDate, possibleDateFormats);
            LOGGER.info("inputDate ==> " + inputDate + ", outputDate ==> " + outputDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDate;
    }

    @Override
    public String fileRetriveFromStorej(String fileName, String fileHashCode) {

        String messageString = new String();
        String encodedBase64 = null;


        try {
        	String statUrl = env.getProperty("getURL") + URLEncoder.encode(fileName, "UTF-8");
            if (fileHashCode != null) {
                statUrl = statUrl + "/" + fileHashCode;
    		}

    		HttpClient client = new DefaultHttpClient();

            HttpGet request = new HttpGet(statUrl);
            HttpResponse response;
            response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                messageString += line;
            }
            // print result
            JSONObject myResponse = new JSONObject(messageString);
            // JSONObject fileObject=myResponse.getJSONObject("fileObjectDTO");
            encodedBase64 = myResponse.optString("fileArray");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return encodedBase64;

    }

    @Override
    public ExpiryDocumentDTO scanImageFile(MultipartFile file) {
        ITesseract instance = new Tesseract();
        ExpiryDocumentDTO expiryDocumentDTO = null;
        //instance.setLanguage("eng");
        //instance.setDatapath("D://Tessdata");
        //In case you don't have your own tessdata, let it also be extracted for you
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        //Set the tessdata path
        instance.setDatapath(tessDataFolder.getAbsolutePath());
        File convFile = null;

        try {
            convFile = new File(file.getOriginalFilename());
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            //File convFile = multipartToFile(file);
            String result = instance.doOCR(convFile);
            LOGGER.info("Result ::" + result);
            expiryDocumentDTO = scanDocInfo(result);
            return expiryDocumentDTO;
        } catch (TesseractException | IOException e) {
            System.err.println(e.getMessage());
            return expiryDocumentDTO;
        } finally {
            if (convFile.exists()) {
                convFile.delete();
            }
        }

    }

    private File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File convFile = new File(multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }

    @Override
    public String uploadFile(MultipartFile file, Long saveInBlockchain) {
        String message = "";
        if (!file.isEmpty()) {
            try {
                ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        LOGGER.info("Original File Name --->" + file.getOriginalFilename());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
                        String dateAsString = simpleDateFormat.format(new Date());
                        String fileExtn = Files.getFileExtension(file.getOriginalFilename());
                        String fileName = Files.getNameWithoutExtension(file.getOriginalFilename()).replaceAll("[-+.^:, ]", "");
                        LOGGER.info("File name --->" + fileName + dateAsString + "." + fileExtn);
                        if (fileName != null && fileExtn != null && !fileName.isEmpty() && !fileExtn.isEmpty()) {
                            return fileName + dateAsString + "." + fileExtn;
                        }
                        return file.getOriginalFilename();
                    }
                };
                LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                map.add("file", fileAsResource);
						System.out.println("Map Content: " + map);

                map.add("saveInBlockchain", saveInBlockchain);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
                String url = env.getProperty("postURL");

                ResponseEntity<String> response = restTemplate.exchange(url,
                        HttpMethod.POST,
                        entity,
                        String.class);
                if (response.getStatusCode().equals(HttpStatus.OK)) {
                    LOGGER.info("FileServiceImpl.uploadFile --->" + response);
                    message = response.getBody();
                    return message;
                }
            } catch (Exception e) {
                LOGGER.error("Failed to upload attachment", e);
                return env.getProperty("failure");
            }
        }
        return env.getProperty("failure");
    }



    private ExpiryDocumentDTO scanDocInfo(String scannedText) {
        ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
        ScanFieldType scanFieldTypes[] = ScanFieldType.values();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (ScanFieldType scanFieldType : scanFieldTypes) {
            LOGGER.info("scanFieldType = [" + scanFieldType.name() + "]");
            String match = "";
            Set<ScanDelimiterInfo> scanDelimiterInfos = scanDelimiterRepository.findByFieldType(scanFieldType.name());
            for (ScanDelimiterInfo scanDelimiterInfo : scanDelimiterInfos) {
                LOGGER.info("scanDelimiterInfo = [" + scanDelimiterInfo.getStartingPattern() + "]");
                LOGGER.info("scanDelimiterInfo = [" + scanDelimiterInfo.getEndingPattern() + "]");
                Matcher m = Pattern.compile(Pattern.quote(scanDelimiterInfo.getStartingPattern()) + "(.*?)" + Pattern.quote(scanDelimiterInfo.getEndingPattern()),Pattern.CASE_INSENSITIVE).matcher(scannedText); //certificate no
                while (m.find()) {
                    match = m.group(1);
                    LOGGER.info(">" + match.trim() + "<");
                    break;
                }
                if (match != null && !match.isEmpty()) {
                    break;
                }
            }
            if (scanFieldType.equals(ScanFieldType.CERT_NO)) {
                LOGGER.info("Certificate No = [" + match + "]");
                if (match != null && !match.isEmpty()) {
                    expiryDocumentDTO.setCertificateNumber(match.trim());
                }
            } else if (scanFieldType.equals(ScanFieldType.ISSUE_PLACE)) {
                LOGGER.info("Issue Place = [" + match + "]");
                if (match != null && !match.isEmpty()) {
                    expiryDocumentDTO.setPlaceOfIssue(match.trim());
                }
            } else if (scanFieldType.equals(ScanFieldType.ISSUE_DATE)) {
                LOGGER.info("Issue Date = [" + match + "]");
                Date date = null;
                if (match != null && !match.isEmpty()) {
                    date = parseDate(match.trim());
                }
                expiryDocumentDTO.setIssueDate(date);
                if(date!=null) {
                	expiryDocumentDTO.setIssueDateString(dateFormat.format(date));
                }
            } else if (scanFieldType.equals(ScanFieldType.EXPIRY_DATE)) {
                LOGGER.info("Expiry Date = [" + match + "]");
                Date date = null;
                if (match != null && !match.isEmpty()) {
                    date = parseDate(match.trim());
                }
                if(date!=null) {
                	expiryDocumentDTO.setExpiryDateString(dateFormat.format(date));
                }
            }
        }
        return expiryDocumentDTO;
    }
    @Override
	public boolean fileWriteIntoLocation(MultipartFile file, String oldPath, String newPath, String fileName) {
		// Check file Already Exist!
		if (oldPath != null) {
			oldPath = oldPath.replace("/", "\\");
			File oldFile = new File(oldPath);
			if (oldFile.exists()) {
				oldFile.delete();
			}
		}

		FileInputStream reader = null;
		FileOutputStream writer = null;

		try {
			// Create Folder Location
			newPath = newPath.replace("/", "\\");
			File createfolder = new File(newPath);
			if (!createfolder.exists()) {
				createfolder.mkdirs();
			}

			// Create File in Folder Location
			reader = (FileInputStream) file.getInputStream();
			byte[] buffer = new byte[1000];
			File outputFile = new File(newPath + fileName);
			outputFile.createNewFile();
			writer = new FileOutputStream(outputFile);
			while ((reader.read(buffer)) != -1) {
				writer.write(buffer);
			}
			LOGGER.info("Created file in the Path=" + newPath + fileName);
			return true;
		} catch (Exception e) {
			LOGGER.info("Failed to create file in the  Path= " + newPath + fileName);
			return false;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				LOGGER.info("Failed to create file in the Path= " + newPath + fileName);
				return false;
			}
		}
	}

	@Override
	public String shipProfileUpload(ShipProfileDTO shipProfileDTO, MultipartFile shipPic) {
		FileInputStream reader = null;
		FileOutputStream writer = null;
		String path = null;
		String dbPath = null;
		LOGGER.info("In  userDocumentUpload : start");
		try {
			Config configInfo = configInfoRepository.findConfigByConfigKey("docuchainfileslocation");
			String basePath = configInfo.getConfigValue();
			LOGGER.info(" Base path from config table : " + basePath);
			String uploadingdir = basePath + File.separator + env.getProperty("ship.profilepic.location");
			LOGGER.info(" uploadingdir : " + uploadingdir);
			File file = new File(uploadingdir);
			if (!file.exists()) {
				LOGGER.info(" In mkdir : " + uploadingdir);
				file.mkdirs();
			}
			LOGGER.info(" uploadingdir : " + uploadingdir);

			String fileType = Files.getFileExtension(shipPic.getOriginalFilename());
			path = uploadingdir + File.separator + shipProfileDTO.getImo() + "." + fileType;
			dbPath = File.separator + env.getProperty("ship.profilepic.location")
					+ File.separator + shipProfileDTO.getImo() + "." + fileType;
			LOGGER.info(" file path : " + path);
			LOGGER.info("dbPath : " + dbPath);
			byte[] buffer = new byte[1000];
			File outputFile = new File(path);

			int totalBytes = 0;
			if(outputFile.exists()){
				outputFile.delete();
				outputFile.createNewFile();
			}
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			reader = (FileInputStream) shipPic.getInputStream();
			writer = new FileOutputStream(outputFile);

			int bytesRead = 0;
			while ((bytesRead = reader.read(buffer)) != -1) {
				writer.write(buffer);
				totalBytes += bytesRead;
			}
			dbPath = dbPath.replace(File.separator, "/");
		} catch (IOException e) {
			path = null;
			dbPath = null;
			LOGGER.error("Problem in userDocumentUpload file path : " + path);
			e.printStackTrace();
		} finally {
			try {
				if(reader != null)
					reader.close();
				if(writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("In  userDocumentUpload : end : dbPath : " + dbPath);
		return dbPath;
	}

	@Override
	public String uploadUserPictureAndSaveFileFromLocal(MultipartFile multipartFile, String fileName) {

		FileInputStream reader = null;
		FileOutputStream writer = null;
		String path = null;
		String dbPath = null;
		LOGGER.info("In  userDocumentUpload : start");
		try {
			Config configInfo = configInfoRepository.findConfigByConfigKey("docuchainfileslocation");
			String basePath = configInfo.getConfigValue();
			LOGGER.info(" Base path from config table : " + basePath);
			String uploadingdir = basePath + File.separator + env.getProperty("user.profile.picture.location");
			LOGGER.info(" uploadingdir : " + uploadingdir);
			File file = new File(uploadingdir);
			if (!file.exists()) {
				LOGGER.info(" In mkdir : " + uploadingdir);
				file.mkdirs();
			}
			LOGGER.info(" uploadingdir : " + uploadingdir);

			String fileType = Files.getFileExtension(multipartFile.getOriginalFilename());
			path = uploadingdir + File.separator + fileName.trim() + "." + fileType;
			dbPath = File.separator + env.getProperty("user.profile.picture.location") + File.separator + fileName.trim() + "." + fileType;
			LOGGER.info(" file path : " + path);
			LOGGER.info("dbPath : " + dbPath);
			byte[] buffer = new byte[1000];
			File outputFile = new File(path);

			int totalBytes = 0;
			if(outputFile.exists()){
				outputFile.delete();
				outputFile.createNewFile();
			}
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			reader = (FileInputStream) multipartFile.getInputStream();
			writer = new FileOutputStream(outputFile);

            int bytesRead = 0;
			while ((bytesRead = reader.read(buffer)) != -1) {
				writer.write(buffer);
				totalBytes += bytesRead;
			}
			dbPath = dbPath.replace(File.separator, "/");
		} catch (IOException e) {
			path = null;
			dbPath = null;
			LOGGER.error("Problem in userDocumentUpload file path : " + path);
			e.printStackTrace();
		} finally {
			try {
				if(reader != null)
					reader.close();
				if(writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("In  userDocumentUpload : end : dbPath : " + dbPath);
		return dbPath;
	}

	@Override
	public String organizationImageUpload(Long userId, MultipartFile shipPic) {
		FileInputStream reader = null;
		FileOutputStream writer = null;
		String path = null;
		String dbPath = null;
		LOGGER.info("In  userDocumentUpload : start");
		try {
			Config configInfo = configInfoRepository.findConfigByConfigKey("docuchainfileslocation");
			String basePath = configInfo.getConfigValue();
			LOGGER.info(" Base path from config table : " + basePath);
			String uploadingdir = basePath + File.separator + env.getProperty("logo.pic.path");
			LOGGER.info(" uploadingdir : " + uploadingdir);
			File file = new File(uploadingdir);
			if (!file.exists()) {
				LOGGER.info(" In mkdir : " + uploadingdir);
				file.mkdirs();
			}
			LOGGER.info(" uploadingdir : " + uploadingdir);

			String fileType = Files.getFileExtension(shipPic.getOriginalFilename());
			path = uploadingdir + File.separator + userId + "." + fileType;
			dbPath = File.separator + env.getProperty("organization.profilepic.location")
					+ File.separator + userId + "." + fileType;
			LOGGER.info(" file path : " + path);
			LOGGER.info("dbPath : " + dbPath);
			byte[] buffer = new byte[1000];
			File outputFile = new File(path);

			int totalBytes = 0;
			if(outputFile.exists()){
				outputFile.delete();
				outputFile.createNewFile();
			}
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			reader = (FileInputStream) shipPic.getInputStream();
			writer = new FileOutputStream(outputFile);

			int bytesRead = 0;
			while ((bytesRead = reader.read(buffer)) != -1) {
				writer.write(buffer);
				totalBytes += bytesRead;
			}
			dbPath = dbPath.replace(File.separator, "/");
		} catch (IOException e) {
			path = null;
			dbPath = null;
			LOGGER.error("Problem in userDocumentUpload file path : " + path);
			e.printStackTrace();
		} finally {
			try {
				if(reader != null)
					reader.close();
				if(writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("In  userDocumentUpload : end : dbPath : " + dbPath);
		return dbPath;
	}
}
