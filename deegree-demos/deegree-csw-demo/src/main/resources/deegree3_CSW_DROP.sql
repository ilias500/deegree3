--  -------------------------------------------------- 
--  Generated by Enterprise Architect Version 7.1.834
--  Created On : Dienstag, 22 September, 2009 
--  DBMS       : PostgreSQL 
--  -------------------------------------------------- 


-- Drop Foreign Key Constraints 
--ALTER TABLE Datasets DROP CONSTRAINT FK_Datasets_SelfQueryableProperties;
ALTER TABLE ISOQP_Abstract DROP CONSTRAINT FK_ISOQP_Abstract_Datasets;
ALTER TABLE ISOQP_AlternateTitle DROP CONSTRAINT FK_ISOQP_AlternateTitle_Datasets;
ALTER TABLE ISOQP_BoundingBox DROP CONSTRAINT FK_ISOQP_BoundingBox_Datasets;
ALTER TABLE ISOQP_CouplingType DROP CONSTRAINT FK_ISOQP_CouplingType_Datasets;
ALTER TABLE ISOQP_CreationDate DROP CONSTRAINT FK_ISOQP_CreationDate_Datasets;
ALTER TABLE ISOQP_Format DROP CONSTRAINT FK_ISOQP_Format_Datasets;
ALTER TABLE ISOQP_GeographicDescriptionCode DROP CONSTRAINT FK_ISOQP_GeographicDescriptionCode_Datasets;
ALTER TABLE ISOQP_Keyword DROP CONSTRAINT FK_ISOQP_KeywordType_Datasets;
ALTER TABLE ISOQP_OperatesOnData DROP CONSTRAINT FK_ISOQP_OperatesOnData_Datasets;
ALTER TABLE ISOQP_Operation DROP CONSTRAINT FK_ISOQP_Operation_Datasets;
ALTER TABLE ISOQP_OrganisationName DROP CONSTRAINT FK_ISOQP_OrganisationName_Datasets;
ALTER TABLE ISOQP_PublicationDate DROP CONSTRAINT FK_ISOQP_PublicationDate_Datasets;
ALTER TABLE ISOQP_ResourceIdentifier DROP CONSTRAINT FK_ISOQP_ResourceIdentifier_Datasets;
ALTER TABLE ISOQP_ResourceLanguage DROP CONSTRAINT FK_ISOQP_ResourceLanguage_Datasets;
ALTER TABLE ISOQP_RevisionDate DROP CONSTRAINT FK_ISOQP_RevisionDate_Datasets;
ALTER TABLE ISOQP_ServiceType DROP CONSTRAINT FK_ISOQP_ServiceType_Datasets;
ALTER TABLE ISOQP_ServiceTypeVersion DROP CONSTRAINT FK_ISOQP_ServiceTypeVersion_Datasets;
ALTER TABLE ISOQP_SpatialResolution DROP CONSTRAINT FK_ISOQP_SpatialResolution_Datasets;
ALTER TABLE ISOQP_TemporalExtent DROP CONSTRAINT FK_ISOQP_TemporalExtent_Datasets;
ALTER TABLE ISOQP_Title DROP CONSTRAINT FK_ISOQP_Title_Datasets;
ALTER TABLE ISOQP_TopicCategory DROP CONSTRAINT FK_ISOQP_TopicCategory_Datasets;
ALTER TABLE ISOQP_Type DROP CONSTRAINT FK_ISOQP_Type_Datasets;
ALTER TABLE QP_Identifier DROP CONSTRAINT fk_qp_identifier_datasets;
ALTER TABLE DCQP_RIGHTS DROP CONSTRAINT FK_DCQP_RIGHTS_Datasets;

-- Drop Tables, Stored Procedures and Views 
DROP TABLE Datasets;
DROP TABLE ISOQP_Abstract;
DROP TABLE ISOQP_AlternateTitle;
DROP TABLE ISOQP_BoundingBox;
DROP TABLE ISOQP_CouplingType;
DROP TABLE ISOQP_CreationDate;
DROP TABLE ISOQP_Format;
DROP TABLE ISOQP_GeographicDescriptionCode;
DROP TABLE ISOQP_Keyword;
DROP TABLE ISOQP_OperatesOnData;
DROP TABLE ISOQP_Operation;
DROP TABLE ISOQP_OrganisationName;
DROP TABLE ISOQP_PublicationDate;
DROP TABLE ISOQP_ResourceIdentifier;
DROP TABLE ISOQP_ResourceLanguage;
DROP TABLE ISOQP_RevisionDate;
DROP TABLE ISOQP_ServiceType;
DROP TABLE ISOQP_ServiceTypeVersion;
DROP TABLE ISOQP_SpatialResolution;
DROP TABLE ISOQP_TemporalExtent;
DROP TABLE ISOQP_Title;
DROP TABLE ISOQP_TopicCategory;
DROP TABLE ISOQP_Type;
DROP TABLE QP_Identifier;
DROP TABLE DCQP_RIGHTS;


--INSPIRE extension
ALTER TABLE ADDQP_Degree DROP CONSTRAINT FK_ADDQP_Degree;
ALTER TABLE ADDQP_Specification DROP CONSTRAINT FK_ADDQP_Specification;
ALTER TABLE ADDQP_Limitation DROP CONSTRAINT FK_ADDQP_Limitation;
ALTER TABLE ADDQP_Lineage DROP CONSTRAINT FK_ADDQP_Lineage;
ALTER TABLE ADDQP_AccessConstraint DROP CONSTRAINT FK_ADDQP_AccessConstraint;
ALTER TABLE ADDQP_OtherConstraint DROP CONSTRAINT FK_ADDQP_OtherConstraint;
ALTER TABLE ADDQP_Classification DROP CONSTRAINT FK_ADDQP_Classification;


DROP TABLE ADDQP_Degree;
DROP TABLE ADDQP_Specification;
DROP TABLE ADDQP_Limitation;
DROP TABLE ADDQP_Lineage;
DROP TABLE ADDQP_AccessConstraint;
DROP TABLE ADDQP_OtherConstraint;
DROP TABLE ADDQP_Classification;


