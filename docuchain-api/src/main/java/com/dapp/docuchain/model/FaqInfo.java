package com.dapp.docuchain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "question_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FaqInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column (name = "question")
	private String question;
	
	@Column (name = "answer")
	private String answer;

}
