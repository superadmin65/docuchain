package com.dapp.docuchain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dapp.docuchain.dto.OrganizationDTO;
import com.dapp.docuchain.dto.UserDTO;

@Service
public interface OrganizationInfoService {

	public List<OrganizationDTO> getAllOrganizationList();
	
	public String isOrganizationExist(Long organizationId);

	public String saveOrganizationInformation(OrganizationDTO organizationDTO);

	public String saveOrganizationWithSubscriptionAndAdmin(OrganizationDTO organizationDTO);

	public String updateOrganizationInformation(OrganizationDTO organizationDTO);

	public String deleteOrganizationInformation(Long superAdminId, Long organizationId);

	public String setSaveInBlockchainOrganizationInformation(OrganizationDTO organizationDTO);

	public String activeActiveAndDeactiveOrganizationInformation(OrganizationDTO organizationDTO);

	public String enableOrDiableDualApprovalOrganizationInformation(OrganizationDTO organizationDTO);

	public OrganizationDTO viewOrganizationInformation(Long organizationId);

	public List<UserDTO> listAdminsBasedOrganizationInformation(Long organizationId);

	public String saveAdminInformationBasedOrganization(OrganizationDTO organizationDTO);

	public String updateSubscriptionAndOrganization(OrganizationDTO organizationDTO);

	public String updateSubscription(OrganizationDTO organizationDTO);

	public OrganizationDTO getOrganizationTopCount();

	public List<OrganizationDTO> getOrganizationStatistics();

	
	
}
