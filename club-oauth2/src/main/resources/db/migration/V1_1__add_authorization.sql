CREATE TABLE auth.authorization (
    id                            varchar(50) NOT NULL,
    registered_client_id          varchar(50) NOT NULL,
    principal_name                varchar(30) NOT NULL,
    authorization_grant_type      varchar(30) NOT NULL,
    state                         varchar(500)  DEFAULT NULL,

    CONSTRAINT pk_auth PRIMARY KEY (id)
);

CREATE TABLE auth.authorization_scopes_set (
    auth_id varchar(50)   NOT NULL,
    scope varchar(30)     NOT NULL,

    CONSTRAINT fk_authorization_scopes_set__authorization
       FOREIGN KEY (auth_id) REFERENCES auth.authorization (id)
);

CREATE TABLE auth.authorization_attributes_map (
    auth_id varchar(50),
    attribute_name varchar(30),
    attribute varchar(100),

    CONSTRAINT fk_authorization_attributes_map__authorization
       FOREIGN KEY (auth_id) REFERENCES auth.authorization (id)
);

CREATE TABLE auth.authorization_scopes_set (
    auth_id varchar(30)   NOT NULL,
    scope varchar(30)       NOT NULL,

    CONSTRAINT fk_authorization_scopes_set__authorization
       FOREIGN KEY (auth_id)  REFERENCES auth.authorization (id)
);

CREATE TABLE auth.authorization_attributes_map (
    auth_id varchar(30) NOT NULL,
    attribute_name varchar (30) NOT NULL,
    attribute varchar(30) DEFAULT NULL,

    CONSTRAINT fk_authorization_attributes_map__authorization
        FOREIGN KEY (auth_id)  REFERENCES auth.authorization (id)
);

CREATE TABLE auth.authorization_token (
    id varchar(50),
    auth_id varchar(50),
    token_type varchar(30) NOT NULL,
    token_value varchar(500) NOT NULL,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    expires_at TIMESTAMP DEFAULT NULL,
    metadata varchar(2000) NOT NULL,
    scopes varchar(1000) DEFAULT NULL,
    claims varchar(1000) DEFAULT NULL,

    CONSTRAINT pk_authorization_token PRIMARY KEY (id, token_type),
    CONSTRAINT fk_authorization_token__authorization
       FOREIGN KEY (auth_id) REFERENCES auth.authorization (id)
);

CREATE TABLE auth.token_metadata_map (
     token_id varchar(30) NOT NULL,
     metadata_name varchar (30) NOT NULL,
     metadata varchar(100) DEFAULT NULL,

     CONSTRAINT fk_token_metadata_map__authorization_token
         FOREIGN KEY (token_id)  REFERENCES auth.authorization_token (id)
);

CREATE TABLE auth.token_scopes_set (
    token_id varchar(50)   NOT NULL,
    scope varchar(30)     NOT NULL,

    CONSTRAINT fk_token_scopes_set__authorization_token
       FOREIGN KEY (token_id) REFERENCES auth.authorization_token (id)
);

CREATE TABLE auth.token_claims_map (
    token_id varchar(30) NOT NULL,
    claim_name varchar (30) NOT NULL,
    claim varchar(100) DEFAULT NULL,

    CONSTRAINT fk_token_claims_map__authorization_token
       FOREIGN KEY (token_id)  REFERENCES auth.authorization_token (id)
);

CREATE TABLE auth.authorization_consent (
    registered_client_id varchar(50)  NOT NULL,
    principal_name       varchar(50)  NOT NULL,

    CONSTRAINT pk_auth_consent
        PRIMARY KEY (registered_client_id, principal_name)
);

CREATE TABLE auth.authorization_consent_authorities_set (
    registered_client_id varchar(50) NOT NULL,
    principal_name varchar(50),
    authority varchar(100),

    CONSTRAINT pk_reg_client_id__principal_name___authorization_consent
        FOREIGN KEY (registered_client_id, principal_name)
            REFERENCES auth.authorization_consent (registered_client_id, principal_name)
)
