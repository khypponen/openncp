CREATE TABLE code_system (
  id          NUMBER(19) NOT NULL,
  oid         VARCHAR2(255 CHAR),
  name        VARCHAR2(255 CHAR),
  description VARCHAR2(4000 CHAR),
  CONSTRAINT code_system_pk PRIMARY KEY (id)
);

CREATE TABLE code_system_version (
  id                  NUMBER(19) NOT NULL,
  full_name           VARCHAR2(255 CHAR),
  local_name          VARCHAR2(255 CHAR),
  previous_version_id NUMBER(19),
  effective_date      TIMESTAMP,
  release_date        TIMESTAMP,
  status              VARCHAR2(255 CHAR),
  status_date         TIMESTAMP,
  description         VARCHAR2(4000 CHAR),
  copyright           VARCHAR2(255 CHAR),
  source              VARCHAR2(255 CHAR),
  code_system_id      NUMBER(19) NOT NULL,
  CONSTRAINT code_system_version_pk PRIMARY KEY (id),
  CONSTRAINT csv_previous_version_fk FOREIGN KEY (previous_version_id) REFERENCES code_system_version (id),
  CONSTRAINT csv_code_system_fk FOREIGN KEY (code_system_id) REFERENCES code_system (id)
);

CREATE TABLE code_system_concept (
  id                     NUMBER(19) NOT NULL,
  code                   VARCHAR2(255 CHAR),
  definition             VARCHAR2(4000 CHAR),
  status                 VARCHAR2(255 CHAR),
  status_date            TIMESTAMP,
  code_system_version_id NUMBER(19) NOT NULL,
  CONSTRAINT code_system_concept_pk PRIMARY KEY (id),
  CONSTRAINT csc_code_system_version_fk FOREIGN KEY (code_system_version_id) REFERENCES code_system_version (id)
);

CREATE TABLE designation (
  id                     NUMBER(19) NOT NULL,
  designation            VARCHAR2(4000 CHAR),
  language_code          VARCHAR2(255 CHAR),
  type                   VARCHAR2(255 CHAR),
  is_preferred           NUMBER(1),
  status                 VARCHAR2(255 CHAR),
  status_date            TIMESTAMP,
  code_system_concept_id NUMBER(19),
  CONSTRAINT designation_pk PRIMARY KEY (id),
  CONSTRAINT des_code_system_concept_fk FOREIGN KEY (code_system_concept_id) REFERENCES code_system_concept (id)
);

CREATE TABLE transcoding_association (
  id                         NUMBER(19) NOT NULL,
  transcoding_association_id NUMBER(19) NOT NULL,
  target_concept_id          NUMBER(19),
  source_concept_id          NUMBER(19),
  quality                    VARCHAR2(255 CHAR),
  status                     VARCHAR2(255 CHAR),
  status_date                TIMESTAMP,
  CONSTRAINT transcoding_association_pk PRIMARY KEY (id),
  CONSTRAINT trs_target_concept_fk FOREIGN KEY (target_concept_id) REFERENCES code_system_concept (id),
  CONSTRAINT trs_source_concept_fk FOREIGN KEY (source_concept_id) REFERENCES code_system_concept (id)
);

CREATE TABLE value_set (
  id                    NUMBER(19) NOT NULL,
  oid                   VARCHAR2(255 CHAR),
  epsos_name            VARCHAR2(255 CHAR),
  description           VARCHAR2(4000 CHAR),
  parent_code_system_id NUMBER(19),
  CONSTRAINT value_set_pk PRIMARY KEY (id),
  CONSTRAINT vst_code_system_fk FOREIGN KEY (parent_code_system_id) REFERENCES code_system (id)
);

CREATE TABLE value_set_version (
  id                  NUMBER(19) NOT NULL,
  version_name        VARCHAR2(255 CHAR),
  effective_date      TIMESTAMP,
  release_date        TIMESTAMP,
  status              VARCHAR2(255 CHAR),
  status_date         TIMESTAMP,
  description         VARCHAR2(4000 CHAR),
  previous_version_id NUMBER(19),
  value_set_id        NUMBER(19),
  CONSTRAINT value_set_version_pk PRIMARY KEY (id),
  CONSTRAINT vsv_previous_version_fk FOREIGN KEY (previous_version_id) REFERENCES value_set_version (id),
  CONSTRAINT vsv_value_set_fk FOREIGN KEY (value_set_id) REFERENCES value_set (id)

);

CREATE TABLE x_concept_value_set (
  code_system_concept_id NUMBER(19) NOT NULL,
  value_set_version_id   NUMBER(19) NOT NULL,
  CONSTRAINT x_concept_value_set_pk PRIMARY KEY (code_system_concept_id, value_set_version_id),
  CONSTRAINT cvs_code_system_concept_fk FOREIGN KEY (code_system_concept_id) REFERENCES code_system_concept (id),
  CONSTRAINT cvs_value_set_version_fk FOREIGN KEY (value_set_version_id) REFERENCES value_set_version (id)
);

CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1 CACHE 20;