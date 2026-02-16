package com.dapp.docuchain;

import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.RoleInfo;
import com.dapp.docuchain.model.TaskStatusInfo;
import com.dapp.docuchain.model.ScanDelimiterInfo;
import com.dapp.docuchain.model.User;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.DocumentHolderRepository;
import com.dapp.docuchain.repository.DocumentNotificationRepository;
import com.dapp.docuchain.repository.ExpiryDocumentRepository;
import com.dapp.docuchain.repository.RoleInfoRepository;
import com.dapp.docuchain.repository.ScanDelimiterRepository;
import com.dapp.docuchain.repository.TaskStatusRepository;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.naming.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.dapp.docuchain")
@EnableJpaRepositories("com.dapp.docuchain.repository")
@EnableLdapRepositories("com.dapp.docuchain.repository")
@PropertySource({ "classpath:application.properties", "classpath:message.properties" })
public class DocuchainApplication extends SpringBootServletInitializer {

	@Autowired
	Environment env;

	@Autowired
	DocumentHolderRepository documentHolderRepository;

	@Autowired
	ExpiryDocumentRepository expiryDocumentRepository;

	@Autowired
	DocumentNotificationRepository documentNotificationRepository;

	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(DocuchainApplication.class, args);
	}

	@Bean
	public Docket myDocuchainApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.dapp.docuchain")).paths(PathSelectors.any()).build()
				.pathMapping("/").apiInfo(metaData());
	}

	private ApiInfo metaData() {
		ApiInfo apiInfo = new ApiInfo("DMS DApp Docuchain REST API", "DMS DApp Docuchain application REST API's", "1.0",
				"Terms of service",
				new Contact("Senthilnathan Subash", "https://colaninfotech.com/",
						"senthilnathan.subash@colanonline.com"),
				"Apache License Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0");
		return apiInfo;
	}

	@Bean
	public CommandLineRunner initializeScanDelimiter(ScanDelimiterRepository scanDelimiterRepository) {
		return args -> {
			List<ScanDelimiterInfo> scanDelimiterInfos = new ArrayList<>();
			scanDelimiterInfos.add(new ScanDelimiterInfo((long) 1, "no:", "\n", "CERT_NO"));
			scanDelimiterInfos.add(new ScanDelimiterInfo((long) 2, "valid until", "subject", "EXPIRY_DATE"));
			scanDelimiterInfos.add(new ScanDelimiterInfo((long) 3, "on:", "f", "ISSUE_DATE"));
			scanDelimiterInfos.add(new ScanDelimiterInfo((long) 4, "Issued at", "on", "ISSUE_PLACE"));
			scanDelimiterRepository.save(scanDelimiterInfos);
		};
	}

	@Bean
	public LdapContextSource contextSource() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(env.getRequiredProperty("ldap.url"));
		contextSource.setBase(env.getRequiredProperty("ldap.partitionSuffix"));
		contextSource.setUserDn(env.getRequiredProperty("ldap.principal"));
		contextSource.setPassword(env.getRequiredProperty("ldap.password"));
		return contextSource;
	}

	@Bean
	public LdapTemplate ldapTemplate() {
		return new LdapTemplate(contextSource());
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(new ObjectMapper());
		restTemplate.getMessageConverters().add(converter);
		return restTemplate;
	}

	@Bean
	protected CommandLineRunner initializeRoleInformation(RoleInfoRepository roleInfoRepository) {
		return args -> {
			List<RoleInfo> roleInfos = new ArrayList<>();
			roleInfos.add(new RoleInfo((long) 1, Role.SuperAdmin));
			roleInfos.add(new RoleInfo((long) 2, Role.Admin));
			roleInfos.add(new RoleInfo((long) 3, Role.ShipMaster));
			roleInfos.add(new RoleInfo((long) 4, Role.TechManager));
			roleInfos.add(new RoleInfo((long) 5, Role.CommercialManager));
			roleInfoRepository.save(roleInfos);
		};
	}

	@Bean
	protected CommandLineRunner initializeTaskStatusInformation(TaskStatusRepository taskStatusRepository) {
		return args -> {
			List<TaskStatusInfo> taskStatusInfos = new ArrayList<>();
			taskStatusInfos.add(new TaskStatusInfo((long) 1, "Yet To Start"));
			taskStatusInfos.add(new TaskStatusInfo((long) 2, "In Progress"));
			taskStatusInfos.add(new TaskStatusInfo((long) 3, "Completed"));
			taskStatusInfos.add(new TaskStatusInfo((long) 4, "Failed"));
			taskStatusInfos.add(new TaskStatusInfo((long) 5, "Rejected"));
			taskStatusRepository.save(taskStatusInfos);
		};
	}

	@Bean
	protected CommandLineRunner initializeSuperAdminInformation(UserProfileRepository userProfileRepository) {
		return args -> {
			String password = "password";
			String base64;
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA");
				digest.update(password.getBytes());
				base64 = Base64.getEncoder().encodeToString(digest.digest());
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}

			User user = userRepository.findByUsername("superadmin");
			if (user != null) {
				UserProfileInfo userProfileInfo = userProfileRepository.findByUserName(user.getUsername());
				if (userProfileInfo == null) {
					userProfileInfo = new UserProfileInfo();
					userProfileInfo.setUserName(user.getUsername());
					userProfileInfo.setRoleId(new RoleInfo((long) 1, Role.SuperAdmin));
					userProfileInfo.setFirstName("Super");
					userProfileInfo.setLastName("Admin");
					userProfileInfo.setStatus(new Long(1));
					userProfileRepository.save(userProfileInfo);
				}
			} else {
				Name dn = LdapNameBuilder.newInstance().add("ou", "Users").add("cn", "superadmin").build();
				DirContextAdapter context = new DirContextAdapter(dn);

				context.setAttributeValues("objectclass",
						new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
				context.setAttributeValue("cn", "superadmin");
				context.setAttributeValue("sn", "superadmin");
				context.setAttributeValue("userPassword", "{SHA}" + base64);
				context.setAttributeValue("businessCategory", Role.SuperAdmin.name());
				context.setAttributeValue("mail", env.getProperty("super.admin.email"));
				ldapTemplate.bind(context);
				if (ldapTemplate != null) {
					UserProfileInfo userProfileInfo1 = new UserProfileInfo();
					userProfileInfo1.setUserName("superadmin");
					userProfileInfo1.setFirstName("Super");
					userProfileInfo1.setLastName("Admin");
					userProfileInfo1.setStatus(new Long(1));
					userProfileRepository.save(userProfileInfo1);
				}
			}
		};
	}

}
