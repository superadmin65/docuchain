##########################
#Roles
##########################
INSERT INTO `dms_docuchain`.`role_info` (`id`, `role`) VALUES ('1', 'Admin');
INSERT INTO `dms_docuchain`.`role_info` (`id`, `role`) VALUES ('2', 'ShipMaster');
INSERT INTO `dms_docuchain`.`role_info` (`id`, `role`) VALUES ('3', 'TechManager');
INSERT INTO `dms_docuchain`.`role_info` (`id`, `role`) VALUES ('4', 'CommercialManager');
INSERT INTO `dms_docuchain`.`role_info` (`id`, `role`) VALUES ('5', 'DataOperator');

INSERT INTO `dms_docuchain`.`user_profile_info` (`id`, `status`, `user_name`, `role_id`) VALUES ('1', '1', 'admin', '1');

##########################
#Country
##########################
INSERT INTO `dms_docuchain`.`country_info` VALUES (null, 'AF', 'Afghanistan');
INSERT INTO `dms_docuchain`.`country_info` VALUES (null, 'AL', 'Albania');
INSERT INTO `dms_docuchain`.`country_info` VALUES (null, 'IN', 'India');

##########################
#State
##########################
INSERT INTO `dms_docuchain`.`state_info` (`id`, `name`, `country_id`) VALUES
(1, 'Andaman and Nicobar Islands', 3),
(2, 'Andhra Pradesh', 3),
(3, 'Arunachal Pradesh', 3),
(4, 'Assam', 3),
(5, 'Bihar', 3),
(6, 'Chandigarh', 3),
(7, 'Chhattisgarh', 3),
(8, 'Dadra and Nagar Haveli', 3),
(9, 'Daman and Diu', 3),
(10, 'Delhi', 3),
(11, 'Goa', 3),
(12, 'Gujarat', 3),
(13, 'Haryana', 3),
(14, 'Himachal Pradesh', 3),
(15, 'Jammu and Kashmir', 3),
(16, 'Jharkhand', 3),
(17, 'Karnataka', 3),
(18, 'Kenmore', 3),
(19, 'Kerala', 3),
(20, 'Lakshadweep', 3),
(21, 'Madhya Pradesh', 3),
(22, 'Maharashtra', 3),
(23, 'Manipur', 3),
(24, 'Meghalaya', 3),
(25, 'Mizoram', 3),
(26, 'Nagaland', 3),
(27, 'Narora', 3),
(28, 'Natwar', 3),
(29, 'Odisha', 3),
(30, 'Paschim Medinipur', 3),
(31, 'Pondicherry', 3),
(32, 'Punjab', 3),
(33, 'Rajasthan', 3),
(34, 'Sikkim', 3),
(35, 'Tamil Nadu', 3),
(36, 'Telangana', 3),
(37, 'Tripura', 3),
(38, 'Uttar Pradesh', 3),
(39, 'Uttarakhand', 3),
(40, 'Vaishali', 3),
(41, 'West Bengal', 3),
(42, 'Badakhshan', 1),
(43, 'Badgis', 1),
(44, 'Baglan', 1),
(45, 'Balkh', 1),
(46, 'Bamiyan', 1),
(47, 'Farah', 1),
(48, 'Faryab', 1),
(49, 'Gawr', 1),
(50, 'Gazni', 1),
(51, 'Herat', 1),
(52, 'Hilmand', 1),
(53, 'Jawzjan', 1),
(54, 'Kabul', 1),
(55, 'Kapisa', 1),
(56, 'Khawst', 1),
(57, 'Kunar', 1),
(58, 'Lagman', 1),
(59, 'Lawghar', 1),
(60, 'Nangarhar', 1),
(61, 'Nimruz', 1),
(62, 'Nuristan', 1),
(63, 'Paktika', 1),
(64, 'Paktiya', 1),
(65, 'Parwan', 1),
(66, 'Qandahar', 1),
(67, 'Qunduz', 1),
(68, 'Samangan', 1),
(69, 'Sar-e Pul', 1),
(70, 'Takhar', 1),
(71, 'Uruzgan', 1),
(72, 'Wardag', 1),
(73, 'Zabul', 1),
(74, 'Berat', 2),
(75, 'Bulqize', 2),
(76, 'Delvine', 2),
(77, 'Devoll', 2),
(78, 'Dibre', 2),
(79, 'Durres', 2),
(80, 'Elbasan', 2),
(81, 'Fier', 2),
(82, 'Gjirokaster', 2),
(83, 'Gramsh', 2),
(84, 'Has', 2),
(85, 'Kavaje', 2),
(86, 'Kolonje', 2),
(87, 'Korce', 2),
(88, 'Kruje', 2),
(89, 'Kucove', 2),
(90, 'Kukes', 2),
(91, 'Kurbin', 2),
(92, 'Lezhe', 2),
(93, 'Librazhd', 2),
(94, 'Lushnje', 2),
(95, 'Mallakaster', 2),
(96, 'Malsi e Madhe', 2),
(97, 'Mat', 2),
(98, 'Mirdite', 2),
(99, 'Peqin', 2),
(100, 'Permet', 2),
(101, 'Pogradec', 2),
(102, 'Puke', 2),
(103, 'Sarande', 2),
(104, 'Shkoder', 2),
(105, 'Skrapar', 2),
(106, 'Tepelene', 2),
(107, 'Tirane', 2),
(108, 'Tropoje', 2),
(109, 'Vlore', 2)

###########################
#Ship Types
###########################

INSERT INTO `dms_docuchain`.`types_of_ship` (`id`, `ship_types_name`) VALUES
(1, 'Cargo Ship'),
(2, 'Roll on Roll Off Ship'),
(3, 'Passenger Ship'),
(4, 'Tankers Ship'),
(5, 'Fishing Vessel Ship'),
(6, 'High Speed Craft Ship');

###########################
#Document Holder
###########################
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (1,'1','Dummy Description','Certificate of Ship Registry','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (2,'2','Dummy Description','Minimum Safe Manning Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (3,'3','Dummy Description','Certificate(s) of Class (including Appendix to Class)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (4,'4','Dummy Description','CAS Certificate (Condition Assessment Survey)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (5,'5','Dummy Description','CAP Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (6,'6','Dummy Description','Antifouling Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (7,'7','Dummy Description','International Tonnage Certificate (1969)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (8,'8','Dummy Description','Panama Canal Tonnage Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (9,'9','Dummy Description','Suez Canal Tonnage Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (10,'10','Dummy Description','International Load Line / Exemption Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (11,'10a','Dummy Description','Load Line Conditions of Assignment (operational limit)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (12,'12','Dummy Description','Safety Equipment Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (13,'12a','Dummy Description','SEC - list of equipment','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (14,'13','Dummy Description','Safety Construction Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (15,'13a','Dummy Description','SCC - Form C (as applicable)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (16,'14','Dummy Description','Safety Radio Certificate (and Flag State Patente, as applicable)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (17,'14a','Dummy Description','SRC - Form R','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (18,'15','Dummy Description','International Ballast Water Management Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (19,'16','Dummy Description','Ship Radio Station License','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (20,'17','Dummy Description','GMDSS Approval/installation Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (21,'17a','Dummy Description','GMDSS Shore Based Maintenance Agreement','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (22,'17b','Dummy Description','LRIT Test Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (23,'17c','Dummy Description','VDR Approval/installation Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (24,'17d','Dummy Description','AIS Approval/installation Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (25,'17e','Dummy Description','EPIRB Approval/installation Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (26,'17f','Dummy Description','SSAS Approval/Installation Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (27,'18','Dummy Description','Special Purpose (SPS Certificate)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (28,'19','Dummy Description','Marpol annex v garbage certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (29,'21','Dummy Description','IOPP Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (30,'21a','Dummy Description','IOPP – Form B','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (31,'22','Dummy Description','Insurance Certificates','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (32,'22a','Dummy Description','CLC Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (33,'22b','Dummy Description','Bunker CLC Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (34,'22c','Dummy Description','P&I Certificate of Entry','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (35,'22d','Dummy Description','H&M Certificate of Entry','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (36,'22e','Dummy Description','FD&D Certificate of Entry','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (37,'22f','Dummy Description','Wreck Removal','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (38,'23','Dummy Description','US Certificates','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (39,'23a','Dummy Description','USCG Federal COFR','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (40,'23b','Dummy Description','State of California COFR','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (41,'23c','Dummy Description','State of Alaska COFR','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (42,'24','Dummy Description','International Sewage Pollution Prevention Certificate (ISPPC)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (43,'25','Dummy Description','International Air Pollution Prevention Certificate (IAPP)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (44,'26','Dummy Description','Engine International Air Pollution Prevention Certificate (EIAPP)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (45,'27','Dummy Description','International Energy Efficiency Certificate (IEEC)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (46,'28','Dummy Description','BC Code Fitness Certificate / IMDG','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (47,'29','Dummy Description','Authorisation Document and Plan for carriage of Bulk Grain (Document of Authorisation)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (48,'30','Dummy Description','NLS / Certificate of Fitness (BCH / IBC / IGC)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (49,'31','Dummy Description','Flag State Accommodation Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (50,'32','Dummy Description','Sanitary certificates (Japanese and others)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (51,'33','Dummy Description','Ship Sanitation / Exemption Certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (52,'34','Dummy Description','ISM – Company DOC (copy) with Owners ISM letter','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (53,'35','Dummy Description','ISM – Ship’s SMC','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (54,'36','Dummy Description','ISPS – ISSC','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (55,'37','Dummy Description','ISPS – CSR (Continuous Synopsis Records)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (56,'38','Dummy Description','MLC','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (57,'39a','Dummy Description','DMLC Part 1 and 2','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (58,'39b','Dummy Description','Crew Accommodation certificate','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (59,'40','Dummy Description','ISO - Certificate of Approval (copy)','EXPIRY_TYPE');
INSERT INTO `dms_docuchain`.`document_holder_info` (`id`,`document_file_number`,`document_holder_description`,`document_holder_name`,`document_holder_type`) VALUES (60,'41','Dummy Description','Medical Chest Certificate','EXPIRY_TYPE');

#################################
#Task Status
#################################
INSERT INTO `dms_docuchain`.`task_status_info` (`id`,`task_status`) VALUES (1,'Yet To Start');
INSERT INTO `dms_docuchain`.`task_status_info` (`id`,`task_status`) VALUES (2,'In Progress');
INSERT INTO `dms_docuchain`.`task_status_info` (`id`,`task_status`) VALUES (3,'Completed');
INSERT INTO `dms_docuchain`.`task_status_info` (`id`,`task_status`) VALUES (4,'Failed');
INSERT INTO `dms_docuchain`.`task_status_info` (`id`,`task_status`) VALUES (5,'Rejected');

#################################
#Scan Delimiter
#################################
INSERT INTO `dms_docuchain`.`scan_delimiter_info` (`id`,`ending_pattern`,`field_type`,`starting_pattern`) VALUES (1,'\n','CERT_NO','no:');
INSERT INTO `dms_docuchain`.`scan_delimiter_info` (`id`,`ending_pattern`,`field_type`,`starting_pattern`) VALUES (2,'subject','EXPIRY_DATE','valid until');
INSERT INTO `dms_docuchain`.`scan_delimiter_info` (`id`,`ending_pattern`,`field_type`,`starting_pattern`) VALUES (3,' f','ISSUE_DATE',' on ');
INSERT INTO `dms_docuchain`.`scan_delimiter_info` (`id`,`ending_pattern`,`field_type`,`starting_pattern`) VALUES (4,' on','ISSUE_PLACE','Issued at');
