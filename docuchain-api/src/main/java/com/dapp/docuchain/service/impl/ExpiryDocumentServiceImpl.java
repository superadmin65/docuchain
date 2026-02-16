package com.dapp.docuchain.service.impl;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.model.*;
import com.dapp.docuchain.repository.*;
import com.dapp.docuchain.service.EmailService;
import com.dapp.docuchain.service.ExpiryDocumentService;
import com.dapp.docuchain.service.FileService;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.ExpiryDocumentUtility;
import com.dapp.docuchain.utility.NotificationUtility;
import com.google.common.io.Files;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ExpiryDocumentServiceImpl implements ExpiryDocumentService{

private static final Logger LOG = LoggerFactory.getLogger(ExpiryDocumentServiceImpl.class);

	@Autowired
	private Environment env;

	@Autowired
	FileService fileService;

	@Autowired
	ExpiryDocumentUtility expiryDocumentUtility;

	@Autowired
	DocumentDataRepository documentDataRepository;

	@Autowired
	ShipProfileRepository shipProfileRepository;

	@Autowired
	DocumentHolderRepository documentHolderRepository;

	@Autowired
	UserProfileRepository userProfileRepository;

	@Autowired
	ExpiryDocumentRepository expiryDocumentRepository;

	@Autowired
	private NotificationUtility notificationUtility;

	@Autowired
	private CommonMethodsUtility commonMethodsUtility;

	@Autowired
	DualApprovalRepository dualApprovalRepository;

	@Autowired
	RoleInfoRepository roleInfoRepository;

	@Autowired
	OrganizationInfoRepository organizationInfoRepository;

	@Autowired
	EmailService emailService;

	@Override
	public String saveDocumentDetails(ExpiryDocumentDTO expiryDocumentDTO, MultipartFile scanFile) {
		String fileExtn=Files.getFileExtension(scanFile.getOriginalFilename());
		try {
		JSONObject myResponse=null;
		System.out.println("File Extension: "+fileExtn);
			String fileName = Files.getNameWithoutExtension(scanFile.getOriginalFilename()).replaceAll("[-+.^:, ]", "");
			expiryDocumentDTO.setDocumentName(fileName);
			UserProfileInfo userProfileInfo = null;
			Long saveInBlockchain = null;
			if (expiryDocumentDTO.getUploadedUserId() != null) {
				userProfileInfo = userProfileRepository.findOne(expiryDocumentDTO.getUploadedUserId());
				SaveInBlockhainInfo saveInBlockhainInfo = userProfileInfo.getOrganizationInfo().getSaveInBlockhainInfo();
				if (saveInBlockhainInfo != null){
					saveInBlockchain = saveInBlockhainInfo.getIsActive();
				}
			}
			String fileResponse=fileService.uploadFile(scanFile, saveInBlockchain);

		if(!fileResponse.equalsIgnoreCase(env.getProperty("failure"))){
			LOG.info("File Response: " + fileResponse);
			myResponse = new JSONObject(fileResponse);
			if(myResponse!=null){
				DocumentDataInfo documentDataInfo=expiryDocumentUtility.convertJSONObjectToDocumentDataInfo(myResponse,fileExtn);
				documentDataInfo=documentDataRepository.save(documentDataInfo);
				if(documentDataInfo.getId()!=null){
					ExpiryDocumentInfo expiryDocumentInfo=expiryDocumentUtility.convertExpiryDocumentDTOToObject(expiryDocumentDTO,documentDataInfo);
					ShipProfileInfo shipProfileInfo=shipProfileRepository.findById(expiryDocumentDTO.getShipProfileId());
					DocumentHolderInfo documentHolderInfo=documentHolderRepository.findOne(expiryDocumentDTO.getDocumentHolderId());
					if(userProfileInfo!=null){
						expiryDocumentInfo.setUploadedBy(userProfileInfo);
					}
					if(shipProfileInfo!=null && documentHolderInfo!=null){
						expiryDocumentInfo.setShipProfileInfo(shipProfileInfo);
						expiryDocumentInfo.setDocumentHolderInfo(documentHolderInfo);
					}
//					created by Velkumar
					DualApprovalInfo dualApprovalInfo=dualApprovalRepository.findByOrganizationInfo(shipProfileInfo.getShipOrganizationInfo());
					if(dualApprovalInfo!=null){
						if(dualApprovalInfo.getFlag()==1){
							List<ExpiryDocumentInfo> expiryDocument=expiryDocumentRepository.findByDocumentHolderInfoAndShipProfileInfoAndCurrentVersion(documentHolderInfo, shipProfileInfo, 1);
							for(ExpiryDocumentInfo expDocument:expiryDocument){
								expDocument.setCurrentVersion(0);
								expiryDocumentRepository.saveAndFlush(expDocument);
							}
							expiryDocumentInfo.setCurrentVersion(1);
							expiryDocumentInfo.setDocumentStatus(env.getProperty("document.status.approve"));
						}
					}

					expiryDocumentInfo = expiryDocumentRepository.saveAndFlush(expiryDocumentInfo);
					if(expiryDocumentInfo!=null){
	                    commonMethodsUtility.maintainHistory(expiryDocumentInfo.getId(),expiryDocumentInfo.getDocumentName(),"ExpiryDocument", env.getProperty("history.created"), expiryDocumentDTO.getLoginId());
	                    }
//					if (dualApprovalInfo.getFlag() == 0) {
						notificationUtility.notifyDocumentUpload(userProfileInfo.getId(), expiryDocumentInfo);
//					}
				if(expiryDocumentInfo.getId()!=null){
						List<ExpiryDocumentInfo> expiryDocumentInfoList=expiryDocumentRepository.findByDocumentHolderInfoAndShipProfileInfoAndDocumentStatus(documentHolderInfo, shipProfileInfo, env.getProperty("document.status.pending"));
						for(ExpiryDocumentInfo expiryDocumentInfoObj:expiryDocumentInfoList){
							if(!expiryDocumentInfoObj.equals(expiryDocumentInfo)){
								if(expiryDocumentInfoObj.getDocumentStatus().equalsIgnoreCase(env.getProperty("document.status.pending"))){
									expiryDocumentInfoObj.setStatus(env.getProperty("inactive"));;
									expiryDocumentInfoObj=expiryDocumentRepository.save(expiryDocumentInfoObj);
								}
							}
						}
//						//boolean documentNotification = notificationUtility.documentUserNotification(expiryDocumentDTO.getUploadedUserId(), expiryDocumentInfo);
//						if (!documentNotification){
//							return env.getProperty("failure");
//						}
						return env.getProperty("success");
					}
				}
			}
		}
		} catch (JSONException e) {
			e.printStackTrace();
			LOG.error("Error in ExpiryDocumentServiceImpl while saveDcoumentData ",e);
			return env.getProperty("document.upload.failed");
		}
		return env.getProperty("document.upload.failed");
	}

	@Override
	public List<ExpiryDocumentDTO> getAllExpiryDocumentAndHolderInfo(Long shipId, Integer archivedStatus) {

		List<ExpiryDocumentDTO> expiryDocumentDTOs=new ArrayList<ExpiryDocumentDTO>();
		List<ExpiryDocumentDTO> expirAvailableDocument=new ArrayList<ExpiryDocumentDTO>();
		List<ExpiryDocumentDTO> expiryEmptyDocument=new ArrayList<ExpiryDocumentDTO>();
//		List<DocumentHolderInfo> documentHolderInfoList=documentHolderRepository.findByDocumentHolderTypeOrderByDocumentHolderName(env.getProperty("document.holder.type"));
//		for(DocumentHolderInfo documentHolderInfo:documentHolderInfoList ){
//			ExpiryDocumentDTO expiryDocumentDTO=expiryDocumentUtility.convertExpiryDocumentInfoToExpiryDocumentDTO(documentHolderInfo,shipId,archivedStatus);
//			if(expiryDocumentDTO.getId() != null)
//				expirAvailableDocument.add(expiryDocumentDTO);
//			if(expiryDocumentDTO.getId() == null)
//				expiryEmptyDocument.add(expiryDocumentDTO);
//		}
		expiryDocumentDTOs = expiryDocumentUtility.convertAvailbleDocumentAndEmptyDocument(expirAvailableDocument,expiryEmptyDocument);
		ShipProfileInfo shipProfileInfo=shipProfileRepository.findById(shipId);
		//Set<DocumentHolderInfo>customDocumentHolders=shipProfileInfo.getCustomDocumentHolders();
		Set<DocumentHolderInfo>customDocumentHolders = documentHolderRepository.findByVesselId(shipProfileInfo.getId());
		System.out.println("size ::::"+customDocumentHolders.size());
		if(customDocumentHolders!=null && !customDocumentHolders.isEmpty()){
			for(DocumentHolderInfo customFolderInfo:customDocumentHolders){
				customFolderInfo = documentHolderRepository.findOne(customFolderInfo.getId());
				ExpiryDocumentDTO expiryDocumentDTO=expiryDocumentUtility.convertExpiryDocumentInfoToExpiryDocumentDTO(customFolderInfo,shipId,archivedStatus);
				if(expiryDocumentDTO.getId() != null)
					expirAvailableDocument.add(expiryDocumentDTO);
				if(expiryDocumentDTO.getId() == null)
					expiryEmptyDocument.add(expiryDocumentDTO);

			}
		}
		expiryDocumentDTOs.addAll(expirAvailableDocument);
		expiryDocumentDTOs.addAll(expiryEmptyDocument);
		return expiryDocumentDTOs;
	}

	@Override
	public ExpiryDocumentDTO downloadFileBasedOnExpiryId(Long id) {
		ExpiryDocumentInfo expiryDocumentInfo=expiryDocumentRepository.findOne(id);
		ExpiryDocumentDTO expiryDocumentDTO=new ExpiryDocumentDTO();


		if (expiryDocumentInfo != null) {
			String documentName = expiryDocumentInfo.getDocumentDataInfo().getDocumentName();
			String documentHashCode = expiryDocumentInfo.getDocumentDataInfo().getDocumentHashCode();

			String fileResponse = fileService.fileRetriveFromStorej(documentName, documentHashCode);
			byte[] decodedBytes = Base64.decodeBase64(fileResponse);
			expiryDocumentDTO.setFileArray(decodedBytes);
			expiryDocumentDTO.setDocumentName(expiryDocumentInfo.getDocumentName());
			expiryDocumentDTO.setFileExtension(expiryDocumentInfo.getDocumentDataInfo().getDocumentFormat());
			return expiryDocumentDTO;
		}

		return null;
	}

	@Override
	public List<ExpiryDocumentDTO> getDocumentHolderHistory(ExpiryDocumentDTO expiryDocumentDTO) {
		List<ExpiryDocumentDTO> expiryDocumentDTOs=new ArrayList<ExpiryDocumentDTO>();
		DocumentHolderInfo documentHolderInfo=documentHolderRepository.findOne(expiryDocumentDTO.getDocumentHolderId());
		ShipProfileInfo shipProfileInfo=shipProfileRepository.findById(expiryDocumentDTO.getShipProfileId());
		int cou = 1;
		if(documentHolderInfo!=null&& shipProfileInfo!=null){
			List<ExpiryDocumentInfo>  expiryDocumentInfoList=expiryDocumentRepository.findByDocumentHolderInfoAndShipProfileInfoAndArchiveStatusAndStatus(documentHolderInfo, shipProfileInfo,0,env.getProperty("active"));
			for(ExpiryDocumentInfo expiryDocumentInfo:expiryDocumentInfoList){
				ExpiryDocumentDTO expiryDocumentDTOInfo=expiryDocumentUtility.convertExpiryDocumentToDTO(expiryDocumentInfo);
				expiryDocumentDTOInfo.setCurrentVersionString("V" + cou);
				expiryDocumentDTOs.add(expiryDocumentDTOInfo);
				cou ++;
			}
		}
		return expiryDocumentDTOs;
	}

	@Override
	public String archiveDocumentHolderInfo(ExpiryDocumentDTO expiryDocumentDTO) {
		DocumentHolderInfo documentHolderInfo=documentHolderRepository.findOne(expiryDocumentDTO.getDocumentHolderId());
		ShipProfileInfo shipProfileInfo=shipProfileRepository.findById(expiryDocumentDTO.getShipProfileId());
		Integer count = 0;
		if(documentHolderInfo!=null&& shipProfileInfo!=null){
			List<ExpiryDocumentInfo>  expiryDocumentInfoList=expiryDocumentRepository.findByDocumentHolderInfoAndShipProfileInfo(documentHolderInfo, shipProfileInfo);
			for(ExpiryDocumentInfo expiryDocumentInfo:expiryDocumentInfoList){
				if(expiryDocumentDTO.getArchivedStatus().equals(1)){
					expiryDocumentInfo.setArchiveStatus(1);
					expiryDocumentInfo=expiryDocumentRepository.save(expiryDocumentInfo);
					count=count+1;
				}
			}
			if(expiryDocumentInfoList.size()==count){
				return env.getProperty("archive.document.success");
			}
		}
		return env.getProperty("failure");

	}

	@Override
	public String unArchiveDocumentHolderInfo(ExpiryDocumentDTO expiryDocumentDTO) {
		DocumentHolderInfo documentHolderInfo=documentHolderRepository.findOne(expiryDocumentDTO.getDocumentHolderId());
		ShipProfileInfo shipProfileInfo=shipProfileRepository.findById(expiryDocumentDTO.getShipProfileId());
		Integer count = 0;
		if(documentHolderInfo!=null&& shipProfileInfo!=null){
			List<ExpiryDocumentInfo>  expiryDocumentInfoListUnarchive=expiryDocumentRepository.findByDocumentHolderInfoAndShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatus(documentHolderInfo, shipProfileInfo, 1, env.getProperty("document.status.approve"),0,env.getProperty("active"));
			List<ExpiryDocumentInfo> archivedList=expiryDocumentRepository.findByDocumentHolderInfoAndShipProfileInfoAndArchiveStatusAndStatus(documentHolderInfo, shipProfileInfo, 1,env.getProperty("active"));
				for(ExpiryDocumentInfo expiryDocumentInfo:archivedList){
					if(expiryDocumentInfoListUnarchive.isEmpty()){
						expiryDocumentInfo.setArchiveStatus(0);
					}
					else{
						expiryDocumentInfo.setArchiveStatus(0);
						expiryDocumentInfo.setCurrentVersion(0);
					}
					expiryDocumentInfo=expiryDocumentRepository.save(expiryDocumentInfo);
					count=count+1;

				}
				if(archivedList.size()==count){
					return env.getProperty("unarchive.document.success");
				}
		}
		return env.getProperty("failure");
	}

	@Override
	public String archiveAndUnArchiveDocumentHolderInfo(ExpiryDocumentDTO expiryDocumentDTO) {
		String response = null;
		if(expiryDocumentDTO.getArchivedStatus().equals(1)){
			response=archiveDocumentHolderInfo(expiryDocumentDTO);
			return response;
		}
		if(expiryDocumentDTO.getArchivedStatus().equals(0)){
			response=unArchiveDocumentHolderInfo(expiryDocumentDTO);
			return response;
		}
		return env.getProperty("failure");
	}

	@Override
	public String checkExpiryDocumentsExists(ExpiryDocumentDTO expiryDocumentDTO) {
		DocumentHolderInfo documentHolderInfo=documentHolderRepository.findOne(expiryDocumentDTO.getDocumentHolderId());
		ShipProfileInfo shipProfileInfo=shipProfileRepository.findById(expiryDocumentDTO.getShipProfileId());
		if(documentHolderInfo!=null&& shipProfileInfo!=null){
			List<ExpiryDocumentInfo>  expiryDocumentInfoListUnarchive=expiryDocumentRepository.findByDocumentHolderInfoAndShipProfileInfo(documentHolderInfo, shipProfileInfo);
			if(expiryDocumentInfoListUnarchive.isEmpty()){
				return env.getProperty("document.not.exists");
			}

		}
		return env.getProperty("success");

	}

	@Override
	public String checkExpiryDocumentIdExits(ExpiryDocumentDTO expiryDocumentDTO) {
		ExpiryDocumentInfo  expiryDocumentInfo=expiryDocumentRepository.findOne(expiryDocumentDTO.getId());
		if(expiryDocumentInfo==null){
			return env.getProperty("expiry.document.not.exists");
		}
		return env.getProperty("success");
	}

	@Override
	public String shareExpiryDocument(ExpiryDocumentDTO expiryDocumentDTO) {
		ExpiryDocumentInfo  expiryDocumentInfo=expiryDocumentRepository.findById(expiryDocumentDTO.getId());
		if(expiryDocumentInfo!=null){
			expiryDocumentDTO.setDocumentName(expiryDocumentInfo.getDocumentName());
			expiryDocumentDTO.setFileExtension(expiryDocumentInfo.getDocumentDataInfo().getDocumentFormat());
			String emailResponse=emailService.SendEmailWithAttachment(expiryDocumentDTO);
			if(emailResponse.equalsIgnoreCase(env.getProperty("success"))){
				return env.getProperty("success");
			}
		}
		return env.getProperty("faliure");
	}

	@Override
	public String checkCustomFolderNameAlreadyExists(ExpiryDocumentDTO expiryDocumentDTO) {
		ShipProfileInfo shipProfileInfo=shipProfileRepository.findById(expiryDocumentDTO.getShipProfileId());
		Integer count=0;
		if(shipProfileInfo!=null){
			Set<DocumentHolderInfo>customDocumentHolderList=shipProfileInfo.getCustomDocumentHolders();
			if(customDocumentHolderList!=null){
				for(DocumentHolderInfo customHolderInfo:customDocumentHolderList){
					if(customHolderInfo.getDocumentHolderName().equalsIgnoreCase(expiryDocumentDTO.getCustomFolderName())){
						count=count+1;
					}
				}
			}
		}
		if(count>0){
			return env.getProperty("folder.name.exists");
		}
		else{
			return env.getProperty("success");
		}
	}

	@Override
	public String saveCustomDocumentFolder(ExpiryDocumentDTO expiryDocumentDTO) {
		ShipProfileInfo shipProfileInfo=shipProfileRepository.findById(expiryDocumentDTO.getShipProfileId());
		DocumentHolderInfo documentHolderInfo=expiryDocumentUtility.convertExpiryDocumentToCustomDocumentHolderObject(expiryDocumentDTO);
		documentHolderInfo=documentHolderRepository.saveAndFlush(documentHolderInfo);
		if(documentHolderInfo.getId()!=null){
			if(shipProfileInfo!=null){
				shipProfileInfo.getCustomDocumentHolders().add(documentHolderInfo);
				shipProfileInfo=shipProfileRepository.save(shipProfileInfo);
				if(shipProfileInfo!=null){
	                commonMethodsUtility.maintainHistory(documentHolderInfo.getId(),documentHolderInfo.getDocumentHolderName(),"Document", env.getProperty("history.created"), expiryDocumentDTO.getLoginId());
	                }
				return env.getProperty("success");
			}
		}
		return env.getProperty("faliure");
	}

	@Override
	public List<ExpiryDocumentDTO> getPendingExpiryDocumentList(ExpiryDocumentDTO expiryDocumentDTO) {
		List<ExpiryDocumentDTO> expiryDocumentDTOs=new ArrayList<ExpiryDocumentDTO>();
		UserProfileInfo profileInfo = userProfileRepository.findOne(expiryDocumentDTO.getUserId());
		if (profileInfo != null){
			if (profileInfo.getRoleId().getRoleName().equals(Role.ShipMaster)) {
				ShipProfileInfo shipProfileInfo = shipProfileRepository.findByShipMaster(profileInfo);
					if (shipProfileInfo != null) {
						List<ExpiryDocumentInfo> expiryDocumentInfoList=expiryDocumentRepository.findByShipProfileInfoAndDocumentStatusAndStatus(shipProfileInfo,env.getProperty("document.status.pending"),env.getProperty("active"));
						if (expiryDocumentInfoList != null) {
							for(ExpiryDocumentInfo expiryDocumentInfo:expiryDocumentInfoList){
								if(!expiryDocumentInfo.getUploadedBy().getId().equals(expiryDocumentDTO.getUserId())){
									ExpiryDocumentDTO expiryDocumentDTOInfo=expiryDocumentUtility.convertExpiryDocumentToDTO(expiryDocumentInfo);
									expiryDocumentDTOInfo.setDocumentHolderName(expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderName());
									expiryDocumentDTOInfo.setDocumentHolderType(expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderType());
									expiryDocumentDTOs.add(expiryDocumentDTOInfo);
								}
							}
						}
					}
			}

			if (profileInfo.getRoleId().getRoleName().equals(Role.TechManager)) {
				List<ShipProfileInfo> techManagerShipProfileInfos = shipProfileRepository.findByStatusAndTechMasters(1,profileInfo);
				if (techManagerShipProfileInfos != null && techManagerShipProfileInfos.size() > 0) {
					for (ShipProfileInfo shipProfile : techManagerShipProfileInfos) {
						List<ExpiryDocumentInfo> expiryDocumentInfoList=expiryDocumentRepository.findByShipProfileInfoAndDocumentStatusAndStatus(shipProfile,env.getProperty("document.status.pending"),env.getProperty("active"));
						if (expiryDocumentInfoList != null) {
							for(ExpiryDocumentInfo expiryDocumentInfo:expiryDocumentInfoList){
								if(!expiryDocumentInfo.getUploadedBy().getId().equals(expiryDocumentDTO.getUserId())){
									ExpiryDocumentDTO expiryDocumentDTOInfo=expiryDocumentUtility.convertExpiryDocumentToDTO(expiryDocumentInfo);
									expiryDocumentDTOInfo.setDocumentHolderName(expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderName());
									expiryDocumentDTOInfo.setDocumentHolderType(expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderType());
									expiryDocumentDTOs.add(expiryDocumentDTOInfo);
								}
							}
						}
					}
				}

			}

		}

		/*ShipProfileInfo shipProfileInfo=shipProfileRepository.findById(expiryDocumentDTO.getShipProfileId());
		List<ExpiryDocumentInfo> expiryDocumentInfoList=expiryDocumentRepository.findByShipProfileInfoAndDocumentStatusAndStatus(shipProfileInfo,env.getProperty("document.status.pending"),env.getProperty("active"));
		if (expiryDocumentInfoList != null) {
			for(ExpiryDocumentInfo expiryDocumentInfo:expiryDocumentInfoList){
				if(!expiryDocumentInfo.getUploadedBy().getId().equals(expiryDocumentDTO.getUserId())){
					ExpiryDocumentDTO expiryDocumentDTOInfo=expiryDocumentUtility.convertExpiryDocumentToDTO(expiryDocumentInfo);
					expiryDocumentDTOInfo.setDocumentHolderName(expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderName());
					expiryDocumentDTOInfo.setDocumentHolderType(expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderType());
					expiryDocumentDTOs.add(expiryDocumentDTOInfo);
				}
			}
		}*/
		return expiryDocumentDTOs;
	}

	@Override
	public List<ExpiryDocumentDTO> getExpiryDocumentInfosByDocumentStatus(ExpiryDocumentDTO expiryDocumentDTO) {
		List<ExpiryDocumentDTO> expiryDocumentDTOs=new ArrayList<ExpiryDocumentDTO>();
		ShipProfileInfo shipProfileInfo=shipProfileRepository.findById(expiryDocumentDTO.getShipProfileId());
		UserProfileInfo userProfileInfo=userProfileRepository.findOne(expiryDocumentDTO.getUserId());
		List<ExpiryDocumentInfo> expiryDocumentInfoList=expiryDocumentRepository.findByShipProfileInfoAndApprovedBy(shipProfileInfo, userProfileInfo);
		if(expiryDocumentInfoList!=null){
			for(ExpiryDocumentInfo expiryDocumentInfo:expiryDocumentInfoList){
				if(!expiryDocumentInfo.getDocumentStatus().equalsIgnoreCase(env.getProperty("document.status.pending")))
				{
					ExpiryDocumentDTO expiryDocumentDTOInfo=expiryDocumentUtility.convertExpiryDocumentToDTO(expiryDocumentInfo);
					expiryDocumentDTOInfo.setDocumentHolderName(expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderName());
					expiryDocumentDTOInfo.setDocumentHolderType(expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderType());
					expiryDocumentDTOs.add(expiryDocumentDTOInfo);
				}
			}
		}
		return expiryDocumentDTOs;
	}

	@Override
	public boolean isSuperAdminId(ExpiryDocumentDTO expiryDocumentDTO) {
		UserProfileInfo userProfileInfo = userProfileRepository.findById(expiryDocumentDTO.getLoginId());
		if (userProfileInfo != null) {
			RoleInfo roleId=userProfileInfo.getRoleId();
			RoleInfo roleInfo = roleInfoRepository.findById(roleId.getId());
			if (roleInfo != null && roleInfo.getRoleName().equals(Role.SuperAdmin)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isExistOrganization(ExpiryDocumentDTO expiryDocumentDTO) {
		List<OrganizationInfo> organizationInfo=organizationInfoRepository.findById(expiryDocumentDTO.getOrganizationId());
		if(organizationInfo.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean changeStatus(ExpiryDocumentDTO expiryDocumentDTO) {
		DualApprovalInfo dualApprovalInfo=dualApprovalRepository.findByOrganizationInfo(organizationInfoRepository.findOne(expiryDocumentDTO.getOrganizationId()));
		dualApprovalInfo.setFlag(expiryDocumentDTO.getFlag());
		DualApprovalInfo dualApproval=dualApprovalRepository.saveAndFlush(dualApprovalInfo);
		if(dualApproval!=null){
			return true;
		}
		return false;
	}

	@Override
	public boolean isExistLoginId(ExpiryDocumentDTO expiryDocumentDTO) {
		UserProfileInfo userProfileInfo = userProfileRepository.findById(expiryDocumentDTO.getLoginId());
		if (userProfileInfo != null) {
			expiryDocumentDTO.setOrganizationInfo(userProfileInfo.getOrganizationInfo());
			RoleInfo roleId=userProfileInfo.getRoleId();
			RoleInfo roleInfo = roleInfoRepository.findById(roleId.getId());
			if (roleInfo != null && (roleInfo.getRoleName().equals(Role.TechManager)||roleInfo.getRoleName().equals(Role.ShipMaster))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<ExpiryDocumentDTO> getExpiryDocumentList(ExpiryDocumentDTO expiryDocumentDTO) {
		List<ExpiryDocumentDTO> expDocument=new ArrayList<ExpiryDocumentDTO>();
		UserProfileInfo userProfileInfo = userProfileRepository.findById(expiryDocumentDTO.getLoginId());
		List<ShipProfileInfo> shipProfileList=shipProfileRepository.findByShipOrganizationInfo(expiryDocumentDTO.getOrganizationInfo());
		if(shipProfileList!=null){
			for(ShipProfileInfo shipProfileInfo:shipProfileList){
				if(shipProfileInfo!=null){
//					List<ExpiryDocumentInfo> expiryDocumentInfoList=expiryDocumentRepository.findByShipProfileInfoOrderByModifiedDateDesc(shipProfileInfo);
					List<ExpiryDocumentInfo> expiryDocumentInfoList=expiryDocumentRepository.findByShipProfileInfoAndDocumentStatusAndStatusAndUploadedByNot(shipProfileInfo,env.getProperty("document.status.pending"),"1",userProfileInfo);
					List<ExpiryDocumentInfo> expiryDocumentInfoLst=expiryDocumentRepository.findByShipProfileInfoAndApprovedByAndDocumentStatusNot(shipProfileInfo,userProfileInfo,env.getProperty("document.status.pending"));
					expiryDocumentInfoList.addAll(expiryDocumentInfoLst);
					for(ExpiryDocumentInfo expiryDocumentInfo:expiryDocumentInfoList){
						if(expiryDocumentInfo!=null){
							ExpiryDocumentDTO expiryDocument=expiryDocumentUtility.convertExpiryDocumentToDTO(expiryDocumentInfo);
							expiryDocument.setShipName(shipProfileInfo.getShipName());
							expDocument.add(expiryDocument);
						}
					}
				}
			}
		}
		return expDocument;
	}

	@Override
	public List<ExpiryDocumentDTO> getExpiryTypeDocumentList(Long shipId) {
		Integer archivedStatus = 0;
		List<ExpiryDocumentDTO> expiryDocumentDTOs=new ArrayList<ExpiryDocumentDTO>();
		List<DocumentHolderInfo> documentHolderInfoList=documentHolderRepository.findByDocumentHolderTypeOrderByDocumentHolderName(env.getProperty("document.holder.type"));
		for(DocumentHolderInfo documentHolderInfo:documentHolderInfoList ){
			ExpiryDocumentDTO expiryDocumentDTO=expiryDocumentUtility.convertExpiryTypeDocumentInfoToExpiryDocumentDTO(documentHolderInfo,shipId,archivedStatus);
			if(expiryDocumentDTO.getId() != null){
				expiryDocumentDTOs.add(expiryDocumentDTO);
			}

		}
		ShipProfileInfo shipProfileInfo=shipProfileRepository.findById(shipId);
		Set<DocumentHolderInfo>customDocumentHolders=shipProfileInfo.getCustomDocumentHolders();
		System.out.println("size ::::"+customDocumentHolders.size());
		if(customDocumentHolders!=null && !customDocumentHolders.isEmpty()){
			for(DocumentHolderInfo customFolderInfo:customDocumentHolders){
				ExpiryDocumentDTO expiryDocumentDTO=expiryDocumentUtility.convertExpiryTypeDocumentInfoToExpiryDocumentDTO(customFolderInfo,shipId,archivedStatus);
				if(expiryDocumentDTO.getId() != null){
					expiryDocumentDTOs.add(expiryDocumentDTO);
				}
			}
		}
		return expiryDocumentDTOs;
	}

	@Override
	public ShipProfileInfo findShipProfileInfo(Long shipId) {
		return shipProfileRepository.findOne(shipId);
	}

	@Override
	public ExpiryDocumentInfo updateDocument(ExpiryDocumentDTO expiryDocumentDTO) {
		ExpiryDocumentInfo expiryDocumentInfo=expiryDocumentRepository.findOne(expiryDocumentDTO.getId());
		expiryDocumentInfo.setCertificateNumber(expiryDocumentDTO.getCertificateNumber());
		expiryDocumentInfo.setPlaceOfIssue(expiryDocumentDTO.getPlaceOfIssue());
		expiryDocumentInfo.setIssueDate(expiryDocumentDTO.getIssueDate());
		expiryDocumentInfo.setExpiryDate(expiryDocumentDTO.getExpiryDate());
			expiryDocumentInfo.setLastAnnual(expiryDocumentDTO.getLastAnnual());
			expiryDocumentInfo.setNextAnnual(expiryDocumentDTO.getNextAnnual());
			expiryDocumentInfo.setRemarks(expiryDocumentDTO.getRemarks());
			expiryDocumentInfo.setIssuingAuthority(expiryDocumentDTO.getIssuingAuthority());
		expiryDocumentInfo.setModifiedDate(new Date());
		expiryDocumentRepository.saveAndFlush(expiryDocumentInfo);
		notificationUtility.notifyDocumentUpload(expiryDocumentDTO.getUploadedUserId(), expiryDocumentInfo);
		return expiryDocumentInfo;
	}

}
