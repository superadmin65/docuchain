package com.dapp.docuchain.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(name = "group_shared_documents")
@NoArgsConstructor
@AllArgsConstructor

public class GroupSharedDocumentInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "title", nullable = true)
	private String title;

	@Column(name = "subject", nullable = true)
	private String subject;

	@Column(name = "toemail")
	private String toEmailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
	private GroupTagDocumentInfo groupTagDocumentInfo;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "group_shared_xref_expiry_document", 
	joinColumns = {@JoinColumn(name = "shared_group_id", referencedColumnName = "id") },
	inverseJoinColumns = {@JoinColumn(name = "document_holder_id", referencedColumnName = "id") })
	private Set<DocumentHolderInfo> documentHolderInfo;
}
