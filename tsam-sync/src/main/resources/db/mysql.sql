drop table if exists CODE_SYSTEM;
drop table if exists CODE_SYSTEM_CONCEPT;
drop table if exists CODE_SYSTEM_VERSION;
drop table if exists DESIGNATION;
drop table if exists TRANSCODING_ASSOCIATION;
drop table if exists VALUE_SET;
drop table if exists VALUE_SET_VERSION;
drop table if exists X_CONCEPT_VALUE_SET;

create table CODE_SYSTEM (
  ID bigint not null,
  OID varchar(255),
  NAME varchar(255),
  DESCRIPTION varchar(255),
  primary key (ID)
);

create table CODE_SYSTEM_CONCEPT (
  ID bigint not null,
  CODE varchar(255),
  STATUS varchar(255),
  STATUS_DATE timestamp,
  DEFINITION varchar(1000),
  CODE_SYSTEM_VERSION_ID bigint,
  primary key (ID)
);

create table CODE_SYSTEM_VERSION (
  ID bigint not null,
  FULL_NAME varchar(255),
  LOCAL_NAME varchar(255),
  PREVIOUS_VERSION_ID bigint,
  EFFECTIVE_DATE timestamp,
  RELEASE_DATE timestamp,
  STATUS varchar(255),
  STATUS_DATE timestamp,
  DESCRIPTION varchar(255),
  COPYRIGHT varchar(255),
  SOURCE varchar(255),
  CODE_SYSTEM_ID bigint,
  primary key (ID)
);

create table DESIGNATION (
  ID bigint not null,
  DESIGNATION varchar(255),
  LANGUAGE_CODE varchar(255),
  TYPE varchar(255),
  IS_PREFERRED boolean,
  STATUS varchar(255),
  STATUS_DATE timestamp,
  CODE_SYSTEM_CONCEPT_ID bigint,
  primary key (ID)
);

create table TRANSCODING_ASSOCIATION (
  ID bigint not null auto_increment,
  TRANSCODING_ASSOCIATION_ID bigint not null,
  TARGET_CONCEPT_ID bigint,
  SOURCE_CONCEPT_ID bigint,
  QUALITY varchar(255),
  STATUS varchar(255),
  STATUS_DATE timestamp,
  primary key (ID)
);

create table VALUE_SET (
  ID bigint not null,
  OID varchar(255),
  EPSOS_NAME varchar(255),
  DESCRIPTION varchar(255),
  PARENT_CODE_SYSTEM_ID bigint,
  primary key (ID)
);

create table VALUE_SET_VERSION (
  ID bigint not null,
  VERSION_NAME varchar(255),
  EFFECTIVE_DATE timestamp,
  RELEASE_DATE timestamp,
  STATUS varchar(255),
  STATUS_DATE timestamp,
  DESCRIPTION varchar(255),
  PREVIOUS_VERSION_ID bigint,
  VALUE_SET_ID bigint,
  primary key (ID)
);

create table X_CONCEPT_VALUE_SET (
  CODE_SYSTEM_CONCEPT_ID bigint not null,
  VALUE_SET_VERSION_ID bigint not null,
  primary key (CODE_SYSTEM_CONCEPT_ID, VALUE_SET_VERSION_ID)
);

alter table CODE_SYSTEM_CONCEPT
  add constraint FK_CODE_SYSTEM_VERSION_ID
foreign key (CODE_SYSTEM_VERSION_ID)
references CODE_SYSTEM_VERSION;

alter table CODE_SYSTEM_VERSION
  add constraint FK_CODE_SYSTEM_ID
foreign key (CODE_SYSTEM_ID)
references CODE_SYSTEM;

alter table DESIGNATION
  add constraint FK_CODE_SYSTEM_CONCEPT_ID
foreign key (CODE_SYSTEM_CONCEPT_ID)
references CODE_SYSTEM_CONCEPT;

alter table TRANSCODING_ASSOCIATION
  add constraint FK_SOURCE_CONCEPT_ID
foreign key (SOURCE_CONCEPT_ID)
references CODE_SYSTEM_CONCEPT;

alter table TRANSCODING_ASSOCIATION
  add constraint FK_TARGET_CONCEPT_ID
foreign key (TARGET_CONCEPT_ID)
references CODE_SYSTEM_CONCEPT;

alter table VALUE_SET
  add constraint FK_PARENT_CODE_SYSTEM_ID
foreign key (PARENT_CODE_SYSTEM_ID)
references CODE_SYSTEM;

alter table VALUE_SET_VERSION
  add constraint FK_PREVIOUS_VERSION_ID
foreign key (PREVIOUS_VERSION_ID)
references VALUE_SET_VERSION;

alter table VALUE_SET_VERSION
  add constraint FK_VALUE_SET_ID
foreign key (VALUE_SET_ID)
references VALUE_SET;

alter table X_CONCEPT_VALUE_SET
  add constraint FK_CODE_SYSTEM_CONCEPT_ID
foreign key (CODE_SYSTEM_CONCEPT_ID)
references CODE_SYSTEM_CONCEPT;

alter table X_CONCEPT_VALUE_SET
  add constraint FK_VALUE_SET_VERSION_ID
foreign key (VALUE_SET_VERSION_ID)
references VALUE_SET_VERSION;