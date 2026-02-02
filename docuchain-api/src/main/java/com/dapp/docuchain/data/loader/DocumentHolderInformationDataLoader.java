package com.dapp.docuchain.data.loader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.DocumentHolderType;
import com.dapp.docuchain.repository.DocumentHolderRepository;


@Component
public class DocumentHolderInformationDataLoader implements ApplicationRunner {
	
	@Autowired
	private DocumentHolderRepository documentHolderRepository;
	
	public DocumentHolderInformationDataLoader(DocumentHolderRepository documentHolderRepository){
		this.documentHolderRepository = documentHolderRepository;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Long count = documentHolderRepository.count();
		if(count == 0) {
			List<DocumentHolderInfo> documentHolderInfos = new ArrayList<>();
			documentHolderInfos.add(new DocumentHolderInfo((long)1,"Certificate of Ship Registry", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)2,"Minimum Safe Manning Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)3,"Certificate(s) of Class (including Appendix to Class)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)4,"CAS Certificate (Condition Assessment Survey)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)5,"CAP Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)6,"Antifouling Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)7,"International Tonnage Certificate (1969)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)8,"Panama Canal Tonnage Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)9,"Suez Canal Tonnage Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)10,"International Load Line / Exemption Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)11,"Load Line Conditions of Assignment (operational limit)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)12,"Safety Equipment Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)13,"SEC - list of equipment", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)14,"Safety Construction Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)15,"SCC - Form C (as applicable)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)16,"Safety Radio Certificate (and Flag State Patente, as applicable)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)17,"SRC - Form R", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)18,"International Ballast Water Management Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)19,"Ship Radio Station License", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)20,"GMDSS Approval/installation Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)21,"GMDSS Shore Based Maintenance Agreement", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)22,"LRIT Test Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)23,"VDR Approval/installation Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)24,"AIS Approval/installation Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)25,"EPIRB Approval/installation Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)26,"SSAS Approval/Installation Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)27,"Special Purpose (SPS Certificate)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)28,"Marpol annex v garbage certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)29,"IOPP Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)30,"IOPP – Form B", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)31,"Insurance Certificates", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)32,"CLC Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)33,"Bunker CLC Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)34,"P&I Certificate of Entry", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)35,"H&M Certificate of Entry", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)36,"FD&D Certificate of Entry", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)37,"Wreck Removal", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)38,"US Certificates", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)39,"USCG Federal COFR", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)40,"State of California COFR", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)41,"State of Alaska COFR", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)42,"International Sewage Pollution Prevention Certificate (ISPPC)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)43,"International Air Pollution Prevention Certificate (IAPP)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)44,"Engine International Air Pollution Prevention Certificate (EIAPP)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)45,"International Energy Efficiency Certificate (IEEC)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)46,"BC Code Fitness Certificate / IMDG", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)47,"Authorisation Document and Plan for carriage of Bulk Grain (Document of Authorisation)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)48,"NLS / Certificate of Fitness (BCH / IBC / IGC)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)49,"Flag State Accommodation Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)50,"Sanitary certificates (Japanese and others)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)51,"Ship Sanitation / Exemption Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)52,"ISM – Company DOC (copy) with Owners ISM letter", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)53,"ISM – Ship’s SMC", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)54,"ISPS – ISSC", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)55,"ISPS – CSR (Continuous Synopsis Records)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)56,"MLC", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)57,"DMLC Part 1 and 2", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)58,"Crew Accommodation certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)59,"ISO - Certificate of Approval (copy)", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderInfos.add(new DocumentHolderInfo((long)60,"Medical Chest Certificate", "Dummy Description", DocumentHolderType.EXPIRY_TYPE.name(),"Standard Type",new Date()));
			documentHolderRepository.save(documentHolderInfos);	
		}
	}

}
